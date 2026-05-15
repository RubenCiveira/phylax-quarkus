# OAuth Feature — Model & Database Changes

> Reference for IAM team and database engineers.
> Cross-referenced with `OAUTH_PLAN.md` and the current migration at
> `src/main/resources/migration/mysql/000.001.001/20260514125137-model.changelog.mysql.sql`.
>
> **Convention:**
> - **Part 1 — Access Model:** master data managed explicitly by the IAM team through the
>   management API / UI. Changes are expressed as additions to the `work-project` XML model
>   and generate `access_*` tables via the model compiler.
> - **Part 2 — SQL DDL:** operational state managed autonomously by the OAuth flow.
>   No IAM management UI. Changes are hand-crafted SQL migrations on `_oauth_*` tables.

---

## Part 1 — Access Model Changes

Each subsection corresponds to one XML change. All properties are additions unless explicitly
marked `MODIFY`.

---

### 1.1 `user` — Add `email-verified`

**Plan:** PLAN-10 (Email Verification After Registration)
**SQL column to add:** `email_verified BIT DEFAULT 0 NULL` on `access_user`

The `register_code` / `register_code_expiration` flow in `user-access-temporal-code` already
models the verification token. What is missing is the confirmed-flag on the user aggregate itself.

**XML to add inside `<entity name="user">`:**
```xml
<property name="email-verified" type="xs:boolean" default="false" calculated="true"
    description="Indicates whether the user has confirmed ownership of their email address
    by clicking the verification link sent at registration. Used by the OIDC flow to emit
    the email_verified claim and optionally block login until confirmed." />
```

**XML to add inside `<facade type="crud">` of `user`:**
```xml
<action name="verify-email" contextual="true" internal="true"
    description="Marks the user's email address as verified. Called by the email verification
    endpoint after validating the register-code token from user-access-temporal-code.">
    <set property="email-verified" value="true" />
</action>
```

**Migration SQL:**
```sql
-- migration: add email_verified to access_user
ALTER TABLE access_user
    ADD COLUMN email_verified BIT NOT NULL DEFAULT 0 AFTER email;
```

---

### 1.2 `trusted-client` — Add `require-pkce`

**Plan:** PLAN-01 (PKCE Full Enforcement)
**SQL column to add:** `require_pkce BIT DEFAULT 0 NOT NULL` on `access_trusted_client`

The existing `public_allow` flag means PKCE is implicit for public clients. `require_pkce`
allows confidential clients to also enforce PKCE explicitly (recommended for all new clients).

**XML to add inside `<entity name="trusted-client">`:**
```xml
<property name="require-pkce" type="xs:boolean" required="true" default="false"
    description="If true, every authorization code request from this client must include
    code_challenge and code_challenge_method=S256. For clients with public-allow=true,
    PKCE is always required regardless of this flag." />
```

**Migration SQL:**
```sql
-- migration: add require_pkce to access_trusted_client
ALTER TABLE access_trusted_client
    ADD COLUMN require_pkce BIT NOT NULL DEFAULT 0 AFTER public_allow;
```

---

### 1.3 `trusted-client` — Add `is-resource-server`

**Plan:** PLAN-11 (Token Introspection, RFC 7662)
**SQL column to add:** `is_resource_server BIT DEFAULT 0 NOT NULL` on `access_trusted_client`

Gates access to the POST /introspect endpoint. Only clients with this flag set can call
introspection to validate tokens issued by the authorization server.

**XML to add inside `<entity name="trusted-client">`:**
```xml
<property name="is-resource-server" type="xs:boolean" required="true" default="false"
    description="If true, this client is allowed to call the token introspection endpoint
    (POST /introspect) to validate access tokens. Resource servers must authenticate at
    the introspection endpoint using their client credentials." />
```

**Migration SQL:**
```sql
-- migration: add is_resource_server to access_trusted_client
ALTER TABLE access_trusted_client
    ADD COLUMN is_resource_server BIT NOT NULL DEFAULT 0 AFTER require_pkce;
```

---

### 1.4 `tenant-login-provider` — Add `OIDC` source option and `oidc-discovery-url`

**Plan:** PLAN-14 (Generic OIDC Delegated Provider)
**SQL column to add:** `oidc_discovery_url VARCHAR(512) NULL` on `access_tenant_login_provider`

