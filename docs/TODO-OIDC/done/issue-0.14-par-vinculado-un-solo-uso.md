# 0.14 — PAR vinculado y de un solo uso

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** hecha (25-10-2026), [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md).
- **Implementación:** comprobar que `client_id` del push coincide con el autenticado y consumir `request_uri` solo una vez; aceptar solo `client_id` exterior igual al almacenado, sin permitir que query altere redirect, scope ni PKCE del PAR.
- **Cierre:** mismatch, parámetros en conflicto y segundo uso rechazados; una solicitud válida conserva sus valores.
- **Dependencias:** [0.11](issue-0.11-state-seguro-secreto-fuera-par.md); [2.3](issue-2.3-politica-par.md) completa política de PAR.
- **Fuente:** [§5, 0.14](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Qué hace (funcional)

- **Push:** el `client_id` enviado debe ser el cliente autenticado (si falta, se toma el autenticado); un `request_uri` dentro del push es `invalid_request`.
- **Authorize con `request_uri`:** solo cuentan los parámetros guardados; de la query solo se leen `client_id` (debe coincidir, si no `invalid_request`) y `request_uri`. Una query que intente cambiar redirect, scope o PKCE no tiene efecto.
- **Un solo uso:** la primera petición consume el `request_uri` de forma atómica y entrega al navegador la cookie `PAR_BINDING` (digest del URI con el *pepper*). Los formularios del login vuelven a enviar el mismo URL: durante `oauth.par.flow-ttl` (10 min) solo ese navegador puede seguir usándolo. Otro navegador, o una cookie falsa, recibe `invalid_request_uri`. Antes, la primera lectura lo marcaba usado y el propio POST del login fallaba.

## Implementación Java

- `ResolveParRequestUsecase` (consumo + enlace), `ParRequestGateway.consume` (`UPDATE … WHERE used_at IS NULL AND expires_at > ?`), `ParBindingGateway`/`ParBindingDigestAdapter`, `AuthorizationRequestResolver` (precedencia de orígenes) y `OidcCookieManager.writeParBinding`.
- **Pruebas:** `AuthorizeRequestFlowTest` (query manipulada sin efecto, segundo uso sin enlace o con enlace falso, otro `client_id`, caducado).
