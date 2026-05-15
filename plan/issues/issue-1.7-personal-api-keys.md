# Issue 1.7 — Personal API Keys — REST driver and M2M authentication

**Model section:** 1.7  
**OAUTH_PLAN:** PLAN-26 (Personal Access Tokens for Users)  
**Wave:** 4

## Architectural constraint

> No se crean nuevos casos de uso en `features/access/`. Los casos de uso CRUD generados
> por el compilador de modelos ya existen. La lógica adicional (autenticación, REST driver
> de self-service) va en `features/oauth/` usando los gateways de Access directamente.

## Current state

- `access_user_personal_api_key` table exists in DB ✅.
- Domain: `UserPersonalApiKey` entity with `uid`, `user`, `name`, `keyHash`, `scopes`,
  `lastUsedAt`, `expiresAt`, `createdAt`, `enabled` ✅.
- Application use cases CRUD generated: `create`, `delete`, `list`, `retrieve`, `update` ✅.
- `UserPersonalApiKeyReadRepositoryGateway` + `UserPersonalApiKeyWriteRepositoryGateway` ✅.
- **No REST driver** for self-service key management.
- **No authentication path** that accepts API keys as bearer credentials.

## What to implement

### 1. Self-service REST driver — in OAuth profile area

`features/oauth/profile/infrastructure/driver/rest/PersonalApiKeyController.java`

Endpoints scoped to the authenticated user:

```
GET    /{tenant}/account/me/api-keys           — list own keys (name, scopes, lastUsedAt, expiresAt; never keyHash)
POST   /{tenant}/account/me/api-keys           — create key → returns raw key ONCE
DELETE /{tenant}/account/me/api-keys/{uid}     — hard delete
PUT    /{tenant}/account/me/api-keys/{uid}/disable — disable without deleting
```

The controller uses `UserPersonalApiKeyReadRepositoryGateway` and
`UserPersonalApiKeyWriteRepositoryGateway` (Access gateways) directly.
The existing generated use cases (`UserPersonalApiKeyCreateUsecase`, etc.) can also be
reused if their authorization policy allows it.

**Key creation flow:**
1. Generate cryptographically random raw key (`SecureTokenService.generate()` from PLAN-18,
   or inline `SecureRandom` 32 bytes Base64URL).
2. Compute `SHA-256` hex → `keyHash`.
3. Build a `UserPersonalApiKeyChangeSet` with `keyHash`, `name`, `scopes`, optional `expiresAt`,
   `enabled=true`, `createdAt=now()`, `user=currentUserUid`.
4. Call `UserPersonalApiKeyWriteRepositoryGateway.create(entity)`.
5. Return the raw key in the response **once** — never stored, never returned again.

### 2. API Key authentication adapter — in OAuth

`features/oauth/authentication/infrastructure/driven/ApiKeyAuthAdapter.java`

Called from the bearer token filter before JWT verification:

1. Extract raw bearer value from `Authorization: Bearer {rawKey}`.
2. Compute `SHA-256` of raw value.
3. Query `UserPersonalApiKeyReadRepositoryGateway` by `keyHash` (filter already supports this).
4. Reject if: not found, `enabled=false`, or `expiresAt < now()`.
5. Update `lastUsedAt = now()` via `UserPersonalApiKeyWriteRepositoryGateway.update(...)`.
   Use fire-and-forget (best-effort); do not fail the request if the touch fails.
6. Load the associated user via `UserReadRepositoryGateway`.
7. Return `AuthenticationData` with `userId`, `scopes` from the key's `scopes` field,
   `acr=1` (password-equivalent — no MFA for API keys).

### 3. `UserPersonalApiKeyDeleteEvent` listener — for audit

`features/oauth/authentication/application/ApiKeyDeletedObserver.java`

```java
void onDeleted(@Observes UserPersonalApiKeyDeleteEvent event) {
    auditGateway.personalApiKeyRevoked(event.getPayload().getUser().getUid(),
                                       event.getPayload().getName());
}
```

### 4. Scope intersection

When the API key authenticates a request, the effective scopes are the **intersection** of:
- The key's declared `scopes`.
- The client's `allowedScopes`.

Never grant more scopes than the key declares.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `oauth/profile/infrastructure/driver/rest/PersonalApiKeyController.java` |
| **Create** | `oauth/authentication/infrastructure/driven/ApiKeyAuthAdapter.java` |
| **Create** (if audit needed) | `oauth/authentication/application/ApiKeyDeletedObserver.java` |
| **Modify** | Request filter / security chain — plug in `ApiKeyAuthAdapter` for `Authorization: Bearer` |
