package net.civeira.phylax.features.access.tenant.application.visibility;

import java.util.Optional;
import java.util.function.Consumer;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.tenant.domain.Tenant;
import net.civeira.phylax.features.access.tenant.domain.TenantChangeSet;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TenantEntityEnrichment {

  /**
   * @autogenerated EntityEnrichmentGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated EntityEnrichmentGenerator
   */
  @NonNull
  private TenantChangeSet dto;

  /**
   * @autogenerated EntityEnrichmentGenerator
   */
  private Tenant original;

  /**
   * @autogenerated EntityEnrichmentGenerator
   * @return
   */
  public Optional<Tenant> getOriginal() {
    return Optional.ofNullable(original);
  }

  /**
   * @autogenerated EntityEnrichmentGenerator
   * @param mapper
   */
  public void peek(Consumer<TenantChangeSet> mapper) {
    mapper.accept(dto);
  }
}
