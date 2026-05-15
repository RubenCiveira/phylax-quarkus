# Issue 1.1 — `email-verified` wiring into the OIDC flow

**Model section:** 1.1  
**OAUTH_PLAN:** PLAN-10 (Email Verification After Registration)  
**Wave:** 1

## Current state

- `access_user.email_verified BIT` column exists in DB (migration applied ✅).
- Domain: `EmailVerifiedVO`, `EmailVerifiedValueHolder`, `UserVerifyEmailEvent` exist in
  `features/access/user/domain/`. The model knows about email verification.
- **No application use case** for triggering verification (no `UserVerifyEmailUsecase`).
- **No HTTP driver** for consuming the verification link.
- **No OAuth claim emission** — `email_verified` is never set in ID tokens or userinfo responses.

## What to implement

### 1. Application use case — `UserVerifyEmailUsecase`

`features/access/user/application/usecase/verifyemail/`

- Input: verification token (opaque string from `user-access-temporal-code`).
- Steps:
  1. Retrieve and consume the temporal code (`UserAccessTemporalCodeReadRepositoryGateway`, filter by `register_code`).
  2. Load the user by the UID stored in the temporal code.
  3. Call `user.verifyEmail()` (or equivalent `update` with `emailVerified=true`).
  4. Persist via `UserWriteRepositoryGateway`.
- Throws `NotFoundException` if token is missing or expired; throws nothing if already verified (idempotent).

### 2. HTTP driver — `EmailVerificationController`

`features/access/user/infrastructure/driver/html/EmailVerificationController.java`

```
GET /access/{tenant}/verify-email?token={token}
```

- Calls `UserVerifyEmailUsecase`.
- On success: redirect to a confirmation page or the tenant login page.
- On invalid/expired token: return 400 with an error page.
- No authentication required (the token itself is the credential).

### 3. Registration flow — set `email_verified = false` on new accounts

In `features/access/user/application/usecase/create/UserCreateEnrich.java` (or equivalent):
- Set `emailVerified = false` when creating a new user (should already be the DB default, but
  set it explicitly in the domain to make the intent clear).

### 4. OIDC claim emission — `email_verified` in ID token and userinfo

`features/oauth/tokensecurity/application/JwtTokenBuilder.java`:
- When building the ID token and when handling the `/userinfo` endpoint:
  - If `email` scope is requested: add `email_verified: boolean` to claims.
  - Source: load via `LoginGateway` or `ProfileGateway` — whichever already carries the user row.

### 5. Tenant policy enforcement (optional, PLAN-10 step 5)

In `features/access/tenantconfig/` — expose `requireEmailVerification` from `TenantConfig`.  
In the OIDC step router: if `tenantConfig.requireEmailVerification && !user.emailVerified`,
inject a `VERIFY_EMAIL_PENDING` challenge step instead of proceeding to the consent step.

## Dependencies

- `user-access-temporal-code` BC (for token storage/retrieval) — already implemented.
- `UserWriteRepositoryGateway` + `UserReadRepositoryGateway` — already implemented.
- PLAN-05 (Userinfo endpoint) — consume `emailVerified` claim there too.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `user/application/usecase/verifyemail/UserVerifyEmailUsecase.java` |
| **Create** | `user/infrastructure/driver/html/EmailVerificationController.java` |
| **Modify** | `oauth/tokensecurity/application/JwtTokenBuilder.java` — add `email_verified` claim |
| **Modify** | `user/application/usecase/create/UserCreateEnrich.java` — explicit `emailVerified=false` |
| **Modify** (optional) | `oauth/authentication/.../OidcStepRouter.java` — `VERIFY_EMAIL_PENDING` step |
