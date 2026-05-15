# Tarea 02-04 — Token Revocation completa (RFC 7009)

> **Fase:** 02 — Cumplimiento OIDC Obligatorio
> **RFC:** 7009
> **Prioridad:** P0
> **Esfuerzo estimado:** Bajo
> **Prerequisito:** 01-05 (JTI en JWT)

---

## Descripción

El endpoint `POST /openid/{tenant}/revoke` ya existe pero la implementación
es incompleta: no invalida los refresh tokens en cascada y no hay un mecanismo
de denylist de JTIs. Sin revocación efectiva, no es posible implementar logout
distribuido ni cerrar sesiones individuales.

---

## Pasos de implementación

### 1. Tabla de JTIs revocados (denylist)

Añadir columna `revoked_at` en `_oauth_sessions`:

```sql
-- liquibase formatted sql

-- changeset phylax-dev:add-revoked-at-to-sessions
ALTER TABLE _oauth_sessions
  ADD COLUMN revoked_at TIMESTAMP NULL DEFAULT NULL;

-- changeset phylax-dev:idx-sessions-revoked
CREATE INDEX idx_oauth_sessions_revoked ON _oauth_sessions (revoked_at);
```

Alternativamente, para tokens de corta vida puede usarse una tabla separada:

```sql
-- changeset phylax-dev:create-revoked-jti-table
CREATE TABLE _oauth_revoked_jti (
  jti        VARCHAR(36)  NOT NULL,
  revoked_at TIMESTAMP    NOT NULL,
  expires_at TIMESTAMP    NOT NULL,
  CONSTRAINT PK_OAUTH_REVOKED_JTI PRIMARY KEY (jti),
  INDEX idx_revoked_jti_expires (expires_at)
);
```

> Usar la tabla separada es más eficiente: los JTIs revocados expirados
> se pueden limpiar sin tocar `_oauth_sessions`.

### 2. Port de salida — `TokenRevocationGateway`

```java
public interface TokenRevocationGateway {
    void revokeByJti(String jti, Instant expiresAt);
    boolean isRevoked(String jti);
    void revokeSessionAndDescendants(String sessionId);
}
```

### 3. Use case — `RevokeTokenUseCase`

```java
public class RevokeTokenUseCase {

    public void execute(String rawToken, String tokenTypeHint, String tenantSlug) {
        // 1. Decodificar el token (JWT o buscar en sessions por valor opaco)
        // 2. Extraer jti y exp del payload
        // 3. Si es access_token:
        //    revocationGateway.revokeByJti(jti, exp)
        // 4. Si es refresh_token:
        //    - Encontrar todos los access_tokens emitidos con ese refresh (session_id)
        //    - Revocar cada uno
        //    - Revocar el refresh_token mismo
        // RFC 7009 §2.2: siempre devolver 200 aunque el token ya estuviera revocado
    }
}
```

### 4. Integrar en el Token Endpoint y en la validación de tokens

En el endpoint `/token` (al validar el `refresh_token`):
```java
if (revocationGateway.isRevoked(jti)) {
    throw new OAuthException("token_revoked");
}
```

En el endpoint `/introspect` (02-02):
```java
boolean active = !revocationGateway.isRevoked(jti) && !isExpired(exp);
```

### 5. Anunciar en `.well-known`

```json
{
  "revocation_endpoint": "https://auth.example.com/openid/{tenant}/revoke",
  "revocation_endpoint_auth_methods_supported": ["client_secret_basic", "client_secret_post"]
}
```

### 6. Cleanup del denylist

Ampliar `OAuthExpiredRecordsCleaner` (01-06) con:

```java
@Scheduled(cron = "0 15 3 * * ?")
public void cleanExpiredRevokedJtis() {
    // DELETE FROM _oauth_revoked_jti WHERE expires_at < NOW()
}
```

### 7. Tests de integración

- Revocar access_token → introspect devuelve `{"active": false}` ✓
- Revocar refresh_token → token endpoint con ese refresh devuelve error ✓
- Revocar token ya revocado → devuelve 200 (RFC 7009 §2.2) ✓
- Revocar token de otro tenant → error de autorización ✗

---

## Criterios de aceptación

- [ ] `_oauth_revoked_jti` creada con migración Liquibase
- [ ] Revocar access_token lo añade al denylist por JTI
- [ ] Revocar refresh_token invalida en cascada los access_tokens asociados
- [ ] El token endpoint rechaza refresh_tokens revocados
- [ ] El endpoint de introspección marca tokens revocados como `active: false`
- [ ] Siempre devuelve HTTP 200 aunque el token ya estuviera revocado (RFC 7009)
- [ ] Cleanup job elimina JTIs del denylist una vez expirados
- [ ] 4 tests de integración
