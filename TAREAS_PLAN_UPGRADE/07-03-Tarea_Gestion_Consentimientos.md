# Tarea 07-03 — Gestión de consentimientos (GDPR Art. 7)

> **Fase:** 07 — Cumplimiento GDPR
> **Artículo GDPR:** 7 (consentimiento), 13 (transparencia)
> **Prioridad:** P3
> **Esfuerzo estimado:** Medio
> **Prerequisito:** 02-03 (Scope Consent — es la base del sistema de consentimientos)

---

## Descripción

Gestión de consentimientos de **procesamiento de datos** (diferente del
scope consent de OAuth). Permite a los usuarios controlar para qué
finalidades se procesan sus datos: marketing, analytics, terceros, etc.

La tabla `access_user_accepted_termns_of_use` ya rastrea aceptación de T&C
pero no gestiona consentimientos de procesamiento con revocabilidad.

---

## Pasos de implementación

### 1. Tabla `access_user_consent`

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-user-consent-table
CREATE TABLE access_user_consent (
  uid         VARCHAR(36)  NOT NULL,
  user_uid    VARCHAR(36)  NOT NULL,
  tenant_id   VARCHAR(36)  NOT NULL,
  purpose     VARCHAR(100) NOT NULL COMMENT 'marketing, analytics, third_party_sharing, etc.',
  granted     TINYINT(1)   NOT NULL,
  version     VARCHAR(20)  NOT NULL COMMENT 'Versión del texto de consentimiento',
  granted_at  TIMESTAMP    NULL,
  revoked_at  TIMESTAMP    NULL,
  CONSTRAINT PK_ACCESS_USER_CONSENT PRIMARY KEY (uid),
  UNIQUE KEY uq_user_consent_purpose (user_uid, tenant_id, purpose),
  INDEX idx_user_consent_user (user_uid, tenant_id)
);
```

### 2. Tabla de versiones de texto de consentimiento

```sql
-- changeset phylax-dev:create-consent-definition-table
CREATE TABLE access_consent_definition (
  uid         VARCHAR(36)  NOT NULL,
  tenant_id   VARCHAR(36)  NOT NULL,
  purpose     VARCHAR(100) NOT NULL,
  version     VARCHAR(20)  NOT NULL,
  title       VARCHAR(255) NOT NULL,
  description TEXT         NOT NULL,
  mandatory   TINYINT(1)   DEFAULT 0,
  active      TINYINT(1)   DEFAULT 1,
  created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT PK_ACCESS_CONSENT_DEFINITION PRIMARY KEY (uid),
  UNIQUE KEY uq_consent_def (tenant_id, purpose, version)
);
```

### 3. Propósitos predefinidos

| Purpose | Descripción | ¿Obligatorio? |
|---------|-------------|---------------|
| `terms_of_service` | Aceptación de Términos y Condiciones | Sí |
| `privacy_policy` | Política de Privacidad | Sí |
| `marketing_email` | Emails de marketing y novedades | No |
| `analytics` | Análisis de uso para mejorar el servicio | No |
| `third_party_sharing` | Compartir datos con terceros | No |

### 4. Value objects del dominio

```java
public record UserConsent(
    UUID uid,
    UUID userUid,
    String tenantId,
    String purpose,
    boolean granted,
    String version,
    Optional<Instant> grantedAt,
    Optional<Instant> revokedAt
) {
    public boolean isActive() {
        return granted && revokedAt.isEmpty();
    }
}

public record ConsentDefinition(
    UUID uid,
    String tenantId,
    String purpose,
    String version,
    String title,
    String description,
    boolean mandatory,
    boolean active
) {}
```

### 5. Port de salida — `UserConsentGateway`

```java
public interface UserConsentGateway {
    List<UserConsent> findByUser(UUID userUid, String tenantId);
    Optional<UserConsent> findByUserAndPurpose(UUID userUid, String tenantId, String purpose);
    void grantConsent(UserConsent consent);
    void revokeConsent(UUID userUid, String tenantId, String purpose);
}
```

### 6. Integración en el registro de usuario

Añadir paso de consentimientos en el authorize flow cuando el usuario
se registra por primera vez:

```
Paso: consent-collection
  → Mostrar formulario con las ConsentDefinitions activas del tenant
  → Obligatorios: deben marcarse para continuar
  → Opcionales: pre-marcados o no según config del tenant
  → Al submit: persistir UserConsent para cada propósito
```

### 7. Endpoints

```java
@Path("/api/me/consents")
@Tag(name = "GDPR Consents")
public class UserConsentController {

    @GET
    @Operation(summary = "Get current consent status for all purposes")
    public List<ConsentStatusDto> getConsents(@Context SecurityContext security) { ... }
    // Response: [{ purpose, granted, version, granted_at, revoked_at, definition }]

    @PUT
    @Path("/{purpose}")
    @Operation(summary = "Grant or revoke consent for a specific purpose")
    public Response updateConsent(
        @PathParam("purpose") String purpose,
        @Valid ConsentUpdateRequest request,
        @Context SecurityContext security
    ) { ... }
    // Body: { granted: true/false }
    // 409 si el purpose es mandatory y granted=false
}

// Endpoint admin para gestionar definiciones
@Path("/api/admin/consent-definitions")
public class ConsentDefinitionController { ... }
```

### 8. Verificación de consentimientos obligatorios

En el token endpoint y en el authorize flow, verificar que el usuario
ha aceptado los consentimientos obligatorios:

```java
boolean hasRequiredConsents = consentDefinitions.stream()
    .filter(ConsentDefinition::mandatory)
    .allMatch(def -> userConsentGateway
        .findByUserAndPurpose(userUid, tenantId, def.purpose())
        .map(UserConsent::isActive)
        .orElse(false));

if (!hasRequiredConsents) {
    // Redirigir al paso de recogida de consentimientos
}
```

### 9. Tests de integración

- Usuario sin consentimientos obligatorios → redirigido al formulario ✓
- Conceder `terms_of_service` + `privacy_policy` → registro completa ✓
- Revocar `marketing_email` → 200, consent marcado como revocado ✓
- Intentar revocar `terms_of_service` (mandatory) → 409 ✗
- `GET /api/me/consents` → devuelve estado completo de todos los propósitos ✓

---

## Criterios de aceptación

- [ ] Tablas `access_user_consent` y `access_consent_definition` con migraciones
- [ ] 5 propósitos predefinidos con definiciones base
- [ ] Paso de recogida de consentimientos en el flujo de registro
- [ ] Consentimientos obligatorios verificados en el authorize flow
- [ ] `GET /api/me/consents` devuelve estado de todos los propósitos
- [ ] `PUT /api/me/consents/{purpose}` permite conceder/revocar
- [ ] No se puede revocar un consentimiento marcado como mandatory
- [ ] 5 tests de integración
