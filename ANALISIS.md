Auditoría Arquitectónica: Phylax API
Proyecto: IAM/OAuth 2.0 Server · Framework: Quarkus 3.30.2 · Java 21 · ~1,505 archivos fuente

1. Resumen de Arquitectura
El proyecto implementa una Arquitectura Hexagonal (Ports & Adapters) con principios DDD sólidos, organizada por features:


features/
├── access/                     ← Gestión de acceso (13 submódulos)
│   ├── tenant/                 ← Multi-tenancy
│   ├── user/                   ← Gestión de usuarios (el más complejo)
│   ├── role/                   ← RBAC
│   ├── apikeyclient/           ← API Keys
│   ├── trustedclient/          ← Clientes confiables
│   └── ...                     ← identity, terms, login providers
└── oauth/                      ← Servidor OAuth 2.0/OIDC
    ├── authentication/         ← Flujos de autenticación
    ├── token/                  ← Gestión de tokens JWT
    ├── delegated/              ← Login social (Google, GitHub...)
    └── rbac/                   ← Control de acceso por recursos

Cada submódulo:
├── domain/          ← Agregados, Value Objects, Gateways (puertos), Eventos
├── application/     ← Use Cases, Commands, Projections, Visibility
└── infrastructure/
    ├── driver/      ← Adaptadores entrantes (REST controllers)
    ├── driven/      ← Adaptadores salientes (repositorios JDBC)
    ├── bootstrap/   ← Configuración DI
    └── event/       ← Despachadores de eventos
Patrones identificados: Hexagonal, DDD (Agregados, Value Objects, Domain Events), CQRS-light (gateways separados read/write), Event Sourcing-light, Builder, Repository, Projection, Batch Processing (Step Executor).

