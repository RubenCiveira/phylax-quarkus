# Tarea 01-05 — Añadir `jti` a todos los JWT emitidos

[x] Done

> **Fase:** 01 — Refactoring y Deuda Técnica
> **Prioridad:** P0
> **Esfuerzo estimado:** Bajo
> **Bloquea:** Token Revocation (02-04), Token Introspection (02-02)

---

## Descripción

El claim `jti` (JWT ID, RFC 7519 §4.1.7) es un identificador único por token.
Es obligatorio para implementar la revocación de tokens (allowlist/denylist de JTIs)
y para que el endpoint de introspección pueda identificar un token específico.

Actualmente los JWT emitidos por el servidor carecen de este claim.

---

## Pasos de implementación

### 1. Localizar la generación de JWT

```bash
grep -rn "JwtClaimsSet\|Jwt.claims\|sign\|buildJwt\|createToken" \
  src/main/java --include="*.java" | grep -v test
```

El JWT builder probablemente está en:
`src/main/java/net/civeira/phylax/features/oauth/token/`
o en `src/main/java/net/civeira/phylax/features/oauth/key/`

### 2. Añadir el claim `jti` en la construcción del token

Usando SmallRye JWT (Quarkus):

```java
import io.smallrye.jwt.build.Jwt;
import java.util.UUID;

// En el builder del token:
Jwt.claims()
    .claim("jti", UUID.randomUUID().toString())  // <-- añadir esta línea
    .claim("sub", userUid.toString())
    .claim("iss", issuer)
    // ... resto de claims
    .sign(privateKey);
```

### 3. Exponer el `jti` en el value object del token

Si existe un `AccessToken` o `TokenResponse` como value object de dominio,
añadir el campo `jti`:

```java
public record AccessToken(
    String jti,      // <-- nuevo campo
    String value,
    Instant expiresAt,
    String subject
    // ...
) {}
```

### 4. Persistir el `jti` en `_oauth_sessions`

Añadir columna `jti VARCHAR(36)` en la tabla `_oauth_sessions` para
poder hacer lookup por JTI en introspección y revocación.

**Migración Liquibase:**

Crear fichero:
`src/main/resources/db/migration/mysql/000.001.002/YYYYMMDDHHMMSS-add-jti-to-sessions.sql`

```sql
-- liquibase formatted sql

-- changeset phylax-dev:add-jti-to-oauth-sessions
ALTER TABLE _oauth_sessions
  ADD COLUMN jti VARCHAR(36) NULL AFTER session;

-- changeset phylax-dev:add-jti-index
CREATE INDEX idx_oauth_sessions_jti ON _oauth_sessions (jti);
```

### 5. Verificar que el `jti` aparece en tokens decodificados

Añadir un test de aserción:

```java
// En OidcFlowClient o en el test de token endpoint
String token = response.path("access_token");
JsonObject claims = decodeJwtPayload(token);
assertThat(claims.getString("jti")).isNotBlank();
```

---

## Archivos afectados

- `src/main/java/.../oauth/token/` — builder del token
- `src/main/java/.../oauth/key/` — si el signing está aquí
- `src/main/resources/db/migration/` — nueva migración
- Tests de integración del token endpoint

---

## Criterios de aceptación

- [ ] Todos los `access_token` emitidos contienen claim `jti` con UUID v4
- [ ] La columna `jti` existe en `_oauth_sessions`
- [ ] Hay un índice sobre `_oauth_sessions.jti`
- [ ] Test que verifica la presencia del `jti` en el token
- [ ] `mvn test -Dgroups="oidc-flow"` verde
