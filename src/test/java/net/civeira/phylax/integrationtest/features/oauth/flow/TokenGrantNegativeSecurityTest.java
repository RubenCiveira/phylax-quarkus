package net.civeira.phylax.integrationtest.features.oauth.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import net.civeira.phylax.integrationtest.features.oauth.client.OidcFlowClient;
import net.civeira.phylax.integrationtest.features.oauth.fixtures.OidcTestFixtures;

/**
 * Negative security cases of the code and refresh grants (1.10): codes and refresh tokens of
 * another client or tenant, double and concurrent redemption, and unregistered redirects. The
 * concurrent cases run against the in-memory stores, whose consumption is atomic like the SQL one;
 * the MariaDB run belongs to the V.1 harness.
 */
@Tag("oidc-flow")
@QuarkusTest
class TokenGrantNegativeSecurityTest extends OidcIntegrationTestBase {

  private static final int CONCURRENT = 8;

  private String code() {
    String code = client.extractAuthCode(client.submitLogin(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD, null));
    Assertions.assertNotNull(code);
    return code;
  }

  private Response exchange(String tenant, String code) {
    return client.exchangeCode(tenant, code, OidcTestFixtures.REDIRECT_URI,
        OidcTestFixtures.CLIENT_ID);
  }

  private String refresh() {
    Response auth = client.passwordGrant(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET, OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD,
        "openid profile");
    Assertions.assertEquals(200, auth.statusCode());
    return auth.jsonPath().getString("refresh_token");
  }

  private Response rotate(String refresh) {
    return client.refreshToken(OidcTestFixtures.TENANT, refresh, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET);
  }

  private static List<Response> concurrently(Callable<Response> call) throws Exception {
    ExecutorService pool = Executors.newFixedThreadPool(CONCURRENT);
    try {
      CountDownLatch start = new CountDownLatch(1);
      List<Future<Response>> futures = new ArrayList<>();
      for (int i = 0; i < CONCURRENT; i++) {
        futures.add(pool.submit(() -> {
          start.await();
          return call.call();
        }));
      }
      start.countDown();
      List<Response> responses = new ArrayList<>();
      for (Future<Response> future : futures) {
        responses.add(future.get());
      }
      return responses;
    } finally {
      pool.shutdownNow();
    }
  }

  private static long successes(List<Response> responses) {
    return responses.stream().filter(r -> r.statusCode() == 200).count();
  }

  // ── authorization code ────────────────────────────────────────────────────

  @Test
  void code_redeemedTwice_onlyTheFirstGetsTokens() {
    String code = code();

    Assertions.assertEquals(200, exchange(OidcTestFixtures.TENANT, code).statusCode());
    Response second = exchange(OidcTestFixtures.TENANT, code);
    Assertions.assertEquals(400, second.statusCode());
    Assertions.assertEquals("invalid_grant", second.jsonPath().getString("error"));
  }

  @Test
  void code_redeemedConcurrently_hasASingleWinner() throws Exception {
    String code = code();

    List<Response> responses = concurrently(() -> exchange(OidcTestFixtures.TENANT, code));

    Assertions.assertEquals(1, successes(responses));
    responses.stream().filter(r -> r.statusCode() != 200)
        .forEach(r -> Assertions.assertEquals("invalid_grant", r.jsonPath().getString("error")));
  }

  @Test
  void code_ofAnotherClient_isInvalidGrant_andNotSpent() {
    String code = code();

    Response other = client.exchangeCodeAsConfidential(OidcTestFixtures.TENANT, code,
        OidcTestFixtures.REDIRECT_URI, OidcTestFixtures.WEB_CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET);
    Assertions.assertEquals(400, other.statusCode());
    Assertions.assertEquals("invalid_grant", other.jsonPath().getString("error"));

    Assertions.assertEquals(200, exchange(OidcTestFixtures.TENANT, code).statusCode());
  }

  @Test
  void code_redeemedInAnotherTenantWhereTheClientExists_isInvalidGrant() {
    String code = code();

    Response other = exchange(OidcTestFixtures.OTHER_TENANT, code);
    Assertions.assertEquals(400, other.statusCode());
    Assertions.assertEquals("invalid_grant", other.jsonPath().getString("error"));

    Assertions.assertEquals(200, exchange(OidcTestFixtures.TENANT, code).statusCode());
  }

  @Test
  void authorize_withUnregisteredRedirect_issuesNoCode() {
    Response login = io.restassured.RestAssured.given().redirects().follow(false)
        .queryParam("client_id", OidcTestFixtures.CLIENT_ID)
        .queryParam("redirect_uri", "http://evil.example/steal").queryParam("response_type", "code")
        .queryParam("scope", OidcTestFixtures.SCOPE).queryParam("state", OidcTestFixtures.STATE)
        .contentType(io.restassured.http.ContentType.URLENC)
        .formParam("username", OidcTestFixtures.USERNAME)
        .formParam("password", OidcTestFixtures.PASSWORD).formParam("csid", OidcTestFixtures.CSID)
        .post("/oauth/openid/" + OidcTestFixtures.TENANT + "/auth");

    Assertions.assertNull(client.extractAuthCode(login));
    String location = login.getHeader("Location");
    Assertions.assertTrue(location == null || !location.contains("evil.example"),
        "Never redirects to an unregistered URI");
  }

  // ── refresh token ─────────────────────────────────────────────────────────

  @Test
  void refresh_stolenByAnotherClient_isInvalidGrant_andNotSpent() {
    String refresh = refresh();

    Response stolen = client.token(OidcTestFixtures.TENANT,
        OidcFlowClient.basic(OidcTestFixtures.WEB_CLIENT_ID, OidcTestFixtures.CLIENT_SECRET),
        Map.of("grant_type", "refresh_token", "refresh_token", refresh));
    Assertions.assertEquals(400, stolen.statusCode());
    Assertions.assertEquals("invalid_grant", stolen.jsonPath().getString("error"));

    Assertions.assertEquals(200, rotate(refresh).statusCode(),
        "The legitimate client keeps its refresh token");
  }

  @Test
  void refresh_ofAnotherTenant_isInvalidGrant() {
    String refresh = refresh();

    Response other = client.refreshToken(OidcTestFixtures.OTHER_TENANT, refresh,
        OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET);
    Assertions.assertEquals(400, other.statusCode());
    Assertions.assertEquals("invalid_grant", other.jsonPath().getString("error"));
  }

  @Test
  void refresh_rotatedConcurrently_hasASingleWinner_andRevokesTheFamily() throws Exception {
    String refresh = refresh();

    List<Response> responses = concurrently(() -> rotate(refresh));

    Assertions.assertEquals(1, successes(responses));
    String rotated = responses.stream().filter(r -> r.statusCode() == 200).findFirst().orElseThrow()
        .jsonPath().getString("refresh_token");
    Response afterReplay = rotate(rotated);
    Assertions.assertEquals(400, afterReplay.statusCode(),
        "The concurrent replay revoked the family, including the winner's new token");
    Assertions.assertEquals("invalid_grant", afterReplay.jsonPath().getString("error"));
  }
}
