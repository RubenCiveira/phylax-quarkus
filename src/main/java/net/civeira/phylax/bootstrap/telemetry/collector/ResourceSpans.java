package net.civeira.phylax.bootstrap.telemetry.collector;

import java.util.List;

public record ResourceSpans(
    OtlpResource resource,
    List<ScopeSpans> scopeSpans
  ) {}
