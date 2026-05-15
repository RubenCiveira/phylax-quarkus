# OAuth Feature — State, Functionalities & Technical Debt

> Analysis of all 16 bounded contexts under `net.civeira.phylax.features.oauth`.
> Generated: 2026-05-14. Complement to `OIDC.md` (protocol compliance table) and `OIDC-TASKS.md` (task backlog).

---

## Table of Contents

1. [Context Map](#1-context-map)
2. [Bounded Context Status](#2-bounded-context-status)
   - [authentication](#21-authentication)
   - [client](#22-client)
   - [user](#23-user)
   - [mfa](#24-mfa)
   - [consent](#25-consent)
   - [session](#26-session)
   - [tokensecurity](#27-tokensecurity)
   - [delegatelogin](#28-delegatelogin)
   - [webauthn](#29-webauthn)
   - [magiclink](#210-magiclink)
   - [device](#211-device)
   - [par](#212-par)
   - [profile](#213-profile)
   - [userinvitation](#214-userinvitation)
   - [oidc](#215-oidc)
   - [theme](#216-theme)
3. [Technical Debt Summary](#3-technical-debt-summary)
4. [Design Debt Summary](#4-design-debt-summary)
5. [Pending Feature Work](#5-pending-feature-work)

---

## 1. Context Map

```
AUTHENTICATION (orchestration hub)
├── → USER              credential validation (LoginUsecase)
├── → CLIENT            client resolution (ClientStoreGateway)
├── → SESSION           session lifecycle (SessionManager)
├── → TOKEN SECURITY    JWT issuance (JwtTokenBuilder)
├── → MFA               OTP verification (UserMfa)
├── → CONSENT           scope / terms / GDPR checks
├── → MAGICLINK         passwordless flow
├── → WEBAUTHN          FIDO2 flow
├── → DELEGATELOGIN     federation / SSO
├── → DEVICE            device-flow polling
└── → PAR               request_uri resolution

TOKEN SECURITY (cross-cutting)
├── ← AUTHENTICATION    all grant types
├── ← MFA               MFA token issuance
└── ← SESSION           JTI binding for revocation

PROFILE
└── → SESSION           session list / revocation
└── → MFA               enable / disable TOTP

USERINVITATION
└── → USER              JIT account provisioning

THEME
└── ← AUTHENTICATION    HTML page decoration
└── ← PROFILE           portal page decoration

OIDC  (read-only, config-derived)
PAR   (pre-processes for AUTHENTICATION)
```

---

## 2. Bounded Context Status

### 2.1 authentication

**Implemented:**
- Authorization Code Flow (GET/POST /authorize with challenge-step router)
- HTML step handlers: LoginStep, MfaStep, MfaSetup (NewMfaStep), NewPassStep, RecoverStep,
  RegistrationStep, ConsentStep, ScopeConsentStep, DelegatedStep, MagicLinkStep
- Token endpoint with strategy dispatch (PasswordGranter, RefreshGranter, MfaGranter,
  DelegatedAccessGranter)
- OIDC session lifecycle (SessionManager, OidcCookieManager)
- Domain events: LoginSucceeded, LoginFailed, UserLocked, UserUnlocked
- Exception hierarchy for all challenge types
- Userinfo endpoint (InformationController, partial)
- Back-channel logout dispatcher (BackChannelLogoutDispatcher)
- Device-flow endpoint (DevicesAccessController)

**Partially implemented:**
- PKCE enforcement: `PkceChallenge` VO exists and S256 hash helper is in `JwtTokenBuilder`,
  but `TemporalAuthCode` does not store the challenge, so token-endpoint verification cannot
  be completed (`TemporalAuthCode.codeChallenge` / `codeChallengeMethod` fields are missing).
- `prompt` parameter: parsing exists in `AuthRequest` but `prompt=none`, `prompt=login`,
  `max_age`, and `login_hint` handling are not enforced by the step router.
- `acr_values` / ACR claim: `acr` is present in emitted ID tokens, but step-up
  re-authentication driven by `acr_values` request parameter is not implemented.
- Front-channel logout: endpoint exists but notification fanout to all RPs is incomplete.
- Userinfo standard claims: partial mapping; `address`, `phone_number`, `birthdate` are absent.

**Technical debt:**
- `FrontAcessController` (typo in name — should be `FrontAccessController`) is a large class
  that mixes request routing, session bootstrapping, PAR resolution, and step dispatching.
  Should be split into smaller collaborators.
- `OidcStepRouter` dispatch is based on `StepName` enum but has no explicit fallback for
  unknown step names — an unknown step silently renders the login form, which could mask bugs.
- No rate limiting at the `/authorize` endpoint level (rate limiting exists at login step only).
- `BackChannelLogoutDispatcher` makes synchronous HTTP calls inline; should be fire-and-forget
  async (Vert.x event loop or CDI async event).
- Granters are CDI beans but are not registered via `@Any` qualifier — adding a new granter
  requires editing the dispatch map instead of just annotating the new class.

---

### 2.2 client

**Implemented:**
- Static client resolution via `ClientStoreGateway` / `ClientRetrieveAdapter`
- Per-user, per-client scope-consent delta computation (`ClientScopeConsentUsecase`)
- API key authentication (`ApiKeyData`, `ApiKeyStoreGateway`, `ApiKeyController`)
- Dynamic client CRUD use cases (register, read, update, delete) per RFC 7591/7592
- `ClientDetails` aggregate with grant type and scope allowlists

**Partially implemented:**
- Dynamic Client Registration (RFC 7591): use cases are present but there is no token-endpoint
  enforcement of `registration_access_token` expiry or rotation.
- Client authentication methods: `client_secret_basic` and `client_secret_post` are handled,
  but `private_key_jwt` (RFC 7523) and `tls_client_auth` (RFC 8705 mTLS) are absent.
- Scope intersection at token endpoint: `ClientDetails.allowedScopes` is defined but the
  intersection enforcement at code-exchange time is not consistently applied for all grant types.

**Technical debt:**
- `ClientDetails` has no `createdAt` / `updatedAt` audit fields.
- The `requirePkce` / `publicClient` flag is not modeled in `ClientDetails`; public-client
  PKCE enforcement cannot be driven by the domain model without it.
- `DynamicClientRequest` validation is dispersed across use cases rather than centralized in
  a domain validator — duplicate validation logic between register and update paths.
- No client secret rotation support; updating the client secret requires a full re-registration.

**Design debt:**
- The boundary between `ClientScopeConsentUsecase` (in this context) and
  `ScopesConsentUsecase` (in the Consent context) is unclear — both manage scope approval
  records for (user, client) pairs, creating a risk of drift between the two stores.
  A clear ownership decision is needed: either one context owns the record and the other
  delegates, or the two usecases operate on different granularities (client-level vs session-level).

---

### 2.3 user

**Implemented:**
- Credential validation via `LoginUsecase` → `LoginGateway` → `UserLoginAdapter`
- User registration flow (`RegisterUserUsecase`, `RegisterUserAdapter`)
- Password change (`ChangePasswordUsecase`, `ChangePasswordAdapter`)
- Mail notification listeners (registration, first-login, password-recovery)
- `ActiveUserFindService` shared helper

**Partially implemented:**
- Email verification after registration: gateway exists (`UserRegistrationMailGateway`) and
  mail is sent, but the verification link token and confirmation endpoint are absent — email
  addresses are marked verified immediately after registration without confirmation.
- Anti-enumeration on registration: configured in code but the control is not consistently
  applied for all error paths (duplicate email vs duplicate username may return different timings).

**Technical debt:**
- `UserLoginService` and `UserLoginAdapter` overlap in responsibility; the separation between
  them is not clearly justified — consolidate into one CDI bean.
- `LoginGateway` returns an untyped user snapshot; a dedicated `AuthenticationData` builder
  should live here rather than in the Authentication context.
- No max-attempts lockout logic in the domain — account locking is side-effected via events
  (`UserLocked` event) rather than enforced as a domain invariant on the `LoginUsecase` itself.

---

### 2.4 mfa

**Implemented:**
- TOTP enrollment (`UserMfa.setupMfa`) with QR code and base32 secret delivery
- TOTP verification (`UserMfa.verifyMfa` → `UserMfaGateway`)
- TOTP secret persistence (`UserMfaConfigAdapter`)
- MFA enable/disable lifecycle managed from Profile context via `MfaGateway`

**Partially implemented:**
- MFA recovery codes: there is no recovery-code generation or verification path — if a user
  loses their TOTP device, there is no self-service recovery option.
- TOTP window tolerance: the time-step tolerance (±1 window) is hardcoded in `OtpMfaService`
  without configuration.

**Technical debt:**
- `OtpMfaService` and `UserMfaConfigAdapter` are in the same infrastructure package but serve
  different concerns (algorithm vs persistence) — `OtpMfaService` belongs in an `algorithm/`
  sub-package or as a pure function to make testing without CDI easier.
- TOTP secret is stored encrypted via the platform crypto service but there is no documented
  key-rotation procedure for the encryption key — a compromised key has no recovery path.

**Design debt:**
- The MFA context has no domain entity modeling the enrollment state machine (not enrolled →
  pending setup → enrolled → disabled). The current boolean flag in the user record is
  insufficient to track partial enrollment (secret generated but not yet verified by the user).

---

### 2.5 consent

**Implemented:**
- OAuth scope consent delta computation and persistence (`ScopesConsentUsecase`)
- Terms-of-use acceptance with version-bump re-prompt (`TermsOfUseConsentUsecase`)
- GDPR purpose consent per-user per-purpose (`GdprConsentUsecase`)
- `RequiredConsentService` aggregating all three consent checks
- `NoOpScopeApprovalAdapter` as the default (disabled) approval workflow stub

**Partially implemented:**
- Scope approval workflow (`ScopeApprovalUsecase`): the domain model and gateway are defined,
  but `NoOpScopeApprovalAdapter` always returns "approved" — there is no real implementation
  or administrative UI for the approval workflow.
- Consent withdrawal: users can grant consent but there is no self-service path to withdraw
  a previously granted scope from within the Profile portal.
- GDPR data export and deletion: the consent records are managed but no export or erasure
  API is implemented.

**Technical debt:**
- `ScopesConsentUsecase` (in Consent) and `ClientScopeConsentUsecase` (in Client) operate on
  scope approval for the same (user, client) entity — see Client context design debt.
- The GDPR consent model assumes a flat list of purposes per tenant; hierarchical or
  category-based purposes (as required by some national GDPR implementations) are not supported.

---

### 2.6 session

**Implemented:**
- `SessionInfo` aggregate covering all session state needed for multi-step auth flows
- `TemporalAuthCode` single-use authorization code with expiry
- `SessionStoreGateway` (save, load, delete, updateTokenJtis)
- `TemporalKeysGateway` (store, retrieve with one-time-use semantics)
- SQL adapters for both gateways

**Partially implemented:**
- Concurrent update protection in `SessionStoreSqlAdapter`: optimistic locking is described
  in the design but the implementation should be verified for correctness under race conditions
  (back-button during MFA step can produce two concurrent POST requests for the same session).
- Session TTL enforcement: sessions have an expiry instant in `SessionInfo` but the SQL adapter
  may not have a scheduled cleanup job, leading to accumulation of expired rows.

**Technical debt:**
- `TemporalAuthCode` is missing `codeChallenge` and `codeChallengeMethod` fields (PKCE debt,
  see TASK-01 in OIDC-TASKS.md) — this is the single most critical missing field blocking PKCE.
- The session aggregate carries both transient challenge state and durable token-binding state
  (JTIs) in the same object, making it harder to reason about what survives session completion.
  Consider splitting into `AuthSession` (transient, deleted on code issue) and
  `TokenSession` (durable, used for refresh and revocation tracking).
- No session export or admin revocation API; `SessionsGateway` in the Profile context supports
  per-user listing/revocation but there is no administrative endpoint.

---

### 2.7 tokensecurity

**Implemented:**
- `JwtTokenBuilder`: access token, ID token, refresh token, MFA token, and challenge token
  generation with full standard and custom claim mapping
- `TokenSigner` gateway and `JoseTokenSigner` JOSE adapter
- JWKS endpoint (`JwksController`)
- Key configuration model (`KeyConfig`, `KeyInformation`, `JwkSet`, `KeyPair`)
- `TokenJwtCallerPrincipalFactory` for Quarkus JAX-RS bearer authentication
- `IntrospectTokenUseCase` and `IntrospectionController` (RFC 7662)
- `TokenStoreSqlAdapter` and `TokenLookupSqlAdapter`

**Partially implemented:**
- Token revocation (RFC 7009): `TokenRevocationGateway` is defined and `TokenStoreSqlAdapter`
  exists, but there is no POST /revocation endpoint REST driver in this context; the
  `/revocation` URL in the discovery document points to an incomplete handler.
- Access token validation at resource servers: `JwtTokenBuilder` can verify JWTs, but there
  is no published client library or documented introspection procedure for external resource
  servers to validate tokens issued by Phylax.
- at_hash / c_hash: hash helpers exist in `JwtTokenBuilder` but inclusion in the ID token
  is conditional and not verified to be present in all required scenarios.
- Token refresh rotation: `RefreshGranter` issues a new refresh token but the old refresh
  token is not invalidated in the token store — replay of a previous refresh token is possible.

**Technical debt:**
- `JwtTokenBuilder` is a large service class (500+ lines) combining token building, claim
  mapping, PKCE helpers, and token verification. Should be decomposed into
  `TokenFactory` (building), `ClaimsMapper` (claim assembly), and `TokenVerifier` (parsing).
- Algorithm agility: RS256 is hardcoded in `JoseTokenSigner` — ES256 is mentioned in
  discovery but not actually available without configuration changes and code updates.
- Key rotation: no automated key rotation procedure or graceful dual-key window (serve new
  key in JWKS before switching signing to allow verifiers to cache the new key).

---

### 2.8 delegatelogin

**Implemented:**
- `DelegateLogin` orchestrator (initiation + callback)
- `GoogleDelegatedAccessProvider` (OIDC/Google OAuth)
- `SamlDelegatedAccessProvider` (SAML 2.0 SP-initiated SSO)
- Provider configuration loading (`DelegatedAccessProviderGateway`)
- State storage for CSRF protection (`DelegatedStoreGateway`)
- Username resolution and JIT provisioning (`DelegateLoginAdapter`)
- HTML driver endpoints (initiation redirect, callback handler)

**Partially implemented:**
- OIDC generic provider: only Google is implemented as an OIDC provider — there is no generic
  OIDC provider implementation for arbitrary OIDC IdPs (Okta, Azure AD, Keycloak, etc.).
- SAML attribute mapping: `SamlDelegatedAccessProvider` parses the assertion but attribute
  mapping to local user profile fields (email, name, roles) may be incomplete or hardcoded.
- JIT provisioning policies: auto-provisioning creates an account but role assignment for
  JIT users is not configurable per provider — all JIT users get the same default role set.
- Error handling on callback: if the provider returns an error response (user denied, server
  error), the callback handler may not produce a user-friendly error page.

**Design debt:**
- Provider implementations (`GoogleDelegatedAccessProvider`, `SamlDelegatedAccessProvider`)
  live inside the `domain/provider/` sub-package, which violates the Hexagonal pattern —
  provider implementations are driven adapters and should be in `infrastructure/driven/provider/`.
  Moving them would clean up the domain model and make the dependency direction explicit.

---

### 2.9 webauthn

**Implemented:**
- Full ceremony domain model (`WebAuthnChallenge`, `WebAuthnCredential`)
- Four use cases: BeginRegistration, FinishRegistration, BeginAuthentication, FinishAuthentication
- Gateway interfaces for challenge, credential, and verifier
- `WebAuthnController` REST driver
- SQL adapters for challenge and credential persistence
- `WebAuthnVerifierAdapter` wrapping a FIDO2 verification library

**Partially implemented:**
- WebAuthn WEBAUTHN challenge step integration with `OidcStepRouter`: the use cases and REST
  endpoints are implemented, but wiring the WEBAUTHN challenge into the HTML step-router
  (`OidcStepRouter`) as a full authentication path (instead of a supplementary verification)
  may not be complete.
- Credential management in Profile portal: users cannot view, rename, or delete registered
  WebAuthn credentials from the self-service portal.
- Attestation verification policy: `WebAuthnVerifierAdapter` verifies signatures but the
  attestation statement policy (allow-list of AAGUIDs, attestation type enforcement) is not
  configurable.

**Technical debt:**
- Missing `package-info.java` for most sub-packages within `webauthn/` (application/usecase
  sub-packages and infrastructure sub-packages are undocumented).
- Sign-count update after authentication: `updateSignCount` in `WebAuthnCredentialGateway` is
  defined but it must be verified that it is actually called in `FinishAuthenticationUsecase`
  — if skipped, cloned-authenticator detection is disabled.

---

### 2.10 magiclink

**Implemented:**
- `MagicLink` aggregate with hash-based token storage and single-use enforcement
- `RequestMagicLinkUsecase` with anti-enumeration (silent failure for unknown email)
- `VerifyMagicLinkUsecase` with expiry, used-flag, and hash validation
- `MagicLinkVerifyHtml` driver for the verification redirect
- `MagicLinkRequestController` REST driver
- All five gateway contracts and their SQL adapters
- `MagicLinkEnabledGateway` feature-flag support

**Partially implemented:**
- OIDC session resume after verification: the `MagicLinkVerifyResult` must restore the original
  OIDC session so the token endpoint can issue tokens — this requires the original `AuthRequest`
  parameters to be persisted in the `MagicLink` record and correctly recovered on verification.
  If the magic link is opened in a different browser from the one that initiated the flow, the
  OIDC session cookie will be absent, and the flow may fail.
- Magic link from outside a OIDC flow: there is no path for a standalone email-based login that
  does not originate from an authorization request (i.e., direct link from an email marketing
  campaign to the application).

**Technical debt:**
- Token hashing algorithm is not documented in the gateway contract — if the storage adapter
  uses a non-constant-time comparison, timing attacks against the token hash are possible.
- Link expiry is hardcoded; it should be configurable per client or tenant.
- No throttle on the `RequestMagicLinkUsecase` endpoint — an attacker can spam the target's
  inbox without being rate-limited at the domain layer (relies solely on infrastructure-level
  rate limiting if present).

---

### 2.11 device

**Implemented:**
- `DeviceAuthorization` aggregate with device_code / user_code pair
- `DeviceAuthorizationStatus` enum (PENDING, APPROVED, DENIED)
- `DeviceAuthorizationException` encoding all RFC 8628 error responses
- `DeviceAuthorizationService` (authorize, pollStatus, approve, deny)
- `DeviceAuthorizationSqlAdapter`
- `DeviceVerificationHtml` user-facing verification page

**Partially implemented:**
- Token endpoint device_code exchange: `DevicesAccessController` is in the Authentication
  context and handles the device authorization endpoint, but the actual `device_code` grant
  type exchange at POST /token may not be fully wired to issue tokens — the device gets
  APPROVED but may not receive tokens if the granter is missing or incomplete.
- Poll-interval enforcement: the `slow_down` response requires tracking the last-poll timestamp
  per device_code. Whether `DeviceAuthorizationSqlAdapter` stores this and whether the service
  returns `slow_down` correctly is unverified.
- User code display: the user_code format (e.g., XXXX-XXXX) and character set (RFC 8628 §6.1
  recommends excluding visually ambiguous characters) may not be enforced.

**Technical debt:**
- The REST driver for the device authorization endpoint lives in the Authentication context
  (`DevicesAccessController`) rather than in this context — the context boundary is blurred.
  The granter for device_code token exchange also lives in Authentication. Consider whether
  Device should own its token-exchange path or remain a dependency of Authentication.
- No integration test coverage for the device flow (not included in the `oidc-flow` test group).

---

### 2.12 par

**Implemented:**
- `ParRequest` aggregate with single-use flag and expiry
- `ParException` with all RFC 9126 error codes
- `PushAuthorizationUsecase` (validate, generate request_uri, store)
- `ResolveParRequestUsecase` (retrieve and mark used)
- `ParRequestSqlAdapter`

**Partially implemented:**
- REST driver for the PAR endpoint: there is no `ParController` in this context's
  `infrastructure/driver/rest/` — the PAR POST endpoint must exist somewhere in Authentication
  infrastructure, but its location is not obvious from the package structure.
- mTLS binding for request_uri: RFC 9126 §10 recommends binding the request_uri to the
  client's mTLS certificate — not implemented.
- Client authentication at the PAR endpoint: `PushAuthorizationUsecase` receives a
  `PushAuthorizationParams` DTO but it is unclear whether client authentication (secret or JWT)
  is enforced before invoking the use case.

**Technical debt:**
- Missing all sub-package `package-info.java` files (application/usecase sub-packages).
- No integration test coverage for PAR.

---

### 2.13 profile

**Implemented:**
- `ProfileService` facade with getProfile, saveProfile, enableMfa, disableMfa,
  changePassword, listSessions, revokeSession
- `ProfileHtmlController` with five self-service panels (View, Edit, ChangePassword, MFA, Sessions)
- `ProfileMeController` REST driver for GET /userinfo and programmatic updates
- SQL adapters for all four gateway contracts (Profile, Mfa, Password, Sessions)
- `HtmlEscape` XSS protection utility

**Partially implemented:**
- Email change flow: the profile edit panel accepts a new email but there is no email
  verification step — the new address is used immediately without confirming ownership.
- WebAuthn credential management panel: passkey listing, naming, and deletion are absent.
- Avatar/picture update: `OidcProfile` carries a picture URL but there is no upload mechanism.
- Notification preferences: no self-service UI for managing email notification opt-ins.

**Technical debt:**
- `HtmlEscape` is a utility class inside `infrastructure/driver/html/panels/` — it should
  be in a shared utilities package to avoid duplication if other HTML drivers need it.
- `ProfileHtmlController` serves multiple panels from a single controller class; consider
  splitting into per-panel controllers for cleaner routing and testability.
- `ProfileMeController` partially duplicates the OIDC userinfo endpoint from the Authentication
  context (`InformationController`) — the claim set served by both endpoints should be
  consistent and driven by the same service.

---

### 2.14 userinvitation

**Implemented:**
- `InvitationCreateUsecase` with 7-day expiry and email delivery
- `InvitationAcceptUsecase` with token validation and user provisioning
- `InvitationResendUsecase` with token regeneration
- `InvitationCancelUsecase`
- `UserInvitationSession` for OIDC-flow auto-login after acceptance
- SQL adapters for all gateway contracts

**Partially implemented:**
- Auto-login after acceptance: the `UserInvitationSession` design assumes the invitation is
  accepted within the same browser session that initiated the OIDC flow. Cross-browser or
  cross-device acceptance (invitee opens the link on a different device) will fail to
  resume the OIDC session.
- Pre-assigned roles via invitation: the domain model may not carry role assignments from
  the invitation record to the provisioned user — roles may be assigned via a separate
  post-provisioning step that is not automated.
- Invitation listing and management UI: administrative endpoints for listing, filtering, and
  bulk-cancelling invitations exist at the gateway level but no HTML or REST driver is present
  in this context (likely delegated to the access management feature).

**Technical debt:**
- `InvitationCreateUsecase` hardcodes 7-day expiry — should be a configurable property per
  tenant or client.
- Token hashing is repeated across MagicLink, UserInvitation, and Session contexts without a
  shared domain service — introduce a `SecureTokenService` in a shared module.
- No duplicate-invitation guard across all states (a cancelled invitation followed by a new
  one for the same email should be allowed, but an already-pending invitation should be blocked).

---

### 2.15 oidc

**Implemented:**
- `OpenIdConfiguration` discovery document model with all major fields
- `MtlsEndpointAliases` nested VO
- `OpenIdConfigurationController` serving GET /.well-known/openid-configuration
- CORS headers for cross-origin fetching by browser-based RPs

**Partially implemented / out of sync:**
- Discovery completeness: several implemented endpoints (PAR, device_authorization, back-channel
  logout) may not be consistently reflected in `OpenIdConfiguration` — the document needs an
  audit against the actual enabled feature set.
- `code_challenge_methods_supported`: should advertise `["S256"]` but may currently be absent
  or incorrect (blocked by PKCE implementation completeness).
- `backchannel_logout_supported` / `frontchannel_logout_supported` flags may not match the
  actual implementation state.
- Discovery is statically assembled at startup — if features are dynamically enabled per tenant,
  the discovery document does not reflect per-tenant capabilities.

**Design debt:**
- The discovery document should be assembled by querying each feature's actual configuration
  rather than being hardcoded. A `DiscoveryContributor` interface implemented by each context
  would allow the OIDC context to be decoupled from knowledge of which features are enabled.

---

### 2.16 theme

**Implemented:**
- `DecorateHtml` two-pass HTML rendering orchestrator
- `DecoratePageGateway` port for inner-content and outer-layout rendering
- Built-in fallback layout when no custom tenant theme is configured
- Used by all HTML drivers in Authentication and Profile contexts

**Partially implemented:**
- Per-client theme override: the current model resolves one theme per tenant. Some deployments
  may require per-client visual customization (different branding per application).
- Dark-mode / responsive layout support depends entirely on the tenant's template; the platform
  does not enforce any responsive-design requirements.

**Technical debt:**
- `DecorateHtml` has no caching layer for rendered outer layouts — each request re-renders
  the tenant layout even when the theme has not changed. A short-lived request-scoped or
  application-scoped cache keyed by (tenant, theme version) would reduce template-engine
  overhead under load.
- The two-pass rendering model doubles template-engine invocations per page — if the outer
  layout is large, this has a measurable latency cost. Should be profiled under load.

---

## 3. Technical Debt Summary

| Area | Item | Severity | Context |
|------|------|----------|---------|
| `TemporalAuthCode` missing PKCE fields | PKCE verification impossible | **Critical** | session |
| Refresh token not invalidated on rotation | Refresh replay attacks possible | **Critical** | tokensecurity |
| No POST /revocation REST driver | Token revocation endpoint non-functional | **High** | tokensecurity |
| `JwtTokenBuilder` god class | Fragile; hard to test in isolation | **High** | tokensecurity |
| `FrontAcessController` typo + size | Maintenance burden; routing, session, PAR mixed | **High** | authentication |
| Provider impls in domain/ | Violates Hexagonal; hard to replace providers | **High** | delegatelogin |
| `BackChannelLogoutDispatcher` sync HTTP | Blocks request thread; risk of cascade failure | **High** | authentication |
| Email verification missing | Email addresses unverified post-registration | **High** | user |
| MFA recovery codes absent | No self-service MFA recovery path | **High** | mfa |
| Token hashing duplicated (MagicLink/Invitation/Session) | No shared secure-token service | **Medium** | cross-cutting |
| Algorithm hardcoded RS256 in JoseTokenSigner | ES256 not actually available | **Medium** | tokensecurity |
| No key rotation procedure | Security risk on key compromise | **Medium** | tokensecurity |
| Scope consent ownership blur (Consent vs Client) | Risk of drift between stores | **Medium** | client/consent |
| Session cleanup job absent | Expired session row accumulation | **Medium** | session |
| `UserLoginService` / `UserLoginAdapter` overlap | Redundant CDI bean | **Low** | user |
| `HtmlEscape` misplaced in panels/ | Duplication risk in other HTML drivers | **Low** | profile |
| `ProfileMeController` / `InformationController` claim duplication | Inconsistent userinfo | **Low** | profile/auth |
| No integration tests for PAR, Device, WebAuthn | Regressions undetected | **High** | test coverage |

---

## 4. Design Debt Summary

| Item | Description | Contexts |
|------|-------------|----------|
| Challenge step-router fallback | Unknown StepName silently renders login — masking routing bugs | authentication |
| Granter dispatch via map vs `@Any` CDI | Adding granters requires editing the dispatch map | authentication |
| MFA enrollment state machine | Boolean flag insufficient; no PENDING_SETUP state | mfa |
| `AuthSession` vs `TokenSession` split | Single `SessionInfo` conflates transient and durable state | session |
| Discovery not feature-driven | `OpenIdConfiguration` is static; does not reflect per-tenant feature state | oidc |
| `DiscoveryContributor` SPI absent | Each new feature must manually register in the discovery class | oidc |
| Per-client theme override | Only per-tenant theming; per-client branding impossible without code changes | theme |
| OIDC generic provider | Only Google OIDC + SAML; no generic OIDC provider for other IdPs | delegatelogin |
| Device token exchange boundary | Device grant type exchange lives in Authentication context, not Device context | device |
| PAR REST driver location | No `ParController` visible in par/ context — unclear where the endpoint lives | par |
| Cross-browser invitation / magic-link | Flow fails if link opened on different device from OIDC flow initiator | magiclink / userinvitation |
| Client credentials grant absent | Machine-to-machine flow only partially covered by API keys | client / authentication |

---

## 5. Pending Feature Work

| Feature | RFC / Spec | Blocking On | Priority |
|---------|-----------|-------------|----------|
| PKCE full enforcement | RFC 7636 | `TemporalAuthCode` fields + `TokenController` verification | **P1** |
| Refresh token invalidation on rotation | OAuth 2.0 Security BCP | `TokenStoreGateway.revoke()` call in `RefreshGranter` | **P1** |
| POST /revocation endpoint | RFC 7009 | REST driver + `TokenRevocationGateway` wiring | **P1** |
| `prompt=none` / `prompt=login` enforcement | OIDC Core §3.1.2.1 | `OidcStepRouter` param handling | **P1** |
| MFA recovery codes | — | New `MfaRecoveryGateway` + UI step | **P1** |
| Email verification after registration | — | Verification-token flow in User context | **P1** |
| Generic OIDC delegated provider | OIDC Core | New `OidcDelegatedAccessProvider` implementation | **P2** |
| Client credentials grant (machine-to-machine) | RFC 6749 §4.4 | New `ClientCredentialsGranter` strategy | **P2** |
| `max_age` / `acr_values` enforcement | OIDC Core §3.1.2.1 | `OidcStepRouter` + `AuthenticateUser` changes | **P2** |
| Front-channel / back-channel logout | OIDC Session 1.0 | `BackChannelLogoutDispatcher` async + fanout completion | **P2** |
| Token introspection (RFC 7662) | RFC 7662 | `IntrospectionController` fully wired (partial) | **P2** |
| Key rotation (JWKS dual-key window) | OIDC Core §10 | Key management in `KeyConfig` / `JoseTokenSigner` | **P2** |
| WebAuthn credential management in Profile | W3C WebAuthn | New panel + gateway in Profile + WebAuthn contexts | **P2** |
| PAR REST driver and client auth | RFC 9126 | `ParController` + client authentication enforcement | **P2** |
| Device flow token exchange complete | RFC 8628 | Verify/fix `DeviceAuthorizationService.pollStatus` + granter | **P2** |
| Consent withdrawal self-service | GDPR Art.7(3) | New `revokeConsent` operation in Profile + Consent contexts | **P3** |
| Per-tenant PKCE enforcement policy | — | `ClientDetails.requirePkce` field + `AuthorizeHtml` check | **P3** |
| Discovery per-tenant feature flags | OIDC Discovery 1.0 | `DiscoveryContributor` SPI design | **P3** |
| `private_key_jwt` client authentication | RFC 7523 | New client-auth strategy in token endpoint | **P3** |
| mTLS client authentication | RFC 8705 | `TlsClientAuthAdapter` + mTLS endpoint aliases | **P3** |
| Step-up authentication (ACR) | OIDC Core §3.1.2.1 | `acr_values` → challenge injection in step-router | **P3** |
| Audit log completeness | — | OidcEvent coverage for all sensitive operations | **P3** |
| `address` / `phone` userinfo claims | OIDC Core §5 | `JwtTokenBuilder` claim mapping extension | **P3** |
