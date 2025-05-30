/* @autogenerated */
package net.civeira.phylax.common.infrastructure.sql;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class NotUniqueExceptionUnitTest {

  @Test
  void testConstructorWithOnlySQLException() {
    SQLException sqlEx = new SQLException("Duplicate entry found");
    NotUniqueException ex = new NotUniqueException(sqlEx);

    assertEquals(sqlEx, ex.getCause());
    assertTrue(ex instanceof UncheckedSqlException);
  }

  @Test
  void testConstructorWithMessageAndSQLException() {
    SQLException sqlEx = new SQLException("Unique constraint failed");
    String message = "Duplicate key violation";
    NotUniqueException ex = new NotUniqueException(message, sqlEx);

    assertEquals(sqlEx, ex.getCause());
    assertEquals(message, ex.getMessage());
    assertTrue(ex instanceof UncheckedSqlException);
  }
}
