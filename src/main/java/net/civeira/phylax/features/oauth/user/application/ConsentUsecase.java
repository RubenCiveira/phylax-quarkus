package net.civeira.phylax.features.oauth.user.application;

import java.util.Locale;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.user.domain.gateway.ConsentGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class ConsentUsecase {

  private final ConsentGateway gateway;

  public Optional<String> getPendingConsent(String tenant, String username, Locale locale) {
    return gateway.getPendingConsent(tenant, username, locale);
  }

  public void storeAcceptedConsent(String tenant, String username) {
    gateway.storeAcceptedConsent(tenant, username);
  }
}
