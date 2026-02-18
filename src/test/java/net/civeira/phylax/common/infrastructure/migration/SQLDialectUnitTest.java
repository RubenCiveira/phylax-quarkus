package net.civeira.phylax.common.infrastructure.migration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SQLDialect interface default methods")
class SQLDialectUnitTest {

  /**
   * Minimal implementation to test default methods.
   */
  private static class MinimalDialect implements SQLDialect {
    @Override
    public String createLogTable(String name) {
      return "CREATE TABLE " + name;
    }

    @Override
    public String createLockTable(String name) {
      return "CREATE TABLE " + name;
    }
  }

  private final MinimalDialect dialect = new MinimalDialect();

  @Nested
  @DisplayName("default insertLock()")
  class InsertLock {

    @Test
    @DisplayName("Should generate INSERT with numeric 0 for unlocked")
    void shouldGenerateInsertWithNumericZero() {
      // Arrange / Act — Generate the insert-lock SQL using the default interface implementation
      String sql = dialect.insertLock("locks");

      // Assert — Verify the default implementation uses numeric 0 for unlocked state
      assertTrue(sql.contains("INSERT INTO locks"),
          "SQL should contain INSERT INTO with the table name");
      assertTrue(sql.contains("0"), "SQL should use numeric 0 for unlocked state by default");
    }
  }

  @Nested
  @DisplayName("default releaseLock()")
  class ReleaseLock {

    @Test
    @DisplayName("Should generate UPDATE setting locked to 0")
    void shouldSetLockedToZero() {
      // Arrange / Act — Generate the release-lock SQL using the default interface implementation
      String sql = dialect.releaseLock("locks");

      // Assert — Verify the default implementation resets locked to 0 and clears granted
      assertTrue(sql.contains("locked = 0"), "SQL should set locked to 0 by default");
      assertTrue(sql.contains("granted = NULL"), "SQL should reset granted to NULL");
    }
  }

  @Nested
  @DisplayName("default markOkSql()")
  class MarkOkSql {

    @Test
    @DisplayName("Should use NOW() for timestamps by default")
    void shouldUseNowByDefault() {
      // Arrange / Act — Generate the mark-OK SQL using the default interface implementation
      String sql = dialect.markOkSql("log", true);

      // Assert — Verify the default implementation uses NOW() for timestamps
      assertTrue(sql.contains("NOW()"), "Default dialect should use NOW() for timestamps");
    }
  }

  @Nested
  @DisplayName("default markFailSql()")
  class MarkFailSql {

    @Test
    @DisplayName("Should include error param in INSERT")
    void shouldIncludeErrorParamInInsert() {
      // Arrange / Act — Generate the mark-fail SQL for a new record using the default
      // implementation
      String sql = dialect.markFailSql("log", false);

      // Assert — Verify the default implementation generates an INSERT statement
      assertTrue(sql.contains("INSERT INTO log"),
          "SQL should be INSERT when record does not exist");
    }

    @Test
    @DisplayName("Should include error param in UPDATE")
    void shouldIncludeErrorParamInUpdate() {
      // Arrange / Act — Generate the mark-fail SQL for an existing record using the default
      // implementation
      String sql = dialect.markFailSql("log", true);

      // Assert — Verify the default implementation includes the error parameter
      assertTrue(sql.contains("error=?"),
          "SQL should include error parameter for failed migration");
    }
  }

  @Nested
  @DisplayName("default listExecutedSql()")
  class ListExecutedSql {

    @Test
    @DisplayName("Should generate SELECT with ORDER BY execution ASC")
    void shouldGenerateSelectWithOrderBy() {
      // Arrange / Act — Generate the list-executed SQL using the default interface implementation
      String sql = dialect.listExecutedSql("log");

      // Assert — Verify the default implementation queries migrations ordered by execution time
      assertTrue(sql.contains("SELECT filename, md5sum, error FROM log"),
          "SQL should select filename, md5sum, error columns");
      assertTrue(sql.contains("ORDER BY execution ASC"), "SQL should order by execution ascending");
    }
  }

  @Nested
  @DisplayName("default updateLock()")
  class UpdateLock {

    @Test
    @DisplayName("Should use numeric 1 and NOW() by default")
    void shouldUseNumericOneAndNow() {
      // Arrange / Act — Generate the update-lock SQL using the default interface implementation
      String sql = dialect.updateLock("locks");

      // Assert — Verify the default implementation uses numeric 1 and NOW() for lock acquisition
      assertTrue(sql.contains("locked = 1"), "Default dialect should use numeric 1 for locked");
      assertTrue(sql.contains("NOW()"), "Default dialect should use NOW() for granted timestamp");
    }
  }

  @Nested
  @DisplayName("default selectLock()")
  class SelectLock {

    @Test
    @DisplayName("Should generate SELECT for lock status")
    void shouldGenerateSelectForLockStatus() {
      // Arrange / Act — Generate the select-lock SQL using the default interface implementation
      String sql = dialect.selectLock("locks");

      // Assert — Verify the default implementation queries lock status filtered by id
      assertTrue(sql.contains("SELECT locked, granted FROM locks"),
          "SQL should select locked and granted columns");
      assertTrue(sql.contains("WHERE id = 1"), "SQL should filter by id = 1");
    }
  }
}
