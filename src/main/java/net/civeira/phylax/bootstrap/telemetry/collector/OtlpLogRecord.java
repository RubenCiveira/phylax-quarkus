package net.civeira.phylax.bootstrap.telemetry.collector;

import java.util.List;

public record OtlpLogRecord(
    long timeUnixNano,
    int severityNumber,
    String severityText,
    String body,
    List<OtlpKeyValue> attributes,
    String traceId,
    String spanId
  ) {}
