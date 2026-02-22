package net.civeira.phylax.testing.oauth.flow;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
class EndToEndFlowTest extends OidcIntegrationTestBase {

  @Test
  void login_then_consent_then_mfa() {
    loginGateway.whenValidate(() -> AuthenticationResult.consentRequired(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));

    AtomicInteger preAuthCalls = new AtomicInteger();
    loginGateway.whenPreAuth(() -> {
      if (preAuthCalls.getAndIncrement() == 0) {
        return AuthenticationResult.mfaRequired(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME);
      }
      return AuthenticationResult.right(loginGatewayResult());
    });

    Response login = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(login);
    Assertions.assertTrue(
        decodeChallenge(preSession).getChallenges().contains(AuthenticationChallege.USE_CONSENT));

    Response consent = client.submitConsent(OidcTestFixtures.TENANT, "on", preSession);
    String preSessionAfterConsent = client.extractPreSessionCookie(consent);
    Assertions.assertNotNull(preSessionAfterConsent);
    Assertions.assertTrue(decodeChallenge(preSessionAfterConsent).getChallenges()
        .contains(AuthenticationChallege.MFA));

    Response mfa = client.submitMfa(OidcTestFixtures.TENANT, OidcTestFixtures.MFA_CODE,
        preSessionAfterConsent);
    Assertions.assertEquals(302, mfa.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(mfa));
  }

  @Test
  void login_then_newPass_then_consent() {
    loginGateway.whenValidate(() -> AuthenticationResult
        .newPasswordRequired(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME));

    AtomicInteger preAuthCalls = new AtomicInteger();
    loginGateway.whenPreAuth(() -> {
      if (preAuthCalls.getAndIncrement() == 0) {
        return AuthenticationResult.consentRequired(OidcTestFixtures.TENANT,
            OidcTestFixtures.USERNAME);
      }
      return AuthenticationResult.right(loginGatewayResult());
    });

    Response login = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(login);
    Assertions.assertTrue(decodeChallenge(preSession).getChallenges()
        .contains(AuthenticationChallege.FRESH_PASSWORD));

    Response newPass = client.submitNewPass(OidcTestFixtures.TENANT, OidcTestFixtures.PASSWORD,
        "New@Password1", preSession);
    String preSessionAfterPass = client.extractPreSessionCookie(newPass);
    Assertions.assertNotNull(preSessionAfterPass);
    Assertions.assertTrue(decodeChallenge(preSessionAfterPass).getChallenges()
        .contains(AuthenticationChallege.USE_CONSENT));

    Response consent = client.submitConsent(OidcTestFixtures.TENANT, "on", preSessionAfterPass);
    Assertions.assertEquals(302, consent.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(consent));
  }

  @Test
  void e2e_scopeConsent_accepted_completesLogin() {
    loginGateway
        .whenValidate(() -> AuthenticationResult.clientScopeConsentRequired(OidcTestFixtures.TENANT,
            OidcTestFixtures.USERNAME, OidcTestFixtures.CLIENT_ID,
            List.of("openid", "profile", "email")));
    loginGateway.whenPreAuth(() -> AuthenticationResult.right(loginGatewayResult()));
    scopeConsentGateway.whenPending((clientId, requested) -> requested);

    Response login = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(login);
    Assertions.assertNotNull(preSession);
    Assertions.assertEquals(200, login.statusCode());
    Assertions
        .assertTrue(login.getBody().asString().contains("name=\"step\" value=\"scope_consent\""));
    Assertions.assertTrue(decodeChallenge(preSession).getChallenges()
        .contains(AuthenticationChallege.CLIENT_CONSENT));

    Response scopeConsent = client.submitScopeConsent(OidcTestFixtures.TENANT, preSession);

    Assertions.assertEquals(302, scopeConsent.statusCode());
    Assertions.assertNotNull(client.extractAuthCode(scopeConsent));
    Assertions.assertEquals(1, scopeConsentGateway.getAcceptedCount());
    Assertions.assertEquals(OidcTestFixtures.CLIENT_ID, scopeConsentGateway.getLastClientId());
  }

  @Test
  void e2e_scopeConsent_denied_returnsToLoginForm() {
    loginGateway
        .whenValidate(() -> AuthenticationResult.clientScopeConsentRequired(OidcTestFixtures.TENANT,
            OidcTestFixtures.USERNAME, OidcTestFixtures.CLIENT_ID,
            List.of("openid", "profile", "email")));
    scopeConsentGateway.whenPending((clientId, requested) -> requested);

    Response login = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String preSession = client.extractPreSessionCookie(login);
    Assertions.assertNotNull(preSession);

    // User clicks Deny — posts step=start which returns to the login form
    Response deny = client.submitDenyScopeConsent(OidcTestFixtures.TENANT, preSession);

    Assertions.assertEquals(200, deny.statusCode());
    Assertions.assertNull(client.extractAuthCode(deny));
    Assertions.assertTrue(deny.getBody().asString().contains("name=\"password\""));
    Assertions.assertEquals(0, scopeConsentGateway.getAcceptedCount());
  }

  private net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData loginGatewayResult() {
    net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData data =
        new net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData();
    data.setUid("user-1");
    data.setUsername(OidcTestFixtures.USERNAME);
    data.setTenant(OidcTestFixtures.TENANT);
    data.setMode(
        net.civeira.phylax.features.oauth.authentication.domain.AuthenticationMode.PASSWORD);
    data.setTime(java.time.Instant.now());
    return data;
  }
}
