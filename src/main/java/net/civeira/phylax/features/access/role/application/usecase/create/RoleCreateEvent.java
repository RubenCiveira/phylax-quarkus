package net.civeira.phylax.features.access.role.application.usecase.create;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.With;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.role.Role;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RoleCreateEvent {

  /**
   * @autogenerated CreateEventGenerator
   */
  @NonNull
  private final Interaction interaction;

  /**
   * @autogenerated CreateEventGenerator
   */
  @NonNull
  private final Role payload;
}