2. Fallos de Diseño Detectados
CRITICOS
#	Fallo	Ubicación	Impacto
C1	CORS completamente abierto	security.yaml	origins: /.*/ + credentials: true anula la protección CORS. Un sitio malicioso puede hacer requests autenticados en nombre del usuario
C2	Credenciales en texto plano en config	oauth.yaml	password: toop, store-pass: zfqvjnxl hardcodeados. Expuestos en repositorio
C3	Clave de cifrado con fallback inseguro	security.yaml	${security.encriptation.key:eventual} — si la variable no existe, usa "eventual" como clave
C4	Zero validación de entrada en controladores	Todos los controllers REST	No hay @Valid, @NotNull, @NotEmpty. Los parámetros se extraen manualmente de MultivaluedMap sin validación de tipo
IMPORTANTES
#	Fallo	Detalle
I1	Sin resiliencia para llamadas externas	No hay @Retry, @CircuitBreaker, @Timeout. Una dependencia caída bloquea toda la aplicación
I2	SecurityFilter lee el body completo en cada request	BufferedInputStream carga todo el body en memoria para análisis de inyección — riesgo de DoS por payload grande
I3	Risk scoring arbitrario sin pesos	El SecurityFilter suma scores de 10+ analizadores con threshold fijo > 10 — sin ponderación, sin documentación del umbral, alto riesgo de falsos positivos/negativos
I4	Connection pool mínimo	min-size: 1, max-size: 1 — un solo hilo de BD en la configuración por defecto. Cuello de botella garantizado bajo carga
I5	SQL strings gigantes hardcodeadas	Queries de 500+ caracteres construidas como strings en los repositorios — refactorización frágil, errores difíciles de detectar
I6	Rate limiting solo por IP, no por usuario/token	Un atacante autenticado puede abusar sin límite. Detrás de un proxy/NAT, usuarios legítimos comparten límite
MENORES
#	Fallo	Detalle
M1	Typo persistente: EventDispacher en vez de EventDispatcher	Propagado en todos los dispatchers por el generador de código
M2	Respuestas de error inconsistentes	Mezcla de Map<String,String>, objetos tipados, y texto plano ("Client not allowed.")
M3	NotFoundException con mensajes vacíos	getOrElseThrow(() -> new NotFoundException(""))
M4	DTOs como inner classes en controllers	Dificulta reutilización y testing
M5	Falta mapper 400 (Bad Request) y 500 (Internal Server Error)	Solo ThrowableMapper genérico para 500, sin 400 para validación
3. Mejoras Necesarias (Priorizadas)
Prioridad 1 — Seguridad (Inmediato)
Restringir CORS: Reemplazar origins: /.*/ con lista explícita de orígenes permitidos
Externalizar secretos: Mover credenciales a vault externo (HashiCorp Vault, AWS Secrets Manager, o al menos variables de entorno sin fallback inseguros)
Eliminar fallback de clave de cifrado: El encryption.key debe ser obligatorio, sin valor por defecto
Añadir validación JSR-380: Integrar quarkus-hibernate-validator y anotar DTOs con @Valid, @NotNull, @Size, etc.
Prioridad 2 — Estabilidad (Corto plazo)
Implementar resiliencia: Añadir quarkus-smallrye-fault-tolerance con @Retry, @CircuitBreaker, @Timeout para llamadas externas
Ajustar connection pool: Configurar max-size según carga esperada (mínimo 10-20 conexiones)
Limitar body en SecurityFilter: Establecer límite máximo de lectura (ej. 1MB) y hacer streaming en vez de buffer completo
Rate limiting por usuario/token: Complementar el rate limit por IP con límites por identidad autenticada
Prioridad 3 — Mantenibilidad (Medio plazo)
Adoptar framework de mapeo (MapStruct): Eliminar conversiones manuales entre capas
Estandarizar formato de error: Definir un ErrorResponse consistente con code, message, field, details
Mejorar tests de integración: La ratio 293 tests / 1,505 fuentes (19.4%) es baja. Tests unitarios fuertes, pero integración casi inexistente
Documentar código generado: Crear guía de qué genera qué, y cómo regenerar sin romper customizaciones
Prioridad 4 — Evolución (Largo plazo)
Architecture Decision Records (ADRs): Documentar decisiones clave (por qué JDBC crudo vs JPA, por qué event sourcing light, etc.)
Guía de onboarding: Cómo crear un nuevo feature module siguiendo el patrón establecido
Contract testing: Validar que la API OpenAPI coincide con la implementación real
4. Riesgos para Equipos de Trabajo
Curva de Aprendizaje
Aspecto	Dificultad	Tiempo estimado de onboarding
Arquitectura hexagonal + DDD	Alta	2-3 semanas
Patrones de Value Objects, Agregados, Eventos	Media-Alta	1-2 semanas
SQL Builder custom (sin ORM)	Media	1 semana
Código auto-generado (OpenAPI + generadores propios)	Media	1 semana
Stack de observabilidad (OTEL, Micrometer)	Baja	2-3 días
Riesgo principal: Un desarrollador junior puede tardar 4-6 semanas en ser productivo. Sin documentación arquitectónica (ADRs, guías), la barrera de entrada es alta.

Riesgos Operativos para el Equipo
Riesgo	Severidad	Mitigación
Modificar código generado: No queda claro qué es generado y qué es manual. Un dev puede editar código que se sobreescribirá	Alta	Separar carpetas generated/manual. Documentar proceso de generación
Inconsistencia al añadir features: Sin guía, cada dev puede interpretar el patrón diferente	Media	Crear template/archetype para nuevos módulos
Debugging complejo: Muchas capas (controller → usecase → visibility → gateway → repository) dificultan seguir el flujo	Media	Mejorar tracing. Documentar flujos principales
Merge conflicts en SQL: Queries como strings largos generan conflictos difíciles de resolver	Media	Considerar query builders más modulares o archivos SQL externos
Acoplamiento al generador: Si el generador de código tiene bugs (ej. Dispacher), se propagan a todo el proyecto	Media	Tests automatizados post-generación. Lint rules
Testing insuficiente para refactoring seguro: Sin tests de integración, un refactoring puede romper contratos entre capas	Alta	Invertir en tests de integración antes de refactorizar
Scorecard General
Área	Puntuación	Notas
Estructura y organización	9/10	Hexagonal + DDD ejemplar, consistencia alta
Patrones de dominio	9/10	Value Objects, Agregados, Eventos bien implementados
Seguridad	4/10	CORS abierto, credenciales en config, sin validación de entrada
Testing	5/10	Buenos unit tests, integración casi inexistente
Resiliencia	3/10	Solo rate limiting por IP, sin circuit breaker/retry
Mantenibilidad para equipo	5/10	Falta documentación, código generado no documentado
Observabilidad	8/10	OTEL + Prometheus + JSON logging bien integrado
API REST	6/10	OpenAPI presente pero respuestas inconsistentes
Conclusión
El proyecto tiene una base arquitectónica excelente — la implementación de Hexagonal + DDD es rigurosa y consistente. Sin embargo, los riesgos de seguridad (CORS, credenciales, validación) son críticos y deben abordarse de inmediato. Para trabajo en equipo, la mayor inversión necesaria es en documentación arquitectónica, tests de integración, y guías de onboarding que permitan a nuevos desarrolladores seguir los patrones establecidos sin divergir.


