# OAuth Feature — Implementation Plan

> Cross-reference of `plan/old/OIDC-TASKS.md`, `plan/old/UPGRADE_PLAN.md` and `plan/OAUTH_STATE.md`.
> Generated: 2026-05-14. Supersedes the individual task lists in `plan/old/`.

---

## Preface: What Changed Since the Old Plans

The old plans (`OIDC-TASKS.md` dated 2026-03-17, `UPGRADE_PLAN.md` dated 2026-04-12) were written
against a less complete codebase. The following originally-planned items are now **already
implemented** and need no further work at the feature level (though they may have partial-completion
notes in `OAUTH_STATE.md`):

| Old Plan Item | Now implemented as |
|---|---|
| UPGRADE_PLAN §2.11 WebAuthn / Passkeys | `webauthn` bounded context — full ceremony + REST driver |
| UPGRADE_PLAN §2.12 Magic Links | `magiclink` bounded context — request + verify + feature flag |
| UPGRADE_PLAN §2.7 PAR | `par` bounded context — PushAuthorization + ResolveParRequest + SQL adapter |
| UPGRADE_PLAN §2.8 Dynamic Client Registration | `client` — RegisterClientUsecase, UpdateDynamic, ReadDynamic, DeleteDynamic (partial) |
| UPGRADE_PLAN §2.17 System of Invitations | `userinvitation` — create, accept, resend, cancel + mail + auto-login session |
| UPGRADE_PLAN §2.15/2.16 Session + Profile API | `profile` — ProfileService, HTML panels, ProfileMeController, SessionsAdapter |
| UPGRADE_PLAN §2.14 Social login (Google + SAML) | `delegatelogin` — GoogleDelegatedAccessProvider + SamlDelegatedAccessProvider |
| TASK-03 Token Introspection (0%) | `tokensecurity` — IntrospectTokenUseCase + IntrospectionController (partial) |
| UPGRADE_PLAN §2.9 Client Credentials (partial) | `client` — ApiKeyData + ApiKeyController (M2M via API keys) |

What follows are only the **remaining open items**, grouped into prioritised waves.

---

## Wave 0 — Critical Security Gaps (block RFC compliance)

These must be resolved before any public client (SPA, mobile, CLI) can safely use the server.

---

### PLAN-01 — PKCE Full Enforcement (RFC 7636)

**Source:** OIDC-TASKS TASK-01 · OAUTH_STATE §2.6 session · §2.1 authentication

**Status:** 40% — `PkceChallenge` VO exists; S256 helper in `JwtTokenBuilder`. `TemporalAuthCode`
does **not** store the challenge so token-endpoint verification is impossible.

**Concrete steps:**

1. `session/domain/TemporalAuthCode.java` — add fields:
   ```java
   @Builder.Default Optional<String> codeChallenge = Optional.empty();
   @Builder.Default Optional<String> codeChallengeMethod = Optional.empty();
   ```
2. `authentication/.../AuthorizeHtml.java` (or wherever `TemporalAuthCode` is built) — propagate
   `authRequest.getCodeChallenge()` and `authRequest.getCodeChallengeMethod()` when creating the code.
3. `authentication/.../TokenController.java` — on code exchange:
   - If `TemporalAuthCode.codeChallenge` is present: require `code_verifier` in request body.
   - Compute `BASE64URL(SHA256(code_verifier))` and compare; reject with `invalid_grant` on mismatch
     or if `code_verifier` absent when required.
   - Reject `code_challenge_method=plain` with `invalid_request`.
4. `client/domain/ClientDetails.java` — add `boolean requirePkce` (or derive from `!hasSecret()`).
   In `AuthorizeHtml`: reject requests from public clients without `code_challenge` with `invalid_request`.
5. `oidc/.../OpenIdConfigurationController.java` — add `"S256"` to `codeChallengMethodsSupported`.

**Files:** `session/domain/TemporalAuthCode.java`, `authentication/infrastructure/driver/html/AuthorizeHtml.java`,
`authentication/infrastructure/driver/rest/TokenController.java`, `client/domain/ClientDetails.java`,
`oidc/infrastructure/driver/rest/OpenIdConfigurationController.java`

---

### PLAN-02 — Refresh Token Invalidation on Rotation

**Source:** OIDC-TASKS TASK-04 · OAUTH_STATE §2.7 tokensecurity

**Status:** 0% — `RefreshGranter` issues a new refresh token but does **not** revoke the old one,
enabling replay attacks with any previously issued refresh token.

