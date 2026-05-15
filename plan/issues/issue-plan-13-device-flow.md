# Issue PLAN-13 — Device Authorization Grant Complete (RFC 8628)

**OAUTH_PLAN:** PLAN-13  
**Wave:** 2

## Problem

Domain model and HTML verification UI exist but the device code token exchange
at the token endpoint may not be wired. `slow_down` enforcement is unverified.

## Current state

- `_oauth_device_codes` table exists with `last_poll_at` ✅.
- `DeviceAuthorizationService` exists in `device/application/` ✅.
- `DeviceAuthorization` domain object exists ✅.
- HTML user code verification endpoint exists ✅.
- `DeviceCodeGranter` — may not exist or may not be wired into `TokenController`.

## Implementation steps

### 1. Verify `DeviceAuthorizationService.pollStatus()`

`features/oauth/device/application/DeviceAuthorizationService.java`:

Ensure `pollStatus(deviceCode, clientId)` returns the correct RFC 8628 error codes:
- `authorization_pending` — approved flag not yet set.
- `slow_down` — if the client polls faster than `interval_sec`:
  ```java
  if (lastPolledAt != null && Duration.between(lastPolledAt, now).getSeconds() < intervalSec) {
      // Update lastPolledAt, increase intervalSec by 5, return slow_down
  }
  ```
- `expired_token` — `expires_at < now()`.
- `access_denied` — user denied.
- Returns `AuthenticationData` on `APPROVED` status.

Update `last_poll_at` on every poll call regardless of status.

### 2. Create or verify `DeviceCodeGranter`

`features/oauth/authentication/application/granter/DeviceCodeGranter.java`

```java
@Override
public boolean canHandle(String grantType) {
    return "urn:ietf:params:oauth:grant-type:device_code".equals(grantType);
}

@Override
public TokenResponse grant(TokenRequest request, ClientDetails client) {
    String deviceCode = request.getDeviceCode();
    AuthenticationData authData = deviceService.pollStatus(deviceCode, client.getClientId());
    // Build full token set (access + refresh + ID token if openid scope)
    return jwtBuilder.buildTokenPair(authData, client, request.getScopes());
}
```

### 3. Wire `DeviceCodeGranter` into `TokenController`

Same pattern as other granters.

### 4. User code character set

`DeviceAuthorizationService` (or wherever `user_code` is generated):
- Character set must exclude visually ambiguous characters: `0`, `O`, `I`, `1`, `l`.
- RFC 8628 recommends the CONSONANT set (20 chars): `BCDFGHJKLMNPQRSTVWXZ`.
- Format: `XXXX-XXXX` (8 chars with hyphen separator).

### 5. Update discovery document

Confirm `deviceAuthorizationEndpoint` is set and correct.
Add `"urn:ietf:params:oauth:grant-type:device_code"` to `grantTypesSupported`.

## Files to create / modify

| Action | File |
|--------|------|
| **Modify** | `device/application/DeviceAuthorizationService.java` — `slow_down` + `last_poll_at` |
| **Create/verify** | `authentication/application/granter/DeviceCodeGranter.java` |
| **Modify** | `authentication/infrastructure/driver/rest/TokenController.java` — wire granter |
| **Modify** | `oidc/.../OpenIdConfigurationController.java` — device grant type |