Análisis de Mejoras para el Generador de Código
Los retos que mencionas: confirmados y ampliados
1. MAPEOS — Boilerplate masivo y frágil
El generador produce 4 conversiones manuales por entidad, todas campo-a-campo con null checks:


// Controller: ApiDto → CreateInput (toDomainModel)
if (null != tenantApiDto.getName()) { dto.setName(tenantApiDto.getName()); }
if (null != tenantApiDto.getDomain()) { dto.setDomain(tenantApiDto.getDomain()); }
// ... N campos más

// Controller: Projection → ApiDto (toApiModel)
tenantApiDto.setUid(dto.getUid());
tenantApiDto.setName(dto.getName());
// ... N campos más

// Repository: converter() - Row → Domain (via VO)
.uidValue(UidVO.from(row.getString(UID)))
.nameValue(NameVO.from(row.getString(NAME)))
// ... N campos más

// Repository: create/update - Domain → SQL params
sq.with(UID, SqlParameterValue.of(entity.getUid()));
sq.with(NAME, SqlParameterValue.of(entity.getName()));
// ... N campos más
Problema para equipos: Cuando alguien añade un campo a la entidad, debe tocar 4+ archivos de mapeo. Si olvida uno, falla silenciosamente (campo queda null). No hay compilación que lo detecte.

Mejora para el generador: En vez de generar mapeo inline, generar una clase Mapper dedicada por entidad:


// Generado: TenantMapper.java
@ApplicationScoped
public class TenantMapper {
    Tenant fromInput(TenantCreateInput input) { ... }
    TenantApiDto toApi(TenantCreateProjection projection) { ... }
    Tenant fromRow(SqlResultSet row) { ... }
    void bindParams(SqlCommand sq, Tenant entity) { ... }
}
Esto centraliza los 4 mapeos en un solo archivo por entidad. Si se añade un campo, solo se toca el mapper.

2. SQL STRINGS — Grandes, frágiles, difíciles de revisar
El generador produce SQL inline como strings gigantes:


"insert into \"access_tenant\" ( \"uid\", \"name\", \"root\", \"domain\", 
\"enabled\", \"mark_for_delete\", \"mark_for_delete_time\", \"version\") 
values ( :uid, :name, :root, :domain, :enabled, :markForDelete, 
:markForDeleteTime, :version)"
Y el UPDATE repite todos los campos otra vez. Para User (14+ campos), las strings son enormes.

Problemas:

Un typo en nombre de columna no se detecta hasta runtime
Los filteredQuery() encadenan .where() manualmente por cada campo del filtro
La paginación por cursor (tryToOrderBy*) genera un método por cada campo ordenable, todos con la misma estructura
Mejora para el generador: Generar SQL a partir de metadatos de columna:


// Generado: TenantSchema.java (metadata)
public class TenantSchema {
    static final String TABLE = "access_tenant";
    static final Column<String> UID = column("uid", "uid", SqlParameterValue::of);
    static final Column<String> NAME = column("name", "name", SqlParameterValue::of);
    static final Column<Boolean> ROOT = column("root", "root", SqlParameterValue::of);
    // ...
    static final List<Column<?>> ALL = List.of(UID, NAME, ROOT, ...);
    static final List<Column<?>> INSERTABLE = List.of(UID, NAME, ROOT, ...);
    static final List<Column<?>> UPDATABLE = List.of(NAME, ROOT, ...); // sin UID
}

