# Análisis de Arquitectura: Bounded Context `features/access`

> Documento de referencia para desarrolladores y agentes LLM.  
> Analiza el diseño actual en `net.civeira.phylax.features.access`, identifica fortalezas y propone mejoras siguiendo DDD + Arquitectura Hexagonal.

---

## 1. Mapa del diseño actual

### 1.1 Estructura de capas (por subdominio)

Cada uno de los 20 subdominios bajo `features/access` replica la siguiente estructura:

```
{subdomain}/
├── domain/                          ← Capa de dominio (independiente de framework)
│   ├── {Entity}.java                   Agregado raíz
│   ├── {Entity}Ref.java                Interfaz marcador de referencia
│   ├── {Entity}Reference.java          Implementación ligera de referencia
│   ├── {Entity}ChangeSet.java          Portador de cambios (entrada a escrituras)
│   ├── event/
│   │   ├── {Entity}Event.java          Interfaz marcador de eventos
│   │   ├── {Entity}CreateEvent.java
│   │   ├── {Entity}UpdateEvent.java
│   │   └── {Entity}DeleteEvent.java
│   ├── valueobject/
│   │   ├── {Field}VO.java              Value Object inmutable
│   │   └── {Field}ValueHolder.java     Contenedor mutable (construcción/deserialización)
│   └── gateway/                     ← Puertos de salida (interfaces puras)
│       ├── {Entity}ReadRepositoryGateway.java
│       ├── {Entity}WriteRepositoryGateway.java
│       ├── {Entity}AuditGateway.java
│       ├── {Entity}CacheGateway.java
│       ├── {Entity}Filter.java
│       └── {Entity}Cursor.java
│
├── application/                     ← Orquestación de casos de uso
│   ├── usecase/
│   │   ├── create/ → {Entity}CreateUsecase + Input + Projection + AuthorizationDecision + Check + Enrich
│   │   ├── update/ → ídem
│   │   ├── delete/ → ídem + BatchCommand
│   │   ├── list/   → ídem + Filter + Cursor + Projection
│   │   └── retrieve/
│   ├── visibility/
│   │   ├── {Entity}sVisibility.java   Orquestador de visibilidad
│   │   ├── {Entity}VisibilityCheck.java
│   │   ├── {Entity}VisibilityFilter.java
│   │   ├── {Entity}VisibleProjection.java
│   │   ├── {Entity}HiddenPropertiesProposal.java
│   │   └── {Entity}FixedPropertiesProposal.java
│   └── importation/
│       └── {Entity}ImportationService.java
│
└── infrastructure/                  ← Adaptadores (entrada y salida)
    ├── driver/rest/                 ← Adaptadores primarios (entrada HTTP)
    │   ├── {Entity}Controller.java          Enrutador principal
    │   ├── {Entity}CreateController.java
    │   ├── {Entity}UpdateController.java
    │   ├── {Entity}DeleteController.java
    │   ├── {Entity}ListController.java
    │   ├── {Entity}RetrieveController.java
    │   ├── {Entity}AuditController.java
    │   ├── {Entity}AclController.java
    │   └── {Entity}BatchController.java
    ├── driven/                      ← Adaptadores secundarios (salida)
    │   ├── {Entity}ReadGatewayAdapter.java
    │   ├── {Entity}WriteGatewayAdapter.java
    │   ├── {Entity}AuditAdapter.java
    │   └── {Entity}CacheGatewayAdapter.java
    ├── repository/
    │   ├── {Entity}Repository.java
    │   └── {Entity}Slider.java
    ├── event/
    │   └── {Entity}EventDispatcher.java
    └── bootstrap/
        ├── {Entity}ProjectionDescriptor.java
        └── {Entity}RbacRegister.java
```

### 1.2 Subdominios identificados

| Subdominio | Capa de Aplicación | Notas |
|---|---|---|
| `user` | ✅ completa | Agregado raíz del BC |
| `trustedclient` | ✅ completa | Registro de clientes OAuth |
| `tenantloginprovider` | ✅ completa | Proveedores de login por tenant |
| `tenanttermsofuse` | ✅ completa | |
| `tenant` | ✅ completa | |
| `tenantconfig` | ✅ completa | |
| `relyingparty` | ✅ completa | |
| `apikeyclient` | ✅ completa | |
| `consentpurpose` | ✅ completa | |
| `userroleassignament` | ✅ completa | |
| `usergroupmembership` | ✅ completa | |
| `role` | ✅ completa | |
| `userprofile` | ❌ ausente | Solo dominio + infraestructura |
| `useraccesstemporalcode` | ❌ ausente | |
| `userwebauthncredential` | ❌ ausente | |
| `userinvitation` | ❌ ausente | |
| `userconsentedscopes` | ❌ ausente | |
| `userconsentpurposes` | ❌ ausente | |
| `useracceptedtermnsofuse` | ❌ ausente | |
| `oauth` | ✅ (parcial, no domain) | Protocolo OIDC/OAuth2 |

