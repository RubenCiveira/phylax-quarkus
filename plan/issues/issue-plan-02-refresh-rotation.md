# Issue PLAN-02 — Refresh Token Invalidation on Rotation

**OAUTH_PLAN:** PLAN-02  
**Wave:** 0 — Critical security gap

## Problem

`RefreshGranter` issues a new refresh token but does **not** revoke the old one.
Any previously issued refresh token remains valid indefinitely, enabling replay attacks.

## Current state

- `_oauth_revoked_jti` table exists ✅.
- `TokenRevocationGateway` port exists (verify if `revokeToken` and `isRevoked` are declared).
- `RefreshGranter` issues new tokens without touching the revocation gateway.

## Implementation steps

### 1. Verify `TokenRevocationGateway` has the required operations

`features/oauth/tokensecurity/domain/gateway/TokenRevocationGateway.java`:

```java
void revokeToken(String jti, Instant expiresAt);
boolean isRevoked(String jti);
void revokeAllForUser(String userId, String clientId);
```

If any are missing, add them. Verify the SQL adapter writes to `_oauth_revoked_jti`.

### 2. Revoke the old refresh token in `RefreshGranter`

`features/oauth/authentication/application/granter/RefreshGranter.java`:

```java
// 1. Before issuing new tokens: check if incoming refresh JTI is already revoked
String incomingJti = jwtBuilder.extractJti(refreshToken);
if (revocationGateway.isRevoked(incomingJti)) {
    // Theft detected — revoke all tokens for this user/client pair
    revocationGateway.revokeAllForUser(userId, clientId);
    throw new OAuthException("invalid_grant", "refresh token already used");
}

// 2. Issue new access + refresh tokens (existing logic)
TokenPair newTokens = jwtBuilder.buildTokenPair(...);

// 3. Revoke the old refresh token now that a new one has been issued
Instant oldExp = jwtBuilder.extractExpiry(refreshToken);
revocationGateway.revokeToken(incomingJti, oldExp);
```

### 3. Verify `TokenStoreSqlAdapter` persistence

`features/oauth/tokensecurity/infrastructure/driven/TokenStoreSqlAdapter.java`:
- `revokeToken` must INSERT into `_oauth_revoked_jti` with `token_type='refresh'`.
- `isRevoked` must do a fast indexed lookup on `jti` — confirm the index exists on the table.
- `revokeAllForUser` must revoke all non-expired refresh tokens for the `(userId, clientId)` pair,
  either by querying `_oauth_session_token` for matching JTIs or by a direct SQL operation.

## Dependencies

- None (Wave 0 — no upstream dependency).
- PLAN-03 depends on this being complete first.

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `tokensecurity/domain/gateway/TokenRevocationGateway.java` — verify/add operations |
| **Modify** | `authentication/application/granter/RefreshGranter.java` — revoke old JTI + theft detection |
| **Modify** | `tokensecurity/infrastructure/driven/TokenStoreSqlAdapter.java` — verify persistence |
