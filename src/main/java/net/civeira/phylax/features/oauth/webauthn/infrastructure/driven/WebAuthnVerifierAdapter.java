package net.civeira.phylax.features.oauth.webauthn.infrastructure.driven;

import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.webauthn.domain.exception.WebAuthnException;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnVerifierGateway;

/**
 * Stub adapter for WebAuthn cryptographic verification.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real implementation using a WebAuthn library such as
 * webauthn4j or Yubico java-webauthn-server.
 * </p>
 */
@ApplicationScoped
@Slf4j
public class WebAuthnVerifierAdapter implements WebAuthnVerifierGateway {

  @Override
  public Map<String, Object> verifyRegistration(Map<String, Object> clientCredential,
      String challenge, String origin, String rpId) {
    log.warn("WebAuthnVerifierAdapter is a stub — registration not verified");
    throw WebAuthnException.verificationFailed("stub adapter — not implemented");
  }

  @Override
  public int verifyAssertion(Map<String, Object> clientCredential, String challenge, String origin,
      String rpId, String storedPublicKey, int storedSignCount) {
    log.warn("WebAuthnVerifierAdapter is a stub — assertion not verified");
    throw WebAuthnException.verificationFailed("stub adapter — not implemented");
  }
}