---

## 2. Fortalezas del diseño actual

### 2.1 Los `*Gateway` como contratos legibles (el punto más valioso)

Las interfaces `*ReadRepositoryGateway` y `*WriteRepositoryGateway` en `domain/gateway/` son el hallazgo más potente del diseño para legibilidad por equipos y LLMs:

```java
// Solo leyendo esta interfaz se entiende TODO lo que puede hacer el agregado
// respecto a persistencia: sin tocar JPA, SQL ni Hibernate.
public interface UserRoleAssignamentReadRepositoryGateway {
  long count(UserRoleAssignamentFilter filter);
  boolean exists(String uid, Optional<UserRoleAssignamentFilter> filter);
  Optional<UserRoleAssignament> find(UserRoleAssignamentFilter filter);
  List<UserRoleAssignament> list(UserRoleAssignamentFilter filter);
  UserRoleAssignament resolve(UserRoleAssignamentRef reference);
  Slider<UserRoleAssignament> slide(UserRoleAssignamentFilter filter, UserRoleAssignamentCursor cursor);
}
```

**Regla a mantener y extender**: cualquier dependencia externa (BBDD, caché, bus de eventos, servicios externos, email) debe expresarse primero como una interfaz `*Gateway` en `domain/gateway/` o `application/port/`. Esto permite que un LLM o un desarrollador entienda las capacidades del agregado leyendo solo las interfaces, sin necesidad de acceder a la implementación.

### 2.2 Separación limpia de capas

La dependencia sigue la dirección correcta: `infrastructure → application → domain`. La capa de dominio no importa nada de CDI, JAX-RS ni persistencia. Los casos de uso solo conocen interfaces. Los adaptadores son los únicos que conocen frameworks.

### 2.3 Patrón `*Ref` / `*Reference` para referencias entre agregados

Permite referenciar otra entidad por identidad sin cargar el agregado completo:

```java
// Referencia ligera: solo el UID
public interface UserRoleAssignamentRef { String getUid(); }

// Resolución explícita cuando se necesita el agregado completo
UserRoleAssignament resolved = gateway.resolve(reference);
```

Este patrón evita el lazy-loading silencioso y hace explícitas las traversals de grafo.

### 2.4 Sistema de extensión por eventos CDI (AuthorizationDecision / Check / Enrich)

El patrón de `Event<*AuthorizationDecision>`, `Event<*Check>` y `Event<*Enrich>` es un mecanismo de extensión abierto/cerrado muy potente. Permite que múltiples observers participen en la decisión de autorización sin modificar el caso de uso:

```java
// En el use case: dispara el evento, los observers (en otros módulos) votan
Event<UserRoleAssignamentCreateAuthorizationDecision> createAllowEmitter;
// ...
createAllowEmitter.fire(decision); // todos los @Observes reaccionan
```

Esto facilita integración entre equipos: el equipo A define el caso de uso, el equipo B añade lógica de autorización sin tocar el código del equipo A.

### 2.5 Observabilidad integrada en las fronteras correctas

`@Observed` en casos de uso, `@ApiObserved` en controllers, `@Trace` en adapters. Las métricas y trazas se registran donde el dominio empieza y termina, no dispersas en lógica de negocio.

### 2.6 Paginación por cursor (`Slider<T>`)

El tipo de retorno `Slider<T>` encapsula la semántica de sliding window, evitando page/offset y siendo apto para streams de millones de registros.

---

## 3. Debilidades y recomendaciones

### 3.1 Falta de documentación en `package-info.java` (CRÍTICO para LLMs y onboarding)

**Estado actual**: solo 8 archivos `package-info.java`, todos en el subdominio `oauth`. Los otros 19 subdominios tienen 0 documentación de paquete.

