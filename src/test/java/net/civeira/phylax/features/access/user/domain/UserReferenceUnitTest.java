package net.civeira.phylax.features.access.user.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserReferenceUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test User reference contruction")
  void test_user_reference_builder() {
    UserReference ref = UserReference.of("one");
    Assertions.assertEquals("one", ref.getUid());
  }
}
