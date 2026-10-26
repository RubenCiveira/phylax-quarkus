package net.civeira.phylax.features.oauth.client.infrastructure.driven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.Instant;

import javax.sql.DataSource;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.client.domain.gateway.JwtReplayGateway;

/**
 * One-time JWT identifiers over {@code _oauth_replay_jti} (task 3.3), the table requested in
 * {@code docs/TODO-OIDC/cambios-modelo-J02-J05.md}. Its primary key makes the first insert win; a
 * duplicate is a replay. Until the model is regenerated the table does not exist and every use
 * fails (closed).
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class JwtReplaySqlAdapter implements JwtReplayGateway {

  private final DataSource source;

  @Override
  public boolean firstUse(String tenant, String clientId, String purpose, String jti,
      Instant expiresAt) {
    String sql = "INSERT INTO _oauth_replay_jti (jti, tenant_id, client_id, purpose, expires_at)"
        + " VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = source.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql)) {
      stat.setString(1, jti);
      stat.setString(2, tenant);
      stat.setString(3, clientId);
      stat.setString(4, purpose);
      stat.setTimestamp(5, Timestamp.from(expiresAt));
      stat.execute();
      return true;
    } catch (SQLIntegrityConstraintViolationException ex) {
      return false;
    } catch (SQLException ex) {
      if ("42S02".equals(ex.getSQLState())) {
        log.error("JWT replay table is unavailable: {}", ex.getMessage());
        return false;
      }
      if ("23000".equals(ex.getSQLState()) || "23505".equals(ex.getSQLState())) {
        return false;
      }
      throw new IllegalStateException("Unable to record a one-time JWT", ex);
    }
  }
}