**Concrete steps:**

1. `tokensecurity/domain/gateway/TokenRevocationGateway.java` — ensure `revokeToken(String jti, Instant expiresAt)` and
   `isRevoked(String jti)` exist (they may already be defined; verify implementation).
2. `authentication/application/granter/RefreshGranter.java` — after successfully issuing new tokens:
   - Extract old refresh token JTI from `JwtTokenBuilder`.
   - Call `TokenRevocationGateway.revokeToken(oldJti, oldExp)`.
   - If the incoming refresh token is already revoked (`isRevoked` returns true): revoke all tokens for
     the (userId, clientId) pair and respond `invalid_grant` (theft detection).
3. `tokensecurity/infrastructure/driven/TokenStoreSqlAdapter.java` — verify `revokeToken` writes to
   `revoked_token` table and `isRevoked` does a fast indexed lookup on `jti`.

**Files:** `authentication/application/granter/RefreshGranter.java`,
`tokensecurity/domain/gateway/TokenRevocationGateway.java`,
`tokensecurity/infrastructure/driven/TokenStoreSqlAdapter.java`

---

### PLAN-03 — Token Revocation Endpoint (RFC 7009)

**Source:** OIDC-TASKS TASK-02 · OAUTH_STATE §2.7 tokensecurity

**Status:** 0% — Current `/revocation` endpoint only clears pre-session cookies, not JWT tokens.
No JTI blacklist. `TokenRevocationGateway` is defined but no REST driver exposes it.

**Concrete steps:**

1. `tokensecurity/domain/gateway/TokenRevocationGateway.java` — ensure it has:
   ```java
   void revokeToken(String jti, Instant expiresAt);
   void revokeAllForUser(String userId, String clientId);
   boolean isRevoked(String jti);
   ```
2. Create `tokensecurity/infrastructure/driver/rest/TokenRevocationController.java`:
   - `POST /.../revoke` accepting `token` + `token_type_hint`
   - Authenticate client (client_secret_basic or client_secret_post)
   - Parse JWT, extract `jti` and `exp`, call `revokeToken`
   - Always respond `200 OK` even for unknown tokens (RFC 7009 mandates this)
3. `authentication/infrastructure/driver/html/AuthorizeHtml.java` (or logout handler) — on logout call
   `revokeAllForUser(userId, clientId)`.
4. `oidc/.../OpenIdConfigurationController.java` — add `revocationEndpoint` and
   `revocationEndpointAuthMethodsSupported`.
5. Verify `JwtTokenBuilder.verify()` checks `isRevoked(jti)` after signature verification.

**Files:** `tokensecurity/infrastructure/driver/rest/TokenRevocationController.java` (new),
`tokensecurity/domain/gateway/TokenRevocationGateway.java`,
`tokensecurity/infrastructure/driven/TokenStoreSqlAdapter.java`,
`oidc/.../OpenIdConfigurationController.java`

**Note:** PLAN-02 is a dependency — complete PLAN-02 before PLAN-03.

---

### PLAN-04 — MFA Recovery Codes

**Source:** OAUTH_STATE §2.4 mfa (new — not in old plans)

**Status:** 0% — If a user loses their TOTP device there is no self-service recovery path.

**Concrete steps:**

1. `mfa/domain/gateway/UserMfaGateway.java` — add:
   ```java
   List<String> generateRecoveryCodes(String userId);   // generates 8 single-use hashed codes
   Optional<String> consumeRecoveryCode(String userId, String rawCode); // removes and returns on match
   ```
2. `mfa/infrastructure/driven/UserMfaConfigAdapter.java` — persist hashed recovery codes in a new
   `user_mfa_recovery_code (user_id, code_hash, used_at)` table.
3. `mfa/application/UserMfa.java` — expose `setupRecoveryCodes(userId)` and `verifyRecoveryCode(userId, code)`.
4. Add `RecoverStep` integration in `authentication` context: when user selects "lost MFA device" on
   the MFA step, route to a recovery-code entry form.
5. `profile` context — add panel for displaying and regenerating recovery codes after fresh authentication.

**Files:** `mfa/domain/gateway/UserMfaGateway.java`, `mfa/application/UserMfa.java`,
`mfa/infrastructure/driven/UserMfaConfigAdapter.java`, plus new step handler in `authentication`.

---

## Wave 1 — Core Protocol Completeness

