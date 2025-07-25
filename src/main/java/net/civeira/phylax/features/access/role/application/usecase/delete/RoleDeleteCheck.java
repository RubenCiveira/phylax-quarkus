package net.civeira.phylax.features.access.role.application.usecase.delete;

import java.util.Optional;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.role.domain.RoleRef;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RoleDeleteCheck {

  /**
   * @autogenerated DeleteCheckGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated DeleteCheckGenerator
   */
  private final RoleRef reference;

  /**
   * @autogenerated DeleteCheckGenerator
   * @return
   */
  public Optional<RoleRef> getReference() {
    return Optional.ofNullable(reference);
  }
}
