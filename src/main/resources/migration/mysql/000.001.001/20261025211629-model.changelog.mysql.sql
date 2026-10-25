-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE _oauth_replay_jti (jti VARCHAR(255) NOT NULL, tenant_id VARCHAR(36) NOT NULL, client_id VARCHAR(250) NOT NULL, purpose VARCHAR(32) NOT NULL, expires_at datetime NOT NULL, CONSTRAINT PK__OAUTH_REPLAY_JTI PRIMARY KEY (jti, tenant_id, client_id, purpose));

-- changeset auto.generated:1825492372-2
CREATE TABLE access_trusted_client_allowed_logout_redirect (uid VARCHAR(255) NOT NULL, version INT NOT NULL, url VARCHAR(255) NOT NULL, client VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_TRUSTED_CLIENT_ALLOWED_LOGOUT_REDIRECT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-3
ALTER TABLE _oauth_session ADD sid VARCHAR(64) NULL;

-- changeset auto.generated:1825492372-4
CREATE INDEX FL_TRUSTED_CLIENT_ALLOWED_LOGOUT_REDIRECT_CLIENTS ON access_trusted_client_allowed_logout_redirect(client);

-- changeset auto.generated:1825492372-5
CREATE INDEX IDX_OAUTH_REPLAY_JTI_EXPIRES ON _oauth_replay_jti(expires_at);

-- changeset auto.generated:1825492372-6
CREATE UNIQUE INDEX UK_OAUTH_SESSION_SID ON _oauth_session(sid);

-- changeset auto.generated:1825492372-7
ALTER TABLE access_trusted_client_allowed_logout_redirect ADD CONSTRAINT FK_ACCESS_TRUSTED_CLIENT_ALLOWED_LOGOUT_REDIRECT_CLIENT FOREIGN KEY (client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

