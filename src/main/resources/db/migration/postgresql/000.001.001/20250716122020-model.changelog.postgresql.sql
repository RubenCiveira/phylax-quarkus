--liquibase formatted sql

--changeset auto.generated:1825492372-1
CREATE TABLE _filestorer (code VARCHAR(250) NOT NULL, temp SMALLINT DEFAULT 0 NOT NULL, name VARCHAR(250) NOT NULL, mime VARCHAR(250) NOT NULL, upload TIMESTAMP WITHOUT TIME ZONE NOT NULL, bytes BYTEA NOT NULL, CONSTRAINT pk_accion_coleccion PRIMARY KEY (code));

--changeset auto.generated:1825492372-2
CREATE TABLE _long_tasks (code VARCHAR(250) NOT NULL, actor VARCHAR(250) NOT NULL, creation TIMESTAMP WITHOUT TIME ZONE NOT NULL, completion TIMESTAMP WITHOUT TIME ZONE, expiration TIMESTAMP WITHOUT TIME ZONE, progress TEXT NOT NULL, CONSTRAINT pk_long_task PRIMARY KEY (code));

--changeset auto.generated:1825492372-3
CREATE TABLE _oauth_delegated_codes (expiration TIMESTAMP WITHOUT TIME ZONE NOT NULL, code TEXT NOT NULL, token TEXT NOT NULL);

--changeset auto.generated:1825492372-4
CREATE TABLE _oauth_jwt_keys (expiration TIMESTAMP WITHOUT TIME ZONE NOT NULL, since TIMESTAMP WITHOUT TIME ZONE NOT NULL, keyid VARCHAR(255) NOT NULL, private TEXT NOT NULL, public TEXT NOT NULL);

--changeset auto.generated:1825492372-5
CREATE TABLE _oauth_sessions (session VARCHAR(255) NOT NULL, expiration TIMESTAMP WITHOUT TIME ZONE NOT NULL, client_id VARCHAR(250) NOT NULL, grant_type VARCHAR(20) NOT NULL, auth_data TEXT NOT NULL, csid TEXT NOT NULL);

--changeset auto.generated:1825492372-6
CREATE TABLE _oauth_temporal_codes (code VARCHAR(255) NOT NULL, code_data TEXT NOT NULL, expiration TIMESTAMP WITHOUT TIME ZONE NOT NULL);

--changeset auto.generated:1825492372-7
CREATE TABLE _oauth_temporal_keys (current VARCHAR(255) NOT NULL, old VARCHAR(255) NOT NULL, expiration TIMESTAMP WITHOUT TIME ZONE NOT NULL);

--changeset auto.generated:1825492372-8
CREATE TABLE access_login_provider (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, certificate TEXT, direct_access BOOLEAN, disabled BOOLEAN, metadata VARCHAR(255), name VARCHAR(255) NOT NULL, private_key VARCHAR(255), public_key VARCHAR(255), source VARCHAR(255) NOT NULL, users_enabled_by_default BOOLEAN NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT access_login_provider_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-9
CREATE TABLE access_relying_party (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, api_key VARCHAR(255) NOT NULL, code VARCHAR(255) NOT NULL, enabled BOOLEAN, CONSTRAINT access_relying_party_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-10
CREATE TABLE access_role (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, name VARCHAR(255) NOT NULL, tenant VARCHAR(255), CONSTRAINT access_role_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-11
CREATE TABLE access_role_domain (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, role VARCHAR(255) NOT NULL, security_domain VARCHAR(255) NOT NULL, CONSTRAINT access_role_domain_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-12
CREATE TABLE access_scope_assignation (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, security_domain VARCHAR(255) NOT NULL, security_scope VARCHAR(255) NOT NULL, CONSTRAINT access_scope_assignation_pkey PRIMARY KEY (uid));

