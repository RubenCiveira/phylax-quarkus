-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
ALTER TABLE notification_smtp_outbound_config ADD max_retries INT NOT NULL;

-- changeset auto.generated:1825492372-2
ALTER TABLE notification_smtp_outbound_config ADD rate_limit INT NOT NULL;

-- changeset auto.generated:1825492372-3
ALTER TABLE notification_smtp_outbound_config ADD retry_delay INT NOT NULL;

