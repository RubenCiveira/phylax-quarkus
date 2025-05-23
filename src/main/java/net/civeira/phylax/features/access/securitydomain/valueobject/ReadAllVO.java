package net.civeira.phylax.features.access.securitydomain.valueobject;

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
 * Value Object representing allow to read on every scope of securityDomain.
 * <p>
 * This class ensures that the read all is a {@code String} and encapsulates its validation and
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
public class ReadAllVO {

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @param readAll
   * @return An empty instance
   */
  public static ReadAllVO from(final Boolean readAll) {
    return tryFrom(readAll);
  }

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @return An empty instance
   */
  public static ReadAllVO nullValue() {
    return new ReadAllVO(null);
  }

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @param readAll
   * @return An empty instance
   */
  public static ReadAllVO tryFrom(final Object readAll) {
    ConstraintFailList list = new ConstraintFailList();
    ReadAllVO result = tryFrom(readAll, list);
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    return result;
  }

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @param readAll temptative value
   * @param fails Error list
   * @return An empty instance
   */
  public static <T extends AbstractFailList> ReadAllVO tryFrom(final Object readAll,
      final T fails) {
    if (null == readAll) {
      return new ReadAllVO(null);
    } else if (readAll instanceof Boolean castedReadAll) {
      return new ReadAllVO(castedReadAll);
    } else {
      fails.add(new ConstraintFail("wrong-type", "readAll", readAll.getClass(),
          "A Boolean type is expected for readAll"));
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
