# 3.8 — Token exchange

- **Fase:** 3 · **Estado:** implementada y verificada (27-10-2026), [J-08](../epicas/done/ep-J-08-login-consentimiento-producto.md) · **Referencia:** RFC 8693.
- **Política:** cliente confidencial con el grant registrado; `subject_token` access token de este tenant **emitido para ese cliente** (su id en `aud`); el actor es siempre el cliente autenticado (`actor_token` rechazado) y queda en `act`, anidado si el token ya venía de un intercambio; scope solo reducible (`invalid_scope`); audiencias/`resource` registradas para el cliente (`invalid_target`), por defecto el propio cliente; solo access tokens de entrada y salida; sin refresh ni ID token; sesión propia revocable.
- **Implementación:** `TokenExchangeGrant`, `JwtTokenBuilder.buildExchangedToken`, `TokenIssuer.issueExchanged`; anunciado en `grant_types_supported`. Sustituye al `delegated` propietario ([2.5](issue-2.5-grants-inalcanzables-codigo-muerto.md)).
- **Pruebas:** `TokenExchangeFlowTest`.
- **Fuente:** [§5, 3.8](../CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
