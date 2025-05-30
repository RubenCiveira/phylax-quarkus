package net.civeira.phylax.features.access.loginprovider.application.service.visibility;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.loginprovider.query.LoginProviderFilter;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LoginProviderFilterProposal {

  /**
   * @autogenerated FilterProposalGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated FilterProposalGenerator
   */
  @NonNull
  private LoginProviderFilter filter;

  /**
   * @autogenerated FilterProposalGenerator
   * @param mapper
   */
  public void map(UnaryOperator<LoginProviderFilter> mapper) {
    filter = mapper.apply(filter);
  }

  /**
   * @autogenerated FilterProposalGenerator
   * @param mapper
   */
  public void peek(Consumer<LoginProviderFilter> mapper) {
    mapper.accept(filter);
  }
}
