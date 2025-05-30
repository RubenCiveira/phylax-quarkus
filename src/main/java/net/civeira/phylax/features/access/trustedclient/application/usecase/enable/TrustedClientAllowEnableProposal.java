package net.civeira.phylax.features.access.trustedclient.application.usecase.enable;

import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.common.security.AllowProposal;
import net.civeira.phylax.features.access.trustedclient.TrustedClientRef;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TrustedClientAllowEnableProposal extends AllowProposal {

  /**
   * @autogenerated AllowActionChangeProposalGenerator
   */
  private final TrustedClientRef reference;

  /**
   * @autogenerated AllowActionChangeProposalGenerator
   * @return
   */
  public String actionName() {
    return "enable";
  }

  /**
   * @autogenerated AllowActionChangeProposalGenerator
   * @return
   */
  public Optional<TrustedClientRef> getReference() {
    return Optional.ofNullable(reference);
  }

  /**
   * @autogenerated AllowActionChangeProposalGenerator
   * @return
   */
  public String resourceName() {
    return "trusted-client";
  }
}
