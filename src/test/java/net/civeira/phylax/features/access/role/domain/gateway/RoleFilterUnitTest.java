package net.civeira.phylax.features.access.role.domain.gateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleFilterUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test a entity reference contruction")
  void test_role_filter_builder() {
    RoleFilter cursor = RoleFilter.builder().build();
    Assertions.assertFalse(cursor.getUid().isPresent(),
        "Since must be empty if cursor is build without it");
    cursor.setUid("one");
    Assertions.assertEquals("one", cursor.getUid().get(),
        "Since Uid must be the same as the assigned");
  }
}
