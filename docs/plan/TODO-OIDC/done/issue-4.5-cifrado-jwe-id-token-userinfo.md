# 4.5 — Cifrado JWE para ID token y UserInfo

- **Fase:** 4 · **Estado:** implementada y verificada (27-10-2026), [J-12](../epicas/done/ep-J-12-algoritmos-firma-cifrado.md).
- **Implementación:** `JweEncryptionAdapter` (jose4j) cifra tras firmar con la primera clave RSA `enc` del JWKS del cliente (inline o `jwks_uri` vía `RemoteJwksFetcher`): RSA-OAEP/RSA-OAEP-256 y A128CBC-HS256 (por defecto), A256CBC-HS512, A128GCM, A256GCM. ID token del endpoint de token y del front-channel (`id_token_encrypted_response_*`) y UserInfo (`userinfo_encrypted_response_*`, firmado o JSON). Sin clave o con algoritmo no soportado no se emite nada en claro.
- **Pruebas:** `JoseResponsesFlowTest` (el RP descifra con su clave privada y verifica la firma; cliente sin clave).
- **Fuente:** [§5, fase 4](../CAPACIDADES_OIDC.md#fase-4--perfil-alto-fapi-20-solo-si-hay-requisito).
