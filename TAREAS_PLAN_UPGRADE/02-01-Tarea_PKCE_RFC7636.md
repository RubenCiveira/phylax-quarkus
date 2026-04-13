# Tarea 02-01 — PKCE — Proof Key for Code Exchange (RFC 7636)

> **Fase:** 02 — Cumplimiento OIDC Obligatorio
> **RFC:** 7636
> **Prioridad:** P0
> **Esfuerzo estimado:** Bajo-Medio
> **Prerequisito:** Ninguno de la Fase 01 (puede hacerse en paralelo)

---

## Descripción

PKCE es obligatorio para cualquier cliente público (SPA, app móvil, CLI).
Sin él, el Authorization Code Flow es vulnerable a intercepción del código
de autorización. La RFC 7636 define los parámetros `code_challenge` y `code_verifier`.

---

## Pasos de implementación

### 1. Añadir columnas en `_oauth_temporal_codes`

Los códigos de autorización ya se almacenan en `_oauth_temporal_codes`.
Necesitamos persister el challenge para validarlo al intercambiar el código.

**Migración Liquibase** en `src/main/resources/db/migration/mysql/000.001.002/`:

```sql
-- liquibase formatted sql

-- changeset phylax-dev:pkce-columns-temporal-codes
ALTER TABLE _oauth_temporal_codes
  ADD COLUMN code_challenge VARCHAR(128) NULL,
  ADD COLUMN code_challenge_method VARCHAR(10) NULL;
```

### 2. Modificar el paso `authorize` — capturar el challenge

**Fichero:** `src/main/java/net/civeira/phylax/features/oauth/authentication/`
(controlador HTML `FrontAcessController` o la `ControllerPart` del authorize)

Al recibir la petición GET `/openid/{tenant}/authorize`:
- Extraer `code_challenge` y `code_challenge_method` de los query params
- Validar que `code_challenge_method` es `S256` o `plain` (o ausente para compatibilidad)
- Persistir los valores junto al código temporal al generar el authorization code

**Value object:**
```java
public record PkceChallenge(String codeChallenge, ChallengeMethod method) {
    public enum ChallengeMethod { S256, PLAIN }

    public static Optional<PkceChallenge> from(String challenge, String method) {
        if (challenge == null || challenge.isBlank()) return Optional.empty();
        var m = "plain".equalsIgnoreCase(method) ? ChallengeMethod.PLAIN : ChallengeMethod.S256;
        return Optional.of(new PkceChallenge(challenge, m));
    }
}
```

### 3. Modificar el endpoint `/token` — validar el verifier

Al recibir `grant_type=authorization_code`:
- Leer `code_verifier` del body del formulario
- Recuperar el `code_challenge` y `code_challenge_method` almacenados con el código
- Validar:

```java
private boolean verifyPkce(String codeVerifier, String storedChallenge, String method) {
    if (storedChallenge == null) {
        // Si no se usó PKCE al authorize, no requerir verifier
        return true;
    }
    if (codeVerifier == null || codeVerifier.isBlank()) {
        throw new OAuthException("code_verifier required");
    }
    if ("S256".equals(method)) {
        // SHA-256 del verifier, codificado en Base64url sin padding
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(
            codeVerifier.getBytes(StandardCharsets.US_ASCII));
        String computed = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        return computed.equals(storedChallenge);
    }
    // plain
    return codeVerifier.equals(storedChallenge);
}
```

### 4. Anunciar en `.well-known/openid-configuration`

**Fichero:** `src/main/java/net/civeira/phylax/features/oauth/oidc/`

```json
{
  "code_challenge_methods_supported": ["S256", "plain"]
}
```

### 5. Tests de integración

Crear `src/test/java/net/civeira/phylax/testing/oauth/flow/PkceFlowTest.java`:

- Test happy path S256: generar challenge correcto → intercambiar → obtener token ✓
- Test verifier incorrecto: challenge S256 → verifier erróneo → `invalid_grant` ✗
- Test sin PKCE: cliente confidencial sin challenge → flujo normal funciona ✓
- Test S256 con verifier `plain` → `invalid_grant` ✗

---

## Criterios de aceptación

- [ ] `code_challenge` y `code_challenge_method` se persisten en `_oauth_temporal_codes`
- [ ] Método `S256` implementado correctamente (SHA-256 + Base64url)
- [ ] Token endpoint valida el verifier antes de emitir el token
- [ ] `.well-known` anuncia `code_challenge_methods_supported: ["S256", "plain"]`
- [ ] 4 tests de integración cubriendo happy path y casos de error
