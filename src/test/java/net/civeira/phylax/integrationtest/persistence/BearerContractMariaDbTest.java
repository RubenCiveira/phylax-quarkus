package net.civeira.phylax.integrationtest.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationMode;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.features.oauth.tokensecurity.domain.gateway.TokenRevocationGateway;
import net.civeira.phylax.features.oauth.tokensecurity.domain.gateway.TokenSigner;
import net.civeira.phylax.integrationtest.features.oauth.client.OidcFlowClient;
import net.civeira.phylax.integrationtest.features.oauth.fixtures.OidcTestFixtures;
import net.civeira.phylax.integrationtest.features.oauth.scenario.ScenarioLoginGateway;

/** J-04: the actual HTTP bearer consumers share the MariaDB JTI and revocation state. */
@Tag("mariadb")
@QuarkusTest
@TestProfile(MariaDbFlowProfile.class)
@EnabledIfEnvironmentVariable(named = "V1_MARIADB_URL", matches = ".+")
class BearerContractMariaDbTest {
  private final OidcFlowClient client = new OidcFlowClient();

  @Inject
  DataSource dataSource;
  @Inject
  TokenSigner signer;
  @Inject
  TokenRevocationGateway revocations;
  @Inject
  ScenarioLoginGateway loginGateway;

  private Response password(String tenant, String scope) {
    Response response =
        client.passwordGrant(tenant, OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET,
            OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD, scope);
    Assertions.assertEquals(200, response.statusCode(), response.asString());
    return response;
  }

  private Response introspect(String tenant, String token) {
    return client.introspect(tenant, token, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET);
  }

  private Response userinfo(String tenant, String token, boolean post) {
    var request = RestAssured.given().header("Authorization", "Bearer " + token);
    String path = "/oauth/openid/" + tenant + "/userinfo";
    return post ? request.post(path) : request.get(path);
  }

  private String jti(String token) {
    return String.valueOf(signer.verifyTokenPayload(OidcTestFixtures.TENANT, token).get("jti"));
  }

