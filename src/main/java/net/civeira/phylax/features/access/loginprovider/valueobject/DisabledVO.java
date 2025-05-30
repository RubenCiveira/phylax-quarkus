package net.civeira.phylax.features.access.loginprovider.valueobject;

import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.AbstractFailList;
import net.civeira.phylax.common.value.validation.ConstraintFail;
import net.civeira.phylax.common.value.validation.ConstraintFailList;

/**
 * Value Object representing indicates if this provider is currently disabled. of loginProvider.
 * <p>
 * This class ensures that the disabled is a {@code String} and encapsulates its validation and
 * formatting logic. It is immutable and compliant with DDD (Domain-Driven Design) principles.
 * </p>
 * <p>
 * Use {@link #from(String)} or {@link #tryFrom(Object)} to safely create instances. Validation
 * errors are reported via {@link ConstraintFailList} or thrown as {@link ConstraintException}.
 * </p>
 */
@Getter
@RequiredArgsConstructor
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DisabledVO {

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @param disabled
   * @return An empty instance
   */
  public static DisabledVO from(final Boolean disabled) {
    return tryFrom(disabled);
  }

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @return An empty instance
   */
  public static DisabledVO nullValue() {
    return new DisabledVO(null);
  }

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @param disabled
   * @return An empty instance
   */
  public static DisabledVO tryFrom(final Object disabled) {
    ConstraintFailList list = new ConstraintFailList();
    DisabledVO result = tryFrom(disabled, list);
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    return result;
  }

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @param disabled temptative value
   * @param fails Error list
   * @return An empty instance
   */
  public static <T extends AbstractFailList> DisabledVO tryFrom(final Object disabled,
      final T fails) {
    if (null == disabled) {
      return new DisabledVO(null);
    } else if (disabled instanceof Boolean castedDisabled) {
      return new DisabledVO(castedDisabled);
    } else {
      fails.add(new ConstraintFail("wrong-type", "disabled", disabled.getClass(),
          "A Boolean type is expected for disabled"));
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
   * @autogenerated ValueObjectGenerator
   * @return
   */
  public Optional<Boolean> getValue() {
    return Optional.ofNullable(value);
  }

  /**
   * the string value wrapped in square brackets.
   *
   * @autogenerated ValueObjectGenerator
   * @return the string value wrapped in square brackets.
   */
  @Override
  public String toString() {
    return "[" + value + "]";
  }
}
