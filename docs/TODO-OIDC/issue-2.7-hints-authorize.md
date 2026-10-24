# 2.7 — Hints en authorize

- **Fase:** 2 · **Esfuerzo:** S · **Estado:** pendiente.
- **Implementación:** prerrellenar con `login_hint` y validar/aplicar `id_token_hint` en authorize, también con `prompt=none`.
- **Cierre:** hint válido afecta el flujo y hint inválido no autentica ni cambia de usuario.
- **Referencia PHP ([067](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/067-hints-y-acr-step-up.md)):** `login_hint` (máx. 256 caracteres) solo rellena, **escapado**, el usuario del formulario (hoy Java lo concatena sin escapar en `LoginStep`: XSS reflejado). `id_token_hint` debe ser un ID token de este tenant para el cliente (firma, emisor y audiencia; se acepta caducado). Si no lo es, `invalid_request`. Si nombra a otro usuario, no se reutiliza la sesión (login, o `login_required` con `prompt=none`). Nunca autentica por sí mismo. ACR y step-up en [3.10](issue-3.10-hints-acr-localizacion.md).
- **Fuente:** [§5, 2.7](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).
