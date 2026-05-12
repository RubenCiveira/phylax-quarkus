package net.civeira.phylax.features.oauth.webauthn.domain.gateway;

import java.util.Optional;

import net.civeira.phylax.features.oauth.webauthn.domain.WebAuthnChallenge;

public interface WebAuthnChallengeGateway {

  void store(WebAuthnChallenge challenge);

  Optional<WebAuthnChallenge> findById(String challengeId, String tenantId);

  void markVerified(WebAuthnChallenge challenge);

  void purgeExpired();
}
