# Issue PLAN-06 — Discovery Document Completeness

**OAUTH_PLAN:** PLAN-06  
**Wave:** 1 (update incrementally as each Wave 0 item lands)

## Problem

`OpenIdConfigurationController` advertises features that are not yet implemented and
is missing fields required by OIDC Discovery 1.0. Relying parties that auto-configure
from the discovery document will get broken or misleading metadata.

## Current state

- `OpenIdConfigurationController` exists and returns a JSON document ✅.
- Known gaps (from OAUTH_STATE §2.15):
  - `codeChallengMethodsSupported` — typo in field name; value may be wrong.
  - `revocationEndpoint` — missing or pointing to wrong path.
  - `grantTypesSupported` — may be incomplete.
  - `backchannel_logout_supported` — may be incorrectly set to `true` before PLAN-08 is complete.
  - Several endpoints advertised but returning 403.

## Implementation steps

This issue is a living checklist. Add a checkmark to each item as the underlying PLAN lands.

### After PLAN-01 (PKCE)
- `codeChallengeMethodsSupported: ["S256"]`
- Fix typo: `codeChallengMethods` → `codeChallengeMethodsSupported`

### After PLAN-03 (Revocation)
- `revocationEndpoint: "{base}/{tenant}/oauth/revoke"`
- `revocationEndpointAuthMethodsSupported: ["client_secret_basic", "client_secret_post"]`

### After PLAN-08 (Logout)
- `endSessionEndpoint: "{base}/{tenant}/oidc/logout"`
- `backchannelLogoutSupported: true` (only after async dispatcher is confirmed working)
- `backchannelLogoutSessionSupported: true`

### After PLAN-11 (Introspection)
- `introspectionEndpoint: "{base}/{tenant}/oauth/introspect"`
- `introspectionEndpointAuthMethodsSupported: ["client_secret_basic", "client_secret_post"]`

### After PLAN-12 (Client Credentials)
- `grantTypesSupported` — add `"client_credentials"`

### After PLAN-13 (Device)
- `grantTypesSupported` — add `"urn:ietf:params:oauth:grant-type:device_code"`
- `deviceAuthorizationEndpoint: "{base}/{tenant}/oidc/device_authorization"`

### After PLAN-16 (PAR)
- Confirm `pushedAuthorizationRequestEndpoint` is set and correct.

### Always (do now)
- `promptValuesSupported: ["none", "login", "consent", "select_account"]`
- `acrValuesSupported: ["0", "1", "2"]` — `2` for MFA (already emitted in tokens)
- Remove or comment out any endpoint that currently returns 403 (does not exist yet).
- Confirm `issuer` matches the actual JWT `iss` claim.
- Confirm `jwksUri` points to the working JWKS endpoint.

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `oidc/infrastructure/driver/rest/OpenIdConfigurationController.java` — iterative updates |
