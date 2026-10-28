package net.civeira.phylax.integrationtest.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import net.civeira.phylax.common.crypto.SecretDigester;

/**
 * DCR endpoints over HTTP and MariaDB (V.1, V.3): RFC 7591/7592 responses, keys and algorithms
 * validated against what discovery announces, credentials of the tenant only and metadata errors
 * that spend nothing.
 */
@Tag("mariadb")
@QuarkusTest
@TestProfile(MariaDbFlowProfile.class)
@EnabledIfEnvironmentVariable(named = "V1_MARIADB_URL", matches = ".+")
class DynamicClientHttpMariaDbTest {

  private static final Map<String, Object> JWKS = Map.of("keys",
      List.of(Map.of("kty", "EC", "crv", "P-256", "kid", "k1", "x",
          "f83OJ3D2xF1Bg8vub9tLe1gHMzV76e8Tus9uPHvRVEU", "y",
          "x_FEzRu9m36HLN_tue659LNpXW6pCyStikYjKIWI5a0")));

  private final String tenantA = "dcr-a-" + UUID.randomUUID().toString().substring(0, 8);
  private final String tenantB = "dcr-b-" + UUID.randomUUID().toString().substring(0, 8);
  private String tenantUidA;

  @Inject
  DataSource dataSource;
  @Inject
  SecretDigester digester;

  @BeforeEach
  void setUp() throws SQLException {
    tenantUidA = tenant(tenantA);
    tenant(tenantB);
  }

  private String tenant(String name) throws SQLException {
    String uid = UUID.randomUUID().toString();
    try (Connection c = dataSource.getConnection()) {
      try (PreparedStatement ps = c.prepareStatement("INSERT INTO access_tenant"
          + " (uid, version, domain, enabled, mark_for_delete, name, root)"
          + " VALUES (?, 0, ?, 1, 0, ?, 0)")) {
        ps.setString(1, uid);
        ps.setString(2, name + ".example.invalid");
        ps.setString(3, name);
        ps.execute();
      }
      try (PreparedStatement ps = c.prepareStatement("INSERT INTO access_tenant_config (uid,"
          + " version, tenant, dynamic_registration_policy, allow_password_grant, force_mfa,"
          + " invitation_expiry_days, magic_link_expiry_minutes,"
          + " refresh_token_max_lifetime_seconds, refresh_token_ttl_seconds,"
          + " require_email_verification, require_pushed_authorization_requests,"
          + " session_sso_ttl_seconds)"
          + " VALUES (?, 0, ?, 'token-required', 0, 0, 7, 15, 86400, 3600, 0, 0, 3600)")) {
        ps.setString(1, "cfg-" + uid);
        ps.setString(2, uid);
        ps.execute();
      }
    }
    return uid;
  }

