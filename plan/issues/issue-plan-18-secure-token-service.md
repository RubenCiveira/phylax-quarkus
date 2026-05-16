# Issue PLAN-18 — SecureTokenService — Shared Token Hashing

**OAUTH_PLAN:** PLAN-18  
**Wave:** 3 — Technical debt

## Problem

`MagicLink`, `UserInvitation`, and `Session` each implement their own token hashing
with no shared service, no documented algorithm, and no constant-time comparison guarantee.
This risks timing-attack vulnerabilities and inconsistent security properties.

## Implementation steps

### 1. Create `SecureTokenService`

`shared/security/SecureTokenService.java` (or a shared infrastructure module):

```java
@ApplicationScoped
public class SecureTokenService {

    private static final int TOKEN_BYTES = 32;

    /** Generates a cryptographically random Base64URL token (no padding). */
    public String generate() {
        byte[] bytes = new byte[TOKEN_BYTES];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /** Returns the SHA-256 hex digest of the raw token for storage. */
    public String hash(String rawToken) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /** Constant-time comparison to prevent timing attacks. */
    public boolean verify(String rawToken, String storedHash) {
        String computedHash = hash(rawToken);
        return MessageDigest.isEqual(
            computedHash.getBytes(StandardCharsets.UTF_8),
            storedHash.getBytes(StandardCharsets.UTF_8)
        );
    }
}
```

### 2. Replace ad-hoc hashing in existing adapters

After creating `SecureTokenService`, replace inline hashing in:

- `features/oauth/magiclink/infrastructure/driven/MagicLinkGatewayAdapter.java`
- `features/oauth/userinvitation/infrastructure/driven/InvitationStoreAdapter.java`
- `features/oauth/session/infrastructure/driven/TemporalKeysSqlAdapter.java` (if it hashes tokens)
- Any new use case that needs token hashing (Issue 1.6 MFA recovery codes, Issue 1.7 API keys).

### 3. Ensure usages in Issue 1.6 and Issue 1.7

- Issue 1.6 (`GenerateRecoveryCodesUseCase`): use `secureTokenService.generate()` and
  `secureTokenService.hash()` instead of inline `SecureRandom` + `MessageDigest`.
- Issue 1.7 (`UserPersonalApiKeyController`): same.

## Acceptance criteria

- No inline `MessageDigest` SHA-256 code remains outside of `SecureTokenService`.
- `SecureTokenService` is unit-tested: generate returns unique values, hash is deterministic,
  verify is correct and uses constant-time comparison.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `shared/security/SecureTokenService.java` |
| **Modify** | `features/oauth/magiclink/infrastructure/driven/MagicLinkGatewayAdapter.java` — use service |
| **Modify** | `features/oauth/userinvitation/infrastructure/driven/InvitationStoreAdapter.java` — use service |
