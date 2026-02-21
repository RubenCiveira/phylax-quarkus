package net.civeira.phylax.features.oauth.user.domain.gateway;

import java.util.Optional;

public interface ChangePasswordGateway {

  /** Indica si el tenant permite la recuperación de contraseña por email. */
  boolean allowRecover(String tenant);

  /**
   * Genera y envía el código de recuperación de contraseña al usuario. El parámetro {@code urlBase}
   * se usa para construir el enlace de recuperación en el email.
   */
  void requestForChange(String urlBase, String tenant, String username);

  /**
   * Valida el código de recuperación y cambia la contraseña. Devuelve el username si el código es
   * válido, vacío en caso contrario.
   */
  Optional<String> validateChangeRequest(String tenant, String code, String newPassword);

  /**
   * Cambia la contraseña del usuario verificando la contraseña antigua. Devuelve true si el cambio
   * fue exitoso.
   */
  boolean forceUpdatePassword(String tenant, String username, String oldPassword,
      String newPassword);
}
