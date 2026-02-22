package net.civeira.phylax.features.oauth.user.domain.gateway;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import net.civeira.phylax.features.oauth.user.domain.PendingConsent;

public interface ConsentGateway {

  /**
   * Devuelve el primer consentimiento pendiente de aceptación entre los relying parties indicados,
   * o vacío si el usuario ya ha aceptado los términos de todos ellos.
   */
  Optional<PendingConsent> getPendingConsent(String tenant, String username, List<String> audiences,
      Locale locale);

  /**
   * Registra la aceptación de los términos de uso del relying party indicado por parte del usuario.
   */
  void storeAcceptedConsent(String tenant, String username, String relyingParty);
}
