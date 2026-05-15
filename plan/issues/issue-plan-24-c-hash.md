# Issue PLAN-24 — `c_hash` Claim in ID Token (Hybrid Flow)

**OAUTH_PLAN:** PLAN-24  
**Wave:** 4 — Deferred

## Problem

The OIDC Core 1.0 spec (§3.3.2.11) requires a `c_hash` claim in the ID token when
`response_type` includes `code`. Without it, Hybrid Flow is technically non-conformant.

## Scope

This is only relevant when a client uses Hybrid Flow (`response_type=code id_token` or
`response_type=code token`). Pure Authorization Code Flow (`response_type=code`) does not
include an ID token in the authorization response — only at the token endpoint — so `c_hash`
is optional there.

## Implementation steps

### 1. Compute `c_hash`

`c_hash` is defined as:
1. Hash the authorization code with the same algorithm used for the ID token signature
   (SHA-256 for RS256/ES256).
2. Take the leftmost half of the hash bytes.
3. Base64URL-encode (no padding).

```java
private String computeHash(String value) throws NoSuchAlgorithmException {
    byte[] digest = MessageDigest.getInstance("SHA-256").digest(
        value.getBytes(StandardCharsets.US_ASCII));
    byte[] half = Arrays.copyOf(digest, digest.length / 2);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(half);
}
```

### 2. Add `c_hash` to the ID token

`features/oauth/tokensecurity/application/JwtTokenBuilder.java`
(or `ClaimsMapper` after PLAN-17):

When building the ID token:
```java
if (authorizationCode != null && responseType.contains("code")) {
    claims.put("c_hash", computeHash(authorizationCode));
}
```

Only add `c_hash` when the ID token is part of the authorization response (Hybrid Flow),
not when it is returned solely from the token endpoint.

### 3. Pass the authorization code to the token builder

Ensure the authorization code string is accessible at the point where the ID token is built
in the Hybrid Flow path. If the token builder currently only builds tokens at the token endpoint,
a Hybrid Flow path needs to be added.

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `tokensecurity/application/JwtTokenBuilder.java` (or `ClaimsMapper`) — add `c_hash` |
