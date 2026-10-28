-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE access_trusted_client_allowed_login_redirect (uid VARCHAR(255) NOT NULL, version INT NOT NULL, url VARCHAR(255) NOT NULL, client VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_TRUSTED_CLIENT_ALLOWED_LOGIN_REDIRECT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-2
CREATE INDEX FL_TRUSTED_CLIENT_ALLOWED_LOGIN_REDIRECT_CLIENT ON access_trusted_client_allowed_login_redirect(client);

-- changeset auto.generated:1825492372-3
ALTER TABLE access_trusted_client_allowed_login_redirect ADD CONSTRAINT FK_ACCESS_TRUSTED_CLIENT_ALLOWED_LOGIN_REDIRECT_CLIENT FOREIGN KEY (client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

