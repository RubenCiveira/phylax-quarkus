package net.civeira.phylax.common.infrastructure.audit;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AuditQueryFilter SQL filter builder")
class AuditQueryFilterUnitTest {

  @Nested
  @DisplayName("appendToFilter()")
  class AppendToFilter {

    @Test
    @DisplayName("Should append performedBy filter when set")
    void shouldAppendPerformedByFilterWhenSet() {
      // Arrange — a filter with performedBy set to "admin" and empty SQL/params are prepared
      AuditQueryFilter filter = AuditQueryFilter.builder().performedBy("admin").build();
      StringBuilder sql = new StringBuilder();
      List<Object> params = new ArrayList<>();

      // Act — the filter appends its clauses to the SQL builder and params list
      filter.appendToFilter(sql, params);

      // Assert — the SQL should contain a performed_by clause with the correct parameter
      assertTrue(sql.toString().contains("AND performed_by = ?"),
          "SQL should contain performed_by filter clause");
      assertEquals(1, params.size(), "Params should contain exactly 1 entry");
      assertEquals("admin", params.get(0), "First param should be the performedBy value");
    }

    @Test
    @DisplayName("Should append entityId filter when set")
    void shouldAppendEntityIdFilterWhenSet() {
      // Arrange — a filter with entityId set to "entity-123" and empty SQL/params are prepared
      AuditQueryFilter filter = AuditQueryFilter.builder().entityId("entity-123").build();
      StringBuilder sql = new StringBuilder();
      List<Object> params = new ArrayList<>();

      // Act — the filter appends its clauses to the SQL builder and params list
      filter.appendToFilter(sql, params);

      // Assert — the SQL should contain an entity_id clause with the correct parameter
      assertTrue(sql.toString().contains("AND entity_id = ?"),
          "SQL should contain entity_id filter clause");
      assertEquals("entity-123", params.get(0), "Param should be the entityId value");
    }

    @Test
    @DisplayName("Should append operation filter when set")
    void shouldAppendOperationFilterWhenSet() {
      // Arrange — a filter with operation set to "delete" and empty SQL/params are prepared
      AuditQueryFilter filter = AuditQueryFilter.builder().operation("delete").build();
      StringBuilder sql = new StringBuilder();
      List<Object> params = new ArrayList<>();

      // Act — the filter appends its clauses to the SQL builder and params list
      filter.appendToFilter(sql, params);

      // Assert — the SQL should contain an operation clause with the correct parameter
      assertTrue(sql.toString().contains("AND operation = ?"),
          "SQL should contain operation filter clause");
      assertEquals("delete", params.get(0), "Param should be the operation value");
    }

    @Test
    @DisplayName("Should append traceId filter when set")
    void shouldAppendTraceIdFilterWhenSet() {
      // Arrange — a filter with traceId set to "trace-abc" and empty SQL/params are prepared
      AuditQueryFilter filter = AuditQueryFilter.builder().traceId("trace-abc").build();
      StringBuilder sql = new StringBuilder();
      List<Object> params = new ArrayList<>();

      // Act — the filter appends its clauses to the SQL builder and params list
      filter.appendToFilter(sql, params);

      // Assert — the SQL should contain a trace_id clause with the correct parameter
      assertTrue(sql.toString().contains("AND trace_id = ?"),
          "SQL should contain trace_id filter clause");
      assertEquals("trace-abc", params.get(0), "Param should be the traceId value");
    }

    @Test
    @DisplayName("Should append spanId filter when set")
    void shouldAppendSpanIdFilterWhenSet() {
      // Arrange — a filter with spanId set to "span-xyz" and empty SQL/params are prepared
      AuditQueryFilter filter = AuditQueryFilter.builder().spanId("span-xyz").build();
      StringBuilder sql = new StringBuilder();
      List<Object> params = new ArrayList<>();

      // Act — the filter appends its clauses to the SQL builder and params list
      filter.appendToFilter(sql, params);

      // Assert — the SQL should contain a span_id clause with the correct parameter
      assertTrue(sql.toString().contains("AND span_id = ?"),
          "SQL should contain span_id filter clause");
      assertEquals("span-xyz", params.get(0), "Param should be the spanId value");
    }

