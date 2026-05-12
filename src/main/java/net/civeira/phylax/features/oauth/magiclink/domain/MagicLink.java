package net.civeira.phylax.features.oauth.magiclink.domain;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;

/**
 * A Magic Link token record — one-time, short-lived, tied to a specific user and OAuth client.
 */
@Builder
@Getter
public final class MagicLink {

  private final String uid;
  private final String tenantId;
  private final String userUid;
  private final String clientId;
  private final String tokenHash;
  private final String redirectUri;
  private final String scope;
  private final String state;
  private final String nonce;
  private final OffsetDateTime createdAt;
  private final OffsetDateTime expiresAt;
  private final OffsetDateTime usedAt;

  public boolean isExpired() {
    return expiresAt.isBefore(OffsetDateTime.now(ZoneOffset.UTC));
  }

  public boolean isUsed() {
    return usedAt != null;
  }

  public boolean isValid() {
    return !isExpired() && !isUsed();
  }

  public Optional<String> getState() {
    return Optional.ofNullable(state);
  }

  public Optional<OffsetDateTime> getUsedAt() {
    return Optional.ofNullable(usedAt);
  }
}
