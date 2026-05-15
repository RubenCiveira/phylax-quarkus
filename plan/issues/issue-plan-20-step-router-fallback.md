# Issue PLAN-20 — OidcStepRouter Unknown-Step Fallback + CDI Granter Discovery

**OAUTH_PLAN:** PLAN-20  
**Wave:** 3 — Technical debt

## Problem

1. An unrecognised `StepName` silently renders the login form, masking routing bugs in production.
2. New granters require manually editing a dispatch map — no CDI-based auto-discovery.

## Implementation steps

### 1. Unknown-step fallback in `OidcStepRouter`

`features/oauth/authentication/infrastructure/driver/html/OidcStepRouter.java` (or `AuthorizeHtml`):

Replace the implicit `default` fallback with an explicit failure:
```java
default -> throw new IllegalStateException(
    "No handler registered for step: " + step.name()
    + ". Register the step in OidcStepRouter or check for typos in StepName.");
```

This causes a 500 (caught by `@ServerExceptionMapper`) instead of silently showing the login form.
The error is immediately visible in logs, making routing bugs easy to diagnose.

### 2. CDI-based granter auto-discovery (optional)

Replace the explicit granter dispatch map in `TokenController` with CDI `@Any` injection:

```java
@Inject
@Any
Instance<TokenGranter> granters;

private TokenGranter resolve(String grantType) {
    return StreamSupport.stream(granters.spliterator(), false)
        .filter(g -> g.canHandle(grantType))
        .findFirst()
        .orElseThrow(() -> new OAuthException("unsupported_grant_type"));
}
```

Each `TokenGranter` implementation becomes `@ApplicationScoped` and is automatically registered
without touching `TokenController`. New granters (PLAN-12, PLAN-13, etc.) just implement the
interface and are injected automatically.

If the current dispatch map is already CDI-based, verify the `unsupported_grant_type` error
is returned correctly (not a 500 or 200 error page).

### 3. Add a `StepName` registry check at startup (optional)

Using Quarkus `@Observes StartupEvent`:
```java
void onStart(@Observes StartupEvent ev) {
    for (StepName step : StepName.values()) {
        if (!router.hasHandler(step)) {
            Log.warnf("OidcStepRouter: no handler registered for step %s", step);
        }
    }
}
```

## Files to modify

| Action | File |
|--------|------|
| **Modify** | `authentication/.../OidcStepRouter.java` — explicit unknown-step exception |
| **Modify** | `authentication/.../TokenController.java` — CDI-based granter resolution (if not already) |
