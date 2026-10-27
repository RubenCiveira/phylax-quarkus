# 1.2 — ID token básico conforme

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** hecha (25-10-2026), [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md).
- **Implementación:** ID token solo para scope `openid`, `aud` del cliente, TTL corto propio, `sid` correlacionado con sesión y `amr` como array; omitir `nonce` ausente.
- **Cierre:** assertions sobre emisión/no emisión, audiencia, expiración, `sid`, `amr` y ausencia de `nonce: ""`.
- **Dependencias:** [1.14](issue-1.14-hint-sid-logout.md) define la representación pública del `sid`.
- **Fuente:** [§5, 1.2](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).

## Qué hace (funcional)

- El ID token solo se emite si el scope concedido incluye `openid` (token endpoint e híbrido).
- `aud` es solo el cliente y `azp` el cliente; caduca con su propio TTL (`oauth.jwt.expiration.id-token`, 5 min por defecto), no con el del refresh.
- `sid`: identificador público y aleatorio de la sesión SSO, distinto de la cookie (que rota en cada autorización); es estable mientras vive la sesión y viaja en ID token, refresh token y logout token. Las sesiones previas reciben uno la primera vez que se leen.
- `amr` es un array con los métodos reales (`pwd`; `pwd otp mfa` tras el segundo factor); `nonce` solo si la petición lo trajo.
- Se retiran del ID token `roles`, `groups`, `grant_type`, `typ`, `s_hash` y `session_state`.

## Implementación Java

- `ClaimsMapper.idToken` y `JwtTokenBuilder` (`buildToken`, `buildIdToken`). El `sid` vive en `AuthenticationData` y se guarda en `_oauth_session.auth_data`, que sobrevive a la rotación de la cookie: la [columna generada](cambios-modelo-J02-J05.md) no se rellena aún y J-07 podrá usarla tras poblarla.
- El logout envía el `sid` público en el back-channel, nunca la cookie.
- **Pruebas:** `IdTokenClaimsFlowTest` (HTTP), `ClaimsMapperUnitTest`.
