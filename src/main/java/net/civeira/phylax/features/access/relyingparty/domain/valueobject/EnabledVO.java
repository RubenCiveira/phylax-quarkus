package net.civeira.phylax.features.access.relyingparty.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.AbstractFailList;
import net.civeira.phylax.common.value.validation.ConstraintFail;
import net.civeira.phylax.common.value.validation.ConstraintFailList;

/**
 * Value Object representing una marca que permite quitar el acceso a una cuenta sin borrarla of
 * relyingParty.
 * <p>
 * This class ensures that the enabled is a {@code String} and encapsulates its validation and
 * formatting logic. It is immutable and compliant with DDD (Domain-Driven Design) principles.
 * </p>
 * <p>
 * Use {@link #from(String)} or {@link #tryFrom(Object)} to safely create instances. Validation
 * errors are reported via {@link ConstraintFailList} or thrown as {@link ConstraintException}.
 * </p>
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class EnabledVO {

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param enabled
   * @return An empty instance
   */
  public static EnabledVO from(final Boolean enabled) {
    return tryFrom(enabled);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @return An empty instance
   */
  public static EnabledVO nullValue() {
    return new EnabledVO(null);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param enabled
   * @return An empty instance
   */
  public static EnabledVO tryFrom(final Object enabled) {
    ConstraintFailList list = new ConstraintFailList();
    EnabledVO result = tryFrom(enabled, list);
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    return result;
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param enabled temptative value
   * @param fails Error list
   * @return An empty instance
   */
  public static <T extends AbstractFailList> EnabledVO tryFrom(final Object enabled,
      final T fails) {
    if (null == enabled) {
      return new EnabledVO(null);
    } else if (enabled instanceof Boolean castedEnabled) {
      return new EnabledVO(castedEnabled);
    } else {
      fails.add(new ConstraintFail("wrong-type", "enabled", enabled.getClass(),
          "A Boolean type is expected for enabled"));
      return null;
    }
  }

  /**
   * The actual value of the name.
   *
   * @autogenerated ValueObjectGenerator
   */
  private final Boolean value;

  /**
   * Get the vo value
   *
   * @autogenerated ValueObjectGenerator
   * @return
   */
  public Boolean isEnabled() {
    return Boolean.TRUE.equals(value);
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
