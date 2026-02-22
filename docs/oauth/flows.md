# OAuth / OIDC — Flow diagrams

## 1. Authorization Code flow (happy path)

The standard path: user has no pending challenges after login.

```mermaid
sequenceDiagram
    actor Browser
    participant FAC as FrontAcessController
    participant Login as LoginUsecase
    participant LG as LoginGateway

    Browser->>FAC: GET /auth?client_id=...&redirect_uri=...&code_challenge=...
    FAC->>FAC: load client (ClientStoreGateway)
    FAC-->>Browser: 200 login form (HTML)

    Browser->>FAC: POST /auth  username + password + csid
    FAC->>Login: validatedUserData(request, user, pass, client, challenges)
    Login->>LG: validateUserData(...)
    LG-->>Login: AuthenticationResult.right(data)
    Login-->>FAC: AuthenticationResult.right(data)
    FAC->>FAC: redirect() — generate auth code, store session
    FAC-->>Browser: 302 redirect_uri?code=...&state=...
```

---

## 2. Multi-challenge flow

After credentials are accepted, additional challenges are evaluated sequentially by `revolve()`.

```mermaid
sequenceDiagram
    actor Browser
    participant FAC as FrontAcessController
    participant Login as LoginUsecase

    Browser->>FAC: POST /auth  username + password
    FAC->>Login: validatedUserData(...)
    Login-->>FAC: consentRequired (AuthenticationResult)
    FAC-->>Browser: 200 consent form  [PRE_SESSION_ID set]

    Browser->>FAC: POST /auth  step=consent  relying_party=rp-a  consent=on
    FAC->>FAC: ConsentControllerPart.process()
    FAC->>FAC: revolve() → loginUsecase.fillPreAuthenticated()
    note over FAC: re-evaluates all challenges
    FAC-->>Browser: 200 MFA form  [PRE_SESSION_ID updated]

    Browser->>FAC: POST /auth  step=mfa  mfa_code=123456
    FAC->>FAC: MfaControllerPart.process()
    FAC->>FAC: revolve() → loginUsecase.fillPreAuthenticated()
    note over FAC: no more challenges
    FAC-->>Browser: 302 redirect_uri?code=...
```

---

## 3. Consent flow — sequential per relying party

When a token request includes multiple `audience` values each RP may require its own consent.
Forms are shown one at a time. See [ADR-001](../adr/001-sequential-consent.md).

```mermaid
sequenceDiagram
    actor Browser
    participant FAC as FrontAcessController
    participant CP as ConsentControllerPart
    participant CU as ConsentUsecase
    participant CG as ConsentGateway

    FAC-->>Browser: 200 consent form for rp-a  (relying_party=rp-a hidden)
    Browser->>FAC: POST step=consent  relying_party=rp-a  consent=on
    CP->>CU: storeAcceptedConsent(tenant, user, "rp-a")
    CU->>CG: storeAcceptedConsent(tenant, user, "rp-a")
    FAC->>FAC: revolve()
    FAC->>CG: getPendingConsent(tenant, user, audiences, locale)
    CG-->>FAC: PendingConsent(relyingParty="rp-b", text="...")
    FAC-->>Browser: 200 consent form for rp-b
    Browser->>FAC: POST step=consent  relying_party=rp-b  consent=on
    FAC->>FAC: revolve()
    FAC->>CG: getPendingConsent(...)
    CG-->>FAC: Optional.empty()
    FAC-->>Browser: 302 redirect_uri?code=...
```

---

## 4. Password recovery flow