**Impacto**: un LLM o desarrollador nuevo debe leer ficheros concretos para entender qué hace cada capa. Sin `package-info.java`, el grafo de paquetes es opaco.

**Recomendación**: añadir `package-info.java` con estructura mínima a cada paquete relevante. El subdominio `oauth` es el modelo a seguir:

```java
/**
 * Dominio del agregado UserRoleAssignament.
 *
 * <p>Responsabilidades:
 * <ul>
 *   <li>Definir la entidad y sus invariantes de negocio.</li>
 *   <li>Declarar los puertos de salida (interfaces *Gateway).</li>
 *   <li>Emitir eventos de dominio.</li>
 * </ul>
 *
 * <p>Restricciones:
 * <ul>
 *   <li>Sin dependencias de framework (CDI, JPA, JAX-RS).</li>
 *   <li>No importa clases de otros subdominios directamente; usa *Ref.</li>
 * </ul>
 */
package net.civeira.phylax.features.access.userroleassignament.domain;
```

**Paquetes prioritarios** (mínimo viable para legibilidad):
- `{subdomain}/` — qué es este subdominio y a qué agregado pertenece
- `{subdomain}/domain/` — invariantes y reglas del dominio
- `{subdomain}/domain/gateway/` — contratos con infraestructura
- `{subdomain}/application/usecase/` — qué operaciones expone
- `{subdomain}/application/visibility/` — qué es el sistema de visibilidad

### 3.2 Los subdominios no tienen una capa de Application (8 subdominios)

**Estado actual**: `userprofile`, `useraccesstemporalcode`, `userwebauthncredential`, `userinvitation`, `userconsentedscopes`, `userconsentpurposes`, `useracceptedtermnsofuse` no tienen capa de aplicación. La infraestructura llama directamente a los gateways.

**Impacto**: 
- No se pueden añadir políticas de autorización (`*AuthorizationDecision`) sin crear la capa completa
- Viola la consistencia del patrón: equipos diferentes asumen lógicas distintas
- LLMs y desarrolladores no saben si la ausencia es intencional o un gap

**Recomendación**: añadir casos de uso mínimos aunque sean delegadores. Aunque no contengan lógica hoy, establecen el punto de extensión correcto y mantienen la predictibilidad del patrón.

### 3.3 Los puertos de caché y auditoría están mezclados con puertos de dominio

**Estado actual**: `*AuditGateway` y `*CacheGateway` viven en `domain/gateway/` junto a `*ReadRepositoryGateway` y `*WriteRepositoryGateway`.

**Impacto**: la caché es una decisión de infraestructura (Redis, Caffeine, sin caché), no un concepto del dominio. Mezclarlos obliga al dominio a saber que existe una caché.

**Recomendación**: separar por responsabilidad:

```
domain/gateway/          ← puertos de dominio puro
  {Entity}ReadRepositoryGateway.java    (qué datos puede leer el dominio)
  {Entity}WriteRepositoryGateway.java   (qué puede persistir)

application/port/        ← puertos de aplicación (orquestación)
  {Entity}AuditPort.java                (registro de auditoría)
  {Entity}CachePort.java                (invalidación de caché)
```

Esto también hace que el nombre sea más honesto: `Port` en la capa de aplicación, `Gateway` en la de dominio (o unificar el término al migrar gradualmente).

### 3.4 Las fronteras entre subdominios no están reforzadas structuralmente

**Estado actual**: `userroleassignament` importa directamente `role.domain.Role` en su gateway. No hay mecanismo que impida que cualquier subdominio importe clases de otro directamente.

**Impacto**: el acoplamiento entre BCs crece silenciosamente sin que el compilador lo detecte. Un desarrollador o un LLM puede resolver la referencia de otra forma y crear dependencias cruzadas no intencionadas.

**Opciones en orden de coste**:

1. **ArchUnit tests** (bajo coste, alta efectividad): reglas que validen que `{subdomain}.domain` no importa clases de `{other-subdomain}.domain` salvo mediante `*Ref`.
   ```java
   // Ejemplo de regla ArchUnit
   noClasses().that().resideInPackage("..domain..")
     .should().dependOnClassesThat()
     .resideInPackage("..other.domain..")
     .andAreNotAssignableTo(SomeRef.class)
     .check(importedClasses);
   ```

2. **Maven multi-módulo** (mayor coste, garantía estructural): cada subdominio como módulo independiente, con `pom.xml` que declara dependencias explícitas.

