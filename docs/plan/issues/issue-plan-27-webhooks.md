# Issue PLAN-27 — Webhooks for Authentication Events

**OAUTH_PLAN:** PLAN-27  
**Wave:** 4 — Deferred

## Problem

No outbound HTTP notification mechanism for authentication events. Tenants cannot integrate
their own systems with `user.login`, `token.issued`, or `token.revoked` events.

## Current state

- `OidcEventDispatcher` fires CDI events internally ✅.
- PLAN-15 (AuditGateway) establishes the event taxonomy — complete PLAN-15 first.
- No webhook endpoint table or delivery mechanism exists.

## New SQL tables needed

```sql
CREATE TABLE access_webhook_endpoint (
    uid         VARCHAR(36)  NOT NULL,
    tenant_id   VARCHAR(36)  NOT NULL,
    url         VARCHAR(512) NOT NULL,
    secret      VARCHAR(128) NOT NULL,   -- HMAC-SHA256 signing secret (stored hashed or encrypted)
    events_json TEXT         NOT NULL,   -- JSON array of event types to subscribe to
    enabled     BIT          NOT NULL DEFAULT 1,
    created_at  DATETIME     NOT NULL DEFAULT NOW(),
    CONSTRAINT PK_WEBHOOK_ENDPOINT PRIMARY KEY (uid),
    INDEX idx_webhook_tenant (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE access_webhook_delivery (
    uid          VARCHAR(36)  NOT NULL,
    endpoint_uid VARCHAR(36)  NOT NULL,
    event_type   VARCHAR(64)  NOT NULL,
    payload_json MEDIUMTEXT   NOT NULL,
    status       VARCHAR(16)  NOT NULL,  -- PENDING, SUCCESS, FAILED
    attempt      INT          NOT NULL DEFAULT 1,
    last_attempt DATETIME     NOT NULL,
    next_attempt DATETIME     NULL,
    response_code INT         NULL,
    CONSTRAINT PK_WEBHOOK_DELIVERY PRIMARY KEY (uid),
    INDEX idx_webhook_delivery_next (next_attempt, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

## Implementation steps

### 1. Webhook endpoint management (Access BC)

New entity `webhook-endpoint` in `features/access/` (or managed via admin API).
CRUD operations to register/update/delete webhook subscriptions per tenant.

### 2. `WebhookDispatcher` (driven by AuditGateway events)

`features/oauth/audit/application/WebhookDispatcher.java`:

- Subscribes to CDI events from `OidcEventDispatcher` (or listens on `AuditGateway` calls).
- Filters events by the tenant's registered subscriptions.
- Builds the payload: `{"event": "user.login", "timestamp": "...", "data": {...}}`.
- Signs with HMAC-SHA256: `X-Signature: sha256={hex(HMAC(secret, payload))}`.
- HTTP POST to each matching endpoint URL (async, Quarkus Vert.x HTTP client).
- Records delivery attempt in `access_webhook_delivery`.

### 3. Retry scheduler

A `@Scheduled` job that picks up `FAILED` delivery records with `next_attempt < NOW()`:
- Exponential backoff: 1min, 5min, 30min, 2h, 6h. After 6h give up (mark `ABANDONED`).
- Maximum 5 attempts.

### 4. Webhook endpoint configuration UI

Admin API endpoints:
```
POST   /api/{tenant}/admin/webhooks          — register endpoint
GET    /api/{tenant}/admin/webhooks          — list
DELETE /api/{tenant}/admin/webhooks/{uid}    — delete
POST   /api/{tenant}/admin/webhooks/{uid}/test — send test event
```

## Dependencies

- PLAN-15 (AuditGateway) — event taxonomy must be established first.

## Note

This is a full sub-project. The SQL tables in this issue need to be added to `OAUTH_MODEL.md`
(or a separate model document) before implementation starts.
