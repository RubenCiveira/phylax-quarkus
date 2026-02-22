package net.civeira.phylax.features.oauth.user.domain.gateway;

import java.util.Optional;

import net.civeira.phylax.features.oauth.user.domain.RegistrationResult;

public interface RegisterUserGateway {

  /** Indica si el tenant permite el auto-registro de usuarios. */
  boolean allowRegister(String tenant);

  /** Devuelve el texto de los términos de uso a aceptar en el registro, si los hay. */
  Optional<String> getRegisterConsent(String tenant);

  /**
   * Inicia el proceso de registro. Devuelve {@link RegistrationResult#ok} si el usuario queda
   * activado inmediatamente, {@link RegistrationResult#pending} si se envió un email de
   * verificación, o {@link RegistrationResult#cancel} si el registro no pudo completarse.
   */
  RegistrationResult requestForRegister(String urlBase, String tenant, String email,
      String password);

  /**
   * Verifica el código de registro. Devuelve el username si el código es válido y el usuario queda
   * activado, vacío en caso contrario.
   */
  Optional<String> verifyRegister(String tenant, String code);
}
