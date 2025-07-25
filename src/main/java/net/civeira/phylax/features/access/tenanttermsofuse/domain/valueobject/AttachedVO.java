package net.civeira.phylax.features.access.tenanttermsofuse.domain.valueobject;

import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.AbstractFailList;
import net.civeira.phylax.common.value.validation.ConstraintFail;
import net.civeira.phylax.common.value.validation.ConstraintFailList;

/**
 * Value Object representing an optional file attachment (e.g., PDF or signed document). of
 * tenantTermsOfUse.
 * <p>
 * This class ensures that the attached is a {@code String} and encapsulates its validation and
 * formatting logic. It is immutable and compliant with DDD (Domain-Driven Design) principles.
 * </p>
 * <p>
 * Use {@link #from(String)} or {@link #tryFrom(Object)} to safely create instances. Validation
 * errors are reported via {@link ConstraintFailList} or thrown as {@link ConstraintException}.
 * </p>
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class AttachedVO {

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param attached
   * @return An empty instance
   */
  public static AttachedVO from(final String attached) {
    return AttachedVO.tryFrom(attached);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param attached
   * @return An empty instance
   */
  public static AttachedVO fromTemporal(final String attached) {
    return new AttachedVO(true, attached);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @return An empty instance
   */
  public static AttachedVO nullValue() {
    return new AttachedVO(false, null);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param attached
   * @return An empty instance
   */
  public static AttachedVO tryFrom(final Object attached) {
    ConstraintFailList list = new ConstraintFailList();
    AttachedVO result = tryFrom(attached, list);
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    return result;
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param attached temptative value
   * @param fails Error list
   * @return An empty instance
   */
  public static <T extends AbstractFailList> AttachedVO tryFrom(final Object attached,
      final T fails) {
    boolean temporal = String.valueOf(attached).startsWith("temp://");
    if (null == attached) {
      return new AttachedVO(false, null);
    } else if (attached instanceof String castedAttached) {
      return new AttachedVO(temporal, temporal ? castedAttached.substring(7) : castedAttached);
    } else {
      fails.add(new ConstraintFail("wrong-type", "attached", attached.getClass(),
          "A String type is expected for attached"));
      return null;
    }
  }

  /**
   * @autogenerated ValueObjectGenerator
   */
  @Getter
  private final boolean temporal;

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
  public Optional<String> getAttached() {
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
