# Issue 1.5 — Tenant config policy properties wired into the OIDC flow

**Model section:** 1.5  
**OAUTH_PLAN:** PLAN-09 (SSO TTL), PLAN-10 (Email Verification policy)  
**Wave:** 1

## Current state

All five policy columns exist in DB (migration applied ✅):

| Column | Default |
|--------|---------|
| `require_email_verification BIT` | 0 |
| `invitation_expiry_days INT` | 7 |
| `magic_link_expiry_minutes INT` | 30 |
| `session_sso_ttl_seconds INT` | 3600 |
| `refresh_token_ttl_seconds INT` | 2592000 |

The access domain has VOs and ValueHolders generated. However **none of these values are consumed
by the OIDC or application layer** — they are stored but not read.

## What to implement

### 1. Expose the policy fields from `TenantConfigGateway`

`features/oauth/` has a gateway for reading tenant configuration (locate via uses of
`TenantConfig` in the OAuth context — likely a `TenantConfigGateway` or similar port).

If a dedicated `OauthTenantConfig` DTO / value object doesn't yet carry these fields, add them:

```java
boolean requireEmailVerification();
int invitationExpiryDays();
int magicLinkExpiryMinutes();
int sessionSsoTtlSeconds();
int refreshTokenTtlSeconds();
```

In the driven adapter that loads `access_tenant_config`, map the new columns.

### 2. `requireEmailVerification` — gate login in the step router

In `features/oauth/authentication/.../OidcStepRouter.java` (or `AuthorizeHtml`):
- After user authentication succeeds: if `tenantConfig.requireEmailVerification && !user.emailVerified`,
  inject a `VERIFY_EMAIL_PENDING` challenge that blocks the flow until the link is clicked.
- Related to Issue 1.1 — coordinate with the `EmailVerificationController` implementation.

### 3. `sessionSsoTtlSeconds` — SSO cookie lifetime

In `features/oauth/authentication/.../OidcCookieManager.java` (or wherever SSO cookies are set):
- Use `tenantConfig.sessionSsoTtlSeconds` as the `Max-Age` of the SSO identity cookie instead
  of a hardcoded value.
- When loading an existing SSO session: check `authTime + sessionSsoTtlSeconds > now()`;
  if expired, force re-authentication.
- Related to PLAN-09 and PLAN-07 (`max_age` enforcement).

### 4. `refreshTokenTtlSeconds` — refresh token expiry

In `features/oauth/tokensecurity/application/JwtTokenBuilder.java` (or wherever `exp` is set for
refresh tokens):
- Replace hardcoded refresh TTL with `tenantConfig.refreshTokenTtlSeconds`.

### 5. `invitationExpiryDays` — invitation token TTL

In `features/access/userinvitation/application/usecase/create/InvitationCreateUsecase.java`
(or equivalent):
- Replace the hardcoded 7-day expiry with `tenantConfig.invitationExpiryDays`.
- The `TenantConfig` can be loaded via the existing `TenantConfigGateway` in the access layer.

### 6. `magicLinkExpiryMinutes` — magic link token TTL

In `features/oauth/magiclink/application/` (wherever the magic link token expiry is set):
- Replace hardcoded expiry with `tenantConfig.magicLinkExpiryMinutes`.

## Dependencies

- Issue 1.1 (email verification) — shares `requireEmailVerification` flag.
- PLAN-09 (SSO) — shares `sessionSsoTtlSeconds`.
- `TenantConfigGateway` implementation — must already be loading the `access_tenant_config` row.

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `TenantConfigGateway` port + driven adapter — expose 5 new fields |
| **Modify** | `OidcStepRouter` / `AuthorizeHtml` — `requireEmailVerification` gate |
| **Modify** | `OidcCookieManager` — `sessionSsoTtlSeconds` for SSO cookie lifetime |
| **Modify** | `JwtTokenBuilder` — `refreshTokenTtlSeconds` |
| **Modify** | `InvitationCreateUsecase` — `invitationExpiryDays` |
| **Modify** | `MagicLink` application service — `magicLinkExpiryMinutes` |