3. **Java Platform Module System (JPMS)**: `module-info.java` por subdominio.

**Recomendación inmediata**: ArchUnit. Es retrocompatible y no requiere reestructurar el build.

### 3.5 Patrón `*VO` + `*ValueHolder` necesita documentación explícita

**Estado actual**: cada campo tiene dos clases — `UidVO` (inmutable) y `UidValueHolder` (mutable/holder). La distinción no está documentada en ningún lugar.

**Impacto**: un desarrollador nuevo no sabe cuándo usar `VO` y cuándo `ValueHolder`. Un LLM asumirá que son intercambiables.

**Recomendación**: añadir un `package-info.java` en `valueobject/` que explique explícitamente la distinción:

```java
/**
 * Value Objects del agregado {Entity}.
 *
 * <p>Cada campo del agregado tiene dos representaciones:
 * <ul>
 *   <li><b>*VO</b>: valor inmutable, es el tipo que usa el agregado en tiempo de ejecución.
 *       Encapsula validación y semántica del campo.</li>
 *   <li><b>*ValueHolder</b>: contenedor mutable para construcción incremental (builders,
 *       deserialización parcial, ChangeSets). Se convierte a VO antes de entrar al agregado.</li>
 * </ul>
 */
package net.civeira.phylax.features.access.userroleassignament.domain.valueobject;
```

### 3.6 El patrón de extensión por eventos CDI no está documentado globalmente

**Estado actual**: `Event<*AuthorizationDecision>`, `Event<*Check>`, `Event<*Enrich>` son el mecanismo principal de extensión. Es un patrón sofisticado que no está documentado más allá de comentarios `@autogenerated`.

**Impacto**: equipos nuevos o LLMs que lean un caso de uso no entienden el contrato de extensión sin rastrear todos los `@Observes` en el código.

**Recomendación**: crear un documento de referencia del patrón (puede ser una sección del CLAUDE.md o un `package-info.java` en `application/usecase/`):

```
El ciclo de vida de un caso de uso sigue tres fases de extensión:

1. AuthorizationDecision — ¿está permitida la operación?
   @Observes UserRoleAssignamentCreateAuthorizationDecision
   → Llama decision.deny("motivo") para bloquear

2. Check — ¿son válidos los datos?
   @Observes UserRoleAssignamentCreateCheck
   → Lanza excepciones de validación

3. Enrich — ¿qué valores por defecto o derivados añadir?
   @Observes UserRoleAssignamentCreateEnrich
   → Modifica el ChangeSet antes de persistir
```

### 3.7 Typo persistente: `assignament` en lugar de `assignment`

**Estado actual**: el paquete `userroleassignament` tiene una errata en todas sus clases (157 ficheros).

**Impacto**: bajo en funcionamiento, alto en búsquedas y primeras impresiones. Un LLM que busque "assignment" no encontrará el paquete.

**Recomendación**: documentar explícitamente el typo en el `package-info.java` del paquete raíz para que las búsquedas semánticas lo encuentren, y planificar el renombrado como tarea de baja prioridad si el generador de código puede regenerarlo.

---

## 4. Recomendaciones específicas para legibilidad por LLMs

Los LLMs leen el código de forma diferente a los humanos: no navegan el IDE, procesan texto en secuencia y construyen su modelo mental a partir del contexto disponible en la ventana de contexto.

### 4.1 Las interfaces Gateway son los mejores puntos de entrada

**Una interfaz `*Gateway` permite a un LLM entender las capacidades del agregado sin leer la implementación** (que puede tener cientos de líneas de JPA). Esto ya existe y debe protegerse: nunca eliminar una interfaz Gateway "porque solo hay una implementación".

La existencia de una interfaz tiene valor semántico independientemente del número de implementaciones.

### 4.2 Los `*Input` y `*Projection` son el contrato del caso de uso

Un LLM puede entender qué acepta y qué devuelve un caso de uso leyendo solo:
```
UserRoleAssignamentCreateInput.java      ← qué datos necesita
UserRoleAssignamentCreateProjection.java ← qué devuelve
```

Manener estos DTOs limpios y bien nombrados es crítico para la legibilidad.

### 4.3 `package-info.java` como contexto de navegación

Cuando un LLM recibe una lista de ficheros de un paquete, no tiene contexto sobre su propósito. Un `package-info.java` en cada paquete convierte la lista de ficheros en contexto navegable.

