# 0.7 — Clave pública y pre-sesión

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** pendiente.
- **Implementación:** quitar `withKeyId(privateKey)` de `signKeypass` y verificar pre-sesiones mediante `scope=challenge`.
- **Cierre:** `kid` solo identifica clave pública y los tokens de otros propósitos no pasan como pre-sesión.
- **Fuente:** [§5, 0.7](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
