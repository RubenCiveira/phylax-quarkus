# Tarea 05-01 — Management API — Sesiones activas del usuario

> **Fase:** 05 — API de Gestión BaaS
> **Prioridad:** P1
> **Esfuerzo estimado:** Bajo
> **Prerequisito:** 02-04 (Token Revocation)

---

## Descripción

Permite al usuario final (o al admin) ver desde qué dispositivos está
conectado y revocar sesiones remotamente. Funcionalidad equivalente al
"Cerrar otras sesiones" de Google/GitHub.

---

## Endpoints

```
GET    /api/access/users/{uid}/sessions          # Listar sesiones activas
DELETE /api/access/users/{uid}/sessions/{sid}    # Revocar sesión concreta
DELETE /api/access/users/{uid}/sessions          # Revocar todas las sesiones
```

---

## Pasos de implementación

### 1. Enriquecer `_oauth_sessions` con metadatos de dispositivo

```sql
-- liquibase formatted sql

-- changeset phylax-dev:add-device-info-to-sessions
ALTER TABLE _oauth_sessions
  ADD COLUMN user_uid    VARCHAR(36)  NULL,
  ADD COLUMN user_agent  TEXT         NULL,
  ADD COLUMN ip_address  VARCHAR(45)  NULL,
  ADD COLUMN created_at  TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP;

-- changeset phylax-dev:idx-sessions-user-uid
CREATE INDEX idx_oauth_sessions_user_uid ON _oauth_sessions (user_uid);
```

Actualizar el paso de login para persistir estos campos al crear la sesión.

### 2. Value object `ActiveSession`

```java
public record ActiveSession(
    String sessionId,
    String clientId,
    String clientName,
    String userAgent,
    String ipAddress,
    Instant createdAt,
    Instant expiresAt,
    boolean isCurrent   // true si es la sesión de la petición actual
) {}
```

### 3. Port de salida — `SessionManagementGateway`

```java
public interface SessionManagementGateway {
    List<ActiveSession> findActiveSessionsByUser(UUID userUid, String tenantId);
    void revokeSession(String sessionId, UUID userUid, String tenantId);
    void revokeAllSessions(UUID userUid, String tenantId);
}
```

### 4. Use cases

**`ListActiveSessionsUseCase`:**
1. Verificar que el solicitante tiene permiso (propio usuario o admin)
2. Consultar sesiones activas (no expiradas, no revocadas) por `user_uid`
3. Marcar la sesión actual como `isCurrent = true` comparando el `session_id` del Bearer token

**`RevokeSessionUseCase`:**
1. Verificar que la sesión pertenece al `user_uid` del path
2. Llamar a `tokenRevocationGateway.revokeSessionAndDescendants(sessionId)`
3. Marcar `revoked_at = NOW()` en `_oauth_sessions`

**`RevokeAllSessionsUseCase`:**
1. Obtener todas las sesiones activas del usuario
2. Revocar cada una en cascada
3. Opcionalmente: preservar la sesión actual (parámetro `?except_current=true`)

### 5. Controller REST

```java
@Path("/api/access/users/{uid}/sessions")
@Tag(name = "Session Management")
public class SessionManagementController {

    @GET
    @Operation(summary = "List active sessions for a user")
    @APIResponse(responseCode = "200", description = "List of active sessions")
    public List<ActiveSessionDto> listSessions(
        @PathParam("uid") UUID userUid,
        @Context SecurityContext security
    ) { ... }

    @DELETE
    @Path("/{sid}")
    @Operation(summary = "Revoke a specific session")
    public Response revokeSession(
        @PathParam("uid") UUID userUid,
        @PathParam("sid") String sessionId
    ) { ... }

    @DELETE
    @Operation(summary = "Revoke all sessions")
    public Response revokeAllSessions(
        @PathParam("uid") UUID userUid,
        @QueryParam("except_current") boolean exceptCurrent
    ) { ... }
}
```

### 6. Tests de integración

- Listar sesiones: devuelve solo las del usuario, marcando la actual ✓
- Revocar sesión propia → 204, token del RP rechazado ✓
- Revocar sesión de otro usuario → 403 ✗
- Revocar todas excepto la actual → sesión actual sigue válida ✓

---

## Criterios de aceptación

- [ ] Columnas `user_uid`, `user_agent`, `ip_address`, `created_at` en `_oauth_sessions`
- [ ] `GET /api/access/users/{uid}/sessions` lista sesiones con metadata de dispositivo
- [ ] `DELETE /api/access/users/{uid}/sessions/{sid}` revoca sesión y sus tokens
- [ ] `DELETE /api/access/users/{uid}/sessions` revoca todas (con opción `except_current`)
- [ ] Autorización: solo el propio usuario o admin puede gestionar sus sesiones
- [ ] 4 tests de integración
