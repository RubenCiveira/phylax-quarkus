package net.civeira.phylax.features.access.relyingparty.domain.gateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RelyingPartyFilterUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test a entity reference contruction")
  void test_relying_party_filter_builder() {
    RelyingPartyFilter cursor = RelyingPartyFilter.builder().build();
    Assertions.assertFalse(cursor.getUid().isPresent(),
        "Since must be empty if cursor is build without it");
    cursor.setUid("one");
    Assertions.assertEquals("one", cursor.getUid().get(),
        "Since Uid must be the same as the assigned");
  }
}
