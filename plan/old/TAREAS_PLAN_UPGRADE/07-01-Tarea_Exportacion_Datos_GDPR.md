# Tarea 07-01 — Derecho de acceso y exportación de datos (GDPR Art. 15 / Art. 20)

> **Fase:** 07 — Cumplimiento GDPR
> **Artículo GDPR:** 15 (acceso), 20 (portabilidad)
> **Prioridad:** P3
> **Esfuerzo estimado:** Medio
> **Prerequisito:** 05-02 (Perfil ampliado), 05-06 (PAT)

---

## Descripción

El usuario tiene derecho a obtener una copia de todos sus datos personales
en un formato portable y legible por máquina. La exportación se genera de
forma asíncrona y se entrega por email.

---

## Datos a exportar

| Categoría | Fuente |
|-----------|--------|
| Perfil personal | `access_user` + `access_user_profile` |
| Sesiones activas e históricas | `_oauth_sessions` |
| Consentimientos de scopes | `access_scope_consent` |
| Consentimientos GDPR | `access_user_consent` |
| Personal Access Tokens (metadatos, sin el valor) | `access_user_api_key` |
| Métodos MFA registrados | `access_user_mfa_method` |
| Credenciales WebAuthn (metadatos) | `access_user_webauthn_credential` |
| Historial de audit log personal | `_audit_action` (donde `actor_uid = user_uid`) |
| Invitaciones enviadas y recibidas | `access_user_invitation` |

---

## Pasos de implementación

### 1. Endpoint de solicitud

```
POST /api/me/data-export
Authorization: Bearer <token>
```

**Respuesta inmediata:**
```json
{
  "task_id": "task-uuid",
  "status": "pending",
  "estimated_at": "2026-04-13T10:05:00Z",
  "message": "Your export is being generated. You'll receive an email when it's ready."
}
```

### 2. Tarea asíncrona — usar `_long_tasks`

La tabla `_long_tasks` ya existe. Crear una tarea de tipo `DATA_EXPORT`:

```java
@ApplicationScoped
public class DataExportJobRunner {

    @Inject
    LongTaskGateway longTaskGateway;

    @Inject
    DataExportUseCase dataExportUseCase;

    @Inject
    NotificationOutboxGateway notificationGateway;

    @Inject
    FileStorerGateway fileStorerGateway;  // _filestorer ya existe en la DB

    // Ejecutado por Quarkus Scheduler cada minuto
    @Scheduled(every = "60s")
    public void processPendingExports() {
        longTaskGateway.findPendingByType("DATA_EXPORT").forEach(task -> {
            try {
                UUID userUid = UUID.fromString(task.actor());
                byte[] zipBytes = dataExportUseCase.generate(userUid, task.tenantId());

                // Almacenar en _filestorer
                String fileCode = fileStorerGateway.store(
                    zipBytes, "application/zip",
                    "phylax-data-export-" + userUid + ".zip");

                // Notificar por email con enlace de descarga (TTL: 48h)
                notificationGateway.enqueue(
                    NotificationRequest.dataExportReady(userUid, downloadUrl(fileCode)));

                longTaskGateway.complete(task.code(), Map.of("file_code", fileCode));
            } catch (Exception e) {
                longTaskGateway.fail(task.code(), e.getMessage());
            }
        });
    }
}
```

### 3. Use case `DataExportUseCase`

```java
@ApplicationScoped
public class DataExportUseCase {

    public byte[] generate(UUID userUid, String tenantId) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(baos)) {

            // profile.json
            addEntry(zip, "profile.json", exportProfile(userUid, tenantId));

            // sessions.json
            addEntry(zip, "sessions.json", exportSessions(userUid, tenantId));

            // consents.json
            addEntry(zip, "consents.json", exportConsents(userUid, tenantId));

            // api_keys.json
            addEntry(zip, "api_keys.json", exportApiKeys(userUid, tenantId));

            // mfa_methods.json
            addEntry(zip, "mfa_methods.json", exportMfaMethods(userUid, tenantId));

            // audit_log.json
            addEntry(zip, "audit_log.json", exportAuditLog(userUid, tenantId));
        }
        return baos.toByteArray();
    }
}
```

### 4. Endpoint de descarga del archivo

```
GET /api/me/data-export/{file_code}
Authorization: Bearer <token>
```

Verificar que `file_code` pertenece al usuario autenticado antes de servir.
El enlace expira en 48 horas.

### 5. Endpoint de estado de la tarea

```
GET /api/me/data-export/status
Authorization: Bearer <token>
```

Consulta `_long_tasks` para el usuario y devuelve el estado.

### 6. Tests de integración

- Solicitar exportación → tarea creada en `_long_tasks` ✓
- Ejecutar job → ZIP generado, email enviado ✓
- Verificar que el ZIP contiene todos los archivos esperados ✓
- Descargar con enlace válido → 200 con ZIP ✓
- Descargar con enlace de otro usuario → 403 ✗

---

## Criterios de aceptación

- [ ] `POST /api/me/data-export` crea tarea en `_long_tasks`
- [ ] Job asíncrono genera ZIP con todos los datos del usuario
- [ ] ZIP contiene: `profile.json`, `sessions.json`, `consents.json`, `api_keys.json`, `audit_log.json`
- [ ] Email enviado al completar con enlace de descarga
- [ ] Enlace de descarga expira en 48 horas
- [ ] Solo el propietario puede descargar su exportación
- [ ] 5 tests de integración
