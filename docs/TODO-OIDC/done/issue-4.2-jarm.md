# 4.2 — JARM

- **Fase:** 4 · **Estado:** implementada y verificada (27-10-2026), [J-12](../epicas/done/ep-J-12-algoritmos-firma-cifrado.md).
- **Implementación:** `response_mode` `jwt`, `query.jwt`, `fragment.jwt` y `form_post.jwt` (`jwt` = query para code, fragment con tokens; `query.jwt` con tokens rechazado). Éxito y error se entregan como un único parámetro `response`: JWT firmado RS256 con `iss`, `aud` = cliente, `exp` 10 min y los parámetros de la respuesta. Un `authorization_signed_response_alg` distinto de RS256 se rechaza. Sin cifrado JARM (el modelo no tiene `authorization_encrypted_response_*`).
- **Pruebas:** `JoseResponsesFlowTest` (code en `query.jwt`, error en `fragment.jwt`, discovery).
- **Fuente:** [§5, fase 4](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-4--perfil-alto-fapi-20-solo-si-hay-requisito).
