# J-01 — Token y grants seguros

**Prioridad:** bloqueante · **Tareas:** [0.1](../../done/issue-0.1-canje-seguro-de-authorization-code.md), [0.2](../../done/issue-0.2-refresh-cliente-sesion.md), [0.9](../../done/issue-0.9-autenticacion-cliente-unificada.md), [0.13](../../done/issue-0.13-ruta-revocacion-no-duplicada.md), [1.4](../../done/issue-1.4-errores-cache-token.md), [1.8](../../done/issue-1.8-code-clientes-confidenciales.md), [1.10](../../done/issue-1.10-tests-negativos-seguridad.md), [1.16](../../done/issue-1.16-emision-canje-fuera-tokencontroller.md). **Depende de:** política de [J-02](ep-J-02-clientes-tenant-secretos.md) para cliente/grant/tenant; la seguridad del code y refresh empieza antes de terminar toda la épica.

**Estado:** cerrada (24-10-2026). Todas sus fichas están en [`done/`](../../done/README.md); la aceptación HTTP + BD se verifica con `make test-mariadb` ([V.1](../../issue-V.1-fixtures-pruebas-http-persistencia.md)). Para cerrar [1.10](../../done/issue-1.10-tests-negativos-seguridad.md) se adelantaron de [J-02](ep-J-02-clientes-tenant-secretos.md) las fichas [0.3](../../done/issue-0.3-audiencias-autorizadas.md) (audiencias) y [0.10](../../done/issue-0.10-redirect-uri-estricta.md) (redirect estricto).

## Implementación

1. Caracterizar flujos existentes; centralizar Basic/post y métodos registrados, sin confundir cliente público con confidencial. Habilitar code confidencial solo con canje autenticado.
2. Code: validar cliente, tenant, redirect y PKCE, consumir con operación atómica. Refresh: validar cliente, sesión, scope y audiencia originales; rotar JTI atómicamente con familia/expiración absoluta y revocar familia ante replay. Definir el tratamiento de grants sin sesión.
3. Consolidar canje y refresh en casos de uso/granters, usando la emisión Java existente; resolver ruta duplicada de revocación. Errores RFC 6749 y cabeceras anticaché en éxito/error.

**Aceptación:** dos canjes concurrentes de code o refresh producen un vencedor; code/refresh de otro cliente/tenant o sesión terminada no emiten tokens; errors y headers verificables por HTTP+BD. [V.1](../../issue-V.1-fixtures-pruebas-http-persistencia.md) conserva el arnés.

**Referencia PHP:** `done/017-canje-code-atomico.md`, `done/019-refresh-cliente-y-sesion.md`, `done/020-rotar-refresh-y-detectar-replay.md`, EP-05 (038, 064). Java difería: `TokenController` resolvía code/refresh en línea y había un `RefreshGranter` inalcanzable; ambos están resueltos.

## Lo construido (documentación funcional)

### El endpoint de token, de principio a fin

`POST /oauth/openid/{tenant}/token` sigue siempre el mismo orden. Cada paso que falla corta la petición con su error OAuth y **no consume** nada:

1. **Grant reconocido.** Sin `grant_type`: `invalid_request`. Desconocido: `unsupported_grant_type`.
2. **Cliente autenticado** con un único método y según la política del grant ([0.9](../../done/issue-0.9-autenticacion-cliente-unificada.md)). Si no: `invalid_client` (401).
3. **Grant resuelto:**
   - `authorization_code` ([0.1](../../done/issue-0.1-canje-seguro-de-authorization-code.md), [1.8](../../done/issue-1.8-code-clientes-confidenciales.md)): el code debe ser de ese cliente, tenant y `redirect_uri`, con PKCE válido (o cliente confidencial sin PKCE). Se consume atómicamente.
   - `refresh_token` ([0.2](../../done/issue-0.2-refresh-cliente-sesion.md)): token del tenant y del cliente, familia viva y no vencida, sesión viva si nació de un login, scope igual o menor. El JTI se consume atómicamente; un JTI reutilizado revoca la familia.
   - `password`, `mfa`, `client_credentials`, `device_code`: el grant debe estar permitido al cliente (`unauthorized_client`) y lo resuelve su granter; credenciales de usuario erróneas son `invalid_grant`.
4. **Emisión** única por `JwtTokenBuilder`/`ClaimsMapper`, ligando los JTI a la sesión ([1.16](../../done/issue-1.16-emision-canje-fuera-tokencontroller.md)).

Toda respuesta lleva `Cache-Control: no-store` y `Pragma: no-cache` ([1.4](../../done/issue-1.4-errores-cache-token.md)).

### Qué cliente puede qué

| Tipo de cliente | Cómo se autentica | Grants que puede usar |
|---|---|---|
| Web app confidencial | Su método registrado (`client_secret_basic` o `client_secret_post`) | code (con o sin PKCE), refresh, password, client_credentials, device |
| SPA / móvil (público) | Solo `client_id` (`none`) | code **con PKCE**, refresh, device |
| Mixto (secreto + `publicAllow`, como el cliente de la consola) | Su método secreto o como público | Lo de cada tipo según cómo se autentique en cada petición |
| Registrado como `private_key_jwt` | — | Ninguno hasta [3.2](../../issue-3.2-autenticacion-jwt-cliente.md) |

