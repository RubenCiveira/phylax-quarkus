package net.civeira.phylax.features.access.tenanttermsofuse.application.usecase.retrieve;

import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.common.security.AllowProposal;
import net.civeira.phylax.features.access.tenanttermsofuse.TenantTermsOfUseRef;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TenantTermsOfUseAllowRetrieveProposal extends AllowProposal {

  /**
   * @autogenerated AllowRetrieveProposalGenerator
   */
  private final TenantTermsOfUseRef reference;

  /**
   * @autogenerated AllowRetrieveProposalGenerator
   * @return
   */
  public String actionName() {
    return "retrieve";
  }

  /**
   * @autogenerated AllowRetrieveProposalGenerator
   * @return
   */
  public Optional<TenantTermsOfUseRef> getReference() {
    return Optional.ofNullable(reference);
  }

  /**
   * @autogenerated AllowRetrieveProposalGenerator
   * @return
   */
  public String resourceName() {
    return "tenant-terms-of-use";
  }
}
