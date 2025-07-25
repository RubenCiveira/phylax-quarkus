package net.civeira.phylax.features.access.user.application.usecase.delete;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.user.application.visibility.UserVisibilityFilter;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDeleteAllInBatchCommand {

  /**
   * The filter to select the results to retrieve
   *
   * @autogenerated EntityGenerator
   */
  @NonNull
  private final UserVisibilityFilter filter;

  /**
   * @autogenerated EntityGenerator
   */
  @NonNull
  private final Interaction interaction;
}
