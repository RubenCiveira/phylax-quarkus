# Issue 1.6 — MFA recovery codes — application layer and OIDC step

**Model section:** 1.6  
**OAUTH_PLAN:** PLAN-04 (MFA Recovery Codes)  
**Wave:** 0

## Architectural constraint

> No se crean nuevos casos de uso en `features/access/`. La lógica va directamente en
> `features/oauth/mfa/` usando los gateways de acceso ya disponibles, o como listener
> `@Observes` sobre eventos del dominio Access.

## Current state

- `access_user_mfa_recovery_code` table exists in DB ✅.
- Domain: `UserMfaRecoveryCode` entity with `uid`, `user`, `codeHash`, `createdAt`, `usedAt` ✅.
- Domain events: `UserMfaRecoveryCodeConsumeEvent`, `UserMfaRecoveryCodeCreateEvent` ✅.
- Infrastructure: `UserMfaRecoveryCodeReadGatewayAdapter`, `UserMfaRecoveryCodeWriteGatewayAdapter`,
  `UserMfaRecoveryCodeRepository` ✅ — injectable from any bounded context.
- **No recovery step** in the OIDC authentication flow.

## What to implement

### 1. Recovery code logic in `features/oauth/mfa/`

All business logic lives in the OAuth MFA context, using the Access gateways directly.

**`MfaRecoveryService.java`** — `features/oauth/mfa/application/MfaRecoveryService.java`

```java
@ApplicationScoped
@RequiredArgsConstructor
public class MfaRecoveryService {

    private final UserMfaRecoveryCodeReadRepositoryGateway readGateway;
    private final UserMfaRecoveryCodeWriteRepositoryGateway writeGateway;
    private final SecureTokenService tokenService;  // PLAN-18

    /** Generates 8 single-use codes, replacing any existing ones for the user. */
    public List<String> generateCodes(String userUid, int count) {
        // 1. Delete all existing codes for the user
        readGateway.list(UserMfaRecoveryCodeFilter.builder().user(userRef(userUid)).build())
            .forEach(code -> writeGateway.delete(code.delete()));

        // 2. Generate count raw codes and persist hashes
        List<String> rawCodes = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String raw = generateRawCode();          // SecureRandom, consonant alphabet, XXXX-XXXX
            String hash = tokenService.hash(raw);
            UserMfaRecoveryCode entity = UserMfaRecoveryCode.create(
                new UserMfaRecoveryCodeChangeSet()
                    .withUid(UUID.randomUUID().toString())
                    .withUser(userRef(userUid))
                    .withCodeHash(hash)
                    .withCreatedAt(Instant.now()));
            writeGateway.create(entity);
            rawCodes.add(raw);
        }
        return rawCodes;  // shown once, never stored again
    }

    /** Returns true and marks the code used if it matches; false otherwise. */
    public boolean consumeCode(String userUid, String rawCode) {
        String hash = tokenService.hash(rawCode);
        Optional<UserMfaRecoveryCode> match = readGateway.list(
            UserMfaRecoveryCodeFilter.builder().user(userRef(userUid)).build())
            .stream()
            .filter(c -> c.getUsedAt().isEmpty() && c.getCodeHash().equals(hash))
            .findFirst();

        match.ifPresent(code ->
            writeGateway.update(code.consume(Instant.now())));

        return match.isPresent();
    }
}
```

Raw code alphabet: consonants only (`BCDFGHJKLMNPQRSTVWXZ`) to avoid visual ambiguity (`0/O`, `I/1`).
Format: `XXXX-XXXX` (8 chars + hyphen separator).

### 2. `UserMfaRecoveryCodeConsumeEvent` listener — for side effects

If audit or notification is needed when a code is consumed, add a CDI observer in OAuth:

`features/oauth/mfa/application/MfaRecoveryCodeConsumedObserver.java`

```java
void onConsumed(@Observes UserMfaRecoveryCodeConsumeEvent event) {
    auditGateway.mfaRecoveryUsed(event.getPayload().getUser().getUid());
}
```

### 3. OIDC `RECOVER_MFA` step handler

`features/oauth/authentication/infrastructure/driver/html/` — add a step handler for `RECOVER_MFA`:

- The MFA form template renders a "Lost your device?" link that routes to `RECOVER_MFA`.
- `GET` renders the recovery code input form.
- `POST` receives `recovery_code` form field (+ `csid` — see Known Pitfall #2):
  1. Extract `userUid` from the current `SessionInfo`.
  2. Call `mfaRecoveryService.consumeCode(userUid, rawCode)`.
  3. If `true`: advance the step router (MFA challenge resolved).
  4. If `false`: re-render form with error key `mfa.recovery.invalid`.

### 4. Profile panel — regenerate recovery codes

In the OAuth profile area (not in Access), add a panel that:
- Shows the count of unused recovery codes (query by `user + usedAt IS NULL`).
- "Regenerate codes" button → calls `mfaRecoveryService.generateCodes(userUid, 8)` → displays
  the raw codes once with a warning.
- Requires ACR ≥ 2 (fresh MFA authentication) before regeneration.

## Dependencies

- `UserMfaRecoveryCodeReadRepositoryGateway` + `UserMfaRecoveryCodeWriteRepositoryGateway` — injectable ✅.
- PLAN-18 (`SecureTokenService`) — for `hash()`. Implement inline if PLAN-18 is not done yet.

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `oauth/mfa/application/MfaRecoveryService.java` |
| **Create** (if side effects needed) | `oauth/mfa/application/MfaRecoveryCodeConsumedObserver.java` |
| **Create** | `RECOVER_MFA` step handler in `oauth/authentication/infrastructure/driver/html/` |
| **Modify** | `oauth/authentication/.../OidcStepRouter.java` — register `RECOVER_MFA` step |
| **Modify** | MFA step template — add "Lost your device?" link |
| **Modify** | `messages/oauth.yaml` — add `mfa.recovery.invalid` |
| **Modify** | Profile area — add recovery code count panel + regenerate action |
