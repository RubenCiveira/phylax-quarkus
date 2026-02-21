package net.civeira.phylax.testing.oauth.flow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationResult;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class NewMfaFlowTest extends OidcIntegrationTestBase {

  @Test
  void newMfaRequired_showsMfaSetupPage() {
    loginGateway.whenValidate(() -> AuthenticationResult.newMfaRequired(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("mfa-setup"));
    Assertions.assertTrue(body.contains("name=\"otp_code\""));
  }

  @Test
  void mfaSetupSelector_isRendered() {
    loginGateway.whenValidate(() -> AuthenticationResult.newMfaRequired(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(response);

    Response setup = client.fetchMfaSetup(OidcTestFixtures.TENANT, preSession);
    Assertions.assertEquals(200, setup.statusCode());
    Assertions.assertTrue(setup.contentType().startsWith("image/"));
  }

  @Test
  void newMfa_verify_wrongCode_staysOnForm() {
    loginGateway.whenValidate(() -> AuthenticationResult.newMfaRequired(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));
    mfaGateway.setValidNewOtp("111111");

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(response);

    Response submit = client.submitNewMfaVerify(OidcTestFixtures.TENANT, "000000", preSession);

    Assertions.assertEquals(200, submit.statusCode());
    Assertions.assertTrue(submit.getBody().asString().contains("No se ha podido guardar"));
  }

  @Test
  void newMfa_verify_correctCode_completesFlow() {
    loginGateway.whenValidate(() -> AuthenticationResult.newMfaRequired(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(response);

    Response submit =
        client.submitNewMfaVerify(OidcTestFixtures.TENANT, OidcTestFixtures.MFA_CODE, preSession);

    Assertions.assertEquals(302, submit.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(submit));
  }
}
