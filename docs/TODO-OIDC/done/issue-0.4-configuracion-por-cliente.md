# 0.4 — Configuración efectiva por cliente

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** hecha (25-10-2026), [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md).
- **Implementación:** aplicar `grantTypesJson`, `responseTypesJson`, `allowAllScopes`, `allowedScopesM2m` y `tokenEndpointAuthMethod` de `TrustedClient`; quitar `password` por defecto.
- **Cierre:** tests con dos clientes de políticas distintas para grants, response types, scopes y método de autenticación.
- **Fuente:** [§5, 0.4](../CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Qué hace (funcional)

La configuración registrada en el cliente de confianza es la que manda en cada flujo:

| Campo de `TrustedClient` | Efecto | Sin valor |
|---|---|---|
| `grantTypesJson` | Grants que el cliente puede usar en `/token`, **también** `authorization_code` y `refresh_token`; `mfa` va con `password`. Otro grant: `unauthorized_client` sin tocar al usuario | `authorization_code` + `refresh_token` (**nunca `password`**) |
| `grantTypesJson` sin `refresh_token` | El cliente no recibe refresh token | — |
| `responseTypesJson` | `response_type` admitidos en authorize (comparados como conjuntos); `code` exige además el grant `authorization_code`. Otro: redirección con `error=unauthorized_client`; sin `response_type`: `invalid_request` | `code` |
| `allowAllScopes` | Flujos de usuario: cualquier scope; si no, solo los OIDC estándar (`openid profile email address phone offline_access`) | solo OIDC estándar |
| `allowedScopesM2m` | `client_credentials`: sin `scope`, todos los suyos; con `scope`, la intersección; si queda vacía, `invalid_scope` | ninguno |
| `tokenEndpointAuthMethod` | Ya aplicado en J-01 ([0.9](issue-0.9-autenticacion-cliente-unificada.md)) | — |

La respuesta de `/token` incluye ahora `scope` con lo concedido de verdad, y omite los campos vacíos (`refresh_token`, `id_token`).

## Implementación Java

- `ClientDetails`: `allowedResponseTypes`, `allowedScopesM2m`, `requirePar`, datos JAR, y `allowsResponseType`, `grantableScopes`, `m2mScopes`; `allowedGrant("mfa")` sigue a `password`.
- `TrustedClientPolicy` (nuevo, puro) construye la política desde `TrustedClient`; las listas se leen como JSON o separadas por espacios/comas.
- `IssueTokenUsecase` comprueba el grant antes de code/refresh; `client_credentials` pasa por su granter y luego por `m2mScopes`. `JwtTokenBuilder` solo emite refresh si el cliente tiene el grant; `TokenIssuer` registra un JTI de relleno para no romper la sesión.
- `AuthorizeHtml.rejectRequest` valida `response_type` y audiencias antes del login.
- **Cambios de contrato:** clientes sin `grantTypesJson` pierden `password`, `client_credentials` y `device_code`; clientes sin `allowAllScopes` solo obtienen scopes OIDC en flujos de usuario; `client_credentials` exige `allowedScopesM2m`; authorize exige `response_type`.
- **Pruebas:** `TrustedClientPolicyUnitTest`, `ClientDetailsUnitTest`, `ClientPolicyFlowTest` (HTTP: grant no registrado, sin refresh, scopes de usuario y M2M, response types).
