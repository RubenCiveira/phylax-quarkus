# Issue PLAN-09 — Session SSO (Cross-Client Single Sign-On)

**OAUTH_PLAN:** PLAN-09  
**Wave:** 1

## Problem

The session is verified per-flow but not reused cross-client. A user who has already authenticated
with client A is forced to re-enter credentials when client B initiates `/authorize`.

## Current state

- A single `AUTH_SESSION_ID` cookie is used for both per-flow state and SSO identity.
- `SessionInfo` does not track `authTime` or the set of clients that have used this SSO session.
- `_oauth_session.sso_clients_json` and `auth_time` columns exist in DB ✅.

## Implementation steps

### 1. Separate SSO cookie from per-flow cookie

Currently one cookie (`AUTH_SESSION_ID`) stores both the per-flow authorization request state
and the SSO identity. These have different lifetimes and scopes:

- **SSO cookie** (`PHYLAX_SSO`): long-lived (`sessionSsoTtlSeconds`), cross-client, set after
  password/MFA completes. Points to the `_oauth_session.session` PK.
- **Per-flow cookie** (`AUTH_SESSION_ID`): short-lived (authorization code lifetime), scoped to a
  single `/authorize` request. Can be deleted once the code is issued.

Implement the split in `OidcCookieManager` (or equivalent):
- On first successful login: set both cookies.
- On subsequent `/authorize` from a different client: read `PHYLAX_SSO`, skip login steps.
- On logout: delete both.

### 2. Add `authTime` and SSO client tracking to `SessionInfo`

`features/oauth/session/domain/SessionInfo.java`:
```java
private Instant authTime;              // real password/MFA timestamp
private List<String> ssoClientIds;     // clients that have used this SSO session
```

These map to `_oauth_session.auth_time` and `_oauth_session.sso_clients_json`.

Persist `ssoClientIds` as a JSON array in `sso_clients_json`.
The session adapter must serialize/deserialize this field.

### 3. SSO session reuse in `AuthorizeHtml`

When a new `/authorize` request arrives:
1. Read `PHYLAX_SSO` cookie → load `SessionInfo`.
2. Validate: not expired, not revoked, `prompt` allows reuse (see PLAN-07).
3. If `max_age` is specified: check `authTime + max_age > now()` (see PLAN-07).
4. If valid: skip login/MFA steps, add `clientId` to `ssoClientIds`, proceed to consent checks.
5. On code issuance: update `sso_clients_json` in the session row (for back-channel logout fanout).

### 4. Configure SSO TTL from `tenantConfig`

Use `tenantConfig.sessionSsoTtlSeconds` (Issue 1.5) as the cookie `Max-Age` and session
expiry for the SSO session. Do not hardcode a value.

### 5. Back-channel logout uses `sso_clients_json`

On logout (PLAN-08), the dispatcher reads `session.getSsoClientIds()` to know which RPs to notify.

## Dependencies

- Issue 1.5 (tenant config) — `sessionSsoTtlSeconds`.
- PLAN-07 (`max_age` / `prompt`) — interacts with SSO session validation.
- PLAN-08 (logout) — uses `ssoClientIds` for fanout.
- `_oauth_session.auth_time` and `sso_clients_json` columns — already in DB ✅.

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `session/domain/SessionInfo.java` — add `authTime`, `ssoClientIds` |
| **Modify** | `authentication/.../OidcCookieManager.java` — split SSO / per-flow cookies |
| **Modify** | `authentication/.../AuthorizeHtml.java` — SSO session reuse logic |
| **Modify** | session SQL adapter — serialize `sso_clients_json` |
