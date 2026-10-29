# V.1 — Fixtures y pruebas HTTP + persistencia

- **Fase:** validación transversal · **Estado:** en curso (27-10-2026): arnés HTTP + MariaDB aplicado a code, refresh, revocación, bearer/UserInfo, logout, device, PAR y DCR, y CI con MariaDB de servicio; faltan login y clientes reales, reloj y claves compartidas con PHP y verificar el CI.
- **Implementación:** compartir escenarios semánticos con PHP: dos tenants, clientes público/confidencial, redirects válidos e inválidos, sesiones, claves y reloj controlado.
- **Cierre:** pruebas de code/refresh concurrentes, grants, device, PAR, bearer/UserInfo, revocación y logout observables por HTTP y almacenamiento.
- **Fuente:** [§5, validación](CAPACIDADES_OIDC.md#validación).

## Qué hay (funcional)

Dos niveles de prueba que se complementan:

| Nivel | Qué ejercita | Almacenamiento | Cuándo corre |
|---|---|---|---|
| Flujo HTTP (`integrationtest/features/oauth/flow`) | Endpoints reales de Quarkus, dos tenants (`test-tenant`, `other-tenant`), clientes legado/confidencial/público | Dobles en memoria (`alt/Fixed*`) | Siempre, con `mvn test` |
| Persistencia (`integrationtest/persistence`, `*SqlAdapterMariaDbTest`) | Los adaptadores SQL reales: consumo atómico, funciones JSON, claves y restricciones | Esquema MariaDB desechable con las migraciones del proyecto | Bajo demanda: `make test-mariadb` |
| HTTP + BD (`integrationtest/persistence/TokenEndpointMariaDbTest`) | Endpoints reales de Quarkus con codes, JTI y sesiones en MariaDB; login, clientes y claves siguen con dobles | Esquema MariaDB desechable migrado por la propia aplicación al arrancar | Bajo demanda: `make test-mariadb` |

**Arnés de persistencia** (`MariaDbSchema`, extensión JUnit 5):

- Crea un esquema `phylax_v1_<aleatorio>` por clase de prueba, aplica `migration/mysql/changelog.txt` con el mismo `Migrations` que el arranque y lo borra al terminar. Nunca usa la base de datos de `.env`.
- Se activa con `V1_MARIADB_URL` (servidor sin base de datos, p. ej. `jdbc:mariadb://localhost:3306`), `V1_MARIADB_USER` y `V1_MARIADB_PASSWORD`, como variables de entorno o propiedades del sistema. Sin ellas las pruebas se **omiten** (no fallan), así que `mvn test` sigue funcionando sin MariaDB.
- `make test-mariadb` toma servidor y credenciales de `.env` (`DATASOURCE_*`); el usuario necesita permiso para crear y borrar bases de datos.
- `Concurrently.run(n, call)` lanza `n` hilos liberados a la vez, cada uno con su propia conexión.

**HTTP + BD** (`MariaDbFlowProfile` + `MariaDbFlowResource`): el recurso de test crea el esquema y apunta a él el datasource, la réplica y `app.migration`; el perfil excluye los dobles `FixedTemporalKeysGateway`, `FixedTokenRevocationGateway` y `FixedSessionStore`, de modo que sirven los adaptadores SQL de producción. `FlowTemporalKeysGateway` hereda de `TemporalKeysSqlAdapter` (codes en MariaDB) y solo fija el manejo de claves de los formularios, como el doble. La clase solo se ejecuta si existe `V1_MARIADB_URL`.

**Casos cubiertos contra MariaDB (24-10-2026):**

| Prueba | Garantía |
|---|---|
| `TemporalKeysSqlAdapterMariaDbTest` | Un code guardado conserva cliente, tenant y redirect; 16 canjes simultáneos → un vencedor; un canje rechazado por el validador no gasta el code y uno canjeado no se reutiliza (0.1) |
| `TokenRevocationSqlAdapterMariaDbTest` | 16 consumos simultáneos del mismo JTI de refresh → un vencedor; la familia revocada se persiste solo para su tenant; tras cerrar la sesión el refresh deja de estar activo (0.2); el barrido por replay revoca access y refresh de las sesiones del usuario con el cliente y **no** toca otro tenant, otro usuario ni otro cliente |

| `TokenEndpointMariaDbTest` (vía `POST /token`) | 8 canjes simultáneos de un code → un vencedor y la fila desaparece; code de otro cliente, tenant o redirect → `invalid_grant` sin borrar la fila; 8 rotaciones simultáneas de un refresh → un vencedor, JTI consumido y familia revocada en la base de datos; refresh de otro cliente/tenant no consume el JTI; refresh de una sesión borrada → `invalid_grant` |

| `BearerContractMariaDbTest` (J-04) | Introspección y UserInfo comparten el estado de revocación; otro tenant o propósito nunca es activo; UserInfo con el email persistido y solo los scopes concedidos |
| `SessionLogoutMariaDbTest` (J-07) | El logout revoca solo los grants de su sesión; front- y back-channel solo a los clientes participantes, con `sid` público |
| `DeviceAndParMariaDbTest` (27-10-2026, HTTP) | La aprobación device exige el token del formulario y una sesión del tenant; un device code aprobado se canjea una vez entre 8 sondeos simultáneos y no en otro tenant; un PAR se guarda en su tenant, otro tenant no lo gasta y entre 8 authorize simultáneos solo uno lo consume |
| `DynamicClientHttpMariaDbTest` (27-10-2026, HTTP) | DCR por HTTP: alta y lectura sin secreto, metadatos inválidos sin gastar usos, credenciales de otro tenant/caducadas/agotadas rechazadas ([V.3](issue-V.3-conformidad-dcr.md)) |
| `InvitationMariaDbTest` (27-10-2026) | Invitación localizada por digest solo en su tenant; 8 aceptaciones simultáneas → una cuenta; caducada o con cuenta existente no se acepta; cancelar y reenviar solo una pendiente del tenant, y el reenvío invalida el enlace anterior |

La prueba de aislamiento se comprobó por mutación: con el SQL anterior (sin filtro de tenant) falla.

**Fallos de producción que encontraron estas pruebas (27-10-2026), corregidos:**

| Fallo | Corrección |
|---|---|
| **PAR inservible contra MariaDB:** el `request_uri` medía 106 caracteres y la columna es `VARCHAR(100)`, así que todo push daba 500 (los dobles en memoria lo ocultaban) | `ParRequest` usa 32 bytes de `SecureRandom` en base64url (77 caracteres, más entropía que el hex de un UUID) |
| **Device code canjeable varias veces:** el canje leía `APPROVED` y borraba sin comprobar filas, así que sondeos simultáneos emitían varios juegos de tokens; `approve`/`deny` no exigían `PENDING` | `consume` devuelve si borró la fila y solo el ganador emite; `approve`/`deny` con `AND status = 'PENDING'` |
| **Device code de otro tenant:** el canje solo comparaba el cliente; un cliente global podía canjear en el tenant B un código aprobado en el A | `exchangeDeviceCode` exige el tenant del token endpoint |
| **CSRF en `POST /device/verify`:** la cookie de sesión es `SameSite=None`, así que una página ajena podía aprobar el código de un atacante con la sesión de la víctima (RFC 8628 §5.5) | Token de doble envío (`csrf` + cookie `DEVICE_CSRF`, `SameSite=Lax`); la sesión debe ser del tenant (`SessionManager.loadSession(id, tenant)`, compartido con `AuthorizeHtml`); `verification_uri_complete` muestra el formulario con el código guardado |
| **Interbloqueo en el sondeo device:** cada lectura lanzaba un `DELETE` por rango de caducados, y sondeos simultáneos daban 500 por *deadlock* | La purga solo va en el alta (device y PAR) y tolera perder la carrera con otra purga |
| **DCR inservible por HTTP:** ver [V.3](issue-V.3-conformidad-dcr.md) | `RegistrationAwareJwtAuthMechanism` |

## Pendiente

- Extender el HTTP + BD a login y clientes reales (hoy con dobles; el doble de login abre siempre sesiones en `test-tenant`). Los flujos con dobles siguen arrancando con el perfil `test` contra la base de `.env`: `OAuthTestProfile` no se aplica porque `@TestProfile` no se hereda; las clases MariaDB llevan su perfil en la propia clase.
- Reloj controlado y claves fijas compartidas con la batería PHP.
- CI: `.github/workflows/ci.yml` levanta MariaDB de servicio y ejecuta `mvn test` con `V1_MARIADB_*`, así que corren las tres capas; **aún no se ha ejecutado en GitHub**, y puede necesitar más variables de `.env` (`SECURITY_SECRETS_*`, correo).
