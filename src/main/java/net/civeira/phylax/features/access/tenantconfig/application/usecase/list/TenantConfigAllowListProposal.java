package net.civeira.phylax.features.access.tenantconfig.application.usecase.list;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.common.security.AllowProposal;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TenantConfigAllowListProposal extends AllowProposal {

  /**
   * @autogenerated AllowListProposalGenerator
   * @return
   */
  public String actionName() {
    return "list";
  }

  /**
   * @autogenerated AllowListProposalGenerator
   * @return
   */
  public String resourceName() {
    return "tenant-config";
  }
}
