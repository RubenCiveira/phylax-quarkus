-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE _audit_events (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, entity_type VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_EVENTS PRIMARY KEY (id));

-- changeset auto.generated:1825492372-2
CREATE INDEX idx_audit_entity ON _audit_events(entity_type, entity_id);

-- changeset auto.generated:1825492372-3
CREATE INDEX idx_audit_timestamp ON _audit_events(timestamp DESC);

-- changeset auto.generated:1825492372-4
CREATE INDEX idx_audit_user ON _audit_events(performed_by);

