# 3.10 — Hints, ACR y localización

- **Fase:** 3 (según necesidad) · **Estado:** pendiente · **Referencia:** OIDC Core §3.1.2.1.
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
- **Fuente:** [§5, 3.10](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
