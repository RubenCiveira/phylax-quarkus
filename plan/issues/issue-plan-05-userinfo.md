# Issue PLAN-05 — Userinfo Endpoint — Full Scope-Driven Claim Mapping

**OAUTH_PLAN:** PLAN-05  
**Wave:** 1

## Problem

`InformationController` (or equivalent) returns only `sub`, `name`, `issuer`.
It does not filter by scope, does not return standard OIDC profile/email/phone claims,
and does not verify token revocation status.

## Current state

- Endpoint exists at some path under `authentication/infrastructure/driver/rest/` or `profile/`.
- Token signature verification is likely present but no revocation check.
- No scope-to-claim mapping logic.
- `ProfileGateway` likely has user data but the mapping to OIDC claim names is missing.

## Implementation steps

### 1. Verify and validate the incoming access token

`InformationController.java`:
1. Extract Bearer token from `Authorization` header.
2. Call `JwtTokenBuilder.verify(token)` — which after PLAN-03 will also check revocation.
3. On invalid/expired/revoked: respond `401 WWW-Authenticate: Bearer error="invalid_token"`.
4. Extract `sub`, `scope`, `client_id` from verified token claims.

### 2. Implement scope-to-claim mapping

```java
Map<String, Object> claims = new LinkedHashMap<>();
claims.put("sub", sub);  // always

if (scopes.contains("profile")) {
    claims.put("name",               user.getName());
    claims.put("given_name",         user.getGivenName());   // if modeled
    claims.put("family_name",        user.getFamilyName());  // if modeled
    claims.put("preferred_username", user.getEmail());
    claims.put("updated_at",         user.getUpdatedAt());   // if modeled
}
if (scopes.contains("email")) {
    claims.put("email",          user.getEmail());
    claims.put("email_verified", user.getEmailVerified());   // requires PLAN-10/Issue 1.1
}
if (scopes.contains("phone")) {
    // Only if phone is modeled; skip if not present
}
```

### 3. Load user data via `LoginGateway` or `ProfileGateway`

Whichever gateway already returns the full user row (avoid adding a new gateway if one exists).
Load by `sub` (which is the user UID in this system).

### 4. Align with `ProfileMeController`

`profile/.../ProfileMeController.java` returns similar data — ensure `sub` in the userinfo
response matches `sub` in the ID token (same UID). Check for claim name mismatches
(OIDC spec requires `sub`, not `uid` or `id`).

### 5. Response format

```
GET /{tenant}/oidc/userinfo
Authorization: Bearer {access_token}

200 OK
Content-Type: application/json
Cache-Control: no-store
```

## Dependencies

- PLAN-03 (revocation check in token verification).
- Issue 1.1 (email-verified) for the `email_verified` claim.

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `authentication/infrastructure/driver/rest/InformationController.java` (or equivalent) — full impl |
| **Modify** | `tokensecurity/application/JwtTokenBuilder.java` — expose `email_verified` claim in ID token |
