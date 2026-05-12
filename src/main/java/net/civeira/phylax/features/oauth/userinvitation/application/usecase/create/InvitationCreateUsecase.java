package net.civeira.phylax.features.oauth.userinvitation.application.usecase.create;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.tenant.domain.TenantReference;
import net.civeira.phylax.features.access.user.domain.UserReference;
import net.civeira.phylax.features.access.userinvitation.domain.UserInvitation;
import net.civeira.phylax.features.access.userinvitation.domain.UserInvitationChangeSet;
import net.civeira.phylax.features.access.userinvitation.domain.UserInvitationStatusOptions;
import net.civeira.phylax.features.access.userinvitation.domain.gateway.UserInvitationWriteRepositoryGateway;
import net.civeira.phylax.features.oauth.userinvitation.domain.gateway.UserInvitationMailGateway;
import net.civeira.phylax.features.oauth.userinvitation.domain.gateway.UserInvitationQueryGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class InvitationCreateUsecase {

  private static final int EXPIRES_DAYS = 7;

  private final UserInvitationWriteRepositoryGateway writeGateway;
  private final UserInvitationQueryGateway queryGateway;
  private final UserInvitationMailGateway mailer;

  public InvitationCreateResult create(InvitationCreateParams params) {
    queryGateway.findPendingByEmail(params.getEmail(), params.getTenantId()).ifPresent(existing -> {
      var exp = existing.getExpiredAt();
      if (exp.isAfter(OffsetDateTime.now(ZoneOffset.UTC))) {
        throw new IllegalStateException("There is already a pending invitation for this email.");
      }
    });

    byte[] rawBytes = new byte[32];
    new SecureRandom().nextBytes(rawBytes);
    String rawToken = HexFormat.of().formatHex(rawBytes);
    String tokenHash = sha256(rawToken);
    OffsetDateTime expiresAt = OffsetDateTime.now(ZoneOffset.UTC).plusDays(EXPIRES_DAYS);

    UserInvitationChangeSet change = new UserInvitationChangeSet();
    change.uid(UUID.randomUUID().toString());
    change.tenant(TenantReference.of(params.getTenantId()));
    change.email(params.getEmail());
    change.invitedBy(UserReference.of(params.getInvitedByUserUid()));
    change.tokenHash(tokenHash);
    change.status(UserInvitationStatusOptions.PENDING);
    change.createdAt(OffsetDateTime.now(ZoneOffset.UTC));
    change.expiredAt(expiresAt);
    if (params.getRoles() != null) {
      change.roles(params.getRoles());
    }
    if (params.getMetadataJson() != null) {
      change.metadataJson(params.getMetadataJson());
    }

    UserInvitation invitation = writeGateway.create(UserInvitation.create(change));

    String acceptUrl = buildAcceptUrl(params, rawToken);

    mailer.sendInvitation(params.getEmail(), TenantReference.of(params.getTenantId()), acceptUrl,
        params.getInvitedByEmail(), expiresAt);

    return new InvitationCreateResult(invitation.getUid(), expiresAt);
  }

  private static String buildAcceptUrl(InvitationCreateParams params, String rawToken) {
    StringBuilder url =
        new StringBuilder(params.getBaseUrl().replaceAll("/+$", "") + "/oauth/openid/"
            + java.net.URLEncoder.encode(params.getTenantName(),
                java.nio.charset.StandardCharsets.UTF_8)
            + "/invitation/accept?token="
            + java.net.URLEncoder.encode(rawToken, java.nio.charset.StandardCharsets.UTF_8));
    if (!params.getClientId().isBlank()) {
      url.append("&client_id=").append(java.net.URLEncoder.encode(params.getClientId(),
          java.nio.charset.StandardCharsets.UTF_8));
    }
    if (!params.getRedirectUri().isBlank()) {
      url.append("&redirect_uri=").append(java.net.URLEncoder.encode(params.getRedirectUri(),
          java.nio.charset.StandardCharsets.UTF_8));
    }
    return url.toString();
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
}
