# Tarea 03-04 — JAR — JWT Secured Authorization Request (RFC 9101)

> **Fase:** 03 — Extensiones OAuth 2.0
> **RFC:** 9101
> **Prioridad:** P4
> **Esfuerzo estimado:** Medio
> **Prerequisito:** 03-01 (PAR — para implementación completa con PAR+JAR)

---

## Descripción

JAR permite enviar los parámetros del authorize como un JWT firmado por el
cliente (`request` parameter). Garantiza la integridad y no-repudio de la
petición de autorización. Es especialmente relevante en contextos FAPI
(Financial-grade API).

---

## Pasos de implementación

### 1. Soporte del parámetro `request` en `/authorize`

Al recibir `GET /openid/{tenant}/authorize?request=<jwt>`:

```java
String requestJwt = queryParams.get("request");
if (requestJwt != null) {
    // 1. Decodificar el JWT (sin verificar aún)
    // 2. Extraer el claim `iss` del JWT (debe coincidir con client_id)
    // 3. Obtener la clave pública del cliente (de su JWKS URI o clave registrada)
    // 4. Verificar la firma del JWT
    // 5. Verificar que `aud` incluye el issuer del servidor
    // 6. Mezclar los claims del JWT con los query params
    //    (JWT tiene precedencia sobre query params)
}
```

### 2. Obtener la clave pública del cliente

El cliente puede registrar su JWKS de dos formas:
- `jwks_uri`: URL al JSON Web Key Set del cliente
- `jwks`: JWKS inline en el registro del cliente

Añadir columnas:

```sql
-- changeset phylax-dev:add-jwks-to-relying-party
ALTER TABLE access_relying_party
  ADD COLUMN jwks_uri  TEXT NULL,
  ADD COLUMN jwks_json TEXT NULL;
```

**Clase de servicio para resolver la clave:**

```java
@ApplicationScoped
public class ClientJwksResolver {

    @Inject
    WebClient webClient;  // Vert.x WebClient para jwks_uri

    public PublicKey resolveSigningKey(RelyingParty client, String kid) {
        if (client.jwksUri() != null) {
            // Fetch y cachear el JWKS (TTL: 5 minutos)
            return fetchFromUri(client.jwksUri(), kid);
        } else if (client.jwksJson() != null) {
            return parseFromInline(client.jwksJson(), kid);
        }
        throw new OAuthException("Client has no JWKS configured");
    }
}
```

### 3. Soporte del parámetro `request_uri`

Si el cliente pasa `request_uri` (una URL a un JWT externo):

```java
String requestUri = queryParams.get("request_uri");
if (requestUri != null) {
    // Verificar que la URI está pre-registrada en redirect_uris del cliente
    // Fetch del JWT desde la URI (con timeout corto: 3s)
    // Proceder igual que con `request`
}
```

### 4. Anunciar en `.well-known`

```json
{
  "request_object_signing_alg_values_supported": ["RS256", "PS256", "ES256"],
  "request_uri_parameter_supported": true,
  "require_request_uri_registration": false
}
```

### 5. Tests de integración

- `request` JWT firmado RS256 → authorize funciona ✓
- `request` JWT con firma inválida → `invalid_request_object` ✗
- `request` JWT con `aud` incorrecto → error ✗
- `request_uri` válida → JWT fetched y procesado ✓

---

## Criterios de aceptación

- [ ] Parámetro `request` (JWT firmado) aceptado en `/authorize`
- [ ] Verificación de firma con JWKS del cliente
- [ ] Parámetro `request_uri` soportado con fetch externo
- [ ] `.well-known` anuncia `request_object_signing_alg_values_supported`
- [ ] Claims del JWT tienen precedencia sobre query params
- [ ] 4 tests de integración
