package net.civeira.phylax.features.oauth.client.domain.gateway;

import java.util.Optional;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;

/**
 * Domain port for loading OAuth client data.
 */
public interface ClientStoreGateway {

  /** Loads a client that was already pre-authorized (e.g. from an authorization_code flow). */
  Optional<ClientDetails> loadPreautorized(String tenant, String clientId);

  /** Loads a public client validating redirect URL (used for authorization_endpoint flows). */
  Optional<ClientDetails> loadPublic(String tenant, String clientId, String redirect);

  /** Loads a confidential client validating the client_secret credential. */
  Optional<ClientDetails> loadPrivate(String tenant, String clientId, String clientSecret);
}
