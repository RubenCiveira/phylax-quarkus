# J-03 — Emisión y claims por propósito

**Prioridad:** alta · **Tareas:** [0.7](../issue-0.7-clave-publica-y-presesion.md), [1.2](../issue-1.2-id-token-conforme.md), [1.13](../issue-1.13-claims-proposito-token.md), [1.15](../issue-1.15-scopes-conjuntos-exactos.md), [2.6](../issue-2.6-generacion-claves-fuera-peticion.md), [3.5](../issue-3.5-perfil-at-jwt.md) (RFC 9068 según necesidad). **Depende de:** clientes J-02 y extracción J-01; **desbloquea:** J-04, J-06, J-07.

## Implementación

1. Mantener `ClaimsMapper`/`JwtTokenBuilder` como autoridad de emisión; caracterizar access, ID, refresh, MFA y challenge; quitar `withKeyId(privateKey)` y no generar RSA en la petición de firma/JWKS.
2. Definir `sid` público estable distinto de cookie, correlacionado con sesión, con transición para sesiones/tokens existentes. ID: solo con `openid`, `aud` RP, expiración propia, `auth_time` de autenticación y `amr`/`acr` sustentados por login real; hashes solo por flujo.
3. Scope tokenizado exactamente; access separado del ID. Perfil RFC 9068 (`typ: at+jwt`, `client_id`, `jti`, `aud`, etc.) si se adopta, con consumidores compatibles antes de activarlo.

**Aceptación:** ID token y logout token llevan el mismo `sid` no utilizable como cookie; `myprofile` no da claims de `profile`; tokens previos/durante/después de rotación verifican según política; ningún ID emitido sin `openid`.

**Referencia PHP:** EP-04 (`done/025`, `done/029`) y EP-05 (032, 038, 049, 064). El mapper Java ya existe; no copiar la extracción PHP literalmente.
