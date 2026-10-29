# 1.5 — UserInfo conforme

- **Fase:** 1 · **Esfuerzo:** S–M · **Estado:** implementada y verificada (27-10-2026).
- **Implementación:** POST además de GET; exigir `openid`, email real, `email_verified` y `address` solo si se conocen y el scope los permite; verificación de access tokens sin depender de sesión interactiva.
- **Cierre:** mismos claims y `sub` en GET/POST, scopes respetados, bearer inválido o revocado rechazado.
- **Dependencias:** [0.15](issue-0.15-audiencia-tenant-revocacion-bearer.md).
- **Pruebas:** `TokenIntrospectionTest`, `BearerContractMariaDbTest` (GET/POST, email persistido y revocación).
- **Fuente:** [§5, 1.5](../CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
