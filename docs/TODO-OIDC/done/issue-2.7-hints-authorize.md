# 2.7 — Hints en authorize

- **Fase:** 2 · **Esfuerzo:** S · **Estado:** hecha (25-10-2026), [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md).
- **Implementación:** prerrellenar con `login_hint` y validar/aplicar `id_token_hint` en authorize, también con `prompt=none`.
- **Cierre:** hint válido afecta el flujo y hint inválido no autentica ni cambia de usuario.
- **Referencia PHP ([067](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/067-hints-y-acr-step-up.md)):** `login_hint` (máx. 256 caracteres) solo rellena, **escapado**, el usuario del formulario (hoy Java lo concatena sin escapar en `LoginStep`: XSS reflejado). `id_token_hint` debe ser un ID token de este tenant para el cliente (firma, emisor y audiencia; se acepta caducado). Si no lo es, `invalid_request`. Si nombra a otro usuario, no se reutiliza la sesión (login, o `login_required` con `prompt=none`). Nunca autentica por sí mismo. ACR y step-up en [3.10](issue-3.10-hints-acr-localizacion.md).
- **Fuente:** [§5, 2.7](../CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).

## Qué hace (funcional)

- `login_hint` (máx. 256 caracteres) solo rellena el usuario, **escapado** (antes se concatenaba sin escapar: XSS reflejado).
- `id_token_hint` debe ser un ID token firmado por este servidor, del tenant, para el cliente (se acepta caducado); un access o refresh token no vale (llevan `scope`). Si no lo es: `invalid_request`. Si nombra a otro usuario, la sesión actual no se reutiliza (login, o `login_required` con `prompt=none`). Nunca autentica por sí mismo.

## Implementación Java

- `AuthRequest.loginHint`/`idTokenHint`, `LoginStep` (`escapeHtml4`), `AuthorizeHtml.withClient`/`idTokenHintSubject`, `TokenSigner.verifyTokenPayloadAcceptingExpired`.
- **Pruebas:** `AuthorizeRequestFlowTest` (hint escapado; hint del mismo usuario caducado reutiliza la sesión; otro usuario, basura y otro cliente rechazados).
