package net.civeira.phylax.features.oauth.user.infrastructure.driven;

import java.util.Locale;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.application.spi.UserConsentSpi;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;
import net.civeira.phylax.features.oauth.user.domain.gateway.ConsentGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class ConsentAdapter implements ConsentGateway {

  private final UserConsentSpi consentSpi;

  @Override
  public Optional<String> getPendingConsent(String tenant, String username, Locale locale) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).locale(locale).build();
    return consentSpi.userRequiredConsent(locale, request, username).filter(c -> c.isPending())
        .map(c -> c.getFullText());
  }

  @Override
  public void storeAcceptedConsent(String tenant, String username) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).build();
    consentSpi.confirmConsent(request, username, null);
  }
}
