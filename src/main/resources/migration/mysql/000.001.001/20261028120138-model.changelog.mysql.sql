-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE _audit_access_oauth_client (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_OAUTH_CLIENT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-2
CREATE TABLE _audit_access_resource_server (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_ACCESS_RESOURCE_SERVER PRIMARY KEY (id));

-- changeset auto.generated:1825492372-3
CREATE TABLE access_oauth_client (uid VARCHAR(255) NOT NULL, version INT NOT NULL, allow_all_scopes BIT DEFAULT 0 NULL, authorization_details_types LONGTEXT NULL, authorization_signed_response_alg VARCHAR(255) NULL, backchannel_client_notification_endpoint VARCHAR(255) NULL, backchannel_logout_session_required BIT DEFAULT 0 NULL, backchannel_logout_uri VARCHAR(255) NULL, backchannel_token_delivery_mode VARCHAR(255) NULL, client_credentials_scopes LONGTEXT NULL, client_credentials_token_ttl_seconds INT NOT NULL, client_name VARCHAR(255) NULL, client_uri VARCHAR(255) NULL, code VARCHAR(255) NOT NULL, default_acr_values VARCHAR(255) NULL, default_max_age INT DEFAULT NULL NULL, dpop_bound_access_tokens BIT NOT NULL, dynamically_registered BIT DEFAULT 0 NULL, enabled BIT NOT NULL, frontchannel_logout_session_required BIT DEFAULT 0 NULL, frontchannel_logout_uri VARCHAR(255) NULL, grant_types_json LONGTEXT NULL, id_token_encrypted_response_alg VARCHAR(255) NULL, id_token_encrypted_response_enc VARCHAR(255) NULL, id_token_signed_response_alg VARCHAR(255) NULL, introspection_allowed BIT NOT NULL, jwks_json LONGTEXT NULL, jwks_uri LONGTEXT NULL, logo_uri VARCHAR(255) NULL, policy_uri VARCHAR(255) NULL, registered_at timestamp DEFAULT NULL NULL, registration_access_token VARCHAR(255) NULL, request_object_signing_alg VARCHAR(255) NULL, require_pkce BIT NOT NULL, require_pushed_authorization_requests BIT NOT NULL, response_types_json LONGTEXT NULL, secret_oauth VARCHAR(255) NULL, sector_identifier_uri VARCHAR(255) NULL, subject_type VARCHAR(255) NOT NULL, tls_client_auth_subject_dn VARCHAR(255) NULL, tls_client_certificate_bound_access_tokens BIT NOT NULL, token_endpoint_auth_method VARCHAR(255) NOT NULL, token_endpoint_auth_signing_alg VARCHAR(255) NULL, tos_uri VARCHAR(255) NULL, userinfo_encrypted_response_alg VARCHAR(255) NULL, userinfo_encrypted_response_enc VARCHAR(255) NULL, userinfo_signed_response_alg VARCHAR(255) NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_ACCESS_OAUTH_CLIENT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-4
CREATE TABLE access_oauth_client_allowed_audience (uid VARCHAR(255) NOT NULL, version INT NOT NULL, audience VARCHAR(255) NOT NULL, client VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_OAUTH_CLIENT_ALLOWED_AUDIENCE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-5
CREATE TABLE access_oauth_client_allowed_login_redirect (uid VARCHAR(255) NOT NULL, version INT NOT NULL, url VARCHAR(255) NOT NULL, client VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_OAUTH_CLIENT_ALLOWED_LOGIN_REDIRECT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-6
CREATE TABLE access_oauth_client_allowed_logout_redirect (uid VARCHAR(255) NOT NULL, version INT NOT NULL, url VARCHAR(255) NOT NULL, client VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_OAUTH_CLIENT_ALLOWED_LOGOUT_REDIRECT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-7
CREATE TABLE access_resource_server (uid VARCHAR(255) NOT NULL, version INT NOT NULL, api_key VARCHAR(255) NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, CONSTRAINT PK_ACCESS_RESOURCE_SERVER PRIMARY KEY (uid), UNIQUE (api_key), UNIQUE (code));

-- changeset auto.generated:1825492372-8
CREATE TABLE access_user_login_throttle (uid VARCHAR(255) NOT NULL, version INT NOT NULL, failed_login_attempts INT DEFAULT NULL NULL, last_failed_at timestamp DEFAULT NULL NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_LOGIN_THROTTLE PRIMARY KEY (uid), UNIQUE (user));

-- changeset auto.generated:1825492372-9
CREATE TABLE access_user_mfa_enrollment (uid VARCHAR(255) NOT NULL, version INT NOT NULL, expired_at timestamp DEFAULT NULL NULL, pending_seed VARCHAR(255) NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_MFA_ENROLLMENT PRIMARY KEY (uid), UNIQUE (user));

-- changeset auto.generated:1825492372-10
CREATE TABLE access_user_purpose_consent (uid VARCHAR(255) NOT NULL, version INT NOT NULL, decision_at timestamp DEFAULT NULL NULL, granted BIT NOT NULL, ip_address VARCHAR(255) NULL, user_agent VARCHAR(255) NULL, consent_purpose VARCHAR(255) NOT NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_PURPOSE_CONSENT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-11
CREATE TABLE access_user_scope_consent (uid VARCHAR(255) NOT NULL, version INT NOT NULL, decision_at timestamp DEFAULT NULL NULL, granted BIT DEFAULT 0 NULL, ip_address VARCHAR(255) NULL, scope VARCHAR(255) NULL, user_agent VARCHAR(255) NULL, oauth_client VARCHAR(255) NOT NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_SCOPE_CONSENT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-12
CREATE TABLE access_user_verification_code (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code_hash VARCHAR(255) NULL, expires_at timestamp DEFAULT NULL NULL, purpose VARCHAR(255) NULL, return_url LONGTEXT NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_VERIFICATION_CODE PRIMARY KEY (uid), UNIQUE (code_hash));

-- changeset auto.generated:1825492372-13
ALTER TABLE access_user ADD approval_status VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-14
ALTER TABLE access_user_role_assignment ADD oauth_client VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-15
ALTER TABLE access_tenant_terms_of_use ADD document VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-16
ALTER TABLE access_user_group_membership ADD oauth_client VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-17
ALTER TABLE access_role ADD resource_server VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-18
ALTER TABLE access_user_role_assignment ADD resource_server VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-19
ALTER TABLE access_tenant_config ADD auto_approve_registered_users BIT DEFAULT 0 NULL;

-- changeset auto.generated:1825492372-20
ALTER TABLE access_user_group_membership ADD resource_server VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-21
ALTER TABLE access_tenant_login_provider ADD enabled BIT DEFAULT 0 NULL;

-- changeset auto.generated:1825492372-22
ALTER TABLE access_user_accepted_terms_of_use ADD terms_of_use VARCHAR(255) NOT NULL;

-- changeset auto.generated:1825492372-23
ALTER TABLE access_tenant_terms_of_use ADD resource_server VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-24
ALTER TABLE access_tenant_config ADD disabled_user_email_template LONGTEXT NULL;

-- changeset auto.generated:1825492372-25
ALTER TABLE access_user ADD federated_provider VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-26
ALTER TABLE access_tenant_config ADD display_name VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-27
ALTER TABLE access_tenant_config ADD enabled_user_email_template LONGTEXT NULL;

-- changeset auto.generated:1825492372-28
ALTER TABLE access_tenant_login_provider ADD provider_type VARCHAR(255) NOT NULL;

-- changeset auto.generated:1825492372-29
ALTER TABLE access_tenant_config ADD password_recovery_enabled BIT DEFAULT 0 NULL;

-- changeset auto.generated:1825492372-30
ALTER TABLE access_tenant_config ADD recover_pass_email_template LONGTEXT NULL;

-- changeset auto.generated:1825492372-31
ALTER TABLE access_tenant_config ADD registered_email_template LONGTEXT NULL;

-- changeset auto.generated:1825492372-32
ALTER TABLE access_tenant_config ADD self_registration_enabled BIT DEFAULT 0 NULL;

-- changeset auto.generated:1825492372-33
ALTER TABLE access_tenant_config ADD welcome_email_template LONGTEXT NULL;

-- changeset auto.generated:1825492372-34
CREATE INDEX FL_OAUTH_CLIENT_ALLOWED_AUDIENCE_AUDIENCE ON access_oauth_client_allowed_audience(audience);

-- changeset auto.generated:1825492372-35
CREATE INDEX FL_OAUTH_CLIENT_ALLOWED_AUDIENCE_CLIENT ON access_oauth_client_allowed_audience(client);

-- changeset auto.generated:1825492372-36
CREATE INDEX FL_OAUTH_CLIENT_ALLOWED_LOGIN_REDIRECT_CLIENTS ON access_oauth_client_allowed_login_redirect(client);

-- changeset auto.generated:1825492372-37
CREATE INDEX FL_OAUTH_CLIENT_ALLOWED_LOGOUT_REDIRECT_CLIENTS ON access_oauth_client_allowed_logout_redirect(client);

-- changeset auto.generated:1825492372-38
CREATE INDEX FL_OAUTH_CLIENT_TENANT ON access_oauth_client(tenant);

-- changeset auto.generated:1825492372-39
CREATE INDEX FL_ROLE_RESOURCE_SERVERS ON access_role(resource_server);

-- changeset auto.generated:1825492372-40
CREATE INDEX FL_TENANT_TERMS_OF_USE_RESOURCE_SERVERS ON access_tenant_terms_of_use(resource_server);

-- changeset auto.generated:1825492372-41
CREATE INDEX FL_USER_ACCEPTED_TERMS_OF_USE_TERMS_OF_USE ON access_user_accepted_terms_of_use(terms_of_use);

-- changeset auto.generated:1825492372-42
CREATE INDEX FL_USER_GROUP_MEMBERSHIP_OAUTH_CLIENTS ON access_user_group_membership(oauth_client);

-- changeset auto.generated:1825492372-43
CREATE INDEX FL_USER_GROUP_MEMBERSHIP_RESOURCE_SERVER ON access_user_group_membership(resource_server);

-- changeset auto.generated:1825492372-44
CREATE INDEX FL_USER_PURPOSE_CONSENT_CONSENT_PURPOSE ON access_user_purpose_consent(consent_purpose);

-- changeset auto.generated:1825492372-45
CREATE INDEX FL_USER_PURPOSE_CONSENT_USER ON access_user_purpose_consent(user);

-- changeset auto.generated:1825492372-46
CREATE INDEX FL_USER_ROLE_ASSIGNMENT_OAUTH_CLIENTS ON access_user_role_assignment(oauth_client);

-- changeset auto.generated:1825492372-47
CREATE INDEX FL_USER_ROLE_ASSIGNMENT_RESOURCE_SERVERS ON access_user_role_assignment(resource_server);

-- changeset auto.generated:1825492372-48
CREATE INDEX FL_USER_SCOPE_CONSENT_OAUTH_CLIENT ON access_user_scope_consent(oauth_client);

-- changeset auto.generated:1825492372-49
CREATE INDEX FL_USER_SCOPE_CONSENT_USER ON access_user_scope_consent(user);

-- changeset auto.generated:1825492372-50
CREATE INDEX FL_USER_VERIFICATION_CODE_USERS ON access_user_verification_code(user);

-- changeset auto.generated:1825492372-51
CREATE INDEX ST_OAUTH_CLIENT_ALLOWED_AUDIENCE_AUDIENCE_DESC ON access_oauth_client_allowed_audience(audience DESC);

-- changeset auto.generated:1825492372-52
CREATE INDEX ST_OAUTH_CLIENT_CODE_ASC ON access_oauth_client(code);

-- changeset auto.generated:1825492372-53
CREATE INDEX ST_OAUTH_CLIENT_CODE_DESC ON access_oauth_client(code DESC);

-- changeset auto.generated:1825492372-54
CREATE UNIQUE INDEX UK_OAUTH_CLIENT_ALLOWED_AUDIENCE_CLIENT_AUDIENCE ON access_oauth_client_allowed_audience(client, audience);

-- changeset auto.generated:1825492372-55
CREATE UNIQUE INDEX UK_OAUTH_CLIENT_TENANT_CODE ON access_oauth_client(tenant, code);

-- changeset auto.generated:1825492372-56
CREATE UNIQUE INDEX UK_ROLE_RESOURCE_SERVER_NAME ON access_role(resource_server, name);

-- changeset auto.generated:1825492372-57
CREATE UNIQUE INDEX UK_USER_ACCEPTED_TERMS_OF_USE_USER_TERMS_OF_USE ON access_user_accepted_terms_of_use(user, terms_of_use);

-- changeset auto.generated:1825492372-58
CREATE UNIQUE INDEX UK_USER_VERIFICATION_CODE_USER_PURPOSE ON access_user_verification_code(user, purpose);

-- changeset auto.generated:1825492372-59
CREATE INDEX idx_audit_entity_id ON _audit_access_oauth_client(entity_id);

-- changeset auto.generated:1825492372-60
CREATE INDEX idx_audit_entity_id ON _audit_access_resource_server(entity_id);

-- changeset auto.generated:1825492372-61
CREATE INDEX idx_audit_timestamp ON _audit_access_oauth_client(timestamp DESC);

-- changeset auto.generated:1825492372-62
CREATE INDEX idx_audit_timestamp ON _audit_access_resource_server(timestamp DESC);

-- changeset auto.generated:1825492372-63
CREATE INDEX idx_audit_user ON _audit_access_oauth_client(performed_by);

-- changeset auto.generated:1825492372-64
CREATE INDEX idx_audit_user ON _audit_access_resource_server(performed_by);

-- changeset auto.generated:1825492372-65
ALTER TABLE access_oauth_client_allowed_audience ADD CONSTRAINT FK_ACCESS_OAUTH_CLIENT_ALLOWED_AUDIENCE_CLIENT FOREIGN KEY (client) REFERENCES access_oauth_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-66
ALTER TABLE access_oauth_client_allowed_login_redirect ADD CONSTRAINT FK_ACCESS_OAUTH_CLIENT_ALLOWED_LOGIN_REDIRECT_CLIENT FOREIGN KEY (client) REFERENCES access_oauth_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-67
ALTER TABLE access_oauth_client_allowed_logout_redirect ADD CONSTRAINT FK_ACCESS_OAUTH_CLIENT_ALLOWED_LOGOUT_REDIRECT_CLIENT FOREIGN KEY (client) REFERENCES access_oauth_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-68
ALTER TABLE access_oauth_client ADD CONSTRAINT FK_ACCESS_OAUTH_CLIENT_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-69
ALTER TABLE access_role ADD CONSTRAINT FK_ACCESS_ROLE_RESOURCE_SERVER FOREIGN KEY (resource_server) REFERENCES access_resource_server (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-70
ALTER TABLE access_tenant_terms_of_use ADD CONSTRAINT FK_ACCESS_TENANT_TERMS_OF_USE_RESOURCE_SERVER FOREIGN KEY (resource_server) REFERENCES access_resource_server (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-71
ALTER TABLE access_user_accepted_terms_of_use ADD CONSTRAINT FK_ACCESS_USER_ACCEPTED_TERMS_OF_USE_TERMS_OF_USE FOREIGN KEY (terms_of_use) REFERENCES access_tenant_terms_of_use (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-72
ALTER TABLE access_user_group_membership ADD CONSTRAINT FK_ACCESS_USER_GROUP_MEMBERSHIP_OAUTH_CLIENT FOREIGN KEY (oauth_client) REFERENCES access_oauth_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-73
ALTER TABLE access_user_group_membership ADD CONSTRAINT FK_ACCESS_USER_GROUP_MEMBERSHIP_RESOURCE_SERVER FOREIGN KEY (resource_server) REFERENCES access_resource_server (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-74
ALTER TABLE access_user_login_throttle ADD CONSTRAINT FK_ACCESS_USER_LOGIN_THROTTLE_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-75
ALTER TABLE access_user_mfa_enrollment ADD CONSTRAINT FK_ACCESS_USER_MFA_ENROLLMENT_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-76
ALTER TABLE access_user_purpose_consent ADD CONSTRAINT FK_ACCESS_USER_PURPOSE_CONSENT_CONSENT_PURPOSE FOREIGN KEY (consent_purpose) REFERENCES access_consent_purpose (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-77
ALTER TABLE access_user_purpose_consent ADD CONSTRAINT FK_ACCESS_USER_PURPOSE_CONSENT_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-78
ALTER TABLE access_user_role_assignment ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNMENT_OAUTH_CLIENT FOREIGN KEY (oauth_client) REFERENCES access_oauth_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-79
ALTER TABLE access_user_role_assignment ADD CONSTRAINT FK_ACCESS_USER_ROLE_ASSIGNMENT_RESOURCE_SERVER FOREIGN KEY (resource_server) REFERENCES access_resource_server (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-80
ALTER TABLE access_user_scope_consent ADD CONSTRAINT FK_ACCESS_USER_SCOPE_CONSENT_OAUTH_CLIENT FOREIGN KEY (oauth_client) REFERENCES access_oauth_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-81
ALTER TABLE access_user_scope_consent ADD CONSTRAINT FK_ACCESS_USER_SCOPE_CONSENT_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-82
ALTER TABLE access_user_verification_code ADD CONSTRAINT FK_ACCESS_USER_VERIFICATION_CODE_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

