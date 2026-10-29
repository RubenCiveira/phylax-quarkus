# 4.10 — Front-channel logout

- **Fase:** 4 · **Estado:** implementada y verificada (27-10-2026).
- **Implementación:** tras cerrar sesión renderizar iframes exclusivamente de RP participantes con URI registrada; incluir `iss` y `sid` público si se requiere y redirigir al destino validado tras la notificación.
- **Cierre:** solo los RP participantes reciben aviso, otra cuenta/tenant no; metadata se publica cuando el flujo está operativo.
- **Dependencias:** [0.6](issue-0.6-logout-clientes-participantes.md), [1.14](issue-1.14-hint-sid-logout.md), [2.4](issue-2.4-redirects-logout.md).
- **Pruebas:** `SessionLogoutMariaDbTest` (iframes solo de participantes, parámetros `iss`/`sid` públicos y redirect validado).
- **Plan PHP:** [EP-09 sesión SSO y logout](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/EP-09-sesion-sso-y-logout.md).
