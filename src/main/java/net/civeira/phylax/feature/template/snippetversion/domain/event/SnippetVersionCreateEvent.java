package net.civeira.phylax.feature.template.snippetversion.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class SnippetVersionCreateEvent extends SnippetVersionEvent {
}
