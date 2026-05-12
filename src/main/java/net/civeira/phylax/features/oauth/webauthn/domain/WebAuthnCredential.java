package net.civeira.phylax.features.oauth.webauthn.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class WebAuthnCredential {

  private final String uid;
  private final String tenantId;
  private final String userUid;
  private final String username;
  private final String credentialId;
  private final String publicKey;
  private final int signCount;
  private final String aaguid;
  private final String deviceName;
  private final List<String> transports;
  private final OffsetDateTime createdAt;
  private final OffsetDateTime lastUsedAt;
  private final boolean enabled;

  public Optional<String> getAaguid() {
    return Optional.ofNullable(aaguid);
  }

  public Optional<String> getDeviceName() {
    return Optional.ofNullable(deviceName);
  }

  public Optional<List<String>> getTransports() {
    return Optional.ofNullable(transports);
  }

  public Optional<OffsetDateTime> getLastUsedAt() {
    return Optional.ofNullable(lastUsedAt);
  }

  public WebAuthnCredential withUpdatedSignCount(int newSignCount, OffsetDateTime newLastUsedAt) {
    return WebAuthnCredential.builder().uid(this.uid).tenantId(this.tenantId).userUid(this.userUid)
        .username(this.username).credentialId(this.credentialId).publicKey(this.publicKey)
        .signCount(newSignCount).aaguid(this.aaguid).deviceName(this.deviceName)
        .transports(this.transports).createdAt(this.createdAt).lastUsedAt(newLastUsedAt)
        .enabled(this.enabled).build();
  }
}
