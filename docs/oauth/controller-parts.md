# OAuth / OIDC — ControllerPart catalog

## The delegation pattern

`FrontAcessController` processes every POST to `/auth` through a shared pipeline:

1. **Load context** — client (`ClientStoreGateway`), session or pre-session cookie.
2. **Dispatch to ControllerPart** — `doExecStep()` finds the part matching the `step` form param
   and delegates to it. If no `step` is provided, the login credentials handler runs.
3. **Render or resolve** — the part either paints a form (200) or calls `resolver.apply()`
   with a `StepResult`, which triggers `revolve()`.
4. **`revolve()`** — re-invokes `fillPreAuthenticated()` to evaluate remaining challenges.
   Repeats until all challenges are cleared, then calls `redirect()` → 302.

```
POST /auth
 └─ doExecStep(step, ...)
     ├─ step == null       → doExecLogin() (credentials)
     ├─ step == "consent"  → ConsentControllerPart.process()
     ├─ step == "mfa"      → MfaControllerPart.process()
     ├─ step == "new_pass" → NewPassControllerPart.process()
     ├─ step == "valid_new_mfa"   → NewMfaControllerPart.process()
     ├─ step == "scope_consent"   → ScopeConsentControllerPart.process()
     ├─ step == "do_register"     → RegistrationControllerPart.process()
     ├─ step == "send-recover"    → RecoverControllerPart.process()
     ├─ step == "show-recover"    → RecoverControllerPart (paint form)
     ├─ step == "show-register"   → RegistrationControllerPart (paint form)
     └─ step == "query-delegated" → DelegatedControllerPart.process()
```

Each part is a `@RequestScoped` CDI bean injected into `FrontAcessController`.

---

## Part reference

### `LoginControllerPart` (inline in `FrontAcessController`)

Handles the initial credential submission. Not a separate class — logic lives in
`doExecLogin()` inside `FrontAcessController`.

- **step value**: `null` (no `step` param in form)
- **challenge produced**: `PASSWORD`
- **on success**: calls `resolver` → `revolve()`
- **on failure**: re-paints login form with error

---

### `ConsentControllerPart`

Handles acceptance of a relying party's terms of use. Consent is tracked per
`(tenant, username, relyingParty)`. Multiple RPs are handled sequentially — after accepting
one, `revolve()` re-checks and may show the next pending RP's form.

- **step value**: `"consent"`
- **challenge**: `AuthenticationChallege.USE_CONSENT`
- **form fields read**: `consent` (checkbox: `on` | `off`), `relying_party` (hidden)
- **form fields written**: `relying_party` (from `PendingConsent.getRelyingParty()`)
- **paint entry point**: `doPaintConsent(locale, request, username, session)`
- **gateway used**: `ConsentGateway`

---

### `MfaControllerPart`

Validates a TOTP/OTP code for a user who has MFA already configured.

- **step value**: `"mfa"`
- **challenge**: `AuthenticationChallege.MFA`
- **form fields read**: `mfa_code`, `csid`
- **paint entry point**: `doPaintMfaForm(locale, session)`
- **gateway used**: `UserMfa` (via `MfaConfigUsecase`)

---

### `NewMfaControllerPart`

Handles the first-time MFA setup for a user who doesn't yet have a second factor configured.
Shows a QR code for the authenticator app, then asks for the first OTP to confirm setup.

- **step value**: `"valid_new_mfa"`
- **challenge**: `AuthenticationChallege.MFA` (same enum value as MFA validation)
- **form fields read**: `otp_code`, `csid`
- **paint entry point**: `doPaintNewMfaForm(request, username, locale, session)`
- **gateway used**: `UserMfa`

---

### `NewPassControllerPart`

Handles a forced password change — required when the user's account is flagged as needing a
new password (e.g. first login with a temporary password).

- **step value**: `"new_pass"`
- **challenge**: `AuthenticationChallege.FRESH_PASSWORD`
- **form fields read**: `old_pass`, `new_pass`, `csid`
- **paint entry point**: `doPaintNewPassForm(locale, session)`
- **gateway used**: `ChangePasswordGateway` (via `ChangePasswordUsecase`)

---

### `ScopeConsentControllerPart`

Handles granular OAuth scope authorization — shows the user which permissions the client
application is requesting and asks for confirmation.

- **step value**: `"scope_consent"`
- **challenge**: `AuthenticationChallege.CLIENT_CONSENT`
- **form fields read**: `csid`
- **paint entry point**: `doPaintScopeConsentForm(locale, request, username, session)`
- **gateway used**: `ClientScopeConsentGateway` (via `ClientScopeConsentUsecase`)

---

### `RecoverControllerPart`

Handles password recovery. The flow has two phases:

1. **Request phase** (inside `/auth`): user provides email/username → recovery email sent.
2. **Reset phase** (on `/recover`): user arrives with recovery code pre-filled in URL → sets
   new password → `revolve()` completes the login.

- **step values**: `"show-recover"` (paint form), `"send-recover"` (send email)
- **Separate endpoint**: GET/POST `/oauth/openid/{tenant}/recover`
- **Form fields read** (reset): `code`, `password`, `csid`
- **Paint entry**: `doPaintRecoverForm(locale, msg)`, `doPaintWaitRecover(locale, msg, user, code)`
- **Gateway used**: `ChangePasswordGateway` (via `ChangePasswordUsecase`)
- **Guard**: `allowRecover(request)` — only shown if tenant has recovery enabled

---

### `RegistrationControllerPart`

Handles user self-registration. The flow has two phases:

1. **Registration phase** (inside `/auth`): user provides email + password → gateway decides
   `OK` (auto-activated), `PENDING` (email verification needed), or `CANCEL` (not allowed).
2. **Verification phase** (on `/register`): user arrives with verification code from email →
   `revolve()` completes the login with the new account.

- **step values**: `"show-register"` (paint form), `"do_register"` (submit registration)
- **Separate endpoint**: GET/POST `/oauth/openid/{tenant}/register`
- **Form fields read** (registration): `reg_email`, `reg_password`, `csid`
- **Form fields read** (verification): `regcode`, `csid`
- **Paint entries**: `doPaintRegisterForm(locale, msg)`, `doPaintVerifyForm(locale, email, code, msg)`, `doPaintPendingPage(locale, email)`
- **Gateway used**: `RegisterUserGateway` (via `RegisterUserUsecase`)
- **Guard**: `allowRegister(request)` — only shown if tenant allows self-registration

---

### `DelegatedControllerPart`

Handles login via external identity providers (Google OIDC, SAML, etc.).

- **step value**: `"query-delegated"`
- **Form fields read**: `provider` (name of the external IdP)
- **Separate endpoint**: GET `/oauth/openid/{tenant}/delegated-auth` (provider callback)
- **Gateway used**: `DelegateLogin` (via `DelegatedLoginProvidersUsecase`)

---

## Adding a new challenge

1. Create a new `*ControllerPart` bean (`@RequestScoped @RequiredArgsConstructor`).
2. Add a `getChallenge()` method returning the new `AuthenticationChallege` enum value.
3. Implement `process(step, oUser, clientDetails, request, paramMap, resolver)` returning
   `Optional<Response>` — empty if the step doesn't belong to this part.
4. Inject the new part into `FrontAcessController` and wire it in `doExecStep()` and the
   `fillIfEmpty` chain.
5. Add the challenge rendering call in `FrontAcessController.revolve()` (paint the form when
   the challenge is detected after re-evaluation).