These complete the OIDC Core 1.0 conformance gaps.

---

### PLAN-05 — Userinfo Endpoint — Full Scope-Driven Claim Mapping

**Source:** OIDC-TASKS TASK-05 · OAUTH_STATE §2.1 authentication

**Status:** 25% — Returns only `sub`, `name`, `issuer`. Does not filter by scope, does not return
standard OIDC profile/email/phone claims.

**Concrete steps:**

1. `authentication/.../InformationController.java` — verify incoming access token (signature, expiry,
   revocation via PLAN-03).
2. Extract `sub`, `scope`, `client_id` from token claims.
3. Scope-to-claim mapping:
   - `openid` → `sub` (always)
   - `profile` → `name`, `given_name`, `family_name`, `preferred_username`, `picture`, `updated_at`
   - `email` → `email`, `email_verified`
   - `phone` → `phone_number`, `phone_number_verified`
4. Load user data for relevant claims via a new `userinfo/` application service or extend `ProfileGateway`.
5. Ensure `sub` in response matches `sub` in access token.
6. On invalid/revoked token: respond `401 WWW-Authenticate: Bearer error="invalid_token"`.
7. Align claim set with `profile/ProfileMeController.java` to eliminate the duplication noted in
   OAUTH_STATE §2.13.

**Files:** `authentication/infrastructure/driver/rest/InformationController.java`,
`profile/domain/gateway/ProfileGateway.java` (or new gateway)

---

### PLAN-06 — Discovery Document Completeness

**Source:** OIDC-TASKS TASK-06 · OAUTH_STATE §2.15 oidc

**Status:** 85% — Several endpoints are either missing, stale, or advertise features not yet
implemented (triggers RFC non-conformance for RPs doing discovery-driven configuration).

**Concrete steps:**

1. `oidc/.../OpenIdConfigurationController.java`:
   - Add `codeChallengMethodsSupported: ["S256"]` (after PLAN-01)
   - Add `revocationEndpoint` and `revocationEndpointAuthMethodsSupported` (after PLAN-03)
   - Correct `grantTypesSupported`: must include `"authorization_code"`, `"refresh_token"`;
     add `"client_credentials"` after PLAN-12; add `"urn:ietf:params:oauth:grant-type:device_code"` after PLAN-13
   - Add `promptValuesSupported: ["none", "login", "consent", "select_account"]`
   - Correct `acrValuesSupported`: add `"2"` for MFA (already emitted in tokens but not advertised)
   - Fix `backchannel_logout_supported` / `frontchannel_logout_supported` to match actual state
   - Confirm `introspectionEndpoint` is correctly wired (after PLAN-03 / TASK-03 state verified)
   - Confirm `pushedAuthorizationRequestEndpoint` is present and points to the real PAR endpoint
2. Remove or mark-as-planned any endpoint advertised but returning 403 (DT-02 from old OIDC-TASKS).

**Files:** `oidc/infrastructure/driver/rest/OpenIdConfigurationController.java`

---

### PLAN-07 — prompt / max_age / login_hint / ACR enforcement

**Source:** OIDC-TASKS TASK-07, TASK-10 · OAUTH_STATE §2.1 authentication

**Status:** 25% — Parameters parsed in `AuthRequest` but not acted upon in the step-router.

**Concrete steps:**

**`prompt`:**
1. `authentication/.../AuthorizeHtml.java` / `OidcStepRouter` — at session load:
   - `prompt=login` → invalidate any existing SSO session, force re-authentication
   - `prompt=consent` → force consent step even if already accepted
   - `prompt=none` → if valid SSO session exists, issue code immediately; otherwise redirect with
     `login_required` or `interaction_required`; never render UI
   - `prompt=select_account` → log unimplemented, treat as `prompt=login` for now

**`max_age`:**
2. `session/domain/SessionInfo.java` — add `Instant authTime` (real authentication timestamp).
3. `authentication/.../AuthorizeHtml.java` — when loading SSO session: if `auth_time + max_age < now()`,
   invalidate session and force re-login.

**`login_hint`:**
4. Pass `loginHint` through `AuthRequest` to the login step template as pre-filled username.

**`acr_values` (Step-up, TASK-10):**
5. Parse `acr_values` in `AuthRequest` (space-separated preference list).
6. After SSO session load: compare `session.acr` with requested minimum. If insufficient:
   - Required ACR=1 but session ACR=0 → force password step
   - Required ACR=2 but session ACR=1 → inject MFA step without re-login
