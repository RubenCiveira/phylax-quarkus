# Análisis del Paquete `bootstrap` — Phylax API

Quarkus 3.x · Java 21 · 51 clases · 7 sub-paquetes

---

## 1. Inventario y organización de paquetes

### 1.1 Estructura actual

```
bootstrap/
├── Refections.java                         ← typo: "Refections"
├── document/
│   └── MockFileReader.java                 ← código de prueba en producción
├── rate/
│   ├── BucketService.java
│   └── RateLimitingFilter.java
├── security/
│   ├── MaliciousInjectionRiskAnalizer.java ← typo: "Analizer"
│   ├── SecurityFilter.java
│   └── analyzers/
│       ├── CommandInjectionAnalyzer.java
│       ├── HttpHeaderInjectionAnalyzer.java
│       ├── LdapInjectionAnalyzer.java
│       ├── PathTraversalAnalyzer.java
│       ├── RceAnalyzer.java
│       ├── SpecialCharactersAnalyzer.java
│       ├── SqlInjectionAnalyzer.java
│       └── XssAnalyzer.java
├── sql/
│   └── MigrationsManager.java
├── store/
│   └── FileStoreImpl.java
└── telemetry/
    ├── ApiObservedInterceptor.java
    ├── JsonSpanLog.java
    ├── LogEntry.java
    ├── LogManagementResource.java
    ├── LogManagementService.java
    ├── LoggingSpanExporter.java
    ├── MdcRegisterFilter.java
    ├── ObservedInterceptor.java
    ├── OpenTelemetryConfig.java           ← 100% comentado (código muerto)
    ├── TelemetryInterceptor.java
    ├── TraceManagementResource.java
    ├── collector/                          ← modelo OTLP + endpoints receptores
    │   ├── (14 clases de modelo, mappers, resources)
    └── exporter/
        ├── JsonSpanRecord.java
        └── SpanToJsonRecordMapper.java
```

### 1.2 Encapsulación — ¿alguien importa bootstrap desde fuera?

**Resultado: ninguna clase externa importa de `bootstrap`.**

El paquete está correctamente cerrado: ni la capa de aplicación, ni los dominios, ni otras clases de infraestructura dependen de él. Las dependencias fluyen solo en una dirección:

```
bootstrap → common (infraestructura compartida)
bootstrap → librerías externas (Quarkus, OpenTelemetry, Bucket4j…)
```

El contrato se cumple.

---

## 2. Problemas de nomenclatura y ubicación

### 2.1 Typos en nombres de clases/interfaces

| Archivo actual | Nombre correcto | Impacto |
|---|---|---|
| `Refections.java` | `Reflections.java` | Cosmético pero visible en el tooling de Quarkus |
| `MaliciousInjectionRiskAnalizer` | `MaliciousInjectionRiskAnalyzer` | La interfaz y los 8 beans que la implementan usan el nombre erróneo |

Los 8 analyzers (`XssAnalyzer`, `SqlInjectionAnalyzer`…) ya tienen el sufijo correcto `Analyzer`. Solo la interfaz mantiene el typo `Analizer`. Cambiarlo requiere renombrar la interfaz y actualizar los `implements` en las 8 clases.

### 2.2 `MockFileReader` en producción

`MockFileReader` está en `bootstrap.document`, marcado `@ApplicationScoped`, y:
- Siempre devuelve `true` en `canRead()` → intercepta cualquier `DataSource`
- Siempre devuelve datos hardcoded (`[{name: quelem}, {name: joanh}]`)

**Es código de prueba activo en producción.** Debería eliminarse del paquete o, si es una implementación por defecto intencional, renombrarse y documentarse como tal. Su ubicación correcta sería `src/test` o reemplazarse por una implementación real.

### 2.3 `OpenTelemetryConfig.java` — código muerto

El archivo contiene solo 19 líneas, todas comentadas. No hay nada activo. Debe eliminarse.

### 2.4 `telemetry/collector/` — endpoints de API pública sin guardia

