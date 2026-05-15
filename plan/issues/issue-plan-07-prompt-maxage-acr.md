# Issue PLAN-07 — prompt / max_age / login_hint / acr_values enforcement

**OAUTH_PLAN:** PLAN-07  
**Wave:** 1

## Problem

`AuthRequest` parses `prompt`, `max_age`, `login_hint`, and `acr_values` but none of them
are acted upon in the step router. OIDC Core 1.0 §3.1.2.1 requires these to be enforced.

## Current state

- `AuthRequest` object carries the parameters ✅.
- `OidcStepRouter` / `AuthorizeHtml` does not branch based on them.
- `SessionInfo` does not store `authTime` (real password/MFA timestamp).

## Implementation steps

### 1. Add `authTime` to `SessionInfo`

`features/oauth/session/domain/SessionInfo.java`:

```java
private Instant authTime;   // set when password or MFA step completes
```

Set this in the step handler that completes authentication (password step or MFA step),
**not** at session creation time (which is earlier).

### 2. `prompt` enforcement

`features/oauth/authentication/infrastructure/driver/html/AuthorizeHtml.java` at session load:

```java
switch (authRequest.getPrompt()) {
    case "login"         -> invalidate SSO session, force re-authentication
    case "consent"       -> force consent step even if already accepted (set flag on AuthRequest)
    case "none"          -> if valid SSO session: skip to code issuance; else redirect error:
                            login_required or interaction_required; NEVER render any UI
    case "select_account"-> treat as "login" for now (log unimplemented)
}
```

For `prompt=none`: this must be enforced **before** any HTML is written to the response.

### 3. `max_age` enforcement

After loading the SSO session:
```java
int maxAge = authRequest.getMaxAge();  // -1 if not specified
if (maxAge >= 0 && session.getAuthTime() != null) {
    if (session.getAuthTime().plusSeconds(maxAge).isBefore(Instant.now())) {
        // Auth time exceeded — invalidate session and force re-authentication
        invalidateSession();
    }
}
```

Coordinate with Issue 1.5 (PLAN-09): the tenant-level `sessionSsoTtlSeconds` is an upper bound
that applies even when `max_age` is not specified.

### 4. `login_hint` propagation

Pass `authRequest.getLoginHint()` as a pre-filled `username` value in the login form template
model. The template should render it as `<input name="username" value="{loginHint}">`.

### 5. `acr_values` step-up

Parse `acr_values` (space-separated preference list, e.g., `"2 1"`).
Extract the **minimum required ACR** (highest numeric value in the list).

After loading an existing SSO session:
```java
int requiredAcr = authRequest.getMinRequiredAcr();  // parse from acr_values
int sessionAcr  = session.getAcr();                 // 0=cookie, 1=password, 2=MFA

if (sessionAcr < requiredAcr) {
    // Inject the missing step(s) without re-login:
    // requiredAcr=1 but sessionAcr=0 → add password step
    // requiredAcr=2 but sessionAcr=1 → add MFA step only
    injectStepUpChallenges(requiredAcr, sessionAcr);
}
```

Store the required ACR in `SessionInfo.ChallengesState` so it survives across step redirects.

### 6. `auth_time` in ID token

`features/oauth/tokensecurity/application/JwtTokenBuilder.java`:
- Include `auth_time: session.getAuthTime().getEpochSecond()` in ID token claims when `max_age`
  was specified in the request or when `session.getAuthTime()` is set.

## Dependencies

- Issue 1.5 (PLAN-09 / tenant config) — `sessionSsoTtlSeconds` interacts with `max_age`.
- PLAN-09 (SSO) — must separate SSO cookie from per-flow cookie before this works correctly.
- `_oauth_session.acr` column — already in DB after model section 2.1 migration.

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `session/domain/SessionInfo.java` — add `authTime`, `acr` |
| **Modify** | `authentication/.../AuthorizeHtml.java` — prompt/max_age/acr enforcement |
| **Modify** | `authentication/.../OidcStepRouter.java` — step-up injection |
| **Modify** | `tokensecurity/application/JwtTokenBuilder.java` — `auth_time` claim |
| **Modify** | Login/MFA step handlers — set `session.authTime` on completion |
