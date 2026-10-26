package net.civeira.phylax.integrationtest.features.oauth.flow;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.civeira.phylax.integrationtest.features.oauth.client.OidcFlowClient;
import net.civeira.phylax.integrationtest.features.oauth.fixtures.OidcTestFixtures;

/**
 * Origin and policy of authorization requests (J-05): PAR bound and single use (0.14, 2.3), PKCE
 * (1.11), encoded state (0.11), hints (2.7), levels of assurance (3.10) and signed request objects
 * (3.3).
 */
@Tag("oidc-flow")
@QuarkusTest
class AuthorizeRequestFlowTest extends OidcIntegrationTestBase {

  private static final String AUTH = "/oauth/openid/" + OidcTestFixtures.TENANT + "/auth";
  private static final String BINDING = "PAR_BINDING";

  @Test
  void discoveryOnlyAdvertisesImplementedSessionAndTokenCapabilities() {
    Response discovery = RestAssured
        .get("/oauth/openid/" + OidcTestFixtures.TENANT + "/.well-known/openid-configuration");
    Assertions.assertEquals(200, discovery.statusCode());
    Map<String, Object> metadata = discovery.jsonPath().getMap("$");
    Assertions.assertFalse(metadata.containsKey("check_session_iframe"));
    Assertions.assertFalse(metadata.containsKey("mtls_endpoint_aliases"));
    Assertions.assertFalse(metadata.containsKey("backchannel_authentication_endpoint"));
    Assertions.assertEquals(true, metadata.get("authorization_response_iss_parameter_supported"));
    Assertions.assertTrue(discovery.jsonPath().getList("scopes_supported").contains("openid"));
  }

  // ── helpers ───────────────────────────────────────────────────────────────

  private static Map<String, String> spaRequest() {
    Map<String, String> params = new LinkedHashMap<>();
    params.put("client_id", OidcTestFixtures.SPA_CLIENT_ID);
    params.put("redirect_uri", OidcTestFixtures.REDIRECT_URI);
    params.put("response_type", "code");
    params.put("scope", OidcTestFixtures.SCOPE);
    params.put("state", "pushed-state");
    params.put("code_challenge", OidcTestFixtures.CODE_CHALLENGE);
    params.put("code_challenge_method", "S256");
    return params;
  }

  private static Response push(Map<String, String> params, String authorization) {
    RequestSpecification spec = RestAssured.given().contentType(ContentType.URLENC);
    if (authorization != null) {
      spec = spec.header("Authorization", authorization);
    }
    return spec.formParams(params)
        .post("/oauth/openid/" + OidcTestFixtures.TENANT + "/par-request");
  }

  private static String pushedUri(Map<String, String> params) {
    Response response = push(params, null);
    Assertions.assertEquals(201, response.statusCode(), response.getBody().asString());
    return response.jsonPath().getString("request_uri");
  }

  private static Response authorize(Map<String, String> query, String binding) {
    RequestSpecification spec = RestAssured.given().redirects().follow(false);
    if (binding != null) {
      spec = spec.cookie(BINDING, binding);
    }
    return spec.queryParams(query).get(AUTH);
  }

  private static Response loginWith(Map<String, String> query, String binding) {
    RequestSpecification spec =
        RestAssured.given().redirects().follow(false).contentType(ContentType.URLENC);
    if (binding != null) {
      spec = spec.cookie(BINDING, binding);
    }
    return spec.queryParams(query).formParam("username", OidcTestFixtures.USERNAME)
        .formParam("password", OidcTestFixtures.PASSWORD).formParam("csid", OidcTestFixtures.CSID)
        .post(AUTH);
  }

  private static Map<String, String> directRequest() {
    Map<String, String> query = new LinkedHashMap<>();
    query.put("client_id", OidcTestFixtures.CLIENT_ID);
    query.put("redirect_uri", OidcTestFixtures.REDIRECT_URI);
    query.put("response_type", "code");
    query.put("scope", OidcTestFixtures.SCOPE);
    query.put("state", OidcTestFixtures.STATE);
    return query;
  }

