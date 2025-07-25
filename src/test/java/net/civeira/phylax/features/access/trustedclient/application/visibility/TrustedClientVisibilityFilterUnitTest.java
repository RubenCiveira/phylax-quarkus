package net.civeira.phylax.features.access.trustedclient.application.visibility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TrustedClientVisibilityFilterUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test a entity reference contruction")
  void test_trusted_client_visibility_filter_builder() {
    TrustedClientVisibilityFilter cursor = TrustedClientVisibilityFilter.builder().build();
    Assertions.assertFalse(cursor.getUid().isPresent(),
        "Since must be empty if cursor is build without it");
    cursor.setUid("one");
    Assertions.assertEquals("one", cursor.getUid().get(),
        "Since Uid must be the same as the assigned");
  }
}
