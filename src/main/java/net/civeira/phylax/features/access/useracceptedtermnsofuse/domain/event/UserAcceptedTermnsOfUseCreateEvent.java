package net.civeira.phylax.features.access.useracceptedtermnsofuse.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UserAcceptedTermnsOfUseCreateEvent extends UserAcceptedTermnsOfUseEvent {
}
