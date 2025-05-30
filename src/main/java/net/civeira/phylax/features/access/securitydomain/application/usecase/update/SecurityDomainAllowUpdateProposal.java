package net.civeira.phylax.features.access.securitydomain.application.usecase.update;

import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.common.security.AllowProposal;
import net.civeira.phylax.features.access.securitydomain.SecurityDomainRef;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class SecurityDomainAllowUpdateProposal extends AllowProposal {

  /**
   * @autogenerated AllowUpdateProposalGenerator
   */
  private final SecurityDomainRef reference;

  /**
   * @autogenerated AllowUpdateProposalGenerator
   * @return
   */
  public String actionName() {
    return "update";
  }

  /**
   * @autogenerated AllowUpdateProposalGenerator
   * @return
   */
  public Optional<SecurityDomainRef> getReference() {
    return Optional.ofNullable(reference);
  }

  /**
   * @autogenerated AllowUpdateProposalGenerator
   * @return
   */
  public String resourceName() {
    return "security-domain";
  }
}
