# 4.7 — Resource indicators

- **Fase:** 4 · **Estado:** implementada y verificada (27-10-2026), [J-13](../epicas/done/ep-J-13-solicitudes-claims-avanzados.md) · **Referencia:** RFC 8707.
- **Implementación:** `resource` (repetible; PAR y JAR lo guardan separado por espacios) debe ser URI absoluta sin fragmento y estar en `allowedAudiences` del cliente (`invalid_target`); se suma a las audiencias. En el token endpoint: password, device y client_credentials lo aceptan; el canje de code y el refresh solo **reducen** (`invalid_target` sin gastar el code) y el refresh rotado conserva las audiencias del grant (`grantAudiences`). El consumo ya verifica `aud` ([0.15](issue-0.15-audiencia-tenant-revocacion-bearer.md)).
- **Pruebas:** `ResourceIndicatorFlowTest`.
- **Fuente:** [§5, fase 4](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-4--perfil-alto-fapi-20-solo-si-hay-requisito).
