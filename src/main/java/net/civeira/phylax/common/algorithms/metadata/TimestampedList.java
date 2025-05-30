/* @autogenerated */
package net.civeira.phylax.common.algorithms.metadata;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.EqualsAndHashCode;

/**
 * A list implementation that carries metadata indicating when the data was generated.
 *
 * <p>
 * This class is useful in scenarios where the data needs to be associated with a generation
 * timestamp, such as for caching, data freshness validation, or audit purposes.
 * </p>
 *
 * <p>
 * It wraps a standard {@link ArrayList} and includes a timestamp retrieved from a
 * {@link WrapMetadata} object, which provides both the data and its associated generation time.
 * </p>
 *
 * @param <T> the type of elements in this list
 */
@EqualsAndHashCode(callSuper = true)
public class TimestampedList<T> extends ArrayList<T> implements Timestamped {
  private static final long serialVersionUID = -2874202296198678629L;

  /**
   * The timestamp indicating when the data was generated or last updated.
   */
  private Instant since;

  /**
   * Constructs a {@code TimestampedList} from a {@link WrapMetadata} instance.
   *
   * @param items a {@code WrapMetadata} object containing both the list data and the timestamp when
   *        the data was generated
   */
  public TimestampedList(WrapMetadata<List<T>> items) {
    super(items.getData());
    this.since = items.getSince();
  }

  /**
   * Returns the timestamp indicating when the data was generated.
   *
   * @return an {@code Optional} containing the generation {@link Instant}, or empty if not set
   */
  @Override
  public Optional<Instant> getGeneratedAt() {
    return Optional.ofNullable(since);
  }
}
