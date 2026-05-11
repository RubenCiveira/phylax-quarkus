package net.civeira.phylax.features.document.snippetasset.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class SnippetAssetEnableEvent extends SnippetAssetEvent {
}
