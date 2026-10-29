# Issue 1.4 — Generic OIDC delegated provider

**Model section:** 1.4  
**OAUTH_PLAN:** PLAN-14 (Generic OIDC Delegated Provider) + PLAN-21 (provider layer migration)  
**Wave:** 2

## Current state

- `access_tenant_login_provider.oidc_discovery_url VARCHAR(255)` column exists in DB ✅.
- `source` enum already has `OIDC` as a valid value (check current enum list in the domain;
  if it is missing, add it — the column is `VARCHAR(255)` so no DDL change needed).
- `GoogleDelegatedAccessProvider` and `SamlDelegatedAccessProvider` exist in
  `delegatelogin/domain/provider/` — **wrong layer** (implementations belong in infrastructure).
- No `OidcDelegatedAccessProvider` exists.
- The domain `DelegatedLoginEndpoint` (or equivalent) does not currently have a `providerType`
  discriminator that routes to a generic OIDC path.

## What to implement

### 1. Move existing providers to infrastructure (PLAN-21)

Move these classes from `delegatelogin/domain/provider/` to
`delegatelogin/infrastructure/driven/provider/`:

- `GoogleDelegatedAccessProvider` → `infrastructure/driven/provider/`
- `SamlDelegatedAccessProvider` → `infrastructure/driven/provider/`

Keep only the interface `DelegatedAccessExternalProvider` in `domain/`. This is a package
rename only — no logic changes. Update imports and CDI injection points.

### 2. Add `OIDC` as a provider type discriminator

In `delegatelogin/domain/` (wherever provider type is modeled — could be a string field or an enum):
- Ensure `OIDC` is a recognised type that routes to `OidcDelegatedAccessProvider`.

### 3. Create `OidcDelegatedAccessProvider`

`delegatelogin/infrastructure/driven/provider/OidcDelegatedAccessProvider.java`

Implements `DelegatedAccessExternalProvider` (or equivalent interface):

**`buildRequest(providerConfig, authRequest)`:**
- Fetch `/.well-known/openid-configuration` from `providerConfig.oidcDiscoveryUrl` at startup
  (or cache with TTL). Extract `authorization_endpoint`.
- Build the redirect URL:
  `authorization_endpoint?client_id=&redirect_uri=&response_type=code&scope=openid+profile+email&state={csrf}&code_challenge={s256}&code_challenge_method=S256`
- Store the PKCE `code_verifier` and `state` in `_oauth_delegated_state` via the existing gateway.

**`handleResponse(providerConfig, callbackParams)`:**
- Verify `state` against `_oauth_delegated_state`; reject on mismatch.
- Exchange `code` at the IdP's `token_endpoint` (from discovery doc).
- Verify the returned ID token:
  - Fetch IdP JWKS from `jwks_uri` (from discovery doc; cache with TTL).
  - Validate signature, `iss`, `aud`, `exp`, `nonce`.
- Return `ExternalUserInfo` with `sub`, `email`, `name` extracted from the ID token or userinfo.

**`resolveUserInfo(providerConfig, accessToken)`** (optional, if userinfo endpoint is needed):
- Call IdP `userinfo_endpoint` with `Authorization: Bearer {accessToken}`.
- Merge claims into the returned `ExternalUserInfo`.

### 4. Wire into the provider dispatcher

In the adapter that delegates to the correct provider by type
(e.g., `DelegateLoginAdapter` or `DelegatedAccessAuthValidatorAdapter`):

```java
case "OIDC" -> oidcProvider.buildRequest(...)
```

### 5. Expose `oidcDiscoveryUrl` from the tenant login provider

Ensure the driven adapter that loads `TenantLoginProvider` exposes `oidcDiscoveryUrl` so
`OidcDelegatedAccessProvider` can read it at runtime. If `TenantLoginProviderGateway` already
returns the full provider object, the field is accessible via `OidcDiscoveryUrlVO`.

## Dependencies

- `_oauth_delegated_state` table (section 2.12 — exists if migration is applied).
- An HTTP client (Quarkus RestClient or `java.net.http.HttpClient`) for discovery and token exchange.
- PLAN-21 must be done as part of this issue (no separate step needed).

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `delegatelogin/infrastructure/driven/provider/OidcDelegatedAccessProvider.java` |
| **Move** | `delegatelogin/domain/provider/GoogleDelegatedAccessProvider` → `infrastructure/driven/provider/` |
| **Move** | `delegatelogin/domain/provider/SamlDelegatedAccessProvider` → `infrastructure/driven/provider/` |
| **Modify** | provider dispatcher — add `OIDC` routing case |
| **Modify** | `oidc/.../OpenIdConfigurationController.java` — nothing new; verify `end_session_endpoint` not pointing to delegated login |
