# 0.9 — Autenticación de cliente unificada

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** servicio común Basic/post para token, device, PAR, introspect y revocation; comparación temporalmente segura.
- **Cierre:** métodos permitidos aplicados por cliente, secreto incorrecto rechazado sin 500 y respuestas OAuth apropiadas en todos los endpoints.
- **Fuente:** [§5, 0.9](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
