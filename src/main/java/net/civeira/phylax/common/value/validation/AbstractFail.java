/* @autogenerated */
package net.civeira.phylax.common.value.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import net.civeira.phylax.common.value.YamlLocaleMessages;
import net.civeira.phylax.common.value.validation.AbstractFail.LocalizedFail.LocalizedFailBuilder;

/**
 * Represents a generic failure structure used for validation or error reporting. Supports
 * localization for error messages and field violations.
 */
@Getter
@SuperBuilder
@RegisterForReflection
public abstract class AbstractFail {

  /**
   * Represents a localized failure message with a description and details about the violation.
   */
  @Getter
  @Builder(toBuilder = true)
  @Jacksonized
  @RegisterForReflection
  public static class LocalizedFail {
    private final String code;
    private final String description;
    private final String violation;
    private final List<LocalizedWrongValue> wrongValues;
  }

  /**
   * Represents a localized incorrect value, including the affected field and an error message.
   */
  @Getter
  @Builder
  @Jacksonized
  @RegisterForReflection
  public static class LocalizedWrongValue {
    private final String field;
    private final Object wrongValue;
    private final String errorMessage;
  }

  /** Stores localized messages for different locales. */
  private static Map<Locale, YamlLocaleMessages> localizedMessages;

  /**
   * Retrieves localized messages for a specific locale. If messages are not available, they are
   * loaded and cached.
   *
   * @param locale The locale for which messages should be retrieved.
   * @return A {@link YamlLocaleMessages} instance containing localized error messages.
   */
  private static synchronized YamlLocaleMessages getMessages(Locale locale) {
    if (localizedMessages == null) {
      localizedMessages = new HashMap<>();
    }
    localizedMessages.computeIfAbsent(locale,
        key -> YamlLocaleMessages.load("/messages/errors", key));
    return localizedMessages.get(locale);
  }

  /**
   * Updates the localized messages for a given locale.
   *
   * @param locale The locale for which the messages should be set.
   * @param message The localized messages to store.
   */
  /* default */ static synchronized void setLocalizedMessages(Locale locale,
      YamlLocaleMessages message) {
    getMessages(locale);
    localizedMessages.put(locale, message);
  }

  private final String code;
  private final String violation;
  private final List<WrongValue> wrongValues;

  /**
   * Constructs an {@code AbstractFail} instance with a specific field failure.
   *
   * @param code The failure code.
   * @param field The affected field.
   * @param wrongValue The incorrect value provided.
   */
  public AbstractFail(String code, String field, Object wrongValue) {
    this.code = code;
    this.violation = null;
    this.wrongValues = List.of(WrongValue.builder().field(field).wrongValue(wrongValue).build());
  }

  /**
   * Constructs an {@code AbstractFail} instance with a specific field failure and an error message.
   *
   * @param code The failure code.
   * @param field The affected field.
   * @param wrongValue The incorrect value provided.
   * @param errorMessage The error message associated with the failure.
   */
  public AbstractFail(String code, String field, Object wrongValue, String errorMessage) {
    this.code = code;
    this.violation = null;
    this.wrongValues = List.of(WrongValue.builder().field(field).wrongValue(wrongValue)
        .errorMessage(errorMessage).build());
  }

  /**
   * Constructs an {@code AbstractFail} instance from an exception.
   *
   * @param code The failure code.
   * @param source The exception that caused the failure.
   */
  public AbstractFail(String code, Exception source) {
    this.code = code;
    this.violation = source.getMessage();
    this.wrongValues = List.of();
  }

  /**
   * Converts the failure instance into a localized version with translated error messages.
   *
   * @param locale The target locale for localization.
   * @param withSource Whether to include the source violation in the localized output.
   * @return A {@link LocalizedFail} instance with translated messages.
   */
  public LocalizedFail localize(Locale locale, boolean withSource) {
    YamlLocaleMessages messages = getMessages(locale);
    LocalizedFailBuilder builder = LocalizedFail.builder()
        .code(messages.contains(code + ".code") ? messages.get(code + ".code") : code);
    if (messages.contains(code + ".description")) {
      builder.description(messages.get(code + ".description"));
    }
    List<LocalizedWrongValue> wrongs = new ArrayList<>();
    for (WrongValue wrongValue : wrongValues) {
      String key = code + ".field." + wrongValue.getField();
      wrongs.add(LocalizedWrongValue.builder().field(wrongValue.getField())
          .wrongValue(wrongValue.getWrongValue())
          .errorMessage(messages.contains(key) ? messages.get(key) : wrongValue.getErrorMessage())
          .build());
    }
    if (withSource && violation != null
        && !"false".equals(messages.get(code + ".show-violation"))) {
      builder = builder.violation(violation);
    }
    return builder.wrongValues(wrongs).build();
  }
}
