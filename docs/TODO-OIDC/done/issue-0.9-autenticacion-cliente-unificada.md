# 0.9 — Autenticación de cliente unificada

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** hecha (24-10-2026).
- **Implementación:** servicio común Basic/post para token, device, PAR, introspect y revocation; comparación temporalmente segura.
- **Cierre:** métodos permitidos aplicados por cliente, secreto incorrecto rechazado sin 500 y respuestas OAuth apropiadas en todos los endpoints.
- **Fuente:** [§5, 0.9](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Qué hace (funcional)

Todo endpoint que exige identificar al cliente lo hace de la misma forma y con las mismas reglas. Un cliente se presenta con **un único** método:

| Método | Cómo llega | Cuándo vale |
|---|---|---|
| `client_secret_basic` | `Authorization: Basic base64(id:secreto)` | cliente confidencial |
| `client_secret_post` | `client_id` + `client_secret` en el cuerpo | cliente confidencial |
| `none` | solo `client_id` en el cuerpo | cliente con acceso público (`publicAllow` o método `none` registrado) |
| `private_key_jwt` | `client_assertion` | **todavía no**: se rechaza hasta [3.2](../issue-3.2-autenticacion-jwt-cliente.md) |

Se responde `invalid_client` (401, `WWW-Authenticate`, sin caché) cuando: se mezclan métodos (Basic y `client_secret` a la vez, RFC 6749 §2.3), el Basic está mal formado, Basic y `client_id` nombran clientes distintos, el secreto no coincide, el cliente no existe en el tenant o el método no es uno de los registrados. Nunca un 500.

**Qué acepta cada endpoint** (`ClientAuthPolicy`):

| Política | Endpoints / grants | Métodos admitidos |
|---|---|---|
| `REGISTERED_METHOD` | `/token` con `authorization_code` y `refresh_token` | exactamente el `tokenEndpointAuthMethod` registrado (Basic **o** post, no ambos) y `none` si el cliente permite acceso público |
| `SECRET` | `/token` con `password`, `client_credentials`, `mfa`; PAR, introspección, revocación | Basic o post; nunca `none` |
| `SECRET_OR_PUBLIC` | `/device` y `/token` con `device_code` | Basic, post o `none` |

Reglas que no dependen de la política:

- Un cliente registrado como `none` no puede autenticarse con secreto.
- Un cliente registrado como `private_key_jwt` no puede autenticarse con secreto ni como público.
- Un cliente que se autentica como público se trata como **no confidencial** (`protectedWithSecret = false`) en todo el flujo: no puede canjear un code sin PKCE ni usar `password`/`client_credentials`.
- Un cliente sin método declarado (solo dobles de prueba; en BD la columna es `NOT NULL`) admite Basic y post.

## Implementación Java (24-10-2026)

- **Dominio** (`features/oauth/client/domain`): `ClientCredentials` (lectura de cabecera y cuerpo, `method()`, `clientId()`, `secret()`; `toString` sin secreto), `ClientAuthMethod` y `ClientAuthPolicy`. `ClientDetails` incorpora `tokenEndpointAuthMethod`, que `ClientRetrieveAdapter` mapea desde `TrustedClient`.
- **Aplicación:** `ClientAuthenticator::authenticate(tenant, credentials, policy)`. El secreto lo verifica `ClientStoreGateway::loadPrivate` mediante `SecretDigester` (HMAC con *pepper*, comparado con `MessageDigest.isEqual`, tiempo constante); el acceso público, `loadPublicById`. Los fallos se registran en `INFO` sin el secreto.
- **Endpoints migrados:** `TokenController` (vía `IssueTokenUsecase`), `DevicesAccessController` (`/device` y PAR), `PushAuthorizationUsecase`, `IntrospectionController` y `TokenRevocationController`. Desaparecen cinco parseos de Basic distintos.
- **Cambios de comportamiento:**
  - `/device` sin secreto usaba `loadPreautorized`, que aceptaba **cualquier** cliente habilitado; ahora exige acceso público.
  - El `device_code` queda ligado al cliente que lo pidió (`DeviceAuthorizationService::exchangeDeviceCode(code, clientId)`): otro cliente recibe `invalid_grant`.
  - El grant `device_code` en `/token` admite ahora clientes públicos (antes exigía secreto, contra RFC 8628).
  - PAR deja de guardar `client_secret`/`client_assertion` entre los parámetros de la solicitud (adelanta parte de [0.11](../issue-0.11-state-seguro-secreto-fuera-par.md)).
  - Introspección y revocación responden `invalid_client` en JSON (antes 401 sin cuerpo).
- **Decisiones:**
  - El Basic se toma **literal**, sin decodificar `application/x-www-form-urlencoded` (RFC 6749 §2.3.1). Decodificarlo rompería a clientes existentes cuyos secretos tienen `+` o `%`; se revisará con la migración de secretos ([2.9](../issue-2.9-migracion-secretos-cifrado.md)).
  - Java conserva el modelo «método secreto registrado + `publicAllow`»: un cliente instalado con `client_secret_post` y `publicAllow` (consola) puede seguir canjeando como SPA con PKCE. PHP exige un único método registrado; la diferencia es intencionada y está acotada por PKCE.
  - Revocación sigue exigiendo cliente confidencial (PHP admite públicos): `RevokeTokenUseCase` aún no comprueba que el token pertenezca al cliente, así que abrirla a públicos debe esperar a esa comprobación ([0.15](../issue-0.15-audiencia-tenant-revocacion-bearer.md)).
- **Pruebas:** `ClientCredentialsUnitTest` y `ClientAuthenticatorUnitTest` (métodos, mezclas, Basic mal formado, `none`, `private_key_jwt`, método registrado exacto); `TokenEndpointContractTest` y `ConfidentialCodeFlowTest` por HTTP. Device y PAR no tienen prueba HTTP: el perfil de test los excluye (sin adaptadores de persistencia en memoria).
