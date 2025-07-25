package net.civeira.phylax.features.access.relyingparty.application.usecase.retrieve;

import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.common.security.AllowDecision;
import net.civeira.phylax.features.access.relyingparty.domain.RelyingPartyRef;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class RelyingPartyRetrieveAllowDecision extends AllowDecision {

  /**
   * @autogenerated RetrieveAllowDecisionGenerator
   */
  private final RelyingPartyRef reference;

  /**
   * @autogenerated RetrieveAllowDecisionGenerator
   * @return
   */
  public String actionName() {
    return "retrieve";
  }

  /**
   * @autogenerated RetrieveAllowDecisionGenerator
   * @return
   */
  public Optional<RelyingPartyRef> getReference() {
    return Optional.ofNullable(reference);
  }

  /**
   * @autogenerated RetrieveAllowDecisionGenerator
   * @return
   */
  public String resourceName() {
    return "relying-party";
  }
}
