package net.civeira.phylax.testing.oauth.flow;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;
import net.civeira.phylax.testing.oauth.scenario.ScenarioDelegateGateway;

@Tag("oidc-flow")
@QuarkusTest
class DelegatedLoginFlowTest extends OidcIntegrationTestBase {

  @Test
  void loginForm_showsDelegatedProviderButton() {
    // Enable the "google" provider
    delegateGateway.setProviderEnabled(true);

    Response response = client.startAuthFlow(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.REDIRECT_URI, OidcTestFixtures.SCOPE);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    // The login form renders a social-form button for the enabled provider
    Assertions.assertTrue(body.contains("social-form-" + ScenarioDelegateGateway.PROVIDER_ID),
        "Login form must contain a button for the delegated provider");
    Assertions.assertTrue(body.contains("delegated-login"),
        "Login form must contain the delegated-login step");
  }

  @Test
  void delegatedCallback_resolvesUsername_and_completesFlow() {
    // Enable provider and configure username resolution
    delegateGateway.setProviderEnabled(true);
    delegateGateway.whenResolveUsername(() -> Optional.of(OidcTestFixtures.USERNAME));

    // Pre-store the delegated token as the DelegatedAccessController would have done
    String delegatedCode = "delegated-test-code";
    delegateGateway.saveToken(null, delegatedCode, ScenarioDelegateGateway.buildTokenInfo());

    // Simulate the final callback step (query-delegated) that doBackLoginForm auto-submits
    Response response = client.submitQueryDelegated(OidcTestFixtures.TENANT, delegatedCode,
        ScenarioDelegateGateway.PROVIDER_ID);

    Assertions.assertEquals(302, response.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(response),
        "Successful delegated login must issue an auth code");
  }

  @Test
  void delegatedCallback_unknownCode_staysOnLoginForm() {
    // Enable provider but do NOT pre-store any token for "bad-code"
    delegateGateway.setProviderEnabled(true);
    // Prevent the fallback doExecLogin from accidentally succeeding with empty credentials
    loginGateway.whenValidate(() -> AuthenticationResult.wrongCredential(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));

    Response response = client.submitQueryDelegated(OidcTestFixtures.TENANT, "bad-code",
        ScenarioDelegateGateway.PROVIDER_ID);

    // No stored token → step returns empty → fallback login with no credentials fails → login form
    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertNull(client.extractAuthCode(response));
    Assertions.assertTrue(response.getBody().asString().contains("name=\"password\""),
        "Login form must be shown when delegated code is unknown");
  }
}
