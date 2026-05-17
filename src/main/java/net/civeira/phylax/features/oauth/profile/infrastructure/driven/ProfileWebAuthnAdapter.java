package net.civeira.phylax.features.oauth.profile.infrastructure.driven;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.profile.domain.WebAuthnCredentialSummary;
import net.civeira.phylax.features.oauth.profile.domain.gateway.ProfileWebAuthnGateway;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnCredentialGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class ProfileWebAuthnAdapter implements ProfileWebAuthnGateway {

  private final WebAuthnCredentialGateway credentialGateway;

  @Override
  public List<WebAuthnCredentialSummary> listCredentials(String userUid, String tenantId) {
    return credentialGateway.findByUser(userUid, tenantId).stream()
        .map(c -> WebAuthnCredentialSummary.builder().id(c.getUid())
            .name(c.getDeviceName().orElse(null)).aaguid(c.getAaguid().orElse(null))
            .createdAt(c.getCreatedAt()).lastUsedAt(c.getLastUsedAt().orElse(null)).build())
        .toList();
  }

  @Override
  public void renameCredential(String credentialId, String userUid, String tenantId,
      String newName) {
    credentialGateway.rename(credentialId, userUid, tenantId, newName);
  }

  @Override
  public void deleteCredential(String credentialId, String userUid, String tenantId) {
    credentialGateway.delete(credentialId, userUid, tenantId);
  }
}
