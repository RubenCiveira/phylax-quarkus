# Issue PLAN-16 — PAR REST Driver and Client Authentication

**OAUTH_PLAN:** PLAN-16  
**Wave:** 2

## Problem

`PushAuthorizationUsecase` and `ResolveParRequestUsecase` are implemented but may not be
exposed via a proper REST driver. Client authentication before push is unverified.

## Current state

- `par/` bounded context with `PushAuthorizationUsecase` ✅.
- `_oauth_par_request` table exists ✅.
- PAR endpoint may live in `authentication/` rather than having its own `par/` driver.

## Implementation steps

### 1. Locate the PAR endpoint

Search for `POST .*par` routes in the codebase. If the PAR controller lives in
`authentication/infrastructure/driver/rest/`, either leave it there or extract to
`par/infrastructure/driver/rest/ParController.java` for cleaner separation.

### 2. Ensure client authentication before `PushAuthorizationUsecase`

The PAR endpoint must authenticate the client (RFC 9126 §2):
- Support `client_secret_basic` and `client_secret_post`.
- Public clients may authenticate with `client_id` only (no secret), but PKCE must then
  be required (see Issue 1.2).
- If client authentication fails: `401 Unauthorized`.

### 3. Validate and store the pushed request

`PushAuthorizationUsecase.execute(clientId, params)`:
- Validate required params: `response_type`, `client_id`, `redirect_uri`.
- Store in `_oauth_par_request` with a generated `request_uri` and short `expires_at` (60–90 seconds).
- Return:
  ```json
  { "request_uri": "urn:ietf:params:oauth:request_uri:...", "expires_in": 60 }
  ```

### 4. Resolve PAR in `AuthorizeHtml`

`features/oauth/authentication/infrastructure/driver/html/AuthorizeHtml.java`:
- When an incoming `/authorize` has `request_uri` param:
  - Call `ResolveParRequestUsecase.execute(requestUri)` to retrieve and **consume** (mark `used_at`) the stored params.
  - Merge the stored params with any additional params from the request (stored params win on conflict).
  - Reject with `invalid_request` if `request_uri` is expired or already used.

### 5. Update discovery document

Confirm `pushedAuthorizationRequestEndpoint` is set to the correct URL.

## Files to create / modify

| Action | File |
|--------|------|
| **Create/verify** | `par/infrastructure/driver/rest/ParController.java` — or locate existing endpoint |
| **Modify** | PAR controller — add client authentication before `PushAuthorizationUsecase` |
| **Modify** | `authentication/.../AuthorizeHtml.java` — `request_uri` resolution via `ResolveParRequestUsecase` |
| **Modify** | `oidc/.../OpenIdConfigurationController.java` — confirm `pushed_authorization_request_endpoint` |
