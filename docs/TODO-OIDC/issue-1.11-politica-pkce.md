# 1.11 — Política PKCE

- **Fase:** 1 · **Esfuerzo:** S · **Estado:** pendiente.
- **Implementación:** admitir solo `S256`, exigir PKCE a públicos y decidir política para confidenciales.
- **Cierre:** ausencia de challenge, método `plain` y verifier incorrecto rechazados; confidenciales siguen la política declarada.
- **Fuente:** [§5, 1.11](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
