# Issue 1.8 — Scope consent tracking — OAuth wiring

**Model section:** 1.8  
**OAUTH_PLAN:** PLAN-31 (Scope Consent Tracking)  
**Wave:** 2

## Architectural constraint

> No se crean nuevos casos de uso en `features/access/userconsentedscopes/`. La lógica
> va directamente en `features/oauth/consent/` usando los gateways de Access. Si se
> necesita reaccionar a persistencia de consentimientos, se añade un listener `@Observes`
> sobre `UserConsentedScopesCreateEvent` / `UserConsentedScopesDeleteEvent`.

## Current state

- `access_user_consented_scopes` table exists in DB ✅ with columns:
  `uid`, `user`, `trusted_client`, `scope`, `granted BIT`, `decision_at`, `ip_address`, `user_agent`.
- Domain: `UserConsentedScopes` entity, `UserConsentedScopesFilter` (with `user` + `trustedClient`
  filter fields), `UserConsentedScopesCreateEvent`, `UserConsentedScopesDeleteEvent` ✅.
- `UserConsentedScopesReadRepositoryGateway` + `UserConsentedScopesWriteRepositoryGateway` ✅.
- `ScopesConsentAdapter` in `features/oauth/consent/infrastructure/driven/` has scaffolding with
  commented-out use case injection:
  ```java
  // private final CheckScopeConsentUseCase checkConsent;
  // private final GrantScopeConsentUseCase grantConsent;
  ```

## What to implement

### 1. Implement `pendingScopes` directly in `ScopesConsentAdapter`

Remove the commented-out use case references. Implement the logic inline using the gateways:

```java
@Override
public List<ScopePermission> pendingScopes(String tenant, String username, String clientId,
        List<String> scopes) {
    if (scopes.isEmpty()) return List.of();

    User user = users.find(UserFilter.builder().name(username).build()).orElse(null);
    if (user == null) return toPermissions(scopes);

    TrustedClient client = clients.find(TrustedClientFilter.builder().code(clientId).build()).orElse(null);
    if (client == null) return toPermissions(scopes);

    // Load all granted consents for (user, client)
    List<UserConsentedScopes> granted = consentRead.list(
        UserConsentedScopesFilter.builder()
            .user(user)
            .trustedClient(client)
            .build())
        .stream()
        .filter(c -> Boolean.TRUE.equals(c.getGranted()))
        .toList();

    Set<String> grantedScopes = granted.stream()
        .map(UserConsentedScopes::getScope)
        .collect(Collectors.toSet());

    // Return only the scopes not yet consented
    return scopes.stream()
        .filter(s -> !grantedScopes.contains(s))
        .map(this::toPermission)
        .toList();
}
```

### 2. Implement `storeAcceptedScopes` directly in `ScopesConsentAdapter`

```java
@Override
public void storeAcceptedScopes(String tenant, String username, String clientId,
        List<String> scopes) {
    if (scopes.isEmpty()) return;

    User user = users.find(UserFilter.builder().name(username).build()).orElse(null);
    if (user == null) return;
    TrustedClient client = clients.find(TrustedClientFilter.builder().code(clientId).build()).orElse(null);
    if (client == null) return;

    String ip = requestContext.getRemoteAddr();     // from injected HttpServletRequest or Vert.x context
    String ua = requestContext.getHeader("User-Agent");

    for (String scope : scopes) {
        // Idempotent: skip if already granted
        boolean exists = consentRead.list(
            UserConsentedScopesFilter.builder()
                .user(user).trustedClient(client).build())
            .stream()
            .anyMatch(c -> scope.equals(c.getScope()) && Boolean.TRUE.equals(c.getGranted()));

        if (!exists) {
            UserConsentedScopes consent = UserConsentedScopes.create(
                new UserConsentedScopesChangeSet()
                    .withUid(UUID.randomUUID().toString())
                    .withUser(user)
                    .withTrustedClient(client)
                    .withScope(scope)
                    .withGranted(true)
                    .withDecisionAt(Instant.now())
                    .withIpAddress(ip)
                    .withUserAgent(ua));
            consentWrite.create(consent);
        }
    }
}
```

### 3. Revocation — via `ScopesConsentAdapter` or direct gateway call

For PLAN-33 (GDPR Consent Management Page), add a `revokeConsent` method to `ScopesConsentGateway`
and implement it in the adapter:

```java
void revokeConsent(String userId, String clientId);           // revoke all scopes for a client
void revokeScope(String userId, String clientId, String scope); // revoke a single scope
```

Implementation: load matching `UserConsentedScopes` records, call `record.delete()` on each,
persist via `UserConsentedScopesWriteRepositoryGateway`.

### 4. `UserConsentedScopesCreateEvent` listener — for audit/side effects

If audit is needed when consent is granted, add a CDI observer in the OAuth context:

`features/oauth/consent/application/ScopeConsentGrantedObserver.java`

```java
void onGranted(@Observes UserConsentedScopesCreateEvent event) {
    auditGateway.consentAccepted(
        event.getPayload().getUser().getUid(),
        event.getPayload().getTrustedClient().getUid(),
        event.getPayload().getScope());
}
```

## Files to modify / create

| Action | File |
|--------|------|
| **Modify** | `oauth/consent/infrastructure/driven/ScopesConsentAdapter.java` — implement using gateways directly |
| **Modify** | `oauth/consent/domain/gateway/ScopesConsentGateway.java` — add `revokeConsent`, `revokeScope` |
| **Create** (if audit needed) | `oauth/consent/application/ScopeConsentGrantedObserver.java` |
| **Add injection** | `ScopesConsentAdapter` — inject `UserConsentedScopesReadRepositoryGateway` + `WriteRepositoryGateway` |
