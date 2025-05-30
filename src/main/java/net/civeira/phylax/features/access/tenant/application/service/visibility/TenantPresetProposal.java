package net.civeira.phylax.features.access.tenant.application.service.visibility;

import java.util.Optional;
import java.util.function.Consumer;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.tenant.Tenant;
import net.civeira.phylax.features.access.tenant.application.request.TenantStateChange;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TenantPresetProposal {

  /**
   * @autogenerated PresetProposalGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated PresetProposalGenerator
   */
  @NonNull
  private TenantStateChange dto;

  /**
   * @autogenerated PresetProposalGenerator
   */
  private Tenant original;

  /**
   * @autogenerated PresetProposalGenerator
   * @return
   */
  public Optional<Tenant> getOriginal() {
    return Optional.ofNullable(original);
  }

  /**
   * @autogenerated PresetProposalGenerator
   * @param mapper
   */
  public void peek(Consumer<TenantStateChange> mapper) {
    mapper.accept(dto);
  }
}
