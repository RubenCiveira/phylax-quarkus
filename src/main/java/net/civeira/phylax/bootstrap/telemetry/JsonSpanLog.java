package net.civeira.phylax.bootstrap.telemetry;

import java.util.Map;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * Estructura JSON "OTLP-friendly" para persistir/emitir un span en logs. Pensada para salir como un
 * objeto JSON (no como MDC plano).
 */
@Value
@Builder
@Jacksonized
@RegisterForReflection
public class JsonSpanLog {

  // Identidad / correlación
  private final String traceId;
  private final String spanId;
  private final String parentSpanId;

  // Span core
  private final String name;
  private final String kind;

  private final long startEpochNanos;
  private final long endEpochNanos;

  // Estado (OK/ERROR/UNSET)
  private final String status;

  // Atributos OTEL (span + resource)
  @Builder.Default
  private final Map<String, Object> attributes = Map.of();
  @Builder.Default
  private final Map<String, Object> resourceAttributes = Map.of();

  // (Opcional) Si quieres incluir scope del instrumentation library
  private final String scopeName;
  private final String scopeVersion;
  private final String scopeSchemaUrl;
}
