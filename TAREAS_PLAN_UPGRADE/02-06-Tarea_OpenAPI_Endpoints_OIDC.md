# Tarea 02-06 — OpenAPI/Swagger en endpoints OIDC

> **Fase:** 02 — Cumplimiento OIDC Obligatorio
> **Prioridad:** P1
> **Esfuerzo estimado:** Bajo
> **Prerequisito:** Ninguno (puede hacerse en paralelo)

---

## Descripción

Los controladores OIDC no tienen anotaciones MicroProfile OpenAPI y no aparecen
en `/q/openapi`. Esto dificulta la integración para desarrolladores y la
generación automática de SDKs. Añadir las anotaciones en todos los controllers.

---

## Pasos de implementación

### 1. Verificar que SmallRye OpenAPI está configurado

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-openapi</artifactId>
</dependency>
```

```properties
# application.properties
quarkus.smallrye-openapi.path=/q/openapi
mp.openapi.extensions.smallrye.info.title=Phylax OIDC Server
mp.openapi.extensions.smallrye.info.version=1.0.0
mp.openapi.extensions.smallrye.info.description=Multi-tenant OIDC/OAuth 2.0 Authorization Server
```

### 2. Anotaciones a añadir en cada controller

**Patrón de referencia:**

```java
@Path("/openid/{tenant}/token")
@Tag(name = "OIDC Token Endpoint", description = "OAuth 2.0 Token endpoint (RFC 6749)")
public class TokenController {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Issue OAuth 2.0 tokens",
        description = "Supports grant_type: authorization_code, refresh_token, client_credentials, password, urn:ietf:params:oauth:grant-type:device_code"
    )
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Token issued successfully",
            content = @Content(schema = @Schema(implementation = TokenResponse.class))),
        @APIResponse(responseCode = "400", description = "Invalid request or grant",
            content = @Content(schema = @Schema(implementation = OAuthErrorResponse.class))),
        @APIResponse(responseCode = "401", description = "Client authentication failed")
    })
    public Response token(/* ... */) { ... }
}
```

### 3. Endpoints a documentar

| Controlador | Endpoint | Descripción |
|-------------|----------|-------------|
| `FrontAcessController` | `GET /openid/{tenant}/authorize` | Authorization endpoint |
| Token controller | `POST /openid/{tenant}/token` | Token endpoint |
| Userinfo controller | `GET/POST /openid/{tenant}/userinfo` | UserInfo endpoint |
| Revocation controller | `POST /openid/{tenant}/revoke` | Token revocation |
| JWKS controller | `GET /openid/{tenant}/.well-known/jwks.json` | JSON Web Key Set |
| Discovery controller | `GET /openid/{tenant}/.well-known/openid-configuration` | Discovery document |
| Device controller | `POST /openid/{tenant}/device/code` | Device authorization |
| Introspection controller | `POST /openid/{tenant}/introspect` | Token introspection (nueva) |

### 4. Schemas OpenAPI para modelos comunes

Crear clases de respuesta con `@Schema`:

```java
@Schema(name = "TokenResponse", description = "OAuth 2.0 Token Response")
public record TokenResponseDto(
    @Schema(description = "The access token") String access_token,
    @Schema(description = "Token type, always 'Bearer'") String token_type,
    @Schema(description = "Seconds until expiration") long expires_in,
    @Schema(description = "Refresh token if requested") String refresh_token,
    @Schema(description = "ID token if openid scope") String id_token,
    @Schema(description = "Granted scope") String scope
) {}

@Schema(name = "OAuthError", description = "OAuth 2.0 Error Response")
public record OAuthErrorDto(
    @Schema(description = "Error code") String error,
    @Schema(description = "Human-readable description") String error_description
) {}
```

### 5. Verificar resultado

```bash
# Arrancar en dev mode
mvn quarkus:dev

# Verificar que aparecen todos los endpoints
curl http://localhost:8080/q/openapi | jq '.paths | keys'
```

---

## Criterios de aceptación

- [ ] Todos los controladores OIDC listados tienen anotaciones `@Operation` y `@APIResponse`
- [ ] Los 8 endpoints aparecen en `GET /q/openapi`
- [ ] Swagger UI en `GET /q/swagger-ui` muestra la documentación legible
- [ ] Schemas `TokenResponse` y `OAuthError` definidos con `@Schema`
- [ ] La spec OpenAPI puede exportarse para generar SDKs
