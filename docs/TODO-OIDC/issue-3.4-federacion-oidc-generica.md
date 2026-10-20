# 3.4 — Federación OIDC genérica

- **Fase:** 3 (según necesidad) · **Estado:** pendiente.
- **Implementación:** RP genérico de IdP externo con discovery, JWKS, code+PKCE, `state`/`nonce`, verificación de ID token, claims y política explícita de vinculación de cuentas (nunca enlazar automáticamente por email no verificado); añadir Microsoft/Apple/GitHub según producto.
- **Cierre:** login válido y rechazos de issuer, audiencia, nonce y firma incorrectos; vinculación de cuenta autorizada.
- **Fuente:** [§5, 3.4](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
