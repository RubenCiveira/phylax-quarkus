package net.civeira.phylax.features.access.user.application.visibility;

import java.util.function.Consumer;
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
public class UserVisibilityCheck {

  /**
   * @autogenerated VisibilityCheckGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated VisibilityCheckGenerator
   */
  @NonNull
  private UserVisibilityFilter filter;

  /**
   * @autogenerated VisibilityCheckGenerator
   * @param mapper
   */
  public void map(UnaryOperator<UserVisibilityFilter> mapper) {
    filter = mapper.apply(filter);
  }

  /**
   * @autogenerated VisibilityCheckGenerator
   * @param mapper
   */
  public void peek(Consumer<UserVisibilityFilter> mapper) {
    mapper.accept(filter);
  }
}
