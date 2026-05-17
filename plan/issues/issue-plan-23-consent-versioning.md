# Issue PLAN-23 — Consent Versioning

**OAUTH_PLAN:** PLAN-23  
**Wave:** 4 — Deferred

## Problem

Users are not prompted to re-accept when terms of use are updated. The system doesn't track
which version of the terms a user accepted — any update to terms requires all users to accept
again with no ability to grandfatherly-accept unchanged clauses.

## Current state

- `UserAcceptedTermnsOfUse` BC exists ✅.
- `TenantTermsOfUse` entity with a version/content field likely exists.
- `TermsOfUseConsentUsecase` has a version-bump check (verify the persistence adapter stores it).

## Implementation steps

### 1. Verify `TermsOfUseConsentUsecase` stores the version

In `features/access/useracceptedtermnsofuse/application/` (or `oauth/consent/application/`):
- Ensure the accepted record stores the `terms_of_use` UID (FK to the specific version document).
- On re-consent prompt: load the latest `TenantTermsOfUse` for the tenant, compare its UID
  with the user's last accepted `terms_of_use` UID.
- If they differ: prompt re-consent. If equal: skip.

### 2. Check in the OIDC step router

`OidcStepRouter` — when evaluating whether the terms step is needed:
```java
Optional<String> lastAcceptedUid = termsGateway.getLastAcceptedTermsUid(userId, tenantId);
String currentTermsUid = termsGateway.getCurrentTermsUid(tenantId);
boolean needsConsent = lastAcceptedUid.map(uid -> !uid.equals(currentTermsUid)).orElse(true);
```

### 3. Store timestamp and IP with acceptance

If not already present, ensure `UserAcceptedTermnsOfUse` stores:
- `accepted_at TIMESTAMP`
- `ip_address VARCHAR(45)` (nullable)

These support GDPR audit (PLAN-28) and are useful for legal records.

## Files to modify

| Action | File |
|--------|------|
| **Verify/Modify** | `TermsOfUseConsentUsecase` — confirm version UID is stored with acceptance |
| **Modify** | `OidcStepRouter` / `TermsOfUseConsentGateway` — version-based re-prompt logic |
