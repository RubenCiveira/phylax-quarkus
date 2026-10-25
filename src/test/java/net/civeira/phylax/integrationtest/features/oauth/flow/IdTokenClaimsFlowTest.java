package net.civeira.phylax.integrationtest.features.oauth.flow;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.civeira.phylax.integrationtest.features.oauth.fixtures.OidcTestFixtures;

/**
 * Claims by token purpose (task 1.13), conformant ID token (task 1.2) and public session id.
 */
@Tag("oidc-flow")
@QuarkusTest
class IdTokenClaimsFlowTest extends OidcIntegrationTestBase {

  private Response login(String scope, String nonce) {
    var spec = RestAssured.given().redirects().follow(false)
        .queryParam("client_id", OidcTestFixtures.CLIENT_ID)
        .queryParam("redirect_uri", OidcTestFixtures.REDIRECT_URI)
        .queryParam("response_type", "code").queryParam("scope", scope)
        .queryParam("state", OidcTestFixtures.STATE);
    if (nonce != null) {
      spec = spec.queryParam("nonce", nonce);
    }
    return spec.contentType(ContentType.URLENC).formParam("username", OidcTestFixtures.USERNAME)
        .formParam("password", OidcTestFixtures.PASSWORD).formParam("csid", OidcTestFixtures.CSID)
        .post("/oauth/openid/" + OidcTestFixtures.TENANT + "/auth");
  }

  private Response exchange(Response authorize) {
    String code = client.extractAuthCode(authorize);
    Assertions.assertNotNull(code, "The login must end with a code");
    Response tokens = client.exchangeCode(OidcTestFixtures.TENANT, code,
        OidcTestFixtures.REDIRECT_URI, OidcTestFixtures.CLIENT_ID);
    Assertions.assertEquals(200, tokens.statusCode());
    return tokens;
  }

  private Map<String, Object> claims(String jwt) {
    Map<String, Object> claims = tokenSigner.verifyTokenPayload(OidcTestFixtures.TENANT, jwt);
    Assertions.assertFalse(claims.isEmpty(), "The token must be signed by the server");
    return claims;
  }

  private static List<?> asList(Object value) {
    return value instanceof List<?> list ? list : List.of(value);
  }

  @Test
  void idToken_isForTheClient_withTheFactsOfTheRealLogin() {
    Response authorize = login(OidcTestFixtures.SCOPE, "n-123");
    String cookie = client.extractAuthSessionCookie(authorize);
    Map<String, Object> id = claims(exchange(authorize).jsonPath().getString("id_token"));

    Assertions.assertEquals(List.of(OidcTestFixtures.CLIENT_ID), asList(id.get("aud")));
    Assertions.assertEquals(OidcTestFixtures.CLIENT_ID, id.get("azp"));
    Assertions.assertEquals("n-123", id.get("nonce"));
    Assertions.assertEquals(List.of("pwd"), id.get("amr"));
    Assertions.assertEquals("urn:lugh:acr:1fa", id.get("acr"));
    Assertions.assertNotNull(id.get("auth_time"));
    Assertions.assertNotNull(id.get("at_hash"));
    Assertions.assertNotNull(id.get("sid"));
    Assertions.assertNotEquals(cookie, id.get("sid"), "The sid is never the session cookie");
    long lifetime = ((Number) id.get("exp")).longValue() - ((Number) id.get("iat")).longValue();
    Assertions.assertEquals(300, lifetime, "Own short lifetime, not the refresh token one");
    for (String leaked : List.of("roles", "groups", "grant_type", "scope", "s_hash",
        "session_state")) {
      Assertions.assertFalse(id.containsKey(leaked), leaked + " does not belong to an ID token");
    }
  }

  @Test
  void withoutNonce_theClaimIsAbsent() {
    Map<String, Object> id =
        claims(exchange(login(OidcTestFixtures.SCOPE, null)).jsonPath().getString("id_token"));

    Assertions.assertFalse(id.containsKey("nonce"));
  }

  @Test
  void withoutOpenid_noIdTokenIsIssued() {
    Response tokens = exchange(login("profile email", null));

    Assertions.assertNull(tokens.jsonPath().getString("id_token"));
    Assertions.assertNotNull(tokens.jsonPath().getString("access_token"));
    Assertions.assertEquals("profile email", tokens.jsonPath().getString("scope"));
  }

  @Test
  void accessToken_carriesTheSameLoginFacts_andItsOwnPurpose() {
    Response tokens = exchange(login(OidcTestFixtures.SCOPE, null));
    Map<String, Object> id = claims(tokens.jsonPath().getString("id_token"));
    Map<String, Object> access = claims(tokens.jsonPath().getString("access_token"));

    Assertions.assertEquals(id.get("auth_time"), access.get("auth_time"));
    Assertions.assertEquals(List.of("pwd"), access.get("amr"));
    Assertions.assertEquals(OidcTestFixtures.CLIENT_ID, access.get("client_id"));
    Assertions.assertEquals(List.of("openid", "profile", "email"), access.get("scope"));
    Assertions.assertFalse(access.containsKey("nonce"));
  }

  @Test
  void refresh_keepsSidAuthTimeAndAmr() {
    Response tokens = exchange(login(OidcTestFixtures.SCOPE, null));
    Map<String, Object> first = claims(tokens.jsonPath().getString("id_token"));

    Response refreshed =
        client.refreshToken(OidcTestFixtures.TENANT, tokens.jsonPath().getString("refresh_token"),
            OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET);
    Assertions.assertEquals(200, refreshed.statusCode());
    Map<String, Object> second = claims(refreshed.jsonPath().getString("id_token"));

    Assertions.assertEquals(first.get("sid"), second.get("sid"));
    Assertions.assertEquals(first.get("auth_time"), second.get("auth_time"));
    Assertions.assertEquals(first.get("amr"), second.get("amr"));
  }

  @Test
  void sid_isStableAcrossLoginsOfTheSameSession_whileTheCookieRotates() {
    Response authorize = login(OidcTestFixtures.SCOPE, null);
    String cookie = client.extractAuthSessionCookie(authorize);
    Object firstSid = claims(exchange(authorize).jsonPath().getString("id_token")).get("sid");

    Response again = client.submitSessionConfirm(OidcTestFixtures.TENANT, cookie);
    Assertions.assertNotEquals(cookie, client.extractAuthSessionCookie(again));
    Object secondSid = claims(exchange(again).jsonPath().getString("id_token")).get("sid");

    Assertions.assertEquals(firstSid, secondSid);
  }
}
