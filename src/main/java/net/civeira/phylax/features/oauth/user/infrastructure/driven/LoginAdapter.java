package net.civeira.phylax.features.oauth.user.infrastructure.driven;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.application.spi.UserLoginSpi;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationResult;
import net.civeira.phylax.features.oauth.client.domain.model.ClientDetails;
import net.civeira.phylax.features.oauth.user.domain.gateway.LoginGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class LoginAdapter implements LoginGateway {

  private final UserLoginSpi loginSpi;

  @Override
  public AuthenticationResult validateUserData(AuthRequest request, String username,
      String password, ClientDetails client, List<AuthenticationChallege> challenges) {
    return loginSpi.validateUserData(request, username, password, client, challenges);
  }

  @Override
  public AuthenticationResult validatePreAuthenticated(AuthRequest request, String username,
      ClientDetails client, List<AuthenticationChallege> challenges) {
    return loginSpi.validatePreAuthenticated(request, username, client, challenges);
  }
}
