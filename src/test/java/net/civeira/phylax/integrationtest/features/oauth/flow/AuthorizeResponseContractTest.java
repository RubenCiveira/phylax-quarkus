package net.civeira.phylax.integrationtest.features.oauth.flow;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
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

/** J-06: authorization responses, not just request parsing, through the HTTP boundary. */
@Tag("oidc-flow")
@QuarkusTest
class AuthorizeResponseContractTest extends OidcIntegrationTestBase {

  private static final String STATE = "s & = / é";
  private static final String REDIRECT = OidcTestFixtures.REDIRECT_URI + "?existing=1";

  private Response authorize(String type, String nonce, String redirect) {
    var request = RestAssured.given().redirects().follow(false).contentType(ContentType.URLENC)
        .queryParam("client_id", OidcTestFixtures.CLIENT_ID).queryParam("redirect_uri", redirect)
        .queryParam("response_type", type).queryParam("scope", OidcTestFixtures.SCOPE)
        .queryParam("state", STATE);
    if (nonce != null)
      request.queryParam("nonce", nonce);
    return request.formParam("username", OidcTestFixtures.USERNAME)
        .formParam("password", OidcTestFixtures.PASSWORD).formParam("csid", OidcTestFixtures.CSID)
        .post("/oauth/openid/" + OidcTestFixtures.TENANT + "/auth");
  }

  private Map<String, String> parameters(String part) {
    Map<String, String> params = new LinkedHashMap<>();
    Arrays.stream(part.split("&")).filter(p -> p.contains("="))
        .forEach(p -> params.put(
            URLDecoder.decode(p.substring(0, p.indexOf('=')), StandardCharsets.UTF_8),
            URLDecoder.decode(p.substring(p.indexOf('=') + 1), StandardCharsets.UTF_8)));
    return params;
  }

  private String issuer() {
    return RestAssured
        .get("/oauth/openid/" + OidcTestFixtures.TENANT + "/.well-known/openid-configuration")
        .jsonPath().getString("issuer");
  }

  private static String halfHash(String artifact) throws Exception {
    byte[] digest =
        MessageDigest.getInstance("SHA-256").digest(artifact.getBytes(StandardCharsets.US_ASCII));
    return Base64.getUrlEncoder().withoutPadding().encodeToString(Arrays.copyOf(digest, 16));
  }

  @Test
  void codeUsesQueryAndPreservesExistingQueryStateAndIssuer() {
    Response response = authorize("code", null, OidcTestFixtures.REDIRECT_URI);
    Assertions.assertEquals(302, response.statusCode(), response.asString());
    URI uri = URI.create(response.getHeader("Location"));
    Assertions.assertNull(uri.getFragment());
    Map<String, String> query = parameters(uri.getRawQuery());
    Assertions.assertNotNull(query.get("code"));
    Assertions.assertEquals(STATE, query.get("state"));
    Assertions.assertEquals(issuer(), query.get("iss"));
    Assertions.assertFalse(query.containsKey("access_token"));
  }

  @Test
  void eachHybridReturnsOnlyRequestedArtifactsWithBoundHashes() throws Exception {
    clientStoreGateway.setAllowedResponseTypes(
        List.of("code", "code id_token", "code token", "code id_token token"));
    for (String type : List.of("code id_token", "code token", "code id_token token",
        "id_token code")) {
      Response response = authorize(type, "bound-nonce", OidcTestFixtures.REDIRECT_URI);
      Assertions.assertEquals(302, response.statusCode(), type + ": " + response.asString());
      URI uri = URI.create(response.getHeader("Location"));
      Assertions.assertNull(uri.getRawQuery(), type);
      Map<String, String> fragment = parameters(uri.getRawFragment());
      Assertions.assertNotNull(fragment.get("code"), type);
      Assertions.assertEquals(STATE, fragment.get("state"));
      Assertions.assertEquals(issuer(), fragment.get("iss"));
      boolean idRequested = type.contains("id_token");
      boolean accessRequested = List.of(type.split(" ")).contains("token");
      Assertions.assertEquals(idRequested, fragment.containsKey("id_token"), type);
      Assertions.assertEquals(accessRequested, fragment.containsKey("access_token"), type);
      if (accessRequested) {
        Assertions.assertEquals("Bearer", fragment.get("token_type"));
        Assertions.assertNotNull(fragment.get("expires_in"));
      } else {
        Assertions.assertFalse(fragment.containsKey("token_type"));
      }
      if (idRequested) {
        Map<String, Object> claims = decodeToken(fragment.get("id_token"));
        Assertions.assertEquals("bound-nonce", claims.get("nonce"));
        Assertions.assertEquals(halfHash(fragment.get("code")), claims.get("c_hash"));
        Assertions.assertEquals(accessRequested, claims.containsKey("at_hash"));
        if (accessRequested)
          Assertions.assertEquals(halfHash(fragment.get("access_token")), claims.get("at_hash"));
      }
    }
  }

  @Test
  void missingHybridNonceIsRedirectedAsAnErrorWithoutPersistingASession() {
    clientStoreGateway.setAllowedResponseTypes(List.of("code id_token"));
    Response response = authorize("code id_token", null, OidcTestFixtures.REDIRECT_URI);
    Assertions.assertEquals(302, response.statusCode());
    Map<String, String> fragment =
        parameters(URI.create(response.getHeader("Location")).getRawFragment());
    Assertions.assertEquals("invalid_request", fragment.get("error"));
    Assertions.assertEquals(issuer(), fragment.get("iss"));
    Assertions.assertNull(response.getCookie("AUTH_SESSION_ID"));
  }

  @Test
  void unsupportedImplicitIsNeverAdvertisedOrIssued() {
    clientStoreGateway
        .setAllowedResponseTypes(List.of("code", "token", "id_token", "id_token token"));
    for (String type : List.of("token", "id_token", "id_token token")) {
      Response response = RestAssured.given().redirects().follow(false)
          .queryParam("client_id", OidcTestFixtures.CLIENT_ID)
          .queryParam("redirect_uri", OidcTestFixtures.REDIRECT_URI)
          .queryParam("response_type", type).queryParam("state", STATE)
          .get("/oauth/openid/" + OidcTestFixtures.TENANT + "/auth");
      Assertions.assertEquals(302, response.statusCode(), type);
      Assertions.assertEquals("unsupported_response_type",
          parameters(URI.create(response.getHeader("Location")).getRawFragment()).get("error"));
    }
    List<String> advertised = RestAssured
        .get("/oauth/openid/" + OidcTestFixtures.TENANT + "/.well-known/openid-configuration")
        .jsonPath().getList("response_types_supported");
    Assertions.assertEquals(List.of("code", "code id_token", "code token", "code id_token token"),
        advertised);
  }
}
