# Issue 1.6 — MFA recovery codes — application layer and OIDC step

**Model section:** 1.6  
**OAUTH_PLAN:** PLAN-04 (MFA Recovery Codes)  
**Wave:** 0

## Current state

- `access_user_mfa_recovery_code` table exists in DB ✅.
- Domain: `UserMfaRecoveryCode` entity with `uid`, `user`, `codeHash`, `createdAt`, `usedAt` ✅.
- Domain events: `UserMfaRecoveryCodeConsumeEvent` ✅.
- Infrastructure: `UserMfaRecoveryCodeReadGatewayAdapter`, `UserMfaRecoveryCodeWriteGatewayAdapter`,
  `UserMfaRecoveryCodeRepository`, `UserMfaRecoveryCodeSlider` ✅.
- **No application use cases** exist for generating or consuming codes.
- **No step handler** in the OIDC flow for the recovery path.

## What to implement

### 1. `GenerateRecoveryCodesUseCase`

`features/access/usermfarecoverycode/application/usecase/generate/GenerateRecoveryCodesUseCase.java`

- Input: `userId (String)`, `count (int, default 8)`.
- Steps:
  1. Delete all existing codes for the user (atomic replacement of the full set).
  2. Generate `count` cryptographically random codes (SecureRandom, 10 printable chars each,
     excluding visually ambiguous: `0`, `O`, `I`, `1`).
  3. For each raw code: compute `SHA-256` hex digest; persist a `UserMfaRecoveryCode` with
     `codeHash` set and `usedAt` null.
  4. Return the raw codes (shown to the user once, never stored again).
- Called after TOTP enrollment success and from the profile "regenerate" action.

### 2. `ConsumeRecoveryCodeUseCase`

`features/access/usermfarecoverycode/application/usecase/consume/ConsumeRecoveryCodeUseCase.java`

- Input: `userId (String)`, `rawCode (String)`.
- Steps:
  1. Compute `SHA-256` of `rawCode`.
  2. Query `UserMfaRecoveryCodeReadRepositoryGateway` with filter `user=userId`:
     find a code with matching `codeHash` and `usedAt == null`.
  3. If not found: return `Optional.empty()` (wrong code or already used).
  4. If found: call `code.consume(Instant.now())` → dispatch `UserMfaRecoveryCodeConsumeEvent`
     → persist via `UserMfaRecoveryCodeWriteGatewayAdapter`.
  5. Return `Optional.of(code.getUid())` to signal success.

### 3. MFA Recovery step in the OIDC authentication flow

`features/oauth/authentication/` — add a `RECOVER_MFA` step handler:

- The MFA step renders a "Lost your device?" link that routes to `RECOVER_MFA`.
- The `RECOVER_MFA` form accepts a single text field (`recovery_code`).
- On POST:
  1. Call `ConsumeRecoveryCodeUseCase` (injected via ACL adapter from the OAuth context).
  2. If success: mark the MFA challenge as resolved, advance the step router.
  3. If failure: re-render the form with an error message (`mfa.recovery.invalid` in `oauth.yaml`).
- The step must include `csid` in the form (see Known Pitfall #2 in project memory).

### 4. Profile panel — show and regenerate recovery codes

`features/access/usermfarecoverycode/infrastructure/driver/html/` or within the existing
`profile/` context:

- Panel showing how many unused codes remain (count only — not the raw codes).
- Button "Regenerate codes" → calls `GenerateRecoveryCodesUseCase` → displays the new raw codes
  once, with a warning that they won't be shown again.
- Only accessible after a fresh authentication (require ACR ≥ 2 or re-auth prompt).

### 5. ACL port in the OAuth context

`features/oauth/mfa/domain/gateway/MfaRecoveryGateway.java` (new port):

```java
public interface MfaRecoveryGateway {
    Optional<String> consumeRecoveryCode(String userId, String rawCode);
}
```

Implemented by an adapter in `features/oauth/mfa/infrastructure/driven/` that delegates to
`ConsumeRecoveryCodeUseCase`.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `access/usermfarecoverycode/application/usecase/generate/GenerateRecoveryCodesUseCase.java` |
| **Create** | `access/usermfarecoverycode/application/usecase/consume/ConsumeRecoveryCodeUseCase.java` |
| **Create** | `oauth/mfa/domain/gateway/MfaRecoveryGateway.java` |
| **Create** | `oauth/mfa/infrastructure/driven/MfaRecoveryAdapter.java` |
| **Create** | Step handler for `RECOVER_MFA` in `oauth/authentication/` |
| **Modify** | `oauth/authentication/.../OidcStepRouter.java` — register `RECOVER_MFA` step |
| **Modify** | MFA step template — add "Lost your device?" link |
| **Modify** | `messages/oauth.yaml` — add `mfa.recovery.invalid` message key |
