package net.civeira.phylax.common.infrastructure.audit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.UUID;
import javax.sql.DataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AuditWriteService {

  private final DataSource dataSource;

  private final ObjectMapper mapper;

  public void record(AuditEvent event) {
    String sql = """
            INSERT INTO audit_events (
                id, operation, usecase, entity_type, entity_id,
                old_values, new_values,
                performed_by, tenant, timestamp,
                source_request, remote_address, remote_application, remote_device,
                claims
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, UUID.randomUUID().toString());
      ps.setString(2, event.getOperation());
      ps.setString(3, event.getUsecase());
      ps.setString(4, event.getEntityType());
      ps.setString(5, event.getEntityId());
      ps.setString(6, mapper.writeValueAsString(event.getOldValue()));
      ps.setString(7, mapper.writeValueAsString(event.getNewValue()));
      ps.setString(8, event.getPerformedBy());
      ps.setString(9, event.getTenant());
      ps.setTimestamp(10, Timestamp.from(event.getTimestamp().toInstant()));
      ps.setString(11, event.getSourceRequest());
      ps.setString(12, event.getRemoteAddress());
      ps.setString(13, event.getRemoteApplication());
      ps.setString(14, event.getRemoteDevice());
      ps.setString(15, mapper.writeValueAsString(event.getClaims()));
      ps.executeUpdate();
    } catch (Exception e) {
      throw new RuntimeException("Error writing audit event", e);
    }
  }
}
