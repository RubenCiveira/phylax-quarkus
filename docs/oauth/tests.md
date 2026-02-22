# OAuth / OIDC — Test suite

## Overview

All OIDC integration tests live in `net.civeira.phylax.testing.oauth` and use
`@QuarkusTest` with a full Quarkus application context. Real gateway implementations
are replaced by configurable in-memory alternatives (`@Alternative @Priority(1)`), so
the tests verify the HTTP/OIDC protocol layer without hitting a database.

Run the full suite with:
```bash
mvn test -Dgroups="oidc-flow"
```

Current count: **62 tests, 0 failures**.

---

## Test infrastructure

| Class | Role |
|-------|------|
| `OidcIntegrationTestBase` | Base class: injects all scenario gateways, resets state `@BeforeEach`, provides helpers `decodeChallenge()`, `decodeToken()`, `assertTokenScopes()` |
| `OidcFlowClient` | RestAssured wrapper — one method per user action (submit login, submit MFA, submit consent, exchange code, revoke, logout, introspect…) |
| `ScenarioLoginGateway` | Configurable `LoginGateway` — `whenValidate()` / `whenPreAuth()` control what `AuthenticationResult` is returned |
| `ScenarioConsentGateway` | Configurable `ConsentGateway` — `whenPending()` controls which `PendingConsent` is returned; tracks `lastRelyingParty`, `acceptedCount` |
| `ScenarioScopeConsentGateway` | Configurable `ClientScopeConsentGateway` |
| `ScenarioMfaGateway` | Configurable MFA gateway — accepts `MFA_CODE`, rejects anything else |
| `ScenarioChangePasswordGateway` | Configurable `ChangePasswordGateway` — `setAllowRecover()` controls recover link visibility |
| `ScenarioRegisterUserGateway` | Configurable `RegisterUserGateway` — `setAllowRegister()` controls register link visibility |
| `ScenarioDelegateGateway` | Configurable `DelegateLoginGateway` — `setProviderEnabled()` toggles provider list; `saveToken()`/`loadToken()` manage delegated codes; `whenResolveUsername()` controls username resolution |
| `ScenarioClientStoreGateway` | Configurable `ClientStoreGateway` — `setAllowedScopes()` |
| `OidcTestFixtures` | Shared constants: `TENANT`, `CLIENT_ID`, `USERNAME`, `PASSWORD`, `MFA_CODE`, `RECOVER_CODE`, `REGISTER_CODE`, `SCOPE`, `STATE`… |

---

## Existing tests

### `OidcSmokeTest` (1 test)

| Test | What it verifies |
|------|-----------------|
| `applicationStarts` | Quarkus boots with all CDI `@Alternative` beans resolved and no ambiguity errors |

---

### `LoginFlowTest` (7 tests)

Covers the main Authorization Code flow entry point.

| Test | What it verifies |
|------|-----------------|
| `loginForm_isRendered` | GET `/auth` returns 200 with a login form; no `AUTH_SESSION_ID` cookie is set |
| `wrongCredentials_staysOnForm` | POST with wrong password returns 200 login form with no redirect |
| `correctCredentials_redirectsWithCode` | POST with correct credentials returns 302 with `code=` and `state=` in Location; `AUTH_SESSION_ID` cookie is set |
| `authCode_isOneTimeUse` | The same auth code can only be exchanged once; the second attempt returns 401 |
| `existingSession_skipLoginForm` | With a valid `AUTH_SESSION_ID` cookie, GET `/auth` shows a confirm page (not a login form); confirm POST returns 302 with a new code |
| `invalidPreSession_staysOnLoginForm` | Submitting an MFA step with a tampered `PRE_SESSION_ID` cannot decode the challenge; fallback login with no credentials stays on the login form |
| `missingAuthSession_onConfirmPost_staysOnLoginForm` | Session-confirm POST without `AUTH_SESSION_ID` cookie falls back to a login attempt; with wrong credentials stays on the login form |

---

### `AuthCodeExchangeTest` (3 tests)