`OtlpTracesResource` (`@Path("/v1/traces")`) y `OtlpLogsResource` (`@Path("/v1/logs")`) son endpoints JAX-RS normales, accesibles desde la API pública. Pero `LogManagementResource` y `TraceManagementResource` usan `@ManagementInterface` y se registran en el puerto de gestión. La inconsistencia significa que cualquiera puede enviar trazas o logs a la API pública sin autenticación.

Si estos endpoints son para consumo interno (agentes del mismo pod), deberían estar en la management interface como el resto, o protegidos con un filtro de autenticación.

---

## 3. Problemas de seguridad

### 3.1 CRÍTICO — Body buffering sin límite de tamaño (`SecurityFilter`)

```java
// SecurityFilter.java:89-108
InputStream originalInputStream = requestContext.getEntityStream();
ByteArrayOutputStream buffer = new ByteArrayOutputStream();
byte[] data = new byte[1024];
int nRead;
while ((nRead = originalInputStream.read(data, 0, data.length)) != -1) {
    buffer.write(data, 0, nRead);  // ← sin límite de tamaño
}
```

Un cliente puede enviar un cuerpo de varios GB. El filter lee todo a memoria antes de cualquier validación, abriendo un vector de DoS/OOM. Se necesita un límite máximo de bytes (`MAX_BODY_BYTES_FOR_ANALYSIS`) antes de leer.

### 3.2 ALTO — IP Spoofing en `BucketService`

```java
// BucketService.java:21-23
String ip = requestContext.getHeaders().getFirst("X-Forwarded-For");
if (ip == null) {
    ip = requestContext.getUriInfo().getRequestUri().getHost();
}
```

El header `X-Forwarded-For` puede ser falsificado por cualquier cliente. Un atacante puede enviar un header distinto en cada petición para obtener siempre un bucket nuevo con tokens completos, neutralizando el rate limiting completamente.

La solución correcta es confiar en `X-Forwarded-For` solo si la petición viene de un reverse proxy de confianza (verificando la IP de origen real de la conexión TCP, no un header). Si no hay proxy, usar siempre la IP de la conexión TCP.

### 3.3 ALTO — Lógica de bloqueo incompleta en `SecurityFilter`

```java
// SecurityFilter.java:59-75
if (riskScore > 10) {
    // Solo registra métrica — NO aborta la petición
    meterRegistry.counter("request.security.risk", ...).increment();
}
// ...
if (riskScore > 0) {
    if (!bucket.tryConsume(riskScore)) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)...);
    }
}
```

El comentario `// Skipping for brevity. Umbral arbitrario, puedes ajustar esto` confirma que la lógica de abort está incompleta. El comportamiento actual es:

- Una petición con `riskScore = 15` (XSS detectado) **no se bloquea directamente**: solo se registra la métrica y se consumen 15 tokens del bucket. Si el bucket tiene tokens suficientes (capacidad 200), la petición maliciosa pasa.
- El abort solo ocurre cuando el bucket se agota por acumulación de tokens consumidos, no por umbral de riesgo inmediato.

Se necesita un abort directo cuando el score supera el umbral configurado, independientemente del estado del bucket.

### 3.4 MEDIO — Patrón de SQL concatenado en `FileStoreImpl`

```java
// FileStoreImpl.java:62-64
String secureQuery = String.format(
    "DELETE FROM _filestorer where temp = 1 and code in (%s)",
    uids.stream().collect(Collectors.joining(","))
);
```

Los UUIDs provienen de una SELECT previa sobre la misma tabla, por lo que no hay un vector de inyección directo desde input externo. Sin embargo:
- El patrón es estructuralmente incorrecto y frágil
- Si la tabla se corrompe o un bug introduce valores no-UUID en `code`, se propagaría SQL arbitrario
- El nombre de la variable (`secureQuery`) es engañoso — el query no es seguro

Solución: usar un batch de `PreparedStatement` con `?` placeholders, o ejecutar deletes individuales en un loop.

### 3.5 BAJO — `MockFileReader.canRead()` siempre devuelve `true`

Con la implementación actual, cualquier `DataSource` que llegue al sistema será "leída" por el mock, devolviendo dos nombres hardcoded. Si hay otra implementación de `FileReader` para producción, el comportamiento CDI depende de cuál bean sea el `@Default`, pero el riesgo de activar el mock en producción existe.

