package net.civeira.phylax.features.access.tenant.domain.valueobject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.civeira.phylax.common.exception.ConstraintException;

class EnabledVOUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test value object contruction for property enabled of tenant ")
  void test_enabled_v_o_builder() {
    Assertions.assertThrows(ConstraintException.class, () -> EnabledVO.tryFrom(null));
    Assertions.assertEquals(true, EnabledVO.from(true).isEnabled());
    Object falingInstance = new Object() {};
    Assertions.assertThrows(ConstraintException.class, () -> EnabledVO.tryFrom(falingInstance));
    Assertions.assertEquals("[" + true + "]", EnabledVO.from(true).toString());
  }
}
