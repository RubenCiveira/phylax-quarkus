# Issue PLAN-29 — Discovery: `DiscoveryContributor` SPI

**OAUTH_PLAN:** PLAN-29  
**Wave:** 4 — Deferred (design debt)

## Problem

`OpenIdConfigurationController` statically assembles the entire discovery document in one place.
Every new feature (new grant type, new endpoint) requires editing this controller, coupling it
to every other bounded context. This violates the Open/Closed Principle.

## Approach

Replace the static assembly with a CDI `DiscoveryContributor` interface. Each bounded context
that owns an OIDC endpoint or capability registers its own contributor.

## Implementation steps

### 1. Define `DiscoveryContributor`

`features/oauth/oidc/domain/DiscoveryContributor.java`:

```java
public interface DiscoveryContributor {
    /**
     * Contributes endpoint URLs and capability flags to the discovery document.
     * Implementations should only set the fields they own.
     */
    void contribute(OpenIdConfigurationBuilder builder);
}
```

### 2. Implement contributors per context

| Context | Contributor | Fields |
|---------|-------------|--------|
| `authentication` | `AuthorizationEndpointContributor` | `authorization_endpoint`, `token_endpoint`, `grant_types_supported` |
| `tokensecurity` | `TokenSecurityContributor` | `jwks_uri`, `id_token_signing_alg_values_supported`, `introspection_endpoint` |
| `par` | `ParContributor` | `pushed_authorization_request_endpoint` |
| `device` | `DeviceContributor` | `device_authorization_endpoint` |
| `session` | `SessionContributor` | `check_session_iframe`, `end_session_endpoint` |
| `profile` | `UserinfoContributor` | `userinfo_endpoint` |

### 3. Assemble in `OpenIdConfigurationController`

```java
@Inject @Any Instance<DiscoveryContributor> contributors;

public OpenIdConfiguration build(String tenant) {
    OpenIdConfigurationBuilder builder = OpenIdConfiguration.builder()
        .issuer(resolveIssuer(tenant));
    contributors.forEach(c -> c.contribute(builder));
    return builder.build();
}
```

### 4. Cache the result

Cache the assembled document per tenant (invalidate on configuration change or on startup).
The current approach of building it once at startup is acceptable; per-tenant caching is needed
if different tenants can have different feature flags.

## Note

This is a design improvement. Implement after Wave 2 is stable and the full set of endpoints
is known, to avoid having to split contributors repeatedly during active development.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `oidc/domain/DiscoveryContributor.java` |
| **Create** | One `*Contributor` class per context |
| **Modify** | `oidc/infrastructure/driver/rest/OpenIdConfigurationController.java` — use `Instance<DiscoveryContributor>` |