The `source` enum currently allows GOOGLE, GITHUB, MICROSOFT, APPLE, SAML. Adding OIDC enables
any standards-compliant OIDC IdP (Okta, Keycloak, Auth0, custom) without code changes.
`public_key` stores the OIDC client_id; `private_key` stores the OIDC client_secret.

**XML to MODIFY inside `<entity name="tenant-login-provider">`:**
```xml
<!-- MODIFY: add OIDC to the existing <property name="source"> options -->
<property name="source" type="xs:string" required="true"
    description="The source protocol or system used for authentication.">
    <option>GOOGLE</option>
    <option>GITHUB</option>
    <option>MICROSOFT</option>
    <option>APPLE</option>
    <option>SAML</option>
    <option>OIDC</option>   <!-- NEW -->
</property>
```

**XML to ADD inside `<entity name="tenant-login-provider">`:**
```xml
<property name="oidc-discovery-url" type="xs:string"
    description="For source=OIDC: the /.well-known/openid-configuration URL of the external
    OIDC identity provider. The system will fetch authorization_endpoint, token_endpoint, and
    jwks_uri from this document at startup. public-key holds the OIDC client_id;
    private-key holds the OIDC client_secret." />
```

**Migration SQL:**
```sql
-- migration: add OIDC provider support to access_tenant_login_provider
ALTER TABLE access_tenant_login_provider
    ADD COLUMN oidc_discovery_url VARCHAR(512) NULL AFTER saml_idp_sso_url;
```

---

### 1.5 `tenant-config` — Add operational policy properties  *(antes 1.7)*

**Plan:** PLAN-09 (SSO), PLAN-10 (Email Verification), PLAN (Magic Link / Invitation TTLs)

Several TTLs and behavioural policies are currently hardcoded in application logic. Moving them
to `tenant-config` enables per-tenant customisation without code changes.

**XML to add inside `<entity name="tenant-config">`:**
```xml
<property name="require-email-verification" type="xs:boolean" required="true" default="false"
    description="If true, users must confirm their email address via the verification link
    before they can complete their first login. The OIDC flow will hold the user at a
    verification-pending challenge until the link is clicked." />

<property name="invitation-expiry-days" type="xs:integer" required="true" default="7"
    description="Number of days before an unused user invitation token expires.
    Currently hardcoded to 7 days in InvitationCreateUsecase." />

<property name="magic-link-expiry-minutes" type="xs:integer" required="true" default="30"
    description="Number of minutes before a magic-link token expires after being sent.
    Only relevant when magic-link-enabled=true." />

<property name="session-sso-ttl-seconds" type="xs:integer" required="true" default="3600"
    description="Lifetime in seconds of the cross-client SSO session cookie.
    After this duration the user must re-authenticate even if a per-flow session is still valid.
    Corresponds to the max_age upper bound for prompt=none requests." />

<property name="refresh-token-ttl-seconds" type="xs:integer" required="true" default="2592000"
    description="Lifetime in seconds of issued refresh tokens. Defaults to 30 days (2 592 000 s).
    Applies to all grant types that issue refresh tokens for this tenant." />
```

**Migration SQL:**
```sql
-- migration: add policy columns to access_tenant_config
ALTER TABLE access_tenant_config
    ADD COLUMN require_email_verification    BIT          NOT NULL DEFAULT 0  AFTER magic_link_enabled,
    ADD COLUMN invitation_expiry_days        INT          NOT NULL DEFAULT 7  AFTER require_email_verification,
    ADD COLUMN magic_link_expiry_minutes     INT          NOT NULL DEFAULT 30 AFTER invitation_expiry_days,
    ADD COLUMN session_sso_ttl_seconds       INT          NOT NULL DEFAULT 3600   AFTER magic_link_expiry_minutes,
    ADD COLUMN refresh_token_ttl_seconds     INT          NOT NULL DEFAULT 2592000 AFTER session_sso_ttl_seconds;
```

---

### 1.6 New entity: `user-mfa-recovery-code`

**Plan:** PLAN-04 (MFA Recovery Codes)

Single-use hashed recovery codes generated when a user enrolls in TOTP. The raw code is shown
once at enrollment and never stored; only the SHA-256 hash is persisted.
Typically 8–10 codes are generated per enrollment set.

