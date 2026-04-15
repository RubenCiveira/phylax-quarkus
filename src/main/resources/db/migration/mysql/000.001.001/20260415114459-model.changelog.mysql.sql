-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE access_user_consented_scopes (uid VARCHAR(255) NOT NULL, version INT NOT NULL, decision_at timestamp DEFAULT NULL NULL, granted BIT DEFAULT 0 NULL, scope VARCHAR(255) NULL, trusted_client VARCHAR(255) NOT NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_USER_CONSENTED_SCOPES PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-2
ALTER TABLE access_trusted_client ADD back_channel_logout_session_required BIT DEFAULT 0 NULL;

-- changeset auto.generated:1825492372-3
ALTER TABLE access_trusted_client ADD back_channel_logout_uri VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-4
ALTER TABLE access_trusted_client ADD front_channel_logout_session_required BIT DEFAULT 0 NULL;

-- changeset auto.generated:1825492372-5
ALTER TABLE access_trusted_client ADD front_channel_logout_uri VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-6
CREATE INDEX FL_TRUSTED_CLIENT_WITH_BACK_CHANNEL_URL ON access_trusted_client(back_channel_logout_uri);

-- changeset auto.generated:1825492372-7
CREATE INDEX FL_TRUSTED_CLIENT_WITH_FRONT_CHANNEL_URL ON access_trusted_client(front_channel_logout_uri);

-- changeset auto.generated:1825492372-8
CREATE INDEX FL_USER_CONSENTED_SCOPES_TRUSTED_CLIENTS ON access_user_consented_scopes(trusted_client);

-- changeset auto.generated:1825492372-9
CREATE INDEX FL_USER_CONSENTED_SCOPES_USERS ON access_user_consented_scopes(user);

-- changeset auto.generated:1825492372-10
ALTER TABLE access_user_consented_scopes ADD CONSTRAINT FK_ACCESS_USER_CONSENTED_SCOPES_TRUSTED_CLIENT FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-11
ALTER TABLE access_user_consented_scopes ADD CONSTRAINT FK_ACCESS_USER_CONSENTED_SCOPES_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

