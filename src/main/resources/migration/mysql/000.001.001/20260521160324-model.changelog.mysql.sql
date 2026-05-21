-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE _audit_access_api_key_client (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_API_KEY_CLIENT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-2
CREATE TABLE _audit_access_consent_purpose (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_CONSENT_PURPOSE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-3
CREATE TABLE _audit_access_relying_party (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_RELYING_PARTY PRIMARY KEY (id));

-- changeset auto.generated:1825492372-4
CREATE TABLE _audit_access_role (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_ROLE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-5
CREATE TABLE _audit_access_tenant (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_TENANT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-6
CREATE TABLE _audit_access_tenant_config (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_TENANT_CONFIG PRIMARY KEY (id));

-- changeset auto.generated:1825492372-7
CREATE TABLE _audit_access_tenant_login_provider (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_TENANT_LOGIN_PROVIDER PRIMARY KEY (id));

-- changeset auto.generated:1825492372-8
CREATE TABLE _audit_access_tenant_terms_of_use (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_TENANT_TERMS_OF_USE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-9
CREATE TABLE _audit_access_trusted_client (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_TRUSTED_CLIENT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-10
CREATE TABLE _audit_access_user (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_USER PRIMARY KEY (id));

-- changeset auto.generated:1825492372-11
CREATE TABLE _audit_access_user_group_membership (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_USER_GROUP_MEMBERSHIP PRIMARY KEY (id));

-- changeset auto.generated:1825492372-12
CREATE TABLE _audit_access_user_personal_api_key (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_USER_PERSONAL_API_KEY PRIMARY KEY (id));

-- changeset auto.generated:1825492372-13
CREATE TABLE _audit_access_user_role_assignament (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_USER_ROLE_ASSIGNAMENT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-14
CREATE TABLE _audit_document_snippet (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_SNIPPET PRIMARY KEY (id));

-- changeset auto.generated:1825492372-15
CREATE TABLE _audit_document_snippet_asset (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_SNIPPET_ASSET PRIMARY KEY (id));

-- changeset auto.generated:1825492372-16
CREATE TABLE _audit_document_snippet_version (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_SNIPPET_VERSION PRIMARY KEY (id));

-- changeset auto.generated:1825492372-17
CREATE TABLE _audit_document_template (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_TEMPLATE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-18
CREATE TABLE _audit_document_template_asset (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_TEMPLATE_ASSET PRIMARY KEY (id));

-- changeset auto.generated:1825492372-19
CREATE TABLE _audit_document_template_variable (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_TEMPLATE_VARIABLE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-20
CREATE TABLE _audit_document_template_version (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_TEMPLATE_VERSION PRIMARY KEY (id));

-- changeset auto.generated:1825492372-21
CREATE TABLE _audit_document_theme (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_THEME PRIMARY KEY (id));

-- changeset auto.generated:1825492372-22
CREATE TABLE _audit_document_theme_asset (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_THEME_ASSET PRIMARY KEY (id));

-- changeset auto.generated:1825492372-23
CREATE TABLE _audit_document_theme_version (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_THEME_VERSION PRIMARY KEY (id));

-- changeset auto.generated:1825492372-24
CREATE TABLE _audit_notification_message (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_NOTIFICATION_MESSAGE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-25
CREATE TABLE _audit_notification_smtp_outbound_config (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_NOTIFICATION_SMTP_OUTBOUND_CONFIG PRIMARY KEY (id));

-- changeset auto.generated:1825492372-26
CREATE TABLE _filestorer (code VARCHAR(250) NOT NULL, temp SMALLINT DEFAULT 0 NOT NULL, name VARCHAR(250) NOT NULL, mime VARCHAR(250) NOT NULL, upload timestamp NOT NULL, bytes LONGBLOB NOT NULL, CONSTRAINT PK__FILESTORER PRIMARY KEY (code), UNIQUE (code));

-- changeset auto.generated:1825492372-27
CREATE TABLE _long_tasks (code VARCHAR(250) NOT NULL, actor VARCHAR(250) NOT NULL, creation timestamp NOT NULL, completion timestamp DEFAULT NULL NULL, expiration timestamp DEFAULT NULL NULL, progress LONGTEXT NOT NULL, CONSTRAINT PK__LONG_TASKS PRIMARY KEY (code), UNIQUE (code));

-- changeset auto.generated:1825492372-28
CREATE TABLE _oauth_audit_log (uid VARCHAR(36) NOT NULL, tenant_id VARCHAR(36) NOT NULL, event_type VARCHAR(64) NOT NULL, user_id VARCHAR(36) NULL, client_id VARCHAR(36) NULL, session_id VARCHAR(255) NULL, jti VARCHAR(36) NULL, grant_type VARCHAR(64) NULL, scope TEXT NULL, acr VARCHAR(8) NULL, ip_address VARCHAR(64) NULL, user_agent VARCHAR(512) NULL, failure_reason VARCHAR(64) NULL, payload_json MEDIUMTEXT NULL, created_at datetime NOT NULL, CONSTRAINT PK__OAUTH_AUDIT_LOG PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-29
CREATE TABLE _oauth_delegated_state (uid VARCHAR(36) NOT NULL, tenant_id VARCHAR(36) NOT NULL, state_token VARCHAR(128) NOT NULL, provider_id VARCHAR(36) NOT NULL, session_context_json MEDIUMTEXT NOT NULL, created_at datetime NOT NULL, expires_at datetime NOT NULL, CONSTRAINT PK__OAUTH_DELEGATED_STATE PRIMARY KEY (uid), UNIQUE (state_token));

-- changeset auto.generated:1825492372-30
CREATE TABLE _oauth_device_codes (device_code VARCHAR(36) NOT NULL, user_code VARCHAR(16) NOT NULL, tenant VARCHAR(64) NOT NULL, client_id VARCHAR(128) NOT NULL, scope TEXT NOT NULL, audiences LONGTEXT NOT NULL, status VARCHAR(16) NOT NULL, auth_data LONGTEXT NULL, requested_at datetime NOT NULL, expires_at datetime NOT NULL, last_poll_at datetime DEFAULT NULL NULL, interval_sec INT NOT NULL, CONSTRAINT PK__OAUTH_DEVICE_CODES PRIMARY KEY (device_code));

-- changeset auto.generated:1825492372-31
CREATE TABLE _oauth_keys_storer (expiration timestamp NOT NULL, since timestamp NOT NULL, keyid VARCHAR(255) NOT NULL, private TEXT NOT NULL, public TEXT NOT NULL, tenant VARCHAR(100) NOT NULL);

-- changeset auto.generated:1825492372-32
CREATE TABLE _oauth_magic_link (uid VARCHAR(36) NOT NULL, tenant_id VARCHAR(36) NOT NULL, user_uid VARCHAR(36) NOT NULL, client_id VARCHAR(36) NOT NULL, token_hash VARCHAR(64) NOT NULL, redirect_uri VARCHAR(500) NOT NULL, nonce VARCHAR(500) NULL, scope VARCHAR(500) DEFAULT 'openid email' NOT NULL, state VARCHAR(500) NULL, created_at datetime DEFAULT NOW() NOT NULL, expires_at datetime NOT NULL, used_at datetime DEFAULT NULL NULL, CONSTRAINT PK__OAUTH_MAGIC_LINK PRIMARY KEY (uid), UNIQUE (token_hash));

-- changeset auto.generated:1825492372-33
CREATE TABLE _oauth_par_request (request_uri VARCHAR(100) NOT NULL, tenant_id VARCHAR(36) NOT NULL, client_id VARCHAR(36) NOT NULL, params_json TEXT NOT NULL, created_at datetime DEFAULT NOW() NOT NULL, expires_at datetime NOT NULL, used_at datetime DEFAULT NULL NULL, CONSTRAINT PK__OAUTH_PAR_REQUEST PRIMARY KEY (request_uri));

-- changeset auto.generated:1825492372-34
CREATE TABLE _oauth_revoked_jti (jti VARCHAR(36) NOT NULL, tenant_id VARCHAR(36) NOT NULL, token_type ENUM('access', 'refresh') DEFAULT 'access' NOT NULL, revoked_at datetime DEFAULT NOW() NOT NULL, expires_at datetime NOT NULL, CONSTRAINT PK__OAUTH_REVOKED_JTI PRIMARY KEY (jti));

-- changeset auto.generated:1825492372-35
CREATE TABLE _oauth_session (session VARCHAR(255) NOT NULL, user_uid VARCHAR(36) NOT NULL, expiration timestamp DEFAULT NULL NULL, client_id VARCHAR(250) NOT NULL, issuer VARCHAR(255) NOT NULL, auth_data TEXT NOT NULL, csid TEXT NOT NULL, revoked_at timestamp DEFAULT NULL NULL, ip_address VARCHAR(45) NULL, user_agent VARCHAR(250) NULL, last_used_at datetime DEFAULT NULL NULL, client_name VARCHAR(200) NULL, auth_time datetime DEFAULT NULL NULL, acr TINYINT DEFAULT 0 NOT NULL, sso_clients_json MEDIUMTEXT NULL, CONSTRAINT PK__OAUTH_SESSION PRIMARY KEY (session));

-- changeset auto.generated:1825492372-36
CREATE TABLE _oauth_session_grant (id VARCHAR(36) NOT NULL, session VARCHAR(255) NOT NULL, client_id VARCHAR(250) NOT NULL, grant_type VARCHAR(50) NOT NULL, scope TEXT NULL, audiences TEXT NOT NULL, auth_data TEXT NOT NULL, created_at datetime NOT NULL, updated_at datetime NOT NULL, revoked_at datetime DEFAULT NULL NULL, CONSTRAINT PK__OAUTH_SESSION_GRANT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-37
CREATE TABLE _oauth_session_token (id VARCHAR(36) NOT NULL, session VARCHAR(255) NOT NULL, jti VARCHAR(36) NOT NULL, refresh_jti VARCHAR(36) NOT NULL, issued_at datetime NOT NULL, expires_at datetime NOT NULL, revoked_at datetime DEFAULT NULL NULL, grant_id VARCHAR(36) NULL, client_id VARCHAR(250) NULL, scope TEXT NULL, audiences TEXT NULL, auth_data TEXT NULL, CONSTRAINT PK__OAUTH_SESSION_TOKEN PRIMARY KEY (id), UNIQUE (jti), UNIQUE (refresh_jti));

-- changeset auto.generated:1825492372-38
CREATE TABLE _oauth_temporal_codes (code VARCHAR(255) NOT NULL, code_data TEXT NOT NULL, expiration timestamp NOT NULL, code_challenge VARCHAR(128) NULL, code_challenge_method VARCHAR(10) NULL);

-- changeset auto.generated:1825492372-39
CREATE TABLE _oauth_temporal_keys (current VARCHAR(255) NOT NULL, old VARCHAR(255) NOT NULL, expiration timestamp NOT NULL);

-- changeset auto.generated:1825492372-40
CREATE TABLE _oauth_webauthn_challenge (challenge_id VARCHAR(36) NOT NULL, tenant_id VARCHAR(36) NOT NULL, user_uid VARCHAR(36) NULL, challenge VARCHAR(100) NOT NULL, type ENUM('register', 'authenticate') NOT NULL, created_at datetime DEFAULT NOW() NOT NULL, expires_at datetime NOT NULL, CONSTRAINT PK__OAUTH_WEBAUTHN_CHALLENGE PRIMARY KEY (challenge_id));

-- changeset auto.generated:1825492372-41
CREATE TABLE _security_magic_links (token VARCHAR(255) NOT NULL, `path` TEXT NOT NULL, jwt_token TEXT NULL, actor_json TEXT NULL, source_json TEXT NULL, auth_json TEXT NULL, current_reads INT NOT NULL, max_reads INT NOT NULL, expiration timestamp NOT NULL, CONSTRAINT PK__SECURITY_MAGIC_LINKS PRIMARY KEY (token));

-- changeset auto.generated:1825492372-42
CREATE INDEX FK__OAUTH_SESSION_GRANT_SESSION ON _oauth_session_grant(session);

-- changeset auto.generated:1825492372-43
CREATE INDEX FK__OAUTH_SESSION_TOKEN_GRANT ON _oauth_session_token(grant_id);

-- changeset auto.generated:1825492372-44
CREATE INDEX idx_audit_entity_id ON _audit_access_api_key_client(entity_id);

-- changeset auto.generated:1825492372-45
CREATE INDEX idx_audit_entity_id ON _audit_access_consent_purpose(entity_id);

-- changeset auto.generated:1825492372-46
CREATE INDEX idx_audit_entity_id ON _audit_access_relying_party(entity_id);

-- changeset auto.generated:1825492372-47
CREATE INDEX idx_audit_entity_id ON _audit_access_role(entity_id);

-- changeset auto.generated:1825492372-48
CREATE INDEX idx_audit_entity_id ON _audit_access_tenant(entity_id);

-- changeset auto.generated:1825492372-49
CREATE INDEX idx_audit_entity_id ON _audit_access_tenant_config(entity_id);

-- changeset auto.generated:1825492372-50
CREATE INDEX idx_audit_entity_id ON _audit_access_tenant_login_provider(entity_id);

-- changeset auto.generated:1825492372-51
CREATE INDEX idx_audit_entity_id ON _audit_access_tenant_terms_of_use(entity_id);

-- changeset auto.generated:1825492372-52
CREATE INDEX idx_audit_entity_id ON _audit_access_trusted_client(entity_id);

-- changeset auto.generated:1825492372-53
CREATE INDEX idx_audit_entity_id ON _audit_access_user(entity_id);

-- changeset auto.generated:1825492372-54
CREATE INDEX idx_audit_entity_id ON _audit_access_user_group_membership(entity_id);

-- changeset auto.generated:1825492372-55
CREATE INDEX idx_audit_entity_id ON _audit_access_user_personal_api_key(entity_id);

-- changeset auto.generated:1825492372-56
CREATE INDEX idx_audit_entity_id ON _audit_access_user_role_assignament(entity_id);

-- changeset auto.generated:1825492372-57
CREATE INDEX idx_audit_entity_id ON _audit_document_snippet(entity_id);

-- changeset auto.generated:1825492372-58
CREATE INDEX idx_audit_entity_id ON _audit_document_snippet_asset(entity_id);

-- changeset auto.generated:1825492372-59
CREATE INDEX idx_audit_entity_id ON _audit_document_snippet_version(entity_id);

-- changeset auto.generated:1825492372-60
CREATE INDEX idx_audit_entity_id ON _audit_document_template(entity_id);

-- changeset auto.generated:1825492372-61
CREATE INDEX idx_audit_entity_id ON _audit_document_template_asset(entity_id);

-- changeset auto.generated:1825492372-62
CREATE INDEX idx_audit_entity_id ON _audit_document_template_variable(entity_id);

-- changeset auto.generated:1825492372-63
CREATE INDEX idx_audit_entity_id ON _audit_document_template_version(entity_id);

-- changeset auto.generated:1825492372-64
CREATE INDEX idx_audit_entity_id ON _audit_document_theme(entity_id);

-- changeset auto.generated:1825492372-65
CREATE INDEX idx_audit_entity_id ON _audit_document_theme_asset(entity_id);

-- changeset auto.generated:1825492372-66
CREATE INDEX idx_audit_entity_id ON _audit_document_theme_version(entity_id);

-- changeset auto.generated:1825492372-67
CREATE INDEX idx_audit_entity_id ON _audit_notification_message(entity_id);

-- changeset auto.generated:1825492372-68
CREATE INDEX idx_audit_entity_id ON _audit_notification_smtp_outbound_config(entity_id);

-- changeset auto.generated:1825492372-69
CREATE INDEX idx_audit_timestamp ON _audit_access_api_key_client(timestamp DESC);

-- changeset auto.generated:1825492372-70
CREATE INDEX idx_audit_timestamp ON _audit_access_consent_purpose(timestamp DESC);

-- changeset auto.generated:1825492372-71
CREATE INDEX idx_audit_timestamp ON _audit_access_relying_party(timestamp DESC);

-- changeset auto.generated:1825492372-72
CREATE INDEX idx_audit_timestamp ON _audit_access_role(timestamp DESC);

-- changeset auto.generated:1825492372-73
CREATE INDEX idx_audit_timestamp ON _audit_access_tenant(timestamp DESC);

-- changeset auto.generated:1825492372-74
CREATE INDEX idx_audit_timestamp ON _audit_access_tenant_config(timestamp DESC);

-- changeset auto.generated:1825492372-75
CREATE INDEX idx_audit_timestamp ON _audit_access_tenant_login_provider(timestamp DESC);

-- changeset auto.generated:1825492372-76
CREATE INDEX idx_audit_timestamp ON _audit_access_tenant_terms_of_use(timestamp DESC);

-- changeset auto.generated:1825492372-77
CREATE INDEX idx_audit_timestamp ON _audit_access_trusted_client(timestamp DESC);

-- changeset auto.generated:1825492372-78
CREATE INDEX idx_audit_timestamp ON _audit_access_user(timestamp DESC);

-- changeset auto.generated:1825492372-79
CREATE INDEX idx_audit_timestamp ON _audit_access_user_group_membership(timestamp DESC);

-- changeset auto.generated:1825492372-80
CREATE INDEX idx_audit_timestamp ON _audit_access_user_personal_api_key(timestamp DESC);

-- changeset auto.generated:1825492372-81
CREATE INDEX idx_audit_timestamp ON _audit_access_user_role_assignament(timestamp DESC);

-- changeset auto.generated:1825492372-82
CREATE INDEX idx_audit_timestamp ON _audit_document_snippet(timestamp DESC);

-- changeset auto.generated:1825492372-83
CREATE INDEX idx_audit_timestamp ON _audit_document_snippet_asset(timestamp DESC);

-- changeset auto.generated:1825492372-84
CREATE INDEX idx_audit_timestamp ON _audit_document_snippet_version(timestamp DESC);

-- changeset auto.generated:1825492372-85
CREATE INDEX idx_audit_timestamp ON _audit_document_template(timestamp DESC);

-- changeset auto.generated:1825492372-86
CREATE INDEX idx_audit_timestamp ON _audit_document_template_asset(timestamp DESC);

-- changeset auto.generated:1825492372-87
CREATE INDEX idx_audit_timestamp ON _audit_document_template_variable(timestamp DESC);

-- changeset auto.generated:1825492372-88
CREATE INDEX idx_audit_timestamp ON _audit_document_template_version(timestamp DESC);

-- changeset auto.generated:1825492372-89
CREATE INDEX idx_audit_timestamp ON _audit_document_theme(timestamp DESC);

-- changeset auto.generated:1825492372-90
CREATE INDEX idx_audit_timestamp ON _audit_document_theme_asset(timestamp DESC);

-- changeset auto.generated:1825492372-91
CREATE INDEX idx_audit_timestamp ON _audit_document_theme_version(timestamp DESC);

-- changeset auto.generated:1825492372-92
CREATE INDEX idx_audit_timestamp ON _audit_notification_message(timestamp DESC);

-- changeset auto.generated:1825492372-93
CREATE INDEX idx_audit_timestamp ON _audit_notification_smtp_outbound_config(timestamp DESC);

-- changeset auto.generated:1825492372-94
CREATE INDEX idx_audit_user ON _audit_access_api_key_client(performed_by);

-- changeset auto.generated:1825492372-95
CREATE INDEX idx_audit_user ON _audit_access_consent_purpose(performed_by);

-- changeset auto.generated:1825492372-96
CREATE INDEX idx_audit_user ON _audit_access_relying_party(performed_by);

-- changeset auto.generated:1825492372-97
CREATE INDEX idx_audit_user ON _audit_access_role(performed_by);

-- changeset auto.generated:1825492372-98
CREATE INDEX idx_audit_user ON _audit_access_tenant(performed_by);

-- changeset auto.generated:1825492372-99
CREATE INDEX idx_audit_user ON _audit_access_tenant_config(performed_by);

-- changeset auto.generated:1825492372-100
CREATE INDEX idx_audit_user ON _audit_access_tenant_login_provider(performed_by);

-- changeset auto.generated:1825492372-101
CREATE INDEX idx_audit_user ON _audit_access_tenant_terms_of_use(performed_by);

-- changeset auto.generated:1825492372-102
CREATE INDEX idx_audit_user ON _audit_access_trusted_client(performed_by);

-- changeset auto.generated:1825492372-103
CREATE INDEX idx_audit_user ON _audit_access_user(performed_by);

-- changeset auto.generated:1825492372-104
CREATE INDEX idx_audit_user ON _audit_access_user_group_membership(performed_by);

-- changeset auto.generated:1825492372-105
CREATE INDEX idx_audit_user ON _audit_access_user_personal_api_key(performed_by);

-- changeset auto.generated:1825492372-106
CREATE INDEX idx_audit_user ON _audit_access_user_role_assignament(performed_by);

-- changeset auto.generated:1825492372-107
CREATE INDEX idx_audit_user ON _audit_document_snippet(performed_by);

-- changeset auto.generated:1825492372-108
CREATE INDEX idx_audit_user ON _audit_document_snippet_asset(performed_by);

-- changeset auto.generated:1825492372-109
CREATE INDEX idx_audit_user ON _audit_document_snippet_version(performed_by);

-- changeset auto.generated:1825492372-110
CREATE INDEX idx_audit_user ON _audit_document_template(performed_by);

-- changeset auto.generated:1825492372-111
CREATE INDEX idx_audit_user ON _audit_document_template_asset(performed_by);

-- changeset auto.generated:1825492372-112
CREATE INDEX idx_audit_user ON _audit_document_template_variable(performed_by);

-- changeset auto.generated:1825492372-113
CREATE INDEX idx_audit_user ON _audit_document_template_version(performed_by);

-- changeset auto.generated:1825492372-114
CREATE INDEX idx_audit_user ON _audit_document_theme(performed_by);

-- changeset auto.generated:1825492372-115
CREATE INDEX idx_audit_user ON _audit_document_theme_asset(performed_by);

-- changeset auto.generated:1825492372-116
CREATE INDEX idx_audit_user ON _audit_document_theme_version(performed_by);

-- changeset auto.generated:1825492372-117
CREATE INDEX idx_audit_user ON _audit_notification_message(performed_by);

-- changeset auto.generated:1825492372-118
CREATE INDEX idx_audit_user ON _audit_notification_smtp_outbound_config(performed_by);

-- changeset auto.generated:1825492372-119
CREATE INDEX idx_challenge_expires ON _oauth_webauthn_challenge(expires_at);

-- changeset auto.generated:1825492372-120
CREATE INDEX idx_device_client ON _oauth_device_codes(client_id);

-- changeset auto.generated:1825492372-121
CREATE INDEX idx_device_expires ON _oauth_device_codes(expires_at);

-- changeset auto.generated:1825492372-122
CREATE INDEX idx_device_status ON _oauth_device_codes(status);

-- changeset auto.generated:1825492372-123
CREATE INDEX idx_long_tasks_code_actor ON _long_tasks(code, actor);

-- changeset auto.generated:1825492372-124
CREATE INDEX idx_magic_link_expires ON _oauth_magic_link(expires_at);

-- changeset auto.generated:1825492372-125
CREATE INDEX idx_oauth_audit_created ON _oauth_audit_log(created_at);

-- changeset auto.generated:1825492372-126
CREATE INDEX idx_oauth_audit_session ON _oauth_audit_log(session_id);

-- changeset auto.generated:1825492372-127
CREATE INDEX idx_oauth_audit_type ON _oauth_audit_log(event_type, tenant_id, created_at);

-- changeset auto.generated:1825492372-128
CREATE INDEX idx_oauth_audit_user ON _oauth_audit_log(user_id, tenant_id, created_at);

-- changeset auto.generated:1825492372-129
CREATE INDEX idx_oauth_delegated_state_expires ON _oauth_delegated_state(expires_at);

-- changeset auto.generated:1825492372-130
CREATE INDEX idx_oauth_session_expiration ON _oauth_session(expiration);

-- changeset auto.generated:1825492372-131
CREATE INDEX idx_oauth_session_expiration ON _oauth_temporal_codes(expiration);

-- changeset auto.generated:1825492372-132
CREATE INDEX idx_oauth_session_user_uid ON _oauth_magic_link(user_uid);

-- changeset auto.generated:1825492372-133
CREATE INDEX idx_par_expires ON _oauth_par_request(expires_at);

-- changeset auto.generated:1825492372-134
CREATE INDEX idx_par_tenant ON _oauth_par_request(tenant_id);

-- changeset auto.generated:1825492372-135
CREATE INDEX idx_revoked_expires ON _oauth_revoked_jti(expires_at);

-- changeset auto.generated:1825492372-136
CREATE INDEX idx_revoked_tenant ON _oauth_revoked_jti(tenant_id);

-- changeset auto.generated:1825492372-137
CREATE INDEX idx_session_token_exp ON _oauth_session_token(expires_at);

-- changeset auto.generated:1825492372-138
CREATE INDEX idx_session_token_session ON _oauth_session_token(session);

-- changeset auto.generated:1825492372-139
CREATE UNIQUE INDEX uq_device_user_code ON _oauth_device_codes(tenant, user_code);

-- changeset auto.generated:1825492372-140
ALTER TABLE _oauth_session_grant ADD CONSTRAINT FK__OAUTH_SESSION_GRANT_SESSION FOREIGN KEY (session) REFERENCES _oauth_session (session) ON UPDATE CASCADE ON DELETE CASCADE;

-- changeset auto.generated:1825492372-141
ALTER TABLE _oauth_session_token ADD CONSTRAINT FK__OAUTH_SESSION_TOKEN_GRANT FOREIGN KEY (grant_id) REFERENCES _oauth_session_grant (id) ON UPDATE RESTRICT ON DELETE SET NULL;

-- changeset auto.generated:1825492372-142
ALTER TABLE _oauth_session_token ADD CONSTRAINT FK__OAUTH_SESSION_TOKEN_SESSION FOREIGN KEY (session) REFERENCES _oauth_session (session) ON UPDATE CASCADE ON DELETE CASCADE;

