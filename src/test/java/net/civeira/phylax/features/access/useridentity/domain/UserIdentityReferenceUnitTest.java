package net.civeira.phylax.features.access.useridentity.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserIdentityReferenceUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test UserIdentity reference contruction")
  void test_user_identity_reference_builder() {
    UserIdentityReference ref = UserIdentityReference.of("one");
    Assertions.assertEquals("one", ref.getUid());
  }
}
