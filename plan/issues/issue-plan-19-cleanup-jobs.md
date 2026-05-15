# Issue PLAN-19 — Session and Code Cleanup Jobs

**OAUTH_PLAN:** PLAN-19  
**Wave:** 3 — Technical debt

## Problem

Expired rows accumulate indefinitely in session and temporal code tables.
No scheduled cleanup exists. Risks DB bloat under production load.

## Tables needing cleanup

| Table | Expiry column | Typical volume |
|-------|--------------|----------------|
| `_oauth_session` | `expiration` | High (one per user session) |
| `_oauth_temporal_codes` | `expiration` | Medium (one per authorization request) |
| `_oauth_revoked_jti` | `expires_at` | High (one per revoked token) |
| `_oauth_device_codes` | `expires_at` | Low |
| `_oauth_magic_link` | `expires_at` | Low |
| `_oauth_par_request` | `expires_at` | Low |
| `_oauth_webauthn_challenge` | `expires_at` | Low |
| `_oauth_delegated_state` | `expires_at` | Low |
| `_oauth_audit_log` | `created_at` | High — 90-day retention policy |

## Implementation steps

### 1. Create a `OAuthCleanupScheduler`

`features/oauth/session/infrastructure/OAuthCleanupScheduler.java`
(or one scheduler per bounded context — use a single class to keep it simple):

```java
@ApplicationScoped
public class OAuthCleanupScheduler {

    @Inject DataSource ds;

    @Scheduled(every = "1h", concurrentExecution = SKIP)
    void cleanExpiredSessions() {
        run("DELETE FROM _oauth_session WHERE expiration < NOW()");
        run("DELETE FROM _oauth_temporal_codes WHERE expiration < NOW()");
        run("DELETE FROM _oauth_revoked_jti WHERE expires_at < NOW()");
        run("DELETE FROM _oauth_device_codes WHERE expires_at < NOW()");
        run("DELETE FROM _oauth_magic_link WHERE expires_at < NOW()");
        run("DELETE FROM _oauth_par_request WHERE expires_at < NOW()");
        run("DELETE FROM _oauth_webauthn_challenge WHERE expires_at < NOW()");
        run("DELETE FROM _oauth_delegated_state WHERE expires_at < NOW()");
    }

    @Scheduled(cron = "0 3 * * *", concurrentExecution = SKIP)  // 03:00 daily
    void cleanAuditLog() {
        // Default 90-day retention; make configurable via @ConfigProperty if needed
        run("DELETE FROM _oauth_audit_log WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY)");
    }

    private void run(String sql) {
        try (var conn = ds.getConnection(); var stmt = conn.prepareStatement(sql)) {
            int deleted = stmt.executeUpdate();
            Log.debugf("Cleanup: %s — %d rows deleted", sql, deleted);
        } catch (Exception e) {
            Log.errorf(e, "Cleanup failed: %s", sql);
        }
    }
}
```

### 2. Verify `expires_at` indexes

Confirm indexes exist for each cleanup column — batch deletes without an index will cause
full table scans. Add missing indexes in a migration:

```sql
-- Only if not already present:
CREATE INDEX IF NOT EXISTS idx_oauth_session_exp       ON _oauth_session (expiration);
CREATE INDEX IF NOT EXISTS idx_oauth_temporal_codes_exp ON _oauth_temporal_codes (expiration);
```

The other tables were created with `idx_*_expires` indexes in `OAUTH_DDL_INIT.sql`.

### 3. Batch size guard (optional, for high-volume tables)

For `_oauth_session` and `_oauth_revoked_jti` which can have millions of rows, wrap the
delete in a loop that deletes at most 1000 rows per iteration to avoid long table locks:

```java
int deleted;
do {
    deleted = stmt.executeUpdate(); // SQL: DELETE ... LIMIT 1000
} while (deleted > 0);
```

## Files to create / modify

| Action | File |
|--------|------|
| **Create** | `oauth/session/infrastructure/OAuthCleanupScheduler.java` |
| **Modify** | Migration file — add missing `expires_at` indexes if absent |
