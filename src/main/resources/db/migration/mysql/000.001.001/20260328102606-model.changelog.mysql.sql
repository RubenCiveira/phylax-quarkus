-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
ALTER TABLE notification_message ADD lock_at timestamp DEFAULT null NULL;