| Test | What it verifies |
|------|-----------------|
| `authCode_exchange_returnsToken` | POST `/token` with a valid code returns 200 with `access_token`, `refresh_token` and `id_token`; the `id_token` contains the correct `sub` (username) and `azp` (client_id) claims |
| `pkce_wrongVerifier_returns401` | Exchanging an auth code with a wrong `code_verifier` returns 401 (PKCE mismatch) |
| `introspect_returns403` | POST `/introspect` returns 403 (not yet implemented) |

---

### `PasswordGrantTest` (3 tests)

| Test | What it verifies |
|------|-----------------|
| `passwordGrant_correctCredentials_returnsToken` | POST `/token grant_type=password` returns 200 with `access_token`, `refresh_token`, `token_type=Bearer`; `sub` claim matches username |
| `passwordGrant_wrongCredentials_returns401` | Wrong password returns 401 |
| `passwordGrant_unknownClient_returns401` | Unknown `client_id` returns 401 |

---

### `RefreshTokenTest` (2 tests)

| Test | What it verifies |
|------|-----------------|
| `refreshToken_valid_returnsNewToken` | POST `/token grant_type=refresh_token` with a valid refresh token returns 200 with a new `access_token` |
| `refreshToken_invalid_returns401` | An invalid refresh token returns 401 |

---

### `ScopesTest` (5 tests)

| Test | What it verifies |
|------|-----------------|
| `passwordGrant_requestedScopesPresentInToken` | Requested scopes `openid profile` appear in the access token `scope` claim |
| `passwordGrant_scopeFilteredByClientConfig` | When the client allows only `openid profile`, requesting `email` as well results in a token without `email` |
| `passwordGrant_wildcardClient_allScopesGranted` | A client configured with scope `*` grants all requested scopes including custom ones |
| `refreshToken_preservesScopes` | A refresh token exchange with scope `openid profile` returns a token with those scopes |
| `authCodeFlow_scopesFromRequest` | Auth code flow with `scope=openid profile email` produces tokens with those scopes |

---

### `MfaFlowTest` (3 tests)

| Test | What it verifies |
|------|-----------------|
| `mfaRequired_showsMfaForm` | When login returns `mfaRequired`, the response is 200 with a MFA form; `PRE_SESSION_ID` encodes `MFA` challenge |
| `mfa_wrongCode_staysOnForm` | Submitting wrong OTP returns 200 with the MFA form and an error message |
| `mfa_correctCode_completesFlow` | Correct OTP returns 302 with auth code; gateway receives correct `tenant`, `username` and `otp` |

---

### `NewMfaFlowTest` (4 tests)

| Test | What it verifies |
|------|-----------------|
| `newMfaRequired_showsMfaSetupPage` | When login returns `newMfaRequired`, response contains the MFA setup page with QR redirect and `otp_code` field |
| `mfaSetupSelector_isRendered` | GET `/mfa-setup` returns a QR code image (`image/*` content type) |
| `newMfa_verify_wrongCode_staysOnForm` | Wrong OTP during setup returns 200 with error message |
| `newMfa_verify_correctCode_completesFlow` | Correct OTP during setup returns 302 with auth code |

---

### `NewPasswordFlowTest` (3 tests)

| Test | What it verifies |
|------|-----------------|
| `newPasswordRequired_showsChangeForm` | When login returns `newPasswordRequired`, response contains `old_pass` and `new_pass` fields; `PRE_SESSION_ID` encodes `FRESH_PASSWORD` challenge |
| `newPass_wrongOldPass_staysOnForm` | When `forceUpdatePassword` returns `false`, stays on form with error message |
| `newPass_correctData_completesFlow` | When `forceUpdatePassword` returns `true`, returns 302 with auth code; gateway receives correct `tenant` and `username` |

---

### `ConsentFlowTest` (3 tests)

| Test | What it verifies |
|------|-----------------|
| `consentRequired_showsConsentForm` | When login returns `consentRequired`, form contains `consent` checkbox and the RP's consent text |
| `consent_notAccepted_staysOnForm` | Submitting `consent=off` stays on the consent form |
| `consent_accepted_completesFlow` | Submitting `consent=on` returns 302 with auth code; gateway records `tenant`, `username` and `relyingParty` |

