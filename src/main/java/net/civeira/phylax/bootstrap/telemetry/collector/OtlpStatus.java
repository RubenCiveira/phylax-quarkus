package net.civeira.phylax.bootstrap.telemetry.collector;

public record OtlpStatus(
    int code,
    String message
  ) {}
