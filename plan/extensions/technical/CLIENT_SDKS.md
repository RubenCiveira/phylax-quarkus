# Technical Extension — Client SDKs

> Extracted from OAUTH_PLAN.md (was PLAN-40). Not part of the OAuth/OIDC implementation scope.

---

## Description

Official client libraries to reduce integration time for developers building on Phylax.

## SDK 1 — JavaScript / TypeScript (`@phylax/client`)

PKCE-first, OIDC Discovery-driven. Auto token refresh. React hooks.

```typescript
const phylax = new PhylaxClient({
  issuer: 'https://auth.example.com/openid/my-tenant',
  clientId: 'my-app',
  redirectUri: 'https://app.example.com/callback',
  scopes: ['openid', 'email', 'profile'],
});
await phylax.signIn();
const session = await phylax.getSession();
const token  = await phylax.getAccessToken(); // auto-refreshed
await phylax.signOut();
```

Published to npm as `@phylax/client`. React adapter as `@phylax/react`.

## SDK 2 — Java / Quarkus (`phylax-java-client`)

Quarkus extension wrapping Vert.x HTTP client + OIDC client-credentials flow.
Published to Maven Central.

## SDK 3 — OpenAPI-generated stubs

CI/CD generates TypeScript and Java clients from the OpenAPI spec after each release.

## Dependencies

- PLAN-06 (Discovery completeness) — clients bootstrap from `/.well-known/openid-configuration`
- PLAN-01 (PKCE) — JS SDK uses S256 by default
- Stable API surface required before publishing versioned SDKs
