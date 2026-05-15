# Tarea 07-02 — Derecho al olvido (GDPR Art. 17)

> **Fase:** 07 — Cumplimiento GDPR
> **Artículo GDPR:** 17 (supresión)
> **Prioridad:** P3
> **Esfuerzo estimado:** Medio
> **Prerequisito:** 07-01 (Exportación — conveniente ofrecer antes del borrado)

---

## Descripción

El usuario tiene derecho a solicitar la eliminación de todos sus datos personales.
El proceso requiere verificación por email para prevenir eliminaciones accidentales.
Los datos necesarios para auditoría legal se anonimizan en lugar de borrarse.

---

## Pasos de implementación

### 1. Tabla `access_deletion_request`

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-deletion-request-table
CREATE TABLE access_deletion_request (
  uid          VARCHAR(36)  NOT NULL,
  user_uid     VARCHAR(36)  NOT NULL,
  tenant_id    VARCHAR(36)  NOT NULL,
  requested_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  confirmed_at TIMESTAMP    NULL,
  executed_at  TIMESTAMP    NULL,
  cancelled_at TIMESTAMP    NULL,
  reason       TEXT         NULL,
  CONSTRAINT PK_ACCESS_DELETION_REQUEST PRIMARY KEY (uid),
  INDEX idx_deletion_request_user (user_uid)
);
```

### 2. Flujo de eliminación en 3 pasos

```
Paso 1: POST /api/me/account
  → Crear DeletionRequest
  → Enviar email de confirmación con token (TTL: 24h)
  → Respuesta: "Te hemos enviado un email para confirmar la eliminación"

Paso 2: GET /api/me/account/confirm-deletion?token=xxx
  → Validar token
  → Marcar DeletionRequest como confirmed_at = NOW()
  → Mostrar página de confirmación final con countdown (30s)

Paso 3: POST /api/me/account/confirm-deletion
  Body: token=xxx&confirmed=true
  → Ejecutar la eliminación
  → Logout inmediato
```

### 3. Use case `ExecuteUserDeletionUseCase`

**Estrategia: borrado en cascada + anonimización selectiva**

```java
@ApplicationScoped
@Transactional
public class ExecuteUserDeletionUseCase {

    public void execute(UUID userUid, String tenantId) {
        // === BORRADO FÍSICO ===
        // Sesiones activas → revocar todos los tokens
        sessionGateway.revokeAllSessions(userUid, tenantId);

        // Personal Access Tokens
        apiKeyGateway.deleteAllForUser(userUid, tenantId);

        // Scope consents
        scopeConsentGateway.deleteAllForUser(userUid, tenantId);

        // GDPR consents
        gdprConsentGateway.deleteAllForUser(userUid, tenantId);

        // Métodos MFA
        mfaMethodGateway.deleteAllForUser(userUid, tenantId);

        // Credenciales WebAuthn
        webAuthnGateway.deleteAllForUser(userUid, tenantId);

        // Perfil de usuario
        userProfileGateway.delete(userUid, tenantId);

        // === ANONIMIZACIÓN (no borrado físico por requisitos de auditoría) ===
        // Entradas de audit log → anonimizar actor
        auditGateway.anonymizeActor(userUid, "DELETED_USER_" + userUid.toString().substring(0, 8));

        // Historial de sesiones → anonimizar user_uid
        sessionGateway.anonymizeUser(userUid, tenantId);

        // === BORRADO DEL USUARIO ===
        // El usuario se borra último (FK constraints)
        userGateway.delete(userUid, tenantId);

        // === REGISTRO DEL BORRADO ===
        deletionRequestGateway.markExecuted(userUid);

        // Publicar evento para webhooks
        domainEventBus.publish(tenantId, "user.deleted",
            Map.of("user_uid", userUid.toString(), "anonymized", true));
    }
}
```

### 4. Anonimización en el audit log

```sql
-- Ejemplo de anonimización (ejecutado desde Java)
UPDATE _audit_action
SET actor_email = 'deleted-user@anonymized',
    actor_uid   = 'DELETED_' || LEFT(actor_uid, 8)
WHERE actor_uid = ?
  AND tenant_id = ?;
```

### 5. Notificación de confirmación de borrado

Enviar email final al usuario confirmando que sus datos han sido eliminados.
Este email es el **último** que se envía antes de eliminar el email de la DB.

### 6. Consideraciones de seguridad

- El token de confirmación expira en 24 horas
- Solo se permite una solicitud de borrado activa por usuario
- El borrado cancela todas las sesiones activas **antes** de ejecutarse
- Rate limiting: máximo 1 solicitud de borrado por usuario por 30 días

### 7. Tests de integración

- Solicitar borrado → email enviado con token ✓
- Confirmar con token válido → DeletionRequest confirmada ✓
- Token expirado → error ✗
- Ejecutar borrado → usuario eliminado, sesiones revocadas ✓
- Audit log anonimizado → sin PII del usuario ✓
- Login posterior → usuario no encontrado ✗

---

## Criterios de aceptación

- [ ] Flujo de 3 pasos: solicitar → confirmar por email → ejecutar
- [ ] Borrado físico de: sesiones, PATs, consents, MFA, WebAuthn, perfil, cuenta
- [ ] Anonimización (no borrado) de: audit log, historial de sesiones
- [ ] Tabla `access_deletion_request` registra cada solicitud
- [ ] Email de confirmación con enlace de verificación (TTL 24h)
- [ ] Email final confirmando el borrado (antes de eliminar el email)
- [ ] Evento `user.deleted` publicado para webhooks
- [ ] 6 tests de integración