  private static void assertErrorRedirect(Response response, String error) {
    Assertions.assertEquals(302, response.statusCode(), response.getBody().asString());
    Assertions.assertTrue(response.getHeader("Location").contains("error=" + error),
        response.getHeader("Location"));
  }

  // ── PAR (0.14, 2.3, 0.11) ─────────────────────────────────────────────────

  @Test
  void par_publicClient_storedParametersWin_overTheQuery() {
    String requestUri = pushedUri(spaRequest());

    Map<String, String> tampered = new LinkedHashMap<>();
    tampered.put("client_id", OidcTestFixtures.SPA_CLIENT_ID);
    tampered.put("request_uri", requestUri);
    tampered.put("redirect_uri", "http://evil.example/steal");
    tampered.put("scope", "admin");
    Response form = authorize(tampered, null);
    Assertions.assertEquals(200, form.statusCode());
    String binding = form.getCookie(BINDING);
    Assertions.assertNotNull(binding, "The browser that consumed the request gets the binding");

    Response done = loginWith(tampered, binding);
    Assertions.assertEquals(302, done.statusCode());
    String location = done.getHeader("Location");
    Assertions.assertTrue(location.startsWith(OidcTestFixtures.REDIRECT_URI + "?code="), location);
    Assertions.assertTrue(location.contains("&state=pushed-state&iss="), location);
  }

  @Test
  void par_requestUri_isSingleUse_forAnyOtherBrowser() {
    String requestUri = pushedUri(spaRequest());
    Map<String, String> query =
        Map.of("client_id", OidcTestFixtures.SPA_CLIENT_ID, "request_uri", requestUri);
    Assertions.assertEquals(200, authorize(query, null).statusCode());

    Response replay = authorize(query, null);
    Assertions.assertEquals(400, replay.statusCode());
    Assertions.assertEquals("invalid_request_uri", replay.jsonPath().getString("error"));
    Response forged = authorize(query, "hmac-sha256:t0:forged");
    Assertions.assertEquals(400, forged.statusCode());
  }

  @Test
  void par_withAnotherClientId_isRejected() {
    String requestUri = pushedUri(spaRequest());

    Response response =
        authorize(Map.of("client_id", OidcTestFixtures.CLIENT_ID, "request_uri", requestUri), null);

    Assertions.assertEquals(400, response.statusCode());
    Assertions.assertEquals("invalid_request", response.jsonPath().getString("error"));
  }

  @Test
  void par_expired_isRejected() {
    String requestUri = pushedUri(spaRequest());
    parRequestGateway.expire(requestUri, OidcTestFixtures.TENANT);

    Assertions.assertEquals(400,
        authorize(Map.of("client_id", OidcTestFixtures.SPA_CLIENT_ID, "request_uri", requestUri),
            null).statusCode());
  }

  @Test
  void push_isValidatedLikeTheAuthorizationEndpoint() {
    Map<String, String> otherClient = spaRequest();
    otherClient.put("client_id", OidcTestFixtures.CLIENT_ID);
    Response mismatch = push(otherClient,
        OidcFlowClient.basic(OidcTestFixtures.WEB_CLIENT_ID, OidcTestFixtures.CLIENT_SECRET));
    // Basic for one client and client_id of another: mixed credentials, invalid_client.
    Assertions.assertEquals(401, mismatch.statusCode());
    Assertions.assertEquals("invalid_client", mismatch.jsonPath().getString("error"));

    Map<String, String> nested = spaRequest();
    nested.put("request_uri", "urn:ietf:params:oauth:request_uri:x");
    Assertions.assertEquals(400, push(nested, null).statusCode());

    Map<String, String> foreignRedirect = spaRequest();
    foreignRedirect.put("redirect_uri", "http://evil.example/steal");
    Assertions.assertEquals(400, push(foreignRedirect, null).statusCode());

    Map<String, String> plain = spaRequest();
    plain.put("code_challenge_method", "plain");
    Assertions.assertEquals(400, push(plain, null).statusCode());

    Map<String, String> withoutPkce = spaRequest();
    withoutPkce.remove("code_challenge");
    withoutPkce.remove("code_challenge_method");
    Assertions.assertEquals("invalid_request",
        push(withoutPkce, null).jsonPath().getString("error"));
  }

