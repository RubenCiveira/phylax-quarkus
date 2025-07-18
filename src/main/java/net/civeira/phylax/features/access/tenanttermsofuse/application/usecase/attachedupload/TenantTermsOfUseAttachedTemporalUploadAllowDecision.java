package net.civeira.phylax.features.access.tenanttermsofuse.application.usecase.attachedupload;

import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.common.security.AllowDecision;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.TenantTermsOfUseRef;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TenantTermsOfUseAttachedTemporalUploadAllowDecision extends AllowDecision {

  /**
   * @autogenerated TemporalUploadAllowDecisionGenerator
   */
  private final TenantTermsOfUseRef reference;

  /**
   * @autogenerated TemporalUploadAllowDecisionGenerator
   * @return
   */
  public String actionName() {
    return "retrieve";
  }

  /**
   * @autogenerated TemporalUploadAllowDecisionGenerator
   * @return
   */
  public Optional<TenantTermsOfUseRef> getReference() {
    return Optional.ofNullable(reference);
  }

  /**
   * @autogenerated TemporalUploadAllowDecisionGenerator
   * @return
   */
  public String resourceName() {
    return "tenant-terms-of-use";
  }
}
