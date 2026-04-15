-- liquibase formatted sql

-- changeset phylax-dev:idx-oauth-temporal-codes-expiration
CREATE INDEX idx_oauth_temporal_codes_expiration ON _oauth_temporal_codes (expiration);

-- changeset phylax-dev:idx-oauth-sessions-expiration
CREATE INDEX idx_oauth_sessions_expiration ON _oauth_sessions (expiration);
