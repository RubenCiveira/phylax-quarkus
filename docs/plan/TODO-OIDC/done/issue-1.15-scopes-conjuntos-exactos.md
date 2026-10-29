# 1.15 — Scopes como conjuntos exactos

- **Fase:** 1 · **Esfuerzo:** S–M · **Estado:** hecha (25-10-2026), [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md).
- **Implementación:** normalizar scopes a valores separados por espacios y comparar por igualdad en authorize, code, refresh, client_credentials, claims y UserInfo; conservar el conjunto concedido y solo permitir reducciones en refresh.
- **Cierre:** `myprofile` no concede `profile` ni `name`, `myemail` no concede `email`; scope no concedido se rechaza o reduce explícitamente y ninguna respuesta lo presenta como concedido.
- **Plan PHP:** [064-respuestas-token-y-scopes.md](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/064-respuestas-token-y-scopes.md). **Java:** complementar [0.4](issue-0.4-configuracion-por-cliente.md), [0.2](issue-0.2-refresh-cliente-sesion.md) y [1.4](issue-1.4-errores-cache-token.md).

## Resultado

- Los scopes se tokenizan por espacios y se comparan por igualdad en todas partes: concesión al cliente (`ClientDetails.grantableScopes`, `m2mScopes`), refresh (solo reduce, con `containsAll`), UserInfo y consentimiento (`List.contains`). `myprofile` no concede `profile`.
- La respuesta de `/token` incluye `scope` con lo concedido, así ninguna respuesta presenta como concedido algo que no lo está; un `client_credentials` sin ningún scope permitido es `invalid_scope`.
- **Pruebas:** `ClientDetailsUnitTest` (`myprofile`), `ClientPolicyFlowTest`, `IdTokenClaimsFlowTest` (`scope` de la respuesta).
