package net.civeira.phylax.bootstrap.telemetry.collector;

import java.util.List;

public record ScopeSpans(OtlpScope scope, List<OtlpSpan> spans) {
}
