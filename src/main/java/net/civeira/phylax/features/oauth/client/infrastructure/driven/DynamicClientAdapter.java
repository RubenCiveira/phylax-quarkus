package net.civeira.phylax.features.oauth.client.infrastructure.driven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.crypto.AesCipherService;
import net.civeira.phylax.features.oauth.client.domain.DynamicClientData;
import net.civeira.phylax.features.oauth.client.domain.DynamicClientRequest;
import net.civeira.phylax.features.oauth.client.domain.gateway.DynamicClientGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class DynamicClientAdapter implements DynamicClientGateway {

  private static final TypeReference<List<String>> LIST_TYPE = new TypeReference<>() {};

  private final DataSource source;
  private final ObjectMapper mapper;
  private final AesCipherService cypher;

  @Override
  public DynamicClientData create(String clientId, String clientSecret,
      String registrationAccessToken, DynamicClientRequest request) {
    Instant now = Instant.now();
    String sql =
        "INSERT INTO access_trusted_client (uid, version, allow_all_scopes, allowed_scopes_m_2m, back_channel_logout_session_required, back_channel_logout_uri, client_name, client_uri, code, dynamically_registered, enabled, front_channel_logout_session_required, front_channel_logout_uri, grant_types_json, jwks_json, jwks_uri, logo_uri, m_2m_token_ttl_seconds, policy_uri, public_allow, registered_at, registration_access, request_object_signing_alg, response_types_json, secret_oauth, token_endpoint_auth_method, tos_uri) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = source.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql)) {
      stat.setString(1, clientId);
      stat.setInt(2, 0);
      stat.setBoolean(3, false);
      stat.setString(4, null);
      stat.setBoolean(5, false);
      stat.setString(6, request.getBackchannelLogoutUri());
      stat.setString(7, request.getClientName());
      stat.setString(8, request.getClientUri());
      stat.setString(9, clientId);
      stat.setBoolean(10, true);
      stat.setBoolean(11, true);
      stat.setBoolean(12, false);
      stat.setString(13, request.getFrontchannelLogoutUri());
      stat.setString(14, mapper.writeValueAsString(request.getGrantTypes()));
      stat.setString(15, null);
      stat.setString(16, null);
      stat.setString(17, request.getLogoUri());
      stat.setInt(18, 3600);
      stat.setString(19, request.getPolicyUri());
      stat.setBoolean(20, true);
      stat.setTimestamp(21, Timestamp.from(now));
      stat.setString(22, sha256(registrationAccessToken));
      stat.setString(23, null);
      stat.setString(24, mapper.writeValueAsString(request.getResponseTypes()));
      stat.setString(25, cypher.encryptForAll(clientSecret));
      stat.setString(26, request.getTokenEndpointAuthMethod());
      stat.setString(27, request.getTosUri());
      stat.execute();
      replaceRedirects(conn, clientId, request.getRedirectUris());
    } catch (SQLException | JsonProcessingException ex) {
      throw new IllegalStateException(ex);
    }
    DynamicClientData data =
        DynamicClientData.builder().clientId(clientId).redirectUris(request.getRedirectUris())
            .grantTypes(request.getGrantTypes()).responseTypes(request.getResponseTypes())
            .scope(request.getScope()).tokenEndpointAuthMethod(request.getTokenEndpointAuthMethod())
            .clientName(request.getClientName()).logoUri(request.getLogoUri())
            .clientUri(request.getClientUri()).policyUri(request.getPolicyUri())
            .tosUri(request.getTosUri()).backchannelLogoutUri(request.getBackchannelLogoutUri())
            .frontchannelLogoutUri(request.getFrontchannelLogoutUri())
            .clientIdIssuedAt(now.getEpochSecond()).registrationClientUri("").build();
    return data;
  }

  @Override
  public Optional<DynamicClientData> findByClientIdAndToken(String clientId,
      String registrationAccessToken) {
    String sql =
        "SELECT uid, token_endpoint_auth_method, client_name, logo_uri, client_uri, policy_uri, tos_uri, back_channel_logout_uri, front_channel_logout_uri, grant_types_json, response_types_json, registered_at FROM access_trusted_client WHERE uid = ? AND dynamically_registered = ? AND registration_access = ?";
    try (Connection conn = source.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql)) {
      stat.setString(1, clientId);
      stat.setBoolean(2, true);
      stat.setString(3, sha256(registrationAccessToken));
      try (ResultSet rs = stat.executeQuery()) {
        if (!rs.next()) {
          return Optional.empty();
        }
        return Optional.of(mapClient(conn, rs));
      }
    } catch (SQLException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  public Optional<DynamicClientData> update(String clientId, String registrationAccessToken,
      DynamicClientRequest request) {
    String sql =
        "UPDATE access_trusted_client SET client_name = ?, logo_uri = ?, client_uri = ?, policy_uri = ?, tos_uri = ?, token_endpoint_auth_method = ?, grant_types_json = ?, response_types_json = ?, back_channel_logout_uri = ?, front_channel_logout_uri = ?, version = version + 1 WHERE uid = ? AND dynamically_registered = ? AND registration_access = ?";
    try (Connection conn = source.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql)) {
      stat.setString(1, request.getClientName());
      stat.setString(2, request.getLogoUri());
      stat.setString(3, request.getClientUri());
      stat.setString(4, request.getPolicyUri());
      stat.setString(5, request.getTosUri());
      stat.setString(6, request.getTokenEndpointAuthMethod());
      stat.setString(7, mapper.writeValueAsString(request.getGrantTypes()));
      stat.setString(8, mapper.writeValueAsString(request.getResponseTypes()));
      stat.setString(9, request.getBackchannelLogoutUri());
      stat.setString(10, request.getFrontchannelLogoutUri());
      stat.setString(11, clientId);
      stat.setBoolean(12, true);
      stat.setString(13, sha256(registrationAccessToken));
      if (stat.executeUpdate() == 0) {
        return Optional.empty();
      }
      replaceRedirects(conn, clientId, request.getRedirectUris());
      return findByClientIdAndToken(clientId, registrationAccessToken);
    } catch (SQLException | JsonProcessingException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  public boolean delete(String clientId, String registrationAccessToken) {
    String sql =
        "DELETE FROM access_trusted_client WHERE uid = ? AND dynamically_registered = ? AND registration_access = ?";
    try (Connection conn = source.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql)) {
      stat.setString(1, clientId);
      stat.setBoolean(2, true);
      stat.setString(3, sha256(registrationAccessToken));
      boolean deleted = stat.executeUpdate() > 0;
      if (deleted) {
        deleteRedirects(conn, clientId);
      }
      return deleted;
    } catch (SQLException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  public String readRegistrationPolicy(String tenant) {
    String sql =
        "SELECT c.dynamic_registration_policy FROM access_tenant_config c JOIN access_tenant t ON t.uid = c.tenant WHERE t.name = ?";
    try (Connection conn = source.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql)) {
      stat.setString(1, tenant);
      try (ResultSet rs = stat.executeQuery()) {
        if (!rs.next()) {
          return "disabled";
        }
        String policy = rs.getString(1);
        return policy == null || policy.isBlank() ? "disabled" : policy;
      }
    } catch (SQLException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private DynamicClientData mapClient(Connection conn, ResultSet rs) throws SQLException {
    List<String> redirectUris = loadRedirects(conn, rs.getString("uid"));
    List<String> grantTypes =
        readList(rs.getString("grant_types_json"), List.of("authorization_code"));
    List<String> responseTypes = readList(rs.getString("response_types_json"), List.of("code"));
    Timestamp issuedAt = rs.getTimestamp("registered_at");
    return DynamicClientData.builder().clientId(rs.getString("uid")).redirectUris(redirectUris)
        .grantTypes(grantTypes).responseTypes(responseTypes).scope(null)
        .tokenEndpointAuthMethod(rs.getString("token_endpoint_auth_method"))
        .clientName(rs.getString("client_name")).logoUri(rs.getString("logo_uri"))
        .clientUri(rs.getString("client_uri")).policyUri(rs.getString("policy_uri"))
        .tosUri(rs.getString("tos_uri"))
        .backchannelLogoutUri(rs.getString("back_channel_logout_uri"))
        .frontchannelLogoutUri(rs.getString("front_channel_logout_uri"))
        .clientIdIssuedAt(issuedAt != null ? issuedAt.toInstant().getEpochSecond() : 0)
        .registrationClientUri("").build();
  }

  private List<String> loadRedirects(Connection conn, String clientId) throws SQLException {
    String sql = "SELECT url FROM access_trusted_client_allowed_redirect WHERE client = ?";
    try (PreparedStatement stat = conn.prepareStatement(sql)) {
      stat.setString(1, clientId);
      try (ResultSet rs = stat.executeQuery()) {
        java.util.ArrayList<String> result = new java.util.ArrayList<>();
        while (rs.next()) {
          String url = rs.getString(1);
          if (url != null && !url.isBlank()) {
            result.add(url);
          }
        }
        return List.copyOf(result);
      }
    }
  }

  private void replaceRedirects(Connection conn, String clientId, List<String> redirectUris)
      throws SQLException {
    deleteRedirects(conn, clientId);
    if (redirectUris == null || redirectUris.isEmpty()) {
      return;
    }
    String sql =
        "INSERT INTO access_trusted_client_allowed_redirect (uid, version, url, client) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stat = conn.prepareStatement(sql)) {
      for (String uri : redirectUris) {
        stat.setString(1, HexFormat.of().formatHex(UUID.randomUUID().toString().getBytes()));
        stat.setInt(2, 0);
        stat.setString(3, uri);
        stat.setString(4, clientId);
        stat.addBatch();
      }
      stat.executeBatch();
    }
  }

  private void deleteRedirects(Connection conn, String clientId) throws SQLException {
    String sql = "DELETE FROM access_trusted_client_allowed_redirect WHERE client = ?";
    try (PreparedStatement stat = conn.prepareStatement(sql)) {
      stat.setString(1, clientId);
      stat.execute();
    }
  }

  private List<String> readList(String json, List<String> defaultValue) {
    if (json == null || json.isBlank()) {
      return defaultValue;
    }
    try {
      List<String> list = mapper.readValue(json, LIST_TYPE);
      return list == null || list.isEmpty() ? defaultValue : list;
    } catch (JsonProcessingException ex) {
      return defaultValue;
    }
  }

  private String sha256(String value) {
    try {
      java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hash);
    } catch (java.security.NoSuchAlgorithmException ex) {
      throw new IllegalStateException(ex);
    }
  }
}