// Generado: repository usa schema
String insertSql = SchemaBuilder.insert(TenantSchema.TABLE, TenantSchema.INSERTABLE);
String updateSql = SchemaBuilder.update(TenantSchema.TABLE, TenantSchema.UPDATABLE);
Esto elimina strings manuales y centraliza la definición de columnas.

3. TRACING — Boilerplate try/catch/finally repetido en cada método
El patrón actual generado es:


Span span = tracer.spanBuilder("description")
    .setParent(Context.current().with(Span.current()))
    .setSpanKind(SpanKind.INTERNAL)
    .startSpan();
try {
    // lógica real (2-5 líneas)
} catch (RuntimeException ex) {
    span.setAttribute("error", true);
    span.recordException(ex);
    throw ex;
} finally {
    span.end();
}
Esto se repite en cada método de cada UseCase y cada GatewayAdapter. En TenantEnableUsecase (265 líneas), la lógica real ocupa ~30% del archivo; el 70% es boilerplate de tracing.

Mejora para el generador — dos opciones:

Opción A: Generar un interceptor CDI con anotación:


@Traced("enable an entity of tenant")
public TenantEnableProjection enable(Interaction query, TenantRef reference) {
    // solo lógica de negocio, sin try/catch/span
}
Opción B: Generar un helper funcional (menos invasivo):


// Generado en common
public class Tracing {
    public static <T> T traced(Tracer tracer, String name, 
                                Map<String,String> attrs, Supplier<T> fn) {
        Span span = tracer.spanBuilder(name)
            .setParent(Context.current().with(Span.current()))
            .setSpanKind(SpanKind.INTERNAL).startSpan();
        try {
            attrs.forEach(span::setAttribute);
            return fn.get();
        } catch (RuntimeException ex) {
            span.setAttribute("error", true);
            span.recordException(ex);
            throw ex;
        } finally {
            span.end();
        }
    }
}

// Uso generado:
public TenantEnableProjection enable(Interaction query, TenantRef ref) {
    return traced(tracer, "enable tenant", Map.of("ref", ref.getUid()), () -> {
        // lógica directa
    });
}
Esto reduce cada método de ~25 líneas a ~5.

4. TESTS — Solo estructurales, faltan los de comportamiento
El generador produce 293 tests, pero el 100% son unitarios de estructura:

Capa	Tests generados	Tests que faltan
Value Objects	~140 (builder + null)	Validaciones de negocio complejas
Entity builders	~30	Comportamiento de dominio
Filters/Cursors	~50	N/A (suficiente)
Use Cases	0	Flujo completo con mocks
Controllers/REST	0	@QuarkusTest + rest-assured
Repositories	0	Contra BD real (testcontainers)
Seguridad	0	JWT, RBAC, rate limiting
Mejora para el generador — generar 3 niveles de tests:

Nivel 1 (actual): Tests de VO/Entity → ya lo hace bien.

Nivel 2 (nuevo): Tests de UseCase con mocks:


// Generado: TenantEnableUsecaseTest.java
@ExtendWith(MockitoExtension.class)
class TenantEnableUsecaseTest {
    @Mock TenantWriteRepositoryGateway writeGateway;
    @Mock TenantAuditGateway audit;
    @Mock TenantCacheGateway cache;
    @Mock TenantsVisibility visibility;
    @Mock Tracer tracer;
    @InjectMocks TenantEnableUsecase usecase;

    @Test
    void enable_existing_tenant_returns_projection() {
        Tenant tenant = TenantTestBuilder.aDisabledTenant();
        when(visibility.retrieveVisibleForUpdate(any(), any()))
            .thenReturn(Optional.of(tenant));
        when(writeGateway.update(any(), any())).thenAnswer(i -> i.getArgument(1));
        
        TenantEnableProjection result = usecase.enable(anInteraction(), tenant);
        
        assertNotNull(result);
        verify(audit).updated(eq("enable"), any(), eq(tenant), any());
    }
    
    @Test
    void enable_nonexistent_tenant_throws_not_found() {
        when(visibility.retrieveVisibleForUpdate(any(), any()))
            .thenReturn(Optional.empty());
        
        assertThrows(NotFoundException.class, 
            () -> usecase.enable(anInteraction(), TenantReference.of("x")));
    }
}
Nivel 3 (nuevo): Tests de integración REST:


