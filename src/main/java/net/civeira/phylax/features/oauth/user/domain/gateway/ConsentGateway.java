package net.civeira.phylax.features.oauth.user.domain.gateway;

import java.util.Locale;
import java.util.Optional;

public interface ConsentGateway {

  /** Devuelve el texto de los términos pendientes de aceptación, o vacío si no hay pendientes. */
  Optional<String> getPendingConsent(String tenant, String username, Locale locale);

  /** Registra la aceptación de los términos de uso por parte del usuario. */
  void storeAcceptedConsent(String tenant, String username);
}
