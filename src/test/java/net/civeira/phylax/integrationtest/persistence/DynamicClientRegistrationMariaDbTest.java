package net.civeira.phylax.integrationtest.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import net.civeira.phylax.common.crypto.SecretDigester;
import net.civeira.phylax.features.oauth.client.application.usecase.deletedynamicclient.DeleteDynamicClientUsecase;
import net.civeira.phylax.features.oauth.client.application.usecase.readdynamicclient.ReadDynamicClientUsecase;
import net.civeira.phylax.features.oauth.client.application.usecase.registerclient.RegisterClientParams;
import net.civeira.phylax.features.oauth.client.application.usecase.registerclient.RegisterClientResult;
import net.civeira.phylax.features.oauth.client.application.usecase.registerclient.RegisterClientUsecase;
import net.civeira.phylax.features.oauth.client.application.usecase.updatedynamicclient.UpdateDynamicClientUsecase;
import net.civeira.phylax.features.oauth.client.domain.DcrExtensionMetadata;
import net.civeira.phylax.features.oauth.client.domain.DynamicClientData;
import net.civeira.phylax.features.oauth.client.domain.DynamicClientRequest;
import net.civeira.phylax.features.oauth.client.domain.exception.DynamicClientException;
import net.civeira.phylax.features.oauth.client.infrastructure.driven.DcrInitialAccessTokenSqlAdapter;
import net.civeira.phylax.features.oauth.client.infrastructure.driven.DynamicClientAdapter;
import net.civeira.phylax.features.oauth.client.infrastructure.driven.JwtReplaySqlAdapter;
import net.civeira.phylax.features.oauth.client.infrastructure.driven.PostLogoutRedirectSqlStore;
import net.civeira.phylax.features.oauth.tokensecurity.application.SubjectResolver;

/**
 * DCR against MariaDB (V.1 for 2.1, 2.8 and V.3): initial access tokens of the tenant with atomic
 * use count, clients born in the tenant of the path and managed only from it, secrets stored as
 * digests and never read back.
 */
@Tag("mariadb")
@ExtendWith(MariaDbSchema.class)
class DynamicClientRegistrationMariaDbTest {

  private static final String TENANT_A = "tenant-a";
  private static final String TENANT_B = "tenant-b";
  private static final String ISSUER = "https://auth.example.com/oauth/openid/" + TENANT_A;

  private DataSource dataSource;
  private RegisterClientUsecase register;
  private ReadDynamicClientUsecase read;
  private DeleteDynamicClientUsecase delete;
  private UpdateDynamicClientUsecase update;
  private final SecretDigester digester = new SecretDigester("t0:test-digest-pepper", "t0");

  @BeforeEach
  void setUp(DataSource dataSource) throws SQLException {
    this.dataSource = dataSource;
    DynamicClientAdapter clients = new DynamicClientAdapter(dataSource, MariaDbSchema.mapper(),
        digester, new PostLogoutRedirectSqlStore(dataSource),
        new SubjectResolver(Optional.of("test-pairwise-salt")));
    register = new RegisterClientUsecase(clients,
        new DcrInitialAccessTokenSqlAdapter(dataSource, digester));
    read = new ReadDynamicClientUsecase(clients);
    delete = new DeleteDynamicClientUsecase(clients);
    update = new UpdateDynamicClientUsecase(clients);
    try (Connection c = dataSource.getConnection()) {
      c.createStatement().execute("DELETE FROM access_trusted_client_allowed_logout_redirect");
      c.createStatement().execute("DELETE FROM access_trusted_client_allowed_redirect");
      c.createStatement().execute("DELETE FROM access_trusted_client");
      c.createStatement().execute("DELETE FROM access_dcr_initial_access_token");
      c.createStatement().execute("DELETE FROM access_tenant_config");
      c.createStatement().execute("DELETE FROM access_tenant");
      tenant(c, "uid-a", TENANT_A, "token-required");
      tenant(c, "uid-b", TENANT_B, "token-required");
    }
  }

  private static void tenant(Connection c, String uid, String name, String policy)
      throws SQLException {
    try (PreparedStatement ps = c.prepareStatement("INSERT INTO access_tenant"
        + " (uid, version, domain, enabled, mark_for_delete, name, root)"
        + " VALUES (?, 0, ?, 1, 0, ?, 0)")) {
      ps.setString(1, uid);
      ps.setString(2, name + ".example");
      ps.setString(3, name);
      ps.execute();
    }
    try (PreparedStatement ps = c.prepareStatement("INSERT INTO access_tenant_config (uid,"
        + " version, tenant, dynamic_registration_policy, allow_password_grant, force_mfa,"
        + " invitation_expiry_days, magic_link_expiry_minutes,"
        + " refresh_token_max_lifetime_seconds, refresh_token_ttl_seconds,"
        + " require_email_verification, require_pushed_authorization_requests,"
        + " session_sso_ttl_seconds) VALUES (?, 0, ?, ?, 0, 0, 7, 15, 86400, 3600, 0, 0, 3600)")) {
      ps.setString(1, "cfg-" + uid);
      ps.setString(2, uid);
      ps.setString(3, policy);
      ps.execute();
    }
  }

