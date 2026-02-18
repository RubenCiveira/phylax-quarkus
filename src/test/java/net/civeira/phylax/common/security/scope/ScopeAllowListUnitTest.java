package net.civeira.phylax.common.security.scope;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ScopeAllowList scope collection")
class ScopeAllowListUnitTest {

  @Nested
  @DisplayName("allowed()")
  class Allowed {

    @Test
    @DisplayName("Should return true when a matching scope exists")
    void shouldReturnTrueWhenMatchingScopeExists() {
      // Arrange — Build a list with two scope entries for different resource-action pairs
      ScopeAllowList list = new ScopeAllowList();
      list.add(ScopeAllow.builder().resource("tenant").name("create").build());
      list.add(ScopeAllow.builder().resource("user").name("read").build());

      // Act — Check if "tenant:create" is allowed
      boolean result = list.allowed("tenant", "create");

      // Assert — The list should confirm the matching scope is allowed
      assertTrue(result, "Should return true when a scope matching resource and action exists");
    }

    @Test
    @DisplayName("Should return false when no matching scope exists")
    void shouldReturnFalseWhenNoMatchingScopeExists() {
      // Arrange — Build a list with a single scope entry for "tenant:create"
      ScopeAllowList list = new ScopeAllowList();
      list.add(ScopeAllow.builder().resource("tenant").name("create").build());

      // Act — Check if a non-existent action "tenant:delete" is allowed
      boolean result = list.allowed("tenant", "delete");

      // Assert — The list should reject the unmatched resource-action combination
      assertFalse(result, "Should return false when no scope matches the resource and action");
    }

    @Test
    @DisplayName("Should return false when list is empty")
    void shouldReturnFalseWhenListIsEmpty() {
      // Arrange — Create an empty ScopeAllowList with no entries
      ScopeAllowList list = new ScopeAllowList();

      // Act — Check if any scope is allowed on the empty list
      boolean result = list.allowed("tenant", "create");

      // Assert — An empty list should never allow any scope
      assertFalse(result, "Should return false when there are no scopes in the list");
    }
  }
}
