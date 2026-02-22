package net.civeira.phylax.testing.oauth.flow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import net.civeira.phylax.features.oauth.user.domain.RegistrationResult;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class RegisterUserFlowTest extends OidcIntegrationTestBase {

  @Test
  void showRegisterForm_allowRegister_showsForm() {
    // registerGateway.allowRegister = true by default
    Response response = client.showRegisterForm(OidcTestFixtures.TENANT);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("value=\"do_register\""));
    Assertions.assertTrue(body.contains("name=\"reg_email\""));
  }

  @Test
  void register_pending_showsPendingPage() {
    registerGateway.whenRequestRegister(() -> RegistrationResult.pending());

    Response response = client.submitRegister(OidcTestFixtures.TENANT,
        OidcTestFixtures.REGISTER_EMAIL, OidcTestFixtures.PASSWORD);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains(OidcTestFixtures.REGISTER_EMAIL));
    Assertions.assertEquals(1, registerGateway.getRequestCalls());
    Assertions.assertEquals(OidcTestFixtures.REGISTER_EMAIL, registerGateway.getLastEmail());
  }

  @Test
  void register_ok_completesFlow() {
    registerGateway.whenRequestRegister(() -> RegistrationResult.ok(OidcTestFixtures.USERNAME));

    Response response = client.submitRegister(OidcTestFixtures.TENANT,
        OidcTestFixtures.REGISTER_EMAIL, OidcTestFixtures.PASSWORD);

    Assertions.assertEquals(302, response.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(response));
    Assertions.assertEquals(1, registerGateway.getRequestCalls());
  }

  @Test
  void register_cancel_showsRegisterFormWithError() {
    registerGateway.whenRequestRegister(() -> RegistrationResult.cancel());

    Response response = client.submitRegister(OidcTestFixtures.TENANT,
        OidcTestFixtures.REGISTER_EMAIL, OidcTestFixtures.PASSWORD);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("value=\"do_register\""));
    Assertions.assertEquals(1, registerGateway.getRequestCalls());
  }

  @Test
  void verify_validCode_completesFlow() {
    Response response = client.submitRegisterVerify(OidcTestFixtures.TENANT,
        OidcTestFixtures.REGISTER_EMAIL, OidcTestFixtures.REGISTER_CODE);

    Assertions.assertEquals(302, response.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(response));
    Assertions.assertEquals(1, registerGateway.getVerifyCalls());
    Assertions.assertEquals(OidcTestFixtures.REGISTER_CODE, registerGateway.getLastCode());
  }

  @Test
  void verify_invalidCode_showsVerifyFormWithError() {
    Response response = client.submitRegisterVerify(OidcTestFixtures.TENANT,
        OidcTestFixtures.REGISTER_EMAIL, "wrong-code");

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("name=\"regcode\""));
    Assertions.assertEquals(1, registerGateway.getVerifyCalls());
  }

  @Test
  void showRegisterVerify_showsVerifyForm() {
    Response response = client.showRegisterVerify(OidcTestFixtures.TENANT,
        OidcTestFixtures.REGISTER_EMAIL, OidcTestFixtures.REGISTER_CODE);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("name=\"regcode\""));
    Assertions.assertTrue(body.contains(OidcTestFixtures.REGISTER_CODE));
  }
}