7. Store required `acr_values` in `SessionInfo.ChallengesState` to resume after step-up.

**Files:** `authentication/domain/AuthRequest.java`, `authentication/infrastructure/driver/html/AuthorizeHtml.java`,
`authentication/infrastructure/driver/html/OidcStepRouter.java`, `session/domain/SessionInfo.java`

---

### PLAN-08 — Logout Complete: End Session + Back-channel Notifications

**Source:** OIDC-TASKS TASK-08, TASK-15 · OAUTH_STATE §2.1 authentication, §2.7 tokensecurity

**Status:** 75% — Session deleted and cookie cleared, but tokens stay alive and RPs are not notified.

**Concrete steps:**

1. **End Session endpoint** (`/end_session` or `/logout`):
   - Validate optional `id_token_hint` (signature + extract `sub`/`sid`)
   - Validate `post_logout_redirect_uri` against client allowlist; reject with page if invalid
   - Call `TokenRevocationGateway.revokeAllForUser(userId, clientId)` (requires PLAN-03)
   - Delete `SessionInfo` from store and clear `AUTH_SESSION_ID` cookie
   - If valid `post_logout_redirect_uri`: redirect with `state`; otherwise show confirmation page
2. **Back-channel logout** (OIDC Back-Channel Logout 1.0):
   - `client/domain/ClientDetails.java` — ensure `backchannelLogoutUri` and
     `backchannelLogoutSessionRequired` are present (check if already modeled)
   - `authentication/application/BackChannelLogoutDispatcher.java` — convert to **async** fire-and-forget:
     use Quarkus `@Asynchronous` or Vert.x `context.executeBlocking` to avoid blocking the logout
     response on slow/failing RPs
   - Build `logout_token` JWT: `sub`, `sid`, `iss`, `aud`, `iat`, `jti`, `events` claim
   - HTTP POST to each RP `backchannelLogoutUri` with `application/x-www-form-urlencoded`
3. `oidc/.../OpenIdConfigurationController.java` — update `backchannel_logout_supported` to `true`
   once dispatcher is async.

**Files:** `authentication/infrastructure/driver/html/AuthorizeHtml.java` (logout handler),
`authentication/application/BackChannelLogoutDispatcher.java`,
`client/domain/ClientDetails.java`

---

### PLAN-09 — Session SSO (cross-client single sign-on)

**Source:** OIDC-TASKS TASK-11 · OAUTH_STATE §2.1 authentication, §2.6 session

**Status:** 40% — Session verified per-flow but not reused cross-client.

**Concrete steps:**

1. `session/domain/SessionInfo.java` — add `Instant authTime` (real password/MFA timestamp, distinct
   from session creation time — also needed for PLAN-07 `max_age`).
2. Separate the SSO identity cookie from the per-flow authorization-request cookie: the SSO cookie
   survives across multiple `/authorize` calls from different clients; the per-flow cookie is scoped
   to a single authorization request.
3. `authentication/.../AuthorizeHtml.java` — when starting a new `/authorize`:
   - Look up SSO session by SSO cookie (not by per-flow state)
   - If valid (not expired, `prompt` allows, `max_age` not exceeded): skip login and advance to
     consent/scope checks
4. `SessionInfo` — store a set of (clientId) pairs for which this SSO session has been used, enabling
   back-channel logout to notify all RPs on session termination.
5. Make SSO TTL configurable (application property, tenant override).

**Files:** `session/domain/SessionInfo.java`, `authentication/infrastructure/driver/html/AuthorizeHtml.java`,
`authentication/infrastructure/driver/html/OidcCookieManager.java`

---

### PLAN-10 — Email Verification After Registration

**Source:** OIDC-TASKS TASK-17 · UPGRADE_PLAN §2.16 · OAUTH_STATE §2.3 user

**Status:** 10% — Registration mail is sent but the verification link token and confirmation endpoint
do not exist; `email_verified=false` is never set on new accounts.

**Concrete steps:**

1. Add `emailVerified: boolean` to the user aggregate in `features/access/user/domain/`.
2. On registration: set `email_verified = false`; generate a short-lived verification token (reuse
   `TemporalKeysGateway`); include the link in the existing registration email.
3. Create `GET /verify-email?token=...` HTML endpoint in `user/infrastructure/driver/html/` that:
   - Retrieves and consumes the token (one-time use)
   - Sets `email_verified = true` on the user
   - Redirects to a confirmation page
