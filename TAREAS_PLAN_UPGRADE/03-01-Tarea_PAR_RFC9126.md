# Tarea 03-01 — PAR — Pushed Authorization Requests (RFC 9126)

> **Fase:** 03 — Extensiones OAuth 2.0
> **RFC:** 9126
> **Prioridad:** P2
> **Esfuerzo estimado:** Medio
> **Prerequisito:** 02-01 (PKCE)

---

## Descripción

PAR permite que los parámetros de autorización se envíen al servidor **antes**
de redirigir al usuario, devolviendo un `request_uri` opaco. Esto elimina la
exposición de `client_id`, `scope`, `redirect_uri` y `code_challenge` en la
URL del navegador — especialmente relevante en aplicaciones de alta seguridad.

---

## Flujo

```
1. Cliente → POST /openid/{tenant}/par
   Body: client_id, redirect_uri, scope, code_challenge, ...
   Response: { request_uri: "urn:ietf:params:oauth:request_uri:abc123", expires_in: 60 }

2. Cliente → GET /openid/{tenant}/authorize?client_id=xxx&request_uri=urn:...
   (sin los demás parámetros — están en el servidor)

3. Authorize flow normal continúa
```

---

## Pasos de implementación

### 1. Tabla para requests PAR

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-par-requests-table
CREATE TABLE _oauth_par_requests (
  request_uri  VARCHAR(255) NOT NULL,
  tenant_id    VARCHAR(36)  NOT NULL,
  client_id    VARCHAR(36)  NOT NULL,
  params_json  TEXT         NOT NULL,
  expires_at   TIMESTAMP    NOT NULL,
  CONSTRAINT PK_OAUTH_PAR_REQUESTS PRIMARY KEY (request_uri),
  INDEX idx_par_requests_expiry (expires_at)
);
```

### 2. Endpoint `POST /openid/{tenant}/par`

```java
@Path("/openid/{tenant}/par")
@ApplicationScoped
public class PushedAuthorizationController {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Pushed Authorization Request (RFC 9126)")
    public PushedAuthorizationResponse par(
        @PathParam("tenant") String tenant,
        @FormParam("client_id") String clientId,
        @FormParam("redirect_uri") String redirectUri,
        @FormParam("scope") String scope,
        @FormParam("code_challenge") String codeChallenge,
        @FormParam("code_challenge_method") String codeChallengeMethod,
        @FormParam("state") String state,
        @FormParam("nonce") String nonce
    ) {
        // 1. Autenticar al cliente
        // 2. Generar request_uri = "urn:ietf:params:oauth:request_uri:" + UUID.randomUUID()
        // 3. Serializar todos los params como JSON
        // 4. Persistir con TTL de 60 segundos
        // 5. Devolver { request_uri, expires_in: 60 }
    }
}

public record PushedAuthorizationResponse(
    @JsonProperty("request_uri") String requestUri,
    @JsonProperty("expires_in") int expiresIn
) {}
```

### 3. Modificar el authorize endpoint para aceptar `request_uri`

En la `ControllerPart` del authorize, al recibir la petición:

```java
String requestUri = queryParams.get("request_uri");
if (requestUri != null) {
    // Recuperar params del PAR store
    ParRequest parRequest = parGateway.findAndConsume(requestUri)
        .orElseThrow(() -> new OAuthException("invalid_request_uri"));
    // Verificar que no ha expirado
    // Mezclar con los params del query string (client_id obligatorio en query)
    params = parRequest.toParams();
}
```

### 4. Anunciar en `.well-known`

```json
{
  "pushed_authorization_request_endpoint": "https://auth.example.com/openid/{tenant}/par",
  "require_pushed_authorization_requests": false
}
```

### 5. Cleanup en el job de mantenimiento

```java
@Scheduled(cron = "0 45 3 * * ?")
public void cleanExpiredParRequests() {
    // DELETE FROM _oauth_par_requests WHERE expires_at < NOW()
}
```

### 6. Tests de integración

- PAR → authorize → token: flujo completo ✓
- PAR request_uri expirado → error `invalid_request_uri` ✗
- PAR request_uri de otro tenant → error ✗
- PAR con PKCE → challenge se conserva del PAR al token ✓

---

## Criterios de aceptación

- [ ] `POST /openid/{tenant}/par` devuelve `request_uri` válido
- [ ] `GET /openid/{tenant}/authorize?request_uri=urn:...` funciona
- [ ] El `request_uri` expira en 60 segundos
- [ ] Solo se puede usar una vez (consume y elimina)
- [ ] PKCE se preserva desde PAR hasta token endpoint
- [ ] Cleanup job elimina PAR expirados
- [ ] 4 tests de integración
