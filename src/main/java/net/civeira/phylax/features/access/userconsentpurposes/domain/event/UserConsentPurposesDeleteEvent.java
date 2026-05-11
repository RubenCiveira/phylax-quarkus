package net.civeira.phylax.features.access.userconsentpurposes.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UserConsentPurposesDeleteEvent extends UserConsentPurposesEvent {
}