4. `tokensecurity/application/JwtTokenBuilder.java` — include `email_verified` claim in ID token
   and userinfo response (controlled by `email` scope).
5. Add tenant-configurable policy: block login until email verified (default: allow).

**Files:** `user/domain/gateway/LoginGateway.java` (add `emailVerified` to returned data),
`user/infrastructure/driven/UserLoginAdapter.java`,
`tokensecurity/application/JwtTokenBuilder.java`,
new `user/infrastructure/driver/html/EmailVerificationController.java`

---

## Wave 2 — Protocol Extensions & Additional Grant Types

---

### PLAN-11 — Introspection Endpoint Complete (RFC 7662)

**Source:** OIDC-TASKS TASK-03 · OAUTH_STATE §2.7 tokensecurity

**Status:** Partial — `IntrospectionController` exists but may return 403. Requires PLAN-03 (revocation)
to check `active` status correctly.

**Concrete steps:**

1. `tokensecurity/infrastructure/driver/rest/IntrospectionController.java` — fully implement:
   - Authenticate caller as a client with an `introspect` scope or a `isResourceServer=true` flag
   - Parse submitted token without throwing on invalid (catch all exceptions → `{"active": false}`)
   - If valid JWT, not expired, and not revoked (call `isRevoked(jti)`): return active response with
     `sub`, `client_id`, `scope`, `exp`, `iat`, `iss`, `jti`, `token_type`, custom claims
   - Any other case: `{"active": false}`
   - Response headers: `Content-Type: application/json`, `Cache-Control: no-store`
2. `client/domain/ClientDetails.java` — add `boolean resourceServer` flag (or scope-based check).

**Files:** `tokensecurity/infrastructure/driver/rest/IntrospectionController.java`,
`client/domain/ClientDetails.java`

---

### PLAN-12 — Client Credentials Grant (M2M, RFC 6749 §4.4)

**Source:** OIDC-TASKS TASK-12 · UPGRADE_PLAN §2.9 · OAUTH_STATE §2.1 authentication, §2.2 client

**Status:** 0% — No `ClientCredentialsGranter`. Current M2M path uses API keys only.

**Concrete steps:**

1. Create `authentication/application/granter/ClientCredentialsGranter.java`:
   - `canHandle("client_credentials")` → true
   - Verify client is confidential (has secret)
   - Verify `"client_credentials"` is in `ClientDetails.allowedGrants`
   - Intersect requested scopes with `ClientDetails.allowedScopes`
   - Delegate to `JwtTokenBuilder` to emit access token with `sub = clientId`, no `sub` user claim,
     no ID token, no refresh token
2. Wire into the granter dispatch in `TokenController`.
3. `oidc/.../OpenIdConfigurationController.java` — add `"client_credentials"` to `grantTypesSupported`.

**Files:** `authentication/application/granter/ClientCredentialsGranter.java` (new),
`authentication/infrastructure/driver/rest/TokenController.java`

---

### PLAN-13 — Device Authorization Grant Complete (RFC 8628)

**Source:** OIDC-TASKS TASK-16 · OAUTH_STATE §2.11 device

**Status:** ~40% — Domain model and service exist; HTML verification UI exists. Token endpoint
`device_code` grant type exchange may not be wired; `slow_down` enforcement unverified.

**Concrete steps:**

1. Verify `DeviceAuthorizationService.pollStatus()` correctly returns `authorization_pending`,
   `slow_down` (add `lastPolledAt` field to `DeviceAuthorization` and enforce the poll interval),
   and `expired_token`.
2. Create or verify `authentication/application/granter/DeviceCodeGranter.java`:
   - `canHandle("urn:ietf:params:oauth:grant-type:device_code")` → true
   - Call `DeviceAuthorizationService.pollStatus(deviceCode)` to get `AuthenticationData`
   - Delegate to `JwtTokenBuilder` to emit full token set on `APPROVED` status
3. Wire `DeviceCodeGranter` into `TokenController`.
4. Enforce RFC 8628 §6.1 user_code character set (exclude visually ambiguous chars: 0, O, I, 1, etc.).
5. `oidc/.../OpenIdConfigurationController.java` — confirm `deviceAuthorizationEndpoint` is present.

**Files:** `device/application/DeviceAuthorizationService.java`,
`device/domain/DeviceAuthorization.java` (add `lastPolledAt`),
`authentication/application/granter/DeviceCodeGranter.java` (new or verify existing)

---

