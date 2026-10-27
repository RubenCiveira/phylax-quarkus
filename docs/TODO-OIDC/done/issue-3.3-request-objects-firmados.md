# 3.3 — Request objects firmados

- **Fase:** 3 (según necesidad) · **Estado:** hecha e integrada con modelo regenerado (26-10-2026), [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md) · **Referencia:** RFC 9101.
- **Implementación:** validar `request`/`request_uri` externos con JWKS, `iss`/`aud`/`exp`/`nbf`/`iat`/`jti` y anti-replay; resolver conflictos de parámetros. Para `jwks_uri` remoto, usar un fetcher de infraestructura con HTTPS, bloqueo de redes privadas/loopback, límites de tiempo/tamaño y caché por cliente; reutilizarlo en [3.2](../issue-3.2-autenticacion-jwt-cliente.md) y [3.4](../pospuestos/issue-3.4-federacion-oidc-generica.md).
- **Cierre:** objeto válido aceptado y objeto expirado, de otro cliente, mal firmado o reutilizado rechazado; discovery coherente.
- **Fuente:** [§5, 3.3](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).

## Qué hace (funcional)

| Origen | Tratamiento |
|---|---|
| `request` en authorize (JAR por valor) | Se verifica, se guarda como un PAR ya consumido y el navegador se redirige (303) a `?client_id&request_uri` con su `PAR_BINDING`; los formularios del login no vuelven a presentar el objeto |
| `request` dentro de un push PAR | Sus claims sustituyen al resto de parámetros (RFC 9126 §3) |
| `request_uri` que no es de PAR (por referencia) | `request_uri_not_supported`: no se descargan objetos remotos |

**Validación** (`RequestObjectVerifier`): firma con una clave del cliente (`jwks_json` en línea o `jwks_uri`) y con su algoritmo registrado, solo `RS256` o `ES256` (nunca `none` ni HMAC); `iss` y `client_id` = cliente; `aud` incluye el issuer del tenant; `exp` obligatorio y vida máxima de una hora; `nbf` no futuro; `jti` obligatorio y de **un solo uso** (`_oauth_replay_jti`, compartido con 3.2); sin `request`/`request_uri` anidados. Error: `invalid_request_object`.

**`jwks_uri` remoto** (`RemoteJwksFetcher`): solo HTTPS, sin redirecciones ni credenciales en la URL, rechazo de hosts que resuelven a loopback, privadas, link-local, *unique local*, CGNAT, multicast o sin especificar; 3 s de conexión y respuesta, 64 KiB máximo; caché de 5 min por URL. Riesgo residual: DNS *rebinding* entre la resolución y la conexión.

## Implementación Java

- `RequestObjectVerifier`, `ClientJwtSignatureGateway`/`JwksSignatureAdapter`, `JwtReplayGateway`/`JwtReplaySqlAdapter`, `RemoteJwksFetcher`, `AuthorizationRequestResolver`, `ResolveParRequestUsecase.adopt`.
- La tabla anti-replay ya figura en las migraciones del [modelo](cambios-modelo-J02-J05.md) (§3). Si no se ha migrado, todo request object devuelve `invalid_request_object` (fallo cerrado).
- Discovery: `request_parameter_supported: true`, `request_uri_parameter_supported: false`, `request_object_signing_alg_values_supported: [RS256, ES256]`, sin algoritmos de cifrado.
- **Pruebas:** `RequestObjectVerifierUnitTest`, `JwksSignatureAdapterUnitTest`, `RemoteJwksFetcherUnitTest` (SSRF), `AuthorizeRequestFlowTest` (válido una vez, replay, otra audiencia, `none`, por referencia) y `DynamicClientRegistrationMariaDbTest` (primer uso atómico y aislado por tenant/cliente).
