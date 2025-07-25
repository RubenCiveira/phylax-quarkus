package net.civeira.phylax.features.access.useracceptedtermnsofuse.domain.gateway;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.features.access.useracceptedtermnsofuse.domain.UserAcceptedTermnsOfUse;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserAcceptedTermnsOfUseCached {

  /**
   * @autogenerated CachedGenerator
   */
  @NonNull
  private OffsetDateTime since;

  /**
   * the real value
   *
   * @autogenerated CachedGenerator
   */
  @NonNull
  private List<UserAcceptedTermnsOfUse> value;

  /**
   * @autogenerated CachedGenerator
   * @return
   */
  public Optional<UserAcceptedTermnsOfUse> first() {
    return value.isEmpty() ? Optional.empty() : Optional.of(value.get(0));
  }
}
