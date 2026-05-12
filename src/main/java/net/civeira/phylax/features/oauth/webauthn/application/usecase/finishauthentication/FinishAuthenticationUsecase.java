package net.civeira.phylax.features.oauth.webauthn.application.usecase.finishauthentication;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantFilter;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantReadRepositoryGateway;
import net.civeira.phylax.features.oauth.webauthn.domain.exception.WebAuthnException;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnChallengeGateway;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnCredentialGateway;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnVerifierGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class FinishAuthenticationUsecase {

  private final TenantReadRepositoryGateway tenants;
  private final WebAuthnChallengeGateway challengeGateway;
  private final WebAuthnCredentialGateway credentialGateway;
  private final WebAuthnVerifierGateway verifier;

  public FinishAuthenticationResult finish(String challengeId, String tenantName,
      Map<String, Object> clientCredential, String origin, String rpId) {
    var tenant =
        tenants.find(TenantFilter.builder().name(tenantName).build()).filter(t -> t.isEnabled())
            .orElseThrow(() -> new IllegalArgumentException("tenant " + tenantName));

    String tenantId = tenant.getUid();

    var challenge = challengeGateway.findById(challengeId, tenantId)
        .orElseThrow(WebAuthnException::challengeNotFound);

    if (challenge.isExpired()) {
      throw WebAuthnException.challengeExpired();
    }
    if (!"authenticate".equals(challenge.getType())) {
      throw WebAuthnException.verificationFailed("wrong challenge type");
    }

    String credentialId = (String) clientCredential.get("id");
    var storedCredential = credentialGateway.findByCredentialId(credentialId, tenantId)
        .orElseThrow(WebAuthnException::credentialNotFound);

    if (!storedCredential.isEnabled()) {
      throw WebAuthnException.credentialDisabled();
    }

    int newSignCount = verifier.verifyAssertion(clientCredential, challenge.getChallenge(), origin,
        rpId, storedCredential.getPublicKey(), storedCredential.getSignCount());

    OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
    credentialGateway.updateSignCount(credentialId, tenantId, newSignCount, now);

    var verified =
        challenge.markVerified(storedCredential.getUserUid(), storedCredential.getUsername());
    challengeGateway.markVerified(verified);

    return new FinishAuthenticationResult(storedCredential.getUserUid(),
        storedCredential.getUsername());
  }
}
