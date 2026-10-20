# 0.1 — Canje seguro de authorization code

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** autenticar al cliente confidencial en `/token`; exigir `client_id`, tenant y `redirect_uri` originales. Consumir el code atómicamente y revocar tokens emitidos si se detecta reutilización.
- **Cierre:** pruebas HTTP + persistencia de canje válido, cliente/tenant/redirect ajenos y dos canjes concurrentes (un solo vencedor).
- **Dependencias:** coordinar con [0.9](issue-0.9-autenticacion-cliente-unificada.md) y [0.5](issue-0.5-aislamiento-clientes-tenant.md).
- **Fuente:** [§5, 0.1](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
