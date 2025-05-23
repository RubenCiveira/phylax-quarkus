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
 * Value Object representing a metadata file required by some providers for configuration (e.g.,
 * SAML descriptor). of loginProvider.
 * <p>
 * This class ensures that the metadata is a {@code String} and encapsulates its validation and
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
public class MetadataVO {

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @param metadata
   * @return An empty instance
   */
  public static MetadataVO from(final String metadata) {
    return MetadataVO.tryFrom(metadata);
  }

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @param metadata
   * @return An empty instance
   */
  public static MetadataVO fromTemporal(final String metadata) {
    return new MetadataVO(true, metadata);
  }

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @return An empty instance
   */
  public static MetadataVO nullValue() {
    return new MetadataVO(false, null);
  }

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @param metadata
   * @return An empty instance
   */
  public static MetadataVO tryFrom(final Object metadata) {
    ConstraintFailList list = new ConstraintFailList();
    MetadataVO result = tryFrom(metadata, list);
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    return result;
  }

  /**
   * An empty instance
   *
   * @autogenerated ValueObjectGenerator
   * @param metadata temptative value
   * @param fails Error list
   * @return An empty instance
   */
  public static <T extends AbstractFailList> MetadataVO tryFrom(final Object metadata,
      final T fails) {
    boolean temporal = String.valueOf(metadata).startsWith("temp://");
    if (null == metadata) {
      return new MetadataVO(false, null);
    } else if (metadata instanceof String castedMetadata) {
      return new MetadataVO(temporal, temporal ? castedMetadata.substring(7) : castedMetadata);
    } else {
      fails.add(new ConstraintFail("wrong-type", "metadata", metadata.getClass(),
          "A String type is expected for metadata"));
      return null;
    }
  }

  /**
   * @autogenerated ValueObjectGenerator
   */
  private final boolean temporal;

  /**
   * The actual value of the name.
   *
   * @autogenerated ValueObjectGenerator
   */
  private final String value;

  /**
   * @autogenerated ValueObjectGenerator
   * @return
   */
  public Optional<String> getValue() {
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
