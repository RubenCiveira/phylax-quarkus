package net.civeira.phylax.features.oauth.user.domain.gateway;

import java.util.List;
import net.civeira.phylax.features.oauth.authentication.domain.AuthRequest;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;

public interface LoginGateway {

  /**
   * Valida las credenciales del usuario y aplica todos los checks del flujo de login: tenant,
   * usuario, password, MFA, términos, scopes.
   */
  AuthenticationResult validateUserData(AuthRequest request, String username, String password,
      ClientDetails client, List<AuthenticationChallege> challenges);

  /**
   * Carga los datos del usuario ya pre-autenticado (por username, sin password). Se usa cuando se
   * han completado los challenges intermedios.
   */
  AuthenticationResult validatePreAuthenticated(AuthRequest request, String username,
      ClientDetails client, List<AuthenticationChallege> challenges);
}
