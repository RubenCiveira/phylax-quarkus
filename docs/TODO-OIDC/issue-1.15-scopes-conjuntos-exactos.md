# 1.15 — Scopes como conjuntos exactos

- **Fase:** 1 · **Esfuerzo:** S–M · **Estado:** pendiente.
- **Implementación:** normalizar scopes a valores separados por espacios y comparar por igualdad en authorize, code, refresh, client_credentials, claims y UserInfo; conservar el conjunto concedido y solo permitir reducciones en refresh.
- **Cierre:** `myprofile` no concede `profile` ni `name`, `myemail` no concede `email`; scope no concedido se rechaza o reduce explícitamente y ninguna respuesta lo presenta como concedido.
- **Plan PHP:** [064-respuestas-token-y-scopes.md](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/064-respuestas-token-y-scopes.md). **Java:** complementar [0.4](issue-0.4-configuracion-por-cliente.md), [0.2](done/issue-0.2-refresh-cliente-sesion.md) y [1.4](issue-1.4-errores-cache-token.md).
