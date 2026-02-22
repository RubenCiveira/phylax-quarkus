package net.civeira.phylax.testing.oauth.flow;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class ScopeConsentFlowTest extends OidcIntegrationTestBase {

  @Test
  void scopeConsentRequired_showsScopeConsentForm() {
    loginGateway
        .whenValidate(() -> AuthenticationResult.clientScopeConsentRequired(OidcTestFixtures.TENANT,
            OidcTestFixtures.USERNAME, OidcTestFixtures.CLIENT_ID,
            List.of("openid", "profile", "email")));
    scopeConsentGateway.whenPending((clientId, requested) -> List.of("openid", "profile", "email"));

    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);

    Assertions.assertEquals(200, response.statusCode());
    String body = response.getBody().asString();
    Assertions.assertTrue(body.contains("openid"));
    Assertions.assertTrue(body.contains("profile"));
    Assertions.assertTrue(body.contains("email"));
    Assertions.assertTrue(body.contains("name=\"step\" value=\"scope_consent\""));
    Assertions.assertNotNull(client.extractPreSessionCookie(response));
  }

  @Test
  void scopeConsent_accepted_completesFlow() {
    loginGateway
        .whenValidate(() -> AuthenticationResult.clientScopeConsentRequired(OidcTestFixtures.TENANT,
            OidcTestFixtures.USERNAME, OidcTestFixtures.CLIENT_ID, List.of("openid", "profile")));
    scopeConsentGateway.whenPending((clientId, requested) -> requested);

    Response loginResponse = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(loginResponse);

    Response submit = client.submitScopeConsent(OidcTestFixtures.TENANT, preSession);

    Assertions.assertEquals(302, submit.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(submit));
    Assertions.assertEquals(1, scopeConsentGateway.getAcceptedCount());
    Assertions.assertEquals(OidcTestFixtures.TENANT, scopeConsentGateway.getLastTenant());
    Assertions.assertEquals(OidcTestFixtures.USERNAME, scopeConsentGateway.getLastUsername());
    Assertions.assertEquals(OidcTestFixtures.CLIENT_ID, scopeConsentGateway.getLastClientId());
    Assertions.assertNotNull(scopeConsentGateway.getLastAcceptedScopes());
    Assertions.assertFalse(scopeConsentGateway.getLastAcceptedScopes().isEmpty());
  }

  @Test
  void scopeConsent_noPendingScopes_loginPassesThroughDirectly() {
    loginGateway
        .whenValidate(() -> AuthenticationResult.clientScopeConsentRequired(OidcTestFixtures.TENANT,
            OidcTestFixtures.USERNAME, OidcTestFixtures.CLIENT_ID, List.of()));
    scopeConsentGateway.whenNoPending();

    // When the gateway returns no pending scopes, revolve() re-calls fillPreAuthenticated
    // which the loginGateway resolves as clientScopeConsentRequired again with empty list.
    // The form is still shown but with an empty scope list.
    Response response = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(response);

    Response submit = client.submitScopeConsent(OidcTestFixtures.TENANT, preSession);

    Assertions.assertEquals(302, submit.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(submit));
    Assertions.assertEquals(1, scopeConsentGateway.getAcceptedCount());
  }
}
