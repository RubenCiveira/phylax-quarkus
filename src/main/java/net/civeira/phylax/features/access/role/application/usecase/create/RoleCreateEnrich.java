package net.civeira.phylax.features.access.role.application.usecase.create;

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
public class RoleCreateEnrich {

  /**
   * @autogenerated CreateEnrichGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated CreateEnrichGenerator
   */
  @NonNull
  private RoleCreateInput input;

  /**
   * @autogenerated CreateEnrichGenerator
   * @param mapper
   */
  public void map(UnaryOperator<RoleCreateInput> mapper) {
    input = mapper.apply(input);
  }
}
