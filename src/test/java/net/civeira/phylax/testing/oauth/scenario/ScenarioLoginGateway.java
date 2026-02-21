package net.civeira.phylax.testing.oauth.scenario;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationData;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationMode;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationResult;
import net.civeira.phylax.features.oauth.client.domain.model.ClientDetails;
import net.civeira.phylax.features.oauth.user.domain.gateway.LoginGateway;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioLoginGateway implements LoginGateway {
  private Supplier<AuthenticationResult> onValidate =
      () -> AuthenticationResult.right(defaultData());
  private Supplier<AuthenticationResult> onPreAuth =
      () -> AuthenticationResult.right(defaultData());

  public void whenValidate(Supplier<AuthenticationResult> behavior) {
    this.onValidate = behavior;
  }

  public void whenPreAuth(Supplier<AuthenticationResult> behavior) {
    this.onPreAuth = behavior;
  }

  public void reset() {
    onValidate = () -> AuthenticationResult.right(defaultData());
    onPreAuth = () -> AuthenticationResult.right(defaultData());
  }

  @Override
  public AuthenticationResult validateUserData(AuthRequest request, String username,
      String password, ClientDetails client, List<AuthenticationChallege> challenges) {
    return onValidate.get();
  }

  @Override
  public AuthenticationResult validatePreAuthenticated(AuthRequest request, String username,
      ClientDetails client, List<AuthenticationChallege> challenges) {
    return onPreAuth.get();
  }

  private static AuthenticationData defaultData() {
    AuthenticationData data = new AuthenticationData();
    data.setUid("user-1");
    data.setUsername(OidcTestFixtures.USERNAME);
    data.setTenant(OidcTestFixtures.TENANT);
    data.setMode(AuthenticationMode.PASSWORD);
    data.setTime(Instant.now());
    return data;
  }
}
