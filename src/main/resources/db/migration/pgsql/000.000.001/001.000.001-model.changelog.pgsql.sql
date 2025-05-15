--liquibase formatted sql

--changeset auto.generated:1825492372-1
ALTER TABLE login_provider ADD users_enabled_by_default BOOLEAN NOT NULL;

--changeset auto.generated:1825492372-2
ALTER TABLE login_provider DROP COLUMN enabled_by_default;