### PLAN-14 — Generic OIDC Delegated Provider

**Source:** UPGRADE_PLAN §2.14 · OAUTH_STATE §2.8 delegatelogin

**Status:** 0% — Only Google OIDC and SAML are implemented. No generic OIDC provider for Okta,
Azure AD, Keycloak, or any standards-compliant OIDC IdP.

**Concrete steps:**

1. Create `delegatelogin/infrastructure/driven/provider/OidcDelegatedAccessProvider.java`
   implementing `DelegatedAccessExternalProvider`:
   - `buildRequest`: redirect to IdP authorization endpoint with PKCE + state
   - `handleResponse`: exchange code at IdP token endpoint, verify ID token signature (fetch JWKS)
   - `resolveUserInfo`: call IdP userinfo endpoint or decode ID token claims
2. Move `GoogleDelegatedAccessProvider` and `SamlDelegatedAccessProvider` from `domain/provider/` to
   `infrastructure/driven/provider/` (fixes the Hexagonal violation noted in OAUTH_STATE §2.8).
3. `delegatelogin/domain/DelegatedLoginEndpoint.java` — add `providerType` enum: `GOOGLE`, `SAML`,
   `OIDC` (generic), `GITHUB` (future).
4. `DelegatedAccessAuthValidatorAdapter` or `DelegateLoginAdapter` — route to the correct provider
   implementation by `providerType`.

**Files:** `delegatelogin/infrastructure/driven/provider/OidcDelegatedAccessProvider.java` (new),
`delegatelogin/domain/provider/` → move to `delegatelogin/infrastructure/driven/provider/`,
`delegatelogin/domain/DelegatedLoginEndpoint.java`

---

### PLAN-15 — Audit Logging Completeness

**Source:** OIDC-TASKS TASK-09 · OAUTH_STATE §2.1 authentication (cross-cutting)

**Status:** 25% — Only login-succeeded/failed events via CDI. No token issuance, consent, logout,
MFA, password-change, or delegation events.

**Concrete steps:**

1. Define `AuditGateway` outbound port (in a shared or cross-cutting module):
   ```java
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
   ```
2. Implement in `OidcEventDispatcher` and connect to the existing `_audit_action` table or a
   dedicated `oauth_audit_log` table.
3. Fire from: `AuthenticateUser`, `TokenController` (each granter), `BackChannelLogoutDispatcher`,
   `ChangePasswordUsecase`, `ScopesConsentUsecase`, `DelegateLogin`.

---

### PLAN-16 — PAR REST Driver and Client Authentication

**Source:** OAUTH_STATE §2.12 par

**Status:** Partial — `PushAuthorizationUsecase` and `ResolveParRequestUsecase` are implemented.
No visible REST driver in the `par/` context; client authentication before push is unverified.

**Concrete steps:**

1. Locate the PAR `POST /.../par` endpoint — it may live in `authentication/infrastructure/driver/rest/`.
   If so, move or extract a dedicated `par/infrastructure/driver/rest/ParController.java`.
2. Ensure client authentication (client_secret_basic or client_secret_post) is enforced before
   calling `PushAuthorizationUsecase`.
3. Ensure `ResolveParRequestUsecase` is called in `authentication/.../AuthorizeHtml.java` when
   `request_uri` parameter is detected.
4. `oidc/.../OpenIdConfigurationController.java` — confirm `pushedAuthorizationRequestEndpoint` is set.

**Files:** `par/infrastructure/driver/rest/ParController.java` (create or locate),
`authentication/.../AuthorizeHtml.java`

---

## Wave 3 — Technical Debt Remediation

Items that are not functional gaps but degrade maintainability, security posture, or testability.

---

### PLAN-17 — `JwtTokenBuilder` Decomposition

**Source:** OAUTH_STATE §2.7 tokensecurity

**Status:** Debt — ~500-line god class combining token building, claim mapping, PKCE helpers, and
token verification. Fragile under change; hard to unit-test without wiring the full CDI container.

**Approach:**
- Extract `ClaimsMapper` — assembles the claim map from user data, scopes, and client config
- Extract `TokenVerifier` — wraps `JoseTokenSigner` for parsing and revocation check
- Keep `JwtTokenBuilder` as a thin facade delegating to the above two

Do this as a refactor-only change behind a feature branch; no functional changes.

---

### PLAN-18 — SecureTokenService — Shared Token Hashing

**Source:** OAUTH_STATE cross-cutting technical debt

