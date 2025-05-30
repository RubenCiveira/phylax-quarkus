/* @autogenerated */
package net.civeira.phylax.features.access.oauth.infrastructure.driver.impl.client;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.oauth.application.usecase.VerifyTrustedClientUsecase;
import net.civeira.phylax.features.oauth.client.application.spi.ClientRetrieveSpi;
import net.civeira.phylax.features.oauth.client.domain.model.ClientDetails;

@Transactional
@ApplicationScoped
@RequiredArgsConstructor
public class ClienteRetrieveInteractor implements ClientRetrieveSpi {
  private final VerifyTrustedClientUsecase verifier;

  @Override
  public Optional<ClientDetails> loadPrivate(String tenant, String clientId, String clientSecret) {
    return verifier.loadPrivate(tenant, clientId, clientSecret);
  }

  @Override
  public Optional<ClientDetails> loadPublic(String tenant, String clientId, String redirect) {
    return verifier.loadPublic(tenant, clientId, redirect);
  }
}
