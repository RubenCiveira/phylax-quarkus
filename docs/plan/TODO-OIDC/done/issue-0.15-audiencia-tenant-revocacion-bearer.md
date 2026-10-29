# 0.15 — Audiencia, tenant y revocación de bearer

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** implementada y verificada (27-10-2026).
- **Decisión heredada (PHP EP-06):** semántica de audiencia **«al menos una»** (RFC 7519 §4.1.3); `aud` obligatorio siempre. Sin audiencias configuradas, la efectiva es el nombre de la aplicación. UserInfo e introspección no restringen audiencia porque son endpoints del propio emisor. Un token sin `jti` se rechaza.
- **Implementación:** verificar `aud`, tenant y JTI en APIs y UserInfo; invalidar caché positiva ante revocación.
- **Cierre:** access token de otro resource server/tenant o revocado no es aceptado.
- **Dependencias:** amplía [0.8](issue-0.8-verificacion-bearer-apis.md).
- **Pruebas:** `ApiBearerTenantFlowTest` (ruta, issuer, audiencia y revocación tras éxito), `BearerContractMariaDbTest` (tenant, JTI y revocación reales).
- **Fuente:** [§5, 0.15](../CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
