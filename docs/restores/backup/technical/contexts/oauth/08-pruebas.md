# Pruebas

Tres capas, de la más rápida a la más cercana a producción. Cada clase de prueba indica en su Javadoc qué ficha de [docs/TODO-OIDC](../../../../../docs/TODO-OIDC/README.md) verifica.

| Capa | Dónde | Qué es real | Cómo se ejecuta |
|---|---|---|---|
| Unitaria | `src/test/java/.../features/oauth/**/*UnitTest` | Políticas de dominio y casos de uso, con Mockito | `mvn test` |
| Flujo HTTP (`@Tag("oidc-flow")`) | `integrationtest/features/oauth/flow` | Todos los endpoints de Quarkus; login, clientes, claves, codes, sesiones y revocaciones son dobles en memoria (`alt/`, `scenario/`) | `mvn test -Dgroups=oidc-flow` |
| MariaDB (`@Tag("mariadb")`) | `integrationtest/persistence/*MariaDbTest` | Los adaptadores SQL de producción sobre un esquema desechable (`phylax_v1_*` en la aplicación de referencia); las `@QuarkusTest` pasan además por HTTP | `make test-mariadb` |

La capa MariaDB se **omite** (no falla) sin `V1_MARIADB_URL` (servidor sin base de datos), `V1_MARIADB_USER` y `V1_MARIADB_PASSWORD`. `make test-mariadb` las toma de `.env`; el usuario debe poder crear y borrar bases de datos. En CI (`.github/workflows/ci.yml`) un MariaDB de servicio cubre las tres capas; ese workflow aún no se ha visto en verde en GitHub.

Toda clase que cambie el flujo de autenticación necesita cobertura en el grupo `oidc-flow`.

## Fixtures

- `OidcTestFixtures`: dos tenants (`test-tenant`, `other-tenant`), el cliente por defecto de las pruebas (`test-client`), un cliente confidencial web (`web-client`), una SPA pública (`spa-client`), un cliente `private_key_jwt` (`jwt-client`), redirects exactos y un usuario.
- `FixedClientStoreGateway`: los clientes anteriores, ajustables por prueba (grants, scopes, PAR, JWKS, pairwise). Aplica la `RedirectUriPolicy` de producción.
- `ScenarioLoginGateway` y el resto de `scenario/`: login, consentimiento, MFA y login federado controlados por la prueba. El doble de login siempre abre sesiones en `test-tenant`.
- `MariaDbFlowProfile` / `MariaDbFlowResource`: crean el esquema, apuntan el datasource a él y excluyen los dobles de codes, revocaciones, sesiones, búsqueda de tokens y PAR, de modo que se usan los adaptadores SQL.
- `MariaDbSchema` aplica `migration/mysql/changelog.txt` con el mismo `Migrations` del arranque; `Concurrently.run(n, call)` libera `n` hilos a la vez, cada uno con su conexión.
- El perfil de test eleva el límite de peticiones por IP, que las pruebas agotarían.

## Qué garantiza la capa MariaDB

| Prueba | Garantía |
|---|---|
| `TokenEndpointMariaDbTest` | Un solo vencedor entre canjes de code y rotaciones de refresh simultáneos; codes y refresh de otro cliente, tenant o redirect rechazados sin gastarse |
| `TemporalKeysSqlAdapterMariaDbTest`, `TokenRevocationSqlAdapterMariaDbTest` | Consumo atómico de codes y JTI; revocación de familia limitada a su tenant |
| `BearerContractMariaDbTest` | Introspección y UserInfo comparten el estado de revocación; otro tenant o propósito nunca es activo |
| `SessionLogoutMariaDbTest` | El logout revoca solo los grants de su sesión; front- y back-channel solo a los participantes |
| `DeviceAndParMariaDbTest` | La aprobación device exige el token del formulario y una sesión del tenant; un device code se canjea una vez y solo en su tenant; un PAR se consume una vez y solo en su tenant |
| `DynamicClientRegistrationMariaDbTest`, `DynamicClientHttpMariaDbTest` | Credenciales DCR del tenant, uso atómico, errores de metadatos que no gastan usos, columnas de ejecución, sin secreto en las lecturas |
| `InvitationMariaDbTest`, `RecoveryCodeMariaDbTest` | Un solo uso bajo concurrencia |

Estas pruebas encontraron fallos que los dobles ocultaban (PAR con `request_uri` más largo que la columna, device code canjeable varias veces o en otro tenant, CSRF en `/device/verify`, interbloqueo en el sondeo, DCR inaccesible por HTTP); todos están corregidos.

## Pruebas por área

| Área | Pruebas HTTP principales |
|---|---|
| Authorize, PKCE, PAR, JAR, hints, ACR | `AuthorizeRequestFlowTest`, `PkceFlowTest` |
| Respuestas, hybrid, `form_post`, nonce | `AuthorizeResponseContractTest`, `IdTokenClaimsFlowTest` |
| JARM, UserInfo firmado, JWE | `JoseResponsesFlowTest` |
| Token endpoint y grants | `TokenEndpointContractTest`, `TokenGrantSecurityTest`, `TokenGrantNegativeSecurityTest`, `ConfidentialCodeFlowTest` |
| Política de cliente | `ClientPolicyFlowTest`, `ClientAssertionFlowTest` |
| Consentimiento | `ConsentFlowTest`, `ScopeConsentFlowTest`, `PromptConsentFlowTest` |
| Claims, pairwise, resource indicators | `ClaimsParameterFlowTest`, `PairwiseSubjectFlowTest`, `ResourceIndicatorFlowTest` |
| DPoP, token exchange | `DpopFlowTest`, `TokenExchangeFlowTest` |
| Bearer en APIs | `ApiBearerTenantFlowTest` |
| Sesión | `SessionStateFlowTest`, `SessionManagementTest` |
| Login federado | `DelegatedLoginFlowTest` |
| Recuperación, registro, MFA | `RecoverPasswordFlowTest`, `RegisterUserFlowTest`, `MfaFlowTest`, `NewMfaFlowTest` |

## Sin cubrir

- Pruebas HTTP de enlace mágico, WebAuthn, aceptación de invitaciones y páginas de perfil.
- Login y clientes con los adaptadores reales en la capa HTTP + MariaDB (siguen con dobles).
- Reloj controlado y claves fijas compartidas con la batería PHP (V.1).

## OpenID Conformance Suite

Arnés en [conformance/](../../../../../conformance/README.md) (`make conformance`, workflow manual `.github/workflows/conformance.yml`):

- `docker-compose.yml`: MariaDB, la aplicación con un seed propio y Caddy con TLS interno en `https://op.phylax.test:9443`.
- `run.sh`: levanta la suite, activa DCR en `main`, crea un initial access token, registra los clientes de los planes y ejecuta *Basic OP*, *Config OP*, *RP-Initiated Logout* y *Back-Channel Logout*; *Dynamic OP* solo con `RUN_DYNAMIC=1`.

**No se ha ejecutado nunca** (V.2). Las variantes de los planes, las URIs y la automatización del navegador deben confirmarse en la primera ejecución; el login depende de JavaScript para cifrar la contraseña y firmar el formulario.
