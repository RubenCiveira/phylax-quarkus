# 4.9 — Session Management iframe

- **Fase:** 4 · **Estado:** implementada y verificada (27-10-2026).
- **Implementación:** `check_session_iframe` operativo, mensajes vinculados al origen registrado del RP y `session_state` derivado del `sid` público, no de la cookie. La comprobación se hace en `/session-check` sin exponer el SID.
- **Cierre:** discovery anuncia la ruta real; cambia tras logout y permanece estable tras rotar la cookie.
- **Dependencias:** [1.14](issue-1.14-hint-sid-logout.md).
- **Pruebas:** `SessionStateFlowTest`, `SessionLogoutMariaDbTest`.
- **Plan PHP:** [062-session-management-iframe.md](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/pospuestos/062-session-management-iframe.md) (pospuesta allí; implementada aquí).
