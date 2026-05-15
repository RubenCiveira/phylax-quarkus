# Tarea 06-04 — Audit Log API pública

> **Fase:** 06 — Infraestructura Reactiva
> **Prioridad:** P3
> **Esfuerzo estimado:** Bajo
> **Prerequisito:** Audit trail interno existente (`_audit_action`, `_audit_change`)

---

## Descripción

El audit trail ya existe internamente pero no tiene API para que las
aplicaciones cliente lo consulten. Esta tarea expone el audit log mediante
una API REST paginada con filtros.

---

## Estado actual

Las tablas de audit trail existen (`_audit_action`, `_audit_change`).
Verificar su estructura exacta:

```bash
grep -r "audit" src/main/resources/db/migration --include="*.sql"
```

---

## Pasos de implementación

### 1. Verificar estructura de las tablas de audit

Revisar las columnas de `_audit_action` y mapear:
- ¿Hay `actor_uid` / `actor_email`?
- ¿Hay `tenant_id`?
- ¿Hay `event_type` / `action`?
- ¿Hay `resource_type` / `resource_uid`?
- ¿Hay `ip_address` / `user_agent`?
- ¿Hay `created_at`?

Si faltan columnas relevantes, añadirlas con migración Liquibase.

### 2. Value object `AuditEntry`

```java
public record AuditEntry(
    UUID uid,
    String tenantId,
    String eventType,
    AuditActor actor,
    AuditResource resource,
    Map<String, Object> changes,  // de _audit_change
    String ipAddress,
    Instant createdAt
) {}

public record AuditActor(UUID uid, String email, String type) {} // user, system, api-key
public record AuditResource(String type, UUID uid, String displayName) {}
```

### 3. Port de salida — `AuditLogGateway`

```java
public interface AuditLogGateway {
    CursorPage<AuditEntry> findByFilters(AuditLogFilter filter);
}

public record AuditLogFilter(
    String tenantId,
    Optional<UUID> actorUid,
    Optional<String> resourceType,
    Optional<UUID> resourceUid,
    Optional<String> eventType,
    Optional<Instant> from,
    Optional<Instant> to,
    Optional<String> cursor,
    int limit
) {}
```

### 4. Endpoint

```java
@Path("/api/access/audit-log")
@Tag(name = "Audit Log")
public class AuditLogController {

    @GET
    @Operation(summary = "Query the audit log")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Paginated audit log entries"),
        @APIResponse(responseCode = "403", description = "Requires scope access:audit")
    })
    public CursorPage<AuditEntryDto> query(
        @QueryParam("actor_uid")     UUID actorUid,
        @QueryParam("resource_type") String resourceType,
        @QueryParam("resource_uid")  UUID resourceUid,
        @QueryParam("event_type")    String eventType,
        @QueryParam("from")          String from,         // ISO-8601
        @QueryParam("to")            String to,
        @QueryParam("cursor")        String cursor,
        @QueryParam("limit") @DefaultValue("50") int limit,
        @Context SecurityContext security
    ) {
        requireScope(security, "access:audit");
        String tenantId = extractTenantId(security);

        AuditLogFilter filter = new AuditLogFilter(tenantId, ...);
        return auditLogUseCase.query(filter);
    }
}
```

### 5. Paginación cursor-based

El cursor es el `uid` del último registro devuelto:

```sql
SELECT *
FROM _audit_action
WHERE tenant_id = ?
  AND (? IS NULL OR actor_uid = ?)
  AND (? IS NULL OR event_type = ?)
  AND (? IS NULL OR created_at >= ?)
  AND (? IS NULL OR created_at <= ?)
  AND (? IS NULL OR uid > ?)   -- cursor
ORDER BY created_at DESC, uid DESC
LIMIT ?
```

### 6. Retención configurable por tenant

```sql
-- changeset phylax-dev:add-audit-retention-to-tenant
ALTER TABLE access_tenant_config
  ADD COLUMN audit_retention_days INT DEFAULT 90;
```

Cleanup job en `OAuthExpiredRecordsCleaner` (01-06):

```java
@Scheduled(cron = "0 0 4 * * ?")
public void cleanExpiredAuditEntries() {
    // DELETE FROM _audit_action WHERE created_at < NOW() - INTERVAL {retention} DAY
    // Respetar la retención configurada por cada tenant
}
```

### 7. Respuesta de ejemplo

```json
{
  "entries": [
    {
      "uid": "entry-uuid",
      "event_type": "user.login.success",
      "actor": { "uid": "user-uuid", "email": "user@example.com", "type": "user" },
      "resource": { "type": "session", "uid": "session-uuid" },
      "changes": {},
      "ip_address": "1.2.3.4",
      "created_at": "2026-04-13T10:00:00Z"
    }
  ],
  "next_cursor": "last-entry-uuid",
  "has_more": true
}
```

### 8. Tests de integración

- Filtrar por `actor_uid` → solo entradas del usuario ✓
- Filtrar por `event_type=user.login.success` → solo logins ✓
- Filtrar por rango de fechas ✓
- Paginación cursor → segunda página correcta ✓
- Sin scope `access:audit` → 403 ✗

---

## Criterios de aceptación

- [ ] `GET /api/access/audit-log` con filtros: `actor_uid`, `resource_type`, `event_type`, `from`, `to`
- [ ] Paginación cursor-based con `next_cursor` y `has_more`
- [ ] Scope `access:audit` requerido
- [ ] Retención configurable por tenant con cleanup job
- [ ] Anotaciones OpenAPI completas
- [ ] 5 tests de integración
