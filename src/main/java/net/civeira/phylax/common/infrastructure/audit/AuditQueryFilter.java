package net.civeira.phylax.common.infrastructure.audit;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditQueryFilter {

  private final String tenant;
  private final String performedBy;
  private final String entityType;
  private final String entityId;
  private final String operation;
  private final ZonedDateTime from;
  private final ZonedDateTime to;
  
  void appendToFilter(StringBuilder sql, List<Object> params) {
    if (tenant != null) {
      sql.append("AND tenant = ? ");
      params.add(tenant);
    }
    if (performedBy != null) {
      sql.append("AND performed_by = ? ");
      params.add(performedBy);
    }
    if (entityType != null) {
      sql.append("AND entity_type = ? ");
      params.add(entityType);
    }
    if (entityId != null) {
      sql.append("AND entity_id = ? ");
      params.add(entityId);
    }
    if (operation != null) {
      sql.append("AND operation = ? ");
      params.add(operation);
    }
    if (from != null) {
      sql.append("AND timestamp >= ? ");
      params.add(Timestamp.from(from.toInstant()));
    }
    if (to != null) {
      sql.append("AND timestamp <= ? ");
      params.add(Timestamp.from(to.toInstant()));
    }
  }
}
