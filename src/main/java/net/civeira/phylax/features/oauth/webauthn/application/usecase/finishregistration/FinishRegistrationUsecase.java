package net.civeira.phylax.features.oauth.webauthn.application.usecase.finishregistration;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantFilter;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantReadRepositoryGateway;
import net.civeira.phylax.features.access.user.domain.gateway.UserFilter;
import net.civeira.phylax.features.access.user.domain.gateway.UserReadRepositoryGateway;
import net.civeira.phylax.features.oauth.webauthn.domain.WebAuthnCredential;
import net.civeira.phylax.features.oauth.webauthn.domain.exception.WebAuthnException;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnChallengeGateway;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnCredentialGateway;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnVerifierGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class FinishRegistrationUsecase {

  private final TenantReadRepositoryGateway tenants;
  private final UserReadRepositoryGateway users;
  private final WebAuthnChallengeGateway challengeGateway;
  private final WebAuthnCredentialGateway credentialGateway;
  private final WebAuthnVerifierGateway verifier;

  public void finish(String challengeId, String tenantName, Map<String, Object> clientCredential,
      String origin, String rpId, String deviceName) {
    var tenant =
        tenants.find(TenantFilter.builder().name(tenantName).build()).filter(t -> t.isEnabled())
            .orElseThrow(() -> new IllegalArgumentException("tenant " + tenantName));

    String tenantId = tenant.getUid();

    var challenge = challengeGateway.findById(challengeId, tenantId)
        .orElseThrow(WebAuthnException::challengeNotFound);

    if (challenge.isExpired()) {
      throw WebAuthnException.challengeExpired();
    }
    if (!"register".equals(challenge.getType())) {
      throw WebAuthnException.verificationFailed("wrong challenge type");
    }

    String userUid = challenge.getUserUid()
        .orElseThrow(() -> WebAuthnException.verificationFailed("no user in challenge"));

    var user = users.find(UserFilter.builder().tenant(tenant).uid(userUid).build())
        .orElseThrow(() -> new IllegalArgumentException("user " + userUid));

    Map<String, Object> result =
        verifier.verifyRegistration(clientCredential, challenge.getChallenge(), origin, rpId);

    String loginName = user.getEmail().orElseGet(() -> user.getName());

    @SuppressWarnings("unchecked")
    var transports = result.get("transports") instanceof java.util.List<?>
        ? (java.util.List<String>) result.get("transports")
        : null;

    credentialGateway
        .store(WebAuthnCredential.builder().uid(UUID.randomUUID().toString()).tenantId(tenantId)
            .userUid(userUid).username(loginName).credentialId((String) result.get("credentialId"))
            .publicKey((String) result.get("publicKey"))
            .signCount(((Number) result.get("signCount")).intValue())
            .aaguid((String) result.get("aaguid")).deviceName(deviceName).transports(transports)
            .createdAt(OffsetDateTime.now(ZoneOffset.UTC)).enabled(true).build());
  }
}
