-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE access_client_identity (uid VARCHAR(255) NOT NULL, version INT NOT NULL, roles LONGTEXT NULL, relying_party VARCHAR(255) NULL, trusted_client VARCHAR(255) NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_CLIENT_IDENTITY PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-2
CREATE TABLE access_client_identity_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_CLIENT_IDENTITY_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-3
CREATE TABLE access_platform_identity (uid VARCHAR(255) NOT NULL, version INT NOT NULL, relying_party VARCHAR(255) NULL, trusted_client VARCHAR(255) NULL, user VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_PLATFORM_IDENTITY PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-4
CREATE TABLE access_platform_identity_audit (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK_ACCESS_PLATFORM_IDENTITY_AUDIT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-5
CREATE TABLE access_platform_identity_role (uid VARCHAR(255) NOT NULL, version INT NOT NULL, platform_identity VARCHAR(255) NOT NULL, `role` VARCHAR(255) NOT NULL, CONSTRAINT PK_ACCESS_PLATFORM_IDENTITY_ROLE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-6
ALTER TABLE access_trusted_client ADD allow_all_scopes BIT DEFAULT 0 NULL;

-- changeset auto.generated:1825492372-7
ALTER TABLE access_tenant_terms_of_use ADD relying_party VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-8
CREATE INDEX FL_CLIENT_IDENTITY_RELYING_PARTYS ON access_client_identity(relying_party);

-- changeset auto.generated:1825492372-9
CREATE INDEX FL_CLIENT_IDENTITY_TRUSTED_CLIENT ON access_client_identity(trusted_client);

-- changeset auto.generated:1825492372-10
CREATE INDEX FL_CLIENT_IDENTITY_USER ON access_client_identity(user);

-- changeset auto.generated:1825492372-11
CREATE INDEX FL_PLATFORM_IDENTITY_RELYING_PARTY ON access_platform_identity(relying_party);

-- changeset auto.generated:1825492372-12
CREATE INDEX FL_PLATFORM_IDENTITY_ROLE_PLATFORM_IDENTITY ON access_platform_identity_role(platform_identity);

-- changeset auto.generated:1825492372-13
CREATE INDEX FL_PLATFORM_IDENTITY_ROLE_ROLE ON access_platform_identity_role(`role`);

-- changeset auto.generated:1825492372-14
CREATE INDEX FL_PLATFORM_IDENTITY_TRUSTED_CLIENT ON access_platform_identity(trusted_client);

-- changeset auto.generated:1825492372-15
CREATE INDEX FL_PLATFORM_IDENTITY_USERS ON access_platform_identity(user);

-- changeset auto.generated:1825492372-16
CREATE INDEX FL_TENANT_TERMS_OF_USE_RELYING_PARTY ON access_tenant_terms_of_use(relying_party);

-- changeset auto.generated:1825492372-17
CREATE UNIQUE INDEX UK_PLATFORM_IDENTITY_ROLE_ROLE_PLATFORM_IDENTITY ON access_platform_identity_role(`role`, platform_identity);

-- changeset auto.generated:1825492372-18
CREATE INDEX idx_audit_entity_id ON access_client_identity_audit(entity_id);

-- changeset auto.generated:1825492372-19
CREATE INDEX idx_audit_entity_id ON access_platform_identity_audit(entity_id);

-- changeset auto.generated:1825492372-20
CREATE INDEX idx_audit_timestamp ON access_client_identity_audit(timestamp DESC);

-- changeset auto.generated:1825492372-21
CREATE INDEX idx_audit_timestamp ON access_platform_identity_audit(timestamp DESC);

-- changeset auto.generated:1825492372-22
CREATE INDEX idx_audit_user ON access_client_identity_audit(performed_by);

-- changeset auto.generated:1825492372-23
CREATE INDEX idx_audit_user ON access_platform_identity_audit(performed_by);

-- changeset auto.generated:1825492372-24
ALTER TABLE access_client_identity ADD CONSTRAINT FK_ACCESS_CLIENT_IDENTITY_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-25
ALTER TABLE access_client_identity ADD CONSTRAINT FK_ACCESS_CLIENT_IDENTITY_TRUSTED_CLIENT FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-26
ALTER TABLE access_client_identity ADD CONSTRAINT FK_ACCESS_CLIENT_IDENTITY_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-27
ALTER TABLE access_platform_identity ADD CONSTRAINT FK_ACCESS_PLATFORM_IDENTITY_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-28
ALTER TABLE access_platform_identity_role ADD CONSTRAINT FK_ACCESS_PLATFORM_IDENTITY_ROLE_PLATFORM_IDENTITY FOREIGN KEY (platform_identity) REFERENCES access_platform_identity (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-29
ALTER TABLE access_platform_identity_role ADD CONSTRAINT FK_ACCESS_PLATFORM_IDENTITY_ROLE_ROLE FOREIGN KEY (`role`) REFERENCES access_role (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-30
ALTER TABLE access_platform_identity ADD CONSTRAINT FK_ACCESS_PLATFORM_IDENTITY_TRUSTED_CLIENT FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-31
ALTER TABLE access_platform_identity ADD CONSTRAINT FK_ACCESS_PLATFORM_IDENTITY_USER FOREIGN KEY (user) REFERENCES access_user (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-32
ALTER TABLE access_tenant_terms_of_use ADD CONSTRAINT FK_ACCESS_TENANT_TERMS_OF_USE_RELYING_PARTY FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