---

## 4. Problemas de calidad de código

### 4.1 Debug prints en `OtlpTracesResource` (producción)

```java
// OtlpTracesResource.java:30-32
System.err.println(">>> GO WITH EXPORTERS: " + spanExporter.stream().count());
// ...
System.err.println("EXPORT: " + spans.size());
```

`System.err.println` en un endpoint de producción. Contaminan stdout/stderr del proceso con output sin formato ni nivel de log, y no pueden ser silenciados en runtime.

### 4.2 Sentencia muerta en `LogsMapper`

```java
// LogsMapper.java:33
SpanContext.create(null, null, null, null);  // resultado no asignado, sin efecto
```

El resultado no se usa en ningún sitio. Es probablemente un residuo de depuración. Debe eliminarse.

### 4.3 `BucketService.lastAccessTime` no se actualiza en cada acceso

```java
// BucketService.java:26-29
return ipBucket.computeIfAbsent(ip, key -> {
    lastAccessTime.put(key, Instant.now().toEpochMilli()); // solo en creación
    return createNewBucket();
});
```

`lastAccessTime` se actualiza solo cuando se crea el bucket (primera petición de esa IP). Si una IP hace peticiones durante 9 minutos, el siguiente `cleanExpiredEntries()` la elimina aunque siga activa, porque el timestamp de último acceso nunca se actualiza. El resultado es que IPs activas pierden su bucket y se resetea su contador de tokens.

### 4.4 `RateLimitingFilter` resuelve el bucket dos veces

```java
// RateLimitingFilter.java — request filter
Bucket bucket = buckets.resolveBucket(requestContext);  // llamada #1
if (!bucket.tryConsume(1)) { ... }

// response filter
Bucket bucket = buckets.resolveBucket(requestContext);  // llamada #2
responseContext.getHeaders().add("X-RateLimit-Remaining", ...);
```

`resolveBucket` llama a `cleanExpiredEntries()` en cada invocación. Entre la request y la response, si se ejecuta una limpieza en otro hilo, podría resolverse un bucket diferente. La cabecera `X-RateLimit-Remaining` podría reportar un valor inconsistente con el consumo real. El bucket debería almacenarse en el `ContainerRequestContext` via `setProperty`.

### 4.5 Mensajes de error y comentarios en español

Varios métodos tienen mensajes de excepción en español que dificultan la operación del sistema por equipos internacionales:

```java
// FileStoreImpl.java
throw new IllegalArgumentException("Imposible updatear para " + key);
throw new IllegalArgumentException("Imposible insertar para " + path);
log.warn("Imposible borrar para " + path);
```

También hay comentarios mixtos español/inglés en `SecurityFilter`, `BucketService` y `RateLimitingFilter`.

---

## 5. Problemas de mantenibilidad

### 5.1 Parámetros de rate limiting hardcodeados

```java
// BucketService.java:33-34
return Bucket.builder()
    .addLimit(limit -> limit.capacity(200).refillGreedy(100, Duration.ofMinutes(1))).build();
```

`200` (capacidad), `100` (refill), `1 minuto` (ventana) y `10 minutos` (expiración de IP) están hardcodeados. No hay forma de ajustarlos sin recompilar. Deberían ser `@ConfigProperty` bajo `app.rate-limit.*`.

### 5.2 Scores de riesgo hardcodeados en cada analyzer

| Analyzer | Score | Ubicación |
|---|---|---|
| XssAnalyzer | 5+5+5 = 15 | clase |
| SqlInjectionAnalyzer | 5 | clase |
| CommandInjectionAnalyzer | 5 | clase |
| PathTraversalAnalyzer | 5 | clase |
| RceAnalyzer | 5 | clase |
| HttpHeaderInjectionAnalyzer | 3+3+3 = 9 | clase |
| XmlInjectionAnalyzer | 4 | clase |
| LdapInjectionAnalyzer | 3 | clase |
| SpecialCharactersAnalyzer | 3 | clase |

No hay una tabla central que permita ver el sistema de scoring completo ni el umbral de bloqueo (10) en relación con los scores posibles. El score máximo acumulable por petición podría ser >50 si todos los analyzers disparan, pero el umbral de bloqueo inmediato debería ser documentado y configurable.

