package net.civeira.phylax.features.oauth.client.application;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientScopeConsentGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class ClientScopeConsentUsecase {

  private final ClientScopeConsentGateway gateway;

  public List<String> getPendingScopes(String tenant, String username, String clientId,
      List<String> requestedScopes) {
    return gateway.getPendingScopes(tenant, username, clientId, requestedScopes);
  }

  public void storeAcceptedScopes(String tenant, String username, String clientId,
      List<String> scopes) {
    gateway.storeAcceptedScopes(tenant, username, clientId, scopes);
  }
}
