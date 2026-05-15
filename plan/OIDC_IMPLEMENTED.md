# OAuth / OIDC Feature — Implemented Items

> Reference document recording what was completed before the unified `OAUTH_PLAN.md` was written.
> Safe-to-delete marker for `plan/old/`: everything below was in `plan/old/` and is confirmed done.
> Generated: 2026-05-15

---

## Phase 01 — Refactoring & Technical Debt (completed items)

### [01-01] Rename `autenticate` → `authenticate`
Typo fixed throughout the codebase. All method names, class names, package references
and REST path segments now use the correct spelling.

### [01-02] Remove `AuthorizedChallenges` legacy class and `toLegacy()` conversions
The legacy `AuthorizedChallenges` record and all `toLegacy()` conversion methods were
removed. Consumers migrated to the current domain types directly.

### [01-04] Fix `UserMfa.storeSeed()` parameter order
Parameter order corrected so that `(tenant, username, seed)` matches all call sites.
Call moved to `NewMfaStep.process()` after OTP verification, not before.

### [01-05] Add `jti` claim to all issued JWTs
Every access token and refresh token now includes a `jti` (JWT ID, RFC 7519 §4.1.7)
UUID claim. The `_oauth_session_token` table stores `jti` (UNIQUE) and `refresh_jti`
(UNIQUE), enabling revocation and introspection lookups by JTI.

### [01-06] Cleanup jobs for expired codes and sessions
Quarkus `@Scheduled` jobs added for `_oauth_temporal_codes` and `_oauth_session`
tables. Expired rows are deleted on a configurable interval to prevent unbounded DB growth.

---

## Phase 03 — OAuth 2.0 Extensions (completed items)

### [03-01] Pushed Authorization Requests — PAR (RFC 9126)
`PushAuthorizationUsecase` and `ResolveParRequestUsecase` implemented in the `par`
bounded context. `_oauth_par_request` table with single-use flag and expiry.
The `pushedAuthorizationRequestEndpoint` is advertised in the discovery document.
(**Note:** REST driver and client authentication enforcement are tracked as PLAN-16.)

### [03-02] Dynamic Client Registration — DCR (RFC 7591 / RFC 7592)
`RegisterClientUsecase`, `UpdateDynamic`, `ReadDynamic`, `DeleteDynamic` implemented
in the `client` bounded context. `ClientRegisterController` exposes:
- `POST /register` — register new client
- `GET /register/{id}` — read registration
- `PUT /register/{id}` — update registration
- `DELETE /register/{id}` — delete registration
Registration access token (`registration_access_token`) enforced for management endpoints.

---

## Phase 04 — Advanced Authentication (fully completed)

### [04-01] WebAuthn / Passkeys (FIDO2)
Full FIDO2 ceremony lifecycle implemented in the `webauthn` bounded context:
- Registration: `InitiateWebAuthnRegistration` → `CompleteWebAuthnRegistration`
- Authentication: `InitiateWebAuthnAuthentication` → `CompleteWebAuthnAuthentication`
`_oauth_webauthn_challenge` table stores pending challenges. `WebAuthnVerifierAdapter`
wraps the FIDO2 library. HTML driver integrates with the challenge-based step router.

### [04-02] Magic Links
Anti-enumeration magic-link authentication in the `magiclink` bounded context.
Single-use SHA-256 hashed token. `_oauth_magic_link` table with `used_at` flag.
Five gateways (send, verify, expire, load, hash). HTML + REST drivers.
Tenant feature flag (`magic_link_enabled`) controls availability.

### [04-03] MFA — TOTP (Time-based One-Time Password)
TOTP enrollment and verification in the `mfa` bounded context.
`OtpMfaService` manages seed generation, QR code delivery, and OTP verification.
`NewMfaStep` and `MfaStep` integrate with the `OidcStepRouter` as challenge steps.
MFA emits ACR=2 into the session and into the issued ID token.

### [04-04] Social / Delegated Login
Delegated authentication in the `delegatelogin` bounded context:
- **Google**: `GoogleDelegatedAccessProvider` — standard OIDC flow with Google's IdP
- **SAML**: `SamlDelegatedAccessProvider` — SAML 2.0 SP-initiated SSO
`DelegatedAccessExternalProvider` strategy interface selects provider by `source` enum.
(**Note:** providers are currently in `domain/provider/` — Hexagonal violation tracked as PLAN-21.)

---

## Phase 05 — BaaS Management API (completed items)

### [05-02] User Profile — Expanded
`ProfileService` application-layer facade in the `profile` bounded context.
Five HTML panels: personal data, password change, MFA setup, active sessions, OAuth apps.
`ProfileMeController` exposes REST endpoints under `/api/me/*`.
`SessionsAdapter` allows profile to list and revoke the user's own sessions.

### [05-03] User Invitation System
Complete invitation lifecycle in the `userinvitation` bounded context:
- `CreateInvitationUsecase`, `AcceptInvitationUsecase`, `ResendInvitationUsecase`, `CancelInvitationUsecase`
- Invitation email delivered via `InvitationMailGateway`
- Auto-login session created after successful acceptance
- Expiry enforced by TTL on the invitation token

---

## Architectural Analysis Reviewed

### [`ACCESS-BOUNDED-CONTEXT-ARCHITECTURE.md`] — Bounded context `features/access` review
Architectural analysis of the `features/access` bounded context completed.
Key outcomes:

| Recommendation | Status |
|---|---|
| Add `package-info.java` to all 16 OAuth bounded contexts | ✅ Done (this session) |
| Collapse redundant Gateway → Repository indirection | → PLAN-30 (pending) |
| Fix `domain/provider/` Hexagonal violation in `delegatelogin` | → PLAN-21 (pending) |
| Enforce boundary rules (no cross-context imports at domain level) | → PLAN-20 (pending) |
| Add VO wrappers for primitive fields where missing | → Low priority, no PLAN item |

---

## Coverage vs. `OIDC-TASKS.md`

| Task | Was | Now |
|---|---|---|
| TASK-01 PKCE | 50% | → PLAN-01 |
| TASK-02 Token Revocation | 0% | → PLAN-03 |
| TASK-03 Token Introspection | 0% | → PLAN-11 |
| TASK-04 Refresh Rotation | 0% | → PLAN-02 |
| TASK-05 Userinfo | 25% | → PLAN-05 |
| TASK-06 Discovery | 85% | → PLAN-06 |
| TASK-07 prompt/max_age | 25% | → PLAN-07 |
| TASK-08 Logout | 75% | → PLAN-08 |
| TASK-09 Audit Logging | 25% | → PLAN-15 |
| TASK-10 Step-up ACR | 50% | → PLAN-07 |
| TASK-11 Session SSO | 40% | → PLAN-09 |
| TASK-12 Client Credentials | 0% | → PLAN-12 |
| TASK-13 Consent Versioning | 0% | → removed (model handles versioning implicitly via record identity) |
| TASK-14 c_hash | 75% | → PLAN-24 |
| TASK-15 Back-channel Logout | 0% | → PLAN-08 |
| TASK-16 Device Authorization | 0% | → PLAN-13 |
| TASK-17 Email Verification | 10% | → PLAN-10 |
| TASK-18 DCR | 0% | ✅ **Implemented** (see Phase 03 above) |
