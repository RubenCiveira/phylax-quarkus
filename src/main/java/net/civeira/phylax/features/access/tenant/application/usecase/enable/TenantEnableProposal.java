package net.civeira.phylax.features.access.tenant.application.usecase.enable;

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
public class TenantEnableProposal {

  /**
   * @autogenerated ActionChangeProposalGenerator
   */
  @NonNull
  private final Tenant entity;

  /**
   * @autogenerated ActionChangeProposalGenerator
   */
  @NonNull
  private final Interaction interaction;
}
