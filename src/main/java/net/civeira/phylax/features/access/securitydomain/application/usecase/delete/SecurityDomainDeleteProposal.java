package net.civeira.phylax.features.access.securitydomain.application.usecase.delete;

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
public class SecurityDomainDeleteProposal {

  /**
   * @autogenerated DeleteProposalGenerator
   */
  @NonNull
  private final SecurityDomain entity;

  /**
   * @autogenerated DeleteProposalGenerator
   */
  @NonNull
  private final Interaction interaction;
}
