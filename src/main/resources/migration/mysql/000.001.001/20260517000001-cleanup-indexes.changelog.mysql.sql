-- liquibase formatted sql

-- changeset phylax:20260517000001-1
CREATE INDEX IF NOT EXISTS idx_oauth_session_expiration ON _oauth_session (expiration);

-- changeset phylax:20260517000001-2
CREATE INDEX IF NOT EXISTS idx_oauth_temporal_codes_expiration ON _oauth_temporal_codes (expiration);
