package net.civeira.phylax.bootstrap.telemetry.collector;

import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Path("/v1/logs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RequiredArgsConstructor
public class OtlpLogsResource {

  private final LogsMapper logsMapper;

  private final Instance<LogRecordExporter> logExporter;

  @POST
  public Response ingestLogs(OtlpLogsRequest request) {
    var logs = logsMapper.toLogRecords(request);
    var success = false;
    for (var exporter : logExporter) {
      var result = exporter.export(logs);
      success |= result.isSuccess();
    }
    return success ? Response.accepted().entity(OtlpSuccessResponse.ok()).build()
        : Response.serverError().entity(OtlpSuccessResponse.partial("log_export_failed")).build();
  }
}
