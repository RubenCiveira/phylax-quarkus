# Issue PLAN-28 — GDPR: Consent Withdrawal, Data Export, Right to Erasure

**OAUTH_PLAN:** PLAN-28  
**Wave:** 4 — Deferred

## Three sub-tasks

### Sub-task A — Consent Withdrawal

**Requires PLAN-31 (Issue 1.8) to be complete.**

Add `revokeConsent(userId, clientId, scope)` to the profile area:

- In `features/access/userconsentedscopes/application/usecase/revoke/RevokeScopeConsentUseCase.java`
  (created in Issue 1.8): support revocation of all scopes for a client or a single scope.
- In `profile/` context: expose a consent withdrawal page (see PLAN-33 — Consent Management Page).
- After revocation: on the user's next login to that client, the consent step will reappear.

### Sub-task B — Data Export (Art. 20 GDPR)

New endpoint: `POST /{tenant}/account/me/export-data`

Flow:
1. User requests export (authenticated via session cookie, ACR ≥ 1).
2. System enqueues an async export job.
3. Job gathers:
   - Profile data (`access_user` row).
   - Active sessions (`_oauth_session` where `user_uid = userId`).
   - Consented scopes (`access_user_consented_scopes`).
   - Accepted terms (`access_user_accepted_termns_of_use`).
   - Audit log entries (`_oauth_audit_log` where `user_id = userId`).
   - Personal API keys (key names only, never `key_hash`).
4. Assembles a JSON or ZIP file.
5. Sends the file to the user's verified email address.
6. Deletes the export file after sending.

No export file is stored server-side longer than needed for the email send.

### Sub-task C — Right to Erasure (Art. 17 GDPR)

New endpoint: `POST /{tenant}/account/me/delete-account`

Flow:
1. User requests deletion (authenticated, ACR ≥ 2 or password re-confirmation).
2. System sends a verification email with a confirmation link (one-time token, 24h TTL).
3. User clicks the link.
4. System performs cascade anonymization:
   - `access_user`: anonymize `email` → `deleted-{uid}@deleted.invalid`, clear `name`, `password`.
   - `_oauth_session`: delete all sessions for the user.
   - `access_user_consented_scopes`: delete all consent records.
   - `_oauth_revoked_jti`: leave in place (token revocations must survive account deletion).
   - `_oauth_audit_log`: anonymize `user_id` → null (retain for security audit purposes).
5. Disable the user account (`enabled=false`) or delete the row depending on legal requirements.

## Dependencies

- Issue 1.8 (PLAN-31) — consent withdrawal.
- PLAN-33 (consent management page) — the UI for sub-task A.
- Issue 1.1 (email verified) — verified email is required for export delivery.

## Note

Each sub-task can be implemented independently. Start with Sub-task A (lowest risk),
then B, then C.
