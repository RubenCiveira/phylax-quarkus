package net.civeira.phylax.features.oauth.webauthn.infrastructure.driven;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.inject.Vetoed;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.webauthn.domain.WebAuthnCredential;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnCredentialGateway;

/**
 * Stub adapter for WebAuthn credential persistence.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real database-backed implementation.
 * </p>
 */
@Vetoed
@Slf4j
public class WebAuthnCredentialAdapter implements WebAuthnCredentialGateway {

  @Override
  public void store(WebAuthnCredential credential) {
    log.warn("WebAuthnCredentialAdapter is a stub — credential {} not persisted",
        credential.getCredentialId());
  }

  @Override
  public Optional<WebAuthnCredential> findByCredentialId(String credentialId, String tenantId) {
    return Optional.empty();
  }

  @Override
  public List<WebAuthnCredential> findByUser(String userUid, String tenantId) {
    return List.of();
  }

  @Override
  public void updateSignCount(String credentialId, String tenantId, int signCount,
      OffsetDateTime lastUsedAt) {
    log.warn("WebAuthnCredentialAdapter is a stub — sign count not updated for {}", credentialId);
  }
}
