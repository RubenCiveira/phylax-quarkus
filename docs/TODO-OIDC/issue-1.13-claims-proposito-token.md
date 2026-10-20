# 1.13 — Claims según propósito del token

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** reflejar `auth_time`, `acr` y `amr` alcanzados; emitir `at_hash`/`c_hash` solo cuando correspondan y separar claims ID/access.
- **Cierre:** claims por flujo comprobados, sin contaminación entre tipos; `sub` de UserInfo coincide con ID token.
- **Dependencias:** [1.2](issue-1.2-id-token-conforme.md), [1.5](issue-1.5-userinfo-conforme.md) y [1.7](issue-1.7-politica-implicit-hybrid.md).
- **Fuente:** [§5, 1.13](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
