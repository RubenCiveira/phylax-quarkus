# 0.3 — Audiencias autorizadas

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** hecha (24-10-2026), adelantada desde [J-02](../epicas/ep-J-02-clientes-tenant-secretos.md) para cerrar [1.10](issue-1.10-tests-negativos-seguridad.md).
- **Implementación:** limitar `audience` a las audiencias configuradas para cliente o tenant en authorize, password y device.
- **Cierre:** una audiencia propia se acepta y una ajena se rechaza en los tres flujos; no se calculan roles para la ajena.
- **Fuente:** [§5, 0.3](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Qué hace (funcional)

Un cliente solo puede pedir tokens para **sí mismo** o para los servidores de recursos que tiene registrados en `allowedAudiences` (administración del cliente de confianza). Pedir cualquier otro se rechaza con `invalid_target` (RFC 8707) **antes** de comprobar las credenciales del usuario, así que nunca se calculan roles para la audiencia ajena.

| Flujo | Dónde llega `audience` | Audiencia ajena |
|---|---|---|
| `/authorize` | query (separada por comas) | redirección a la `redirect_uri` con `error=invalid_target`, sin mostrar login |
| `/token` con `password`, `client_credentials`, `mfa`, `device_code` | query o cuerpo (comas o espacios) | 400 `invalid_target` |
| `/device` | cuerpo | 400 `invalid_target` |
| `/token` con `authorization_code` | se ignora: valen las aprobadas en authorize | — |
| `/token` con `refresh_token` | se ignora: valen las del refresh original | — |

Un code o un refresh nunca amplían la audiencia con la que nacieron.

## Implementación Java (24-10-2026)

- `ClientDetails.allowedAudiences` y `allowsAudiences(list)`; `ClientRetrieveAdapter` los carga de `TrustedClient.allowedAudiences`.
- Comprobación en `AuthorizeHtml` (GET y POST, antes de cualquier paso del login), en `IssueTokenUsecase` (junta query y cuerpo y propaga solo las validadas con `AuthRequest.withAudiences`) y en `DevicesAccessController`. Nuevo `OAuthTokenException.invalidTarget()`.
- **Cambio de contrato:** el `audience` del **cuerpo** de `/token` ya se tiene en cuenta (antes solo la query). Un cliente que enviaba una audiencia no registrada, y la veía ignorada, ahora recibe `invalid_target`: hay que registrarla en el cliente.
- **Decisión:** solo audiencias del cliente, como PHP (018); no hay audiencias por tenant.
- **Pruebas:** `ClientDetailsAudiencesUnitTest`; en `TokenGrantNegativeSecurityTest` (HTTP) password con audiencia registrada y ajena (sin llegar a validar credenciales), client_credentials, `/device` y authorize con audiencia ajena, y code/refresh que no amplían la audiencia.
