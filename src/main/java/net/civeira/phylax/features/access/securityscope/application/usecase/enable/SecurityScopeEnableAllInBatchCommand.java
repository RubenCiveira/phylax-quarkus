package net.civeira.phylax.features.access.securityscope.application.usecase.enable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.securityscope.application.visibility.SecurityScopeVisibilityFilter;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SecurityScopeEnableAllInBatchCommand {

  /**
   * The filter to select the results to retrieve
   *
   * @autogenerated EntityGenerator
   */
  @NonNull
  private final SecurityScopeVisibilityFilter filter;

  /**
   * @autogenerated EntityGenerator
   */
  @NonNull
  private final Interaction interaction;
}
