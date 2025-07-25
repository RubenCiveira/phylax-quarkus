package net.civeira.phylax.features.access.user.domain.valueobject;

import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.AbstractFailList;
import net.civeira.phylax.common.value.validation.ConstraintFail;
import net.civeira.phylax.common.value.validation.ConstraintFailList;

/**
 * Value Object representing an optional email used to send notifications to the user of user.
 * <p>
 * This class ensures that the email is a {@code String} and encapsulates its validation and
 * formatting logic. It is immutable and compliant with DDD (Domain-Driven Design) principles.
 * </p>
 * <p>
 * Use {@link #from(String)} or {@link #tryFrom(Object)} to safely create instances. Validation
 * errors are reported via {@link ConstraintFailList} or thrown as {@link ConstraintException}.
 * </p>
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class EmailVO {

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param email
   * @return An empty instance
   */
  public static EmailVO from(final String email) {
    return tryFrom(email);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @return An empty instance
   */
  public static EmailVO nullValue() {
    return new EmailVO(null);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param email
   * @return An empty instance
   */
  public static EmailVO tryFrom(final Object email) {
    ConstraintFailList list = new ConstraintFailList();
    EmailVO result = tryFrom(email, list);
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    return result;
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param email temptative value
   * @param fails Error list
   * @return An empty instance
   */
  public static <T extends AbstractFailList> EmailVO tryFrom(final Object email, final T fails) {
    if (null == email) {
      return new EmailVO(null);
    } else if (email instanceof String castedEmail) {
      return new EmailVO(castedEmail);
    } else {
      fails.add(new ConstraintFail("wrong-type", "email", email.getClass(),
          "A String type is expected for email"));
      return null;
    }
  }

  /**
   * The actual value of the name.
   *
   * @autogenerated ValueObjectGenerator
   */
  private final String value;

  /**
   * Get the vo value
   *
   * @autogenerated ValueObjectGenerator
   * @return
   */
  public Optional<String> getEmail() {
    return Optional.ofNullable(value);
  }

  /**
   * Returns a formatted string representation of the name.
   *
   * @autogenerated ValueObjectGenerator
   * @return the string value wrapped in square brackets.
   */
  @Override
  public String toString() {
    return "[" + value + "]";
  }
}
