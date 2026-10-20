# 0.8 — Verificación de bearer en APIs

- **Fase:** 0 · **Esfuerzo:** S–M · **Estado:** pendiente.
- **Implementación:** verificar `iss`, propósito/tipo y revocación además de firma; no aceptar ID, refresh, MFA ni challenge como bearer.
- **Cierre:** cada tipo incorrecto y cada JTI revocado es rechazado aunque su firma sea válida.
- **Dependencias:** [0.15](issue-0.15-audiencia-tenant-revocacion-bearer.md) completa audiencia y tenant.
- **Fuente:** [§5, 0.8](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