  private int stored(String jti) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT count(*) FROM _oauth_session_token WHERE jti = ? AND revoked_at IS NULL")) {
      stmt.setString(1, jti);
      try (ResultSet rows = stmt.executeQuery()) {
        rows.next();
        return rows.getInt(1);
      }
    } catch (SQLException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Test
  void passwordGrantIsIntrospectableWithoutBrowserSessionAndRevocationTakesEffectImmediately() {
    Response issued = password(OidcTestFixtures.TENANT, OidcTestFixtures.SCOPE);
    String token = issued.jsonPath().getString("access_token");
    String jti = jti(token);
    Assertions.assertEquals(1, stored(jti), "standalone grant persists its access JTI");

    Assertions
        .assertTrue(introspect(OidcTestFixtures.TENANT, token).jsonPath().getBoolean("active"));
    Assertions.assertEquals(200, userinfo(OidcTestFixtures.TENANT, token, false).statusCode());
    Response post = userinfo(OidcTestFixtures.TENANT, token, true);
    Assertions.assertEquals(200, post.statusCode(), post.asString());
    Assertions.assertEquals(OidcTestFixtures.USERNAME, post.jsonPath().getString("sub"));

    revocations.revokeJti(jti, OidcTestFixtures.TENANT, "access", Instant.now().plusSeconds(600));
    Assertions
        .assertFalse(introspect(OidcTestFixtures.TENANT, token).jsonPath().getBoolean("active"));
    Response revoked = userinfo(OidcTestFixtures.TENANT, token, false);
    Assertions.assertEquals(401, revoked.statusCode());
    Assertions.assertTrue(revoked.getHeader("WWW-Authenticate").startsWith("Bearer"));
  }

  @Test
  void wrongTenantAndOtherPurposesNeverIntrospectAsAccess() {
    Response issued = password(OidcTestFixtures.TENANT, OidcTestFixtures.SCOPE);
    String access = issued.jsonPath().getString("access_token");
    Assertions.assertFalse(
        introspect(OidcTestFixtures.OTHER_TENANT, access).jsonPath().getBoolean("active"));
    Assertions.assertFalse(
        introspect(OidcTestFixtures.TENANT, issued.jsonPath().getString("refresh_token")).jsonPath()
            .getBoolean("active"));
    Assertions
        .assertFalse(introspect(OidcTestFixtures.TENANT, issued.jsonPath().getString("id_token"))
            .jsonPath().getBoolean("active"));
    Assertions.assertEquals(401,
        userinfo(OidcTestFixtures.TENANT, issued.jsonPath().getString("refresh_token"), false)
            .statusCode());
  }

  @Test
  void clientCredentialsIsIntrospectableAndCannotOpenUserinfo() {
    Response issued = client.token(OidcTestFixtures.TENANT,
        OidcFlowClient.basic(OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET),
        Map.of("grant_type", "client_credentials", "scope", "orders:read"));
    Assertions.assertEquals(200, issued.statusCode(), issued.asString());
    String token = issued.jsonPath().getString("access_token");
    Assertions.assertEquals(1, stored(jti(token)));
    Assertions
        .assertTrue(introspect(OidcTestFixtures.TENANT, token).jsonPath().getBoolean("active"));
    Assertions.assertEquals(401, userinfo(OidcTestFixtures.TENANT, token, false).statusCode());
  }

  @Test
  void audienceOfResourceServerDoesNotConstrainIssuerIntrospection() {
    Response issued = client.token(OidcTestFixtures.TENANT,
        OidcFlowClient.basic(OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET),
        Map.of("grant_type", "password", "username", OidcTestFixtures.USERNAME, "password",
            OidcTestFixtures.PASSWORD, "scope", OidcTestFixtures.SCOPE, "audience",
            OidcTestFixtures.API_AUDIENCE));
    Assertions.assertEquals(200, issued.statusCode(), issued.asString());
    String token = issued.jsonPath().getString("access_token");
    Assertions
        .assertTrue(introspect(OidcTestFixtures.TENANT, token).jsonPath().getBoolean("active"));
  }

  @Test
  void userinfoUsesPersistedEmailRatherThanUsernameAndOnlyPermittedScopes() throws SQLException {
    String uid = UUID.randomUUID().toString();
    String email = "actual-user@example.invalid";
    boolean verified = true;
    try (Connection connection = dataSource.getConnection();
        PreparedStatement stmt =
            connection.prepareStatement("SELECT uid FROM access_tenant LIMIT 1");
        ResultSet row = stmt.executeQuery()) {
      Assertions.assertTrue(row.next());
      String protectedPassword;
      try (
          PreparedStatement existing =
              connection.prepareStatement("SELECT password FROM access_user LIMIT 1");
          ResultSet passwords = existing.executeQuery()) {
        Assertions.assertTrue(passwords.next());
        protectedPassword = passwords.getString(1);
      }
      try (PreparedStatement insert = connection.prepareStatement(
          "INSERT INTO access_user (uid, version, tenant, name, password, email, email_verified)"
              + " VALUES (?, 0, ?, ?, ?, ?, 1)")) {
        insert.setString(1, uid);
        insert.setString(2, row.getString(1));
        insert.setString(3, "profile-backed");
        insert.setString(4, protectedPassword);
        insert.setString(5, email);
        insert.executeUpdate();
      }
    }
    AuthenticationData data = new AuthenticationData();
    data.setUid(uid);
    data.setUsername(OidcTestFixtures.USERNAME);
    data.setTenant(OidcTestFixtures.TENANT);
    data.setMode(AuthenticationMode.PASSWORD);
    data.setAmr(List.of("pwd"));
    data.setTime(Instant.now());
    loginGateway.whenValidate(() -> AuthenticationResult.right(data));
    try {
      String token =
          password(OidcTestFixtures.TENANT, "openid email").jsonPath().getString("access_token");
      Response get = userinfo(OidcTestFixtures.TENANT, token, false);
      Response post = userinfo(OidcTestFixtures.TENANT, token, true);
      Assertions.assertEquals(200, get.statusCode(), get.asString());
      Assertions.assertEquals(get.jsonPath().getMap("$"), post.jsonPath().getMap("$"));
      Assertions.assertEquals(email, get.jsonPath().getString("email"));
      Assertions.assertEquals(verified, get.jsonPath().getBoolean("email_verified"));
      Assertions.assertNull(get.jsonPath().getString("name"));
    } finally {
      loginGateway.reset();
    }
  }
}
