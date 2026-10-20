# 0.4 — Configuración efectiva por cliente

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** aplicar `grantTypesJson`, `responseTypesJson`, `allowAllScopes`, `allowedScopesM2m` y `tokenEndpointAuthMethod` de `TrustedClient`; quitar `password` por defecto.
- **Cierre:** tests con dos clientes de políticas distintas para grants, response types, scopes y método de autenticación.
- **Fuente:** [§5, 0.4](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
