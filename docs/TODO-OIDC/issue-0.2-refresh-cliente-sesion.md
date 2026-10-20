# 0.2 — Refresh ligado al cliente y a la sesión

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** autenticar al cliente, verificar que la sesión siga viva y limitar los scopes solicitados a los originales. Consumir el JTI de forma atómica antes de emitir el siguiente refresh; revocar la familia y sesión afectada ante replay. Registrar `fid` y expiración absoluta `fexp` para que la rotación no prolongue indefinidamente el grant; decidir y documentar qué grants sin sesión pueden recibir refresh y cómo se revocan.
- **Cierre:** refresh robado, sesión cerrada, scope/audiencia ampliados, familia vencida y dos canjes concurrentes rechazados según corresponda; un único vencedor y replay revoca la familia. No revocar tokens de otros usuarios o tenants.
- **Plan PHP:** `done/019-refresh-cliente-y-sesion.md` y `done/020-rotar-refresh-y-detectar-replay.md` sirven de contrato, no de implementación Java.
- **Dependencias:** [0.9](issue-0.9-autenticacion-cliente-unificada.md) y [0.5](issue-0.5-aislamiento-clientes-tenant.md).
- **Fuente:** [§5, 0.2](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