  private void initialToken(String uid, String tenantUid, String token, int maxUses,
      Instant expiresAt, Instant revokedAt) throws SQLException {
    try (Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO access_dcr_initial_access_token"
            + " (uid, version, tenant, token_hash, max_uses, uses, expires_at, revoked_at)"
            + " VALUES (?, 0, ?, ?, ?, 0, ?, ?)")) {
      ps.setString(1, uid);
      ps.setString(2, tenantUid);
      ps.setString(3, digester.digest(token));
      ps.setInt(4, maxUses);
      ps.setTimestamp(5, Timestamp.from(expiresAt));
      ps.setTimestamp(6, revokedAt == null ? null : Timestamp.from(revokedAt));
      ps.execute();
    }
  }

  private RegisterClientResult registerIn(String tenant, String token) {
    return register.register(RegisterClientParams.builder().tenant(tenant)
        .issuer("https://auth.example.com/oauth/openid/" + tenant)
        .request(DynamicClientRequest.builder().redirectUris(List.of("https://app.example/cb"))
            .postLogoutRedirectUris(List.of("https://app.example/bye")).clientName("App").build())
        .initialAccessToken(token).build());
  }

  private String column(String table, String column, String uid) throws SQLException {
    try (Connection c = dataSource.getConnection();
        PreparedStatement ps =
            c.prepareStatement("SELECT " + column + " FROM " + table + " WHERE uid = ?")) {
      ps.setString(1, uid);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getString(1) : null;
      }
    }
  }

  private static Instant later() {
    return Instant.now().plus(1, ChronoUnit.HOURS);
  }

  @Test
  void registration_createsClientInTenant_withDigestsOnly() throws Exception {
    initialToken("iat", "uid-a", "iat-secret", 2, later(), null);

    RegisterClientResult result = registerIn(TENANT_A, "iat-secret");

    Assertions.assertEquals("uid-a",
        column("access_trusted_client", "tenant", result.getClientId()));
    String storedSecret = column("access_trusted_client", "secret_oauth", result.getClientId());
    Assertions.assertTrue(storedSecret.startsWith(SecretDigester.PREFIX));
    Assertions.assertTrue(digester.verify(result.getClientSecret(), storedSecret));
    Assertions.assertNotEquals(result.getRegistrationAccessToken(),
        column("access_trusted_client", "registration_access", result.getClientId()));
    Assertions.assertEquals("CLIENT_SECRET_BASIC",
        column("access_trusted_client", "token_endpoint_auth_method", result.getClientId()));
    Assertions.assertEquals("1", column("access_dcr_initial_access_token", "uses", "iat"));
    Assertions.assertEquals(List.of("https://app.example/bye"),
        read.read(TENANT_A, ISSUER, result.getClientId(), result.getRegistrationAccessToken())
            .getPostLogoutRedirectUris());
  }

  @Test
  void initialToken_ofAnotherTenant_expired_revoked_orExhausted_isRejected() throws Exception {
    initialToken("of-b", "uid-b", "token-b", 5, later(), null);
    initialToken("expired", "uid-a", "token-expired", 5, Instant.now().minusSeconds(60), null);
    initialToken("revoked", "uid-a", "token-revoked", 5, later(), Instant.now());
    initialToken("once", "uid-a", "token-once", 1, later(), null);
    registerIn(TENANT_A, "token-once");

    for (String token : List.of("token-b", "token-expired", "token-revoked", "token-once")) {
      DynamicClientException ex = Assertions.assertThrows(DynamicClientException.class,
          () -> registerIn(TENANT_A, token), token);
      Assertions.assertEquals("invalid_token", ex.getErrorCode());
    }
  }

  @Test
  void concurrentRegistrations_neverExceedMaxUses() throws Exception {
    initialToken("iat", "uid-a", "iat-secret", 1, later(), null);

    List<Boolean> outcomes = Concurrently.run(8, () -> {
      try {
        registerIn(TENANT_A, "iat-secret");
        return true;
      } catch (DynamicClientException ex) {
        return false;
      }
    });

    Assertions.assertEquals(1, outcomes.stream().filter(Boolean::booleanValue).count());
    Assertions.assertEquals("1", column("access_dcr_initial_access_token", "uses", "iat"));
  }

  @Test
  void client_isOnlyManagedFromItsTenant_andReadNeverReturnsTheSecret() throws Exception {
    initialToken("iat", "uid-a", "iat-secret", 1, later(), null);
    RegisterClientResult result = registerIn(TENANT_A, "iat-secret");
    String token = result.getRegistrationAccessToken();

    Assertions.assertThrows(DynamicClientException.class,
        () -> read.read(TENANT_B, ISSUER, result.getClientId(), token));
    Assertions.assertThrows(DynamicClientException.class,
        () -> read.read(TENANT_A, ISSUER, result.getClientId(), "wrong-token"));
    DynamicClientData data = read.read(TENANT_A, ISSUER, result.getClientId(), token);
    Assertions.assertEquals(List.of("https://app.example/cb"), data.getRedirectUris());
    Assertions.assertEquals(ISSUER + "/connect/" + result.getClientId(),
        data.getRegistrationClientUri());

    Assertions.assertThrows(DynamicClientException.class,
        () -> delete.delete(TENANT_B, result.getClientId(), token));
    delete.delete(TENANT_A, result.getClientId(), token);
    Assertions.assertNull(column("access_trusted_client", "uid", result.getClientId()));
  }

  @Test
  void tenantWithoutDcr_rejectsEvenAValidToken() throws Exception {
    try (Connection c = dataSource.getConnection()) {
      c.createStatement()
          .execute("UPDATE access_tenant_config SET dynamic_registration_policy = 'disabled'");
    }
    initialToken("iat", "uid-a", "iat-secret", 1, later(), null);

    DynamicClientException ex = Assertions.assertThrows(DynamicClientException.class,
        () -> registerIn(TENANT_A, "iat-secret"));
    Assertions.assertEquals(403, ex.getStatus());
    Assertions.assertEquals("0", column("access_dcr_initial_access_token", "uses", "iat"));
  }

  @Test
  void keysAlgorithmsAndFlags_areStoredInTheRuntimeColumns_andReadBack() throws Exception {
    initialToken("iat", "uid-a", "iat-secret", 1, later(), null);
    Map<String, Object> jwks =
        Map.of("keys", List.of(Map.of("kty", "EC", "crv", "P-256", "x", "AQAB", "y", "AQAB")));
    RegisterClientResult result =
        register.register(RegisterClientParams.builder().tenant(TENANT_A).issuer(ISSUER)
            .request(DynamicClientRequest.builder().redirectUris(List.of("https://app.example/cb"))
                .tokenEndpointAuthMethod("private_key_jwt")
                .extensions(DcrExtensionMetadata.builder().jwks(jwks)
                    .tokenEndpointAuthSigningAlg("ES256").requestObjectSigningAlg("ES256")
                    .idTokenEncryptedResponseAlg("RSA-OAEP-256").userinfoSignedResponseAlg("RS256")
                    .authorizationSignedResponseAlg("RS256").subjectType("pairwise")
                    .dpopBoundAccessTokens(true).requirePushedAuthorizationRequests(true)
                    .backchannelLogoutSessionRequired(true).build())
                .build())
            .initialAccessToken("iat-secret").build());
    String id = result.getClientId();

    Assertions.assertNull(result.getClientSecret());
    Assertions.assertNull(column("access_trusted_client", "secret_oauth", id));
    Assertions.assertEquals("PRIVATE_KEY_JWT",
        column("access_trusted_client", "token_endpoint_auth_method", id));
    Assertions.assertEquals(jwks, MariaDbSchema.mapper()
        .readValue(column("access_trusted_client", "jwks_json", id), Map.class));
    Assertions.assertEquals("ES256",
        column("access_trusted_client", "request_object_signing_alg", id));
    Assertions.assertEquals("RSA-OAEP-256",
        column("access_trusted_client", "id_token_encrypted_response_alg", id));
    Assertions.assertEquals("PAIRWISE", column("access_trusted_client", "subject_type", id));
    Assertions.assertEquals("true",
        column("access_trusted_client", "dpop_bound_access_tokens", id));
    Assertions.assertEquals("true",
        column("access_trusted_client", "require_pushed_authorization_requests", id));

    DcrExtensionMetadata read =
        this.read.read(TENANT_A, ISSUER, id, result.getRegistrationAccessToken()).getExtensions();
    Assertions.assertEquals(jwks, read.getJwks());
    Assertions.assertEquals("pairwise", read.getSubjectType());
    Assertions.assertTrue(read.isBackchannelLogoutSessionRequired());

    DynamicClientData updated = update.update(TENANT_A, ISSUER, id,
        result.getRegistrationAccessToken(),
        DynamicClientRequest.builder().redirectUris(List.of("https://app.example/cb"))
            .tokenEndpointAuthMethod("private_key_jwt")
            .extensions(DcrExtensionMetadata.builder().jwksUri("https://app.example/jwks").build())
            .build());
    Assertions.assertNull(updated.getExtensions().getJwks());
    Assertions.assertEquals("https://app.example/jwks", updated.getExtensions().getJwksUri());
    Assertions.assertEquals("PUBLIC", column("access_trusted_client", "subject_type", id));
    Assertions.assertEquals("false",
        column("access_trusted_client", "dpop_bound_access_tokens", id));
  }

  @Test
  void requestObjectJti_isAtomicAndScopedByTenantAndClient() throws Exception {
    JwtReplaySqlAdapter replay = new JwtReplaySqlAdapter(dataSource);
    Instant expiry = later();
    Assertions.assertEquals(1,
        Concurrently
            .run(8, () -> replay.firstUse("uid-a", "client-a", "request_object", "jti-1", expiry))
            .stream().filter(Boolean::booleanValue).count());
    Assertions.assertTrue(replay.firstUse("uid-b", "client-a", "request_object", "jti-1", expiry));
    Assertions.assertTrue(replay.firstUse("uid-a", "client-b", "request_object", "jti-1", expiry));
  }
}
