package net.civeira.phylax.features.oauth.magiclink.application.usecase.requestmagiclink;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantFilter;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantReadRepositoryGateway;
import net.civeira.phylax.features.access.user.domain.gateway.UserFilter;
import net.civeira.phylax.features.access.user.domain.gateway.UserReadRepositoryGateway;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientStoreGateway;
import net.civeira.phylax.features.oauth.magiclink.domain.MagicLink;
import net.civeira.phylax.features.oauth.magiclink.domain.gateway.MagicLinkEnabledGateway;
import net.civeira.phylax.features.oauth.magiclink.domain.gateway.MagicLinkGateway;
import net.civeira.phylax.features.oauth.magiclink.domain.gateway.MagicLinkMailGateway;

/**
 * Generates and delivers a magic link for passwordless login.
 *
 * <p>
 * All failures are silent: if the user is not found, the tenant is disabled, or the client is
 * invalid, the method returns without error to avoid user-enumeration.
 * </p>
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class RequestMagicLinkUsecase {

  private static final int EXPIRES_MINUTES = 15;

  private final UserReadRepositoryGateway users;
  private final TenantReadRepositoryGateway tenants;
  private final ClientStoreGateway clients;
  private final MagicLinkGateway gateway;
  private final MagicLinkMailGateway mailer;
  private final MagicLinkEnabledGateway enabled;

  @ConfigProperty(name = "oauth.base-url", defaultValue = "")
  String baseUrl;

  public boolean isEnabled(String tenantName) {
    return enabled.isEnabled(tenantName);
  }

  public void request(String email, String clientId, String redirectUri, String tenantName,
      String scope, String state, String nonce) {
    if (!enabled.isEnabled(tenantName)) {
      return;
    }

    var tenant = tenants.find(TenantFilter.builder().name(tenantName).build());
    if (tenant.isEmpty() || !tenant.get().isEnabled()) {
      return;
    }

    var user = users.find(UserFilter.builder().nameOrEmail(email).tenant(tenant.get()).build());
    if (user.isEmpty() || !user.get().isEnabled()) {
      return;
    }

    if (clients.loadPublic(tenantName, clientId, redirectUri).isEmpty()) {
      return;
    }

    String tenantUid = tenant.get().getUid();
    String userUid = user.get().getUid();

    byte[] rawBytes = new byte[32];
    new SecureRandom().nextBytes(rawBytes);
    String rawToken = HexFormat.of().formatHex(rawBytes);
    String tokenHash = sha256(rawToken);
    OffsetDateTime expiresAt = OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(EXPIRES_MINUTES);

    MagicLink magicLink = MagicLink.builder().uid(UUID.randomUUID().toString()).tenantId(tenantName)
        .userUid(userUid).clientId(clientId).tokenHash(tokenHash).redirectUri(redirectUri)
        .scope(scope).state(state).nonce(nonce != null ? nonce : "")
        .createdAt(OffsetDateTime.now(ZoneOffset.UTC)).expiresAt(expiresAt).build();

    gateway.store(magicLink);

    String effectiveBaseUrl = resolveBaseUrl(redirectUri);
    String verifyUrl = effectiveBaseUrl.replaceAll("/+$", "") + "/oauth/openid/"
        + java.net.URLEncoder.encode(tenantName, java.nio.charset.StandardCharsets.UTF_8)
        + "/magic-link/verify?token="
        + java.net.URLEncoder.encode(rawToken, java.nio.charset.StandardCharsets.UTF_8)
        + "&client_id="
        + java.net.URLEncoder.encode(clientId, java.nio.charset.StandardCharsets.UTF_8);

    mailer.sendMagicLink(email, user.get().getName(), new TenantRef() {
      @Override
      public String getUid() {
        return tenantUid;
      }
    }, verifyUrl, expiresAt);
  }

  private static String sha256(String value) {
    try {
      byte[] hash = MessageDigest.getInstance("SHA-256")
          .digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 not available", e);
    }
  }

  private String resolveBaseUrl(String redirectUri) {
    if (baseUrl != null && !baseUrl.isBlank()) {
      return baseUrl;
    }
    try {
      URI uri = URI.create(redirectUri);
      if (uri.getScheme() != null && uri.getAuthority() != null) {
        return uri.getScheme() + "://" + uri.getAuthority();
      }
    } catch (RuntimeException ignored) {
      log.debug("Unable to derive oauth.base-url from redirect_uri");
    }
    return "http://localhost";
  }
}
