# Tarea 06-01 — Sistema de Webhooks

> **Fase:** 06 — Infraestructura Reactiva
> **Prioridad:** P2
> **Esfuerzo estimado:** Medio
> **Prerequisito:** Fases 02 y 05 (eventos de dominio publicados)

---

## Descripción

Permite a las aplicaciones cliente suscribirse a eventos de autenticación
en tiempo real mediante HTTP POST a una URL registrada. El payload está
firmado con HMAC-SHA256 para verificar la autenticidad.

---

## Eventos a publicar

| Evento | Cuándo |
|--------|--------|
| `user.created` | Nuevo usuario registrado |
| `user.updated` | Perfil o credenciales actualizados |
| `user.deleted` | Usuario eliminado |
| `user.login.success` | Login correcto |
| `user.login.failed` | Intento de login fallido |
| `user.logout` | Logout del usuario |
| `user.password.changed` | Contraseña cambiada |
| `user.mfa.enabled` | MFA activado |
| `user.mfa.disabled` | MFA desactivado |
| `token.issued` | Token emitido |
| `token.revoked` | Token revocado |
| `session.created` | Sesión creada |
| `session.revoked` | Sesión revocada |

---

## Pasos de implementación

### 1. Tablas

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-webhook-endpoint-table
CREATE TABLE access_webhook_endpoint (
  uid           VARCHAR(36)  NOT NULL,
  tenant_id     VARCHAR(36)  NOT NULL,
  url           TEXT         NOT NULL,
  secret_hash   VARCHAR(64)  NOT NULL COMMENT 'SHA-256 del secret HMAC',
  events        TEXT         NOT NULL COMMENT 'JSON array de event types',
  enabled       TINYINT(1)   DEFAULT 1,
  created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT PK_ACCESS_WEBHOOK_ENDPOINT PRIMARY KEY (uid),
  INDEX idx_webhook_tenant (tenant_id)
);

-- changeset phylax-dev:create-webhook-delivery-table
CREATE TABLE access_webhook_delivery (
  uid            VARCHAR(36)  NOT NULL,
  endpoint_uid   VARCHAR(36)  NOT NULL,
  event_type     VARCHAR(100) NOT NULL,
  payload_json   TEXT         NOT NULL,
  status         VARCHAR(20)  NOT NULL DEFAULT 'pending',
  attempts       INT          NOT NULL DEFAULT 0,
  next_attempt   TIMESTAMP    NULL,
  last_response  TEXT         NULL,
  created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT PK_ACCESS_WEBHOOK_DELIVERY PRIMARY KEY (uid),
  INDEX idx_webhook_delivery_status (status, next_attempt),
  INDEX idx_webhook_delivery_endpoint (endpoint_uid)
);
```

### 2. Dominio — `WebhookEndpoint` y `WebhookDelivery`

```java
public record WebhookEndpoint(
    UUID uid,
    String tenantId,
    String url,
    String secretHash,      // SHA-256 — para verificar, no para firmar
    Set<String> events,
    boolean enabled
) {}

public record WebhookDelivery(
    UUID uid,
    UUID endpointUid,
    String eventType,
    String payloadJson,
    DeliveryStatus status,
    int attempts,
    Optional<Instant> nextAttempt
) {
    public enum DeliveryStatus { PENDING, SUCCESS, FAILED, ABANDONED }
}
```

### 3. Dispatcher — `WebhookDispatcher`

Escucha CDI Events del dominio y encola entregas:

```java
@ApplicationScoped
public class WebhookDispatcher {

    @Inject
    WebhookEndpointGateway endpointGateway;

    @Inject
    WebhookDeliveryGateway deliveryGateway;

    @Inject
    Vertx vertx;

    public void onDomainEvent(@ObservesAsync Object domainEvent) {
        String eventType = resolveEventType(domainEvent);
        String tenantId  = resolveTenantId(domainEvent);
        String payload   = buildPayload(domainEvent);

        endpointGateway.findByTenantAndEvent(tenantId, eventType)
            .forEach(endpoint -> {
                WebhookDelivery delivery = WebhookDelivery.pending(endpoint.uid(), eventType, payload);
                deliveryGateway.save(delivery);
                // Intentar entrega inmediata de forma asíncrona
                dispatchAsync(endpoint, delivery);
            });
    }

