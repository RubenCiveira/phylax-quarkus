package net.civeira.phylax.features.oauth.authentication.application;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.auth0.jwt.JWT;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClient;
import net.civeira.phylax.features.access.trustedclient.domain.gateway.TrustedClientFilter;
import net.civeira.phylax.features.access.trustedclient.domain.gateway.TrustedClientReadRepositoryGateway;
import net.civeira.phylax.features.oauth.tokensecurity.domain.gateway.TokenSigner;

/**
 * Dispatches OIDC Back-Channel Logout notifications (server-to-server) to all registered Relying
 * Parties that have a {@code backchannel_logout_uri} configured.
 *
 * <p>
 * Per OIDC Back-Channel Logout spec, delivery is best-effort: a failure to notify one RP MUST NOT
 * prevent the user's session from being terminated at the IdP.
 * </p>
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class BackChannelLogoutDispatcher {

  private static final String LOGOUT_TOKEN_SCOPE = "backchannel_logout";
  private static final Duration HTTP_TIMEOUT = Duration.ofSeconds(5);

  private final TrustedClientReadRepositoryGateway clients;
  private final TokenSigner signer;

  private final HttpClient http = HttpClient.newBuilder().connectTimeout(HTTP_TIMEOUT).build();

  /**
   * Sends a {@code logout_token} JWT to every enabled RP that has a {@code backchannel_logout_uri}.
   *
   * @param tenant tenant identifier used to sign tokens
   * @param issuer token {@code iss} claim
   * @param userSub subject being logged out ({@code sub} claim)
   * @param sessionId session being terminated ({@code sid} claim)
   */
  public void dispatch(String tenant, String issuer, String userSub, String sessionId) {
    if (userSub == null || userSub.isBlank() || sessionId == null || sessionId.isBlank()) {
      return;
    }

    List<TrustedClient> allClients =
        clients.list(TrustedClientFilter.builder().withBackChannelUrl(true).build());

    for (TrustedClient client : allClients) {
      client.getBackChannelLogoutUri().filter(uri -> !uri.isBlank()).ifPresent(
          uri -> sendLogoutToken(tenant, issuer, userSub, sessionId, client.getCode(), uri));
    }
  }

  private void sendLogoutToken(String tenant, String issuer, String userSub, String sessionId,
      String clientId, String uri) {
    try {
      String logoutToken = buildLogoutToken(tenant, issuer, userSub, sessionId, clientId);
      String body = "logout_token=" + logoutToken;

      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).timeout(HTTP_TIMEOUT)
          .header("Content-Type", "application/x-www-form-urlencoded")
          .POST(HttpRequest.BodyPublishers.ofString(body)).build();

      http.sendAsync(request, HttpResponse.BodyHandlers.discarding()).exceptionally(ex -> {
        log.warn("Back-channel logout notification failed for client={} uri={}: {}", clientId, uri,
            ex.getMessage());
        return null;
      });
    } catch (Exception ex) {
      // Best-effort per spec — log but never propagate
      log.warn("Back-channel logout dispatch error for client={} uri={}: {}", clientId, uri,
          ex.getMessage());
    }
  }

  private String buildLogoutToken(String tenant, String issuer, String userSub, String sessionId,
      String clientId) {
    Instant expiry = Instant.now().plus(Duration.ofMinutes(2));
    return signer.sign(tenant,
        JWT.create().withIssuer(issuer).withIssuedAt(new Date())
            .withJWTId(UUID.randomUUID().toString()).withSubject(userSub).withAudience(clientId)
            .withClaim("sid", sessionId)
            .withClaim("events",
                java.util.Map.of("http://schemas.openid.net/event/backchannel-logout",
                    java.util.Map.of()))
            .withArrayClaim("scope", new String[] {LOGOUT_TOKEN_SCOPE}),
        expiry);
  }
}
