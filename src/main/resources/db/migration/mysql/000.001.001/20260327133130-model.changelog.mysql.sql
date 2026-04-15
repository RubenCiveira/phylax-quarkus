-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE _filestorer (code VARCHAR(250) NOT NULL, temp SMALLINT DEFAULT 0 NOT NULL, name VARCHAR(250) NOT NULL, mime VARCHAR(250) NOT NULL, upload timestamp NOT NULL, bytes LONGBLOB NOT NULL, CONSTRAINT PK__FILESTORER PRIMARY KEY (code), UNIQUE (code));

-- changeset auto.generated:1825492372-2
CREATE TABLE _long_tasks (code VARCHAR(250) NOT NULL, actor VARCHAR(250) NOT NULL, creation timestamp NOT NULL, completion timestamp DEFAULT NULL NULL, expiration timestamp DEFAULT NULL NULL, progress LONGTEXT NOT NULL, CONSTRAINT PK__LONG_TASKS PRIMARY KEY (code), UNIQUE (code));

-- changeset auto.generated:1825492372-3
CREATE TABLE _oauth_delegated_codes (expiration timestamp NOT NULL, code TEXT NOT NULL, token TEXT NOT NULL);

-- changeset auto.generated:1825492372-4
CREATE TABLE _oauth_jwt_keys (expiration timestamp NOT NULL, since timestamp NOT NULL, keyid VARCHAR(255) NOT NULL, private TEXT NOT NULL, public TEXT NOT NULL);

-- changeset auto.generated:1825492372-5
CREATE TABLE _oauth_sessions (session VARCHAR(255) NOT NULL, jti VARCHAR(36) NULL, expiration timestamp NOT NULL, client_id VARCHAR(250) NOT NULL, grant_type VARCHAR(20) NOT NULL, auth_data TEXT NOT NULL, csid TEXT NOT NULL, INDEX idx_oauth_sessions_jti (jti));

-- changeset auto.generated:1825492372-6
CREATE TABLE _oauth_temporal_codes (code VARCHAR(255) NOT NULL, code_data TEXT NOT NULL, expiration timestamp NOT NULL);

-- changeset auto.generated:1825492372-7
CREATE TABLE _oauth_temporal_keys (current VARCHAR(255) NOT NULL, old VARCHAR(255) NOT NULL, expiration timestamp NOT NULL);

-- changeset auto.generated:1825492372-8
CREATE TABLE _security_magic_links (token VARCHAR(255) NOT NULL, `path` TEXT NOT NULL, jwt_token TEXT NULL, actor_json TEXT NULL, source_json TEXT NULL, current_reads INT NOT NULL, max_reads INT NOT NULL, expiration timestamp NOT NULL, CONSTRAINT PK__SECURITY_MAGIC_LINKS PRIMARY KEY (token));

-- changeset auto.generated:1825492372-9
CREATE TABLE access_api_key_client (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT NOT NULL, `key` VARCHAR(255) NULL, scopes VARCHAR(255) NULL, CONSTRAINT PK_ACCESS_API_KEY_CLIENT PRIMARY KEY (uid), UNIQUE (code));

-- changeset auto.generated:1825492372-10
CREATE TABLE access_api_key_client_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_API_KEY_CLIENT_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-11
CREATE TABLE access_relying_party (uid VARCHAR(255) NOT NULL, version INT NOT NULL, api_key VARCHAR(255) NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, CONSTRAINT PK_ACCESS_RELYING_PARTY PRIMARY KEY (uid), UNIQUE (api_key), UNIQUE (code));