### 5.3 Parámetros de almacenamiento temporal hardcodeados

```java
// FileStoreImpl.java:38-39
private static final int tempCapacity = 1000;
private static final int minutesLifeTime = 60;
```

Sin `@ConfigProperty`. Cualquier ajuste operacional requiere recompilación.

### 5.4 `MigrationsManager` con credenciales sin validación

```java
// MigrationsManager.java:73-74
.principal(new NamePrincipal(config.username().orElse("")))
.credential(new SimplePassword(config.password().orElse("")))
```

Si `app.migration.username` o `app.migration.password` no están configurados, se usará cadena vacía como credencial. Esto debería al menos emitir un warning o fallar explícitamente en ausencia de configuración.

---

## 6. Scorecard del paquete bootstrap

| Área | Puntuación | Notas |
|---|---|---|
| Encapsulación | 9/10 | Ninguna clase externa importa bootstrap — excelente |
| Nomenclatura | 5/10 | 2 typos en nombres, mock en producción, archivo muerto |
| Seguridad | 4/10 | Body sin límite, IP spoofable, lógica de bloqueo incompleta |
| Calidad de código | 5/10 | System.err en producción, sentencia muerta, bugs en bucket |
| Mantenibilidad | 4/10 | 15+ valores hardcodeados sin configuración externalizable |
| Observabilidad | 8/10 | Telemetría completa, interceptores bien diseñados |

---

## 7. Resumen priorizado de mejoras

| Prioridad | Área | Problema | Cambio |
|---|---|---|---|
| **P0** | Seguridad | Body sin límite en SecurityFilter | Añadir `MAX_BODY_BYTES` (ej. 512KB) antes del análisis |
| **P0** | Seguridad | Lógica de bloqueo incompleta en SecurityFilter | Añadir `abortWith(FORBIDDEN)` cuando `riskScore >= umbral` |
| **P0** | Calidad | `System.err.println` en OtlpTracesResource | Reemplazar por `log.debug(...)` |
| **P0** | Nomenclatura | `MockFileReader` en producción | Mover a `src/test` o añadir `@IfBuildProperty(name="quarkus.profile", stringValue="dev")` |
| **P1** | Seguridad | IP Spoofing en BucketService | Validar `X-Forwarded-For` contra proxies de confianza configurados |
| **P1** | Seguridad | SQL concatenado en FileStoreImpl | Usar batch `PreparedStatement` con `?` para el DELETE IN |
| **P1** | Calidad | `BucketService.lastAccessTime` no se actualiza | Actualizar timestamp en cada `resolveBucket`, no solo en creación |
| **P1** | Calidad | `RateLimitingFilter` resuelve bucket dos veces | Guardar bucket en `requestContext.setProperty(...)` |
| **P1** | Calidad | Sentencia muerta en `LogsMapper:33` | Eliminar `SpanContext.create(null, null, null, null)` |
| **P1** | Nomenclatura | Typo `Analizer` → `Analyzer` en la interfaz | Renombrar `MaliciousInjectionRiskAnalizer` |
| **P1** | Nomenclatura | Typo `Refections` → `Reflections` | Renombrar clase y archivo |
| **P2** | Mantenibilidad | Rate limit hardcodeado | `@ConfigProperty` bajo `app.rate-limit.*` |
| **P2** | Mantenibilidad | Scores de riesgo hardcodeados | Centralizar en un mapa de configuración o constantes agrupadas |
| **P2** | Seguridad | Endpoints OTLP en API pública | Mover a management interface o añadir autenticación |
| **P2** | Calidad | Mensajes de error en español | Traducir a inglés |
| **P3** | Nomenclatura | Eliminar `OpenTelemetryConfig.java` | Borrar archivo completamente comentado |
| **P3** | Mantenibilidad | `tempCapacity` y `minutesLifeTime` en FileStoreImpl | `@ConfigProperty` bajo `app.store.*` |
| **P3** | Mantenibilidad | Credenciales vacías en MigrationsManager | Fallar explícitamente si password está vacío cuando se configura una URL custom |
