# Issue PLAN-25 — WebAuthn Credential Management in Profile

**OAUTH_PLAN:** PLAN-25  
**Wave:** 4 — Deferred

## Problem

Users cannot list, rename, or delete their registered WebAuthn credentials from the profile area.
The `webauthn/` bounded context manages the flow but exposes no self-service management UI.

## Current state

- `webauthn/` bounded context with registration + authentication ceremonies ✅.
- `profile/` context has panels for user data and sessions.
- No `WebAuthnCredentialGateway` visible from the profile context.
- No UI for credential management.

## Implementation steps

### 1. Define `ProfileWebAuthnGateway`

`features/oauth/profile/domain/gateway/ProfileWebAuthnGateway.java`:

```java
public interface ProfileWebAuthnGateway {
    List<WebAuthnCredentialSummary> listCredentials(String userId);
    void renameCredential(String credentialId, String userId, String newName);
    void deleteCredential(String credentialId, String userId);
}
```

`WebAuthnCredentialSummary` — VO with: `id`, `name`, `createdAt`, `lastUsedAt`, `aaguid`.

This gateway keeps the profile context decoupled from the webauthn BC internals.

### 2. Implement `ProfileWebAuthnAdapter`

`features/oauth/profile/infrastructure/driven/ProfileWebAuthnAdapter.java`:

Delegates to the `webauthn/` BC's read/write repository gateways.
Uses `userId` to filter credentials (each credential is associated to a user).

### 3. Add `PasskeysPanel` to `ProfileHtmlController`

`features/oauth/profile/infrastructure/driver/html/ProfileHtmlController.java`:

Add a panel endpoint:
```
GET /{tenant}/account/passkeys           — list credentials
POST /{tenant}/account/passkeys/{id}/rename — rename
DELETE /{tenant}/account/passkeys/{id}    — delete (requires re-authentication prompt)
```

The delete operation should require step-up authentication (ACR ≥ 2 or re-auth `prompt=login`)
before allowing credential deletion to prevent account lockout attacks.

### 4. Template

`src/main/resources/templates/oauth/profile/passkeys.html`:
- List of registered credentials with name, creation date, last-used date.
- Inline rename form (AJAX or plain form POST).
- Delete button with confirmation modal.

## Dependencies

- PLAN-07 (ACR enforcement) for the step-up before delete.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `profile/domain/gateway/ProfileWebAuthnGateway.java` |
| **Create** | `profile/infrastructure/driven/ProfileWebAuthnAdapter.java` |
| **Modify** | `profile/infrastructure/driver/html/ProfileHtmlController.java` — add passkeys panel |
| **Create** | `templates/oauth/profile/passkeys.html` |