    @Test
    @DisplayName("Should append from timestamp filter when set")
    void shouldAppendFromTimestampFilterWhenSet() {
      // Arrange — a filter with a from timestamp and empty SQL/params are prepared
      ZonedDateTime from = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
      AuditQueryFilter filter = AuditQueryFilter.builder().from(from).build();
      StringBuilder sql = new StringBuilder();
      List<Object> params = new ArrayList<>();

      // Act — the filter appends its clauses to the SQL builder and params list
      filter.appendToFilter(sql, params);

      // Assert — the SQL should contain a timestamp >= clause with a Timestamp parameter
      assertTrue(sql.toString().contains("AND timestamp >= ?"),
          "SQL should contain timestamp >= filter clause");
      assertInstanceOf(Timestamp.class, params.get(0), "Param should be a java.sql.Timestamp");
    }

    @Test
    @DisplayName("Should append to timestamp filter when set")
    void shouldAppendToTimestampFilterWhenSet() {
      // Arrange — a filter with a to timestamp and empty SQL/params are prepared
      ZonedDateTime to = ZonedDateTime.of(2025, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);
      AuditQueryFilter filter = AuditQueryFilter.builder().to(to).build();
      StringBuilder sql = new StringBuilder();
      List<Object> params = new ArrayList<>();

      // Act — the filter appends its clauses to the SQL builder and params list
      filter.appendToFilter(sql, params);

      // Assert — the SQL should contain a timestamp <= clause with a Timestamp parameter
      assertTrue(sql.toString().contains("AND timestamp <= ?"),
          "SQL should contain timestamp <= filter clause");
      assertInstanceOf(Timestamp.class, params.get(0), "Param should be a java.sql.Timestamp");
    }

    @Test
    @DisplayName("Should append all filters when all fields are set")
    void shouldAppendAllFiltersWhenAllFieldsAreSet() {
      // Arrange — a filter with all fields populated and empty SQL/params are prepared
      ZonedDateTime now = ZonedDateTime.now();
      AuditQueryFilter filter = AuditQueryFilter.builder().performedBy("admin").entityId("e1")
          .operation("update").traceId("t1").spanId("s1").from(now.minusDays(1)).to(now).build();
      StringBuilder sql = new StringBuilder();
      List<Object> params = new ArrayList<>();

      // Act — the filter appends its clauses to the SQL builder and params list
      filter.appendToFilter(sql, params);

      // Assert — the SQL should contain all seven filter clauses with corresponding parameters
      assertEquals(7, params.size(), "Should have 7 params when all filters are set");
      String sqlStr = sql.toString();
      assertTrue(sqlStr.contains("AND performed_by = ?"), "SQL should contain performed_by clause");
      assertTrue(sqlStr.contains("AND entity_id = ?"), "SQL should contain entity_id clause");
      assertTrue(sqlStr.contains("AND operation = ?"), "SQL should contain operation clause");
      assertTrue(sqlStr.contains("AND trace_id = ?"), "SQL should contain trace_id clause");
      assertTrue(sqlStr.contains("AND span_id = ?"), "SQL should contain span_id clause");
      assertTrue(sqlStr.contains("AND timestamp >= ?"), "SQL should contain timestamp >= clause");
      assertTrue(sqlStr.contains("AND timestamp <= ?"), "SQL should contain timestamp <= clause");
    }

    @Test
    @DisplayName("Should not append anything when no fields are set")
    void shouldNotAppendAnythingWhenNoFieldsAreSet() {
      // Arrange — a filter with no fields set and empty SQL/params are prepared
      AuditQueryFilter filter = AuditQueryFilter.builder().build();
      StringBuilder sql = new StringBuilder();
      List<Object> params = new ArrayList<>();

      // Act — the filter appends its clauses to the SQL builder and params list
      filter.appendToFilter(sql, params);

      // Assert — the SQL and params should remain empty since no filters were configured
      assertEquals("", sql.toString(), "SQL should be empty when no filters are set");
      assertTrue(params.isEmpty(), "Params should be empty when no filters are set");
    }
  }
}
