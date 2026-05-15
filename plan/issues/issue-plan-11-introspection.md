# Issue PLAN-11 — Introspection Endpoint Complete (RFC 7662)

**OAUTH_PLAN:** PLAN-11  
**Wave:** 2

## Problem

`IntrospectionController` exists but may return 403. No caller authentication,
no `active: false` fallback, no revocation check.

## Current state

- `IntrospectionController` exists in `tokensecurity/` ✅.
- `is_resource_server` model field exists in DB ✅ (see Issue 1.3 — `ClientDetails` needs the field).
- Correct `active` response requires PLAN-03 (`isRevoked` check).

## Implementation steps

### 1. Authenticate the caller

The introspection endpoint may only be called by clients with `isResourceServer=true`
(Issue 1.3 must be complete). Use the same client authentication helper as `TokenController`:
- `client_secret_basic` (Authorization header) or `client_secret_post` (form body).
- If client authentication fails: `401 Unauthorized`.
- If client is not a resource server: `403 Forbidden`.

### 2. Inspect the submitted token

```java
try {
    Claims claims = jwtBuilder.verify(submittedToken);  // includes revocation check (PLAN-03)
    // Build active response:
    return Response.ok(Map.of(
        "active",     true,
        "sub",        claims.getSubject(),
        "client_id",  claims.get("client_id"),
        "scope",      claims.get("scope"),
        "exp",        claims.getExpiration().getTime() / 1000,
        "iat",        claims.getIssuedAt().getTime() / 1000,
        "iss",        claims.getIssuer(),
        "jti",        claims.getId(),
        "token_type", "Bearer"
    )).build();
} catch (Exception e) {
    return Response.ok(Map.of("active", false)).build();
}
```

All exceptions (parse error, expired, revoked) → `{"active": false}`.

### 3. Response headers

```
Content-Type: application/json
Cache-Control: no-store
Pragma: no-cache
```

### 4. Update discovery document

Confirm `introspectionEndpoint` is set and points to the correct path.
Add `introspectionEndpointAuthMethodsSupported`.

## Dependencies

- Issue 1.3 (`ClientDetails.resourceServer` flag).
- PLAN-03 (revocation check in `jwtBuilder.verify()`).

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `tokensecurity/infrastructure/driver/rest/IntrospectionController.java` — full impl |
| **Modify** | `oidc/.../OpenIdConfigurationController.java` — introspection endpoint metadata |