**Full XML entity to add to the model:**
```xml
<entity name="user-mfa-recovery-code" id="uid" generator="uuid" display="uid" group="access">
    <description>
        Single-use MFA recovery codes generated at TOTP enrollment time.
        Each code is stored as a SHA-256 hash; the raw value is shown to the user only once.
        A consumed code has used-at set; unused codes have used-at null.
        All codes for a user are replaced atomically when the user regenerates their recovery set.
    </description>

    <property name="uid" type="xs:string" unique="true" required="true"
        description="A uid string to identify the entity" />
    <property name="user" type="tns:user" required="true"
        description="The user who owns these recovery codes." />
    <property name="code-hash" type="xs:string" required="true"
        description="SHA-256 hex digest of the raw recovery code. The raw code is never stored." />
    <property name="used-at" type="xs:datetime" calculated="true"
        description="Timestamp when this code was consumed via the MFA recovery step.
        Null means the code is still valid and unused." />
    <property name="created-at" type="xs:datetime" required="true" default="now" calculated="true"
        description="Timestamp when this recovery code was generated." />

    <filter name="user-unused" intern="true">
        <and>
            <part property="user" operator="eq" />
            <part property="used-at" operator="is-null" />
        </and>
    </filter>
    <filter name="user" property="user" operator="eq" />

    <facade type="crud">
        <action name="consume" contextual="true" internal="true"
            description="Marks this code as used. Called atomically when a valid code is
            submitted during MFA recovery. Once used, the code cannot be reused.">
            <param required="true" property="used-at" />
        </action>
    </facade>
</entity>
```

**Generated table DDL (reference — produced by model compiler):**
```sql
CREATE TABLE access_user_mfa_recovery_code (
    uid        VARCHAR(255) NOT NULL,
    version    INT          NOT NULL,
    code_hash  VARCHAR(255) NOT NULL,
    created_at timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    used_at    timestamp    DEFAULT NULL NULL,
    user       VARCHAR(255) NOT NULL,
    CONSTRAINT PK_ACCESS_USER_MFA_RECOVERY_CODE PRIMARY KEY (uid),
    INDEX idx_umrc_user (user),
    INDEX idx_umrc_user_unused (user, used_at)
);
```

---

### 1.7 New entity: `user-personal-api-key` *(Wave 4 — deferred)*

**Plan:** PLAN-26 (Personal Access Tokens for Users)

User-scoped long-lived API keys for CLI tools and scripts. The raw key is shown once at creation;
only its SHA-256 hash is stored for comparison.

**Full XML entity to add to the model:**
```xml
<entity name="user-personal-api-key" id="uid" generator="uuid" display="name" group="access">
    <description>
        A personal API key (PAT) scoped to a specific user. The raw key is shown to the user
        only at creation time and is never stored. Subsequent authentication uses the SHA-256
        hash for constant-time comparison.
    </description>

    <property name="uid" type="xs:string" unique="true" required="true" />
    <property name="user" type="tns:user" required="true" />
    <property name="name" type="xs:string" required="true"
        description="A label to identify the key (e.g. 'CI pipeline', 'local dev')." />
    <property name="key-hash" type="xs:string" required="true" unique="true"
        description="SHA-256 hex digest of the raw key. Never stored in plain text." />
    <property name="scopes" type="xs:string" required="true"
        description="Space-separated OAuth scopes granted to this key." />
    <property name="last-used-at" type="xs:datetime" calculated="true"
        description="Timestamp of the most recent successful authentication." />
    <property name="expires-at" type="xs:datetime"
        description="Optional expiry. Null means the key does not expire." />
    <property name="created-at" type="xs:datetime" required="true" default="now" calculated="true" />
    <property name="enabled" type="xs:boolean" required="true" default="true" calculated="true" />

    <filter name="key-hash" property="key-hash" operator="eq" />
    <filter name="user"     property="user"     operator="eq" />

    <facade type="crud" path="access/me/api-keys">
        <action name="touch" contextual="true" internal="true"
            description="Updates last-used-at on each successful authentication.">
            <param required="true" property="last-used-at" />
        </action>
        <action name="revoke" contextual="true" icon="delete"
            description="Disables the key without deleting it, preserving the audit trail.">
            <set property="enabled" value="false" />
        </action>
    </facade>
</entity>
```

---

## Part 2 — SQL DDL: Operational State Tables

Hand-crafted tables for ephemeral OAuth state. No IAM management UI. Table names use the
`_oauth_` prefix consistent with existing schema conventions.

