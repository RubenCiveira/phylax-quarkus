package net.civeira.phylax.features.oauth.profile.infrastructure.driven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.profile.domain.ActiveSession;
import net.civeira.phylax.features.oauth.profile.domain.gateway.SessionsGateway;

/**
 * Stub adapter for active session listing and revocation.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real implementation backed by the session store.
 * </p>
 */
@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class SessionsAdapter implements SessionsGateway {

  private final DataSource source;
  private final ObjectMapper mapper;

  @Override
  public List<ActiveSession> listByUser(String userUid) {
    List<ActiveSession> out = new ArrayList<>();
    try (Connection conn = source.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT session, expiration, client_id, client_name, ip_address, user_agent, last_used_at, auth_data FROM _oauth_session WHERE expiration > ? ORDER BY last_used_at DESC")) {
      stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          String authData = rs.getString("auth_data");
          if (!matchesUser(authData, userUid)) {
            continue;
          }
          out.add(new ActiveSession(rs.getString("session"), rs.getString("client_id"),
              rs.getString("client_name"), rs.getString("ip_address"), rs.getString("user_agent"),
              toString(rs.getTimestamp("last_used_at")), toString(rs.getTimestamp("expiration"))));
        }
      }
      return out;
    } catch (SQLException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  public void revoke(String sessionId) {
    try (Connection conn = source.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "UPDATE _oauth_session SET expiration = ?, revoked_at = ? WHERE session = ?")) {
      Timestamp now = new Timestamp(System.currentTimeMillis());
      stmt.setTimestamp(1, now);
      stmt.setTimestamp(2, now);
      stmt.setString(3, sessionId);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private boolean matchesUser(String authData, String userUid) {
    if (authData == null || authData.isBlank()) {
      return false;
    }
    try {
      JsonNode node = mapper.readTree(authData);
      String uid = node.path("uid").asText("");
      return userUid.equals(uid);
    } catch (Exception ex) {
      log.debug("Cannot parse session auth_data", ex);
      return false;
    }
  }

  private String toString(Timestamp ts) {
    return ts == null ? null : ts.toInstant().toString();
  }
}
