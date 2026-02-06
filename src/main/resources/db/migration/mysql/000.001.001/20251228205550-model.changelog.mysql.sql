-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE access_api_key_client_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_API_KEY_CLIENT_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-2
CREATE TABLE access_relying_party_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_RELYING_PARTY_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-3
CREATE TABLE access_role_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_ROLE_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-4
CREATE TABLE access_tenant_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_TENANT_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-5
CREATE TABLE access_tenant_config_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_TENANT_CONFIG_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-6
CREATE TABLE access_tenant_login_provider_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_TENANT_LOGIN_PROVIDER_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-7
CREATE TABLE access_tenant_terms_of_use_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_TENANT_TERMS_OF_USE_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-8
CREATE TABLE access_trusted_client_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_TRUSTED_CLIENT_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-9
CREATE TABLE access_user_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_USER_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-10
CREATE TABLE access_user_identity_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_USER_IDENTITY_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-11
CREATE INDEX idx_audit_entity_id ON access_api_key_client_audit(entity_id);

-- changeset auto.generated:1825492372-12
CREATE INDEX idx_audit_entity_id ON access_relying_party_audit(entity_id);

-- changeset auto.generated:1825492372-13
CREATE INDEX idx_audit_entity_id ON access_role_audit(entity_id);

-- changeset auto.generated:1825492372-14
CREATE INDEX idx_audit_entity_id ON access_tenant_audit(entity_id);

-- changeset auto.generated:1825492372-15
CREATE INDEX idx_audit_entity_id ON access_tenant_config_audit(entity_id);

-- changeset auto.generated:1825492372-16
CREATE INDEX idx_audit_entity_id ON access_tenant_login_provider_audit(entity_id);

-- changeset auto.generated:1825492372-17
CREATE INDEX idx_audit_entity_id ON access_tenant_terms_of_use_audit(entity_id);

-- changeset auto.generated:1825492372-18
CREATE INDEX idx_audit_entity_id ON access_trusted_client_audit(entity_id);

-- changeset auto.generated:1825492372-19
CREATE INDEX idx_audit_entity_id ON access_user_audit(entity_id);

-- changeset auto.generated:1825492372-20
CREATE INDEX idx_audit_entity_id ON access_user_identity_audit(entity_id);

-- changeset auto.generated:1825492372-21
CREATE INDEX idx_audit_timestamp ON access_api_key_client_audit(timestamp DESC);

-- changeset auto.generated:1825492372-22
CREATE INDEX idx_audit_timestamp ON access_relying_party_audit(timestamp DESC);

-- changeset auto.generated:1825492372-23
CREATE INDEX idx_audit_timestamp ON access_role_audit(timestamp DESC);

-- changeset auto.generated:1825492372-24
CREATE INDEX idx_audit_timestamp ON access_tenant_audit(timestamp DESC);

-- changeset auto.generated:1825492372-25
CREATE INDEX idx_audit_timestamp ON access_tenant_config_audit(timestamp DESC);

-- changeset auto.generated:1825492372-26
CREATE INDEX idx_audit_timestamp ON access_tenant_login_provider_audit(timestamp DESC);

-- changeset auto.generated:1825492372-27
CREATE INDEX idx_audit_timestamp ON access_tenant_terms_of_use_audit(timestamp DESC);

-- changeset auto.generated:1825492372-28
CREATE INDEX idx_audit_timestamp ON access_trusted_client_audit(timestamp DESC);

-- changeset auto.generated:1825492372-29
CREATE INDEX idx_audit_timestamp ON access_user_audit(timestamp DESC);

-- changeset auto.generated:1825492372-30
CREATE INDEX idx_audit_timestamp ON access_user_identity_audit(timestamp DESC);

-- changeset auto.generated:1825492372-31
CREATE INDEX idx_audit_user ON access_api_key_client_audit(performed_by);

-- changeset auto.generated:1825492372-32
CREATE INDEX idx_audit_user ON access_relying_party_audit(performed_by);

-- changeset auto.generated:1825492372-33
CREATE INDEX idx_audit_user ON access_role_audit(performed_by);

-- changeset auto.generated:1825492372-34
CREATE INDEX idx_audit_user ON access_tenant_audit(performed_by);

-- changeset auto.generated:1825492372-35
CREATE INDEX idx_audit_user ON access_tenant_config_audit(performed_by);

-- changeset auto.generated:1825492372-36
CREATE INDEX idx_audit_user ON access_tenant_login_provider_audit(performed_by);

-- changeset auto.generated:1825492372-37
CREATE INDEX idx_audit_user ON access_tenant_terms_of_use_audit(performed_by);

-- changeset auto.generated:1825492372-38
CREATE INDEX idx_audit_user ON access_trusted_client_audit(performed_by);

-- changeset auto.generated:1825492372-39
CREATE INDEX idx_audit_user ON access_user_audit(performed_by);

-- changeset auto.generated:1825492372-40
CREATE INDEX idx_audit_user ON access_user_identity_audit(performed_by);

