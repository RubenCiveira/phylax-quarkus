# J-07 — Sesión SSO y logout

**Prioridad:** alta · **Tareas:** [0.6](../../done/issue-0.6-logout-clientes-participantes.md), [0.12](../../done/issue-0.12-logout-post-confirmacion.md), [1.14](../../done/issue-1.14-hint-sid-logout.md), [4.9](../../done/issue-4.9-session-management-iframe.md) y [4.10](../../done/issue-4.10-front-channel-logout.md). **Depende de:** clientes/tenant J-02 y `sid`/`auth_time` J-03.

**Estado:** cerrada (27-10-2026), incluidos Session Management y front-channel. `SessionStateFlowTest` y `SessionLogoutMariaDbTest` verifican `sid` público, cookie rotada, origen de RP, clientes participantes, notificaciones y revocación selectiva.

## Implementación

Guardar clientes participantes en la sesión; back-channel únicamente a ellos, con `sid` público del ID token y `typ: logout+jwt`. Validar hint por firma/tipo/issuer/RP/sesión antes de terminar; POST lee formulario, GET sin hint requiere confirmación y CSRF, URI post-logout exacta separada del redirect de login (J-02). Conservar `max_age`, TTL SSO y step-up Java; probar `prompt=none` sin interacción. Iframe Session Management y front-channel solo si se requieren y se anuncian después de estar operativos.

**Aceptación:** petición inválida conserva la sesión; cierre válido revoca tokens de participantes y no notifica otro cliente o tenant; cookies no aparecen en ID/logout token; POST y confirmación protegidos.

**Referencia PHP:** EP-09 (031, 036, 067, 062), `done/021` (participantes). PHP aún publica el SID de cookie; Java debe evitar reproducirlo.
