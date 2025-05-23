/* @autogenerated */
package net.civeira.phylax.bootstrap.telemetry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class LogManagementService {
  private static final String TRACE_TAG = LoggingSpanExporter.class.getName();

  private final @ConfigProperty(name = "quarkus.log.file.path") String logFile;
  private final ObjectMapper objectMapper;

  public List<LogEntry> getLogs(String afterOffset) {
    return readLogs(afterOffset, false);
  }

  public List<LogEntry> getTraces(String afterOffset) {
    return readLogs(afterOffset, true);
  }

  private List<LogEntry> readLogs(String afterOffset, boolean include) {
    List<LogEntry> logs = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        Map<String, Object> logEntryMap =
            objectMapper.readValue(line, new TypeReference<Map<String, Object>>() {});
        Instant timestamp = Instant.parse(logEntryMap.get("timestamp").toString());
        long sequence = Long.parseLong(logEntryMap.get("sequence").toString());

        String offset = timestamp.toString() + "-" + sequence;

        String loggerName = (String) logEntryMap.get("loggerName");
        String message = (String) logEntryMap.get("message");
        if (TRACE_TAG.equals(loggerName) && null != message
            && (message.equals("GET /q/logs") || message.equals("GET /q/traces"))) {
          continue;
        }
        if (include && !TRACE_TAG.equals(loggerName)) {
          continue;
        } else if (!include && TRACE_TAG.equals(loggerName)) {
          continue;
        }
        if (afterOffset == null || offset.compareTo(afterOffset) > 0) {
          logs.add(LogEntry.builder().offset(offset).data(logEntryMap).build());
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading log file", e);
    }
    return logs;
  }
}
