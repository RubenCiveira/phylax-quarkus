/* @autogenerated */
package net.civeira.phylax.common.value.validation.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateWithRangeValidatorUnitTest {

  private DateTimeFormatter dateFormatter;
  private DateTimeFormatter dateTimeFormatter;

  @BeforeEach
  void setUp() {
    dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  }

  @Test
  void shouldValidateDateWithinRange() {
    LocalDate now = LocalDate.now();
    String validDate = now.format(dateFormatter);

    DateWithRangeValidator validator = new DateWithRangeValidator("yyyy-MM-dd",
        Duration.ofDays(-30), Duration.ofDays(30), "Error", false);
    assertTrue(validator.validate(validDate).isValid());
  }

  @Test
  void shouldValidateDateTimeWithinRange() {
    LocalDateTime now = LocalDateTime.now();
    String validDateTime = now.format(dateTimeFormatter);

    DateWithRangeValidator validator = new DateWithRangeValidator("yyyy-MM-dd HH:mm:ss",
        Duration.ofDays(-30), Duration.ofDays(30), "Error", true);
    assertTrue(validator.validate(validDateTime).isValid());
  }

  @Test
  void shouldInvalidateDateOutOfRange() {
    DateWithRangeValidator validator = new DateWithRangeValidator("yyyy-MM-dd",
        Duration.ofDays(-10), Duration.ofDays(10), "Error", false);
    assertFalse(validator.validate("2023-01-01").isValid());
  }

  @Test
  void shouldInvalidateMalformedDate() {
    DateWithRangeValidator validator = new DateWithRangeValidator("yyyy-MM-dd",
        Duration.ofDays(-30), Duration.ofDays(30), "Error", false);
    assertFalse(validator.validate("invalid-date").isValid());
  }

  @Test
  void shouldInvalidateWithInconsistentDurationRange() {
    Duration min = Duration.ofDays(30);
    Duration max = Duration.ofDays(10);
    assertThrows(IllegalArgumentException.class,
        () -> new DateWithRangeValidator("yyyy-MM-dd", min, max, "Error", false));
  }

  @Test
  void shouldInvalidateNullDate() {
    DateWithRangeValidator validator = new DateWithRangeValidator("yyyy-MM-dd",
        Duration.ofDays(-30), Duration.ofDays(30), "Error", false);
    assertFalse(validator.validate(null).isValid());
  }

  @Test
  void shouldInvalidateEmptyDate() {
    DateWithRangeValidator validator = new DateWithRangeValidator("yyyy-MM-dd",
        Duration.ofDays(-30), Duration.ofDays(30), "Error", false);
    assertFalse(validator.validate("").isValid());
  }

  @Test
  void shouldInvalidateDateBeforeMinRange() {
    LocalDate pastDate = LocalDate.now().plusDays(-31);
    String formattedDate = pastDate.format(dateFormatter);

    DateWithRangeValidator validator = new DateWithRangeValidator("yyyy-MM-dd",
        Duration.ofDays(-30), Duration.ofDays(30), "Error", false);
    assertFalse(validator.validate(formattedDate).isValid());
  }

  @Test
  void shouldInvalidateDateAfterMaxRange() {
    LocalDate futureDate = LocalDate.now().plusDays(31);
    String formattedDate = futureDate.format(dateFormatter);

    DateWithRangeValidator validator = new DateWithRangeValidator("yyyy-MM-dd",
        Duration.ofDays(-30), Duration.ofDays(30), "Error", false);
    assertFalse(validator.validate(formattedDate).isValid());
  }

  @Test
  void shouldInvalidateDateTimeBeforeMinRange() {
    LocalDateTime pastDateTime = LocalDateTime.now().plusDays(-31);
    String formattedDateTime = pastDateTime.format(dateTimeFormatter);

    DateWithRangeValidator validator = new DateWithRangeValidator("yyyy-MM-dd HH:mm:ss",
        Duration.ofDays(-30), Duration.ofDays(30), "Error", true);
    assertFalse(validator.validate(formattedDateTime).isValid());
  }

  @Test
  void shouldInvalidateDateTimeAfterMaxRange() {
    LocalDateTime futureDateTime = LocalDateTime.now().plusDays(31);
    String formattedDateTime = futureDateTime.format(dateTimeFormatter);

    DateWithRangeValidator validator = new DateWithRangeValidator("yyyy-MM-dd HH:mm:ss",
        Duration.ofDays(-30), Duration.ofDays(30), "Error", true);
    assertFalse(validator.validate(formattedDateTime).isValid());
  }
}
