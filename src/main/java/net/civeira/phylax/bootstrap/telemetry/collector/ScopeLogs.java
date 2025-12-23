package net.civeira.phylax.bootstrap.telemetry.collector;

import java.util.List;

public record ScopeLogs(
    OtlpScope scope,
    List<OtlpLogRecord> logRecords
  ) {}