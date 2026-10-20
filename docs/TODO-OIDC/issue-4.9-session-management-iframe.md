# 4.9 — Session Management iframe

- **Fase:** 4 (solo con requisito) · **Estado:** pendiente.
- **Implementación:** si hay RP que lo necesite, implementar `check_session_iframe` conforme a OIDC Session Management con `session_state` vinculado a `sid` público, nunca valor de cookie; de lo contrario retirar stub y anuncio en [1.1](issue-1.1-discovery-honesto.md).
- **Cierre:** cambio/cierre de sesión detectable por el RP sin revelar cookie; metadata solo anuncia ruta funcional.
- **Dependencias:** [1.14](issue-1.14-hint-sid-logout.md).
- **Plan PHP:** [062-session-management-iframe.md](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/062-session-management-iframe.md).
