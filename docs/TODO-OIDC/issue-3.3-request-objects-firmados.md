# 3.3 — Request objects firmados

- **Fase:** 3 (según necesidad) · **Estado:** pendiente · **Referencia:** RFC 9101.
- **Implementación:** validar `request`/`request_uri` externos con JWKS, `iss`/`aud`/`exp`/`nbf`/`iat`/`jti` y anti-replay; resolver conflictos de parámetros. Para `jwks_uri` remoto, usar un fetcher de infraestructura con HTTPS, bloqueo de redes privadas/loopback, límites de tiempo/tamaño y caché por cliente; reutilizarlo en [3.2](issue-3.2-autenticacion-jwt-cliente.md) y [3.4](issue-3.4-federacion-oidc-generica.md).
- **Cierre:** objeto válido aceptado y objeto expirado, de otro cliente, mal firmado o reutilizado rechazado; discovery coherente.
- **Fuente:** [§5, 3.3](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
