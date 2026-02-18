package net.civeira.phylax.common.infrastructure.migration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PostgreSQLDialect SQL dialect for PostgreSQL database")
class PostgreSQLDialectUnitTest {

  private PostgreSQLDialect dialect;

  @BeforeEach
  void setUp() {
    dialect = new PostgreSQLDialect();
  }

  @Nested
  @DisplayName("createLogTable()")
  class CreateLogTable {

    @Test
    @DisplayName("Should generate CREATE TABLE with TIMESTAMP type")
    void shouldGenerateCreateTableWithTimestamp() {
      // Arrange / Act — Generate the CREATE TABLE SQL for the log table using PostgreSQL dialect
      String sql = dialect.createLogTable("migration_log");

      // Assert — Verify the SQL uses PostgreSQL's TIMESTAMP type
      assertTrue(sql.contains("CREATE TABLE IF NOT EXISTS migration_log"),
          "SQL should contain CREATE TABLE IF NOT EXISTS");
      assertTrue(sql.contains("TIMESTAMP"), "SQL should use TIMESTAMP type for PostgreSQL");
    }
  }

  @Nested
  @DisplayName("createLockTable()")
  class CreateLockTable {

    @Test
    @DisplayName("Should generate CREATE TABLE with BOOLEAN type")
    void shouldGenerateCreateTableWithBoolean() {
      // Arrange / Act — Generate the CREATE TABLE SQL for the lock table using PostgreSQL dialect
      String sql = dialect.createLockTable("migration_lock");

      // Assert — Verify the SQL uses PostgreSQL's native BOOLEAN type
      assertTrue(sql.contains("BOOLEAN"), "SQL should use BOOLEAN type for PostgreSQL");
    }
  }

  @Nested
  @DisplayName("insertLock()")
  class InsertLock {

    @Test
    @DisplayName("Should generate INSERT with ON CONFLICT DO NOTHING")
    void shouldGenerateInsertWithOnConflict() {
      // Arrange / Act — Generate the insert-lock SQL using PostgreSQL's ON CONFLICT syntax
      String sql = dialect.insertLock("migration_lock");

      // Assert — Verify the SQL uses PostgreSQL's ON CONFLICT DO NOTHING for conflict handling
      assertTrue(sql.contains("ON CONFLICT DO NOTHING"),
          "SQL should use ON CONFLICT DO NOTHING for PostgreSQL upsert");
      assertTrue(sql.contains("FALSE"), "Initial locked value should be FALSE");
    }
  }

  @Nested
  @DisplayName("releaseLock()")
  class ReleaseLock {

    @Test
    @DisplayName("Should generate UPDATE with FALSE")
    void shouldGenerateUpdateWithFalse() {
      // Arrange / Act — Generate the release-lock SQL to unlock the migration lock
      String sql = dialect.releaseLock("migration_lock");

      // Assert — Verify the SQL resets the locked column to FALSE
      assertTrue(sql.contains("locked = FALSE"), "SQL should set locked to FALSE for PostgreSQL");
    }
  }

  @Nested
  @DisplayName("updateLock()")
  class UpdateLock {

    @Test
    @DisplayName("Should generate UPDATE with TRUE for lock acquisition")
    void shouldGenerateUpdateWithTrue() {
      // Arrange / Act — Generate the update-lock SQL to acquire the migration lock
      String sql = dialect.updateLock("migration_lock");

      // Assert — Verify the SQL sets locked to TRUE and uses PostgreSQL's NOW() function
      assertTrue(sql.contains("locked = TRUE"), "SQL should set locked to TRUE");
      assertTrue(sql.contains("NOW()"), "SQL should use NOW() for granted timestamp");
    }
  }

  @Nested
  @DisplayName("markOkSql()")
  class MarkOkSql {

    @Test
    @DisplayName("Should generate UPDATE when record exists")
    void shouldGenerateUpdateWhenExists() {
      // Arrange / Act — Generate the mark-OK SQL for an existing migration record
      String sql = dialect.markOkSql("log", true);

      // Assert — Verify the SQL updates the existing record
      assertTrue(sql.startsWith("UPDATE log"), "SQL should be UPDATE when record exists");
    }

    @Test
    @DisplayName("Should generate INSERT when record does not exist")
    void shouldGenerateInsertWhenNotExists() {
      // Arrange / Act — Generate the mark-OK SQL for a new migration record
      String sql = dialect.markOkSql("log", false);

      // Assert — Verify the SQL inserts a new record
      assertTrue(sql.startsWith("INSERT INTO log"),
          "SQL should be INSERT when record does not exist");
    }
  }

  @Nested
  @DisplayName("markFailSql()")
  class MarkFailSql {

    @Test
    @DisplayName("Should generate UPDATE with error param when record exists")
    void shouldGenerateUpdateWithErrorWhenExists() {
      // Arrange / Act — Generate the mark-fail SQL for an existing migration record
      String sql = dialect.markFailSql("log", true);

      // Assert — Verify the SQL updates the record with an error parameter
      assertTrue(sql.startsWith("UPDATE log"), "SQL should be UPDATE when record exists");
      assertTrue(sql.contains("error=?"), "SQL should include error parameter");
    }

    @Test
    @DisplayName("Should generate INSERT with error param when record does not exist")
    void shouldGenerateInsertWithErrorWhenNotExists() {
      // Arrange / Act — Generate the mark-fail SQL for a new migration record
      String sql = dialect.markFailSql("log", false);

      // Assert — Verify the SQL inserts a new record with the error
      assertTrue(sql.startsWith("INSERT INTO log"),
          "SQL should be INSERT when record does not exist");
    }
  }
}
