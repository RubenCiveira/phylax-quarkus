# 3.2 — Autenticación JWT de cliente

- **Fase:** 3 (según necesidad) · **Estado:** pendiente · **Referencia:** RFC 7523, OIDC Core §9.
- **Implementación:** admitir `private_key_jwt`/`client_secret_jwt` con claves registradas y verificación de firma, issuer, subject, audience, expiración y replay.
- **Cierre:** assertion válida aceptada; firma, audiencia y JTI ajenos rechazados; metadata ajustada.
- **Fuente:** [§5, 3.2](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
