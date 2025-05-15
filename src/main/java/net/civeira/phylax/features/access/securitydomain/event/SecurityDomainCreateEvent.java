package net.civeira.phylax.features.access.securitydomain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class SecurityDomainCreateEvent extends SecurityDomainEvent {
}
