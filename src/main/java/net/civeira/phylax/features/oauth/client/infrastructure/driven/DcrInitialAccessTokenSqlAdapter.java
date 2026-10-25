package net.civeira.phylax.features.oauth.client.infrastructure.driven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import javax.sql.DataSource;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.crypto.SecretDigester;
import net.civeira.phylax.features.oauth.client.domain.DcrInitialAccessToken;
import net.civeira.phylax.features.oauth.client.domain.gateway.DcrInitialAccessTokenGateway;

/**
 * Initial access tokens of DCR over {@code access_dcr_initial_access_token} (task 2.1).
 *
 * <p>
 * The token is looked up by its {@link SecretDigester} digest, as the master stores it; a token
 * issued with a pepper that is no longer current is not found after rotation. A use is spent with a
 * single conditional {@code UPDATE}, so concurrent registrations never exceed {@code max_uses}.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class DcrInitialAccessTokenSqlAdapter implements DcrInitialAccessTokenGateway {

  private static final String USABLE =
      "i.revoked_at IS NULL AND i.expires_at > ? AND i.uses < i.max_uses";

  private final DataSource source;
  private final SecretDigester digester;

  @Override
  public Optional<DcrInitialAccessToken> findUsable(String tenant, String token) {
    String sql = "SELECT i.uid, i.allowed_grant_types FROM access_dcr_initial_access_token i "
        + "JOIN access_tenant t ON t.uid = i.tenant "
        + "WHERE t.name = ? AND t.enabled = ? AND i.token_hash = ? AND " + USABLE;
    try (Connection conn = source.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql)) {
      stat.setString(1, tenant);
      stat.setBoolean(2, true);
      stat.setString(3, digester.digest(token));
      stat.setTimestamp(4, Timestamp.from(Instant.now()));
      try (ResultSet rs = stat.executeQuery()) {
        if (!rs.next()) {
          return Optional.empty();
        }
        return Optional.of(new DcrInitialAccessToken(rs.getString(1),
            TrustedClientPolicy.parseList(rs.getString(2))));
      }
    } catch (SQLException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  public boolean consume(String uid) {
    String sql = "UPDATE access_dcr_initial_access_token i SET i.uses = i.uses + 1, "
        + "i.version = i.version + 1 WHERE i.uid = ? AND " + USABLE;
    try (Connection conn = source.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql)) {
      stat.setString(1, uid);
      stat.setTimestamp(2, Timestamp.from(Instant.now()));
      return stat.executeUpdate() == 1;
    } catch (SQLException ex) {
      throw new IllegalStateException(ex);
    }
  }
}
