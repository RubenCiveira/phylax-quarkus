package net.civeira.phylax.features.oauth.webauthn.application.usecase.beginauthentication;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantFilter;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantReadRepositoryGateway;
import net.civeira.phylax.features.oauth.webauthn.domain.WebAuthnChallenge;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnChallengeGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class BeginAuthenticationUsecase {

  private final TenantReadRepositoryGateway tenants;
  private final WebAuthnChallengeGateway challengeGateway;

  public Map<String, Object> begin(String tenantName, String rpId) {
    var tenant =
        tenants.find(TenantFilter.builder().name(tenantName).build()).filter(t -> t.isEnabled())
            .orElseThrow(() -> new IllegalArgumentException("tenant " + tenantName));

    String tenantId = tenant.getUid();

    byte[] challengeBytes = new byte[32];
    new SecureRandom().nextBytes(challengeBytes);
    String challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(challengeBytes);
    String challengeId = UUID.randomUUID().toString();

    challengeGateway.store(WebAuthnChallenge.builder().challengeId(challengeId).tenantId(tenantId)
        .challenge(challenge).type("authenticate").createdAt(OffsetDateTime.now(ZoneOffset.UTC))
        .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(5)).verified(false).build());

    return Map.of("challengeId", challengeId, "challenge", challenge, "timeout", 60000, "rpId",
        rpId, "userVerification", "preferred", "allowCredentials", List.of());
  }
}
