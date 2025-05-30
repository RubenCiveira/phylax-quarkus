package net.civeira.phylax.features.access.user.application.usecase.unlock;

import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.common.security.AllowProposal;
import net.civeira.phylax.features.access.user.UserRef;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UserAllowUnlockProposal extends AllowProposal {

  /**
   * @autogenerated AllowActionChangeProposalGenerator
   */
  private final UserRef reference;

  /**
   * @autogenerated AllowActionChangeProposalGenerator
   * @return
   */
  public String actionName() {
    return "unlock";
  }

  /**
   * @autogenerated AllowActionChangeProposalGenerator
   * @return
   */
  public Optional<UserRef> getReference() {
    return Optional.ofNullable(reference);
  }

  /**
   * @autogenerated AllowActionChangeProposalGenerator
   * @return
   */
  public String resourceName() {
    return "user";
  }
}
