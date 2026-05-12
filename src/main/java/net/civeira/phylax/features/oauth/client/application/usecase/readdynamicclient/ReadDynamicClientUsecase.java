package net.civeira.phylax.features.oauth.client.application.usecase.readdynamicclient;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.client.domain.DynamicClientData;
import net.civeira.phylax.features.oauth.client.domain.gateway.DynamicClientGateway;

/**
 * Retrieves a registered dynamic client by client_id and registration access token (RFC 7592 GET).
 */
@ApplicationScoped
@RequiredArgsConstructor
public class ReadDynamicClientUsecase {

  private final DynamicClientGateway gateway;

  /**
   * Returns client metadata for the given credentials.
   *
   * @param clientId client_id
   * @param registrationAccessToken registration management token
   * @return client data, or empty when not found or unauthorized
   */
  public Optional<DynamicClientData> read(String clientId, String registrationAccessToken) {
    return gateway.findByClientIdAndToken(clientId, registrationAccessToken);
  }
}
