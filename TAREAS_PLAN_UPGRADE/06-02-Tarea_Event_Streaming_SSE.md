# Tarea 06-02 — Event Streaming — SSE con Mutiny / Vert.x

> **Fase:** 06 — Infraestructura Reactiva
> **Prioridad:** P4
> **Esfuerzo estimado:** Alto
> **Prerequisito:** 06-01 (Webhooks — mismo bus de eventos)

---

## Descripción

Endpoint Server-Sent Events (SSE) que permite a dashboards de administración
y aplicaciones reactivas recibir eventos en tiempo real sin polling.
La implementación usa SmallRye Reactive Messaging con Vert.x Event Bus
como backend en memoria.

---

## Endpoint

```
GET /api/stream/events?topics=user.login,session.created
Authorization: Bearer <token_con_scope_stream:events>
Accept: text/event-stream
```

---

## Pasos de implementación

### 1. Bus de eventos Vert.x interno

En Quarkus, el Vert.x Event Bus está disponible directamente.
Publicar eventos desde el dispatcher de dominio:

```java
@ApplicationScoped
public class DomainEventBus {

    @Inject
    io.vertx.mutiny.core.eventbus.EventBus eventBus;

    public void publish(String tenantId, String eventType, Object payload) {
        String address = "phylax.events." + tenantId + "." + eventType;
        eventBus.publish(address, Json.encode(payload));
    }
}
```

### 2. Endpoint SSE con RESTEasy Reactive

```java
@Path("/api/stream/events")
@ApplicationScoped
public class EventStreamController {

    @Inject
    io.vertx.mutiny.core.eventbus.EventBus eventBus;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    @Operation(summary = "Real-time event stream (SSE)")
    @APIResponse(responseCode = "200", description = "SSE stream — one JSON event per line")
    public Multi<String> stream(
        @QueryParam("topics") String topics,
        @Context SecurityContext security
    ) {
        String tenantId = extractTenantId(security);
        Set<String> requestedTopics = parseTopics(topics);

        // Verificar que el token tiene scope 'stream:events'
        requireScope(security, "stream:events");

        // Crear un Multi que escucha múltiples addresses del Event Bus
        return Multi.createBy().merging().streams(
            requestedTopics.stream()
                .map(topic -> eventBus.<String>consumer("phylax.events." + tenantId + "." + topic).toMulti()
                    .map(Message::body))
                .collect(Collectors.toList())
        );
    }
}
```

### 3. Heartbeat para mantener conexiones vivas

```java
// Añadir un keepalive cada 30s para conexiones idle
Multi<String> keepalive = Multi.createFrom().ticks().every(Duration.ofSeconds(30))
    .map(tick -> ": keepalive\n\n");

return Multi.createBy().merging().streams(eventStream, keepalive);
```

### 4. Aislamiento por tenant

El address del Event Bus incluye `tenantId`:
```
phylax.events.{tenantId}.{eventType}
```

Esto garantiza que un tenant solo recibe sus propios eventos,
incluso si comparten el mismo proceso Quarkus.

### 5. Alternativa con fan-out por tenant (escalado)

Para múltiples instancias del servidor, sustituir el Vert.x Event Bus local
por SmallRye Reactive Messaging con AMQP/Kafka:

```java
// Opcional — solo si hay múltiples instancias
@Incoming("auth-events")
public void consumeFromBroker(AuthEvent event) {
    // Fan-out a los subscribers SSE del tenant correspondiente
}
```

La implementación local con Vert.x Event Bus es suficiente para instancia única.

### 6. Tests

Los tests SSE requieren un cliente HTTP que soporte `text/event-stream`.
Usar el cliente Vert.x WebClient:

```java
@QuarkusTest
class EventStreamTest {
    @Test
    void sseStream_emitsLoginEvent() {
        // 1. Conectar al SSE endpoint
        // 2. Disparar un login
        // 3. Verificar que llega el evento user.login.success
    }
}
```

---

## Criterios de aceptación

- [ ] `GET /api/stream/events` devuelve `text/event-stream`
- [ ] Solo tenants del token reciben los eventos (aislamiento)
- [ ] Scope `stream:events` requerido
- [ ] Filtrado por `topics` funcional
- [ ] Heartbeat cada 30s para mantener conexión
- [ ] Documentado con `@Operation` / `@APIResponse`
- [ ] Test de integración que verifica recepción de evento post-login
