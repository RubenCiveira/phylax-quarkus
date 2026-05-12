package net.civeira.phylax.features.oauth.client.application.usecase.updatedynamicclient;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.client.domain.DynamicClientData;
import net.civeira.phylax.features.oauth.client.domain.DynamicClientRequest;
import net.civeira.phylax.features.oauth.client.domain.gateway.DynamicClientGateway;

/**
 * Replaces a registered dynamic client's metadata (RFC 7592 PUT).
 */
@ApplicationScoped
@RequiredArgsConstructor
public class UpdateDynamicClientUsecase {

  private final DynamicClientGateway gateway;

  /**
   * Updates client metadata.
   *
   * @param clientId client_id
   * @param registrationAccessToken registration management token
   * @param request new metadata
   * @return updated client data, or empty when not found or unauthorized
   */
  public Optional<DynamicClientData> update(String clientId, String registrationAccessToken,
      DynamicClientRequest request) {
    return gateway.update(clientId, registrationAccessToken, request);
  }
}
