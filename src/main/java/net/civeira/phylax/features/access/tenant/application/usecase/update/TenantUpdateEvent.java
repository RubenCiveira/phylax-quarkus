package net.civeira.phylax.features.access.tenant.application.usecase.update;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.tenant.Tenant;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TenantUpdateEvent {

  /**
   * @autogenerated UpdateEventGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated UpdateEventGenerator
   */
  @NonNull
  private final Tenant payload;
}
