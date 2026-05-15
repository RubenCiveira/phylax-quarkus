# Tarea 03-03 — Client Credentials Grant M2M (completar)

> **Fase:** 03 — Extensiones OAuth 2.0
> **RFC:** 6749 §4.4
> **Prioridad:** P1
> **Esfuerzo estimado:** Bajo
> **Prerequisito:** Ninguno (independiente, puede adelantarse)

---

## Descripción

El grant `client_credentials` permite autenticación máquina-a-máquina (M2M)
sin intervención de un usuario. El token emitido tiene `sub = client_id` en
lugar de `sub = user_uid`. Este caso de uso es esencial para microservicios
que necesitan llamarse entre sí de forma autenticada.

---

## Estado actual

El `access_api_key_client` existe para clientes M2M, pero el grant
`client_credentials` en el token endpoint puede no estar completamente
implementado o no emitir JWT válidos con las claims correctas.

---

## Pasos de implementación

### 1. Verificar el handler actual

```bash
grep -rn "client_credentials" src/main/java --include="*.java"
```

Localizar el `Granter` o `ControllerPart` que maneja este grant type.

### 2. Asegurar las claims correctas en el JWT M2M

El JWT para `client_credentials` debe tener:

```json
{
  "iss": "https://auth.example.com/openid/tenant",
  "sub": "client-uuid",          // sub = client_id, NO user_uid
  "aud": ["resource-server"],
  "iat": 1234567800,
  "exp": 1234571400,
  "jti": "unique-uuid",
  "scope": "api:read api:write",
  "client_id": "client-uuid"
}
```

**No debe incluir:** `email`, `name`, ni claims de usuario.

### 3. Política de scopes para M2M

Añadir columna `allowed_scopes` en `access_api_key_client` (o en `access_relying_party`
si los clientes M2M están ahí):

```sql
-- changeset phylax-dev:add-allowed-scopes-to-api-key-client
ALTER TABLE access_api_key_client
  ADD COLUMN allowed_scopes TEXT NULL COMMENT 'Comma-separated list of scopes allowed for client_credentials';
```

El token endpoint solo concede los scopes que estén en `allowed_scopes`
(intersección entre los solicitados y los permitidos).

### 4. Autenticación del cliente en el token endpoint

Soportar dos métodos:
- `client_secret_basic`: `Authorization: Basic base64(client_id:client_secret)`
- `client_secret_post`: `client_id` y `client_secret` en el body del form

```java
ClientCredentials credentials = extractClientCredentials(request);
ClientRecord client = clientGateway.findByCredentials(credentials)
    .orElseThrow(() -> new OAuthException("invalid_client"));
```

### 5. Use case `IssueClientCredentialsTokenUseCase`

```java
public TokenResponse execute(String clientId, String requestedScope, String tenantSlug) {
    // 1. Verificar que el cliente existe y está activo
    // 2. Verificar que el cliente tiene grant_type=client_credentials habilitado
    // 3. Calcular scopes concedidos = intersección(requestedScope, client.allowedScopes)
    // 4. Emitir JWT con sub=clientId, sin refresh_token
    // 5. No guardar sesión de usuario — optional: registrar en audit log
}
```

### 6. Tests de integración

- `client_credentials` con credenciales válidas → token con `sub=client_id` ✓
- Scopes fuera de `allowed_scopes` → no concedidos en el token ✓
- Credenciales incorrectas → `invalid_client` ✗
- Cliente sin permiso para `client_credentials` → `unauthorized_client` ✗
- Token emitido no contiene claims de usuario (`email`, `name`) ✓

---

## Criterios de aceptación

- [ ] `grant_type=client_credentials` emite JWT con `sub=client_id`
- [ ] JWT no incluye claims de usuario
- [ ] Política de `allowed_scopes` por cliente
- [ ] Autenticación por `client_secret_basic` y `client_secret_post`
- [ ] Sin `refresh_token` en la respuesta (no aplica para M2M)
- [ ] 5 tests de integración
