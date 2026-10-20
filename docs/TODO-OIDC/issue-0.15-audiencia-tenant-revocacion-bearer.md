# 0.15 — Audiencia, tenant y revocación de bearer

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** verificar `aud`, tenant y JTI en APIs y UserInfo; invalidar caché positiva ante revocación.
- **Cierre:** access token de otro resource server/tenant o revocado no es aceptado.
- **Dependencias:** amplía [0.8](issue-0.8-verificacion-bearer-apis.md).
- **Fuente:** [§5, 0.15](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
