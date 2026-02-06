package net.civeira.phylax.bootstrap.telemetry.collector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.opentelemetry.api.logs.Severity;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.logs.data.LogRecordData;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class LogsMapper {
  private final OtelResourceMapper resourceMapper;

  public Collection<LogRecordData> toLogRecords(OtlpLogsRequest request) {

    List<LogRecordData> result = new ArrayList<>();

    for (var rl : request.resourceLogs()) {
      var resource = resourceMapper.toResource(rl.resource());

      for (var sl : rl.scopeLogs()) {
        var scope = InstrumentationScopeInfo.builder(sl.scope().name())
            .setVersion(sl.scope().version()).build();

        for (var log : sl.logRecords()) {
          SpanContext.create(null, null, null, null);
          result.add(OtlpLogRecordData.builder()
              // .body(log.body())
              .severity(toSeverity(log.severityNumber())).severityText(log.severityText())
              .timestampEpochNanos(log.timeUnixNano())
              .spanContext(toSpanContext(log.traceId(), log.spanId()))
              .attributes(resourceMapper.toAttributes(log.attributes())).resource(resource)
              .instrumentationScopeInfo(scope).build());
        }
      }
    }
    return result;
  }

  private Severity toSeverity(int n) {
    return switch (n) {
      case 1 -> Severity.TRACE;
      case 2 -> Severity.TRACE2;
      case 3 -> Severity.TRACE3;
      case 4 -> Severity.TRACE4;

      case 5 -> Severity.DEBUG;
      case 6 -> Severity.DEBUG2;
      case 7 -> Severity.DEBUG3;
      case 8 -> Severity.DEBUG4;

      case 9 -> Severity.INFO;
      case 10 -> Severity.INFO2;
      case 11 -> Severity.INFO3;
      case 12 -> Severity.INFO4;

      case 13 -> Severity.WARN;
      case 14 -> Severity.WARN2;
      case 15 -> Severity.WARN3;
      case 16 -> Severity.WARN4;

      case 17 -> Severity.ERROR;
      case 18 -> Severity.ERROR2;
      case 19 -> Severity.ERROR3;
      case 20 -> Severity.ERROR4;

      case 21 -> Severity.FATAL;
      case 22 -> Severity.FATAL2;
      case 23 -> Severity.FATAL3;
      case 24 -> Severity.FATAL4;

      case 0 -> Severity.UNDEFINED_SEVERITY_NUMBER; // o UNSPECIFIED según tu versión
      default -> Severity.UNDEFINED_SEVERITY_NUMBER;
    };
  }

  private SpanContext toSpanContext(String traceIdHex, String spanIdHex) {
    if (traceIdHex == null || spanIdHex == null) {
      return SpanContext.getInvalid();
    }
    // Si no tienes trazaflags en el JSON, usa sampled=false por defecto
    TraceFlags flags = TraceFlags.getDefault();
    TraceState state = TraceState.getDefault();
    return SpanContext.createFromRemoteParent(traceIdHex, spanIdHex, flags, state);
  }
}
