-- liquibase formatted sql

-- changeset phylax:oauth-session-grant-1
CREATE TABLE _oauth_session_grant (
  id VARCHAR(36) NOT NULL,
  session VARCHAR(255) NOT NULL,
  client_id VARCHAR(250) NOT NULL,
  grant_type VARCHAR(50) NOT NULL,
  scope TEXT NULL,
  audiences TEXT NOT NULL,
  auth_data TEXT NOT NULL,
  created_at datetime NOT NULL,
  updated_at datetime NOT NULL,
  revoked_at datetime DEFAULT NULL NULL,
  CONSTRAINT PK__OAUTH_SESSION_GRANT PRIMARY KEY (id)
);

-- changeset phylax:oauth-session-grant-2
ALTER TABLE _oauth_session_grant ADD CONSTRAINT FK__OAUTH_SESSION_GRANT_SESSION
  FOREIGN KEY (session) REFERENCES _oauth_session (session)
  ON UPDATE CASCADE ON DELETE CASCADE;

-- changeset phylax:oauth-session-token-grant-cols
ALTER TABLE _oauth_session_token
  ADD COLUMN grant_id VARCHAR(36) NULL,
  ADD COLUMN client_id VARCHAR(250) NULL,
  ADD COLUMN scope TEXT NULL,
  ADD COLUMN audiences TEXT NULL,
  ADD COLUMN auth_data TEXT NULL;

-- changeset phylax:oauth-session-token-grant-fk
ALTER TABLE _oauth_session_token ADD CONSTRAINT FK__OAUTH_SESSION_TOKEN_GRANT
  FOREIGN KEY (grant_id) REFERENCES _oauth_session_grant (id)
  ON DELETE SET NULL;
