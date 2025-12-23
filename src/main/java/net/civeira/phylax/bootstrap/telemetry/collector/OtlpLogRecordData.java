package net.civeira.phylax.bootstrap.telemetry.collector;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.logs.Severity;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.logs.data.LogRecordData;
import io.opentelemetry.sdk.resources.Resource;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtlpLogRecordData implements LogRecordData {
  private final Resource resource;
  private final InstrumentationScopeInfo instrumentationScopeInfo;
  private final long timestampEpochNanos;
  private final long observedTimestampEpochNanos;
  private final SpanContext spanContext;
  private final Severity severity;
  private final String severityText;
  @Deprecated
  private final io.opentelemetry.sdk.logs.data.Body body;
  @Builder.Default
  private final Attributes attributes = Attributes.empty();
  private final int totalAttributeCount;
  private final String eventName;
}
