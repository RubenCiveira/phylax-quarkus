# 0.13 — Ruta de revocación no duplicada

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** pendiente.
- **Implementación:** separar `POST /revocation` RFC 7009 de la operación de pre-sesión.
- **Cierre:** ambas rutas llegan inequívocamente a su controlador con independencia de `Content-Type`.
- **Fuente:** [§5, 0.13](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
