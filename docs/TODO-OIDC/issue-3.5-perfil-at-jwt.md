# 3.5 — Perfil at+jwt

- **Fase:** 3 (según necesidad) · **Estado:** pendiente · **Referencia:** RFC 9068.
- **Implementación:** emitir `typ: at+jwt` en cabecera y claims de access token según perfil.
- **Cierre:** resource server acepta access token de audiencia propia y rechaza propósito/audiencia incorrectos.
- **Fuente:** [§5, 3.5](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
