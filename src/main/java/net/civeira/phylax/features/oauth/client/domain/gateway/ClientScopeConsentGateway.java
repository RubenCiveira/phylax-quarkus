package net.civeira.phylax.features.oauth.client.domain.gateway;

import java.util.List;

/**
 * Port for checking and storing user authorizations for OAuth client scope requests. Distinct from
 * {@code ConsentGateway} which handles tenant-level usage policy acceptance.
 */
public interface ClientScopeConsentGateway {

  /**
   * Returns the list of scopes requested by the client that the user has not yet authorized.
   * Returns an empty list if all scopes are already authorized.
   */
  List<String> getPendingScopes(String tenant, String username, String clientId,
      List<String> requestedScopes);

  /** Stores the user's authorization for the given scopes on the given client. */
  void storeAcceptedScopes(String tenant, String username, String clientId, List<String> scopes);
}
