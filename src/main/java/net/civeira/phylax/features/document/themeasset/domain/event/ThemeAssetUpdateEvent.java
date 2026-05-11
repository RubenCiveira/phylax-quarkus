package net.civeira.phylax.features.document.themeasset.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ThemeAssetUpdateEvent extends ThemeAssetEvent {
}
