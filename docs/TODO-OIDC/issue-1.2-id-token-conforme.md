# 1.2 — ID token básico conforme

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** ID token solo para scope `openid`, `aud` del cliente, TTL corto propio, `sid` correlacionado con sesión y `amr` como array; omitir `nonce` ausente.
- **Cierre:** assertions sobre emisión/no emisión, audiencia, expiración, `sid`, `amr` y ausencia de `nonce: ""`.
- **Dependencias:** [1.14](issue-1.14-hint-sid-logout.md) define la representación pública del `sid`.
- **Fuente:** [§5, 1.2](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
