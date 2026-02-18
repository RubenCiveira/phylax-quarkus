package net.civeira.phylax.common.infrastructure.migration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MySQLDialect SQL dialect for MySQL database")
class MySQLDialectUnitTest {

  private MySQLDialect dialect;

  @BeforeEach
  void setUp() {
    dialect = new MySQLDialect();
  }

  @Nested
  @DisplayName("createLogTable()")
  class CreateLogTable {

    @Test
    @DisplayName("Should generate CREATE TABLE with DATETIME type")
    void shouldGenerateCreateTableWithDatetime() {
      // Arrange / Act — Generate the CREATE TABLE SQL for the log table using MySQL dialect
      String sql = dialect.createLogTable("migration_log");

      // Assert — Verify the SQL uses MySQL-specific DATETIME type for the execution column
      assertTrue(sql.contains("CREATE TABLE IF NOT EXISTS migration_log"),
          "SQL should contain CREATE TABLE IF NOT EXISTS");
      assertTrue(sql.contains("DATETIME"),
          "SQL should use DATETIME type for MySQL execution column");
    }
  }

  @Nested
  @DisplayName("createLockTable()")
  class CreateLockTable {

    @Test
    @DisplayName("Should generate CREATE TABLE with TINYINT for boolean")
    void shouldGenerateCreateTableWithTinyint() {
      // Arrange / Act — Generate the CREATE TABLE SQL for the lock table using MySQL dialect
      String sql = dialect.createLockTable("migration_lock");

      // Assert — Verify the SQL uses MySQL's TINYINT(1) for boolean representation
      assertTrue(sql.contains("TINYINT(1)"),
          "SQL should use TINYINT(1) for MySQL boolean representation");
    }
  }

  @Nested
  @DisplayName("insertLock()")
  class InsertLock {

    @Test
    @DisplayName("Should generate INSERT IGNORE for MySQL")
    void shouldGenerateInsertIgnore() {
      // Arrange / Act — Generate the insert-lock SQL using MySQL's INSERT IGNORE syntax
      String sql = dialect.insertLock("migration_lock");

      // Assert — Verify the SQL uses INSERT IGNORE for MySQL conflict handling
      assertTrue(sql.contains("INSERT IGNORE INTO migration_lock"),
          "SQL should use INSERT IGNORE for MySQL conflict handling");
    }
  }

  @Nested
  @DisplayName("markOkSql()")
  class MarkOkSql {

    @Test
    @DisplayName("Should generate UPDATE when exists")
    void shouldGenerateUpdateWhenExists() {
      // Arrange / Act — Generate the mark-OK SQL for an existing record in MySQL
      String sql = dialect.markOkSql("log", true);

      // Assert — Verify the SQL uses MySQL's NOW() function for timestamps
      assertTrue(sql.startsWith("UPDATE log"), "SQL should be UPDATE when record exists");
      assertTrue(sql.contains("NOW()"), "SQL should use NOW() for MySQL timestamps");
    }

    @Test
    @DisplayName("Should generate INSERT when not exists")
    void shouldGenerateInsertWhenNotExists() {
      // Arrange / Act — Generate the mark-OK SQL for a new record in MySQL
      String sql = dialect.markOkSql("log", false);

      // Assert — Verify the SQL inserts a new record
      assertTrue(sql.startsWith("INSERT INTO log"),
          "SQL should be INSERT when record does not exist");
    }
  }
}
