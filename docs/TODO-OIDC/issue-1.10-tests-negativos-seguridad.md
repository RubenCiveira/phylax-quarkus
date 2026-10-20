# 1.10 — Tests negativos de seguridad

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** pruebas HTTP + persistencia de code ajeno/doble canje, refresh robado/concurrente, audiencia ajena y redirect no registrado.
- **Cierre:** los casos negativos fallan correctamente y los positivos continúan funcionando, incluidos los escenarios concurrentes.
- **Dependencias:** [0.1](issue-0.1-canje-seguro-de-authorization-code.md), [0.2](issue-0.2-refresh-cliente-sesion.md), [0.3](issue-0.3-audiencias-autorizadas.md) y [0.10](issue-0.10-redirect-uri-estricta.md).
- **Fuente:** [§5, 1.10](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
