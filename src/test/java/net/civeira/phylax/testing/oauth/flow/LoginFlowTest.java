package net.civeira.phylax.testing.oauth.flow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class LoginFlowTest extends OidcIntegrationTestBase {

  @Test
  void loginForm_isRendered() {
    Response response = client.startAuthFlow(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.REDIRECT_URI, OidcTestFixtures.SCOPE);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("<form"));
    Assertions.assertTrue(body.contains("name=\"username\""));
    String authCookie = response.getCookie("AUTH_SESSION_ID");
    Assertions.assertTrue(authCookie == null || authCookie.isEmpty());
  }

  @Test
  void wrongCredentials_staysOnForm() {
    loginGateway.whenValidate(() -> AuthenticationResult.wrongCredential(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.WRONG_PASSWORD, null);

    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertNull(response.getHeader("Location"));
    Assertions.assertTrue(response.getBody().asString().contains("name=\"username\""));
  }

  @Test
  void correctCredentials_redirectsWithCode() {
    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);

    Assertions.assertEquals(302, response.statusCode());
    String location = response.getHeader("Location");
    Assertions.assertNotNull(location);
    Assertions.assertTrue(location.contains("code="));
    Assertions.assertTrue(location.contains("state=" + OidcTestFixtures.STATE));
    String authCookie = client.extractAuthSessionCookie(response);
    Assertions.assertNotNull(authCookie);
  }

  @Test
  void authCode_isOneTimeUse() {
    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String code = client.extractAuthCode(response);
    Assertions.assertNotNull(code);

    Response exchange = client.exchangeCode(OidcTestFixtures.TENANT, code,
        OidcTestFixtures.REDIRECT_URI, OidcTestFixtures.CLIENT_ID);
    Assertions.assertEquals(200, exchange.statusCode());

    Response retry = client.exchangeCode(OidcTestFixtures.TENANT, code,
        OidcTestFixtures.REDIRECT_URI, OidcTestFixtures.CLIENT_ID);
    Assertions.assertEquals(401, retry.statusCode());
  }

  @Test
  void existingSession_skipLoginForm() {
    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String authCookie = client.extractAuthSessionCookie(response);
    Assertions.assertNotNull(authCookie);

    Response show =
        client.startAuthFlowWithSession(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
            OidcTestFixtures.REDIRECT_URI, OidcTestFixtures.SCOPE, authCookie);

    Assertions.assertEquals(200, show.statusCode());
    String body = show.getBody().asString();
    Assertions.assertTrue(body.contains("id=\"enter\""));
    Assertions.assertFalse(body.contains("name=\"username\""));

    Response confirm = client.submitSessionConfirm(OidcTestFixtures.TENANT, authCookie);
    Assertions.assertEquals(302, confirm.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(confirm));
  }
}
