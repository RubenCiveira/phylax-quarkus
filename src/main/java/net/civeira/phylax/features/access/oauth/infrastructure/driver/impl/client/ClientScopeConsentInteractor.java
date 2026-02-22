package net.civeira.phylax.features.access.oauth.infrastructure.driver.impl.client;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientScopeConsentGateway;

/**
 * Interactor for OAuth client scope authorization. Returns all requested scopes as pending until a
 * real persistence layer is integrated.
 *
 * TODO: integrate with TrustedClient / UserScopeConsent persistence.
 */
@Transactional
@ApplicationScoped
public class ClientScopeConsentInteractor implements ClientScopeConsentGateway {

  @Override
  public List<String> getPendingScopes(String tenant, String username, String clientId,
      List<String> requestedScopes) {
    return requestedScopes;
  }

  @Override
  public void storeAcceptedScopes(String tenant, String username, String clientId,
      List<String> scopes) {
    // TODO: persist accepted scopes
  }
}
