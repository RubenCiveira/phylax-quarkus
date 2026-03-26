-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE notification_smtp_outbound_config (uid VARCHAR(255) NOT NULL, version INT NOT NULL, host VARCHAR(255) NOT NULL, login VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, port BIT NOT NULL, sender_email VARCHAR(255) NOT NULL, sender_name VARCHAR(255) NULL, timeout INT NOT NULL, use_tls BIT NOT NULL, tenant VARCHAR(255) NULL, CONSTRAINT PK_NOTIFICATION_SMTP_OUTBOUND_CONFIG PRIMARY KEY (uid), UNIQUE (tenant));

-- changeset auto.generated:1825492372-2
CREATE TABLE notification_smtp_outbound_config_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_NOTIFICATION_SMTP_OUTBOUND_CONFIG_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-3
CREATE INDEX idx_audit_entity_id ON notification_smtp_outbound_config_audit(entity_id);

-- changeset auto.generated:1825492372-4
CREATE INDEX idx_audit_timestamp ON notification_smtp_outbound_config_audit(timestamp DESC);

-- changeset auto.generated:1825492372-5
CREATE INDEX idx_audit_user ON notification_smtp_outbound_config_audit(performed_by);

-- changeset auto.generated:1825492372-6
ALTER TABLE notification_smtp_outbound_config ADD CONSTRAINT FK_NOTIFICATION_SMTP_OUTBOUND_CONFIG_TENANT FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-7
ALTER TABLE notification_smtp_outbound_config ADD COLUMN max_retries INT NOT NULL DEFAULT 1;

-- changeset auto.generated:1825492372-8
ALTER TABLE notification_smtp_outbound_config ADD COLUMN retry_delay INT NOT NULL DEFAULT 30;

-- changeset auto.generated:1825492372-9
ALTER TABLE notification_smtp_outbound_config ADD COLUMN rate_limit INT NOT NULL DEFAULT 0;

