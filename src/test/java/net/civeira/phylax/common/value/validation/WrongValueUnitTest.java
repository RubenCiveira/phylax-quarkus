package net.civeira.phylax.common.value.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("WrongValue validation error representation")
class WrongValueUnitTest {

  @Nested
  @DisplayName("Builder and getters")
  class BuilderAndGetters {

    @Test
    @DisplayName("Should build WrongValue with all fields")
    void shouldBuildWrongValueWithAllFields() {
      // Arrange / Act — Build a WrongValue with field, wrongValue, and errorMessage populated
      WrongValue wv = WrongValue.builder().field("email").wrongValue("not-an-email")
          .errorMessage("Invalid email format").build();

      // Assert — Verify all getters return the values provided to the builder
      assertEquals("email", wv.getField(), "Field should match the value set in builder");
      assertEquals("not-an-email", wv.getWrongValue(),
          "Wrong value should match the value set in builder");
      assertEquals("Invalid email format", wv.getErrorMessage(),
          "Error message should match the value set in builder");
    }

    @Test
    @DisplayName("Should allow null fields in WrongValue")
    void shouldAllowNullFieldsInWrongValue() {
      // Arrange / Act — Build a WrongValue without setting any fields
      WrongValue wv = WrongValue.builder().build();

      // Assert — Verify all getters return null when no values are set
      assertNull(wv.getField(), "Field should be null when not set");
      assertNull(wv.getWrongValue(), "Wrong value should be null when not set");
      assertNull(wv.getErrorMessage(), "Error message should be null when not set");
    }

    @Test
    @DisplayName("Should accept any object type as wrong value")
    void shouldAcceptAnyObjectTypeAsWrongValue() {
      // Arrange / Act — Build a WrongValue with an Integer as the wrong value
      WrongValue wv = WrongValue.builder().field("age").wrongValue(Integer.valueOf(-5))
          .errorMessage("Age must be positive").build();

      // Assert — Verify the wrong value holds the Integer object correctly
      assertEquals(-5, wv.getWrongValue(), "Wrong value should hold an Integer object");
    }
  }

  @Nested
  @DisplayName("Equality")
  class Equality {

    @Test
    @DisplayName("Should be equal when all fields are the same")
    void shouldBeEqualWhenAllFieldsAreTheSame() {
      // Arrange — Build two WrongValue instances with identical field values
      WrongValue wv1 =
          WrongValue.builder().field("name").wrongValue("").errorMessage("Required").build();
      WrongValue wv2 =
          WrongValue.builder().field("name").wrongValue("").errorMessage("Required").build();

      // Act / Assert — Verify the two instances are considered equal
      assertEquals(wv1, wv2, "Two WrongValues with identical fields should be equal");
    }

    @Test
    @DisplayName("Should not be equal when fields differ")
    void shouldNotBeEqualWhenFieldsDiffer() {
      // Arrange — Build two WrongValue instances with different field names
      WrongValue wv1 =
          WrongValue.builder().field("name").wrongValue("").errorMessage("Required").build();
      WrongValue wv2 =
          WrongValue.builder().field("email").wrongValue("").errorMessage("Required").build();

      // Act / Assert — Verify the two instances are not considered equal
      assertNotEquals(wv1, wv2, "WrongValues with different field names should not be equal");
    }
  }
}
