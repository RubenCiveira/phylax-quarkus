package net.civeira.phylax.bootstrap.install;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.sql.DataSource;

import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.common.crypto.AesCipherService;
import net.civeira.phylax.common.crypto.PasswordHasher;
import net.civeira.phylax.common.crypto.SecretDigester;
import net.civeira.phylax.common.infrastructure.migration.Migrations;

/**
 * Bulk migration of credentials still stored with reversible encryption (task 2.9).
 *
 * <p>
 * Before the protected-field masters, client secrets and user passwords were encrypted with
 * {@link AesCipherService#encryptForAll}. The masters now only read verifiers: an HMAC digest with
 * pepper for client secrets ({@link SecretDigester}, option A of PHP EP-02) and Argon2id for
 * passwords ({@link PasswordHasher}). On startup, right after the schema migrations, every value
 * not yet in its verifier format is decrypted once and replaced by its verifier, so old clients and
 * users keep authenticating and nothing reversible is left. It is idempotent: once migrated, the
 * queries find nothing. A value that cannot be decrypted is left untouched and logged (without the
 * value): that client or user must get a new credential.
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class LegacyCredentialMigration {

  private final DataSource source;
  private final AesCipherService aes;
  private final SecretDigester digester;
  private final PasswordHasher hasher;

  void onStart(@Observes @Priority(Migrations.POST_MIGRATION_PHASE_PRIORITY) StartupEvent event) {
    migrate();
  }

  /**
   * Replaces every legacy credential by its verifier.
   *
   * @return number of migrated credentials
   */
  public int migrate() {
    try (Connection conn = source.getConnection()) {
      int secrets = migrate(conn, "access_oauth_client", "secret_oauth",
          "secret_oauth NOT LIKE '" + SecretDigester.PREFIX + "%'", digester::digest);
      int passwords = migrate(conn, "access_user", "password",
          "password <> '' AND password NOT LIKE '$argon2id$%'", hasher::hash);
      if (secrets + passwords > 0) {
        log.info("Migrated {} client secrets and {} passwords to non-reversible verifiers", secrets,
            passwords);
      }
      return secrets + passwords;
    } catch (SQLException ex) {
      log.error("Unable to migrate legacy credentials: {}", ex.getMessage());
      return 0;
    }
  }

  private int migrate(Connection conn, String table, String column, String legacy,
      Function<String, String> verifier) throws SQLException {
    Map<String, String> found = new LinkedHashMap<>();
    String select = "SELECT uid, " + column + " FROM " + table + " WHERE " + column
        + " IS NOT NULL AND " + legacy;
    try (PreparedStatement stat = conn.prepareStatement(select);
        ResultSet rs = stat.executeQuery()) {
      while (rs.next()) {
        found.put(rs.getString(1), rs.getString(2));
      }
    }
    int migrated = 0;
    String update =
        "UPDATE " + table + " SET " + column + " = ? WHERE uid = ? AND " + column + " = ?";
    try (PreparedStatement stat = conn.prepareStatement(update)) {
      for (Map.Entry<String, String> row : found.entrySet()) {
        Optional<String> plain = aes.decryptForAll(row.getValue());
        if (plain.isEmpty()) {
          log.error("Legacy {}.{} of {} cannot be decrypted: issue a new credential", table, column,
              row.getKey());
          continue;
        }
        stat.setString(1, verifier.apply(plain.get()));
        stat.setString(2, row.getKey());
        stat.setString(3, row.getValue());
        migrated += stat.executeUpdate();
      }
    }
    return migrated;
  }
}
