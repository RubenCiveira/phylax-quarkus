# Issue PLAN-12 — Client Credentials Grant (M2M, RFC 6749 §4.4)

**OAUTH_PLAN:** PLAN-12  
**Wave:** 2

## Problem

No `client_credentials` grant type. Machine-to-machine clients currently authenticate via
API keys only. This blocks standard OAuth M2M flows.

## Current state

- `TokenController` dispatches to granters by grant type ✅.
- `ClientDetails.allowedGrants` and `allowedScopes` exist ✅.
- No `ClientCredentialsGranter` exists.

## Implementation steps

### 1. Create `ClientCredentialsGranter`

`features/oauth/authentication/application/granter/ClientCredentialsGranter.java`

```java
@Override
public boolean canHandle(String grantType) {
    return "client_credentials".equals(grantType);
}

@Override
public TokenResponse grant(TokenRequest request, ClientDetails client) {
    // 1. Verify client is confidential (has secret)
    if (!client.isProtectedWithSecret()) {
        throw new OAuthException("unauthorized_client", "public clients may not use client_credentials");
    }
    // 2. Verify grant is allowed for this client
    if (!client.allowdedGrant("client_credentials")) {
        throw new OAuthException("unauthorized_client");
    }
    // 3. Intersect requested scopes with client's allowed scopes
    List<String> scopes = intersect(request.getScopes(), client.getAllowedScopes());

    // 4. Build access token — sub = clientId, no user, no ID token, no refresh token
    return jwtBuilder.buildClientCredentialsToken(client.getClientId(), scopes, client);
}
```

### 2. Build the token in `JwtTokenBuilder`

Add `buildClientCredentialsToken(clientId, scopes, client)`:
- `sub` = clientId (not a user UID).
- No `email`, `name`, or user claims.
- No refresh token issued.
- No ID token issued.
- `exp` = `client.getM2mTokenTtlSeconds()` from `ClientDetails` (map this field from `TrustedClient`).

### 3. Wire into `TokenController`

Add `ClientCredentialsGranter` to the granter dispatch list (CDI injection or existing map).

### 4. Update discovery document

```java
.grantTypesSupported(List.of("authorization_code", "refresh_token", "client_credentials"))
```

## Dependencies

- `ClientDetails.m2mTokenTtlSeconds` — map from `TrustedClient.m2mTokenTtlSeconds`.
  Add to `ClientDetails` if not already present.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `authentication/application/granter/ClientCredentialsGranter.java` |
| **Modify** | `tokensecurity/application/JwtTokenBuilder.java` — `buildClientCredentialsToken` |
| **Modify** | `authentication/infrastructure/driver/rest/TokenController.java` — wire new granter |
| **Modify** | `oauth/client/domain/ClientDetails.java` — add `m2mTokenTtlSeconds` if missing |
| **Modify** | `oidc/.../OpenIdConfigurationController.java` — `client_credentials` in `grant_types_supported` |
