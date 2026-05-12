package net.civeira.phylax.features.oauth.webauthn.domain;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class WebAuthnChallenge {

  private final String challengeId;
  private final String tenantId;
  private final String userUid;
  private final String challenge;
  private final String type;
  private final String optionsJson;
  private final OffsetDateTime createdAt;
  private final OffsetDateTime expiresAt;
  private final boolean verified;
  private final OffsetDateTime verifiedAt;
  private final String verifiedUserUid;
  private final String verifiedUsername;

  public boolean isExpired() {
    return expiresAt.isBefore(OffsetDateTime.now(ZoneOffset.UTC));
  }

  public Optional<String> getUserUid() {
    return Optional.ofNullable(userUid);
  }

  public Optional<String> getOptionsJson() {
    return Optional.ofNullable(optionsJson);
  }

  public Optional<OffsetDateTime> getVerifiedAt() {
    return Optional.ofNullable(verifiedAt);
  }

  public Optional<String> getVerifiedUserUid() {
    return Optional.ofNullable(verifiedUserUid);
  }

  public Optional<String> getVerifiedUsername() {
    return Optional.ofNullable(verifiedUsername);
  }

  public WebAuthnChallenge markVerified(String resolvedUserUid, String username) {
    return WebAuthnChallenge.builder().challengeId(this.challengeId).tenantId(this.tenantId)
        .userUid(this.userUid).challenge(this.challenge).type(this.type)
        .optionsJson(this.optionsJson).createdAt(this.createdAt).expiresAt(this.expiresAt)
        .verified(true).verifiedAt(OffsetDateTime.now(ZoneOffset.UTC))
        .verifiedUserUid(resolvedUserUid).verifiedUsername(username).build();
  }
}
