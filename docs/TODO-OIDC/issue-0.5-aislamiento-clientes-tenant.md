# 0.5 — Aislamiento de clientes por tenant

- **Fase:** 0 · **Esfuerzo:** M–L · **Estado:** pendiente.
- **Decisión heredada (PHP EP-03):** cliente con `tenant = null` = global, visible desde todos los tenants y gestionable solo por root; si un tenant tiene un cliente con el mismo `code` que uno global, gana el del tenant; un cliente de otro tenant nunca se devuelve. Los clientes de DCR nacen con el tenant de la ruta. Verificar sobre el esquema Java si hace falta migración.
- **Implementación:** decidir entre cliente de tenant o cliente global con asignación explícita; filtrar carga y back-channel conforme a la política.
- **Cierre:** un cliente asignado a A no opera en B; tests de carga, emisión y notificaciones entre dos tenants.
- **Fuente:** [§5, 0.5](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
