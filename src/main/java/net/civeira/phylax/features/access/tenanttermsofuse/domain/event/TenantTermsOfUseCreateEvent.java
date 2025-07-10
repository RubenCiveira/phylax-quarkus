package net.civeira.phylax.features.access.tenanttermsofuse.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TenantTermsOfUseCreateEvent extends TenantTermsOfUseEvent {
}
