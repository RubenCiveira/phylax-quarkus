package net.civeira.phylax.features.access.tenant.application.usecase.create;

import java.util.function.UnaryOperator;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TenantCreateEnrich {

  /**
   * @autogenerated CreateEnrichGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated CreateEnrichGenerator
   */
  @NonNull
  private TenantCreateInput input;

  /**
   * @autogenerated CreateEnrichGenerator
   * @param mapper
   */
  public void map(UnaryOperator<TenantCreateInput> mapper) {
    input = mapper.apply(input);
  }
}
