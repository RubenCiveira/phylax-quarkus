# 1.14 — Hint y SID seguros para logout

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** implementada y verificada (27-10-2026).
- **Implementación:** validar `id_token_hint` contra issuer, RP y sesión; generar `sid` público estable, diferente de la cookie, en ID y logout token.
- **Cierre:** correlación de `sid`, hint ajeno rechazado y logout/notificaciones/revocación limitados a participantes y tenant.
- **Dependencias:** [0.5](issue-0.5-aislamiento-clientes-tenant.md), [0.6](issue-0.6-logout-clientes-participantes.md) y [1.2](issue-1.2-id-token-conforme.md).
- **Pruebas:** `SessionLogoutMariaDbTest` (hint de RP/sesión ajenos, SID público y token logout), `IdTokenClaimsFlowTest`.
- **Fuente:** [§5, 1.14](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
