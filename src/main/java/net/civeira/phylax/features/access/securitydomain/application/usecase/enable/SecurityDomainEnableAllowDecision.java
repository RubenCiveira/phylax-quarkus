package net.civeira.phylax.features.access.securitydomain.application.usecase.enable;

import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.common.security.AllowDecision;
import net.civeira.phylax.features.access.securitydomain.domain.SecurityDomainRef;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class SecurityDomainEnableAllowDecision extends AllowDecision {

  /**
   * @autogenerated ActionAllowDecisionGenerator
   */
  private final SecurityDomainRef reference;

  /**
   * @autogenerated ActionAllowDecisionGenerator
   * @return
   */
  public String actionName() {
    return "enable";
  }

  /**
   * @autogenerated ActionAllowDecisionGenerator
   * @return
   */
  public Optional<SecurityDomainRef> getReference() {
    return Optional.ofNullable(reference);
  }

  /**
   * @autogenerated ActionAllowDecisionGenerator
   * @return
   */
  public String resourceName() {
    return "security-domain";
  }
}
