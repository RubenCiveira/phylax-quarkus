package net.civeira.phylax.features.access.tenant.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.features.access.tenant.Tenant;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class TenantEvent {

  /**
   * @autogenerated DomainBaseEventGenerator
   */
  @NonNull
  private final Tenant payload;
}
