# 3.2 — Autenticación JWT de cliente

- **Fase:** 3 · **Estado:** implementada y verificada (27-10-2026), [J-10](../epicas/done/ep-J-10-aserciones-jwt-cliente-grant.md) · **Referencia:** RFC 7523, OIDC Core §9.
- **Decisión (heredada de PHP EP-02/EP-11):** solo `private_key_jwt`; sin `client_secret_jwt`.
- **Implementación:** `ClientCredentials` transporta `client_assertion`/`client_assertion_type` (el cliente puede nombrarse solo por `sub`); `ClientAuthenticator` carga el cliente con `loadForAssertion` (solo si registró `private_key_jwt`) y `ClientAssertionVerifier` comprueba firma con su JWKS y su `token_endpoint_auth_signing_alg` (RS256/ES256), `iss` = `sub` = cliente, `aud` = issuer o un endpoint suyo, `exp` ≤ 1 h, `jti` de un solo uso bajo el propósito `client_jwt` **compartido con JAR**, y rechaza una aserción con `response_type`. Vale para token, PAR, introspección y revocación. Un cliente `private_key_jwt` no se autentica con secreto ni como público.
- **Pendiente real:** DCR no acepta aún `jwks`/`jwks_uri` ni `private_key_jwt` (alta por administración); probar con un `jwks_uri` real.
- **Pruebas:** `ClientAssertionFlowTest` (válida una vez, firma ajena, otra audiencia u otro tenant, > 1 h, request object como aserción, secreto o métodos mezclados, revocación, discovery), `ClientAssertionVerifierUnitTest`, `ClientAuthenticatorUnitTest`.
- **Fuente:** [§5, 3.2](../CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
