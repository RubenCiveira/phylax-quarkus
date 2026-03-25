-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE document_template (uid VARCHAR(255) NOT NULL, version INT NOT NULL, channel VARCHAR(255) NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, tenant VARCHAR(255) NULL, theme VARCHAR(255) NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-2
CREATE TABLE document_template_asset (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, content VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, type VARCHAR(255) NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_ASSET PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-3
CREATE TABLE document_template_asset_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_ASSET_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-4
CREATE TABLE document_template_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-5
CREATE TABLE document_template_snippet (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, content_html VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_SNIPPET PRIMARY KEY (uid), UNIQUE (code));

-- changeset auto.generated:1825492372-6
CREATE TABLE document_template_snippet_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_SNIPPET_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-7
CREATE TABLE document_template_variable (uid VARCHAR(255) NOT NULL, version INT NOT NULL, code VARCHAR(255) NOT NULL, enabled BIT DEFAULT 0 NULL, type VARCHAR(255) NOT NULL, value VARCHAR(255) NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_VARIABLE PRIMARY KEY (uid), UNIQUE (code));

-- changeset auto.generated:1825492372-8
CREATE TABLE document_template_variable_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_VARIABLE_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-9
CREATE TABLE document_template_version (uid VARCHAR(255) NOT NULL, version INT NOT NULL, content_html VARCHAR(255) NOT NULL, content_text VARCHAR(255) NULL, subject VARCHAR(255) NULL, template VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_VERSION PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-10
CREATE TABLE document_template_version_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_TEMPLATE_VERSION_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-11
CREATE TABLE document_theme (uid VARCHAR(255) NOT NULL, version INT NOT NULL, custom_css VARCHAR(255) NULL, enabled BIT DEFAULT 0 NULL, is_default BIT DEFAULT 0 NULL, name VARCHAR(255) NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT PK_DOCUMENT_THEME PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-12
CREATE TABLE document_theme_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_DOCUMENT_THEME_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-13
CREATE TABLE notification_message (uid VARCHAR(255) NOT NULL, version INT NOT NULL, content VARCHAR(255) NOT NULL, created_at timestamp NOT NULL, retries INT NOT NULL, send_at timestamp DEFAULT NULL NULL, target VARCHAR(255) NOT NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_NOTIFICATION_MESSAGE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-14
CREATE TABLE notification_message_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_NOTIFICATION_MESSAGE_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-15
CREATE INDEX FL_MESSAGE_TENANT ON notification_message(tenant);

-- changeset auto.generated:1825492372-16
CREATE INDEX FL_TEMPLATE_ASSET_CODE ON document_template_asset(code);

-- changeset auto.generated:1825492372-17
CREATE INDEX FL_TEMPLATE_ASSET_TENANT ON document_template_asset(tenant);

-- changeset auto.generated:1825492372-18
CREATE INDEX FL_TEMPLATE_CODE ON document_template(code);

-- changeset auto.generated:1825492372-19
CREATE INDEX FL_TEMPLATE_SNIPPET_TENANT ON document_template_snippet(tenant);

-- changeset auto.generated:1825492372-20
CREATE INDEX FL_TEMPLATE_TENANT ON document_template(tenant);

-- changeset auto.generated:1825492372-21
CREATE INDEX FL_TEMPLATE_THEMES ON document_template(theme);

-- changeset auto.generated:1825492372-22
CREATE INDEX FL_TEMPLATE_VARIABLE_TENANT ON document_template_variable(tenant);

-- changeset auto.generated:1825492372-23
CREATE INDEX FL_TEMPLATE_VERSION_TEMPLATES ON document_template_version(template);

-- changeset auto.generated:1825492372-24
CREATE INDEX FL_THEME_TENANTS ON document_theme(tenant);

-- changeset auto.generated:1825492372-25
CREATE INDEX ST_TEMPLATE_ASSET_CODE_DESC ON document_template_asset(code DESC);

-- changeset auto.generated:1825492372-26
CREATE INDEX ST_TEMPLATE_CODE_DESC ON document_template(code DESC);

-- changeset auto.generated:1825492372-27
CREATE INDEX ST_THEME_NAME_ASC ON document_theme(name);

-- changeset auto.generated:1825492372-28
CREATE INDEX ST_THEME_NAME_DESC ON document_theme(name DESC);

-- changeset auto.generated:1825492372-29
CREATE UNIQUE INDEX UK_TEMPLATE_ASSET_CODE_TENANT ON document_template_asset(code, tenant);

-- changeset auto.generated:1825492372-30
CREATE UNIQUE INDEX UK_TEMPLATE_CODE_TENANT ON document_template(code, tenant);

-- changeset auto.generated:1825492372-31
CREATE UNIQUE INDEX UK_TEMPLATE_SNIPPET_CODE_TENANT ON document_template_snippet(code, tenant);

-- changeset auto.generated:1825492372-32
CREATE UNIQUE INDEX UK_TEMPLATE_VARIABLE_CODE_TENANT ON document_template_variable(code, tenant);

-- changeset auto.generated:1825492372-33
CREATE INDEX idx_audit_entity_id ON document_template_asset_audit(entity_id);

-- changeset auto.generated:1825492372-34
CREATE INDEX idx_audit_entity_id ON document_template_audit(entity_id);

-- changeset auto.generated:1825492372-35
CREATE INDEX idx_audit_entity_id ON document_template_snippet_audit(entity_id);

-- changeset auto.generated:1825492372-36
CREATE INDEX idx_audit_entity_id ON document_template_variable_audit(entity_id);

-- changeset auto.generated:1825492372-37
CREATE INDEX idx_audit_entity_id ON document_template_version_audit(entity_id);

-- changeset auto.generated:1825492372-38
CREATE INDEX idx_audit_entity_id ON document_theme_audit(entity_id);

-- changeset auto.generated:1825492372-39
CREATE INDEX idx_audit_entity_id ON notification_message_audit(entity_id);

-- changeset auto.generated:1825492372-40
CREATE INDEX idx_audit_timestamp ON document_template_asset_audit(timestamp DESC);

-- changeset auto.generated:1825492372-41
CREATE INDEX idx_audit_timestamp ON document_template_audit(timestamp DESC);

-- changeset auto.generated:1825492372-42
CREATE INDEX idx_audit_timestamp ON document_template_snippet_audit(timestamp DESC);

-- changeset auto.generated:1825492372-43
CREATE INDEX idx_audit_timestamp ON document_template_variable_audit(timestamp DESC);

-- changeset auto.generated:1825492372-44
CREATE INDEX idx_audit_timestamp ON document_template_version_audit(timestamp DESC);

-- changeset auto.generated:1825492372-45
CREATE INDEX idx_audit_timestamp ON document_theme_audit(timestamp DESC);

-- changeset auto.generated:1825492372-46
CREATE INDEX idx_audit_timestamp ON notification_message_audit(timestamp DESC);

-- changeset auto.generated:1825492372-47
CREATE INDEX idx_audit_user ON document_template_asset_audit(performed_by);

-- changeset auto.generated:1825492372-48
CREATE INDEX idx_audit_user ON document_template_audit(performed_by);

-- changeset auto.generated:1825492372-49
CREATE INDEX idx_audit_user ON document_template_snippet_audit(performed_by);

-- changeset auto.generated:1825492372-50
CREATE INDEX idx_audit_user ON document_template_variable_audit(performed_by);

-- changeset auto.generated:1825492372-51
CREATE INDEX idx_audit_user ON document_template_version_audit(performed_by);

-- changeset auto.generated:1825492372-52
CREATE INDEX idx_audit_user ON document_theme_audit(performed_by);

-- changeset auto.generated:1825492372-53
CREATE INDEX idx_audit_user ON notification_message_audit(performed_by);

-- changeset auto.generated:1825492372-54
ALTER TABLE document_template_asset ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_ASSET_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-55
ALTER TABLE document_template_snippet ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_SNIPPET_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-56
ALTER TABLE document_template ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-57
ALTER TABLE document_template ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_THEME FOREIGN KEY (theme) REFERENCES document_theme (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-58
ALTER TABLE document_template_variable ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_VARIABLE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-59
ALTER TABLE document_template_version ADD CONSTRAINT FK_DOCUMENT_TEMPLATE_VERSION_TEMPLATE FOREIGN KEY (template) REFERENCES document_template (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-60
ALTER TABLE document_theme ADD CONSTRAINT FK_DOCUMENT_THEME_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-61
ALTER TABLE notification_message ADD CONSTRAINT FK_NOTIFICATION_MESSAGE_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

