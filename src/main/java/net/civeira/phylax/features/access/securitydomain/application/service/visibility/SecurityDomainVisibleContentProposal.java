package net.civeira.phylax.features.access.securitydomain.application.service.visibility;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.securitydomain.SecurityDomain;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SecurityDomainVisibleContentProposal {

  /**
   * @autogenerated GuardProposalGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated GuardProposalGenerator
   */
  @NonNull
  private SecurityDomain entity;

  /**
   * @autogenerated GuardProposalGenerator
   */
  @NonNull
  private Boolean visible;
}
