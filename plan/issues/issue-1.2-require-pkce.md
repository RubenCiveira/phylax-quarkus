# Issue 1.2 — `require-pkce` wiring into the authorization flow

**Model section:** 1.2  
**OAUTH_PLAN:** PLAN-01 (PKCE Full Enforcement, RFC 7636)  
**Wave:** 0

## Current state

- `access_trusted_client.require_pkce BIT NOT NULL` column exists in DB (migration applied ✅).
- Access domain: `RequirePkceVO` and `RequirePkceValueHolder` exist in `TrustedClient`.
- OAuth domain: `ClientDetails` has **no** `requirePkce` field — it only carries
  `clientId`, `allowedScopes`, `allowedGrants`, `protectedWithSecret`.
- `TemporalAuthCode` does not store `codeChallenge` / `codeChallengeMethod`.
- No S256 verification in `TokenController`.

## What to implement

### 1. Add `requirePkce` to `ClientDetails`

`features/oauth/client/domain/ClientDetails.java`:

```java
private final boolean requirePkce;
```

### 2. Map `require_pkce` when building `ClientDetails`

In the driven adapter that loads `TrustedClient` and builds `ClientDetails`
(locate via `ClientDetailsGateway` implementation):

```java
.requirePkce(client.getRequirePkce().orElse(false))
```

Also map `publicAllow` (public clients always require PKCE):

```java
.requirePkce(client.getPublicAllow() || client.getRequirePkce().orElse(false))
```

### 3. Add PKCE fields to `TemporalAuthCode`

`features/oauth/session/domain/TemporalAuthCode.java`:

```java
@Builder.Default Optional<String> codeChallenge = Optional.empty();
@Builder.Default Optional<String> codeChallengeMethod = Optional.empty();
```

### 4. Propagate challenge from `AuthRequest` when creating `TemporalAuthCode`

In `AuthorizeHtml` (or wherever `TemporalAuthCode` is built):

```java
.codeChallenge(authRequest.getCodeChallenge())
.codeChallengeMethod(authRequest.getCodeChallengeMethod())
```

Validate at this point: if `clientDetails.requirePkce` and no `code_challenge` is present,
return `invalid_request` immediately without storing a code.

### 5. Enforce PKCE at the token endpoint

`features/oauth/authentication/infrastructure/driver/rest/TokenController.java` (code exchange):

```java
if (temporalCode.getCodeChallenge().isPresent()) {
    String verifier = formParams.getFirst("code_verifier");
    if (verifier == null) throw new OAuthException("invalid_grant");
    String computed = Base64.getUrlEncoder().withoutPadding()
        .encodeToString(MessageDigest.getInstance("SHA-256").digest(verifier.getBytes(UTF_8)));
    if (!computed.equals(temporalCode.getCodeChallenge().get()))
        throw new OAuthException("invalid_grant");
}
```

Reject `code_challenge_method=plain` at step 4 with `invalid_request`.

### 6. Update the discovery document

`features/oauth/oidc/infrastructure/driver/rest/OpenIdConfigurationController.java`:

```java
.codeChallengMethodsSupported(List.of("S256"))
```

(Also fix the typo `codeChallengMethods` → `codeChallengeMethodsSupported` per the Typo Fixes section.)

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `oauth/client/domain/ClientDetails.java` — add `requirePkce` |
| **Modify** | adapter that builds `ClientDetails` — map from `TrustedClient.requirePkce` |
| **Modify** | `oauth/session/domain/TemporalAuthCode.java` — add `codeChallenge`, `codeChallengeMethod` |
| **Modify** | `oauth/authentication/.../AuthorizeHtml.java` — propagate + validate PKCE at request start |
| **Modify** | `oauth/authentication/.../TokenController.java` — verify S256 at code exchange |
| **Modify** | `oauth/oidc/.../OpenIdConfigurationController.java` — advertise `S256` |
