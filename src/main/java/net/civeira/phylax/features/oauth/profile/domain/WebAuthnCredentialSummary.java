package net.civeira.phylax.features.oauth.profile.domain;

import java.time.OffsetDateTime;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class WebAuthnCredentialSummary {

  private final String id;
  private final String name;
  private final String aaguid;
  private final OffsetDateTime createdAt;
  private final OffsetDateTime lastUsedAt;

  public Optional<String> getName() {
    return Optional.ofNullable(name);
  }

  public Optional<String> getAaguid() {
    return Optional.ofNullable(aaguid);
  }

  public Optional<OffsetDateTime> getLastUsedAt() {
    return Optional.ofNullable(lastUsedAt);
  }
}
