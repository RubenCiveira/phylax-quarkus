package net.civeira.phylax.features.access.trustedclient.application.usecase.disable;

import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.common.security.AllowDecision;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientRef;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TrustedClientDisableAllowDecision extends AllowDecision {

  /**
   * @autogenerated ActionAllowDecisionGenerator
   */
  private final TrustedClientRef reference;

  /**
   * @autogenerated ActionAllowDecisionGenerator
   * @return
   */
  public String actionName() {
    return "disable";
  }

  /**
   * @autogenerated ActionAllowDecisionGenerator
   * @return
   */
  public Optional<TrustedClientRef> getReference() {
    return Optional.ofNullable(reference);
  }

  /**
   * @autogenerated ActionAllowDecisionGenerator
   * @return
   */
  public String resourceName() {
    return "trusted-client";
  }
}