---

### `ConsentMultiRpFlowTest` (1 test)

| Test | What it verifies |
|------|-----------------|
| `multiRp_sequentialConsent_bothAccepted_completesFlow` | Login requires consent for two relying parties sequentially; accepting rp-a shows the rp-b form (200), accepting rp-b completes the flow (302 with auth code); `acceptedCount == 2` |

---

### `ScopeConsentFlowTest` (3 tests)

| Test | What it verifies |
|------|-----------------|
| `scopeConsentRequired_showsScopeConsentForm` | When login returns `clientScopeConsentRequired`, form lists the pending scopes and contains `step=scope_consent` |
| `scopeConsent_accepted_completesFlow` | Accepting scope consent returns 302; gateway records `tenant`, `username`, `clientId` and accepted scopes |
| `scopeConsent_noPendingScopes_loginPassesThroughDirectly` | When gateway returns no pending scopes, the flow completes after one scope consent POST |

---

### `RecoverPasswordFlowTest` (5 tests)

| Test | What it verifies |
|------|-----------------|
| `showRecoverForm_isRendered` | POST `step=show-recover` returns 200 with a form containing a `username` field |
| `recoverPage_withCode_isRendered` | GET `/recover?username=...&recovercode=...` returns 200 with `code` and `password` fields |
| `recover_validCode_changesPassword` | POST `/recover` with a valid code returns 302; gateway receives correct `tenant` and `recoverCode` |
| `recover_invalidCode_showsError` | POST `/recover` with an invalid code returns 200 with a "Wrong code" error |
| `showLoginForm_allowRecover_false_hidesRecoverLink` | When `allowRecover=false`, the login form does not contain the recover link (`show-recover`) |

---

### `DelegatedLoginFlowTest` (3 tests)

Covers the Social / SSO delegated login callback path (`DelegatedControllerPart`).

| Test | What it verifies |
|------|-----------------|
| `loginForm_showsDelegatedProviderButton` | When a delegated provider is enabled, the login form contains `social-form-google` and the `delegated-login` step element |
| `delegatedCallback_resolvesUsername_and_completesFlow` | With a pre-stored delegated token and a `whenResolveUsername` supplier returning the test user, POSTing `step=query-delegated` returns 302 with an auth code |
| `delegatedCallback_unknownCode_staysOnLoginForm` | When no token is stored for the given code, the fallback login (with wrong credentials) stays on the login form (200); no auth code is issued |

---

### `RegisterUserFlowTest` (10 tests)

| Test | What it verifies |
|------|-----------------|
| `showRegisterForm_allowRegister_showsForm` | POST `step=show-register` returns 200 with `reg_email` field and `do_register` step |
| `register_pending_showsPendingPage` | When gateway returns `PENDING`, response shows the user's email on a "check your email" page; `requestCalls` incremented |
| `register_ok_completesFlow` | When gateway returns `OK(username)`, returns 302 with auth code |
| `register_cancel_showsRegisterFormWithError` | When gateway returns `CANCEL`, re-shows the registration form |
| `verify_validCode_completesFlow` | POST `/register` with valid code returns 302; gateway receives correct `code` |
| `verify_invalidCode_showsVerifyFormWithError` | POST `/register` with invalid code returns 200 with `regcode` field |
| `showRegisterVerify_showsVerifyForm` | GET `/register?email=...&regcode=...` returns 200 with `regcode` field pre-filled |
| `showLoginForm_allowRegister_false_hidesRegisterLink` | When `allowRegister=false`, the login form does not contain the register link (`show-register`) |
| `register_ok_withMfaChallenge_showsMfaFormThenCompletesFlow` | When registration succeeds but pre-auth requires MFA, the MFA form is shown (200); correct OTP then returns 302 with auth code |
| `register_ok_withMfaAndConsent_chainedChallenges` | Registration OK → pre-auth returns MFA → MFA form shown → correct OTP → pre-auth returns consent → consent form shown → consent accepted → 302 with auth code; full three-step post-registration chain |

