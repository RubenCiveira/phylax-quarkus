# 1.7 — Política implicit/hybrid

- **Fase:** 1 · **Esfuerzo:** S–M · **Estado:** implementada y verificada (27-10-2026).
- **Decisión (heredada de PHP EP-08, 22-10-2026):** retirar implicit (OAuth 2.1). `/authorize` acepta solo `code` y los hybrid `code id_token`, `code token` y `code id_token token`; `token`, `id_token` e `id_token token` dan `unsupported_response_type`. `implicit` sigue en `grant_types_supported` porque RFC 7591 §2.1 lo exige para los hybrid, pero DCR rechaza registrar un `response_type` no soportado.
- **Implementación:** `response_type` tratado como conjunto (`id_token code` = `code id_token`) y comparado así con el registrado por el cliente; en hybrid, emitir solo los artefactos pedidos, con `c_hash`/`at_hash`, en `fragment` (nunca `query`) y `token_type=Bearer` si hay access token; `nonce` obligatorio si se pide `id_token` (véase [1.12](issue-1.12-nonce-ligado-autorizacion.md)).
- **Cierre:** combinaciones anunciadas probadas individualmente; los tipos implicit puros se rechazan y no se anuncian.
- **Plan PHP:** [EP-08](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/EP-08-autorizacion-respuesta.md), [033](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/033-hybrid-y-response-types.md).
- **Pruebas:** `AuthorizeResponseContractTest` (combinaciones hybrid, hashes y tipos implicit rechazados; discovery).
- **Fuente:** [§5, 1.7](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
