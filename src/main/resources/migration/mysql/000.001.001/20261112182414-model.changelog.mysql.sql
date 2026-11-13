-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
ALTER TABLE observe_observability_config ADD retention_hours_debug INT NOT NULL;

-- changeset auto.generated:1825492372-2
ALTER TABLE observe_observability_config ADD retention_hours_error INT NOT NULL;

-- changeset auto.generated:1825492372-3
ALTER TABLE observe_observability_config ADD retention_hours_info INT NOT NULL;

-- changeset auto.generated:1825492372-4
ALTER TABLE observe_observability_config ADD retention_hours_warn INT NOT NULL;

