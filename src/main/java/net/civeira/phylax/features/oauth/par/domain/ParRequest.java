package net.civeira.phylax.features.oauth.par.domain;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.Getter;

/**
 * A Pushed Authorization Request (PAR) — RFC 9126.
 *
 * <p>
 * Stores the authorization parameters submitted before the redirect so that the authorization
 * endpoint can retrieve them via a short-lived {@code request_uri}.
 * </p>
 */
@Getter
public final class ParRequest {

  private static final int EXPIRES_IN_SECONDS = 60;

  private final String requestUri;
  private final String tenant;
  private final String clientId;
  private final Map<String, String> params;
  private final OffsetDateTime expiresAt;
  private final OffsetDateTime usedAt;

  private ParRequest(String requestUri, String tenant, String clientId, Map<String, String> params,
      OffsetDateTime expiresAt, OffsetDateTime usedAt) {
    this.requestUri = requestUri;
    this.tenant = tenant;
    this.clientId = clientId;
    this.params = Map.copyOf(params);
    this.expiresAt = expiresAt;
    this.usedAt = usedAt;
  }

  public static ParRequest create(String tenant, String clientId, Map<String, String> params) {
    String token = HexFormat.of().formatHex(UUID.randomUUID().toString().getBytes());
    String requestUri = "urn:ietf:params:oauth:request_uri:" + token;
    OffsetDateTime expiresAt = OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(EXPIRES_IN_SECONDS);
    return new ParRequest(requestUri, tenant, clientId, params, expiresAt, null);
  }

  public static ParRequest reconstitute(String requestUri, String tenant, String clientId,
      Map<String, String> params, OffsetDateTime expiresAt, OffsetDateTime usedAt) {
    return new ParRequest(requestUri, tenant, clientId, params, expiresAt, usedAt);
  }

  public boolean isExpired() {
    return expiresAt.isBefore(OffsetDateTime.now(ZoneOffset.UTC));
  }

  public boolean isUsed() {
    return usedAt != null;
  }

  public Optional<OffsetDateTime> getUsedAt() {
    return Optional.ofNullable(usedAt);
  }

  public int expiresInSeconds() {
    return EXPIRES_IN_SECONDS;
  }
}
