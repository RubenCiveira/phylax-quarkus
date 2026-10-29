# Issue PLAN-15 — Audit Logging Completeness

**OAUTH_PLAN:** PLAN-15  
**Wave:** 2

## Problem

Only login-succeeded/failed events are fired via CDI. Token issuance, consent, logout,
MFA, password changes, and delegation events are not audited. The `_oauth_audit_log` table
(section 2.13) is not yet created or populated.

## Current state

- `OidcEventDispatcher` fires CDI events for login success/failure ✅.
- `_oauth_audit_log` table is defined in `plan/OAUTH_DDL_INIT.sql` but the migration
  to create it has not been applied yet.
- No `AuditGateway` port in the OAuth context.

## Implementation steps

### 1. Apply the `_oauth_audit_log` DDL migration

Create and apply a migration file with the `_oauth_audit_log` CREATE TABLE from section 2.13
of `OAUTH_MODEL.md`.

### 2. Define `AuditGateway` port

`features/oauth/audit/domain/gateway/AuditGateway.java` (new bounded context or cross-cutting):

```java
public interface AuditGateway {
    void loginSucceeded(String userId, String clientId, String ip, String acr, String sessionId);
    void loginFailed(String username, String ip, String reason);
    void accountLocked(String userId, String ip);
    void mfaVerified(String userId, String clientId);
    void tokenIssued(String userId, String clientId, String grantType, Set<String> scopes, String jti);
    void tokenRevoked(String jti, String revokedBy);
    void passwordChanged(String userId, String reason);
    void consentAccepted(String userId, String clientId, Set<String> scopes);
    void logoutPerformed(String userId, String sessionId);
    void delegatedLogin(String userId, String provider, String externalId);
    void emailVerified(String userId);
}
```

### 3. Implement `AuditSqlAdapter`

`features/oauth/audit/infrastructure/driven/AuditSqlAdapter.java`:
- Inserts rows into `_oauth_audit_log` using Panache or a raw `DataSource`.
- `uid` = `UUID.randomUUID()`.
- Fire-and-forget; log and swallow exceptions — audit failures must never break the main flow.

### 4. Fire from event callsites

| Event | Callsite |
|-------|----------|
| `LOGIN_SUCCESS` | `AuthenticateUser` or password step handler |
| `LOGIN_FAILED` | Same, on authentication failure |
| `ACCOUNT_LOCKED` | Wherever account locking is enforced |
| `MFA_VERIFIED` | MFA step handler |
| `TOKEN_ISSUED` | Each granter in `authentication/application/granter/` |
| `TOKEN_REVOKED` | `TokenRevocationGateway` implementation |
| `PASSWORD_CHANGED` | `ChangePasswordUsecase` |
| `CONSENT_ACCEPTED` | `ScopeApprovalUsecase` or `ScopesConsentAdapter.storeAcceptedScopes` |
| `LOGOUT` | End-session handler (PLAN-08) |
| `DELEGATION_LOGIN` | `DelegateLogin` application service |
| `EMAIL_VERIFIED` | `UserVerifyEmailUsecase` (Issue 1.1) |

### 5. Retain `OidcEventDispatcher` CDI events

Keep the existing CDI event mechanism for internal observers. `AuditGateway` is the persistence
side; CDI events remain for in-process listeners (e.g., WebSocket push, future Webhooks PLAN-27).

## Files to create

| Action | File |
|--------|------|
| **Create** | `oauth/audit/domain/gateway/AuditGateway.java` |
| **Create** | `oauth/audit/infrastructure/driven/AuditSqlAdapter.java` |
| **Create** | Migration SQL — `_oauth_audit_log` CREATE TABLE |
| **Modify** | Multiple callsites — inject and call `AuditGateway` |
