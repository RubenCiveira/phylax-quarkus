package net.civeira.phylax.features.access.securityscope.application.usecase.list;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.common.security.AllowDecision;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class SecurityScopeListAllowDecision extends AllowDecision {

  /**
   * @autogenerated ListAllowDecisionGenerator
   * @return
   */
  public String actionName() {
    return "list";
  }

  /**
   * @autogenerated ListAllowDecisionGenerator
   * @return
   */
  public String resourceName() {
    return "security-scope";
  }
}