    private void dispatchAsync(WebhookEndpoint endpoint, WebhookDelivery delivery) {
        String signature = computeSignature(delivery.payloadJson(), endpoint.secretHash());
        WebClient.create(vertx)
            .postAbs(endpoint.url())
            .putHeader("Content-Type", "application/json")
            .putHeader("X-Phylax-Event", delivery.eventType())
            .putHeader("X-Phylax-Signature", "sha256=" + signature)
            .putHeader("X-Phylax-Delivery", delivery.uid().toString())
            .sendBuffer(Buffer.buffer(delivery.payloadJson()))
            .subscribe().with(
                resp -> handleSuccess(delivery, resp),
                err  -> handleFailure(delivery, err)
            );
    }

    private String computeSignature(String payload, String secretHash) {
        // HMAC-SHA256 del payload con el secret (recuperado del hash si se almacena cifrado)
        // En producción: el secret se almacena cifrado con la clave maestra del tenant
    }
}
```

### 4. Reintentos exponenciales

```java
@Scheduled(every = "60s")
public void retryFailedDeliveries() {
    deliveryGateway.findPendingRetries(Instant.now())
        .forEach(delivery -> {
            WebhookEndpoint endpoint = endpointGateway.findByUid(delivery.endpointUid());
            dispatchAsync(endpoint, delivery);
        });
}

private void handleFailure(WebhookDelivery delivery, Throwable err) {
    int nextAttempt = delivery.attempts() + 1;
    if (nextAttempt >= 5) {
        deliveryGateway.abandon(delivery.uid());
    } else {
        // Backoff exponencial: 1m, 5m, 30m, 2h, 8h
        Duration delay = Duration.ofMinutes((long) Math.pow(5, nextAttempt - 1));
        deliveryGateway.scheduleRetry(delivery.uid(), Instant.now().plus(delay));
    }
}
```

### 5. Endpoints de gestión

```
GET    /api/access/webhooks                        # Listar endpoints
POST   /api/access/webhooks                        # Crear endpoint
PUT    /api/access/webhooks/{uid}                  # Actualizar
DELETE /api/access/webhooks/{uid}                  # Eliminar
GET    /api/access/webhooks/{uid}/deliveries       # Historial de entregas
POST   /api/access/webhooks/{uid}/test             # Enviar evento de prueba
```

### 6. Formato del payload

```json
{
  "id": "delivery-uuid",
  "type": "user.login.success",
  "tenant_id": "tenant-uuid",
  "created_at": "2026-04-13T10:00:00Z",
  "data": {
    "user_uid": "user-uuid",
    "email": "user@example.com",
    "ip": "1.2.3.4",
    "user_agent": "Mozilla/5.0..."
  }
}
```

### 7. Tests de integración

Usar WireMock como receptor de webhooks:

- Evento `user.login.success` → webhook recibido con firma válida ✓
- Firma HMAC incorrecta en el receptor → log de error ✓
- Endpoint no disponible → reintento exponencial programado ✓
- 5 fallos consecutivos → estado `ABANDONED` ✓
- `POST /api/access/webhooks/{uid}/test` → entrega de prueba ✓

---

## Criterios de aceptación

- [ ] Tablas `access_webhook_endpoint` y `access_webhook_delivery` con migraciones
- [ ] 13 tipos de eventos publicados desde el dominio
- [ ] Firma `X-Phylax-Signature: sha256=<HMAC>` en cada entrega
- [ ] Reintentos exponenciales: 5 intentos máximo
- [ ] Estado `ABANDONED` tras 5 fallos
- [ ] CRUD de endpoints + historial de entregas
- [ ] Endpoint de test (`/test`) funcional
- [ ] 5 tests de integración con WireMock
