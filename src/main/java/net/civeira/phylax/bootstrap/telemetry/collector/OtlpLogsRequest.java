package net.civeira.phylax.bootstrap.telemetry.collector;

import java.util.List;

public record OtlpLogsRequest(
    List<ResourceLogs> resourceLogs
  ) {}