**Status:** Debt — `MagicLink`, `UserInvitation`, and `Session` all implement their own token
hashing without a shared service, with no documented algorithm or constant-time comparison guarantee.

**Approach:**
1. Create `common/crypto/SecureTokenService.java` (or in a shared infrastructure module):
   - `generate()` → cryptographically random raw token (SecureRandom, 32 bytes, Base64URL)
   - `hash(rawToken)` → SHA-256 hex digest for storage
   - `verify(rawToken, storedHash)` → constant-time comparison (MessageDigest.isEqual)
2. Replace ad-hoc hashing in `MagicLinkGatewayAdapter`, `InvitationStoreAdapter`, and
   `TemporalKeysSqlAdapter` with calls to `SecureTokenService`.

---

### PLAN-19 — Session and Code Cleanup Jobs

**Source:** OIDC-TASKS DT from UPGRADE_PLAN §2.30 · OAUTH_STATE §2.6 session

**Status:** Debt — Expired `SessionInfo` rows and `TemporalAuthCode` records accumulate indefinitely;
no scheduled cleanup. Risks DB bloat under load.

**Approach:**
1. Add a Quarkus `@Scheduled` job in `session/infrastructure/` that deletes rows where `expires_at < now()`.
2. Same for `TemporalAuthCode` (authorization codes unused after ~10 minutes).
3. Add `expires_at` index to both tables if not already present.

---

### PLAN-20 — `OidcStepRouter` Unknown-Step Fallback + Explicit Step Registration

**Source:** OAUTH_STATE §2.1 authentication (design debt)

**Status:** Debt — An unknown `StepName` silently renders the login form, masking routing bugs.
Granters require editing a dispatch map instead of CDI-based auto-discovery.

**Approach:**
1. `OidcStepRouter` — add explicit `default` / fallback that throws an `IllegalStateException`
   with the unknown step name rather than silently falling back.
2. Granters — consider migrating from explicit dispatch map to `@Any`-injected CDI beans resolved
   by a `canHandle(grantType)` predicate, so new granters are self-registering.

---

### PLAN-21 — Provider Implementations: domain/ → infrastructure/driven/

**Source:** OAUTH_STATE §2.8 delegatelogin (Hexagonal violation)

**Status:** Debt — `GoogleDelegatedAccessProvider` and `SamlDelegatedAccessProvider` are in
`delegatelogin/domain/provider/`, violating the Hexagonal pattern (implementations belong in the
infrastructure layer; only the interface `DelegatedAccessExternalProvider` belongs in domain).

**Approach:** Move both classes to `delegatelogin/infrastructure/driven/provider/`. This is a
package move only — no logic change. Done as part of PLAN-14 if that task is scheduled.

---

### PLAN-22 — `FrontAcessController` Rename and Split

**Source:** OAUTH_STATE §2.1 authentication · OIDC-TASKS DT-01

**Status:** Debt — `FrontAcessController` has a typo (`Acess` → `Access`) and mixes multiple
responsibilities (request routing, session bootstrapping, PAR resolution, step dispatch, revocation).

**Approach (incremental — do not big-bang refactor):**
1. Fix the typo: `FrontAcessController` → `FrontAccessController` (search-and-replace; update all
   references and imports).
2. Extract the pre-session cookie revocation handler into its own `SessionRevocationController`.
3. Over time, extract PAR resolution into a dedicated collaborator called from `AuthorizeHtml`.

---

## Wave 4 — Deferred / Low Priority

Work that is valuable but not blocking compliance or security.

---

### PLAN-23 — Consent Versioning

**Source:** OIDC-TASKS TASK-13 · OAUTH_STATE §2.5 consent

Add `version` field to `PendingConsent` and `TermsOfUseAcceptance`. Store accepted version with
timestamp and IP. Re-prompt only when the current version differs from the user's last accepted version.
`TermsOfUseConsentUsecase` already has the version-bump check — verify the persistence adapter stores `version`.

---

### PLAN-24 — `c_hash` in ID Token (Hybrid Flow)

**Source:** OIDC-TASKS TASK-14 · OAUTH_STATE §2.7 tokensecurity

Add `c_hash = LEFT128(BASE64URL(SHA256(authorizationCode)))` claim to the ID token when
`response_type` contains `code`. Only relevant when Hybrid Flow is activated.
File: `tokensecurity/application/JwtTokenBuilder.java`.

---

### PLAN-25 — WebAuthn Credential Management in Profile

