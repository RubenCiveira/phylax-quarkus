package net.civeira.phylax.features.access.relyingparty.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class RelyingPartyDeleteEvent extends RelyingPartyEvent {
}
