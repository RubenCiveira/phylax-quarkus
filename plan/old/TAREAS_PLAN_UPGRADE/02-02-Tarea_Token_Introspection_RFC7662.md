# Tarea 02-02 — Token Introspection (RFC 7662)

[x] Done

> **Fase:** 02 — Cumplimiento OIDC Obligatorio
> **RFC:** 7662
> **Prioridad:** P1
> **Esfuerzo estimado:** Bajo
> **Prerequisito:** 01-05 (JTI en JWT)

---

## Descripción

Permite a los resource servers (microservicios) validar tokens de acceso
opacos o JWT emitidos por este servidor sin verificar la firma localmente.
Caso de uso típico: microservicios que reciben un Bearer token y necesitan
saber si es válido, sus scopes y el sujeto.

---

## Endpoint a crear

```
POST /openid/{tenant}/introspect
Authorization: Basic <client_id:client_secret>
Content-Type: application/x-www-form-urlencoded

token=<access_token>&token_type_hint=access_token
```

### Respuesta activa

```json
{
  "active": true,
  "sub": "550e8400-e29b-41d4-a716-446655440000",
  "client_id": "my-app",
  "scope": "openid email profile",
  "exp": 1234567890,
  "iat": 1234567800,
  "iss": "https://auth.example.com/openid/my-tenant",
  "jti": "a1b2c3d4-..."
}
```

### Respuesta inactiva

```json
{ "active": false }
```

---

## Pasos de implementación

### 1. Crear el sub-feature `introspection`

Estructura de paquetes:
```
src/main/java/net/civeira/phylax/features/oauth/introspection/
├── domain/
│   ├── TokenIntrospectionResult.java          (value object)
│   └── gateway/
│       └── TokenLookupGateway.java            (port de salida)
├── application/
│   └── usecase/
│       └── IntrospectTokenUseCase.java
└── infrastructure/
    ├── driver/
    │   └── rest/
    │       └── IntrospectionController.java   (@Path("/openid/{tenant}/introspect"))
    └── driven/
        └── TokenLookupRepository.java         (impl del port)
```

### 2. Dominio — `TokenIntrospectionResult`

```java
public record TokenIntrospectionResult(
    boolean active,
    String sub,
    String clientId,
    String scope,
    long exp,
    long iat,
    String iss,
    String jti
) {
    public static TokenIntrospectionResult inactive() {
        return new TokenIntrospectionResult(false, null, null, null, 0, 0, null, null);
    }
}
```

### 3. Port — `TokenLookupGateway`

```java
public interface TokenLookupGateway {
    Optional<TokenIntrospectionResult> lookupByJti(String tenantSlug, String jti);
    Optional<TokenIntrospectionResult> lookupByRawToken(String tenantSlug, String rawToken);
}
```

### 4. Use case — `IntrospectTokenUseCase`

1. Validar credenciales del cliente que hace la petición (Basic Auth)
2. Si el token es JWT: decodificar y extraer `jti` del payload
3. Si el token es opaco: usarlo directamente como lookup key
4. Llamar a `TokenLookupGateway` para verificar que:
   - El JTI está en `_oauth_sessions` (no ha sido revocado)
   - La sesión no ha expirado
5. Devolver `TokenIntrospectionResult` activo o inactivo

### 5. Controller REST

```java
@Path("/openid/{tenant}/introspect")
@ApplicationScoped
public class IntrospectionController {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Token introspection (RFC 7662)")
    @APIResponse(responseCode = "200", description = "Introspection result")
    public TokenIntrospectionResult introspect(
        @PathParam("tenant") String tenant,
        @FormParam("token") String token,
        @FormParam("token_type_hint") String tokenTypeHint,
        @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        // 1. Validar Basic Auth del client
        // 2. Delegar en use case
    }
}
```

### 6. Repositorio — `TokenLookupRepository`

Buscar en `_oauth_sessions` por `jti` (columna añadida en 01-05):

```sql
SELECT session, expiration, client_id, auth_data
FROM _oauth_sessions
WHERE jti = ?
  AND expiration > NOW()
```

### 7. Anunciar en `.well-known`

```json
{
  "introspection_endpoint": "https://auth.example.com/openid/{tenant}/introspect"
}
```

### 8. Tests de integración

- Token activo → `{"active": true, "sub": ...}`
- Token expirado → `{"active": false}`
- Token revocado → `{"active": false}`
- Token inválido/aleatorio → `{"active": false}`
- Sin credenciales de cliente → `401`

---

## Criterios de aceptación

- [ ] `POST /openid/{tenant}/introspect` existe y responde RFC 7662
- [ ] Lookup por JTI en `_oauth_sessions`
- [ ] Tokens expirados y revocados devuelven `{"active": false}`
- [ ] Endpoint anunciado en `.well-known`
- [ ] Anotaciones `@Operation` / `@APIResponse` presentes
- [ ] 5 tests de integración
