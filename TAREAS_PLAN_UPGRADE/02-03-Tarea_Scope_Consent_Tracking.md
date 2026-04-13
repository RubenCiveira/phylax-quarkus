# Tarea 02-03 — Scope Consent Tracking (completar implementación)

> **Fase:** 02 — Cumplimiento OIDC Obligatorio
> **Prioridad:** P0
> **Esfuerzo estimado:** Medio
> **Prerequisito:** Ninguno (independiente)

---

## Descripción

El módulo de scopes actualmente es un stub que siempre devuelve `[]` (ningún
scope consentido) y no persiste las decisiones del usuario. Esto hace que
el formulario de consentimiento aparezca en cada login, rompiendo la experiencia
de usuario en aplicaciones que usan OIDC.

---

## Cambios necesarios

### 1. Tabla `access_scope_consent`

**Migración Liquibase:**

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-scope-consent-table
CREATE TABLE access_scope_consent (
  uid          VARCHAR(36)  NOT NULL,
  user_uid     VARCHAR(36)  NOT NULL,
  client_uid   VARCHAR(36)  NOT NULL,
  tenant_id    VARCHAR(36)  NOT NULL,
  scope        VARCHAR(100) NOT NULL,
  granted_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  revoked_at   TIMESTAMP    NULL,
  CONSTRAINT PK_ACCESS_SCOPE_CONSENT PRIMARY KEY (uid),
  INDEX idx_scope_consent_user_client (user_uid, client_uid, tenant_id)
);
```

### 2. Domain — Value objects y eventos

```java
// Value object
public record ScopeConsent(
    UUID uid,
    UUID userUid,
    UUID clientUid,
    String tenantId,
    String scope,
    Instant grantedAt,
    Optional<Instant> revokedAt
) {}

// Evento de dominio
public record ScopeConsentGrantedEvent(UUID userUid, UUID clientUid, String scope) {}
public record ScopeConsentRevokedEvent(UUID userUid, UUID clientUid, String scope) {}
```

### 3. Port de salida — `ScopeConsentGateway`

```java
public interface ScopeConsentGateway {
    Set<String> findGrantedScopes(UUID userUid, UUID clientUid, String tenantId);
    void grantConsent(ScopeConsent consent);
    void revokeConsent(UUID userUid, UUID clientUid, String tenantId, String scope);
    void revokeAllConsentsForClient(UUID userUid, UUID clientUid, String tenantId);
}
```

### 4. Use cases

**`GrantScopeConsentUseCase`:**
1. Recibir `(userUid, clientUid, tenantId, scopes[])`
2. Para cada scope, crear `ScopeConsent` y persistir
3. Publicar `ScopeConsentGrantedEvent` via CDI Event

**`CheckPendingConsentUseCase`:**
1. Recibir `(userUid, clientUid, tenantId, requestedScopes[])`
2. Consultar `ScopeConsentGateway.findGrantedScopes()`
3. Devolver la diferencia: `requestedScopes - alreadyGranted`

### 5. Integrar en el authorize flow

**Fichero:** `FrontAcessController` / `ControllerPart` del paso `scopes-consent`

Modificar la lógica del paso:
```
ANTES: Siempre mostrar formulario de consentimiento

DESPUÉS:
  pendingScopes = CheckPendingConsentUseCase.execute(user, client, requestedScopes)
  if (pendingScopes.isEmpty()) → saltar al siguiente paso sin mostrar formulario
  else → mostrar formulario solo con los scopes pendientes
```

Al submit del formulario de consent:
```
GrantScopeConsentUseCase.execute(user, client, approvedScopes)
```

### 6. Endpoint de revocación

```
DELETE /openid/{tenant}/consent/{client_uid}
Authorization: Bearer <user_token>
```

Invoca `ScopeConsentGateway.revokeAllConsentsForClient()`.

### 7. Tests de integración

- Primera vez: formulario de consent se muestra ✓
- Segunda vez mismo cliente/scopes: formulario NO se muestra ✓
- Scopes adicionales: formulario se muestra solo con los nuevos ✓
- Revocar consent: formulario vuelve a aparecer en siguiente login ✓

---

## Criterios de aceptación

- [ ] Tabla `access_scope_consent` creada con migración Liquibase
- [ ] `findGrantedScopes()` consultado antes de mostrar el formulario
- [ ] Consentimiento persistido al aprobar el formulario
- [ ] Formulario no se muestra si todos los scopes ya fueron concedidos
- [ ] `DELETE /openid/{tenant}/consent/{client_uid}` funciona
- [ ] 4 tests de integración cubriendo los escenarios descritos
