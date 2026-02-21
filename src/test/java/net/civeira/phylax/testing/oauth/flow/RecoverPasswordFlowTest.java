package net.civeira.phylax.testing.oauth.flow;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class RecoverPasswordFlowTest extends OidcIntegrationTestBase {

  @Test
  void showRecoverForm_isRendered() {
    Response response = client.showRecoverForm(OidcTestFixtures.TENANT);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("recover"));
    Assertions.assertTrue(body.contains("name=\"username\""));
  }

  @Test
  void recoverPage_withCode_isRendered() {
    Response response = client.showRecoverWithCode(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME, OidcTestFixtures.RECOVER_CODE);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("name=\"code\""));
    Assertions.assertTrue(body.contains("name=\"password\""));
  }

  @Test
  void recover_validCode_changesPassword() {
    Response response = client.submitRecover(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.RECOVER_CODE, "newPass1!");

    Assertions.assertEquals(302, response.statusCode());
    Assertions.assertEquals(OidcTestFixtures.TENANT, changePasswordGateway.getLastTenant());
    Assertions.assertEquals(OidcTestFixtures.RECOVER_CODE,
        changePasswordGateway.getLastRecoverCode());
  }

  @Test
  void recover_invalidCode_showsError() {
    changePasswordGateway.whenValidateChange((code, pass) -> Optional.empty());

    Response response = client.submitRecover(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        "bad-code", "newPass1!");

    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertTrue(response.getBody().asString().contains("Wrong code"));
  }
}