  @Test
  void push_ofAConfidentialClient_neverStoresItsSecret() {
    Map<String, String> params = directRequest();
    params.put("client_secret", OidcTestFixtures.CLIENT_SECRET);

    Response response = push(params, null);

    Assertions.assertEquals(201, response.statusCode(), response.getBody().asString());
    Map<String, String> stored = parRequestGateway
        .findByUri(response.jsonPath().getString("request_uri"), OidcTestFixtures.TENANT)
        .orElseThrow().getParams();
    Assertions.assertFalse(stored.containsKey("client_secret"));
  }

  @Test
  void clientThatRequiresPar_cannotAuthorizeDirectly() {
    clientStoreGateway.setRequirePar(true);

    assertErrorRedirect(authorize(directRequest(), null), "invalid_request");

    Map<String, String> pushed = directRequest();
    Response response = push(pushed,
        OidcFlowClient.basic(OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET));
    Assertions.assertEquals(201, response.statusCode(), response.getBody().asString());
    Assertions.assertEquals(200, authorize(Map.of("client_id", OidcTestFixtures.CLIENT_ID,
        "request_uri", response.jsonPath().getString("request_uri")), null).statusCode());
  }

  @Test
  void state_withReservedCharacters_comesBackIntact() {
    Map<String, String> query = directRequest();
    query.put("state", "a b&c=d/é");

    Response done = loginWith(query, null);

    Assertions.assertTrue(done.getHeader("Location").contains("&state=a+b%26c%3Dd%2F%C3%A9&iss="),
        done.getHeader("Location"));
  }

  @Test
  void logoutGetAndInvalidPostKeepTheExistingSession() {
    Response login = loginWith(directRequest(), null);
    Assertions.assertEquals(302, login.statusCode());
    String session = login.getCookie("AUTH_SESSION_ID");
    Assertions.assertNotNull(session);
    String logout = "/oauth/openid/" + OidcTestFixtures.TENANT + "/logout";
    Response confirmation = RestAssured.given().cookie("AUTH_SESSION_ID", session).get(logout);
    Assertions.assertEquals(200, confirmation.statusCode());
    Assertions.assertNotNull(confirmation.getCookie("LOGOUT_CSRF"));
    Response rejected = RestAssured.given().contentType(ContentType.URLENC)
        .cookie("AUTH_SESSION_ID", session).formParam("csrf", "invalid").post(logout);
    Assertions.assertEquals(403, rejected.statusCode());
    Assertions.assertEquals(200,
        RestAssured.given().cookie("AUTH_SESSION_ID", session).get(logout).statusCode());
  }

  @Test
  void logoutConfirmationPostsWithCsrfAndClearsSession() {
    Response login = loginWith(directRequest(), null);
    Assertions.assertEquals(302, login.statusCode());
    String session = login.getCookie("AUTH_SESSION_ID");
    String logout = "/oauth/openid/" + OidcTestFixtures.TENANT + "/logout";
    String csrf =
        RestAssured.given().cookie("AUTH_SESSION_ID", session).get(logout).getCookie("LOGOUT_CSRF");
    Response done =
        RestAssured.given().contentType(ContentType.URLENC).cookie("AUTH_SESSION_ID", session)
            .cookie("LOGOUT_CSRF", csrf).formParam("csrf", csrf).post(logout);
    Assertions.assertEquals(200, done.statusCode(), done.getBody().asString());
  }

  // ── PKCE (1.11) ───────────────────────────────────────────────────────────

