-- liquibase formatted sql

-- changeset phylax-dev:pkce-columns-temporal-codes
ALTER TABLE _oauth_temporal_codes
  ADD COLUMN code_challenge VARCHAR(128) NULL,
  ADD COLUMN code_challenge_method VARCHAR(10) NULL;
