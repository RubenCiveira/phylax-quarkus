# 0.12 — Logout POST y confirmación

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** implementada y verificada (27-10-2026).
- **Implementación:** leer parámetros del formulario POST, no solo query; pedir confirmación sin `id_token_hint`.
- **Cierre:** POST con cuerpo funciona y un logout sin hint no termina la sesión sin confirmación.
- **Pruebas:** `SessionManagementTest`, `SessionLogoutMariaDbTest` (GET confirmación, POST formulario y CSRF).
- **Fuente:** [§5, 0.12](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