**Coste**: bajo. **Impacto en legibilidad**: muy alto.

### 4.4 Los `*Filter` y `*Cursor` como contratos de consulta

```java
// Sin leer el Repository, un LLM sabe exactamente qué filtros soporta la query
UserRoleAssignamentFilter filter = UserRoleAssignamentFilter.builder()
    .tenant(tenantRef)
    .user(userRef)
    .build();
```

Mantener `*Filter` en el dominio (no en infraestructura) garantiza que el vocabulario de consulta pertenece al dominio.

### 4.5 Evitar abreviaciones en nombres de paquetes y clases

`assignament`, `termns` — los errores ortográficos rompen la búsqueda semántica. Los LLMs tokenizan el texto y una palabra mal escrita genera tokens distintos que la versión correcta.

### 4.6 Patrón de nombre predecible → pattern matching por LLMs

La consistencia actual del naming (`*CreateUsecase`, `*UpdateController`, `*ReadGatewayAdapter`) permite a un LLM inferir la existencia de un archivo sin tenerlo en contexto. Esta consistencia es uno de los activos más valiosos del diseño y debe protegerse en el generador de código.

---

## 5. Recomendaciones para trabajo en equipo multi-desarrollador

### 5.1 Cada subdominio como unidad de trabajo independiente

La estructura actual permite asignar un subdominio completo a un equipo/desarrollador sin conflictos de merge, siempre que:
- Las referencias cruzadas usen `*Ref` (ya cumplido en gran parte)
- No se añadan dependencias directas entre capas de dominio de subdominios distintos

### 5.2 Los contratos de integración son las interfaces Gateway + los eventos

Cuando un equipo A necesita datos del equipo B, el contrato es:
1. El equipo B define una interfaz `*Gateway` o emite un `*Event`
2. El equipo A implementa un `@Observes` o llama al Gateway vía inyección

Esto permite desarrollo paralelo sin coordinación constante.

### 5.3 ADRs (Architecture Decision Records) para decisiones no obvias

Decisiones como "¿por qué `*VO` + `*ValueHolder`?", "¿por qué eventos CDI en lugar de llamadas directas?", "¿por qué `Slider` en lugar de `Page`?" deben documentarse en ADRs en `docs/adr/`. Sin esto, cada nuevo miembro del equipo reproduce el mismo proceso de descubrimiento.

### 5.4 CLAUDE.md en el raíz del feature como guía de contribución

Un fichero `src/main/java/net/civeira/phylax/features/access/CLAUDE.md` (o equivalente en `docs/`) que describa:
- Cómo añadir un nuevo subdominio (qué ficheros crear y en qué orden)
- Cómo extender un caso de uso con un nuevo Observer
- Qué convenciones de naming son obligatorias vs opcionales
- Qué tests se esperan para cada tipo de clase

---

## 6. Resumen de prioridades

| Prioridad | Acción | Impacto | Coste |
|---|---|---|---|
| **P1** | Añadir `package-info.java` a todos los subdominios y capas | Legibilidad LLM + onboarding | Bajo |
| **P1** | Documentar el patrón `AuthorizationDecision/Check/Enrich` | Extensibilidad por equipos | Bajo |
| **P2** | Añadir capa Application a los 8 subdominios que la faltan | Consistencia + extensibilidad | Medio |
| **P2** | Tests ArchUnit para reforzar fronteras entre subdominios | Prevención de acoplamiento | Bajo |
| **P3** | Mover `*AuditGateway` y `*CacheGateway` a `application/port/` | Pureza del modelo de dominio | Medio (refactor gradual) |
| **P3** | Documentar el patrón `*VO` + `*ValueHolder` | Comprensión del modelo | Bajo |
| **P4** | ADRs para decisiones de diseño clave | Preservación del conocimiento | Bajo |
| **P4** | Corregir typos en nombres de paquetes (`assignament`, `termns`) | Búsqueda semántica | Alto (rompe API si hay refactor) |

---

## 7. Patrón de referencia: `userroleassignament`

Este subdominio es el ejemplo más completo del diseño y puede usarse como plantilla para generar nuevos subdominios. Tiene todas las capas, todos los patrones y es el punto de partida recomendado para:
- Leer antes de crear un nuevo subdominio
- Mostrar a un LLM como contexto cuando se le pide generar código para un subdominio nuevo
- Validar que el generador de código produce la estructura correcta