Introspección, revocación y PAR exigen secreto; `/device` admite públicos.

### Garantías frente a ataques

| Ataque | Defensa |
|---|---|
| Robo de un code | Ligado a cliente, tenant, redirect y PKCE; quien no es el dueño recibe `invalid_grant` y el code no se gasta |
| Doble canje o canjes simultáneos | Consumo atómico: un único vencedor |
| Robo de un refresh | Ligado al cliente (`azp`) y al tenant (`iss`) |
| Replay de un refresh rotado o rotaciones simultáneas | Un único vencedor; el reuso revoca la familia completa y, además, los access y refresh de las sesiones interactivas del usuario con ese cliente en ese tenant (los access de grants sin sesión siguen válidos hasta caducar) |
| Refresh tras cerrar sesión | Rechazado si la familia nació de un login interactivo |
| Prolongar un grant indefinidamente | Expiración absoluta de familia (`fexp`) |
| Downgrade de PKCE o confidencial haciéndose pasar por público | Un público nunca canjea sin PKCE; un verifier para un code sin challenge se rechaza |
| Token para un servidor de recursos ajeno | Solo el propio cliente o sus audiencias registradas; si no, `invalid_target` antes de validar al usuario ([0.3](../../done/issue-0.3-audiencias-autorizadas.md)) |
| Code enviado a una URI parecida a la registrada | Coincidencia exacta; solo loopback `/*` en desarrollo ([0.10](../../done/issue-0.10-redirect-uri-estricta.md)) |
| Revocación confundida con cierre de pre-sesión | Rutas separadas: `/revocation` (RFC 7009) y `/revoke` (pre-sesión) ([0.13](../../done/issue-0.13-ruta-revocacion-no-duplicada.md)) |

### Cambios de contrato para integradores

- `password` con credenciales erróneas: 400 `invalid_grant` (antes 401).
- `mfa_required`: 403 (antes 401), mismo cuerpo con `mfa_token`.
- Grant no permitido al cliente: `unauthorized_client` (antes `unsupported_grant_type`).
- Code y refresh exigen el método de autenticación **registrado**: un cliente `client_secret_basic` que enviaba el secreto en el cuerpo deja de funcionar.
- `/device` sin secreto solo para clientes públicos; el `device_code` solo lo canjea el cliente que lo pidió.
- `/revocation` solo como RFC 7009: los formularios de login deben usar `/revoke`.
- Introspección y revocación devuelven `invalid_client` en JSON.
- `audience` (query o cuerpo) solo admite el propio cliente o sus audiencias registradas: `invalid_target` en otro caso.
- `redirect_uri` exacta: los clientes con `*` o `https://…/*` deben registrar sus URIs concretas.

### Dónde está el código

| Pieza | Ubicación |
|---|---|
| Credenciales, métodos y políticas de cliente | `features/oauth/client/domain/ClientCredentials`, `ClientAuthMethod`, `ClientAuthPolicy` |
| Autenticación de cliente | `features/oauth/client/application/ClientAuthenticator` |
| Caso de uso del endpoint | `features/oauth/authentication/application/token/IssueTokenUsecase` |
| Code, refresh y emisión | `…/token/AuthorizationCodeGrant`, `RefreshTokenGrant`, `TokenIssuer` |
| Redirect y audiencias | `features/oauth/client/domain/RedirectUriPolicy`, `ClientDetails::allowsAudiences` |
| Error OAuth | `features/oauth/authentication/domain/exception/OAuthTokenException` |
| Driver HTTP | `features/oauth/authentication/infrastructure/driver/rest/TokenController` |
| Pruebas | `TokenGrantSecurityTest`, `TokenEndpointContractTest`, `ConfidentialCodeFlowTest`, `TokenGrantNegativeSecurityTest`, `ClientCredentialsUnitTest`, `ClientAuthenticatorUnitTest`; unitarios `RedirectUriPolicyUnitTest` y `ClientDetailsAudiencesUnitTest`; contra MariaDB, `TokenEndpointMariaDbTest` (HTTP + BD), `TemporalKeysSqlAdapterMariaDbTest` y `TokenRevocationSqlAdapterMariaDbTest` |

### Límites conocidos (no bloquean el cierre)

- Revocar los tokens del primer canje si se reintenta un code ya usado (requiere guardar los codes gastados; anotado en 0.1).
- `allowedGrants` aún son los valores por defecto del adaptador, y code/refresh no se filtran por ellos: aplicar `grantTypesJson` es [0.4](../../done/issue-0.4-configuracion-por-cliente.md) (J-02).
- La revocación de access tokens solo surte efecto donde se consulta (introspección); el bearer de las APIs aún no mira la lista de revocados ([0.8](../../issue-0.8-verificacion-bearer-apis.md), [0.15](../../issue-0.15-audiencia-tenant-revocacion-bearer.md)).
- `post_logout_redirect_uri` todavía admite comodines ([2.4](../../done/issue-2.4-redirects-logout.md)).
- El canje de `device_code` no es atómico (lee y borra sin comprobar filas).
- Los flujos de prueba con dobles arrancan con el perfil `test` y la base de `.env`: `OAuthTestProfile` no se aplica porque `@TestProfile` no se hereda de la clase base.
