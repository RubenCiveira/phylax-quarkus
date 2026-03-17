package net.civeira.phylax.features.access.role.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.civeira.phylax.features.access.role.domain.Role;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class RoleCreateEvent extends RoleEvent {
}