  @Test
  void pkce_onlyS256_andRequiredForPublicClients() {
    Map<String, String> noMethod = directRequest();
    noMethod.put("code_challenge", OidcTestFixtures.CODE_CHALLENGE);
    assertErrorRedirect(authorize(noMethod, null), "invalid_request");

    Map<String, String> plain = new LinkedHashMap<>(noMethod);
    plain.put("code_challenge_method", "plain");
    assertErrorRedirect(authorize(plain, null), "invalid_request");

    Map<String, String> spaWithout = directRequest();
    spaWithout.put("client_id", OidcTestFixtures.SPA_CLIENT_ID);
    assertErrorRedirect(authorize(spaWithout, null), "invalid_request");
  }

  // ── hints (2.7) ───────────────────────────────────────────────────────────

  @Test
  void loginHint_isEscapedInTheForm() {
    Map<String, String> query = directRequest();
    query.put("login_hint", "\"><script>alert(1)</script>");

    String body = authorize(query, null).getBody().asString();

    Assertions.assertFalse(body.contains("<script>alert(1)</script>"));
    Assertions.assertTrue(body.contains("&lt;script&gt;"));
  }

  private String idToken(String subject, String audience) {
    return tokenSigner.sign(OidcTestFixtures.TENANT, JWT.create().withIssuer(issuer())
        .withSubject(subject).withAudience(audience).withClaim("azp", audience),
        Instant.now().minusSeconds(60));
  }

  private String sessionCookie() {
    String cookie = client.extractAuthSessionCookie(loginWith(directRequest(), null));
    Assertions.assertNotNull(cookie);
    return cookie;
  }

  private static Response silent(String cookie, String hint) {
    Map<String, String> query = directRequest();
    query.put("prompt", "none");
    query.put("id_token_hint", hint);
    return RestAssured.given().redirects().follow(false).cookie("AUTH_SESSION_ID", cookie)
        .queryParams(query).get(AUTH);
  }

  @Test
  void idTokenHint_ofTheSessionUser_reusesTheSession_evenIfExpired() {
    Response response =
        silent(sessionCookie(), idToken(OidcTestFixtures.USERNAME, OidcTestFixtures.CLIENT_ID));

    Assertions.assertEquals(302, response.statusCode());
    Assertions.assertTrue(response.getHeader("Location").contains("code="));
  }

  @Test
  void idTokenHint_ofAnotherUser_neverReusesTheSession() {
    assertErrorRedirect(
        silent(sessionCookie(), idToken("bob@example.com", OidcTestFixtures.CLIENT_ID)),
        "login_required");
  }

  @Test
  void idTokenHint_invalidOrForAnotherClient_isInvalidRequest() {
    String cookie = sessionCookie();
    assertErrorRedirect(silent(cookie, "not-a-jwt"), "invalid_request");
    assertErrorRedirect(silent(cookie, idToken(OidcTestFixtures.USERNAME, "other-client")),
        "invalid_request");
  }

  // ── levels of assurance (3.10) ────────────────────────────────────────────

  @Test
  void acrValues_multiFactor_overASingleFactorSession() {
    String cookie = sessionCookie();
    Map<String, String> query = directRequest();
    query.put("acr_values", "unknown urn:lugh:acr:mfa");

    Map<String, String> silentQuery = new LinkedHashMap<>(query);
    silentQuery.put("prompt", "none");
    assertErrorRedirect(RestAssured.given().redirects().follow(false)
        .cookie("AUTH_SESSION_ID", cookie).queryParams(silentQuery).get(AUTH),
        "interaction_required");

    Response stepUp = RestAssured.given().redirects().follow(false)
        .cookie("AUTH_SESSION_ID", cookie).queryParams(query).get(AUTH);
    Assertions.assertEquals(200, stepUp.statusCode());
    Assertions.assertTrue(stepUp.getBody().asString().contains("mfa_code"),
        "Step-up asks for the second factor");
  }

