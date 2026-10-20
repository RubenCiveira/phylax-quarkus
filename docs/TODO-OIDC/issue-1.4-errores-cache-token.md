# 1.4 — Errores y caché de token

- **Fase:** 1 · **Esfuerzo:** S · **Estado:** pendiente.
- **Implementación:** devolver errores OAuth RFC 6749 §5.2 (`invalid_grant`, `invalid_client`, etc.), `WWW-Authenticate` cuando proceda y `Cache-Control: no-store` y `Pragma: no-cache` en éxito y error.
- **Cierre:** respuestas negativas y positivas de `/token` con códigos, cuerpo y cabeceras correctos; los scopes son elementos exactos ([1.15](issue-1.15-scopes-conjuntos-exactos.md)).
- **Fuente:** [§5, 1.4](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