---

### 2.1 `_oauth_session` — Modify existing table

**Plan:** PLAN-07 (`max_age` / `acr_values`), PLAN-09 (SSO cross-client)

Current DDL has: `session, expiration, client_id, issuer, auth_data, csid, revoked_at,
ip_address, user_agent, last_used_at, client_name`.

Missing columns needed for SSO and step-up authentication:

```sql
-- migration: extend _oauth_session for SSO and ACR support
ALTER TABLE _oauth_session
    -- Real authentication timestamp (password/MFA step) — needed for max_age enforcement
    ADD COLUMN auth_time        DATETIME(3) NULL AFTER last_used_at,

    -- ACR level reached: 0=cookie-only, 1=password, 2=mfa
    ADD COLUMN acr              TINYINT     NOT NULL DEFAULT 0 AFTER auth_time,

    -- JSON array of client_ids that share this SSO session (for back-channel logout fanout)
    -- Example: ["client-a", "client-b"]
    ADD COLUMN sso_clients_json MEDIUMTEXT  NULL AFTER acr;
```

*Reference — full table DDL (existing columns + new columns):*
```sql
CREATE TABLE _oauth_session (
    session             VARCHAR(255) NOT NULL,
    expiration          TIMESTAMP    DEFAULT NULL NULL,
    client_id           VARCHAR(250) NOT NULL,
    issuer              VARCHAR(255) NOT NULL,
    auth_data           TEXT         NOT NULL,
    csid                TEXT         NOT NULL,
    revoked_at          TIMESTAMP    DEFAULT NULL NULL,
    ip_address          VARCHAR(45)  NULL,
    user_agent          VARCHAR(250) NULL,
    last_used_at        DATETIME     DEFAULT NULL NULL,
    client_name         VARCHAR(200) NULL,
    auth_time           DATETIME(3)  NULL,                    -- ← new: real auth timestamp
    acr                 TINYINT      NOT NULL DEFAULT 0,      -- ← new: 0=cookie, 1=password, 2=mfa
    sso_clients_json    MEDIUMTEXT   NULL,                    -- ← new: SSO client fanout list
    CONSTRAINT PK__OAUTH_SESSION PRIMARY KEY (session)
);
```

---

### 2.2 `_oauth_temporal_codes` — Already complete ✅

**Plan:** PLAN-01 (PKCE)

The columns `code_challenge VARCHAR(128) NULL` and `code_challenge_method VARCHAR(10) NULL`
already exist in the current migration. **No DDL change required.**

The gap is entirely in the Java layer: `TemporalAuthCode` must read/write these columns and
`TokenController` must enforce the S256 verification.

*Reference — current DDL:*
```sql
-- existing — no change needed
CREATE TABLE _oauth_temporal_codes (
    code                  VARCHAR(255) NOT NULL,
    code_data             TEXT         NOT NULL,
    expiration            TIMESTAMP    NOT NULL,
    code_challenge        VARCHAR(128) NULL,       -- ← already present
    code_challenge_method VARCHAR(10)  NULL        -- ← already present
);
```

---

### 2.3 `_oauth_revoked_jti` — Already complete ✅

**Plan:** PLAN-02 (Refresh Token Rotation), PLAN-03 (Revocation Endpoint)

The table already exists with the correct structure:
`jti, tenant_id, token_type ENUM('access','refresh'), revoked_at, expires_at`.
**No DDL change required.**

The gap is in the Java layer: `RefreshGranter` must call `TokenRevocationGateway.revokeToken()`
after issuing the replacement refresh token, and the `POST /revocation` REST driver must be wired.

*Reference — current DDL:*
```sql
-- existing — no change needed
CREATE TABLE _oauth_revoked_jti (
    jti        VARCHAR(36)                   NOT NULL,
    tenant_id  VARCHAR(36)                   NOT NULL,
    token_type ENUM('access','refresh')      NOT NULL DEFAULT 'access',
    revoked_at DATETIME                      NOT NULL DEFAULT NOW(),
    expires_at DATETIME                      NOT NULL,
    CONSTRAINT PK__OAUTH_REVOKED_JTI PRIMARY KEY (jti)
);
```

---

### 2.4 `_oauth_session_grant` — Already complete ✅

Grants associated to a session. Each grant records the client, grant type, scope, and audiences
for which tokens have been issued. Referenced by `_oauth_session_token.grant_id`.
**No DDL change required.**

