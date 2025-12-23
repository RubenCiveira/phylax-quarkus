package net.civeira.phylax.bootstrap.telemetry.collector;

public record OtlpKeyValue(
    String key,
    OtlpAnyValue value
  ) {}
