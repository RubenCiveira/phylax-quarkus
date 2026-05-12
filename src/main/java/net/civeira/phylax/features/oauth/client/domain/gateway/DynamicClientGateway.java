package net.civeira.phylax.features.oauth.client.domain.gateway;

import java.util.Optional;

import net.civeira.phylax.features.oauth.client.domain.DynamicClientData;
import net.civeira.phylax.features.oauth.client.domain.DynamicClientRequest;

/**
 * Domain port for Dynamic Client Registration storage (RFC 7591 / RFC 7592).
 */
public interface DynamicClientGateway {

  /**
   * Persists a new dynamic client and returns stored metadata.
   *
   * @param clientId generated client_id
   * @param clientSecret generated client_secret
   * @param registrationAccessToken token for subsequent management requests
   * @param request client metadata provided during registration
   * @return persisted client data
   */
  DynamicClientData create(String clientId, String clientSecret, String registrationAccessToken,
      DynamicClientRequest request);

  /**
   * Retrieves a dynamic client by client_id and registration access token.
   *
   * @param clientId client_id
   * @param registrationAccessToken management token
   * @return client data, or empty when not found or unauthorized
   */
  Optional<DynamicClientData> findByClientIdAndToken(String clientId,
      String registrationAccessToken);

  /**
   * Replaces a dynamic client's metadata (RFC 7592 PUT).
   *
   * @param clientId client_id
   * @param registrationAccessToken management token
   * @param request new metadata
   * @return updated client data, or empty when not found or unauthorized
   */
  Optional<DynamicClientData> update(String clientId, String registrationAccessToken,
      DynamicClientRequest request);

  /**
   * Deletes a dynamic client (RFC 7592 DELETE).
   *
   * @param clientId client_id
   * @param registrationAccessToken management token
   * @return true when deleted, false when not found or unauthorized
   */
  boolean delete(String clientId, String registrationAccessToken);

  /**
   * Returns the registration policy for the given tenant.
   *
   * <p>
   * Known values: {@code open} (anyone may register), {@code token-required} (initial access token
   * needed), {@code disabled} (registration not allowed).
   * </p>
   *
   * @param tenant tenant identifier
   * @return policy string
   */
  String readRegistrationPolicy(String tenant);
}
