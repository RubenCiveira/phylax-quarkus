# 1.4 — Errores y caché de token

- **Fase:** 1 · **Esfuerzo:** S · **Estado:** hecha (24-10-2026).
- **Implementación:** devolver errores OAuth RFC 6749 §5.2 (`invalid_grant`, `invalid_client`, etc.), `WWW-Authenticate` cuando proceda y `Cache-Control: no-store` y `Pragma: no-cache` en éxito y error.
- **Cierre:** respuestas negativas y positivas de `/token` con códigos, cuerpo y cabeceras correctos; los scopes son elementos exactos ([1.15](../issue-1.15-scopes-conjuntos-exactos.md)).
- **Fuente:** [§5, 1.4](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).

## Qué hace (funcional)

Toda respuesta de `POST /token`, de cualquier grant, lleva `Cache-Control: no-store` y `Pragma: no-cache`. Los errores son JSON `{"error", "error_description"}`:

| Situación | Estado | `error` |
|---|---|---|
| Falta `grant_type`, `code`, `refresh_token`, `username` o `password` | 400 | `invalid_request` |
| `grant_type` desconocido | 400 | `unsupported_grant_type` |
| Cliente no autenticado (ver [0.9](issue-0.9-autenticacion-cliente-unificada.md)) | 401 + `WWW-Authenticate: Basic realm="oauth"` | `invalid_client` |
| Grant no permitido al cliente | 400 | `unauthorized_client` |
| Code o refresh inválido, ajeno, gastado o de sesión cerrada; credenciales del usuario erróneas | 400 | `invalid_grant` |
| Refresh que amplía el scope original | 400 | `invalid_scope` |
| Device: pendiente, demasiado rápido, denegado, caducado (RFC 8628 §3.5) | 400 | `authorization_pending`, `slow_down`, `access_denied`, `expired_token` |
| Grant `password` de un usuario con segundo factor | **403** | `mfa_required` + `mfa_token`, `expires_in` |

## Implementación Java (24-10-2026)

- `OAuthTokenException` (dominio de `authentication`) lleva `error`, descripción y estado; los casos de uso la lanzan y `TokenController` solo la serializa. `DeviceCodeGranter` traduce `DeviceAuthorizationException` a ella en lugar de lanzar una `WebApplicationException` desde la capa de aplicación.
- **Cambios de contrato:**
  - Credenciales de usuario erróneas en `password`: de 401 sin formato a 400 `invalid_grant` (RFC 6749 §5.2).
  - `mfa_required`: de 401 a 403 con el mismo cuerpo. 401 queda reservado a `invalid_client`; 403 es la convención de facto (Auth0) para este error no estándar. El grant `mfa` está descartado en PHP (decisión 040) y queda pendiente de [2.5](../issue-2.5-grants-inalcanzables-codigo-muerto.md).
  - Grant no permitido: de `unsupported_grant_type` a `unauthorized_client`.
- **Corregido:** `PasswordGranter` y `MfaGranter` lanzaban `NullPointerException` (500) si faltaba un parámetro.
- **Pendiente fuera de la ficha:** scopes como conjuntos exactos en toda la emisión ([1.15](../issue-1.15-scopes-conjuntos-exactos.md)); el endpoint ya los trocea por espacios y compara elementos completos.
- **Pruebas:** `TokenEndpointContractTest` (cada fila de la tabla salvo device, excluido del perfil de test) y `PasswordGrantTest` adaptado.
