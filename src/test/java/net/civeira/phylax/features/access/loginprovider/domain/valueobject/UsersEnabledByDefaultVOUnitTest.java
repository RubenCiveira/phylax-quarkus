package net.civeira.phylax.features.access.loginprovider.domain.valueobject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.civeira.phylax.common.exception.ConstraintException;

class UsersEnabledByDefaultVOUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test value object contruction for property users enabled by default of login provider ")
  void test_users_enabled_by_default_v_o_builder() {
    Assertions.assertThrows(ConstraintException.class, () -> UsersEnabledByDefaultVO.tryFrom(null));
    Assertions.assertEquals(true, UsersEnabledByDefaultVO.from(true).isUsersEnabledByDefault());
    Object falingInstance = new Object() {};
    Assertions.assertThrows(ConstraintException.class,
        () -> UsersEnabledByDefaultVO.tryFrom(falingInstance));
    Assertions.assertEquals("[" + true + "]", UsersEnabledByDefaultVO.from(true).toString());
  }
}