// Generado: TenantApiIT.java
@QuarkusTest
@TestHTTPEndpoint(TenantCreateController.class)
class TenantApiIT {
    @Test
    void create_tenant_returns_201() {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + testToken())
            .body("""
                {"name": "test", "domain": "test.com"}
                """)
        .when()
            .post()
        .then()
            .statusCode(200)
            .body("uid", notNullValue())
            .body("name", equalTo("test"));
    }
}
5. MEJORAS ADICIONALES para el generador
Más allá de los 4 retos que mencionas, detecto estas oportunidades:

5a. Validación de entrada (Jakarta Validation)
El generador no produce @Valid ni constraints en los DTOs. Debería generar:


// En TenantCreateInput (generado)
@NotNull @Size(min = 1, max = 255)
private Optional<NameVO> name;
Y en los controllers:


public Response tenantApiCreate(@Valid TenantApiDto tenant) { ... }
5b. Reducir Projections duplicadas
Hay 6 proyecciones por entidad (Create, Update, Retrieve, List, Enable, Disable...) y todas tienen los mismos campos. El generador podría producir una sola proyección base con factory methods:


// En vez de 6 clases casi idénticas:
public class TenantProjection {
    public static TenantProjection forCreate(Tenant entity) { ... }
    public static TenantProjection forUpdate(TenantVisibleProjection visible) { ... }
    // ...
}
5c. GatewayAdapter: tracing inconsistente
El WriteGatewayAdapter tiene tracing en create() y delete() pero no en update():


// create → tiene Span
// delete → tiene Span  
// update → NO tiene Span ← bug del generador
@Override
public Tenant update(TenantRef reference, Tenant entity) {
    Tenant result = repository.update(entity);  // sin tracing!
    eventDispacher.dispatch(entity);
    return result;
}
5d. Error messages vacíos
El generador produce:


throw new NotFoundException("");  // string vacío
throw new OptimistLockException("No delete from");  // confuso en update
Debería incluir contexto:


throw new NotFoundException("Tenant not found: " + reference.getUid());
throw new OptimistLockException("Concurrent update on Tenant: " + entity.getUid());
5e. Typos propagados
Además de Dispacher → Dispatcher, hay:

WriteAdaterGatewayGenerator → debería ser Adapter
ReadAdaterGatewayGenerator → debería ser Adapter
CacheAdaterGatewayGenerator → debería ser Adapter
AccesibleCheckGenerator → debería ser Accessible
ProjectionDescriptorGeneratorGenerator → doble "Generator"
RegisterGeneratorGenerator → doble "Generator"
5f. Generar application-test.yaml
El generador no produce configuración de tests. Debería generar:


# application-test.yaml
quarkus:
  datasource:
    devservices:
      enabled: true
      image-name: mariadb:11
  log:
    level: WARN
5g. OpenAPI spec sync
El generador produce controllers y OpenAPI spec por separado. Si el modelo cambia, pueden desincronizarse. Considerar generar la spec desde el modelo, no mantenerla a mano.

Resumen priorizado de mejoras para el generador
Prioridad	Área	Impacto	Esfuerzo
P0	Corregir typos en nombres de generadores	Credibilidad	Bajo
P1	Extraer Mapper por entidad (centralizar 4 mapeos)	Mantenibilidad	Medio
P1	Reducir SQL con Schema metadata	Fiabilidad	Medio
P1	Helper de tracing (eliminar try/catch boilerplate)	Legibilidad, -70% líneas	Bajo
P1	Añadir tracing al update() del WriteGatewayAdapter	Observabilidad	Bajo
P2	Generar tests de UseCase (nivel 2 con mocks)	Confianza en refactoring	Medio
P2	Generar @Valid en controllers y constraints en DTOs	Seguridad	Bajo
P2	Error messages con contexto (uid, tipo entidad)	Debuggabilidad	Bajo
P3	Unificar Projections (1 base en vez de 6 clases)	-5 clases por entidad	Medio
P3	Generar tests de integración REST (nivel 3)	Confianza en deploy	Alto
P3	Generar application-test.yaml y test infra	DX para equipos	Bajo
P3	Sync OpenAPI ↔ modelo generado	Consistencia API	Alto