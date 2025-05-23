/* @autogenerated */
package net.civeira.phylax.common.value.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExecutionFailUnitTest {

  @Test
  @DisplayName("Test the rigth build of the exception")
  void test_constructor() {
    ExecutionFail cf1 = new ExecutionFail("code", "wrong");
    Assertions.assertEquals("code", cf1.getCode(), "The fail must keep code value");
    Assertions.assertNull(cf1.getWrongValues().get(0).getField(),
        "The fail must have null field value");
    Assertions.assertNull(cf1.getWrongValues().get(0).getWrongValue(),
        "The fail must have null value");
    Assertions.assertEquals("wrong", cf1.getWrongValues().get(0).getErrorMessage(),
        "The fail must keep error message value");
  }
}
