# Issue PLAN-32 — Active Sessions Management API

**OAUTH_PLAN:** PLAN-32  
**Wave:** 4 — Deferred

## Problem

Users have no way to see or revoke their active sessions from other devices.
This is a standard security feature ("Sign out from other devices").

## Current state

- `_oauth_session` table has `user_uid VARCHAR(36) NULL` column (added in model section 2.1) ✅.
- `_oauth_session` has `ip_address`, `user_agent`, `client_name`, `last_used_at` ✅.
- Sessions are not queryable by user without a full table scan (hence `user_uid` column).

## Prerequisite

`user_uid` must be populated when sessions are created. Verify the session creation code
sets `user_uid` from the authenticated user's UID. If not, add it to the session bootstrap
in `AuthorizeHtml` after authentication succeeds.

## Implementation steps

### 1. REST endpoints

```
GET    /api/{tenant}/access/users/{userUid}/sessions
DELETE /api/{tenant}/access/users/{userUid}/sessions/{sessionId}
DELETE /api/{tenant}/access/users/{userUid}/sessions
```

Authorization: the user can only manage their own sessions. An admin can manage any user's sessions.

### 2. `GET` — list active sessions

Query `_oauth_session WHERE user_uid = ? AND expiration > NOW() AND revoked_at IS NULL`
ordered by `last_used_at DESC`.

Response per session:
```json
{
  "sessionId": "...",
  "clientName": "My App",
  "ipAddress": "1.2.3.4",
  "userAgent": "Mozilla/5.0...",
  "createdAt": "...",
  "lastUsedAt": "...",
  "expiresAt": "...",
  "current": true   // true if this is the session from the current request
}
```

Mark the current session (`AUTH_SESSION_ID` cookie value) as `"current": true`
so the UI can prevent self-revocation.

### 3. `DELETE /{sessionId}` — revoke a specific session

1. Load the session; verify it belongs to `userUid`.
2. Set `revoked_at = NOW()` on the `_oauth_session` row.
3. Call `TokenRevocationGateway.revokeAllForSession(sessionId)` — revoke all JTIs issued
   for this session (query `_oauth_session_token WHERE session = ?`).
4. Prevent revoking the current session from the API (use `204` but do nothing, or `400`).

### 4. `DELETE` — revoke all sessions

Revoke all sessions for the user **except the current one** (keeping the user's own
session alive prevents them from locking themselves out immediately).

Loop: for each non-current session, call the single-session revocation logic.

### 5. Profile panel integration

Add a "Active Sessions" panel to the profile area:
- Lists all sessions from the `GET` endpoint.
- Each row has a "Revoke" button.
- "Sign out everywhere" button calls `DELETE` all.

## Dependencies

- PLAN-03 (token revocation) — `revokeAllForSession` requires writing to `_oauth_revoked_jti`.
- `user_uid` populated at session creation — verify this happens.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `oauth/session/infrastructure/driver/rest/UserSessionController.java` |
| **Modify** | `oauth/tokensecurity/domain/gateway/TokenRevocationGateway.java` — add `revokeAllForSession` |
| **Modify** | `oauth/authentication/.../AuthorizeHtml.java` — set `user_uid` on session creation |
| **Modify** | `profile/` — add active sessions panel |
