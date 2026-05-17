-- liquibase formatted sql

-- changeset phylax:20260517000002-1
ALTER TABLE _oauth_session ADD COLUMN user_uid VARCHAR(36) NULL;

-- changeset phylax:20260517000002-2
CREATE INDEX IF NOT EXISTS idx_oauth_session_user_uid ON _oauth_session (user_uid);
