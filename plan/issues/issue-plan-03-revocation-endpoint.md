# Issue PLAN-03 — Token Revocation Endpoint (RFC 7009)

**OAUTH_PLAN:** PLAN-03  
**Wave:** 0 — Critical security gap

## Problem

The current `/revocation` endpoint only clears pre-session cookies, not JWT tokens.
Clients have no standards-compliant way to invalidate tokens on logout or key compromise.

## Current state

- `_oauth_revoked_jti` table and `TokenRevocationGateway` port exist (see PLAN-02).
- No `POST /revoke` REST driver in `tokensecurity/`.
- `JwtTokenBuilder.verify()` does not check `isRevoked(jti)` after signature verification.

## Implementation steps

### 1. Create `TokenRevocationController`

`features/oauth/tokensecurity/infrastructure/driver/rest/TokenRevocationController.java`

```
POST /{tenant}/oauth/revoke
Content-Type: application/x-www-form-urlencoded
Body: token=...&token_type_hint=refresh_token
```

Steps:
1. Authenticate the client (client_secret_basic or client_secret_post).
2. Parse the submitted `token` as a JWT — **catch all exceptions**: any parse failure → `200 OK`
   (RFC 7009 requires 200 even for invalid tokens; never reveal whether a token exists).
3. If valid JWT: extract `jti` and `exp`; call `TokenRevocationGateway.revokeToken(jti, exp)`.
4. Always respond `200 OK` with empty body.
5. Set `Cache-Control: no-store` on the response.

### 2. Check revocation in token verification

`features/oauth/tokensecurity/application/JwtTokenBuilder.java` (or `JoseTokenSigner`):

After successful signature validation, add:
```java
String jti = claims.getJti();
if (jti != null && revocationGateway.isRevoked(jti)) {
    throw new TokenRevokedException("token has been revoked");
}
```

This ensures revoked tokens are rejected at every endpoint that validates tokens
(userinfo, introspection, resource endpoints).

### 3. Update the discovery document

`features/oauth/oidc/infrastructure/driver/rest/OpenIdConfigurationController.java`:

```java
.revocationEndpoint(baseUrl + "/{tenant}/oauth/revoke")
.revocationEndpointAuthMethodsSupported(List.of("client_secret_basic", "client_secret_post"))
```

## Dependencies

- PLAN-02 must be complete (shared `TokenRevocationGateway`).
- PLAN-08 (Logout) will call `revokeAllForUser` — same gateway.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `tokensecurity/infrastructure/driver/rest/TokenRevocationController.java` |
| **Modify** | `tokensecurity/application/JwtTokenBuilder.java` — add `isRevoked` check post-verification |
| **Modify** | `oidc/.../OpenIdConfigurationController.java` — add revocation endpoint metadata |
