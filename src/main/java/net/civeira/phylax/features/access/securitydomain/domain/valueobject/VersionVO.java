package net.civeira.phylax.features.access.securitydomain.domain.valueobject;

import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.AbstractFailList;
import net.civeira.phylax.common.value.validation.ConstraintFail;
import net.civeira.phylax.common.value.validation.ConstraintFailList;

/**
 * Value Object representing campo con el número de version de security domain para controlar
 * bloqueos optimistas of securityDomain.
 * <p>
 * This class ensures that the version is a {@code String} and encapsulates its validation and
 * formatting logic. It is immutable and compliant with DDD (Domain-Driven Design) principles.
 * </p>
 * <p>
 * Use {@link #from(String)} or {@link #tryFrom(Object)} to safely create instances. Validation
 * errors are reported via {@link ConstraintFailList} or thrown as {@link ConstraintException}.
 * </p>
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class VersionVO {

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param version
   * @return An empty instance
   */
  public static VersionVO from(final Integer version) {
    return tryFrom(version);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @return An empty instance
   */
  public static VersionVO nullValue() {
    return new VersionVO(null);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param version
   * @return An empty instance
   */
  public static VersionVO tryFrom(final Object version) {
    ConstraintFailList list = new ConstraintFailList();
    VersionVO result = tryFrom(version, list);
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    return result;
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param version temptative value
   * @param fails Error list
   * @return An empty instance
   */
  public static <T extends AbstractFailList> VersionVO tryFrom(final Object version,
      final T fails) {
    if (null == version) {
      return new VersionVO(null);
    } else if (version instanceof Integer castedVersion) {
      return new VersionVO(castedVersion);
    } else {
      fails.add(new ConstraintFail("wrong-type", "version", version.getClass(),
          "A Integer type is expected for version"));
      return null;
    }
  }

  /**
   * The actual value of the name.
   *
   * @autogenerated ValueObjectGenerator
   */
  private final Integer value;

  /**
   * Get the vo value
   *
   * @autogenerated ValueObjectGenerator
   * @return
   */
  public Optional<Integer> getVersion() {
    return Optional.ofNullable(value);
  }

  /**
   * @autogenerated ValueObjectGenerator
   * @return Value
   */
  public Integer nextVersion() {
    return null == value ? value.intValue() + 1 : 1;
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