-- changeset auto.generated:1825492372-12
CREATE TABLE access_relying_party_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_RELYING_PARTY_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-13
CREATE TABLE access_role (uid VARCHAR(255) NOT NULL, version INT NOT NULL, name VARCHAR(255) NOT NULL, relying_party VARCHAR(255) NULL, CONSTRAINT PK_ACCESS_ROLE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-14
CREATE TABLE access_role_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_ROLE_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-15
CREATE TABLE access_tenant (uid VARCHAR(255) NOT NULL, version INT NOT NULL, domain VARCHAR(255) NOT NULL, enabled BIT NOT NULL, mark_for_delete BIT NOT NULL, mark_for_delete_time timestamp DEFAULT NULL NULL, name VARCHAR(255) NOT NULL, root BIT DEFAULT 0 NULL, CONSTRAINT PK_ACCESS_TENANT PRIMARY KEY (uid), UNIQUE (domain), UNIQUE (name));

-- changeset auto.generated:1825492372-16
CREATE TABLE access_tenant_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_TENANT_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-17
CREATE TABLE access_tenant_config (uid VARCHAR(255) NOT NULL, version INT NOT NULL, allow_recover_pass BIT DEFAULT 0 NULL, allow_register BIT DEFAULT 0 NULL, disabled_user_email LONGTEXT NULL, enable_register_users BIT DEFAULT 0 NULL, enabled_user_email LONGTEXT NULL, force_mfa BIT NOT NULL, inner_label VARCHAR(255) NULL, recover_pass_email LONGTEXT NULL, registerd_email LONGTEXT NULL, wellcome_email LONGTEXT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_TENANT_CONFIG PRIMARY KEY (uid), UNIQUE (tenant));

-- changeset auto.generated:1825492372-18
CREATE TABLE access_tenant_config_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_TENANT_CONFIG_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-19
CREATE TABLE access_tenant_login_provider (uid VARCHAR(255) NOT NULL, version INT NOT NULL, certificate LONGTEXT NULL, direct_access BIT DEFAULT 0 NULL, disabled BIT DEFAULT 0 NULL, metadata VARCHAR(255) NULL, name VARCHAR(255) NOT NULL, private_key VARCHAR(255) NULL, public_key VARCHAR(255) NULL, source VARCHAR(255) NOT NULL, users_enabled_by_default BIT NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_TENANT_LOGIN_PROVIDER PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-20
CREATE TABLE access_tenant_login_provider_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_TENANT_LOGIN_PROVIDER_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-21
CREATE TABLE access_tenant_terms_of_use (uid VARCHAR(255) NOT NULL, version INT NOT NULL, activation_date timestamp DEFAULT NULL NULL, attached VARCHAR(255) NULL, enabled BIT NOT NULL, text LONGTEXT NOT NULL, relying_party VARCHAR(255) NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_TENANT_TERMS_OF_USE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-22
CREATE TABLE access_tenant_terms_of_use_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_TENANT_TERMS_OF_USE_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-23
CREATE TABLE access_trusted_client (uid VARCHAR(255) NOT NULL, version INT NOT NULL, allow_all_scopes BIT DEFAULT 0 NULL, code VARCHAR(255) NOT NULL, enabled BIT NOT NULL, public_allow BIT NOT NULL, secret_oauth VARCHAR(255) NULL, CONSTRAINT PK_ACCESS_TRUSTED_CLIENT PRIMARY KEY (uid), UNIQUE (code));

-- changeset auto.generated:1825492372-24
CREATE TABLE access_trusted_client_allowed_redirect (uid VARCHAR(255) NOT NULL, version INT NOT NULL, url VARCHAR(255) NOT NULL, client VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_TRUSTED_CLIENT_ALLOWED_REDIRECT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-25
CREATE TABLE access_trusted_client_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_TRUSTED_CLIENT_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-26
CREATE TABLE access_user (uid VARCHAR(255) NOT NULL, version INT NOT NULL, approve VARCHAR(255) NULL, blocked_until timestamp DEFAULT NULL NULL, email VARCHAR(255) NULL, enabled BIT DEFAULT 0 NULL, name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, provider VARCHAR(255) NULL, second_factor_seed VARCHAR(255) NULL, temporal_password BIT DEFAULT 0 NULL, use_second_factors BIT DEFAULT 0 NULL, wellcome_at timestamp DEFAULT NULL NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-27
CREATE TABLE access_user_accepted_termns_of_use (uid VARCHAR(255) NOT NULL, version INT NOT NULL, accept_date timestamp DEFAULT NULL NULL, conditions VARCHAR(255) NOT NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_ACCEPTED_TERMNS_OF_USE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-28
CREATE TABLE access_user_access_temporal_code (uid VARCHAR(255) NOT NULL, version INT NOT NULL, failed_login_attempts INT DEFAULT NULL NULL, recovery_code VARCHAR(255) NULL, recovery_code_expiration timestamp DEFAULT NULL NULL, register_code VARCHAR(255) NULL, register_code_expiration timestamp DEFAULT NULL NULL, register_code_url VARCHAR(255) NULL, temp_second_factor_seed VARCHAR(255) NULL, temp_second_factor_seed_expiration timestamp DEFAULT NULL NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_ACCESS_TEMPORAL_CODE PRIMARY KEY (uid), UNIQUE (recovery_code), UNIQUE (register_code), UNIQUE (user));

-- changeset auto.generated:1825492372-29
CREATE TABLE access_user_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_USER_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-30
CREATE TABLE access_user_group_membership (uid VARCHAR(255) NOT NULL, version INT NOT NULL, `groups` LONGTEXT NULL, relying_party VARCHAR(255) NULL, trusted_client VARCHAR(255) NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_GROUP_MEMBERSHIP PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-31
CREATE TABLE access_user_group_membership_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_USER_GROUP_MEMBERSHIP_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-32
CREATE TABLE access_user_role_assignament (uid VARCHAR(255) NOT NULL, version INT NOT NULL, relying_party VARCHAR(255) NULL, trusted_client VARCHAR(255) NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_ROLE_ASSIGNAMENT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-33
CREATE TABLE access_user_role_assignament_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_USER_ROLE_ASSIGNAMENT_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-34
CREATE TABLE access_user_role_assignament_role (uid VARCHAR(255) NOT NULL, version INT NOT NULL, `role` VARCHAR(255) NOT NULL, user_role_assignament VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_ROLE_ASSIGNAMENT_ROLE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-35
CREATE TABLE document_template (uid VARCHAR(255) NOT NULL, version INT NOT NULL, channel VARCHAR(255) NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, tenant VARCHAR(255) NULL, theme VARCHAR(255) NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-36
CREATE TABLE document_template_asset (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, content VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, type VARCHAR(255) NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_ASSET PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-37
CREATE TABLE document_template_asset_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_ASSET_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-38
CREATE TABLE document_template_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-39
CREATE TABLE document_template_snippet (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, content_html VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_SNIPPET PRIMARY KEY (uid), UNIQUE (code));

-- changeset auto.generated:1825492372-40
CREATE TABLE document_template_snippet_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_SNIPPET_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-41
CREATE TABLE document_template_variable (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, type VARCHAR(255) NOT NULL, value VARCHAR(255) NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_VARIABLE PRIMARY KEY (uid), UNIQUE (code));

-- changeset auto.generated:1825492372-42
CREATE TABLE document_template_variable_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_VARIABLE_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-43
CREATE TABLE document_template_version (uid VARCHAR(255) NOT NULL, version INT NOT NULL, content_html LONGTEXT NOT NULL, content_text LONGTEXT NULL, subject VARCHAR(255) NULL, template VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_VERSION PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-44
CREATE TABLE document_template_version_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_VERSION_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-45
CREATE TABLE document_theme (uid VARCHAR(255) NOT NULL, version INT NOT NULL, custom_css VARCHAR(255) NULL, enabled BIT DEFAULT 0 NULL, is_default BIT DEFAULT 0 NULL, name VARCHAR(255) NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_THEME PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-46
CREATE TABLE document_theme_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_THEME_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-47
CREATE TABLE notification_message (uid VARCHAR(255) NOT NULL, version INT NOT NULL, content LONGTEXT NOT NULL, created_at timestamp NOT NULL, retries INT NOT NULL, send_at timestamp DEFAULT NULL NULL, target VARCHAR(255) NOT NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_NOTIFICATION_MESSAGE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-48
CREATE TABLE notification_message_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_NOTIFICATION_MESSAGE_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-49
CREATE TABLE notification_smtp_outbound_config (uid VARCHAR(255) NOT NULL, version INT NOT NULL, host VARCHAR(255) NOT NULL, login VARCHAR(255) NOT NULL, max_retries INT NOT NULL, password VARCHAR(255) NOT NULL, port BIT NOT NULL, rate_limit INT NOT NULL, retry_delay INT NOT NULL, sender_email VARCHAR(255) NOT NULL, sender_name VARCHAR(255) NULL, timeout INT NOT NULL, use_tls BIT NOT NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_NOTIFICATION_SMTP_OUTBOUND_CONFIG PRIMARY KEY (uid), UNIQUE (tenant));

-- changeset auto.generated:1825492372-50
CREATE TABLE notification_smtp_outbound_config_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_NOTIFICATION_SMTP_OUTBOUND_CONFIG_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-51
CREATE INDEX FL_MESSAGE_TENANT ON notification_message(tenant);

-- changeset auto.generated:1825492372-52
CREATE INDEX FL_ROLE_NAME ON access_role(name);

-- changeset auto.generated:1825492372-53
CREATE INDEX FL_ROLE_RELYING_PARTY ON access_role(relying_party);

-- changeset auto.generated:1825492372-54
CREATE INDEX FL_TEMPLATE_ASSET_CODE ON document_template_asset(code);

-- changeset auto.generated:1825492372-55
CREATE INDEX FL_TEMPLATE_ASSET_TENANT ON document_template_asset(tenant);

-- changeset auto.generated:1825492372-56
CREATE INDEX FL_TEMPLATE_CODE ON document_template(code);

-- changeset auto.generated:1825492372-57
CREATE INDEX FL_TEMPLATE_SNIPPET_TENANT ON document_template_snippet(tenant);

-- changeset auto.generated:1825492372-58
CREATE INDEX FL_TEMPLATE_TENANT ON document_template(tenant);

-- changeset auto.generated:1825492372-59
CREATE INDEX FL_TEMPLATE_THEMES ON document_template(theme);

-- changeset auto.generated:1825492372-60
CREATE INDEX FL_TEMPLATE_VARIABLE_TENANT ON document_template_variable(tenant);

-- changeset auto.generated:1825492372-61
CREATE INDEX FL_TEMPLATE_VERSION_TEMPLATES ON document_template_version(template);

-- changeset auto.generated:1825492372-62
CREATE INDEX FL_TENANT_LOGIN_PROVIDER_NAME ON access_tenant_login_provider(name);

-- changeset auto.generated:1825492372-63
CREATE INDEX FL_TENANT_LOGIN_PROVIDER_TENANT ON access_tenant_login_provider(tenant);

-- changeset auto.generated:1825492372-64
CREATE INDEX FL_TENANT_TERMS_OF_USE_RELYING_PARTY ON access_tenant_terms_of_use(relying_party);

-- changeset auto.generated:1825492372-65
CREATE INDEX FL_TENANT_TERMS_OF_USE_TENANT ON access_tenant_terms_of_use(tenant);

-- changeset auto.generated:1825492372-66
CREATE INDEX FL_THEME_TENANTS ON document_theme(tenant);

-- changeset auto.generated:1825492372-67
CREATE INDEX FL_TRUSTED_CLIENT_ALLOWED_REDIRECT_CLIENT ON access_trusted_client_allowed_redirect(client);

-- changeset auto.generated:1825492372-68
CREATE INDEX FL_USER_ACCEPTED_TERMNS_OF_USE_CONDITIONS ON access_user_accepted_termns_of_use(conditions);

-- changeset auto.generated:1825492372-69
CREATE INDEX FL_USER_ACCEPTED_TERMNS_OF_USE_USERS ON access_user_accepted_termns_of_use(user);

-- changeset auto.generated:1825492372-70
CREATE INDEX FL_USER_GROUP_MEMBERSHIP_RELYING_PARTY ON access_user_group_membership(relying_party);

-- changeset auto.generated:1825492372-71
CREATE INDEX FL_USER_GROUP_MEMBERSHIP_TRUSTED_CLIENT ON access_user_group_membership(trusted_client);

-- changeset auto.generated:1825492372-72
CREATE INDEX FL_USER_GROUP_MEMBERSHIP_USERS ON access_user_group_membership(user);

-- changeset auto.generated:1825492372-73
CREATE INDEX FL_USER_NAME ON access_user(name);

-- changeset auto.generated:1825492372-74
CREATE INDEX FL_USER_ROLE_ASSIGNAMENT_RELYING_PARTY ON access_user_role_assignament(relying_party);

-- changeset auto.generated:1825492372-75
CREATE INDEX FL_USER_ROLE_ASSIGNAMENT_ROLE_ROLE ON access_user_role_assignament_role(`role`);

-- changeset auto.generated:1825492372-76
CREATE INDEX FL_USER_ROLE_ASSIGNAMENT_ROLE_USER_ROLE_ASSIGNAMENTS ON access_user_role_assignament_role(user_role_assignament);

-- changeset auto.generated:1825492372-77
CREATE INDEX FL_USER_ROLE_ASSIGNAMENT_TRUSTED_CLIENT ON access_user_role_assignament(trusted_client);

-- changeset auto.generated:1825492372-78
CREATE INDEX FL_USER_ROLE_ASSIGNAMENT_USERS ON access_user_role_assignament(user);

-- changeset auto.generated:1825492372-79
CREATE INDEX FL_USER_TENANT ON access_user(tenant);

-- changeset auto.generated:1825492372-80
CREATE INDEX ST_ROLE_NAME_DESC ON access_role(name DESC);

-- changeset auto.generated:1825492372-81
CREATE INDEX ST_TEMPLATE_ASSET_CODE_DESC ON document_template_asset(code DESC);

-- changeset auto.generated:1825492372-82
CREATE INDEX ST_TEMPLATE_CODE_DESC ON document_template(code DESC);

-- changeset auto.generated:1825492372-83
CREATE INDEX ST_TENANT_LOGIN_PROVIDER_NAME_DESC ON access_tenant_login_provider(name DESC);

-- changeset auto.generated:1825492372-84
CREATE INDEX ST_THEME_NAME_ASC ON document_theme(name);

-- changeset auto.generated:1825492372-85
CREATE INDEX ST_THEME_NAME_DESC ON document_theme(name DESC);

-- changeset auto.generated:1825492372-86
CREATE INDEX ST_USER_NAME_DESC ON access_user(name DESC);

-- changeset auto.generated:1825492372-87
CREATE UNIQUE INDEX UK_ROLE_RELYING_PARTY_NAME ON access_role(relying_party, name);

-- changeset auto.generated:1825492372-88
CREATE UNIQUE INDEX UK_TEMPLATE_ASSET_CODE_TENANT ON document_template_asset(code, tenant);

-- changeset auto.generated:1825492372-89
CREATE UNIQUE INDEX UK_TEMPLATE_CODE_TENANT ON document_template(code, tenant);

-- changeset auto.generated:1825492372-90
CREATE UNIQUE INDEX UK_TEMPLATE_SNIPPET_CODE_TENANT ON document_template_snippet(code, tenant);

-- changeset auto.generated:1825492372-91
CREATE UNIQUE INDEX UK_TEMPLATE_VARIABLE_CODE_TENANT ON document_template_variable(code, tenant);

-- changeset auto.generated:1825492372-92
CREATE UNIQUE INDEX UK_TENANT_LOGIN_PROVIDER_TENANT_NAME ON access_tenant_login_provider(tenant, name);

-- changeset auto.generated:1825492372-93
CREATE UNIQUE INDEX UK_USER_ACCEPTED_TERMNS_OF_USE_USER_CONDITIONS ON access_user_accepted_termns_of_use(user, conditions);

-- changeset auto.generated:1825492372-94
CREATE UNIQUE INDEX UK_USER_ROLE_ASSIGNAMENT_ROLE_ROLE_USER_ROLE_ASSIGNAMENT ON access_user_role_assignament_role(`role`, user_role_assignament);

-- changeset auto.generated:1825492372-95
CREATE UNIQUE INDEX UK_USER_TENANT_NAME ON access_user(tenant, name);

-- changeset auto.generated:1825492372-96
CREATE INDEX idx_audit_entity_id ON access_api_key_client_audit(entity_id);

-- changeset auto.generated:1825492372-97
CREATE INDEX idx_audit_entity_id ON access_relying_party_audit(entity_id);

-- changeset auto.generated:1825492372-98
CREATE INDEX idx_audit_entity_id ON access_role_audit(entity_id);

-- changeset auto.generated:1825492372-99
CREATE INDEX idx_audit_entity_id ON access_tenant_audit(entity_id);

-- changeset auto.generated:1825492372-100
CREATE INDEX idx_audit_entity_id ON access_tenant_config_audit(entity_id);

-- changeset auto.generated:1825492372-101
CREATE INDEX idx_audit_entity_id ON access_tenant_login_provider_audit(entity_id);

-- changeset auto.generated:1825492372-102
CREATE INDEX idx_audit_entity_id ON access_tenant_terms_of_use_audit(entity_id);

-- changeset auto.generated:1825492372-103
CREATE INDEX idx_audit_entity_id ON access_trusted_client_audit(entity_id);

-- changeset auto.generated:1825492372-104
CREATE INDEX idx_audit_entity_id ON access_user_audit(entity_id);

-- changeset auto.generated:1825492372-105
CREATE INDEX idx_audit_entity_id ON access_user_group_membership_audit(entity_id);

-- changeset auto.generated:1825492372-106
CREATE INDEX idx_audit_entity_id ON access_user_role_assignament_audit(entity_id);

-- changeset auto.generated:1825492372-107
CREATE INDEX idx_audit_entity_id ON document_template_asset_audit(entity_id);

-- changeset auto.generated:1825492372-108
CREATE INDEX idx_audit_entity_id ON document_template_audit(entity_id);

-- changeset auto.generated:1825492372-109
CREATE INDEX idx_audit_entity_id ON document_template_snippet_audit(entity_id);

-- changeset auto.generated:1825492372-110
CREATE INDEX idx_audit_entity_id ON document_template_variable_audit(entity_id);

-- changeset auto.generated:1825492372-111
CREATE INDEX idx_audit_entity_id ON document_template_version_audit(entity_id);

-- changeset auto.generated:1825492372-112
CREATE INDEX idx_audit_entity_id ON document_theme_audit(entity_id);

-- changeset auto.generated:1825492372-113
CREATE INDEX idx_audit_entity_id ON notification_message_audit(entity_id);

-- changeset auto.generated:1825492372-114
CREATE INDEX idx_audit_entity_id ON notification_smtp_outbound_config_audit(entity_id);

-- changeset auto.generated:1825492372-115
CREATE INDEX idx_audit_timestamp ON access_api_key_client_audit(timestamp DESC);

-- changeset auto.generated:1825492372-116
CREATE INDEX idx_audit_timestamp ON access_relying_party_audit(timestamp DESC);

-- changeset auto.generated:1825492372-117
CREATE INDEX idx_audit_timestamp ON access_role_audit(timestamp DESC);

-- changeset auto.generated:1825492372-118
CREATE INDEX idx_audit_timestamp ON access_tenant_audit(timestamp DESC);

-- changeset auto.generated:1825492372-119
CREATE INDEX idx_audit_timestamp ON access_tenant_config_audit(timestamp DESC);

-- changeset auto.generated:1825492372-120
CREATE INDEX idx_audit_timestamp ON access_tenant_login_provider_audit(timestamp DESC);

-- changeset auto.generated:1825492372-121
CREATE INDEX idx_audit_timestamp ON access_tenant_terms_of_use_audit(timestamp DESC);

-- changeset auto.generated:1825492372-122
CREATE INDEX idx_audit_timestamp ON access_trusted_client_audit(timestamp DESC);

-- changeset auto.generated:1825492372-123
CREATE INDEX idx_audit_timestamp ON access_user_audit(timestamp DESC);

-- changeset auto.generated:1825492372-124
CREATE INDEX idx_audit_timestamp ON access_user_group_membership_audit(timestamp DESC);

-- changeset auto.generated:1825492372-125
CREATE INDEX idx_audit_timestamp ON access_user_role_assignament_audit(timestamp DESC);

-- changeset auto.generated:1825492372-126
CREATE INDEX idx_audit_timestamp ON document_template_asset_audit(timestamp DESC);

-- changeset auto.generated:1825492372-127
CREATE INDEX idx_audit_timestamp ON document_template_audit(timestamp DESC);

-- changeset auto.generated:1825492372-128
CREATE INDEX idx_audit_timestamp ON document_template_snippet_audit(timestamp DESC);

-- changeset auto.generated:1825492372-129
CREATE INDEX idx_audit_timestamp ON document_template_variable_audit(timestamp DESC);

-- changeset auto.generated:1825492372-130
CREATE INDEX idx_audit_timestamp ON document_template_version_audit(timestamp DESC);

-- changeset auto.generated:1825492372-131
CREATE INDEX idx_audit_timestamp ON document_theme_audit(timestamp DESC);

-- changeset auto.generated:1825492372-132
CREATE INDEX idx_audit_timestamp ON notification_message_audit(timestamp DESC);

-- changeset auto.generated:1825492372-133
CREATE INDEX idx_audit_timestamp ON notification_smtp_outbound_config_audit(timestamp DESC);

-- changeset auto.generated:1825492372-134
CREATE INDEX idx_audit_user ON access_api_key_client_audit(performed_by);

-- changeset auto.generated:1825492372-135
CREATE INDEX idx_audit_user ON access_relying_party_audit(performed_by);

-- changeset auto.generated:1825492372-136
CREATE INDEX idx_audit_user ON access_role_audit(performed_by);

-- changeset auto.generated:1825492372-137
CREATE INDEX idx_audit_user ON access_tenant_audit(performed_by);

-- changeset auto.generated:1825492372-138
CREATE INDEX idx_audit_user ON access_tenant_config_audit(performed_by);

-- changeset auto.generated:1825492372-139
CREATE INDEX idx_audit_user ON access_tenant_login_provider_audit(performed_by);

-- changeset auto.generated:1825492372-140
CREATE INDEX idx_audit_user ON access_tenant_terms_of_use_audit(performed_by);

-- changeset auto.generated:1825492372-141
CREATE INDEX idx_audit_user ON access_trusted_client_audit(performed_by);

-- changeset auto.generated:1825492372-142
CREATE INDEX idx_audit_user ON access_user_audit(performed_by);

-- changeset auto.generated:1825492372-143
CREATE INDEX idx_audit_user ON access_user_group_membership_audit(performed_by);

-- changeset auto.generated:1825492372-144
CREATE INDEX idx_audit_user ON access_user_role_assignament_audit(performed_by);

-- changeset auto.generated:1825492372-145
CREATE INDEX idx_audit_user ON document_template_asset_audit(performed_by);

-- changeset auto.generated:1825492372-146
CREATE INDEX idx_audit_user ON document_template_audit(performed_by);

-- changeset auto.generated:1825492372-147
CREATE INDEX idx_audit_user ON document_template_snippet_audit(performed_by);

-- changeset auto.generated:1825492372-148
CREATE INDEX idx_audit_user ON document_template_variable_audit(performed_by);

-- changeset auto.generated:1825492372-149
CREATE INDEX idx_audit_user ON document_template_version_audit(performed_by);

-- changeset auto.generated:1825492372-150
CREATE INDEX idx_audit_user ON document_theme_audit(performed_by);

-- changeset auto.generated:1825492372-151
CREATE INDEX idx_audit_user ON notification_message_audit(performed_by);

-- changeset auto.generated:1825492372-152
CREATE INDEX idx_audit_user ON notification_smtp_outbound_config_audit(performed_by);

-- changeset auto.generated:1825492372-153
CREATE INDEX idx_long_tasks_code_actor ON _long_tasks(code, actor);

-- changeset auto.generated:1825492372-154
ALTER TABLE access_role ADD CONSTRAINT FK_ACCESS_ROLE_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-155
ALTER TABLE access_tenant_config ADD CONSTRAINT FK_ACCESS_TENANT_CONFIG_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-156
ALTER TABLE access_tenant_login_provider ADD CONSTRAINT FK_ACCESS_TENANT_LOGIN_PROVIDER_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-157
ALTER TABLE access_tenant_terms_of_use ADD CONSTRAINT FK_ACCESS_TENANT_TERMS_OF_USE_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-158
ALTER TABLE access_tenant_terms_of_use ADD CONSTRAINT FK_ACCESS_TENANT_TERMS_OF_USE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-159
ALTER TABLE access_trusted_client_allowed_redirect ADD CONSTRAINT FK_ACCESS_TRUSTED_CLIENT_ALLOWED_REDIRECT_CLIENT FOREIGN KEY (client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-160
ALTER TABLE access_user_accepted_termns_of_use ADD CONSTRAINT FK_ACCESS_USER_ACCEPTED_TERMNS_OF_USE_CONDITIONS FOREIGN KEY (conditions) REFERENCES access_tenant_terms_of_use (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-161
ALTER TABLE access_user_accepted_termns_of_use ADD CONSTRAINT FK_ACCESS_USER_ACCEPTED_TERMNS_OF_USE_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-162
ALTER TABLE access_user_access_temporal_code ADD CONSTRAINT FK_ACCESS_USER_ACCESS_TEMPORAL_CODE_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-163
ALTER TABLE access_user_group_membership ADD CONSTRAINT FK_ACCESS_USER_GROUP_MEMBERSHIP_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-164
ALTER TABLE access_user_group_membership ADD CONSTRAINT FK_ACCESS_USER_GROUP_MEMBERSHIP_TRUSTED_CLIENT FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-165
ALTER TABLE access_user_group_membership ADD CONSTRAINT FK_ACCESS_USER_GROUP_MEMBERSHIP_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-166
ALTER TABLE access_user_role_assignament ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNAMENT_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-167
ALTER TABLE access_user_role_assignament_role ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNAMENT_ROLE_ROLE FOREIGN KEY (`role`) REFERENCES access_role (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-168
ALTER TABLE access_user_role_assignament_role ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNAMENT_ROLE_USER_ROLE_ASSIGNAMENT FOREIGN KEY (user_role_assignament) REFERENCES access_user_role_assignament (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-169
ALTER TABLE access_user_role_assignament ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNAMENT_TRUSTED_CLIENT FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-170
ALTER TABLE access_user_role_assignament ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNAMENT_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-171
ALTER TABLE access_user ADD CONSTRAINT FK_ACCESS_USER_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-172
ALTER TABLE document_template_asset ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_ASSET_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-173
ALTER TABLE document_template_snippet ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_SNIPPET_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-174
ALTER TABLE document_template ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-175
ALTER TABLE document_template ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_THEME FOREIGN KEY (theme) REFERENCES document_theme (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-176
ALTER TABLE document_template_variable ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_VARIABLE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-177
ALTER TABLE document_template_version ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_VERSION_TEMPLATE FOREIGN KEY (template) REFERENCES document_template (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-178
ALTER TABLE document_theme ADD CONSTRAINT FK_DOCUMENT_THEME_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-179
ALTER TABLE notification_message ADD CONSTRAINT FK_NOTIFICATION_MESSAGE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-180
ALTER TABLE notification_smtp_outbound_config ADD CONSTRAINT FK_NOTIFICATION_SMTP_OUTBOUND_CONFIG_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

