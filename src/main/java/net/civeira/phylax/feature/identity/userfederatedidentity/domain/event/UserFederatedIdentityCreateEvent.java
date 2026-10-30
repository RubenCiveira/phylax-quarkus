package net.civeira.phylax.feature.identity.userfederatedidentity.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UserFederatedIdentityCreateEvent extends UserFederatedIdentityEvent {
}
