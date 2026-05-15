# Tarea 05-06 — API Keys para usuarios (Personal Access Tokens)

> **Fase:** 05 — API de Gestión BaaS
> **Prioridad:** P2
> **Esfuerzo estimado:** Medio
> **Prerequisito:** Ninguno específico

---

## Descripción

Los Personal Access Tokens (PAT) permiten a los usuarios autenticarse en APIs
sin pasar por el flujo OIDC — ideal para scripts, CI/CD, y herramientas CLI.
El token se muestra una sola vez al crearlo; solo se almacena su hash.

Ya existe `access_api_key_client` para clientes M2M; esta tarea añade
el equivalente para usuarios finales.

---

## Pasos de implementación

### 1. Tabla `access_user_api_key`

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-user-api-key-table
CREATE TABLE access_user_api_key (
  uid          VARCHAR(36)   NOT NULL,
  user_uid     VARCHAR(36)   NOT NULL,
  tenant_id    VARCHAR(36)   NOT NULL,
  name         VARCHAR(255)  NOT NULL COMMENT 'Nombre descriptivo del token',
  key_prefix   VARCHAR(8)    NOT NULL COMMENT 'Primeros 8 chars en claro para identificación',
  key_hash     VARCHAR(64)   NOT NULL COMMENT 'SHA-256 del token completo',
  scopes       TEXT          NULL     COMMENT 'Scopes separados por espacio',
  last_used_at TIMESTAMP     NULL,
  expires_at   TIMESTAMP     NULL,
  created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT PK_ACCESS_USER_API_KEY PRIMARY KEY (uid),
  INDEX idx_user_api_key_user (user_uid, tenant_id),
  INDEX idx_user_api_key_hash (key_hash)
);
```

### 2. Generación del token

**Formato:** `phx_{prefix}_{random}` donde:
- `phx_` — prefijo fijo para identificar tokens de Phylax
- `{prefix}` — 8 caracteres aleatorios (se almacenan en claro para identificación)
- `{random}` — 32 bytes aleatorios en Base64url (256 bits de entropía)

```java
public String generateToken() {
    byte[] random = new byte[32];
    new SecureRandom().nextBytes(random);
    String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(random);
    String prefix = base64.substring(0, 8);
    return "phx_" + base64;  // prefix es base64[0..7]
}

public String hashToken(String token) {
    // SHA-256 del token completo
    byte[] digest = MessageDigest.getInstance("SHA-256")
        .digest(token.getBytes(StandardCharsets.UTF_8));
    return HexFormat.of().formatHex(digest);
}
```

### 3. Use cases

**`CreateApiKeyUseCase`:**
1. Verificar límite de PATs por usuario (configurable, default: 10)
2. Generar el token en claro
3. Extraer `key_prefix` (caracteres 4-12 del token)
4. Almacenar `key_hash = SHA-256(token)` — NUNCA el token en claro
5. Devolver el token en claro **una sola vez**
6. Publicar evento de creación para audit log

**`AuthenticateWithApiKeyUseCase`:**
1. Calcular SHA-256 del token recibido en el header
2. Buscar en `access_user_api_key` por `key_hash`
3. Verificar que no ha expirado
4. Actualizar `last_used_at`
5. Devolver el `UserIdentity` del usuario propietario

### 4. Integración en la autenticación de requests

En el security interceptor de Quarkus, añadir soporte para PAT como
alternativa a Bearer JWT:

```java
@ApplicationScoped
public class PhylaxApiKeyAuthMechanism implements HttpAuthenticationMechanism {

    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context,
                                              IdentityProviderManager identityProviderManager) {
        String auth = context.request().getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer phx_")) {
            String token = auth.substring("Bearer ".length());
            return authenticateWithApiKey(token);
        }
        return Uni.createFrom().optional(Optional.empty());
    }
}
```

### 5. Endpoints

```java
@Path("/api/me/api-keys")
@Tag(name = "Personal Access Tokens")
public class PersonalApiKeyController {

    @GET
    @Operation(summary = "List API keys (without token values)")
    public List<ApiKeyDto> listKeys(@Context SecurityContext security) { ... }

    @POST
    @Operation(summary = "Create a new API key — token shown only once")
    @APIResponse(responseCode = "201", description = "API key created with token in plain text")
    public ApiKeyCreationDto createKey(
        @Valid CreateApiKeyRequest request,
        @Context SecurityContext security
    ) { ... }
    // Request: { name, scopes?, expires_at? }
    // Response: { uid, name, token, prefix, scopes, expires_at }
    //   ↑ token solo en esta respuesta

    @DELETE
    @Path("/{uid}")
    @Operation(summary = "Revoke an API key")
    public Response revokeKey(@PathParam("uid") UUID uid,
                               @Context SecurityContext security) { ... }
}
```

### 6. Tests de integración

- Crear PAT → token mostrado una vez, `GET /api/me/api-keys` no lo muestra ✓
- Usar PAT como Bearer → request autenticado con el usuario propietario ✓
- PAT expirado → 401 ✗
- Revocar PAT → token rechazado inmediatamente ✓
- PAT de otro usuario → 403 ✗
- Superar límite de PATs → error con mensaje informativo ✗

---

## Criterios de aceptación

- [ ] Tabla `access_user_api_key` con migración, token almacenado como SHA-256
- [ ] Token mostrado en texto claro **solo** en la respuesta de creación
- [ ] `GET /api/me/api-keys` lista sin revelar los tokens
- [ ] `POST /api/me/api-keys` crea con nombre y scopes opcionales
- [ ] `DELETE /api/me/api-keys/{uid}` revoca inmediatamente
- [ ] `HttpAuthenticationMechanism` acepta `Bearer phx_*` como alternativa a JWT
- [ ] `last_used_at` se actualiza en cada uso
- [ ] Límite de PATs por usuario configurable
- [ ] 6 tests de integración
