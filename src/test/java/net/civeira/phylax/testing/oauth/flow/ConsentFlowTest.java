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
class ConsentFlowTest extends OidcIntegrationTestBase {

  @Test
  void consentRequired_showsConsentForm() {
    loginGateway.whenValidate(() -> AuthenticationResult.consentRequired(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));
    consentGateway.whenPending(() -> java.util.Optional.of("Texto de los terminos"));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("name=\"consent\""));
    Assertions.assertTrue(body.contains("Texto de los terminos"));
    Assertions.assertNotNull(client.extractPreSessionCookie(response));
  }

  @Test
  void consent_notAccepted_staysOnForm() {
    loginGateway.whenValidate(() -> AuthenticationResult.consentRequired(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(response);

    Response submit = client.submitConsent(OidcTestFixtures.TENANT, "off", preSession);

    Assertions.assertEquals(200, submit.statusCode());
    Assertions.assertTrue(submit.getBody().asString().contains("name=\"consent\""));
  }

  @Test
  void consent_accepted_completesFlow() {
    loginGateway.whenValidate(() -> AuthenticationResult.consentRequired(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(response);

    Response submit = client.submitConsent(OidcTestFixtures.TENANT, "on", preSession);

    Assertions.assertEquals(302, submit.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(submit));
    Assertions.assertEquals(1, consentGateway.getAcceptedCount());
    Assertions.assertEquals(OidcTestFixtures.TENANT, consentGateway.getLastTenant());
    Assertions.assertEquals(OidcTestFixtures.USERNAME, consentGateway.getLastUsername());
  }
}
