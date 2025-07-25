package net.civeira.phylax.features.access.useraccesstemporalcode.domain.valueobject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.civeira.phylax.common.exception.ConstraintException;

class RecoveryCodeVOUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test value object contruction for property recovery code of user access temporal code ")
  void test_recovery_code_v_o_builder() {
    Assertions.assertFalse(RecoveryCodeVO.nullValue().getRecoveryCode().isPresent(),
        "A empty vo should have a null value");
    Assertions.assertFalse(RecoveryCodeVO.tryFrom(null).getRecoveryCode().isPresent(),
        "With a null value, no present result");
    Assertions.assertEquals("one", RecoveryCodeVO.from("one").getRecoveryCode().orElse(null));
    Object falingInstance = new Object() {};
    Assertions.assertThrows(ConstraintException.class,
        () -> RecoveryCodeVO.tryFrom(falingInstance));
    Assertions.assertEquals("[" + "one" + "]", RecoveryCodeVO.from("one").toString());
  }
}
