# 2.4 — Redirects propios de logout

- **Fase:** 2 · **Esfuerzo:** S · **Estado:** hecha (25-10-2026), [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md); **requiere regenerar el modelo**.
- **Implementación:** separar `post_logout_redirect_uris` de `allowedRedirects` del login.
- **Cierre:** URI registrado para logout aceptado; URI registrado solo para login y destino externo rechazados.
- **Fuente:** [§5, 2.4](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).

## Qué hace (funcional)

- `post_logout_redirect_uri` solo se sigue si está registrada como **URI post-logout** del cliente (coincidencia exacta; loopback como en el login). Una URI registrada solo para el login, o externa, no se sigue: se muestra la página de sesión cerrada.
- Se conserva la query registrada y `state` se añade codificado (`?state=a+b%26c%3Dd`).

## Implementación Java

- `PostLogoutRedirectSqlStore` lee y escribe `access_trusted_client_allowed_post_logout_redirect`, la composición pedida en [cambios de modelo](../cambios-modelo-J02-J05.md) (§1). `ClientRetrieveAdapter` la carga en `ClientDetails.postLogoutRedirectUris`; `AuthorizeHtml.doLogout` la valida con `RedirectUriPolicy`.
- **Hasta regenerar el modelo la tabla no existe y ninguna URI post-logout es válida** (fallo cerrado, con log). Cambio de contrato: los clientes deben registrar sus URIs post-logout.
- **Pruebas:** `ClientPolicyFlowTest` (URI de logout aceptada con `state` codificado; de login y externa rechazadas).
