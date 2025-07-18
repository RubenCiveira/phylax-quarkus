package net.civeira.phylax.features.access.securitydomain.domain.gateway;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.features.access.securitydomain.domain.SecurityDomain;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SecurityDomainCached {

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
  private List<SecurityDomain> value;

  /**
   * @autogenerated CachedGenerator
   * @return
   */
  public Optional<SecurityDomain> first() {
    return value.isEmpty() ? Optional.empty() : Optional.of(value.get(0));
  }
}
