# 1.9 — Introspección para todos los grants

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** implementada y verificada (27-10-2026).
- **Implementación:** verificar firma, issuer, tipo y revocación sin depender de pre-sesión interactiva.
- **Cierre:** password/client_credentials válidos activos y tokens expirados/revocados inactivos.
- **Pruebas:** `BearerContractMariaDbTest` (password/client_credentials activos, otros propósitos y revocación), `TokenIntrospectionTest`.
- **Fuente:** [§5, 1.9](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
