# 0.3 — Audiencias autorizadas

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** pendiente.
- **Implementación:** limitar `audience` a las audiencias configuradas para cliente o tenant en authorize, password y device.
- **Cierre:** una audiencia propia se acepta y una ajena se rechaza en los tres flujos; no se calculan roles para la ajena.
- **Fuente:** [§5, 0.3](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