*Reference — current DDL:*
```sql
-- existing — no change needed
CREATE TABLE _oauth_session_grant (
    id         VARCHAR(36)  NOT NULL,
    session    VARCHAR(255) NOT NULL,
    client_id  VARCHAR(250) NOT NULL,
    grant_type VARCHAR(50)  NOT NULL,
    scope      TEXT         NULL,
    audiences  TEXT         NOT NULL,
    auth_data  TEXT         NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    revoked_at DATETIME     DEFAULT NULL NULL,
    CONSTRAINT PK__OAUTH_SESSION_GRANT PRIMARY KEY (id),
    CONSTRAINT FK__OAUTH_SESSION_GRANT_SESSION
        FOREIGN KEY (session) REFERENCES _oauth_session (session)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### 2.5 `_oauth_session_token` — Already complete ✅

**Plan:** PLAN-11 (Token Introspection)

The table already tracks issued JTIs with expiry and revocation state:
`id, session, jti, refresh_jti, issued_at, expires_at, revoked_at, grant_id, client_id, scope, audiences, auth_data`.
**No DDL change required.**

*Reference — current DDL:*
```sql
-- existing — no change needed
CREATE TABLE _oauth_session_token (
    id          VARCHAR(36)  NOT NULL,
    session     VARCHAR(255) NOT NULL,
    jti         VARCHAR(36)  NOT NULL,
    refresh_jti VARCHAR(36)  NOT NULL,
    issued_at   DATETIME     NOT NULL,
    expires_at  DATETIME     NOT NULL,
    revoked_at  DATETIME     DEFAULT NULL NULL,
    grant_id    VARCHAR(36)  NULL,
    client_id   VARCHAR(250) NULL,
    scope       TEXT         NULL,
    audiences   TEXT         NULL,
    auth_data   TEXT         NULL,
    CONSTRAINT PK__OAUTH_SESSION_TOKEN PRIMARY KEY (id),
    UNIQUE KEY UK__OAUTH_SESSION_TOKEN_JTI (jti),
    UNIQUE KEY UK__OAUTH_SESSION_TOKEN_REFRESH_JTI (refresh_jti),
    CONSTRAINT FK__OAUTH_SESSION_TOKEN_SESSION
        FOREIGN KEY (session) REFERENCES _oauth_session (session)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK__OAUTH_SESSION_TOKEN_GRANT
        FOREIGN KEY (grant_id) REFERENCES _oauth_session_grant (id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_session_token_session ON _oauth_session_token (session);
CREATE INDEX idx_session_token_exp     ON _oauth_session_token (expires_at);
```

---

### 2.6 `_oauth_device_codes` — Already complete ✅

**Plan:** PLAN-13 (Device Authorization Grant)

The `last_poll_at` column already exists for `slow_down` enforcement.
The `status` column is `VARCHAR(16)` (PENDING, APPROVED, DENIED).
**No DDL change required.**

*Reference — current DDL:*
```sql
-- existing — no change needed
CREATE TABLE _oauth_device_codes (
    device_code  VARCHAR(36)  NOT NULL,
    user_code    VARCHAR(16)  NOT NULL,
    tenant       VARCHAR(64)  NOT NULL,
    client_id    VARCHAR(128) NOT NULL,
    scope        TEXT         NOT NULL,
    audiences    JSON         NOT NULL,
    status       VARCHAR(16)  NOT NULL,
    auth_data    JSON         NULL,
    requested_at DATETIME     NOT NULL,
    expires_at   DATETIME     NOT NULL,
    last_poll_at DATETIME     NULL,                  -- ← slow_down enforcement
    interval_sec INT          NOT NULL,
    PRIMARY KEY (device_code),
    UNIQUE KEY uq_device_user_code (tenant, user_code),
    KEY idx_device_expires (expires_at),
    KEY idx_device_status  (status),
    KEY idx_device_client  (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

---

### 2.7 `_oauth_magic_link` — Already complete ✅

**Plan:** PLAN (MagicLink)

Existing structure is complete: `uid, tenant_id, user_uid, client_id, token_hash, redirect_uri, nonce, scope, state, created_at, expires_at, used_at`.
**No DDL change required.**

---

### 2.8 `_oauth_par_request` — Already complete ✅

**Plan:** PLAN-16 (PAR REST Driver)

Existing structure is complete: `request_uri (PK), tenant_id, client_id, params_json, created_at, expires_at, used_at`.
**No DDL change required.** The REST driver and client authentication enforcement are Java-only gaps.

---

### 2.9 `_oauth_webauthn_challenge` — Already complete ✅

**Plan:** PLAN (WebAuthn)

Existing structure: `challenge_id, tenant_id, user_uid, challenge, type ENUM('register','authenticate'), created_at, expires_at`.
**No DDL change required.**

---

### 2.10 `_oauth_keys_storer` — Already complete ✅

Stores the active JWK signing key pairs per tenant. Each row holds the RSA private and public key
material (PEM/JWK format) along with its `keyid`, validity window (`since`/`expiration`), and
tenant reference. Used by `TokenSigner` to locate the current signing key and expose the JWKS endpoint.
**No DDL change required.**

*Reference — current DDL:*
```sql
-- existing — no change needed
CREATE TABLE _oauth_keys_storer (
    expiration TIMESTAMP    NOT NULL,
    since      TIMESTAMP    NOT NULL,
    keyid      VARCHAR(255) NOT NULL,
    private    TEXT         NOT NULL,
    public     TEXT         NOT NULL,
    tenant     VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```

---

### 2.11 `_oauth_temporal_keys` — Already complete ✅

Tracks the current and previous CSRF/session encryption key IDs used by `TemporalKeysGateway`.
The single-row table (one row per tenant) allows the system to rotate HMAC keys while still
accepting tokens signed with the immediately previous key during the grace period.
**No DDL change required.**

*Reference — current DDL:*
```sql
-- existing — no change needed
CREATE TABLE _oauth_temporal_keys (
    current    VARCHAR(255) NOT NULL,
    old        VARCHAR(255) NOT NULL,
    expiration TIMESTAMP    NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```

---

### 2.12 `_oauth_delegated_codes` — **DROP TABLE** ⚠️

**Plan:** PLAN-14 (Generic OIDC Provider), PLAN-08 (Federated Login CSRF State)

The current `_oauth_delegated_codes (expiration, code, token)` is a minimal 3-column table that
stores the external provider's token against an internal code. It lacks CSRF state, session
context, and provider reference. It must be **dropped** and replaced by `_oauth_delegated_state`.

```sql
-- migration: drop obsolete delegated codes table
DROP TABLE IF EXISTS _oauth_delegated_codes;
```

The replacement table:

```sql
-- migration: replace delegated codes with structured delegated state table
CREATE TABLE _oauth_delegated_state (
    uid                  VARCHAR(36)   NOT NULL,
    tenant_id            VARCHAR(36)   NOT NULL,

    -- Opaque CSRF state token included in the IdP redirect as the OAuth `state` parameter
    state_token          VARCHAR(128)  NOT NULL,

    -- Reference to access_tenant_login_provider.uid
    provider_id          VARCHAR(36)   NOT NULL,

    -- Serialized OIDC session context to restore on callback (AuthRequest snapshot + session uid)
    session_context_json MEDIUMTEXT    NOT NULL,

    created_at           DATETIME(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    expires_at           DATETIME(3)   NOT NULL,   -- typically 10 minutes

    CONSTRAINT PK__OAUTH_DELEGATED_STATE PRIMARY KEY (uid),
    UNIQUE  INDEX idx_oauth_delegated_state_token   (state_token),
    INDEX        idx_oauth_delegated_state_expires  (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**Cleanup job:** `DELETE FROM _oauth_delegated_state WHERE expires_at < NOW();`

---

### 2.13 `_oauth_audit_log` — New table (NEW)

**Plan:** PLAN-15 (Audit Logging Completeness)

Append-only event log for all security-relevant OAuth operations. Complements the existing
`_audit_access_user` / `_audit_access_trusted_client` model-level audit tables which track
IAM data changes; this table tracks runtime authentication events.

```sql
-- migration: create OAuth runtime audit log
CREATE TABLE _oauth_audit_log (
    uid            VARCHAR(36)    NOT NULL,
    tenant_id      VARCHAR(36)    NOT NULL,

    event_type     VARCHAR(64)    NOT NULL,
    -- Vocabulary (non-exhaustive):
    --   LOGIN_SUCCESS, LOGIN_FAILED, ACCOUNT_LOCKED, ACCOUNT_UNLOCKED
    --   TOKEN_ISSUED, TOKEN_REVOKED, TOKEN_REFRESHED
    --   MFA_VERIFIED, MFA_FAILED, MFA_RECOVERY_USED
    --   LOGOUT, SESSION_REVOKED
    --   CONSENT_ACCEPTED, TERMS_ACCEPTED
    --   DELEGATION_LOGIN, EMAIL_VERIFIED
    --   PASSWORD_CHANGED, PASSWORD_RECOVERY_REQUESTED

    user_id        VARCHAR(36)    NULL,    -- null for pure client events
    client_id      VARCHAR(36)    NULL,
    session_id     VARCHAR(255)   NULL,

    -- Token context (for TOKEN_* events)
    jti            VARCHAR(36)    NULL,
    grant_type     VARCHAR(64)    NULL,
    scope          TEXT           NULL,
    acr            VARCHAR(8)     NULL,

    -- Request context
    ip_address     VARCHAR(64)    NULL,
    user_agent     VARCHAR(512)   NULL,

    -- Failure reason (anti-enumeration: never reveals whether the user exists)
    failure_reason VARCHAR(64)    NULL,
    -- Values: WRONG_CREDENTIALS, ACCOUNT_LOCKED, ACCOUNT_DISABLED,
    --         MFA_FAILED, TOKEN_EXPIRED, TOKEN_REVOKED

    -- Optional structured payload (delegation provider, consent scopes, etc.)
    payload_json   MEDIUMTEXT     NULL,

    created_at     DATETIME(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    CONSTRAINT PK__OAUTH_AUDIT_LOG PRIMARY KEY (uid),
    INDEX idx_oauth_audit_user    (user_id,    tenant_id, created_at),
    INDEX idx_oauth_audit_type    (event_type, tenant_id, created_at),
    INDEX idx_oauth_audit_session (session_id),
    INDEX idx_oauth_audit_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**Retention policy:** Configurable per tenant; default 90 days.
**Cleanup job:** `DELETE FROM _oauth_audit_log WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY);`

---

## Summary Tables

### Access Model — changes by wave

| # | Entity | Type | Change | Wave |
|---|--------|------|--------|------|
| 1.1 | `user` | Modify | Add `email-verified` + action `verify-email` | 1 |
| 1.2 | `trusted-client` | Modify | Add `require-pkce` | 0 |
| 1.3 | `trusted-client` | Modify | Add `is-resource-server` | 1 |
| 1.4 | `tenant-login-provider` | Modify | Add `OIDC` source option + `oidc-discovery-url` | 2 |
| 1.5 | `tenant-config` | Modify | Add 5 policy properties (email-verification, TTLs, SSO) | 1 |
| 1.6 | `user-mfa-recovery-code` | **New entity** | Full TOTP recovery code model | 0 |
| 1.7 | `user-personal-api-key` | **New entity** | User-scoped PAT | 4 |

### SQL State Tables — status by wave

| # | Table | Status | Action | Wave |
|---|-------|--------|--------|------|
| 2.1 | `_oauth_session` | Exists | **ALTER** — add `auth_time`, `acr`, `sso_clients_json` | 1 |
| 2.2 | `_oauth_temporal_codes` | Exists ✅ | No change — PKCE columns already present | — |
| 2.3 | `_oauth_revoked_jti` | Exists ✅ | No change — structure complete | — |
| 2.4 | `_oauth_session_grant` | Exists ✅ | No change | — |
| 2.5 | `_oauth_session_token` | Exists ✅ | No change — JTI tracking complete | — |
| 2.6 | `_oauth_device_codes` | Exists ✅ | No change — `last_poll_at` already present | — |
| 2.7 | `_oauth_magic_link` | Exists ✅ | No change | — |
| 2.8 | `_oauth_par_request` | Exists ✅ | No change | — |
| 2.9 | `_oauth_webauthn_challenge` | Exists ✅ | No change | — |
| 2.10 | `_oauth_keys_storer` | Exists ✅ | No change | — |
| 2.11 | `_oauth_temporal_keys` | Exists ✅ | No change | — |
| 2.12 | `_oauth_delegated_codes` | Exists (sparse) | ⚠️ **DROP TABLE** + **CREATE** `_oauth_delegated_state` | 2 |
| 2.13 | `_oauth_audit_log` | **New** | **CREATE** | 2 |