```mermaid
sequenceDiagram
    actor Browser
    participant FAC as FrontAcessController
    participant RCP as RecoverControllerPart
    participant CPW as ChangePasswordUsecase

    Browser->>FAC: POST /auth  step=show-recover
    FAC-->>Browser: 200 recover form (email/username field)

    Browser->>FAC: POST /auth  step=send-recover  username=...
    RCP->>CPW: requestForChange(urlBase, tenant, username)
    note over CPW: sends email with link containing recover code
    FAC-->>Browser: 200 "check your email" page

    note over Browser: user clicks link in email
    Browser->>FAC: GET /recover?username=...&recovercode=...
    FAC-->>Browser: 200 new-password form (code pre-filled)

    Browser->>FAC: POST /recover  code=...  password=...
    RCP->>CPW: validateChangeRequest(tenant, code, newPassword)
    CPW-->>RCP: Optional.of(username)
    FAC->>FAC: revolve() with recovered username
    FAC-->>Browser: 302 redirect_uri?code=...
```

---

## 5. User self-registration flow

```mermaid
sequenceDiagram
    actor Browser
    participant FAC as FrontAcessController
    participant REGCP as RegistrationControllerPart
    participant RU as RegisterUserUsecase

    Browser->>FAC: POST /auth  step=show-register
    REGCP->>RU: allowRegister(tenant)
    FAC-->>Browser: 200 registration form

    Browser->>FAC: POST /auth  step=do_register  reg_email=...  reg_password=...
    REGCP->>RU: requestForRegister(urlBase, tenant, email, password)
    RU-->>REGCP: RegistrationResult.pending()
    FAC-->>Browser: 200 "verify your email" page

    note over Browser: user clicks link in email
    Browser->>FAC: GET /register?email=...&regcode=...
    FAC-->>Browser: 200 verification form (code pre-filled)

    Browser->>FAC: POST /register?email=...  regcode=...
    REGCP->>RU: verifyRegister(tenant, code)
    RU-->>REGCP: Optional.of(username)
    FAC->>FAC: revolve() with registered username
    FAC-->>Browser: 302 redirect_uri?code=...
```

---

## 6. Delegated login (external provider)

```mermaid
sequenceDiagram
    actor Browser
    participant FAC as FrontAcessController
    participant DCP as DelegatedControllerPart
    participant DLP as DelegatedLoginProvidersUsecase

    Browser->>FAC: POST /auth  step=query-delegated  provider=google
    DCP->>DCP: doPaintLoginForm(tenant, provider, locale)
    FAC-->>Browser: 302 to provider authorization URL

    note over Browser: user authenticates with provider
    Browser->>FAC: GET /delegated-auth?provider=google&code=...
    DCP->>DCP: doBackLoginForm() — exchange code, decode user info
    DCP-->>FAC: auto-submit form with encoded token

    FAC->>FAC: POST /auth  step=query-delegated  (auto-submitted)
    DCP->>DLP: retrieveUsername(tenant, audiences, provider, userData)
    note over DLP: creates user if not found
    DLP-->>DCP: Optional.of(username)
    FAC->>FAC: revolve() with resolved username
    FAC-->>Browser: 302 redirect_uri?code=...
```

---

## 7. Token exchange (Authorization Code → tokens)

```mermaid
sequenceDiagram
    actor Client as Client App
    participant AC as AuthenticationController

    Client->>AC: POST /token  grant_type=authorization_code  code=...  code_verifier=...
    AC->>AC: verify PKCE code_verifier against stored code_challenge
    AC->>AC: load session, build AuthenticationData
    AC-->>Client: 200 { access_token, id_token, refresh_token }
```

---

## 8. Existing session — no re-login

```mermaid
sequenceDiagram
    actor Browser
    participant FAC as FrontAcessController

    Browser->>FAC: GET /auth?client_id=...  [AUTH_SESSION_ID cookie present]
    FAC->>FAC: loadSession(AUTH_SESSION_ID)
    note over FAC: valid session found — show confirm page
    FAC-->>Browser: 200 confirm page (one-click re-authorize)

    Browser->>FAC: POST /auth  [AUTH_SESSION_ID cookie]  csid=...
    FAC->>FAC: doCheckSession() — verifies csid
    FAC->>FAC: redirect()
    FAC-->>Browser: 302 redirect_uri?code=...
```
