# Functional Extension — Admin Dashboard API

> Extracted from OAUTH_PLAN.md (was PLAN-39). Not part of the OAuth/OIDC implementation scope.

---

## Description

Administrative REST endpoints for tenant operators and support teams to monitor
and manage the platform without direct database access.

## Endpoints

### Tenant statistics
```
GET /api/admin/tenants/{uid}/stats
```
Response: `active_users`, `active_sessions`, `logins_last_24h`, `failed_logins_last_24h`,
`tokens_issued_last_24h`, `top_clients[]`. Cache with 5-minute TTL.

### User search
```
GET /api/admin/tenants/{uid}/users?q=email:juan&status=active&cursor=xxx&limit=50
```
Filters: `status` (active/disabled/pending_mfa), `has_mfa`, `created_after/before`,
`last_login_after/before`. Cursor-based pagination.

### Client list with usage stats
```
GET /api/admin/tenants/{uid}/clients
```

### Session and token management
```
DELETE /api/admin/tenants/{uid}/users/{uid}/sessions       -- revoke all sessions
DELETE /api/admin/tenants/{uid}/users/{uid}/sessions/{sid} -- revoke specific session
```

## Dependencies

- PLAN-15 (audit log) for statistics queries
- PLAN-32 (Active Sessions API) for session management operations
- PLAN-03 (Token Revocation) for token invalidation on session revoke
- Requires Waves 0–2 to be complete so underlying data is populated
- No direct dependency on the OAuth protocol itself
