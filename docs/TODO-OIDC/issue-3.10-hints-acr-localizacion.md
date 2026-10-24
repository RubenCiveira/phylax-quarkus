# 3.10 — Hints, ACR y localización

- **Fase:** 3 (según necesidad) · **Estado:** pendiente · **Referencia:** OIDC Core §3.1.2.1.
- **Referencia PHP (067, hecha en EP-09):** `select_account` se trata como `prompt=login` (una sesión por navegador y tenant); `acr_values` se mapean con `tenant-config.acr-values-supported` y provocan step-up a MFA cuando la sesión no alcanza; `login_hint` solo precarga la cuenta. `ui_locales` (068): primer idioma soportado de `ui_locales` y, si no, de `Accept-Language`.
- **Implementación:** selector real para `select_account`, niveles `acr_values` documentados y `ui_locales`/`claims_locales` efectivos si se ofrecen.
- **Cierre:** step-up devuelve ACR alcanzado; selección e idioma siguen la solicitud y los valores no soportados no se anuncian.
- **Fuente:** [§5, 3.10](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
