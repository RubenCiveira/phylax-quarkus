package net.civeira.phylax.bootstrap.telemetry.exporter;

import io.opentelemetry.sdk.trace.data.SpanData;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
public class SpanToJsonRecordMapper {

  public JsonSpanRecord map(SpanData span) {

    Map<String, Object> spanAttrs = new LinkedHashMap<>();
    span.getAttributes().forEach((k, v) -> spanAttrs.put(k.getKey(), v));

    Map<String, Object> resourceAttrs = new LinkedHashMap<>();
    span.getResource().getAttributes()
        .forEach((k, v) -> resourceAttrs.put(k.getKey(), v));

    var scope = span.getInstrumentationScopeInfo();

    return JsonSpanRecord.builder()
        .traceId(span.getTraceId())
        .spanId(span.getSpanId())
        .parentSpanId(span.getParentSpanId())
        .name(span.getName())
        .kind(span.getKind().name())
        .startEpochNanos(span.getStartEpochNanos())
        .endEpochNanos(span.getEndEpochNanos())
        .status(span.getStatus().getStatusCode().name())
        .attributes(spanAttrs)
        .resourceAttributes(resourceAttrs)
        .scopeName(scope.getName())
        .scopeVersion(scope.getVersion())
        .scopeSchemaUrl(scope.getSchemaUrl())
        .build();
  }
}