---

### `EndToEndFlowTest` (4 tests)

Multi-challenge flows that chain two or more steps.

| Test | What it verifies |
|------|-----------------|
| `login_then_consent_then_mfa` | After login → consent accepted → MFA verified → 302 with code. `PRE_SESSION_ID` encodes `USE_CONSENT` after login and `MFA` after consent |
| `login_then_newPass_then_consent` | After login → new password set → consent accepted → 302 with code. `PRE_SESSION_ID` encodes `FRESH_PASSWORD` after login and `USE_CONSENT` after password change |
| `e2e_scopeConsent_accepted_completesLogin` | After login → scope consent accepted → 302 with code; `PRE_SESSION_ID` encodes `CLIENT_CONSENT` challenge; scope consent gateway records the client and scopes |
| `e2e_scopeConsent_denied_returnsToLoginForm` | After login → user denies scope consent (posts `step=start`) → 200 login form with no code issued; scope consent gateway records 0 accepted |

---

### `SessionManagementTest` (2 tests)

| Test | What it verifies |
|------|-----------------|
| `revocation_returns200` | POST `/revocation` with a `PRE_SESSION_ID` cookie returns 200 |
| `logout_clears_session_and_redirects` | GET `/logout?post_logout_redirect_uri=...` returns 302 to the redirect URI (query params stripped); `AUTH_SESSION_ID` cookie is cleared; a subsequent auth flow with the old session shows the login form |

---

## Regression test battery

The following scenarios define the **minimum set that must pass** before any change to the
OAuth module is considered safe to merge. They map directly to the existing tests plus the
critical paths not yet automated.

### Must-pass automated tests (62 tests, `mvn test -Dgroups="oidc-flow"`)

| Area | Critical scenarios covered |
|------|---------------------------|
| Login | Form rendering, wrong credentials, correct credentials → code, one-time-use code, existing session re-use |
| Session security | Tampered `PRE_SESSION_ID` stays on login form, absent `AUTH_SESSION_ID` on confirm stays on login form |
| Token exchange | Code → tokens (access, refresh, id), correct claims (`sub`, `azp`) |
| PKCE | Wrong `code_verifier` → 401 |
| Token introspection | `POST /introspect` → 403 (reserved) |
| Password grant | Correct credentials → token, wrong credentials → 401, unknown client → 401 |
| Refresh token | Valid refresh → new token, invalid refresh → 401 |
| Scopes | Scopes present in token, filtered by client config, wildcard client, refresh preserves scopes, auth code flow scopes |
| MFA | Form shown, wrong OTP stays on form, correct OTP → code |
| New MFA setup | Setup page rendered, QR image served, wrong OTP stays on form, correct OTP → code |
| New password | Form shown, wrong old password stays on form, correct change → code |
| Consent (RP terms) | Form shown with consent text, not accepted stays on form, accepted → code with `relyingParty` recorded |
| Multi-RP consent | Two sequential RPs: accept rp-a → rp-b form, accept rp-b → code; `acceptedCount == 2` |
| Scope consent | Form shown with scope list, accepted → code with scopes recorded, denied → login form |
| Password recovery | Form rendered, recovery page with code, valid code → code, invalid code → error; recover link hidden when `allowRecover=false` |
| User registration | Form rendered, PENDING → verify page, OK → code, CANCEL → form; verify valid code → code, invalid → error, GET verify page; register link hidden when `allowRegister=false`; registration OK + MFA → code; registration OK + MFA + consent → code |
| Delegated login | Provider button shown on login form; callback with stored token → code; callback with unknown code → login form |
| Multi-challenge | Consent → MFA, NewPass → Consent, ScopeConsent accepted, ScopeConsent denied |
| Revocation | `POST /revocation` → 200 |
| Logout | `GET /logout` → 302 to redirect URI, `AUTH_SESSION_ID` cleared, deleted session shows login form |
| Startup | CDI context boots without ambiguity |
