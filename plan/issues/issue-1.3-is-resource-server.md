# Issue 1.3 — `is-resource-server` wiring into the introspection endpoint

**Model section:** 1.3  
**OAUTH_PLAN:** PLAN-11 (Token Introspection, RFC 7662)  
**Wave:** 2

## Current state

- `access_trusted_client.is_resource_server BIT NOT NULL` column exists in DB (migration applied ✅).
- Access domain: `IsResourceServerVO` and `IsResourceServerValueHolder` exist in `TrustedClient`.
- OAuth domain: `ClientDetails` has **no** `resourceServer` flag.
- `IntrospectionController` exists in `tokensecurity/` but may return 403 or skip caller
  authentication entirely.

## What to implement

### 1. Add `resourceServer` to `ClientDetails`

`features/oauth/client/domain/ClientDetails.java`:

```java
private final boolean resourceServer;
```

### 2. Map `is_resource_server` when building `ClientDetails`

In the driven adapter that loads `TrustedClient` and builds `ClientDetails`:

```java
.resourceServer(client.getIsResourceServer())
```

### 3. Gate the introspection endpoint on `resourceServer`

`features/oauth/tokensecurity/infrastructure/driver/rest/IntrospectionController.java`:

1. Authenticate the caller using `client_secret_basic` or `client_secret_post`
   (same client authentication logic used in `TokenController`).
2. Load `ClientDetails` for the authenticated caller.
3. If `!clientDetails.isResourceServer()`, return `403 Forbidden` with
   `{"error": "access_denied", "error_description": "not a resource server"}`.
4. Proceed with token inspection:
   - Try to parse the submitted token (catch all exceptions → `{"active": false}`).
   - If valid JWT, not expired, and not revoked (`TokenRevocationGateway.isRevoked(jti)`):
     return active response with `sub`, `client_id`, `scope`, `exp`, `iat`, `iss`, `jti`,
     `token_type`, and any custom claims.
   - Any other case: `{"active": false}`.
5. Set response headers: `Content-Type: application/json`, `Cache-Control: no-store`.

### 4. Update the discovery document

`features/oauth/oidc/infrastructure/driver/rest/OpenIdConfigurationController.java`:

- Confirm `introspectionEndpoint` is set to the correct URL.
- Add `introspectionEndpointAuthMethodsSupported: ["client_secret_basic", "client_secret_post"]`.

## Dependencies

- PLAN-03 (Token Revocation) — `isRevoked(jti)` must be functional for a correct `active` response.
- Issue 1.2 (ClientDetails fields) — both issues extend `ClientDetails`; coordinate to avoid
  conflicting edits to the same class.

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `oauth/client/domain/ClientDetails.java` — add `resourceServer` |
| **Modify** | adapter that builds `ClientDetails` — map from `TrustedClient.isResourceServer` |
| **Modify** | `oauth/tokensecurity/infrastructure/driver/rest/IntrospectionController.java` — full impl |
| **Modify** | `oauth/oidc/.../OpenIdConfigurationController.java` — introspection endpoint metadata |
