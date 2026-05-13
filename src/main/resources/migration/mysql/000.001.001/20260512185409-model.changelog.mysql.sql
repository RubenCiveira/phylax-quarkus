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
CREATE TABLE _audit_access_user_role_assignament (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_USER_ROLE_ASSIGNAMENT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-13
CREATE TABLE _audit_document_snippet (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_SNIPPET PRIMARY KEY (id));

-- changeset auto.generated:1825492372-14
CREATE TABLE _audit_document_snippet_asset (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_SNIPPET_ASSET PRIMARY KEY (id));

-- changeset auto.generated:1825492372-15
CREATE TABLE _audit_document_snippet_version (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_SNIPPET_VERSION PRIMARY KEY (id));

-- changeset auto.generated:1825492372-16
CREATE TABLE _audit_document_template (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_TEMPLATE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-17
CREATE TABLE _audit_document_template_asset (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_TEMPLATE_ASSET PRIMARY KEY (id));

-- changeset auto.generated:1825492372-18
CREATE TABLE _audit_document_template_variable (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_TEMPLATE_VARIABLE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-19
CREATE TABLE _audit_document_template_version (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_TEMPLATE_VERSION PRIMARY KEY (id));

-- changeset auto.generated:1825492372-20
CREATE TABLE _audit_document_theme (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_THEME PRIMARY KEY (id));

-- changeset auto.generated:1825492372-21
CREATE TABLE _audit_document_theme_asset (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_THEME_ASSET PRIMARY KEY (id));

-- changeset auto.generated:1825492372-22
CREATE TABLE _audit_document_theme_version (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_DOCUMENT_THEME_VERSION PRIMARY KEY (id));

-- changeset auto.generated:1825492372-23
CREATE TABLE _audit_notification_message (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_NOTIFICATION_MESSAGE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-24
CREATE TABLE _audit_notification_smtp_outbound_config (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_NOTIFICATION_SMTP_OUTBOUND_CONFIG PRIMARY KEY (id));

-- changeset auto.generated:1825492372-25
CREATE TABLE _filestorer (code VARCHAR(250) NOT NULL, temp SMALLINT DEFAULT 0 NOT NULL, name VARCHAR(250) NOT NULL, mime VARCHAR(250) NOT NULL, upload timestamp NOT NULL, bytes LONGBLOB NOT NULL, CONSTRAINT PK__FILESTORER PRIMARY KEY (code), UNIQUE (code));

-- changeset auto.generated:1825492372-26
CREATE TABLE _long_tasks (code VARCHAR(250) NOT NULL, actor VARCHAR(250) NOT NULL, creation timestamp NOT NULL, completion timestamp DEFAULT NULL NULL, expiration timestamp DEFAULT NULL NULL, progress LONGTEXT NOT NULL, CONSTRAINT PK__LONG_TASKS PRIMARY KEY (code), UNIQUE (code));

-- changeset auto.generated:1825492372-27
CREATE TABLE _oauth_device_codes (device_code VARCHAR(36) NOT NULL, user_code VARCHAR(16) NOT NULL, tenant VARCHAR(64) NOT NULL, client_id VARCHAR(128) NOT NULL, scope TEXT NOT NULL, audiences LONGTEXT NOT NULL, status VARCHAR(16) NOT NULL, auth_data LONGTEXT NULL, requested_at datetime NOT NULL, expires_at datetime NOT NULL, last_poll_at datetime DEFAULT NULL NULL, interval_sec INT NOT NULL, CONSTRAINT PK__OAUTH_DEVICE_CODES PRIMARY KEY (device_code));

-- changeset auto.generated:1825492372-28
CREATE TABLE _oauth_keys_storer (expiration timestamp NOT NULL, since timestamp NOT NULL, keyid VARCHAR(255) NOT NULL, private TEXT NOT NULL, public TEXT NOT NULL, tenant VARCHAR(100) NOT NULL);

-- changeset auto.generated:1825492372-29
CREATE TABLE _oauth_magic_link (uid VARCHAR(36) NOT NULL, tenant_id VARCHAR(36) NOT NULL, user_uid VARCHAR(36) NOT NULL, client_id VARCHAR(36) NOT NULL, token_hash VARCHAR(64) NOT NULL, redirect_uri VARCHAR(500) NOT NULL, nonce VARCHAR(500) NULL, scope VARCHAR(500) DEFAULT 'openid email' NOT NULL, state VARCHAR(500) NULL, created_at datetime DEFAULT NOW() NOT NULL, expires_at datetime NOT NULL, used_at datetime DEFAULT NULL NULL, CONSTRAINT PK__OAUTH_MAGIC_LINK PRIMARY KEY (uid), UNIQUE (token_hash));

-- changeset auto.generated:1825492372-30
CREATE TABLE _oauth_par_request (request_uri VARCHAR(100) NOT NULL, tenant_id VARCHAR(36) NOT NULL, client_id VARCHAR(36) NOT NULL, params_json TEXT NOT NULL, created_at datetime DEFAULT NOW() NOT NULL, expires_at datetime NOT NULL, used_at datetime DEFAULT NULL NULL, CONSTRAINT PK__OAUTH_PAR_REQUEST PRIMARY KEY (request_uri));

-- changeset auto.generated:1825492372-31
CREATE TABLE _oauth_revoked_jti (jti VARCHAR(36) NOT NULL, tenant_id VARCHAR(36) NOT NULL, token_type ENUM('access', 'refresh') DEFAULT 'access' NOT NULL, revoked_at datetime DEFAULT NOW() NOT NULL, expires_at datetime NOT NULL, CONSTRAINT PK__OAUTH_REVOKED_JTI PRIMARY KEY (jti));

-- changeset auto.generated:1825492372-32
CREATE TABLE _oauth_session (session VARCHAR(255) NOT NULL, expiration timestamp DEFAULT NULL NULL, client_id VARCHAR(250) NOT NULL, issuer VARCHAR(255) NOT NULL, auth_data TEXT NOT NULL, csid TEXT NOT NULL, jti VARCHAR(36) NULL, refresh_jti VARCHAR(36) NULL, revoked_at timestamp DEFAULT NULL NULL, ip_address VARCHAR(45) NULL, user_agent VARCHAR(250) NULL, last_used_at datetime DEFAULT NULL NULL, client_name VARCHAR(200) NULL);

-- changeset manual:20260513083000-session-token-table
CREATE TABLE _oauth_session_token (id VARCHAR(36) NOT NULL, session VARCHAR(255) NOT NULL, jti VARCHAR(36) NOT NULL, refresh_jti VARCHAR(36) NOT NULL, issued_at datetime NOT NULL, expires_at datetime NOT NULL, revoked_at datetime DEFAULT NULL NULL, CONSTRAINT PK__OAUTH_SESSION_TOKEN PRIMARY KEY (id), UNIQUE (jti), UNIQUE (refresh_jti));

-- changeset auto.generated:1825492372-33
CREATE TABLE _oauth_temporal_codes (code VARCHAR(255) NOT NULL, code_data TEXT NOT NULL, expiration timestamp NOT NULL, code_challenge VARCHAR(128) NULL, code_challenge_method VARCHAR(10) NULL);

-- changeset auto.generated:1825492372-34
CREATE TABLE _oauth_temporal_keys (current VARCHAR(255) NOT NULL, old VARCHAR(255) NOT NULL, expiration timestamp NOT NULL);

-- changeset auto.generated:1825492372-35
CREATE TABLE _oauth_webauthn_challenge (challenge_id VARCHAR(36) NOT NULL, tenant_id VARCHAR(36) NOT NULL, user_uid VARCHAR(36) NULL, challenge VARCHAR(100) NOT NULL, type ENUM('register', 'authenticate') NOT NULL, created_at datetime DEFAULT NOW() NOT NULL, expires_at datetime NOT NULL, CONSTRAINT PK__OAUTH_WEBAUTHN_CHALLENGE PRIMARY KEY (challenge_id));

-- changeset auto.generated:1825492372-36
CREATE TABLE _security_magic_links (token VARCHAR(255) NOT NULL, `path` TEXT NOT NULL, jwt_token TEXT NULL, actor_json TEXT NULL, source_json TEXT NULL, current_reads INT NOT NULL, max_reads INT NOT NULL, expiration timestamp NOT NULL, CONSTRAINT PK__SECURITY_MAGIC_LINKS PRIMARY KEY (token));

-- changeset auto.generated:1825492372-37
CREATE TABLE access_api_key_client (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT NOT NULL, `key` VARCHAR(255) NULL, scopes VARCHAR(255) NULL, CONSTRAINT PK_ACCESS_API_KEY_CLIENT PRIMARY KEY (uid), UNIQUE (code));

-- changeset auto.generated:1825492372-38
CREATE TABLE access_consent_purpose (uid VARCHAR(255) NOT NULL, version INT NOT NULL, activation_date timestamp NOT NULL, `description` LONGTEXT NOT NULL, `key` VARCHAR(255) NOT NULL, required BIT NOT NULL, title VARCHAR(255) NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_CONSENT_PURPOSE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-39
CREATE TABLE access_relying_party (uid VARCHAR(255) NOT NULL, version INT NOT NULL, api_key VARCHAR(255) NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, CONSTRAINT PK_ACCESS_RELYING_PARTY PRIMARY KEY (uid), UNIQUE (api_key), UNIQUE (code));

-- changeset auto.generated:1825492372-40
CREATE TABLE access_role (uid VARCHAR(255) NOT NULL, version INT NOT NULL, name VARCHAR(255) NOT NULL, relying_party VARCHAR(255) NULL, CONSTRAINT PK_ACCESS_ROLE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-41
CREATE TABLE access_tenant (uid VARCHAR(255) NOT NULL, version INT NOT NULL, domain VARCHAR(255) NOT NULL, enabled BIT NOT NULL, mark_for_delete BIT NOT NULL, mark_for_delete_time timestamp DEFAULT NULL NULL, name VARCHAR(255) NOT NULL, root BIT DEFAULT 0 NULL, CONSTRAINT PK_ACCESS_TENANT PRIMARY KEY (uid), UNIQUE (domain), UNIQUE (name));

-- changeset auto.generated:1825492372-42
CREATE TABLE access_tenant_config (uid VARCHAR(255) NOT NULL, version INT NOT NULL, allow_recover_pass BIT DEFAULT 0 NULL, allow_register BIT DEFAULT 0 NULL, disabled_user_email LONGTEXT NULL, dynamic_registration_policy VARCHAR(255) NULL, enable_register_users BIT DEFAULT 0 NULL, enabled_user_email LONGTEXT NULL, force_mfa BIT NOT NULL, inner_label VARCHAR(255) NULL, magic_link_enabled BIT DEFAULT 0 NULL, recover_pass_email LONGTEXT NULL, registerd_email LONGTEXT NULL, webauthn_enabled BIT DEFAULT 0 NULL, webauthn_rp_id VARCHAR(255) NULL, webauthn_rp_name VARCHAR(255) NULL, wellcome_email LONGTEXT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_TENANT_CONFIG PRIMARY KEY (uid), UNIQUE (tenant));

-- changeset auto.generated:1825492372-43
CREATE TABLE access_tenant_login_provider (uid VARCHAR(255) NOT NULL, version INT NOT NULL, certificate LONGTEXT NULL, direct_access BIT DEFAULT 0 NULL, disabled BIT DEFAULT 0 NULL, metadata VARCHAR(255) NULL, name VARCHAR(255) NOT NULL, private_key VARCHAR(255) NULL, public_key VARCHAR(255) NULL, saml_idp_entity_id VARCHAR(255) NULL, saml_idp_idp_cert VARCHAR(255) NULL, saml_idp_metadata_url VARCHAR(255) NULL, saml_idp_sso_url VARCHAR(255) NULL, source VARCHAR(255) NOT NULL, users_enabled_by_default BIT NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_TENANT_LOGIN_PROVIDER PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-44
CREATE TABLE access_tenant_terms_of_use (uid VARCHAR(255) NOT NULL, version INT NOT NULL, activation_date timestamp DEFAULT NULL NULL, attached VARCHAR(255) NULL, enabled BIT NOT NULL, text LONGTEXT NOT NULL, relying_party VARCHAR(255) NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_TENANT_TERMS_OF_USE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-45
CREATE TABLE access_trusted_client (uid VARCHAR(255) NOT NULL, version INT NOT NULL, allow_all_scopes BIT DEFAULT 0 NULL, allowed_scopes_m_2m LONGTEXT NULL, back_channel_logout_session_required BIT DEFAULT 0 NULL, back_channel_logout_uri VARCHAR(255) NULL, client_name VARCHAR(255) NULL, client_uri VARCHAR(255) NULL, code VARCHAR(255) NOT NULL, dynamically_registered BIT DEFAULT 0 NULL, enabled BIT NOT NULL, front_channel_logout_session_required BIT DEFAULT 0 NULL, front_channel_logout_uri VARCHAR(255) NULL, grant_types_json LONGTEXT NULL, jwks_json LONGTEXT NULL, jwks_uri LONGTEXT NULL, logo_uri VARCHAR(255) NULL, m_2m_token_ttl_seconds INT NOT NULL, policy_uri VARCHAR(255) NULL, public_allow BIT NOT NULL, registered_at timestamp DEFAULT NULL NULL, registration_access VARCHAR(255) NULL, request_object_signing_alg VARCHAR(255) NULL, response_types_json LONGTEXT NULL, secret_oauth VARCHAR(255) NULL, token_endpoint_auth_method VARCHAR(255) NOT NULL, tos_uri VARCHAR(255) NULL, CONSTRAINT PK_ACCESS_TRUSTED_CLIENT PRIMARY KEY (uid), UNIQUE (code));

-- changeset auto.generated:1825492372-46
CREATE TABLE access_trusted_client_allowed_redirect (uid VARCHAR(255) NOT NULL, version INT NOT NULL, url VARCHAR(255) NOT NULL, client VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_TRUSTED_CLIENT_ALLOWED_REDIRECT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-47
CREATE TABLE access_user (uid VARCHAR(255) NOT NULL, version INT NOT NULL, approve VARCHAR(255) NULL, blocked_until timestamp DEFAULT NULL NULL, email VARCHAR(255) NULL, enabled BIT DEFAULT 0 NULL, name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, provider VARCHAR(255) NULL, second_factor_seed VARCHAR(255) NULL, temporal_password BIT DEFAULT 0 NULL, use_second_factors BIT DEFAULT 0 NULL, wellcome_at timestamp DEFAULT NULL NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-48
CREATE TABLE access_user_accepted_termns_of_use (uid VARCHAR(255) NOT NULL, version INT NOT NULL, accept_date timestamp DEFAULT NULL NULL, ip_address VARCHAR(255) NULL, user_agent VARCHAR(255) NULL, conditions VARCHAR(255) NOT NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_ACCEPTED_TERMNS_OF_USE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-49
CREATE TABLE access_user_access_temporal_code (uid VARCHAR(255) NOT NULL, version INT NOT NULL, failed_login_attempts INT DEFAULT NULL NULL, recovery_code VARCHAR(255) NULL, recovery_code_expiration timestamp DEFAULT NULL NULL, register_code VARCHAR(255) NULL, register_code_expiration timestamp DEFAULT NULL NULL, register_code_url LONGTEXT NULL, temp_second_factor_seed VARCHAR(255) NULL, temp_second_factor_seed_expiration timestamp DEFAULT NULL NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_ACCESS_TEMPORAL_CODE PRIMARY KEY (uid), UNIQUE (recovery_code), UNIQUE (register_code), UNIQUE (user));

-- changeset auto.generated:1825492372-50
CREATE TABLE access_user_consent_purposes (uid VARCHAR(255) NOT NULL, version INT NOT NULL, decision_at timestamp DEFAULT NULL NULL, granted BIT NOT NULL, ip_address VARCHAR(255) NULL, user_agent VARCHAR(255) NULL, consent_purpose VARCHAR(255) NOT NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_CONSENT_PURPOSES PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-51
CREATE TABLE access_user_consented_scopes (uid VARCHAR(255) NOT NULL, version INT NOT NULL, decision_at timestamp DEFAULT NULL NULL, granted BIT DEFAULT 0 NULL, ip_address VARCHAR(255) NULL, scope VARCHAR(255) NULL, user_agent VARCHAR(255) NULL, trusted_client VARCHAR(255) NOT NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_CONSENTED_SCOPES PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-52
CREATE TABLE access_user_group_membership (uid VARCHAR(255) NOT NULL, version INT NOT NULL, `groups` LONGTEXT NULL, relying_party VARCHAR(255) NULL, trusted_client VARCHAR(255) NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_GROUP_MEMBERSHIP PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-53
CREATE TABLE access_user_invitation (uid VARCHAR(255) NOT NULL, version INT NOT NULL, accepted_at timestamp DEFAULT NULL NULL, created_at timestamp NOT NULL, email VARCHAR(255) NOT NULL, expired_at timestamp NOT NULL, metadata_json LONGTEXT NULL, roles VARCHAR(255) NULL, status VARCHAR(255) NULL, token_hash VARCHAR(255) NOT NULL, accepted_by VARCHAR(255) NULL, invited_by VARCHAR(255) NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_INVITATION PRIMARY KEY (uid), UNIQUE (token_hash));

-- changeset auto.generated:1825492372-54
CREATE TABLE access_user_profile (uid VARCHAR(255) NOT NULL, version INT NOT NULL, address_json LONGTEXT NULL, birthdate VARCHAR(255) NULL, family_name VARCHAR(255) NULL, gender VARCHAR(255) NULL, given_name VARCHAR(255) NULL, locale VARCHAR(255) NULL, middle_name VARCHAR(255) NULL, nickname VARCHAR(255) NULL, phone_number VARCHAR(255) NULL, phone_number_verified BIT DEFAULT 0 NULL, picture_url VARCHAR(255) NULL, preferred_username VARCHAR(255) NULL, updated_at VARCHAR(255) NULL, website_url VARCHAR(255) NULL, zoneinfo VARCHAR(255) NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_PROFILE PRIMARY KEY (uid), UNIQUE (user));

-- changeset auto.generated:1825492372-55
CREATE TABLE access_user_role_assignament (uid VARCHAR(255) NOT NULL, version INT NOT NULL, relying_party VARCHAR(255) NULL, trusted_client VARCHAR(255) NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_ROLE_ASSIGNAMENT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-56
CREATE TABLE access_user_role_assignament_role (uid VARCHAR(255) NOT NULL, version INT NOT NULL, `role` VARCHAR(255) NOT NULL, user_role_assignament VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_ROLE_ASSIGNAMENT_ROLE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-57
CREATE TABLE access_user_webauthn_credential (uid VARCHAR(255) NOT NULL, version INT NOT NULL, aaguid VARCHAR(255) NULL, autenticator VARCHAR(255) NOT NULL, created_at timestamp DEFAULT NULL NULL, device_name VARCHAR(255) NULL, enabled BIT DEFAULT 0 NULL, last_used_at timestamp DEFAULT NULL NULL, name VARCHAR(255) NOT NULL, public_key LONGTEXT NOT NULL, sign_count INT NOT NULL, transports_json LONGTEXT NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_WEBAUTHN_CREDENTIAL PRIMARY KEY (uid), UNIQUE (autenticator));

-- changeset auto.generated:1825492372-58
CREATE TABLE document_snippet (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_DOCUMENT_SNIPPET PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-59
CREATE TABLE document_snippet_asset (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, content VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, type VARCHAR(255) NOT NULL, snippet VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_SNIPPET_ASSET PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-60
CREATE TABLE document_snippet_version (uid VARCHAR(255) NOT NULL, version INT NOT NULL, content_html LONGTEXT NOT NULL, content_text LONGTEXT NULL, locale VARCHAR(255) NULL, subject VARCHAR(255) NULL, snippet VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_SNIPPET_VERSION PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-61
CREATE TABLE document_template (uid VARCHAR(255) NOT NULL, version INT NOT NULL, channel VARCHAR(255) NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, theme VARCHAR(255) NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-62
CREATE TABLE document_template_asset (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, content VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, type VARCHAR(255) NOT NULL, template VARCHAR(255) NOT NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_ASSET PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-63
CREATE TABLE document_template_variable (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, type VARCHAR(255) NOT NULL, value VARCHAR(255) NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_VARIABLE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-64
CREATE TABLE document_template_version (uid VARCHAR(255) NOT NULL, version INT NOT NULL, content_html LONGTEXT NOT NULL, content_text LONGTEXT NULL, locale VARCHAR(255) NULL, subject VARCHAR(255) NULL, template VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_VERSION PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-65
CREATE TABLE document_theme (uid VARCHAR(255) NOT NULL, version INT NOT NULL, enabled BIT DEFAULT 0 NULL, is_default BIT DEFAULT 0 NULL, name VARCHAR(255) NOT NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_DOCUMENT_THEME PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-66
CREATE TABLE document_theme_asset (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, content VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, type VARCHAR(255) NOT NULL, theme VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_THEME_ASSET PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-67
CREATE TABLE document_theme_version (uid VARCHAR(255) NOT NULL, version INT NOT NULL, channel VARCHAR(255) NOT NULL, content_html LONGTEXT NOT NULL, content_text LONGTEXT NULL, locale VARCHAR(255) NULL, theme VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_THEME_VERSION PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-68
CREATE TABLE notification_message (uid VARCHAR(255) NOT NULL, version INT NOT NULL, content LONGTEXT NOT NULL, created_at timestamp NOT NULL, lock_at timestamp DEFAULT NULL NULL, retries INT NOT NULL, send_at timestamp DEFAULT NULL NULL, target VARCHAR(255) NOT NULL, urgent BIT NOT NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_NOTIFICATION_MESSAGE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-69
CREATE TABLE notification_smtp_outbound_config (uid VARCHAR(255) NOT NULL, version INT NOT NULL, host VARCHAR(255) NOT NULL, login VARCHAR(255) NOT NULL, max_retries INT NOT NULL, password VARCHAR(255) NOT NULL, port INT NOT NULL, rate_limit INT NOT NULL, retry_delay INT NOT NULL, sender_email VARCHAR(255) NOT NULL, sender_name VARCHAR(255) NULL, timeout INT NOT NULL, use_tls BIT NOT NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_NOTIFICATION_SMTP_OUTBOUND_CONFIG PRIMARY KEY (uid), UNIQUE (tenant));

-- changeset auto.generated:1825492372-70
CREATE INDEX FL_CONSENT_PURPOSE_TENANT ON access_consent_purpose(tenant);

-- changeset auto.generated:1825492372-71
CREATE INDEX FL_MESSAGE_TENANT ON notification_message(tenant);

-- changeset auto.generated:1825492372-72
CREATE INDEX FL_ROLE_NAME ON access_role(name);

-- changeset auto.generated:1825492372-73
CREATE INDEX FL_ROLE_RELYING_PARTY ON access_role(relying_party);

-- changeset auto.generated:1825492372-74
CREATE INDEX FL_SNIPPET_ASSET_SNIPPETS ON document_snippet_asset(snippet);

-- changeset auto.generated:1825492372-75
CREATE INDEX FL_SNIPPET_TENANT ON document_snippet(tenant);

-- changeset auto.generated:1825492372-76
CREATE INDEX FL_SNIPPET_VERSION_SNIPPET ON document_snippet_version(snippet);

-- changeset auto.generated:1825492372-77
CREATE INDEX FL_TEMPLATE_ASSET_CODE ON document_template_asset(code);

-- changeset auto.generated:1825492372-78
CREATE INDEX FL_TEMPLATE_ASSET_TEMPLATES ON document_template_asset(template);

-- changeset auto.generated:1825492372-79
CREATE INDEX FL_TEMPLATE_ASSET_TENANT ON document_template_asset(tenant);

-- changeset auto.generated:1825492372-80
CREATE INDEX FL_TEMPLATE_CODE ON document_template(code);

-- changeset auto.generated:1825492372-81
CREATE INDEX FL_TEMPLATE_TENANT ON document_template(tenant);

-- changeset auto.generated:1825492372-82
CREATE INDEX FL_TEMPLATE_VARIABLE_TENANT ON document_template_variable(tenant);

-- changeset auto.generated:1825492372-83
CREATE INDEX FL_TEMPLATE_VERSION_TEMPLATES ON document_template_version(template);

-- changeset auto.generated:1825492372-84
CREATE INDEX FL_TENANT_LOGIN_PROVIDER_NAME ON access_tenant_login_provider(name);

-- changeset auto.generated:1825492372-85
CREATE INDEX FL_TENANT_LOGIN_PROVIDER_TENANT ON access_tenant_login_provider(tenant);

-- changeset auto.generated:1825492372-86
CREATE INDEX FL_TENANT_TERMS_OF_USE_RELYING_PARTY ON access_tenant_terms_of_use(relying_party);

-- changeset auto.generated:1825492372-87
CREATE INDEX FL_TENANT_TERMS_OF_USE_TENANT ON access_tenant_terms_of_use(tenant);

-- changeset auto.generated:1825492372-88
CREATE INDEX FL_THEME_ASSET_CODE ON document_theme_asset(code);

-- changeset auto.generated:1825492372-89
CREATE INDEX FL_THEME_ASSET_THEME ON document_theme_asset(theme);

-- changeset auto.generated:1825492372-90
CREATE INDEX FL_THEME_GLOBAL ON document_theme(tenant);

-- changeset auto.generated:1825492372-91
CREATE INDEX FL_THEME_VERSION_THEMES ON document_theme_version(theme);

-- changeset auto.generated:1825492372-92
CREATE INDEX FL_TRUSTED_CLIENT_ALLOWED_REDIRECT_CLIENT ON access_trusted_client_allowed_redirect(client);

-- changeset auto.generated:1825492372-93
CREATE INDEX FL_TRUSTED_CLIENT_WITH_BACK_CHANNEL_URL ON access_trusted_client(back_channel_logout_uri);

-- changeset auto.generated:1825492372-94
CREATE INDEX FL_TRUSTED_CLIENT_WITH_FRONT_CHANNEL_URL ON access_trusted_client(front_channel_logout_uri);

-- changeset auto.generated:1825492372-95
CREATE INDEX FL_USER_ACCEPTED_TERMNS_OF_USE_CONDITIONS ON access_user_accepted_termns_of_use(conditions);

-- changeset auto.generated:1825492372-96
CREATE INDEX FL_USER_ACCEPTED_TERMNS_OF_USE_USERS ON access_user_accepted_termns_of_use(user);

-- changeset auto.generated:1825492372-97
CREATE INDEX FL_USER_CONSENTED_SCOPES_TRUSTED_CLIENTS ON access_user_consented_scopes(trusted_client);

-- changeset auto.generated:1825492372-98
CREATE INDEX FL_USER_CONSENTED_SCOPES_USERS ON access_user_consented_scopes(user);

-- changeset auto.generated:1825492372-99
CREATE INDEX FL_USER_CONSENT_PURPOSES_CONSENT_PURPOSES ON access_user_consent_purposes(consent_purpose);

-- changeset auto.generated:1825492372-100
CREATE INDEX FL_USER_CONSENT_PURPOSES_USERS ON access_user_consent_purposes(user);

-- changeset auto.generated:1825492372-101
CREATE INDEX FL_USER_GROUP_MEMBERSHIP_RELYING_PARTY ON access_user_group_membership(relying_party);

-- changeset auto.generated:1825492372-102
CREATE INDEX FL_USER_GROUP_MEMBERSHIP_TRUSTED_CLIENT ON access_user_group_membership(trusted_client);

-- changeset auto.generated:1825492372-103
CREATE INDEX FL_USER_GROUP_MEMBERSHIP_USERS ON access_user_group_membership(user);

-- changeset auto.generated:1825492372-104
CREATE INDEX FL_USER_INVITATION_ACCEPTED_BYS ON access_user_invitation(accepted_by);

-- changeset auto.generated:1825492372-105
CREATE INDEX FL_USER_INVITATION_EMAIL ON access_user_invitation(email);

-- changeset auto.generated:1825492372-106
CREATE INDEX FL_USER_INVITATION_INVITED_BYS ON access_user_invitation(invited_by);

-- changeset auto.generated:1825492372-107
CREATE INDEX FL_USER_INVITATION_STATUS ON access_user_invitation(status);

-- changeset auto.generated:1825492372-108
CREATE INDEX FL_USER_INVITATION_TENANT ON access_user_invitation(tenant);

-- changeset auto.generated:1825492372-109
CREATE INDEX FL_USER_NAME ON access_user(name);

-- changeset auto.generated:1825492372-110
CREATE INDEX FL_USER_ROLE_ASSIGNAMENT_RELYING_PARTY ON access_user_role_assignament(relying_party);

-- changeset auto.generated:1825492372-111
CREATE INDEX FL_USER_ROLE_ASSIGNAMENT_ROLE_ROLE ON access_user_role_assignament_role(`role`);

-- changeset auto.generated:1825492372-112
CREATE INDEX FL_USER_ROLE_ASSIGNAMENT_ROLE_USER_ROLE_ASSIGNAMENTS ON access_user_role_assignament_role(user_role_assignament);

-- changeset auto.generated:1825492372-113
CREATE INDEX FL_USER_ROLE_ASSIGNAMENT_TRUSTED_CLIENT ON access_user_role_assignament(trusted_client);

-- changeset auto.generated:1825492372-114
CREATE INDEX FL_USER_ROLE_ASSIGNAMENT_USERS ON access_user_role_assignament(user);

-- changeset auto.generated:1825492372-115
CREATE INDEX FL_USER_TENANT ON access_user(tenant);

-- changeset auto.generated:1825492372-116
CREATE INDEX FL_USER_WEBAUTHN_CREDENTIAL_USER ON access_user_webauthn_credential(user);

-- changeset auto.generated:1825492372-117
CREATE INDEX ST_ROLE_NAME_DESC ON access_role(name DESC);

-- changeset auto.generated:1825492372-118
CREATE INDEX ST_SNIPPET_ASSET_CODE_ASC ON document_snippet_asset(code);

-- changeset auto.generated:1825492372-119
CREATE INDEX ST_SNIPPET_ASSET_CODE_DESC ON document_snippet_asset(code DESC);

-- changeset auto.generated:1825492372-120
CREATE INDEX ST_SNIPPET_CODE_ASC ON document_snippet(code);

-- changeset auto.generated:1825492372-121
CREATE INDEX ST_SNIPPET_CODE_DESC ON document_snippet(code DESC);

-- changeset auto.generated:1825492372-122
CREATE INDEX ST_TEMPLATE_ASSET_CODE_DESC ON document_template_asset(code DESC);

-- changeset auto.generated:1825492372-123
CREATE INDEX ST_TEMPLATE_CODE_DESC ON document_template(code DESC);

-- changeset auto.generated:1825492372-124
CREATE INDEX ST_TEMPLATE_VARIABLE_CODE_ASC ON document_template_variable(code);

-- changeset auto.generated:1825492372-125
CREATE INDEX ST_TEMPLATE_VARIABLE_CODE_DESC ON document_template_variable(code DESC);

-- changeset auto.generated:1825492372-126
CREATE INDEX ST_TENANT_LOGIN_PROVIDER_NAME_DESC ON access_tenant_login_provider(name DESC);

-- changeset auto.generated:1825492372-127
CREATE INDEX ST_THEME_ASSET_CODE_DESC ON document_theme_asset(code DESC);

-- changeset auto.generated:1825492372-128
CREATE INDEX ST_THEME_NAME_ASC ON document_theme(name);

-- changeset auto.generated:1825492372-129
CREATE INDEX ST_THEME_NAME_DESC ON document_theme(name DESC);

-- changeset auto.generated:1825492372-130
CREATE INDEX ST_USER_INVITATION_CREATED_AT_DESC ON access_user_invitation(created_at DESC);

-- changeset auto.generated:1825492372-131
CREATE INDEX ST_USER_NAME_DESC ON access_user(name DESC);

-- changeset auto.generated:1825492372-132
CREATE UNIQUE INDEX UK_CONSENT_PURPOSE_TENANT_TITLE ON access_consent_purpose(tenant, title);

-- changeset auto.generated:1825492372-133
CREATE UNIQUE INDEX UK_ROLE_RELYING_PARTY_NAME ON access_role(relying_party, name);

-- changeset auto.generated:1825492372-134
CREATE UNIQUE INDEX UK_SNIPPET_ASSET_CODE_SNIPPET ON document_snippet_asset(code, snippet);

-- changeset auto.generated:1825492372-135
CREATE UNIQUE INDEX UK_SNIPPET_CODE_TENANT ON document_snippet(code, tenant);

-- changeset auto.generated:1825492372-136
CREATE UNIQUE INDEX UK_TEMPLATE_ASSET_CODE_TEMPLATE_TENANT ON document_template_asset(code, template, tenant);

-- changeset auto.generated:1825492372-137
CREATE UNIQUE INDEX UK_TEMPLATE_CODE_TENANT ON document_template(code, tenant);

-- changeset auto.generated:1825492372-138
CREATE UNIQUE INDEX UK_TEMPLATE_VARIABLE_CODE_TENANT ON document_template_variable(code, tenant);

-- changeset auto.generated:1825492372-139
CREATE UNIQUE INDEX UK_TENANT_LOGIN_PROVIDER_TENANT_NAME ON access_tenant_login_provider(tenant, name);

-- changeset auto.generated:1825492372-140
CREATE UNIQUE INDEX UK_THEME_ASSET_CODE_THEME ON document_theme_asset(code, theme);

-- changeset auto.generated:1825492372-141
CREATE UNIQUE INDEX UK_THEME_NAME_TENANT ON document_theme(name, tenant);

-- changeset auto.generated:1825492372-142
CREATE UNIQUE INDEX UK_USER_ACCEPTED_TERMNS_OF_USE_USER_CONDITIONS ON access_user_accepted_termns_of_use(user, conditions);

-- changeset auto.generated:1825492372-143
CREATE UNIQUE INDEX UK_USER_ROLE_ASSIGNAMENT_ROLE_ROLE_USER_ROLE_ASSIGNAMENT ON access_user_role_assignament_role(`role`, user_role_assignament);

-- changeset auto.generated:1825492372-144
CREATE UNIQUE INDEX UK_USER_TENANT_NAME ON access_user(tenant, name);

-- changeset auto.generated:1825492372-145
CREATE UNIQUE INDEX UK_USER_WEBAUTHN_CREDENTIAL_USER_NAME ON access_user_webauthn_credential(user, name);

-- changeset auto.generated:1825492372-146
CREATE INDEX idx_audit_entity_id ON _audit_access_api_key_client(entity_id);

-- changeset auto.generated:1825492372-147
CREATE INDEX idx_audit_entity_id ON _audit_access_consent_purpose(entity_id);

-- changeset auto.generated:1825492372-148
CREATE INDEX idx_audit_entity_id ON _audit_access_relying_party(entity_id);

-- changeset auto.generated:1825492372-149
CREATE INDEX idx_audit_entity_id ON _audit_access_role(entity_id);

-- changeset auto.generated:1825492372-150
CREATE INDEX idx_audit_entity_id ON _audit_access_tenant(entity_id);

-- changeset auto.generated:1825492372-151
CREATE INDEX idx_audit_entity_id ON _audit_access_tenant_config(entity_id);

-- changeset auto.generated:1825492372-152
CREATE INDEX idx_audit_entity_id ON _audit_access_tenant_login_provider(entity_id);

-- changeset auto.generated:1825492372-153
CREATE INDEX idx_audit_entity_id ON _audit_access_tenant_terms_of_use(entity_id);

-- changeset auto.generated:1825492372-154
CREATE INDEX idx_audit_entity_id ON _audit_access_trusted_client(entity_id);

-- changeset auto.generated:1825492372-155
CREATE INDEX idx_audit_entity_id ON _audit_access_user(entity_id);

-- changeset auto.generated:1825492372-156
CREATE INDEX idx_audit_entity_id ON _audit_access_user_group_membership(entity_id);

-- changeset auto.generated:1825492372-157
CREATE INDEX idx_audit_entity_id ON _audit_access_user_role_assignament(entity_id);

-- changeset auto.generated:1825492372-158
CREATE INDEX idx_audit_entity_id ON _audit_document_snippet(entity_id);

-- changeset auto.generated:1825492372-159
CREATE INDEX idx_audit_entity_id ON _audit_document_snippet_asset(entity_id);

-- changeset auto.generated:1825492372-160
CREATE INDEX idx_audit_entity_id ON _audit_document_snippet_version(entity_id);

-- changeset auto.generated:1825492372-161
CREATE INDEX idx_audit_entity_id ON _audit_document_template(entity_id);

-- changeset auto.generated:1825492372-162
CREATE INDEX idx_audit_entity_id ON _audit_document_template_asset(entity_id);

-- changeset auto.generated:1825492372-163
CREATE INDEX idx_audit_entity_id ON _audit_document_template_variable(entity_id);

-- changeset auto.generated:1825492372-164
CREATE INDEX idx_audit_entity_id ON _audit_document_template_version(entity_id);

-- changeset auto.generated:1825492372-165
CREATE INDEX idx_audit_entity_id ON _audit_document_theme(entity_id);

-- changeset auto.generated:1825492372-166
CREATE INDEX idx_audit_entity_id ON _audit_document_theme_asset(entity_id);

-- changeset auto.generated:1825492372-167
CREATE INDEX idx_audit_entity_id ON _audit_document_theme_version(entity_id);

-- changeset auto.generated:1825492372-168
CREATE INDEX idx_audit_entity_id ON _audit_notification_message(entity_id);

-- changeset auto.generated:1825492372-169
CREATE INDEX idx_audit_entity_id ON _audit_notification_smtp_outbound_config(entity_id);

-- changeset auto.generated:1825492372-170
CREATE INDEX idx_audit_timestamp ON _audit_access_api_key_client(timestamp DESC);

-- changeset auto.generated:1825492372-171
CREATE INDEX idx_audit_timestamp ON _audit_access_consent_purpose(timestamp DESC);

-- changeset auto.generated:1825492372-172
CREATE INDEX idx_audit_timestamp ON _audit_access_relying_party(timestamp DESC);

-- changeset auto.generated:1825492372-173
CREATE INDEX idx_audit_timestamp ON _audit_access_role(timestamp DESC);

-- changeset auto.generated:1825492372-174
CREATE INDEX idx_audit_timestamp ON _audit_access_tenant(timestamp DESC);

-- changeset auto.generated:1825492372-175
CREATE INDEX idx_audit_timestamp ON _audit_access_tenant_config(timestamp DESC);

-- changeset auto.generated:1825492372-176
CREATE INDEX idx_audit_timestamp ON _audit_access_tenant_login_provider(timestamp DESC);

-- changeset auto.generated:1825492372-177
CREATE INDEX idx_audit_timestamp ON _audit_access_tenant_terms_of_use(timestamp DESC);

-- changeset auto.generated:1825492372-178
CREATE INDEX idx_audit_timestamp ON _audit_access_trusted_client(timestamp DESC);

-- changeset auto.generated:1825492372-179
CREATE INDEX idx_audit_timestamp ON _audit_access_user(timestamp DESC);

-- changeset auto.generated:1825492372-180
CREATE INDEX idx_audit_timestamp ON _audit_access_user_group_membership(timestamp DESC);

-- changeset auto.generated:1825492372-181
CREATE INDEX idx_audit_timestamp ON _audit_access_user_role_assignament(timestamp DESC);

-- changeset auto.generated:1825492372-182
CREATE INDEX idx_audit_timestamp ON _audit_document_snippet(timestamp DESC);

-- changeset auto.generated:1825492372-183
CREATE INDEX idx_audit_timestamp ON _audit_document_snippet_asset(timestamp DESC);

-- changeset auto.generated:1825492372-184
CREATE INDEX idx_audit_timestamp ON _audit_document_snippet_version(timestamp DESC);

-- changeset auto.generated:1825492372-185
CREATE INDEX idx_audit_timestamp ON _audit_document_template(timestamp DESC);

-- changeset auto.generated:1825492372-186
CREATE INDEX idx_audit_timestamp ON _audit_document_template_asset(timestamp DESC);

-- changeset auto.generated:1825492372-187
CREATE INDEX idx_audit_timestamp ON _audit_document_template_variable(timestamp DESC);

-- changeset auto.generated:1825492372-188
CREATE INDEX idx_audit_timestamp ON _audit_document_template_version(timestamp DESC);

-- changeset auto.generated:1825492372-189
CREATE INDEX idx_audit_timestamp ON _audit_document_theme(timestamp DESC);

-- changeset auto.generated:1825492372-190
CREATE INDEX idx_audit_timestamp ON _audit_document_theme_asset(timestamp DESC);

-- changeset auto.generated:1825492372-191
CREATE INDEX idx_audit_timestamp ON _audit_document_theme_version(timestamp DESC);

-- changeset auto.generated:1825492372-192
CREATE INDEX idx_audit_timestamp ON _audit_notification_message(timestamp DESC);

-- changeset auto.generated:1825492372-193
CREATE INDEX idx_audit_timestamp ON _audit_notification_smtp_outbound_config(timestamp DESC);

-- changeset auto.generated:1825492372-194
CREATE INDEX idx_audit_user ON _audit_access_api_key_client(performed_by);

-- changeset auto.generated:1825492372-195
CREATE INDEX idx_audit_user ON _audit_access_consent_purpose(performed_by);

-- changeset auto.generated:1825492372-196
CREATE INDEX idx_audit_user ON _audit_access_relying_party(performed_by);

-- changeset auto.generated:1825492372-197
CREATE INDEX idx_audit_user ON _audit_access_role(performed_by);

-- changeset auto.generated:1825492372-198
CREATE INDEX idx_audit_user ON _audit_access_tenant(performed_by);

-- changeset auto.generated:1825492372-199
CREATE INDEX idx_audit_user ON _audit_access_tenant_config(performed_by);

-- changeset auto.generated:1825492372-200
CREATE INDEX idx_audit_user ON _audit_access_tenant_login_provider(performed_by);

-- changeset auto.generated:1825492372-201
CREATE INDEX idx_audit_user ON _audit_access_tenant_terms_of_use(performed_by);

-- changeset auto.generated:1825492372-202
CREATE INDEX idx_audit_user ON _audit_access_trusted_client(performed_by);

-- changeset auto.generated:1825492372-203
CREATE INDEX idx_audit_user ON _audit_access_user(performed_by);

-- changeset auto.generated:1825492372-204
CREATE INDEX idx_audit_user ON _audit_access_user_group_membership(performed_by);

-- changeset auto.generated:1825492372-205
CREATE INDEX idx_audit_user ON _audit_access_user_role_assignament(performed_by);

-- changeset auto.generated:1825492372-206
CREATE INDEX idx_audit_user ON _audit_document_snippet(performed_by);

-- changeset auto.generated:1825492372-207
CREATE INDEX idx_audit_user ON _audit_document_snippet_asset(performed_by);

-- changeset auto.generated:1825492372-208
CREATE INDEX idx_audit_user ON _audit_document_snippet_version(performed_by);

-- changeset auto.generated:1825492372-209
CREATE INDEX idx_audit_user ON _audit_document_template(performed_by);

-- changeset auto.generated:1825492372-210
CREATE INDEX idx_audit_user ON _audit_document_template_asset(performed_by);

-- changeset auto.generated:1825492372-211
CREATE INDEX idx_audit_user ON _audit_document_template_variable(performed_by);

-- changeset auto.generated:1825492372-212
CREATE INDEX idx_audit_user ON _audit_document_template_version(performed_by);

-- changeset auto.generated:1825492372-213
CREATE INDEX idx_audit_user ON _audit_document_theme(performed_by);

-- changeset auto.generated:1825492372-214
CREATE INDEX idx_audit_user ON _audit_document_theme_asset(performed_by);

-- changeset auto.generated:1825492372-215
CREATE INDEX idx_audit_user ON _audit_document_theme_version(performed_by);

-- changeset auto.generated:1825492372-216
CREATE INDEX idx_audit_user ON _audit_notification_message(performed_by);

-- changeset auto.generated:1825492372-217
CREATE INDEX idx_audit_user ON _audit_notification_smtp_outbound_config(performed_by);

-- changeset auto.generated:1825492372-218
CREATE INDEX idx_challenge_expires ON _oauth_webauthn_challenge(expires_at);

-- changeset auto.generated:1825492372-219
CREATE INDEX idx_device_client ON _oauth_device_codes(client_id);

-- changeset auto.generated:1825492372-220
CREATE INDEX idx_device_expires ON _oauth_device_codes(expires_at);

-- changeset auto.generated:1825492372-221
CREATE INDEX idx_device_status ON _oauth_device_codes(status);

-- changeset auto.generated:1825492372-222
CREATE INDEX idx_long_tasks_code_actor ON _long_tasks(code, actor);

-- changeset auto.generated:1825492372-223
CREATE INDEX idx_magic_link_expires ON _oauth_magic_link(expires_at);

-- changeset auto.generated:1825492372-224
CREATE INDEX idx_par_expires ON _oauth_par_request(expires_at);

-- changeset auto.generated:1825492372-225
CREATE INDEX idx_par_tenant ON _oauth_par_request(tenant_id);

-- changeset auto.generated:1825492372-226
CREATE INDEX idx_revoked_expires ON _oauth_revoked_jti(expires_at);

-- changeset auto.generated:1825492372-227
CREATE INDEX idx_revoked_tenant ON _oauth_revoked_jti(tenant_id);

-- changeset auto.generated:1825492372-228
CREATE INDEX idx_session_jti ON _oauth_session(jti);

-- changeset auto.generated:1825492372-229
CREATE INDEX idx_session_refresh_jti ON _oauth_session(refresh_jti);

-- changeset manual:20260513083001-session-token-index-session
CREATE INDEX idx_session_token_session ON _oauth_session_token(session);

-- changeset manual:20260513083002-session-token-index-exp
CREATE INDEX idx_session_token_exp ON _oauth_session_token(expires_at);

-- changeset auto.generated:1825492372-230
CREATE UNIQUE INDEX uq_device_user_code ON _oauth_device_codes(tenant, user_code);

-- changeset auto.generated:1825492372-231
ALTER TABLE access_consent_purpose ADD CONSTRAINT FK_ACCESS_CONSENT_PURPOSE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-232
ALTER TABLE access_role ADD CONSTRAINT FK_ACCESS_ROLE_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-233
ALTER TABLE access_tenant_config ADD CONSTRAINT FK_ACCESS_TENANT_CONFIG_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-234
ALTER TABLE access_tenant_login_provider ADD CONSTRAINT FK_ACCESS_TENANT_LOGIN_PROVIDER_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-235
ALTER TABLE access_tenant_terms_of_use ADD CONSTRAINT FK_ACCESS_TENANT_TERMS_OF_USE_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-236
ALTER TABLE access_tenant_terms_of_use ADD CONSTRAINT FK_ACCESS_TENANT_TERMS_OF_USE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-237
ALTER TABLE access_trusted_client_allowed_redirect ADD CONSTRAINT FK_ACCESS_TRUSTED_CLIENT_ALLOWED_REDIRECT_CLIENT FOREIGN KEY (client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-238
ALTER TABLE access_user_accepted_termns_of_use ADD CONSTRAINT FK_ACCESS_USER_ACCEPTED_TERMNS_OF_USE_CONDITIONS FOREIGN KEY (conditions) REFERENCES access_tenant_terms_of_use (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-239
ALTER TABLE access_user_accepted_termns_of_use ADD CONSTRAINT FK_ACCESS_USER_ACCEPTED_TERMNS_OF_USE_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-240
ALTER TABLE access_user_access_temporal_code ADD CONSTRAINT FK_ACCESS_USER_ACCESS_TEMPORAL_CODE_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-241
ALTER TABLE access_user_consented_scopes ADD CONSTRAINT FK_ACCESS_USER_CONSENTED_SCOPES_TRUSTED_CLIENT FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-242
ALTER TABLE access_user_consented_scopes ADD CONSTRAINT FK_ACCESS_USER_CONSENTED_SCOPES_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-243
ALTER TABLE access_user_consent_purposes ADD CONSTRAINT FK_ACCESS_USER_CONSENT_PURPOSES_CONSENT_PURPOSE FOREIGN KEY (consent_purpose) REFERENCES access_consent_purpose (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-244
ALTER TABLE access_user_consent_purposes ADD CONSTRAINT FK_ACCESS_USER_CONSENT_PURPOSES_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-245
ALTER TABLE access_user_group_membership ADD CONSTRAINT FK_ACCESS_USER_GROUP_MEMBERSHIP_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-246
ALTER TABLE access_user_group_membership ADD CONSTRAINT FK_ACCESS_USER_GROUP_MEMBERSHIP_TRUSTED_CLIENT FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-247
ALTER TABLE access_user_group_membership ADD CONSTRAINT FK_ACCESS_USER_GROUP_MEMBERSHIP_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-248
ALTER TABLE access_user_invitation ADD CONSTRAINT FK_ACCESS_USER_INVITATION_ACCEPTED_BY FOREIGN KEY (accepted_by) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-249
ALTER TABLE access_user_invitation ADD CONSTRAINT FK_ACCESS_USER_INVITATION_INVITED_BY FOREIGN KEY (invited_by) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-250
ALTER TABLE access_user_invitation ADD CONSTRAINT FK_ACCESS_USER_INVITATION_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-251
ALTER TABLE access_user_profile ADD CONSTRAINT FK_ACCESS_USER_PROFILE_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-252
ALTER TABLE access_user_role_assignament ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNAMENT_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-253
ALTER TABLE access_user_role_assignament_role ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNAMENT_ROLE_ROLE FOREIGN KEY (`role`) REFERENCES access_role (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-254
ALTER TABLE access_user_role_assignament_role ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNAMENT_ROLE_USER_ROLE_ASSIGNAMENT FOREIGN KEY (user_role_assignament) REFERENCES access_user_role_assignament (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-255
ALTER TABLE access_user_role_assignament ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNAMENT_TRUSTED_CLIENT FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-256
ALTER TABLE access_user_role_assignament ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNAMENT_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset manual:20260513083003-session-token-fk
ALTER TABLE _oauth_session_token ADD CONSTRAINT FK__OAUTH_SESSION_TOKEN_SESSION FOREIGN KEY (session) REFERENCES _oauth_session (session) ON UPDATE CASCADE ON DELETE CASCADE;

-- changeset auto.generated:1825492372-257
ALTER TABLE access_user ADD CONSTRAINT FK_ACCESS_USER_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-258
ALTER TABLE access_user_webauthn_credential ADD CONSTRAINT FK_ACCESS_USER_WEBAUTHN_CREDENTIAL_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-259
ALTER TABLE document_snippet_asset ADD CONSTRAINT FK_DOCUMENT_SNIPPET_ASSET_SNIPPET FOREIGN KEY (snippet) REFERENCES document_snippet (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-260
ALTER TABLE document_snippet ADD CONSTRAINT FK_DOCUMENT_SNIPPET_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-261
ALTER TABLE document_snippet_version ADD CONSTRAINT FK_DOCUMENT_SNIPPET_VERSION_SNIPPET FOREIGN KEY (snippet) REFERENCES document_snippet (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-262
ALTER TABLE document_template_asset ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_ASSET_TEMPLATE FOREIGN KEY (template) REFERENCES document_template (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-263
ALTER TABLE document_template_asset ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_ASSET_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-264
ALTER TABLE document_template ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-265
ALTER TABLE document_template_variable ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_VARIABLE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-266
ALTER TABLE document_template_version ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_VERSION_TEMPLATE FOREIGN KEY (template) REFERENCES document_template (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-267
ALTER TABLE document_theme_asset ADD CONSTRAINT FK_DOCUMENT_THEME_ASSET_THEME FOREIGN KEY (theme) REFERENCES document_theme (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-268
ALTER TABLE document_theme ADD CONSTRAINT FK_DOCUMENT_THEME_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-269
ALTER TABLE document_theme_version ADD CONSTRAINT FK_DOCUMENT_THEME_VERSION_THEME FOREIGN KEY (theme) REFERENCES document_theme (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-270
ALTER TABLE notification_message ADD CONSTRAINT FK_NOTIFICATION_MESSAGE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-271
ALTER TABLE notification_smtp_outbound_config ADD CONSTRAINT FK_NOTIFICATION_SMTP_OUTBOUND_CONFIG_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;
