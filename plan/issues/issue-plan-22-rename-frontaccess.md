# Issue PLAN-22 — `FrontAcessController` Rename and Split

**OAUTH_PLAN:** PLAN-22  
**Wave:** 3 — Zero-effort typo fix (do immediately); split is incremental

## Problem

1. **Typo**: `FrontAcessController` (`Acess` → `Access`) propagates across imports and references.
2. **Responsibilities**: The controller mixes request routing, session bootstrapping, PAR resolution,
   step dispatch, and revocation in a single class.

## Implementation steps

### 1. Fix the typo (do now — zero risk)

Rename:
- `FrontAcessController.java` → `FrontAccessController.java`
- Class name, constructor, CDI qualifiers, all `@Path` annotations.
- Update all `import` statements across the codebase.
- Update any reference in `@Inject`, `@QuarkusTest`, REST client interfaces.

Use IDE rename refactoring (search-and-replace is error-prone for Java class names).

Also fix the other typos listed in OAUTH_PLAN.md while in the area:
- `allowdedGrant` → `allowedGrant` in `ClientDetails.java`
- `codeChallengMethodsSupported` → `codeChallengeMethodsSupported` in `OpenIdConfigurationController.java`
- `AuthenticationChallege` → `AuthenticationChallenge` in `authentication/domain/`

### 2. Extract `SessionRevocationController` (incremental)

Move the pre-session cookie revocation handler out of `FrontAccessController`:

`features/oauth/authentication/infrastructure/driver/html/SessionRevocationController.java`

This handles the `POST /{tenant}/oidc/revoke-session` (or equivalent) that clears the
`AUTH_SESSION_ID` cookie before starting a new authorization. The operation has nothing to do
with OIDC authorization — separating it makes the main controller's responsibility clearer.

### 3. Extract PAR resolution (incremental, low priority)

Over time, move the `request_uri` resolution logic into a dedicated
`ParRequestResolver.java` collaborator injected by `FrontAccessController`. This is a
low-priority cleanup — do it as part of PLAN-16 (PAR REST Driver) if possible.

## Files to rename / modify

| Action | File |
|--------|------|
| **Rename** | `authentication/.../FrontAcessController.java` → `FrontAccessController.java` |
| **Modify** | All files that import `FrontAcessController` |
| **Modify** | `client/domain/ClientDetails.java` — `allowdedGrant` → `allowedGrant` |
| **Modify** | `oidc/.../OpenIdConfigurationController.java` — `codeChallengMethods` typo |
| **Modify** | `authentication/domain/AuthenticationChallege.java` → `AuthenticationChallenge.java` |
| **Create** (incremental) | `authentication/.../SessionRevocationController.java` |
