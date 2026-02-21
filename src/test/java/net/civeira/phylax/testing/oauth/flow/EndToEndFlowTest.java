package net.civeira.phylax.testing.oauth.flow;

import java.util.concurrent.atomic.AtomicInteger;

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

  private net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationData loginGatewayResult() {
    net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationData data =
        new net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationData();
    data.setUid("user-1");
    data.setUsername(OidcTestFixtures.USERNAME);
    data.setTenant(OidcTestFixtures.TENANT);
    data.setMode(
        net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationMode.PASSWORD);
    data.setTime(java.time.Instant.now());
    return data;
  }
}
