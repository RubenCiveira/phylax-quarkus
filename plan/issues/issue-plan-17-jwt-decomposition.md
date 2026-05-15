# Issue PLAN-17 — `JwtTokenBuilder` Decomposition

**OAUTH_PLAN:** PLAN-17  
**Wave:** 3 — Technical debt

## Problem

`JwtTokenBuilder` is a ~500-line god class combining token building, claim mapping,
PKCE helpers, and token verification. Every change to JWT logic risks regressions across
all grant types because everything is entangled.

## Approach

Refactor-only change — no functional changes. Implement behind a separate branch.
No new tests needed beyond ensuring existing tests still pass.

## Decomposition plan

### 1. Extract `ClaimsMapper`

`features/oauth/tokensecurity/application/ClaimsMapper.java`

Responsibilities:
- Map user data + scopes + client config → claim `Map<String, Object>`.
- Scope-to-claim logic: `profile` → `name`, `given_name`, etc.; `email` → `email`, `email_verified`.
- Standard claims: `iss`, `aud`, `sub`, `iat`, `exp`, `jti`.
- Custom claims: `acr`, `auth_time`, `nonce`, `client_id`.

All claim building that currently lives in `JwtTokenBuilder` moves here.

### 2. Extract `TokenVerifier`

`features/oauth/tokensecurity/application/TokenVerifier.java`

Responsibilities:
- Wraps the `JoseTokenSigner` (or equivalent) for signature verification.
- Checks `exp` (not expired).
- Checks `isRevoked(jti)` via `TokenRevocationGateway` (required by PLAN-03).
- Returns parsed `Claims` or throws a typed exception.

The verification path that currently lives in `JwtTokenBuilder.verify()` moves here.

### 3. Keep `JwtTokenBuilder` as a thin facade

After extraction, `JwtTokenBuilder` becomes:
```java
@ApplicationScoped
public class JwtTokenBuilder {
    private final ClaimsMapper claimsMapper;
    private final TokenVerifier tokenVerifier;
    private final JoseTokenSigner signer;

    public TokenPair buildTokenPair(AuthenticationData data, ClientDetails client, List<String> scopes) {
        Map<String, Object> claims = claimsMapper.map(data, client, scopes);
        return signer.sign(claims);
    }

    public Claims verify(String token) {
        return tokenVerifier.verify(token);
    }
}
```

### 4. Remove PKCE helpers from `JwtTokenBuilder`

PKCE helper methods (S256 computation, verifier comparison) belong in a utility class or in
the `TemporalAuthCode` domain, not in the token builder. Move them to:
`features/oauth/authentication/domain/PkceVerifier.java` (static utility).

## Acceptance criteria

- All existing tests pass unchanged.
- `JwtTokenBuilder` has ≤ 80 lines.
- `ClaimsMapper` is independently unit-testable without standing up a CDI container.
- `TokenVerifier` is independently unit-testable with a mocked `TokenRevocationGateway`.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `tokensecurity/application/ClaimsMapper.java` |
| **Create** | `tokensecurity/application/TokenVerifier.java` |
| **Create** | `authentication/domain/PkceVerifier.java` |
| **Modify** | `tokensecurity/application/JwtTokenBuilder.java` — thin facade only |
