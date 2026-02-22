package net.civeira.phylax.features.oauth.scopes.domain.gateway;

import java.util.List;

import net.civeira.phylax.features.oauth.scopes.domain.ScopePermission;

public interface ScopesConsentGateway {

  List<ScopePermission> pendingScopes(String tenant, String username, String clientId,
      List<String> scopes);

  void storeAcceptedScopes(String tenant, String username, String clientId, List<String> scopes);
}
