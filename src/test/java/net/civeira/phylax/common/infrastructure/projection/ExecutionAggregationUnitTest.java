package net.civeira.phylax.common.infrastructure.projection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ExecutionAggregation data holder")
class ExecutionAggregationUnitTest {

  @Nested
  @DisplayName("Builder and getters")
  class BuilderAndGetters {

    @Test
    @DisplayName("Should build with name and alias")
    void shouldBuildWithNameAndAlias() {
      // Arrange / Act — build an aggregation with a name and an alias
      ExecutionAggregation agg = ExecutionAggregation.builder().name("tenantName")
          .alias("tenant_name").selection(List.of()).build();

      // Assert — both name and alias match the configured values
      assertEquals("tenantName", agg.getName(), "Name should match the value set in builder");
      assertEquals("tenant_name", agg.getAlias(), "Alias should match the value set in builder");
    }

    @Test
    @DisplayName("Should hold nested selection list")
    void shouldHoldNestedSelectionList() {
      // Arrange — a child aggregation representing a nested field
      ExecutionAggregation child =
          ExecutionAggregation.builder().name("city").alias("city").selection(List.of()).build();

      // Act — build a parent aggregation containing the child in its selection
      ExecutionAggregation parent = ExecutionAggregation.builder().name("address").alias("address")
          .selection(List.of(child)).build();

      // Assert — the parent holds exactly one child with the expected name
      assertEquals(1, parent.getSelection().size(),
          "Selection should contain exactly 1 child aggregation");
      assertEquals("city", parent.getSelection().get(0).getName(),
          "Child aggregation name should match");
    }

    @Test
    @DisplayName("Should allow null alias")
    void shouldAllowNullAlias() {
      // Arrange / Act — build an aggregation without setting an alias
      ExecutionAggregation agg =
          ExecutionAggregation.builder().name("field").selection(List.of()).build();

      // Assert — the alias defaults to null when omitted
      assertNull(agg.getAlias(), "Alias should be null when not set");
    }
  }
}
