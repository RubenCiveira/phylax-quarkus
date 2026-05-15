# Issue 1.8 — Scope consent tracking — application layer and OAuth wiring

**Model section:** 1.8  
**OAUTH_PLAN:** PLAN-31 (Scope Consent Tracking)  
**Wave:** 2

## Current state

- `access_user_consented_scopes` table exists in DB ✅.
- The actual generated table has a richer schema than the plan originally described:
  `uid`, `user`, `trusted_client`, `scope`, `granted BIT`, `decision_at`, `ip_address`, `user_agent`.
- Domain: `UserConsentedScopes` entity and `UserConsentedScopesFilter` (with `user`, `trustedClient`
  filter fields) exist ✅.
- Domain events: `UserConsentedScopesCreateEvent`, `UserConsentedScopesDeleteEvent` ✅.
- Infrastructure: `UserConsentedScopesReadRepositoryGateway`, `UserConsentedScopesWriteRepositoryGateway` ✅.
- **No application use cases** — `features/access/userconsentedscopes/application/` does not exist.
- OAuth adapter `ScopesConsentAdapter` has scaffolding with commented-out use case calls:
  `// private final CheckScopeConsentUseCase checkConsent;`
  `// private final GrantScopeConsentUseCase grantConsent;`

## What to implement

### 1. `CheckScopeConsentUseCase`

`features/access/userconsentedscopes/application/usecase/check/CheckScopeConsentUseCase.java`

- Input: `userUid (String)`, `clientUid (String)`, `requestedScopes (List<String>)`.
- Steps:
  1. Load all `UserConsentedScopes` records for `user=userUid` AND `trustedClient=clientUid`
     via `UserConsentedScopesReadRepositoryGateway` (use `UserConsentedScopesFilter.builder().user(userRef).trustedClient(clientRef).build()`).
  2. Filter to those with `granted=true` and no `usedAt`/revocation flag.
  3. Return the subset of `requestedScopes` that have **no** matching granted record
     (i.e., the scopes still requiring consent).

### 2. `GrantScopeConsentUseCase`

`features/access/userconsentedscopes/application/usecase/grant/GrantScopeConsentUseCase.java`

- Input: `userUid`, `clientUid`, `scope`, `ipAddress (optional)`, `userAgent (optional)`.
- Steps:
  1. Check if a record already exists for `(user, trustedClient, scope)` — idempotent.
  2. If not: create a `UserConsentedScopes` via `UserConsentedScopes.create(changeset)` with
     `granted=true`, `decisionAt=now()`, `ipAddress`, `userAgent`.
  3. Persist via `UserConsentedScopesWriteRepositoryGateway`.
- If a record exists with `granted=false` (previously denied and stored): update it to `granted=true`.

### 3. `RevokeScopeConsentUseCase`

`features/access/userconsentedscopes/application/usecase/revoke/RevokeScopeConsentUseCase.java`

- Input: `userUid`, `clientUid`, optional `scope` (if null, revoke all scopes for the client).
- Steps: load matching records, call `record.delete()`, persist deletions.
- Called from the GDPR Consent Management Page (PLAN-33).

### 4. Wire into `ScopesConsentAdapter`

`features/oauth/consent/infrastructure/driven/ScopesConsentAdapter.java` — uncomment and implement:

```java
private final CheckScopeConsentUseCase checkConsent;
private final GrantScopeConsentUseCase grantConsent;
```

**`pendingScopes`:**
```java
// After resolving user UID and client UID:
List<String> pending = checkConsent.execute(user.getUid(), client.getUid(), scopes);
return toPermissions(pending);
```

**`storeAcceptedScopes`:**
```java
// After resolving user UID and client UID:
scopes.forEach(scope ->
    grantConsent.execute(user.getUid(), client.getUid(), scope, requestIp, requestUserAgent));
```

Pass `ip_address` and `user_agent` from the HTTP context if available — useful for GDPR audit.

### 5. UserRef and TrustedClientRef resolution

The filter uses `UserRef` and `TrustedClientRef` domain references, not raw strings.
`ScopesConsentAdapter` already resolves `username → User` and `clientId → TrustedClient`.
Pass `user` and `client` as refs directly into the filter:

```java
UserConsentedScopesFilter.builder()
    .user(user)            // UserRef
    .trustedClient(client) // TrustedClientRef
    .build()
```

## Scope of this issue vs PLAN-33

This issue covers the persistence layer and OAuth flow integration.  
PLAN-33 (GDPR Consent Management Page) uses `RevokeScopeConsentUseCase` from this issue
as its prerequisite — create the use case here even if the HTML driver comes later.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `access/userconsentedscopes/application/usecase/check/CheckScopeConsentUseCase.java` |
| **Create** | `access/userconsentedscopes/application/usecase/grant/GrantScopeConsentUseCase.java` |
| **Create** | `access/userconsentedscopes/application/usecase/revoke/RevokeScopeConsentUseCase.java` |
| **Modify** | `oauth/consent/infrastructure/driven/ScopesConsentAdapter.java` — uncomment + wire use cases |
