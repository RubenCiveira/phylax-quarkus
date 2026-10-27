# 3.10 — Hints, ACR y localización

- **Fase:** 3 (según necesidad) · **Estado:** hecha (25-10-2026), [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md) · **Referencia:** OIDC Core §3.1.2.1.
- **Referencia PHP:** [067 Evaluar hints y step-up ACR en autorización](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/067-hints-y-acr-step-up.md) (hecha en EP-09) y [068 ui_locales](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/068-ui-locales.md). Decisiones que Java adopta:
  - **Sin cambios de modelo:** el ACR no se guarda, se deriva del `amr` de la sesión. No se usan `_oauth_session.acr`, `tenant-config.acr-values-supported` ni `trusted-client.default-acr-values`. **Java hoy hace lo contrario:** compara `acr` numérico de la sesión con `AuthRequest::getMinRequiredAcr` (enteros en `acr_values`).
  - **Dos niveles:** `SINGLE_FACTOR` (cualquier sesión autenticada) y `MULTI_FACTOR` (`amr` con `mfa`). Sus valores son configuración (`security.acr.single-factor` / `security.acr.multi-factor`, por defecto `urn:lugh:acr:1fa` / `urn:lugh:acr:mfa`). El primero es el que se emite y anuncia; los demás son alias aceptados; un valor no puede nombrar dos niveles.
  - **`acr_values` es voluntario:** se toma el primer valor conocido y los desconocidos se ignoran.
  - **Step-up sobre la misma sesión SSO:** solo se pide el TOTP, o su alta si el usuario no tiene. Se rechaza si esa sesión se cerró o es de otro usuario. Con `prompt=none`, `interaction_required`.
  - **El enlace mágico no se ofrece ni se acepta si se pide MFA.**
  - **Nunca se guarda ni reutiliza una sesión por debajo del nivel pedido.** El nivel es de la sesión, no del cliente.
  - **Tokens y discovery:** ID y access token llevan `acr`, `amr` y `auth_time`. Discovery publica `acr_values_supported` (valor emitido de cada nivel).
  - **APIs (RFC 9470):** el verificador bearer expone `acr`/`auth_time` y responde `401 insufficient_user_authentication` con `acr_values` y `max_age`.
  - **Otros parámetros:** `select_account` = `prompt=login`. `ui_locales`: primer idioma soportado y, si no, `Accept-Language`.
  - **Hints** (`login_hint`, `id_token_hint`): los cubre [2.7](issue-2.7-hints-authorize.md).
- **Implementación:** selector real para `select_account`, niveles `acr_values` documentados y `ui_locales`/`claims_locales` efectivos si se ofrecen.
- **Cierre:** step-up devuelve ACR alcanzado; selección e idioma siguen la solicitud y los valores no soportados no se anuncian.
- **Fuente:** [§5, 3.10](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).

## Resultado

- **ACR por niveles** con `AcrPolicy` (decisiones arriba): `acr_values` toma el primer valor conocido; una sesión de un factor ante `urn:lugh:acr:mfa` pasa por step-up (solo el TOTP sobre la misma sesión) o, con `prompt=none`, `interaction_required`. Un login nuevo que pide MFA exige el segundo factor, o su alta si el usuario no lo tiene. El enlace mágico no se ofrece ni se acepta cuando se pide MFA. Se retira el `acr` numérico (`0`/`1`/`2`).
- **Tokens:** `acr`, `amr` y `auth_time` (J-03, [1.13](issue-1.13-claims-proposito-token.md)). Discovery: `acr_values_supported` = valor emitido de cada nivel.
- `select_account` = `prompt=login` (ya lo era).
- `ui_locales`: primer idioma soportado (`oidc.ui-locales`, `en` por defecto; anunciado como `ui_locales_supported`), si no `Accept-Language`. `claims_locales` no se ofrece ni se anuncia.
- **Límites:** el step-up de una sesión cuyo usuario no tiene TOTP muestra el formulario de TOTP, no el alta; la respuesta `401 insufficient_user_authentication` de las APIs (RFC 9470) es de J-04, y debe emitir los nuevos valores URN (la consola web reenvía el `acr` que le devuelve el servidor).
- **Pruebas:** `AcrPolicyUnitTest`, `AuthorizationRequestPolicyUnitTest` (`ui_locales`), `AuthorizeRequestFlowTest` (`interaction_required` y step-up).
