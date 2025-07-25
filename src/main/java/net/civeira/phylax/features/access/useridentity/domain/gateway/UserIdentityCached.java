package net.civeira.phylax.features.access.useridentity.domain.gateway;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.features.access.useridentity.domain.UserIdentity;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserIdentityCached {

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
  private List<UserIdentity> value;

  /**
   * @autogenerated CachedGenerator
   * @return
   */
  public Optional<UserIdentity> first() {
    return value.isEmpty() ? Optional.empty() : Optional.of(value.get(0));
  }
}
