# Issue PLAN-33 — GDPR Consent Management Page (Art. 7)

**OAUTH_PLAN:** PLAN-33  
**Wave:** 4 — Deferred

## Problem

Users have no unified interface to see and withdraw the consents they have given:
terms of use and per-client OAuth scope grants. This is required by GDPR Art. 7(3).

## Current state

- `UserAcceptedTermnsOfUse` BC exists ✅.
- `userconsentedscopes` BC exists at the domain level; application use cases are created
  in Issue 1.8 (PLAN-31) ✅ (prerequisite).
- No HTML controller for the consent management page.

## Implementation steps

### 1. HTML controller

`features/access/userconsentedscopes/infrastructure/driver/html/ConsentManagementController.java`

(Alternatively in `profile/infrastructure/driver/html/` — pick the location that fits the
tenant's URL scheme best.)

```
GET  /{tenant}/account/consents          — show the page
POST /{tenant}/account/consents/revoke   — revoke a specific consent
```

Authentication required (session cookie). ACR ≥ 1 (password-level) — no MFA required for viewing,
but confirm with a re-auth prompt before batch revocation.

### 2. Page content

The page renders two sections:

**Section A — Terms of Use**
- List all `UserAcceptedTermnsOfUse` records for the user.
- Each row: terms title, accepted date, current version vs accepted version.
- If current version differs from accepted: show a "re-accept required" badge.
- No revocation UI for terms (legally cannot unaccept; withdrawal means account deletion — PLAN-28).

**Section B — OAuth Scope Grants (per client)**
- Group `access_user_consented_scopes` records by `trusted_client`.
- Per client: client name, list of granted scopes with grant dates.
- "Revoke access" button per client: calls `RevokeScopeConsentUseCase` for all scopes of that client.
- "Revoke" button per scope: calls `RevokeScopeConsentUseCase` for a single scope.

### 3. After revocation

After revoking OAuth scope grants:
- Do **not** revoke existing access tokens immediately (that would be PLAN-28 territory).
- On the user's next login to that client, the consent step will re-appear.
- Show a confirmation message: "Access for [client] has been revoked. Existing sessions
  for this app may remain active until they expire."

### 4. Template

`src/main/resources/templates/oauth/account/consents.html`:
- Two sections as described above.
- Responsive layout consistent with the profile pages.
- Use the tenant theme (via `DecorateHtml` — two-pass rendering).

## Dependencies

- Issue 1.8 (PLAN-31) — `RevokeScopeConsentUseCase` must exist.
- PLAN-23 (consent versioning) — "re-accept required" badge needs version tracking.

## Files to create

| Action | File |
|--------|------|
| **Create** | `access/userconsentedscopes/infrastructure/driver/html/ConsentManagementController.java` |
| **Create** | `templates/oauth/account/consents.html` |
