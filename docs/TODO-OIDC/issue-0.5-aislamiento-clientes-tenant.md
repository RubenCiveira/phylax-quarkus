# 0.5 — Aislamiento de clientes por tenant

- **Fase:** 0 · **Esfuerzo:** M–L · **Estado:** pendiente.
- **Implementación:** decidir entre cliente de tenant o cliente global con asignación explícita; filtrar carga y back-channel conforme a la política.
- **Cierre:** un cliente asignado a A no opera en B; tests de carga, emisión y notificaciones entre dos tenants.
- **Fuente:** [§5, 0.5](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
