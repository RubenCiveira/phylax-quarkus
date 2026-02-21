package net.civeira.phylax.features.oauth.client.infrastructure.driven;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.client.application.spi.ClientRetrieveSpi;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientStoreGateway;
import net.civeira.phylax.features.oauth.client.domain.model.ClientDetails;

/**
 * Bridge adapter that implements {@link ClientStoreGateway} by delegating to the legacy
 * {@link ClientRetrieveSpi}. Allows gradual migration without breaking existing SPI implementors.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class ClientStoreAdapter implements ClientStoreGateway {

  private final ClientRetrieveSpi spi;

  @Override
  public Optional<ClientDetails> loadPreautorized(String tenant, String clientId) {
    return spi.loadPreautorized(tenant, clientId);
  }

  @Override
  public Optional<ClientDetails> loadPublic(String tenant, String clientId, String redirect) {
    return spi.loadPublic(tenant, clientId, redirect);
  }

  @Override
  public Optional<ClientDetails> loadPrivate(String tenant, String clientId, String clientSecret) {
    return spi.loadPrivate(tenant, clientId, clientSecret);
  }
}
