# 2.3 — Completar política de PAR

- **Fase:** 2 · **Esfuerzo:** S · **Estado:** hecha (25-10-2026), [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md).
- **Implementación:** aceptar clientes públicos, validar redirect y solicitud en el push y permitir PAR obligatorio por cliente.
- **Cierre:** solicitudes inválidas rechazadas en `/par`, clientes públicos admitidos según política y discovery alineado con exigencia real.
- **Dependencias:** [0.14](issue-0.14-par-vinculado-un-solo-uso.md).
- **Fuente:** [§5, 2.3](../CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).

## Qué hace (funcional)

- El push admite clientes públicos (`client_id` solo) y confidenciales con su método registrado.
- El push se valida como authorize: redirect registrado, `response_type` del cliente, audiencias y PKCE. Una solicitud inválida nunca recibe `request_uri` (400 con el error OAuth).
- PAR obligatorio por cliente (`require-pushed-authorization-requests`): authorize directo es `invalid_request`.
- Discovery anuncia `require_pushed_authorization_requests: false`: la obligación es por cliente, nunca de todo el tenant.

## Implementación Java

- `PushAuthorizationUsecase`, `ClientDetails.requirePar`, `AuthorizationRequestPolicy`, `ParContributor`.
- **Pruebas:** `AuthorizeRequestFlowTest` (público admitido, redirect ajeno, `plain`, sin PKCE, `client_id` ajeno, cliente con PAR obligatorio).
