package net.civeira.phylax.bootstrap.telemetry;

import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;

@Singleton
public class OpenTelemetryConfig {

  @Produces
  @Singleton
  @IfBuildProperty(name = "mp.telemetry.procesor", stringValue = "log")
  public SdkTracerProvider sdkTracerProvider() {
    return SdkTracerProvider.builder()
        .addSpanProcessor(SimpleSpanProcessor.create(new LoggingSpanExporter())).build();
  }
}
