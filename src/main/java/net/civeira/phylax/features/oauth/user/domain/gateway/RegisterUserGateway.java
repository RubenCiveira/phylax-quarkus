package net.civeira.phylax.features.oauth.user.domain.gateway;

import java.util.Optional;

public interface RegisterUserGateway {

  /** Indica si el tenant permite el auto-registro de usuarios. */
  boolean allowRegister(String tenant);

  /** Devuelve el texto de los términos de uso a aceptar en el registro, si los hay. */
  Optional<String> getRegisterConsent(String tenant);

  /**
   * Inicia el proceso de registro: crea el usuario en estado UNVERIFIED y envía un email con el
   * código de verificación. El parámetro {@code urlBase} se usa para construir el enlace de
   * verificación.
   */
  void requestForRegister(String urlBase, String tenant, String email, String password);

  /**
   * Verifica el código de registro. Devuelve el username si el código es válido y el usuario queda
   * activado, vacío en caso contrario.
   */
  Optional<String> verifyRegister(String tenant, String code);
}