  private String initialToken(int maxUses, Instant expiresAt) throws SQLException {
    String token = UUID.randomUUID().toString();
    try (Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO access_dcr_initial_access_token"
            + " (uid, version, tenant, token_hash, max_uses, uses, expires_at)"
            + " VALUES (?, 0, ?, ?, ?, 0, ?)")) {
      ps.setString(1, token);
      ps.setString(2, tenantUidA);
      ps.setString(3, digester.digest(token));
      ps.setInt(4, maxUses);
      ps.setTimestamp(5, Timestamp.from(expiresAt));
      ps.execute();
    }
    return token;
  }

  private int uses(String token) throws SQLException {
    try (Connection c = dataSource.getConnection();
        PreparedStatement ps =
            c.prepareStatement("SELECT uses FROM access_dcr_initial_access_token WHERE uid = ?")) {
      ps.setString(1, token);
      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        return rs.getInt(1);
      }
    }
  }

  private static Response register(String tenant, String token, Map<String, Object> body) {
    return RestAssured.given().contentType(ContentType.JSON)
        .header("Authorization", "Bearer " + token).body(body)
        .post("/oauth/openid/" + tenant + "/connect");
  }

  private static Instant later() {
    return Instant.now().plusSeconds(3600);
  }

  private static void assertError(Response response, int status, String error) {
    Assertions.assertEquals(status, response.statusCode(), response.asString());
    Assertions.assertEquals(error, response.jsonPath().getString("error"));
    Assertions.assertEquals("no-store", response.getHeader("Cache-Control"));
  }

  @Test
  void privateKeyJwtClient_isRegisteredWithItsKeys_andReadWithoutSecret() throws Exception {
    String token = initialToken(1, later());

    Response created = register(tenantA, token,
        Map.of("redirect_uris", List.of("https://rp.example/cb"), "token_endpoint_auth_method",
            "private_key_jwt", "jwks", JWKS, "token_endpoint_auth_signing_alg", "ES256",
            "request_object_signing_alg", "ES256", "id_token_signed_response_alg", "RS256",
            "dpop_bound_access_tokens", true));

    Assertions.assertEquals(201, created.statusCode(), created.asString());
    Assertions.assertEquals("no-store", created.getHeader("Cache-Control"));
    Assertions.assertNull(created.jsonPath().get("client_secret"));
    Assertions.assertEquals(JWKS, created.jsonPath().getMap("jwks"));
    Assertions.assertEquals("public", created.jsonPath().getString("subject_type"));
    Assertions.assertEquals(true, created.jsonPath().getBoolean("dpop_bound_access_tokens"));
    Assertions.assertEquals(1, uses(token));

    Response read = RestAssured.given()
        .header("Authorization",
            "Bearer " + created.jsonPath().getString("registration_access_token"))
        .get(created.jsonPath().getString("registration_client_uri").replaceFirst("^https?://[^/]+",
            ""));
    Assertions.assertEquals(200, read.statusCode(), read.asString());
    Assertions.assertEquals("ES256", read.jsonPath().getString("request_object_signing_alg"));
    Assertions.assertEquals(JWKS, read.jsonPath().getMap("jwks"));
    Assertions.assertFalse(read.asString().contains("client_secret"));
  }

  @Test
  void invalidKeysOrAlgorithms_areInvalidClientMetadata_andSpendNoUse() throws Exception {
    String token = initialToken(1, later());
    List<Map<String, Object>> invalid = List.of(
        Map.of("redirect_uris", List.of("https://rp.example/cb"), "token_endpoint_auth_method",
            "private_key_jwt"),
        Map.of("redirect_uris", List.of("https://rp.example/cb"), "jwks", JWKS, "jwks_uri",
            "https://rp.example/jwks"),
        Map.of("redirect_uris", List.of("https://rp.example/cb"), "id_token_signed_response_alg",
            "HS256"),
        Map.of("redirect_uris", List.of("https://rp.example/cb"), "sector_identifier_uri",
            "https://rp.example/sector.json"),
        Map.of("redirect_uris", List.of("https://rp.example/cb"),
            "tls_client_certificate_bound_access_tokens", true),
        Map.of("redirect_uris", List.of("https://rp.example/cb"), "dpop_bound_access_tokens",
            "yes"));

    for (Map<String, Object> body : invalid) {
      assertError(register(tenantA, token, body), 400, "invalid_client_metadata");
    }
    Assertions.assertEquals(0, uses(token));
  }

  @Test
  void initialToken_ofAnotherTenant_expiredOrExhausted_isInvalidToken() throws Exception {
    Map<String, Object> body = Map.of("redirect_uris", List.of("https://rp.example/cb"));
    String token = initialToken(1, later());

    Response foreign = register(tenantB, token, body);
    assertError(foreign, 401, "invalid_token");
    Assertions.assertTrue(foreign.getHeader("WWW-Authenticate").startsWith("Bearer"));
    assertError(register(tenantA, initialToken(1, Instant.now().minusSeconds(60)), body), 401,
        "invalid_token");

    Assertions.assertEquals(201, register(tenantA, token, body).statusCode());
    assertError(register(tenantA, token, body), 401, "invalid_token");
  }

  @Test
  void discovery_announcesTheEndpointAndWhatRegistrationAccepts() {
    Response discovery =
        RestAssured.get("/oauth/openid/" + tenantA + "/.well-known/openid-configuration");
    Assertions.assertEquals(200, discovery.statusCode());
    Assertions.assertTrue(
        discovery.jsonPath().getString("registration_endpoint").endsWith(tenantA + "/connect"));
    Assertions.assertTrue(discovery.jsonPath().getList("token_endpoint_auth_methods_supported")
        .contains("private_key_jwt"));
    Assertions.assertEquals(List.of("RS256"),
        discovery.jsonPath().getList("id_token_signing_alg_values_supported"));
    Assertions.assertEquals(List.of("RS256", "ES256"),
        discovery.jsonPath().getList("request_object_signing_alg_values_supported"));
  }
}