  // ── signed request objects (3.3) ──────────────────────────────────────────

  private RSAPrivateKey registerRsaKey() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    KeyPair pair = generator.generateKeyPair();
    RSAPublicKey pub = (RSAPublicKey) pair.getPublic();
    Base64.Encoder url = Base64.getUrlEncoder().withoutPadding();
    clientStoreGateway
        .setJwksJson("{\"keys\":[{\"kty\":\"RSA\",\"kid\":\"k1\",\"use\":\"sig\"," + "\"n\":\""
            + url.encodeToString(unsigned(pub.getModulus().toByteArray())) + "\"," + "\"e\":\""
            + url.encodeToString(unsigned(pub.getPublicExponent().toByteArray())) + "\"}]}");
    return (RSAPrivateKey) pair.getPrivate();
  }

  private static byte[] unsigned(byte[] bytes) {
    return bytes.length > 1 && bytes[0] == 0 ? java.util.Arrays.copyOfRange(bytes, 1, bytes.length)
        : bytes;
  }

  private static String issuer() {
    return RestAssured
        .get("/oauth/openid/" + OidcTestFixtures.TENANT + "/.well-known/openid-configuration")
        .jsonPath().getString("issuer");
  }

  private static String requestObject(RSAPrivateKey key, String audience) {
    Map<String, Object> claims = new HashMap<>(directRequest());
    return JWT.create().withKeyId("k1").withIssuer(OidcTestFixtures.CLIENT_ID)
        .withAudience(audience).withJWTId(UUID.randomUUID().toString())
        .withExpiresAt(Date.from(Instant.now().plusSeconds(300))).withPayload(claims)
        .sign(Algorithm.RSA256(null, key));
  }

  @Test
  void requestObject_isVerified_keptAsAPushedRequest_andUsedOnce() throws Exception {
    String jwt = requestObject(registerRsaKey(), issuer());
    Map<String, String> query =
        Map.of("client_id", OidcTestFixtures.CLIENT_ID, "request", jwt, "scope", "admin");

    Response first = authorize(query, null);
    Assertions.assertEquals(303, first.statusCode(), first.getBody().asString());
    String location = first.getHeader("Location");
    Assertions.assertTrue(location.contains("request_uri=urn"), location);
    String binding = first.getCookie(BINDING);
    Assertions.assertNotNull(binding);
    Assertions.assertEquals(200, RestAssured.given().redirects().follow(false)
        .urlEncodingEnabled(false).cookie(BINDING, binding).get(location).statusCode());

    Response replay = authorize(query, null);
    Assertions.assertEquals(400, replay.statusCode());
    Assertions.assertEquals("invalid_request_object", replay.jsonPath().getString("error"));
  }

  @Test
  void requestObject_forAnotherAudience_orUnsigned_isRejected() throws Exception {
    RSAPrivateKey key = registerRsaKey();
    Response foreign = authorize(Map.of("client_id", OidcTestFixtures.CLIENT_ID, "request",
        requestObject(key, "https://other.example")), null);
    Assertions.assertEquals("invalid_request_object", foreign.jsonPath().getString("error"));

    String unsigned =
        JWT.create().withIssuer(OidcTestFixtures.CLIENT_ID).withAudience(issuer()).withJWTId("x")
            .withExpiresAt(Date.from(Instant.now().plusSeconds(300))).sign(Algorithm.none());
    Response none =
        authorize(Map.of("client_id", OidcTestFixtures.CLIENT_ID, "request", unsigned), null);
    Assertions.assertEquals("invalid_request_object", none.jsonPath().getString("error"));
  }

  @Test
  void requestUri_byReference_isNotSupported() {
    Response response = authorize(Map.of("client_id", OidcTestFixtures.CLIENT_ID, "request_uri",
        "https://client.example/request.jwt"), null);

    Assertions.assertEquals(400, response.statusCode());
    Assertions.assertEquals("request_uri_not_supported", response.jsonPath().getString("error"));
  }
}
