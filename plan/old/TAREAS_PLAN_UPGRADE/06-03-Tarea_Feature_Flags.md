# Tarea 06-03 — Feature Flags por Tenant

> **Fase:** 06 — Infraestructura Reactiva
> **Prioridad:** P3
> **Esfuerzo estimado:** Medio
> **Prerequisito:** Ninguno (independiente)

---

## Descripción

Permite habilitar/deshabilitar funcionalidades por tenant sin despliegues.
Útil para rollouts graduales, A/B testing y gestión de beta features.
Integrado con la configuración de tenant existente para flags de sistema.

---

## Pasos de implementación

### 1. Tabla `access_feature_flag`

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-feature-flag-table
CREATE TABLE access_feature_flag (
  uid                 VARCHAR(36)  NOT NULL,
  tenant_id           VARCHAR(36)  NOT NULL,
  flag_key            VARCHAR(100) NOT NULL,
  enabled             TINYINT(1)   DEFAULT 0,
  rollout_percentage  INT          DEFAULT 100 COMMENT '0-100',
  conditions_json     TEXT         NULL COMMENT 'JSON de condiciones de activación',
  description         TEXT         NULL,
  updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT PK_ACCESS_FEATURE_FLAG PRIMARY KEY (uid),
  UNIQUE KEY uq_flag_tenant_key (tenant_id, flag_key)
);
```

### 2. Value object `FeatureFlag`

```java
public record FeatureFlag(
    UUID uid,
    String tenantId,
    String key,
    boolean enabled,
    int rolloutPercentage,        // 0-100
    Optional<String> conditionsJson
) {
    public boolean isEnabledFor(UUID userUid) {
        if (!enabled) return false;
        if (rolloutPercentage >= 100) return true;
        // Deterministic rollout: hash del userUid % 100
        int bucket = Math.abs(userUid.hashCode()) % 100;
        return bucket < rolloutPercentage;
    }
}
```

### 3. Port de salida — `FeatureFlagGateway`

```java
public interface FeatureFlagGateway {
    Optional<FeatureFlag> findByKey(String tenantId, String key);
    List<FeatureFlag> findByKeys(String tenantId, Collection<String> keys);
    List<FeatureFlag> findAllByTenant(String tenantId);
    void save(FeatureFlag flag);
    void delete(UUID uid);
}
```

### 4. Caché con Quarkus Cache (Caffeine)

Los flags son leídos en cada request — necesitan caché con TTL corto:

```java
@ApplicationScoped
public class CachedFeatureFlagService {

    @Inject
    FeatureFlagGateway gateway;

    @CacheResult(cacheName = "feature-flags")
    public List<FeatureFlag> getFlags(String tenantId, List<String> keys) {
        return gateway.findByKeys(tenantId, keys);
    }

    @CacheInvalidate(cacheName = "feature-flags")
    public void invalidate(String tenantId, String key) {
        // Invocado al actualizar/eliminar un flag vía API
    }
}
```

```properties
# application.properties
quarkus.cache.caffeine."feature-flags".expire-after-write=60S
quarkus.cache.caffeine."feature-flags".maximum-size=1000
```

### 5. Endpoints

**SDK endpoint (para aplicaciones cliente):**
```
GET /api/flags?keys=webauthn,magic-link,new-dashboard
Authorization: Bearer <token>
```

**Response:**
```json
{
  "flags": {
    "webauthn": true,
    "magic-link": false,
    "new-dashboard": true
  }
}
```

**Admin endpoints:**
```
GET    /api/admin/flags                    # Listar todos los flags del tenant
POST   /api/admin/flags                    # Crear flag
PUT    /api/admin/flags/{uid}              # Actualizar (enabled, rollout_percentage)
DELETE /api/admin/flags/{uid}              # Eliminar flag
```

### 6. Flags de sistema predefinidos

Flags usados internamente por Phylax (configurados por el admin del sistema,
no por el tenant):

| Flag | Descripción |
|------|-------------|
| `feature.webauthn` | Habilitar WebAuthn para el tenant |
| `feature.magic-link` | Habilitar magic links |
| `feature.social-login` | Habilitar login social |
| `feature.dynamic-client-registration` | Permitir registro dinámico |
| `feature.mfa-email` | Habilitar MFA por email |
| `feature.mfa-sms` | Habilitar MFA por SMS |

### 7. Integración con el authorize flow

En cada paso del authorize flow, verificar el flag correspondiente:

```java
// En el paso 'webauthn' del authorize
if (!featureFlagService.isEnabled(tenantId, "feature.webauthn")) {
    // Saltar el paso WebAuthn
    return nextStep();
}
```

### 8. Tests de integración

- Flag habilitado → SDK devuelve `true` ✓
- Flag deshabilitado → SDK devuelve `false` ✓
- Rollout al 0% → siempre `false` ✓
- Rollout al 100% → siempre `true` ✓
- Rollout al 50% → determinístico por userUid ✓
- Actualizar flag → caché invalidada, nuevo valor en siguiente request ✓

---

## Criterios de aceptación

- [ ] Tabla `access_feature_flag` con migración
- [ ] `GET /api/flags?keys=...` devuelve mapa de flags para el tenant del token
- [ ] Rollout porcentual determinístico por `userUid`
- [ ] Caché con TTL de 60 segundos (Caffeine)
- [ ] CRUD admin de flags
- [ ] 6 flags de sistema predefinidos para features de esta plataforma
- [ ] Integración con el authorize flow (checks de flags por paso)
- [ ] 6 tests de integración
