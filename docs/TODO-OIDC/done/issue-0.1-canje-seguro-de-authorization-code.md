# 0.1 — Canje seguro de authorization code

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** hecha (23-10-2026); queda la prueba de concurrencia contra MariaDB.
- **Implementación:** autenticar al cliente confidencial en `/token`; exigir `client_id`, tenant y `redirect_uri` originales. Consumir el code atómicamente y revocar tokens emitidos si se detecta reutilización.
- **Cierre:** pruebas HTTP + persistencia de canje válido, cliente/tenant/redirect ajenos y dos canjes concurrentes (un solo vencedor).
- **Dependencias:** coordinar con [0.9](issue-0.9-autenticacion-cliente-unificada.md) y [0.5](../issue-0.5-aislamiento-clientes-tenant.md).
- **Fuente:** [§5, 0.1](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Implementación Java (23-10-2026)

- `TokenController::exchangeCode` autentica al cliente antes de mirar el code. Con secreto (Basic o `client_secret`) debe ser confidencial y el secreto válido; sin secreto, el cliente debe permitir acceso público (`ClientStoreGateway::loadPublicById`, nuevo). Si Basic y cuerpo discrepan, `invalid_client` (401).
- `TemporalKeysGateway::consumeTemporalAuthCode(code, validator)` sustituye a `retrieveTemporalAuthCode`. El validador (`TokenController::codeMatches`) exige el mismo cliente, el tenant de la ruta, la `redirect_uri` original y PKCE S256 con verifier válido. Sin challenge, solo un confidencial autenticado y sin `code_verifier` (bloquea el *downgrade*, RFC 9700). Una petición rechazada **no gasta** el code. El borrado solo cuenta si afecta a una fila, así que de dos canjes simultáneos solo uno obtiene tokens; antes se leía y borraba sin comprobarlo.
- Errores OAuth (`invalid_client` 401 con `WWW-Authenticate`, `invalid_grant` 400) y `Cache-Control: no-store` en éxito y error (adelanta [1.4](issue-1.4-errores-cache-token.md) para code y refresh).
- **Pendiente:** revocar los tokens del primer canje si se intenta un segundo (como PHP, requiere guardar los codes usados) y la prueba concurrente sobre MariaDB ([V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md)).
- **Pruebas:** `TokenGrantSecurityTest` (redirect ajena sin gastar el code, secreto erróneo, otro cliente, público sin PKCE, público con PKCE, otro tenant) y `LoginFlowTest`, `PkceFlowTest`, `AuthCodeExchangeTest` adaptados a 400 `invalid_grant`.
- **Actualización (24-10-2026, [1.16](issue-1.16-emision-canje-fuera-tokencontroller.md)):** la lógica vive ahora en `AuthorizationCodeGrant` (`codeMatches` incluido) y la autenticación del cliente en `ClientAuthenticator` con el método registrado ([0.9](issue-0.9-autenticacion-cliente-unificada.md)). La emisión usa el cliente autenticado en el canje, no el guardado en el code.
