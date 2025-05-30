package net.civeira.phylax.features.access.tenantconfig.application.usecase.delete;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.tenantconfig.TenantConfig;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TenantConfigDeleteEvent {

  /**
   * @autogenerated DeleteEventGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated DeleteEventGenerator
   */
  @NonNull
  private final TenantConfig payload;
}
