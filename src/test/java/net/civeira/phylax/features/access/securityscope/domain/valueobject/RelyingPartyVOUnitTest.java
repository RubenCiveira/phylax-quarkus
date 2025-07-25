package net.civeira.phylax.features.access.securityscope.domain.valueobject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.ConstraintFailList;
import net.civeira.phylax.features.access.relyingparty.domain.RelyingPartyReference;

class RelyingPartyVOUnitTest {

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Test
  @DisplayName("Test value object contruction for property relying party of security scope ")
  void test_relying_party_v_o_builder() {
    ConstraintFailList fails = new ConstraintFailList();
    Assertions.assertEquals("10",
        RelyingPartyVO.fromReference("10").getRelyingPartyUid().orElse(null),
        "The builder must store the reference value");
    Assertions.assertEquals("11",
        RelyingPartyVO.tryFromReference("11").getRelyingPartyUid().orElse(null),
        "The builder must store the reference value");
    Assertions.assertEquals("12",
        RelyingPartyVO.tryFromReference("12", fails).getRelyingPartyUid().orElse(null),
        "The builder must store the reference value");
    Assertions.assertTrue(fails.isEmpty());
    Assertions.assertFalse(RelyingPartyVO.nullValue().getRelyingParty().isPresent(),
        "A empty vo should have a null value");
    Assertions.assertFalse(RelyingPartyVO.tryFrom(null).getRelyingParty().isPresent(),
        "With a null value, no present result");
    Assertions.assertFalse(RelyingPartyVO.fromReference(null).getRelyingParty().isPresent(),
        "With a null value, no present result");
    Assertions.assertEquals(RelyingPartyReference.of("one"),
        RelyingPartyVO.from(RelyingPartyReference.of("one")).getRelyingParty().orElse(null));
    Assertions.assertThrows(ConstraintException.class,
        () -> RelyingPartyVO.tryFrom(new Object() {}));
    Assertions.assertEquals("[" + RelyingPartyReference.of("one") + "]",
        RelyingPartyVO.from(RelyingPartyReference.of("one")).toString());
  }
}
