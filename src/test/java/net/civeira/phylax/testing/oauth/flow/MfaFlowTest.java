package net.civeira.phylax.testing.oauth.flow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationResult;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class MfaFlowTest extends OidcIntegrationTestBase {

  @Test
  void mfaRequired_showsMfaForm() {
    loginGateway.whenValidate(
        () -> AuthenticationResult.mfaRequired(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("name=\"mfa_code\""));
    Assertions.assertTrue(body.contains("value=\"mfa\""));
    String preSession = client.extractPreSessionCookie(response);
    Assertions.assertNotNull(preSession);
    Assertions.assertTrue(
        decodeChallenge(preSession).getChallenges().contains(AuthenticationChallege.MFA));
  }

  @Test
  void mfa_wrongCode_staysOnForm() {
    loginGateway.whenValidate(
        () -> AuthenticationResult.mfaRequired(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(response);

    Response submit = client.submitMfa(OidcTestFixtures.TENANT, "000000", preSession);

    Assertions.assertEquals(200, submit.statusCode());
    String body = submit.getBody().asString();
    Assertions.assertTrue(body.contains("name=\"mfa_code\""));
    Assertions.assertTrue(body.contains("MFA incorrecto"));
  }

  @Test
  void mfa_correctCode_completesFlow() {
    loginGateway.whenValidate(
        () -> AuthenticationResult.mfaRequired(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(response);

    Response submit =
        client.submitMfa(OidcTestFixtures.TENANT, OidcTestFixtures.MFA_CODE, preSession);

    Assertions.assertEquals(302, submit.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(submit));
    Assertions.assertEquals(OidcTestFixtures.TENANT, mfaGateway.getLastTenant());
    Assertions.assertEquals(OidcTestFixtures.USERNAME, mfaGateway.getLastUsername());
    Assertions.assertEquals(OidcTestFixtures.MFA_CODE, mfaGateway.getLastOtp());
  }
}
