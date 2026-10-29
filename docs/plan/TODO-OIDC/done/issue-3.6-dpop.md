# 3.6 — DPoP

- **Fase:** 3 · **Estado:** implementada y verificada (27-10-2026), [J-11](../epicas/done/ep-J-11-tokens-ligados-clave-certificado.md) · **Referencia:** RFC 9449.
- **Implementación:** `DpopProofVerifier` (jose4j): `typ dpop+jwt`, RS256/ES256/PS256, `jwk` público, firma, `htm`/`htu`, `iat` ±60 s, `jti` único por clave y `ath` con access token. `TokenController` verifica la cabecera `DPoP` y `DpopBinding` liga access y refresh (`cnf.jkt`, `token_type: DPoP`); un cliente con `dpop_bound_access_tokens` necesita prueba; un refresh ligado exige la misma clave. En consumo, `AccessTokenPolicy` rechaza un token ligado presentado como Bearer (API y UserInfo); UserInfo acepta el esquema `DPoP` con prueba; la introspección devuelve `cnf` y `token_type`. Discovery: `dpop_signing_alg_values_supported`.
- **Pendiente real:** `dpop_jkt` en authorize y `DPoP-Nonce` no implementados (opcionales); los resource servers externos deben validar la prueba ellos mismos.
- **Pruebas:** `DpopFlowTest` (vínculo, replay, otra URL/método, prueba obligatoria, Bearer rechazado, otra clave, refresh, introspección).
- **Fuente:** [§5, 3.6](../CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
