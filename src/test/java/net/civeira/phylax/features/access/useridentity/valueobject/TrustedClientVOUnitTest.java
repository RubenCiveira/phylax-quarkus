package net.civeira.phylax.features.access.useridentity.valueobject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.ConstraintFailList;
import net.civeira.phylax.features.access.trustedclient.TrustedClientReference;

class TrustedClientVOUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test value object contruction for property trusted client of user identity ")
  void test_trusted_client_v_o_builder() {
    ConstraintFailList fails = new ConstraintFailList();
    Assertions.assertEquals("10",
        TrustedClientVO.fromReference("10").getReferenceValue().orElse(null),
        "The builder must store the reference value");
    Assertions.assertEquals("11",
        TrustedClientVO.tryFromReference("11").getReferenceValue().orElse(null),
        "The builder must store the reference value");
    Assertions.assertEquals("12",
        TrustedClientVO.tryFromReference("12", fails).getReferenceValue().orElse(null),
        "The builder must store the reference value");
    Assertions.assertTrue(fails.isEmpty());
    Assertions.assertFalse(TrustedClientVO.nullValue().getValue().isPresent(),
        "A empty vo should have a null value");
    Assertions.assertFalse(TrustedClientVO.tryFrom(null).getValue().isPresent(),
        "With a null value, no present result");
    Assertions.assertFalse(TrustedClientVO.fromReference(null).getValue().isPresent(),
        "With a null value, no present result");
    Assertions.assertEquals(TrustedClientReference.of("one"),
        TrustedClientVO.from(TrustedClientReference.of("one")).getValue().orElse(null));
    Assertions.assertThrows(ConstraintException.class,
        () -> TrustedClientVO.tryFrom(new Object() {}));
    Assertions.assertEquals("[" + TrustedClientReference.of("one") + "]",
        TrustedClientVO.from(TrustedClientReference.of("one")).toString());
  }
}
