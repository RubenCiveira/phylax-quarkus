package net.civeira.phylax.testing.oauth.flow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class NewPasswordFlowTest extends OidcIntegrationTestBase {

  @Test
  void newPasswordRequired_showsChangeForm() {
    loginGateway.whenValidate(() -> AuthenticationResult
        .newPasswordRequired(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("name=\"old_pass\""));
    Assertions.assertTrue(body.contains("name=\"new_pass\""));
    String preSession = client.extractPreSessionCookie(response);
    Assertions.assertNotNull(preSession);
    Assertions.assertTrue(decodeChallenge(preSession).getChallenges()
        .contains(AuthenticationChallege.FRESH_PASSWORD));
  }

  @Test
  void newPass_wrongOldPass_staysOnForm() {
    loginGateway.whenValidate(() -> AuthenticationResult
        .newPasswordRequired(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME));
    changePasswordGateway.setForceUpdatePasswordResult(false);

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(response);

    Response submit =
        client.submitNewPass(OidcTestFixtures.TENANT, "wrong", "newPass123", preSession);

    Assertions.assertEquals(200, submit.statusCode());
    String body = submit.getBody().asString();
    Assertions.assertTrue(body.contains("name=\"old_pass\""));
    Assertions.assertTrue(body.contains("No se ha podido guardar"));
  }

  @Test
  void newPass_correctData_completesFlow() {
    loginGateway.whenValidate(() -> AuthenticationResult
        .newPasswordRequired(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME));
    changePasswordGateway.setForceUpdatePasswordResult(true);

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(response);

    Response submit = client.submitNewPass(OidcTestFixtures.TENANT, OidcTestFixtures.PASSWORD,
        "New@Password1", preSession);

    Assertions.assertEquals(302, submit.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(submit));
    Assertions.assertEquals(1, changePasswordGateway.getForceUpdateCalls());
    Assertions.assertEquals(OidcTestFixtures.TENANT, changePasswordGateway.getLastTenant());
    Assertions.assertEquals(OidcTestFixtures.USERNAME, changePasswordGateway.getLastUsername());
  }
}
