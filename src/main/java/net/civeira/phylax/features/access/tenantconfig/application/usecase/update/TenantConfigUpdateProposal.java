package net.civeira.phylax.features.access.tenantconfig.application.usecase.update;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.tenantconfig.TenantConfigRef;
import net.civeira.phylax.features.access.tenantconfig.application.request.TenantConfigStateChange;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TenantConfigUpdateProposal {

  /**
   * @autogenerated UpdateProposalGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated UpdateProposalGenerator
   */
  private final TenantConfigRef reference;

  /**
   * @autogenerated UpdateProposalGenerator
   */
  @NonNull
  private TenantConfigStateChange dto;

  /**
   * @autogenerated UpdateProposalGenerator
   * @return
   */
  public Optional<TenantConfigRef> getReference() {
    return Optional.ofNullable(reference);
  }

  /**
   * @autogenerated UpdateProposalGenerator
   * @param mapper
   */
  public void map(UnaryOperator<TenantConfigStateChange> mapper) {
    dto = mapper.apply(dto);
  }

  /**
   * @autogenerated UpdateProposalGenerator
   * @param mapper
   */
  public void peek(Consumer<TenantConfigStateChange> mapper) {
    mapper.accept(dto);
  }
}
