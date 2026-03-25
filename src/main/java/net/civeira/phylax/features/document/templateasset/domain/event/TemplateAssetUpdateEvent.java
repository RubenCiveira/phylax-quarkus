package net.civeira.phylax.features.document.templateasset.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TemplateAssetUpdateEvent extends TemplateAssetEvent {
}
