# 0.6 — Logout solo para clientes participantes

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** registrar los `client_id` participantes en cada sesión y enviar back-channel logout únicamente a ellos.
- **Cierre:** un cliente no participante y otro tenant no reciben notificación; los participantes sí.
- **Dependencias:** [0.5](issue-0.5-aislamiento-clientes-tenant.md); coordinar `sid` con [1.14](issue-1.14-hint-sid-logout.md).
- **Fuente:** [§5, 0.6](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
