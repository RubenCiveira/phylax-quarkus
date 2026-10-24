# V.1 — Fixtures y pruebas HTTP + persistencia

- **Fase:** validación transversal · **Estado:** en curso (24-10-2026): arnés HTTP + MariaDB operativo y aplicado a code, refresh y revocación ([J-01](epicas/done/ep-J-01-token-y-grants-seguros.md), cerrada); falta el resto de áreas.
- **Implementación:** compartir escenarios semánticos con PHP: dos tenants, clientes público/confidencial, redirects válidos e inválidos, sesiones, claves y reloj controlado.
- **Cierre:** pruebas de code/refresh concurrentes, grants, device, PAR, bearer/UserInfo, revocación y logout observables por HTTP y almacenamiento.
- **Fuente:** [§5, validación](../../src/docs/oidc/CAPACIDADES_OIDC.md#validación).

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

La prueba de aislamiento se comprobó por mutación: con el SQL anterior (sin filtro de tenant) falla.

## Pendiente

- Extender el HTTP + BD a login y clientes reales (hoy con dobles). Los flujos con dobles siguen arrancando con el perfil `test` contra la base de `.env`: `OAuthTestProfile` no se aplica porque `@TestProfile` no se hereda; `TokenEndpointMariaDbTest` lleva su perfil en la propia clase.
- Device, PAR, bearer/UserInfo, DCR y logout.
- Reloj controlado y claves fijas compartidas con la batería PHP.
- Ejecutar el arnés en CI con un MariaDB de servicio.
