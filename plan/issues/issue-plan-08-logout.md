# Issue PLAN-08 — Logout: End Session Endpoint + Back-Channel Notifications

**OAUTH_PLAN:** PLAN-08  
**Wave:** 1

## Problem

Session is deleted and cookie cleared, but:
- Issued tokens remain alive (no JTI revocation on logout).
- Other registered relying parties are not notified (no back-channel logout fanout).
- `post_logout_redirect_uri` is not validated against the client allowlist.

## Current state

- Basic logout handler exists in `AuthorizeHtml` or a dedicated controller (cookie clearing) ✅.
- `BackChannelLogoutDispatcher` exists (verify whether it is synchronous — must be async).
- `TokenRevocationGateway.revokeAllForUser` needs to exist (see PLAN-03).

## Implementation steps

### 1. End Session endpoint

`features/oauth/authentication/infrastructure/driver/html/AuthorizeHtml.java`
(or extract to a dedicated `EndSessionController`):

```
GET|POST /{tenant}/oidc/logout
Params: id_token_hint, post_logout_redirect_uri, state, client_id
```

Steps:
1. If `id_token_hint` is present: verify its signature and extract `sub` and `sid`.
   Reject if the token was issued by a different client (compare `aud`).
2. Load the current session from `AUTH_SESSION_ID` cookie.
3. Validate `post_logout_redirect_uri`:
   - Must match the client's registered `post_logout_redirect_uris` allowlist.
   - If invalid or missing: ignore the redirect and show a generic logout confirmation page.
4. Call `TokenRevocationGateway.revokeAllForUser(userId, clientId)` (requires PLAN-03).
5. Delete `SessionInfo` from the session store.
6. Clear `AUTH_SESSION_ID` cookie (and SSO cookie if separate — see PLAN-09).
7. Fire back-channel logout fanout (step 2) **asynchronously**.
8. If valid `post_logout_redirect_uri`: redirect with `?state=` appended. Else: show logout page.

### 2. Back-Channel logout fanout (async)

`features/oauth/authentication/application/BackChannelLogoutDispatcher.java`:

- Make the HTTP fanout **asynchronous** using Quarkus `@Asynchronous` or Vert.x
  `context.executeBlocking` — never block the logout response on slow/failing RPs.
- Build `logout_token` JWT:
  - Required claims: `iss`, `sub`, `aud` (RP client_id), `iat`, `jti` (new UUID), `events`
    (`{"http://schemas.openid.net/event/backchannel-logout": {}}`), `sid`.
  - No `exp` or `nonce` — per OIDC Back-Channel Logout spec.
- HTTP POST to each RP's `backchannelLogoutUri`:
  ```
  POST {backchannelLogoutUri}
  Content-Type: application/x-www-form-urlencoded
  Body: logout_token={signed_jwt}
  ```
- Each RP is notified independently; failure of one RP must not block others.
  Log failures but do not retry (retry logic is Wave 4 / PLAN-27 territory).
- Source of RP list: `_oauth_session.sso_clients_json` (added in section 2.1 migration).

### 3. Register `backchannelLogoutUri` on `ClientDetails`

`features/oauth/client/domain/ClientDetails.java` — add:
```java
private final String backchannelLogoutUri;
private final boolean backchannelLogoutSessionRequired;
```

Map from `TrustedClient` (these fields already exist in the Access domain).

### 4. Update discovery document

`features/oauth/oidc/infrastructure/driver/rest/OpenIdConfigurationController.java`:
- `endSessionEndpoint: "{base}/{tenant}/oidc/logout"`
- `backchannelLogoutSupported: true` (once dispatcher is confirmed async)
- `backchannelLogoutSessionSupported: true`

## Dependencies

- PLAN-02/03 (token revocation) — `revokeAllForUser` must be implemented.
- PLAN-09 (SSO) — SSO cookie cleanup on logout.

## Files to create / modify

| Action | File |
|--------|------|
| **Modify** | `authentication/.../AuthorizeHtml.java` (or extract `EndSessionController`) — full end-session logic |
| **Modify** | `authentication/application/BackChannelLogoutDispatcher.java` — make async, build `logout_token` |
| **Modify** | `oauth/client/domain/ClientDetails.java` — add `backchannelLogoutUri` |
| **Modify** | `oidc/.../OpenIdConfigurationController.java` — `end_session_endpoint` metadata |
