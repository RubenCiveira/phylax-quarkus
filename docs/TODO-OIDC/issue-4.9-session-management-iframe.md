# 4.9 — Session Management iframe

- **Fase:** 4 (solo con requisito) · **Estado:** pospuesta (heredada de PHP EP-09); la retirada del stub sigue pendiente.
- **Decisión:** no se implementa mientras ningún RP dependa de `check_session_iframe`. Lo que sí se hace ya es **retirar el stub y su anuncio** en [1.1](issue-1.1-discovery-honesto.md).
- **Si se reabre:** `check_session_iframe` conforme a OIDC Session Management, en ruta separada de `/check-session`, con `session_state` derivado del `sid` público, nunca de la cookie.
- **Cierre (retirada):** la ruta stub no existe y discovery no anuncia `check_session_iframe`.
- **Dependencias:** [1.14](issue-1.14-hint-sid-logout.md).
- **Plan PHP:** [062-session-management-iframe.md](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/pospuestos/062-session-management-iframe.md).
