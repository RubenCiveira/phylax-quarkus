# 0.8 — Verificación de bearer en APIs

- **Fase:** 0 · **Esfuerzo:** S–M · **Estado:** implementada y verificada (27-10-2026).
- **Decisión heredada (PHP EP-06):** un único verificador de access token (firma con la clave del tenant, `typ` `at+jwt`, `iss` canónico, `aud`, `tenant`, revocación por `jti`) que usan el filtro bearer, UserInfo e introspección. La caché solo guarda claims ya verificados (máx. 300 s) y `exp`, `iss`, `aud`, `tenant` y revocación se evalúan en cada petición.
- **Implementación:** verificar `iss`, propósito/tipo y revocación además de firma; no aceptar ID, refresh, MFA ni challenge como bearer.
- **Cierre:** cada tipo incorrecto y cada JTI revocado es rechazado aunque su firma sea válida.
- **Dependencias:** [0.15](issue-0.15-audiencia-tenant-revocacion-bearer.md) completa audiencia y tenant.
- **Pruebas:** `ApiBearerTenantFlowTest`, `BearerContractMariaDbTest`, `TokenJwtCallerPrincipalFactoryUnitTest`.
- **Fuente:** [§5, 0.8](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
