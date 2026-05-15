# Technical Extension — Event Streaming via Server-Sent Events

> Extracted from OAUTH_PLAN.md (was PLAN-33). Not part of the OAuth/OIDC implementation scope.

---

## Description

Real-time event stream for admin dashboards and reactive applications using SSE.
Endpoint backed by the Vert.x Event Bus internal to Quarkus.

## Endpoint

```
GET /api/stream/events?topics=user.login,session.created
Authorization: Bearer <token_with_scope_stream:events>
Accept: text/event-stream
```

## Motivation

- Admin dashboards need live login/session data without polling
- Reduces load from high-frequency REST polling
- Reuses the same internal event bus as Webhooks (PLAN-27)

## Dependencies

- PLAN-27 (Webhooks) must be implemented first — it establishes the event taxonomy
  and the `DomainEventBus` internal bus that SSE consumes as a second subscriber
- Requires Quarkus RESTEasy Reactive + SmallRye Mutiny (already in stack)

## Implementation notes

- Vert.x Event Bus publishes to address `phylax.events.{tenantId}.{eventType}`
- SSE endpoint subscribes using `eventBus.consumer(address)` → `Multi<String>`
- Scope `stream:events` required on the bearer token
- Heartbeat every 30s to keep connections alive through proxies
- Connection limit per tenant to avoid resource exhaustion
