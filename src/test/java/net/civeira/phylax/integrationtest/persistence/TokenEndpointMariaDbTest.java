package net.civeira.phylax.integrationtest.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import net.civeira.phylax.features.oauth.tokensecurity.domain.gateway.TokenSigner;
import net.civeira.phylax.integrationtest.features.oauth.client.OidcFlowClient;
import net.civeira.phylax.integrationtest.features.oauth.fixtures.OidcTestFixtures;

/**
 * Acceptance of J-01 over HTTP and MariaDB (V.1): the negative and concurrent code and refresh
 * scenarios go through {@code POST /token} while codes, consumed JTIs, revoked families and
 * sessions live in a throwaway MariaDB schema ({@link MariaDbFlowProfile}).
 */
@Tag("mariadb")
@QuarkusTest
@TestProfile(MariaDbFlowProfile.class)
@EnabledIfEnvironmentVariable(named = "V1_MARIADB_URL", matches = ".+")
class TokenEndpointMariaDbTest {

  private static final int CONCURRENT = 8;

  private final OidcFlowClient client = new OidcFlowClient();

  @Inject
  DataSource dataSource;
  @Inject
  TokenSigner tokenSigner;

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

  private Response rotate(String refresh) {
    return client.refreshToken(OidcTestFixtures.TENANT, refresh, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET);
  }

  private String passwordRefresh() {
    Response auth = client.passwordGrant(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET, OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD,
        "openid profile");
    Assertions.assertEquals(200, auth.statusCode(), auth.asString());
    return auth.jsonPath().getString("refresh_token");
  }

  private Map<String, Object> claims(String token) {
    return tokenSigner.verifyTokenPayload(OidcTestFixtures.TENANT, token);
  }

  private int count(String sql, String value) {
    try (Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, value);
      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        return rs.getInt(1);
      }
    } catch (SQLException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private int storedCodes(String code) {
    return count("SELECT COUNT(*) FROM _oauth_temporal_codes WHERE code = ?", code);
  }

  private boolean revoked(String jti) {
    return count("SELECT COUNT(*) FROM _oauth_revoked_jti WHERE jti = ?", jti) == 1;
  }

  private static long successes(List<Response> responses) {
    return responses.stream().filter(r -> r.statusCode() == 200).count();
  }

  private static void assertInvalidGrant(Response response) {
    Assertions.assertEquals(400, response.statusCode(), response.asString());
    Assertions.assertEquals("invalid_grant", response.jsonPath().getString("error"));
    Assertions.assertEquals("no-store", response.getHeader("Cache-Control"));
  }

  // ── authorization code ────────────────────────────────────────────────────

  @Test
  void code_redeemedConcurrently_hasASingleWinner_andLeavesNoRow() throws Exception {
    String code = code();
    Assertions.assertEquals(1, storedCodes(code), "The code is stored in MariaDB");

    List<Response> responses =
        Concurrently.run(CONCURRENT, () -> exchange(OidcTestFixtures.TENANT, code));

    Assertions.assertEquals(1, successes(responses));
    responses.stream().filter(r -> r.statusCode() != 200)
        .forEach(TokenEndpointMariaDbTest::assertInvalidGrant);
    Assertions.assertEquals(0, storedCodes(code));
  }

  @Test
  void code_ofAnotherClientOrTenant_isRejected_andStaysInTheDatabase() {
    String code = code();

    assertInvalidGrant(client.exchangeCodeAsConfidential(OidcTestFixtures.TENANT, code,
        OidcTestFixtures.REDIRECT_URI, OidcTestFixtures.WEB_CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET));
    assertInvalidGrant(exchange(OidcTestFixtures.OTHER_TENANT, code));
    assertInvalidGrant(client.exchangeCode(OidcTestFixtures.TENANT, code, "http://localhost/other",
        OidcTestFixtures.CLIENT_ID));
    Assertions.assertEquals(1, storedCodes(code), "Rejected requests do not spend the code");

    Assertions.assertEquals(200, exchange(OidcTestFixtures.TENANT, code).statusCode());
    assertInvalidGrant(exchange(OidcTestFixtures.TENANT, code));
  }

  // ── refresh token ─────────────────────────────────────────────────────────

  @Test
  void refresh_rotatedConcurrently_hasASingleWinner_andRevokesTheFamilyInTheDatabase()
      throws Exception {
    String refresh = passwordRefresh();
    String fid = String.valueOf(claims(refresh).get("fid"));

    List<Response> responses = Concurrently.run(CONCURRENT, () -> rotate(refresh));

    Assertions.assertEquals(1, successes(responses));
    Assertions.assertTrue(revoked(String.valueOf(claims(refresh).get("jti"))),
        "The rotated JTI is consumed in MariaDB");
    Assertions.assertTrue(revoked(fid), "The replay revoked the family in MariaDB");
    String winner = responses.stream().filter(r -> r.statusCode() == 200).findFirst().orElseThrow()
        .jsonPath().getString("refresh_token");
    assertInvalidGrant(rotate(winner));
  }

  @Test
  void refresh_ofAnotherClientOrTenant_isRejected_andNotConsumed() {
    String refresh = passwordRefresh();

    assertInvalidGrant(client.token(OidcTestFixtures.TENANT,
        OidcFlowClient.basic(OidcTestFixtures.WEB_CLIENT_ID, OidcTestFixtures.CLIENT_SECRET),
        Map.of("grant_type", "refresh_token", "refresh_token", refresh)));
    assertInvalidGrant(client.refreshToken(OidcTestFixtures.OTHER_TENANT, refresh,
        OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET));
    Assertions.assertFalse(revoked(String.valueOf(claims(refresh).get("jti"))));

    Assertions.assertEquals(200, rotate(refresh).statusCode());
  }

  @Test
  void refresh_ofAnEndedSession_isRejected() throws SQLException {
    Response exchange = exchange(OidcTestFixtures.TENANT, code());
    Assertions.assertEquals(200, exchange.statusCode(), exchange.asString());
    String refresh = exchange.jsonPath().getString("refresh_token");
    String jti = String.valueOf(claims(refresh).get("jti"));
    Assertions.assertEquals(1,
        count("SELECT COUNT(*) FROM _oauth_session_token WHERE refresh_jti = ?", jti),
        "The code exchange registered its tokens on the session in MariaDB");

    try (Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("DELETE FROM _oauth_session WHERE session ="
            + " (SELECT session FROM _oauth_session_token WHERE refresh_jti = ?)")) {
      ps.setString(1, jti);
      Assertions.assertEquals(1, ps.executeUpdate());
    }

    assertInvalidGrant(rotate(refresh));
  }
}
