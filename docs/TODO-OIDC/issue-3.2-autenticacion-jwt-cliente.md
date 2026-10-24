# 3.2 — Autenticación JWT de cliente

- **Fase:** 3 (según necesidad) · **Estado:** pendiente · **Referencia:** RFC 7523, OIDC Core §9.
- **Decisión (heredada de PHP EP-02/EP-11):** solo `private_key_jwt`. Con secretos no recuperables ([2.9](issue-2.9-migracion-secretos-cifrado.md), opción A) el servidor no conoce el secreto en claro, así que `client_secret_jwt` no se implementa ni se anuncia.
- **Implementación:** verificador común de JWT firmados por el cliente (también lo usa JAR, [3.3](issue-3.3-request-objects-firmados.md)): algoritmo asimétrico (`RS256`, `ES256`, `PS256`) y el registrado si lo hay; JWKS en línea o `jwks_uri` con descarga segura; `iss` = `sub` = cliente; `aud` = issuer, token endpoint o endpoint llamado; `exp` ≤ 1 h; `jti` de un solo uso **compartido entre aserción y request object** (un mismo JWT no vale para los dos usos). Un cliente registrado con `private_key_jwt` no puede autenticarse con secreto ni como público. Un solo método por petición (RFC 6749 §2.3).
- **Cierre:** assertion válida aceptada en token, PAR, introspección y revocación; firma, audiencia, algoritmo, JTI repetido o reutilizado como JAR rechazados; discovery con `*_auth_signing_alg_values_supported` y sin `client_secret_jwt`.
- **Plan PHP:** [EP-11](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/EP-11-aserciones-jwt-cliente.md), [044](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/044-client-auth-jwt.md).
- **Fuente:** [§5, 3.2](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
