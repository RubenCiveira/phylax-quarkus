# V.3 — Conformidad de DCR

- **Fase:** validación condicional · **Estado:** en curso (27-10-2026): metadatos de extensión y pruebas HTTP + MariaDB hechos; falta *Dynamic OP*, bloqueado por una decisión (ver abajo).
- **Implementación:** tras exponer DCR, ejecutar perfil *Dynamic OP* y probar credenciales de registro caducadas/agotadas, metadatos inválidos y aislamiento por tenant.
- **Cierre:** pruebas HTTP + persistencia y suite sin fallos relevantes para lo anunciado.
- **Dependencias:** [2.1](done/issue-2.1-registro-dinamico-clientes.md), [V.2](issue-V.2-openid-conformance-suite.md) (arnés de la suite).
- **Fuente:** [§5, validación](../../src/docs/oidc/CAPACIDADES_OIDC.md#validación).

## Qué hace (funcional, 27-10-2026)

DCR acepta, además del núcleo de 2.1, los metadatos que el servidor ya aplica en ejecución, validados contra **las mismas constantes que publica el discovery**:

| Metadato | Regla | Lo usa |
|---|---|---|
| `token_endpoint_auth_method=private_key_jwt` | exige `jwks` o `jwks_uri`; no recibe `client_secret` | 3.2 |
| `jwks` / `jwks_uri` | nunca los dos (RFC 7591 §2); `jwks` con `keys` no vacío de claves RSA/EC **públicas** (sin `d`, `p`, `q`, `dp`, `dq`, `qi`, `oth`, `k`); `jwks_uri` HTTPS | 3.2, 3.3, 4.5 |
| `token_endpoint_auth_signing_alg`, `request_object_signing_alg` | `JwtAlgorithms.CLIENT_SIGNING` (RS256, ES256); el segundo exige claves | 3.2, 3.3 |
| `id_token_signed_response_alg`, `userinfo_signed_response_alg`, `authorization_signed_response_alg` | `JwtAlgorithms.SERVER_SIGNING` (RS256) | 1.2, 3.11, 4.2 |
| `id_token_encrypted_response_alg/enc`, `userinfo_encrypted_response_alg/enc` | `ClientJwtEncryptionGateway.ALGORITHMS/ENCRYPTIONS`; `enc` exige `alg`; exige claves | 4.5 |
| `subject_type` | `public` (por defecto) o `pairwise` con `OIDC_PAIRWISE_SALT` y redirects en un único host | 4.4 |
| `backchannel_logout_session_required`, `frontchannel_logout_session_required`, `dpop_bound_access_tokens`, `require_pushed_authorization_requests` | booleanos | 0.6, 4.10, 3.6, 2.3 |
| `sector_identifier_uri` | **rechazado**: el servidor no descarga ni verifica el documento del sector (OIDC Registration §5) | — |
| `tls_client_certificate_bound_access_tokens=true` | **rechazado**: mTLS pospuesto (4.1) | — |

Todos se guardan en las columnas de `access_trusted_client` que lee `TrustedClientPolicy`, así que un cliente registrado por DCR se comporta igual que uno dado de alta por un administrador. Un error de metadatos devuelve `invalid_client_metadata` **sin gastar** uso del initial access token.

**Fallo corregido:** DCR **no funcionaba por HTTP**. El initial access token y el registration access token son opacos, y el mecanismo JWT de Quarkus (autenticación proactiva) los rechazaba con un 401 vacío antes de llegar al controlador; 2.1 solo se había probado a nivel de caso de uso. `RegistrationAwareJwtAuthMechanism` delega en `JWTAuthMechanism` salvo en `/connect` y `/connect/{clientId}`.

## Implementación Java

- Dominio: `JwtAlgorithms` (fuente única de algoritmos; `ClientAuthMethod`, `AuthorizationRequestPolicy`, `InformationController`, los contributors y `JwksSignatureAdapter` apuntan a ella), `DcrExtensionMetadata`, `DcrMetadataPolicy` ampliada, `DcrRegistrationRules` con `pairwiseSubjects` y `private_key_jwt` entre los métodos verificables.
- Aplicación: `RegisterClientUsecase` solo genera secreto para `client_secret_*`.
- Infraestructura: `DynamicClientAdapter` escribe y lee las columnas de extensión con una lista única (`EXTENSION_COLUMNS`); `ClientRegisterController` las lee y las devuelve; `RegistrationAwareJwtAuthMechanism`.
- **Sin cambios de modelo:** las columnas ya existían.

## Pruebas

| Prueba | Qué demuestra |
|---|---|
| `DcrMetadataPolicyUnitTest` (15) | Reglas de claves, algoritmos, cifrado, `subject_type` y booleanos |
| `RegisterClientUsecaseUnitTest` | `private_key_jwt` no recibe secreto |
| `DynamicClientRegistrationMariaDbTest.keysAlgorithmsAndFlags_…` | Las extensiones llegan a las columnas de ejecución, se leen y se reemplazan en un `PUT` |
| `DynamicClientHttpMariaDbTest` (HTTP) | Alta `private_key_jwt` con `jwks` (201, sin secreto, `no-store`) y lectura sin secreto; seis metadatos inválidos → 400 sin gastar usos; token de otro tenant, caducado o agotado → 401 `invalid_token` con `WWW-Authenticate`; el discovery anuncia lo que DCR acepta |

## Pendiente

- **Decisión de producto antes de *Dynamic OP*:** la suite registra clientes **sin** initial access token, y phylax lo exige siempre (decisión heredada de PHP EP-03, sin modo `open`). Opciones: (a) aceptar que *Dynamic OP* falla en el registro y documentarlo; (b) un modo `open` por tenant solo para entornos de conformidad; (c) comprobar si la versión de la suite usada admite un initial access token configurado. No se ha implementado ninguna.
- `sector_identifier_uri` verificado (descarga con la protección SSRF de `RemoteJwksFetcher`) si algún RP pairwise necesita varios hosts.
- `default_max_age`, `default_acr_values`, `require_auth_time`, `initiate_login_uri`, `request_uris` y `contacts` se ignoran (no se devuelven): el servidor no los aplica.
