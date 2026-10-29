# 3.4 — Federación OIDC genérica

- **Fase:** 3 (según necesidad) · **Estado:** descartada (heredada de PHP, 22-10-2026).
- **Decisión:** no hay demanda de IdPs OIDC configurables por tenant; para SSO corporativo se usa SAML. Las redes sociales que se plantearon no implementan OIDC en el login web, así que un proveedor genérico no las cubriría. Se reabre ante un cliente que necesite conectar su propio IdP OIDC.
- **Consecuencia:** al revisar el login federado existente aparecieron fallos críticos, recogidos en [0.16](../done/issue-0.16-seguridad-login-federado.md).
- **Diseño previsto si se reabre:** RP genérico con discovery, JWKS, code+PKCE, `state`/`nonce`, verificación de ID token y vinculación explícita de cuentas por `(proveedor, sub)` (la de 0.16).
- **Plan PHP:** [051](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/051-federacion-oidc-generica.md).
- **Fuente:** [§5, 3.4](../CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
