package net.civeira.phylax.integrationtest.features.oauth.flow;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import net.civeira.phylax.integrationtest.features.oauth.client.OidcFlowClient;
import net.civeira.phylax.integrationtest.features.oauth.fixtures.OidcTestFixtures;

/**
 * Authorization code flow of a confidential web app without public access, registered for
 * client_secret_basic (1.8).
 */
@Tag("oidc-flow")
@QuarkusTest
class ConfidentialCodeFlowTest extends OidcIntegrationTestBase {

  private static final String WEB_BASIC =
      OidcFlowClient.basic(OidcTestFixtures.WEB_CLIENT_ID, OidcTestFixtures.CLIENT_SECRET);

  private String webCode() {
    String code = client.extractAuthCode(client.submitLoginAs(OidcTestFixtures.TENANT,
        OidcTestFixtures.WEB_CLIENT_ID, OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD));
    Assertions.assertNotNull(code, "A confidential web app obtains a code without PKCE");
    return code;
  }

  private Response exchange(String authorization, Map<String, String> extra, String code) {
    Map<String, String> form = new java.util.HashMap<>(Map.of("grant_type", "authorization_code",
        "code", code, "redirect_uri", OidcTestFixtures.REDIRECT_URI));
    form.putAll(extra);
    return client.token(OidcTestFixtures.TENANT, authorization, form);
  }

  @Test
  void webApp_redeemsItsCode_withRegisteredMethod_andRefreshes() {
    Response exchange = exchange(WEB_BASIC, Map.of(), webCode());

    Assertions.assertEquals(200, exchange.statusCode());
    Map<String, Object> access = decodeToken(exchange.jsonPath().getString("access_token"));
    Assertions.assertEquals(OidcTestFixtures.USERNAME, String.valueOf(access.get("sub")));

    Response refresh = client.token(OidcTestFixtures.TENANT, WEB_BASIC, Map.of("grant_type",
        "refresh_token", "refresh_token", exchange.jsonPath().getString("refresh_token")));
    Assertions.assertEquals(200, refresh.statusCode());
  }

  @Test
  void webApp_withoutCredentials_isInvalidClient_andKeepsTheCode() {
    String code = webCode();

    Response anonymous = exchange(null, Map.of("client_id", OidcTestFixtures.WEB_CLIENT_ID), code);
    Assertions.assertEquals(401, anonymous.statusCode());
    Assertions.assertEquals("invalid_client", anonymous.jsonPath().getString("error"));

    Assertions.assertEquals(200, exchange(WEB_BASIC, Map.of(), code).statusCode());
  }

  @Test
  void webApp_withUnregisteredMethod_isInvalidClient() {
    Response post = exchange(null, Map.of("client_id", OidcTestFixtures.WEB_CLIENT_ID,
        "client_secret", OidcTestFixtures.CLIENT_SECRET), webCode());

    Assertions.assertEquals(401, post.statusCode());
    Assertions.assertEquals("invalid_client", post.jsonPath().getString("error"));
  }

  @Test
  void webApp_withWrongSecret_isInvalidClient() {
    Response wrong = exchange(OidcFlowClient.basic(OidcTestFixtures.WEB_CLIENT_ID, "wrong-secret"),
        Map.of(), webCode());

    Assertions.assertEquals(401, wrong.statusCode());
  }

  @Test
  void codeOfTheWebApp_cannotBeRedeemedByAnotherClient() {
    String code = webCode();

    Response other =
        exchange(OidcFlowClient.basic(OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET),
            Map.of(), code);
    Assertions.assertEquals(400, other.statusCode());
    Assertions.assertEquals("invalid_grant", other.jsonPath().getString("error"));

    Assertions.assertEquals(200, exchange(WEB_BASIC, Map.of(), code).statusCode());
  }

  @Test
  void publicSpa_withPkce_redeemsWithoutSecret() throws Exception {
    String verifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk";
    String challenge = java.util.Base64.getUrlEncoder().withoutPadding()
        .encodeToString(java.security.MessageDigest.getInstance("SHA-256")
            .digest(verifier.getBytes(java.nio.charset.StandardCharsets.US_ASCII)));
    Response login = io.restassured.RestAssured.given().redirects().follow(false)
        .queryParam("client_id", OidcTestFixtures.SPA_CLIENT_ID)
        .queryParam("redirect_uri", OidcTestFixtures.REDIRECT_URI)
        .queryParam("response_type", "code").queryParam("scope", OidcTestFixtures.SCOPE)
        .queryParam("state", OidcTestFixtures.STATE).queryParam("code_challenge", challenge)
        .queryParam("code_challenge_method", "S256")
        .contentType(io.restassured.http.ContentType.URLENC)
        .formParam("username", OidcTestFixtures.USERNAME)
        .formParam("password", OidcTestFixtures.PASSWORD).formParam("csid", OidcTestFixtures.CSID)
        .post("/oauth/openid/" + OidcTestFixtures.TENANT + "/auth");
    String code = client.extractAuthCode(login);
    Assertions.assertNotNull(code);

    Response exchange = exchange(null,
        Map.of("client_id", OidcTestFixtures.SPA_CLIENT_ID, "code_verifier", verifier), code);
    Assertions.assertEquals(200, exchange.statusCode(), exchange.asString());
  }
}
