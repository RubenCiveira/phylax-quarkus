# 0.14 — PAR vinculado y de un solo uso

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** comprobar que `client_id` del push coincide con el autenticado y consumir `request_uri` solo una vez; aceptar solo `client_id` exterior igual al almacenado, sin permitir que query altere redirect, scope ni PKCE del PAR.
- **Cierre:** mismatch, parámetros en conflicto y segundo uso rechazados; una solicitud válida conserva sus valores.
- **Dependencias:** [0.11](issue-0.11-state-seguro-secreto-fuera-par.md); [2.3](issue-2.3-politica-par.md) completa política de PAR.
- **Fuente:** [§5, 0.14](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
