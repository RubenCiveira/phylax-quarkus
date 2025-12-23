-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
ALTER TABLE _audit_events ADD trace_id VARCHAR(100) NOT NULL;

-- changeset auto.generated:1825492372-2
ALTER TABLE _audit_events ADD span_id VARCHAR(100) NOT NULL;

