/* @autogenerated */
package net.civeira.phylax.common.value.validation.validators;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import net.civeira.phylax.common.value.validation.ValidationResult;
import net.civeira.phylax.common.value.validation.Validator;

/**
 * Validator for date and time ranges. Allows validation of both date-only and date-time formats.
 */
public class DateWithRangeValidator implements Validator<String> {
  private final DateTimeFormatter formatter;
  private final Duration minDuration;
  private final Duration maxDuration;
  private final String errorMessage;
  private final boolean allowTimeComponent;

  /**
   * Constructs a DateWithRangeValidator.
   *
   * @param dateFormat The expected date format (e.g., "yyyy-MM-dd" or "yyyy-MM-dd HH:mm:ss").
   * @param minDuration The minimum allowed duration from the current date.
   * @param maxDuration The maximum allowed duration from the current date.
   * @param errorMessage The error message to return when validation fails.
   * @param allowTimeComponent If true, the validator will support time components.
   * @throws IllegalArgumentException if minDuration is greater than maxDuration.
   */
  public DateWithRangeValidator(String dateFormat, Duration minDuration, Duration maxDuration,
      String errorMessage, boolean allowTimeComponent) {
    if (minDuration.compareTo(maxDuration) > 0) {
      throw new IllegalArgumentException(
          "Minimum duration cannot be greater than maximum duration.");
    }
    this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    this.minDuration = minDuration;
    this.maxDuration = maxDuration;
    this.errorMessage = errorMessage;
    this.allowTimeComponent = allowTimeComponent;
  }

  /**
   * Validates whether the given date string is within the configured range.
   *
   * @param dateStr The date string to validate.
   * @return A ValidationResult indicating success or failure.
   */
  @Override
  public ValidationResult validate(String dateStr) {
    if (dateStr == null || dateStr.isEmpty()) {
      return new ValidationResult(errorMessage);
    }

    try {
      LocalDateTime dateTime;
      if (allowTimeComponent) {
        dateTime = LocalDateTime.parse(dateStr, formatter);
      } else {
        LocalDate dayDate = LocalDate.parse(dateStr, formatter);
        dateTime = dayDate.atStartOfDay();
      }

      LocalDateTime now = LocalDateTime.now();
      LocalDateTime minDate = now.plus(minDuration);
      LocalDateTime maxDate = now.plus(maxDuration);

      if (dateTime.isBefore(minDate) || dateTime.isAfter(maxDate)) {
        return new ValidationResult(errorMessage);
      }
      return new ValidationResult();
    } catch (DateTimeParseException e) {
      return new ValidationResult(errorMessage);
    }
  }
}
