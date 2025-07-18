package net.civeira.phylax.features.access.useraccesstemporalcode.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserAccessTemporalCodeReferenceUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test UserAccessTemporalCode reference contruction")
  void test_user_access_temporal_code_reference_builder() {
    UserAccessTemporalCodeReference ref = UserAccessTemporalCodeReference.of("one");
    Assertions.assertEquals("one", ref.getUid());
  }
}
