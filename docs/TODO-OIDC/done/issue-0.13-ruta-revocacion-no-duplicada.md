# 0.13 — Ruta de revocación no duplicada

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** hecha (24-10-2026).
- **Implementación:** separar `POST /revocation` RFC 7009 de la operación de pre-sesión.
- **Cierre:** ambas rutas llegan inequívocamente a su controlador con independencia de `Content-Type`.
- **Fuente:** [§5, 0.13](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Qué hace (funcional)

| Ruta | Para qué | Quién la usa |
|---|---|---|
| `POST /oauth/openid/{tenant}/revocation` | Revocar un access o refresh token (RFC 7009). Exige cliente confidencial; responde 200 aunque el token no exista | Aplicaciones cliente |
| `POST /oauth/openid/{tenant}/revoke` | Abandonar un login en curso: borra la pre-sesión de la cookie `PRE_SESSION_ID` | Pantallas de login |

Antes, `AuthorizeHtml` y `TokenRevocationController` declaraban el mismo `POST /revocation` y JAX-RS elegía por `Content-Type`: una petición que no fuese un formulario borraba la pre-sesión y respondía 200, sin autenticar cliente ni revocar nada.

## Implementación Java (24-10-2026)

- `AuthorizeHtml` solo publica `POST /revoke` (`revokePreSession`); la ruta `/revocation` es exclusiva de `TokenRevocationController`. El *discovery* ya anunciaba `/revocation` como RFC 7009.
- `/revocation` sin formulario responde 415. Para ello `ThrowableMapper` respeta ahora el estado de las `WebApplicationException` de JAX-RS menores de 500 (antes cualquier 404/405/415 del framework salía como 500).
- **Pruebas** (`TokenEndpointContractTest`): `/revocation` sin cliente da `invalid_client` 401; con cliente, 200 `no-store` y el JTI revocado; con JSON y cookie de pre-sesión, 415. `SessionManagementTest.revocation_returns200` sigue cubriendo `/revoke`.