**Source:** OAUTH_STATE §2.9 webauthn, §2.13 profile

Add a `PasskeysPanel` to `profile/.../ProfileHtmlController.java` and expose list/rename/delete
operations via `WebAuthnCredentialGateway`. Requires a `WebAuthnCredentialGateway` reference in the
Profile context (or a dedicated `ProfileWebAuthnGateway` to avoid coupling).

---

### PLAN-26 — Personal Access Tokens for Users

**Source:** UPGRADE_PLAN §2.20

API keys scoped to individual users (not clients). Allows CLI tools and scripts to authenticate
on behalf of a specific user. Store hashed key only. Requires new persistence table and REST
endpoints under `/api/me/api-keys`. This is a net-new feature not yet started.

---

### PLAN-27 — Webhooks for Authentication Events

**Source:** UPGRADE_PLAN §2.21

Outbound HTTP notifications for `user.login`, `token.issued`, `token.revoked`, etc. Requires:
- `access_webhook_endpoint` table (url, secret, events[])
- Dispatcher connected to the `OidcEventDispatcher` CDI observers
- HMAC-SHA256 payload signing
- Exponential retry with `access_webhook_delivery` table

Best implemented after PLAN-15 (audit gateway) which establishes the event taxonomy.

---

### PLAN-28 — GDPR: Consent Withdrawal, Data Export, Right to Erasure

**Source:** UPGRADE_PLAN §2.25–2.27 · OAUTH_STATE §2.5 consent

Three sub-tasks of GDPR compliance:
- **Consent withdrawal**: `revokeConsent(userId, clientId, scope)` operation in Profile + Consent contexts
- **Data export**: async ZIP generation (profile, sessions, consents, audit log) → email on completion
- **Right to erasure**: `DELETE /api/me/account` → verification email → cascade anonymization

---

### PLAN-29 — Discovery: DiscoveryContributor SPI

**Source:** OAUTH_STATE §2.15 oidc (design debt)

Replace the static `OpenIdConfiguration` assembly with a `DiscoveryContributor` CDI interface
implemented by each context that needs to register its endpoints or capabilities. The OIDC context
assembles the document by calling all contributors. Decouples the discovery document from needing
to know about every feature.

---

## Execution Order

```
Wave 0 (security baseline — do first, in parallel where possible):
  PLAN-01 PKCE
  PLAN-02 Refresh rotation      (prerequisite for PLAN-03)
  PLAN-03 Revocation endpoint   (requires PLAN-02)
  PLAN-04 MFA recovery codes

Wave 1 (OIDC conformance — after Wave 0):
  PLAN-05 Userinfo              (requires PLAN-03 for revocation check)
  PLAN-06 Discovery fixes       (update as each Wave 0 item lands)
  PLAN-07 prompt / max_age / ACR
  PLAN-08 Logout + back-channel (requires PLAN-03)
  PLAN-09 Session SSO           (coordinate with PLAN-07 max_age)
  PLAN-10 Email verification

Wave 2 (extensions — after Wave 1):
  PLAN-11 Introspection complete (requires PLAN-03)
  PLAN-12 Client Credentials Grant
  PLAN-13 Device flow complete
  PLAN-14 Generic OIDC provider  (includes PLAN-21 as a subtask)
  PLAN-15 Audit logging
  PLAN-16 PAR REST driver

Wave 3 (debt — can interleave with Wave 2):
  PLAN-17 JwtTokenBuilder decomposition
  PLAN-18 SecureTokenService
  PLAN-19 Session cleanup jobs
  PLAN-20 StepRouter fallback
  PLAN-22 FrontAccessController rename (typo fix is zero-risk, do immediately)

Wave 4 (deferred):
  PLAN-23 through PLAN-29
```

---

## Typo Fixes — Zero-Effort, Do Now

These are pure rename/fix with no logic change and should be done immediately regardless of sprint:

| Item | File | Fix |
|------|------|-----|
| `FrontAcessController` → `FrontAccessController` | `authentication/infrastructure/driver/html/` | Rename class and file |
| `allowdedGrant` → `allowedGrant` | `client/domain/ClientDetails.java` | Fix method name |
| `codeChallengMethodsSupported` → `codeChallengeMethodsSupported` | `oidc/.../OpenIdConfigurationController.java` | Fix field/builder name |
| `AuthenticationChallege` → `AuthenticationChallenge` | `authentication/domain/` | Rename class |
