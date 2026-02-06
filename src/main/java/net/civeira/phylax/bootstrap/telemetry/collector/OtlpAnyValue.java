package net.civeira.phylax.bootstrap.telemetry.collector;

public record OtlpAnyValue(
    String stringValue,
    Long intValue,
    Double doubleValue,
    Boolean boolValue
  ) {}

