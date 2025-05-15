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
CREATE TABLE login_provider (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, certificate TEXT, direct_access BOOLEAN, disabled BOOLEAN, enabled_by_default BOOLEAN NOT NULL, metadata VARCHAR(255), name VARCHAR(255) NOT NULL, private_key VARCHAR(255), public_key VARCHAR(255), source VARCHAR(255) NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT login_provider_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-9
CREATE TABLE relying_party (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, api_key VARCHAR(255) NOT NULL, code VARCHAR(255) NOT NULL, enabled BOOLEAN, CONSTRAINT relying_party_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-10
CREATE TABLE role (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, name VARCHAR(255) NOT NULL, tenant VARCHAR(255), CONSTRAINT role_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-11
CREATE TABLE role_domain (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, role VARCHAR(255) NOT NULL, security_domain VARCHAR(255) NOT NULL, CONSTRAINT role_domain_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-12
CREATE TABLE scope_assignation (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, security_domain VARCHAR(255) NOT NULL, security_scope VARCHAR(255) NOT NULL, CONSTRAINT scope_assignation_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-13
CREATE TABLE security_domain (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, enabled BOOLEAN, level INTEGER NOT NULL, manage_all BOOLEAN, name VARCHAR(255) NOT NULL, read_all BOOLEAN, write_all BOOLEAN, CONSTRAINT security_domain_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-14
CREATE TABLE security_scope (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, enabled BOOLEAN, kind VARCHAR(255), resource VARCHAR(255) NOT NULL, scope VARCHAR(255) NOT NULL, visibility VARCHAR(255), relying_party VARCHAR(255), trusted_client VARCHAR(255), CONSTRAINT security_scope_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-15
CREATE TABLE tenant (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, access_to_all_applications BOOLEAN, domain VARCHAR(255) NOT NULL, enabled BOOLEAN NOT NULL, name VARCHAR(255) NOT NULL, CONSTRAINT tenant_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-16
CREATE TABLE tenant_config (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, force_mfa BOOLEAN NOT NULL, inner_label VARCHAR(255), tenant VARCHAR(255) NOT NULL, CONSTRAINT tenant_config_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-17
CREATE TABLE tenant_relying_party (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, relying_party VARCHAR(255) NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT tenant_relying_party_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-18
CREATE TABLE tenant_terms_of_use (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, activation_date TIMESTAMP WITHOUT TIME ZONE, attached VARCHAR(255), text TEXT NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT tenant_terms_of_use_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-19
CREATE TABLE tenant_trusted_client (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, tenant VARCHAR(255) NOT NULL, trusted_client VARCHAR(255) NOT NULL, CONSTRAINT tenant_trusted_client_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-20
CREATE TABLE trusted_client (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, allowed_redirects VARCHAR(255), code VARCHAR(255) NOT NULL, enabled BOOLEAN, public_allow BOOLEAN NOT NULL, secret_oauth VARCHAR(255), CONSTRAINT trusted_client_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-21
CREATE TABLE "user" (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, blocked_until TIMESTAMP WITHOUT TIME ZONE, email VARCHAR(255), enabled BOOLEAN, language VARCHAR(255), name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, provider VARCHAR(255), second_factor_seed VARCHAR(255), temporal_password BOOLEAN, use_second_factors BOOLEAN, tenant VARCHAR(255), CONSTRAINT user_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-22
CREATE TABLE user_accepted_termns_of_use (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, accept_date TIMESTAMP WITHOUT TIME ZONE, conditions VARCHAR(255) NOT NULL, "user" VARCHAR(255) NOT NULL, CONSTRAINT user_accepted_termns_of_use_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-23
CREATE TABLE user_access_temporal_code (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, failed_login_attempts INTEGER, recovery_code VARCHAR(255), recovery_code_expiration TIMESTAMP WITHOUT TIME ZONE, temp_second_factor_seed VARCHAR(255), temp_second_factor_seed_expiration TIMESTAMP WITHOUT TIME ZONE, "user" VARCHAR(255) NOT NULL, CONSTRAINT user_access_temporal_code_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-24
CREATE TABLE user_identity (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, relying_party VARCHAR(255), trusted_client VARCHAR(255), "user" VARCHAR(255) NOT NULL, CONSTRAINT user_identity_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-25
CREATE TABLE user_identity_role (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, role VARCHAR(255) NOT NULL, user_identity VARCHAR(255) NOT NULL, CONSTRAINT user_identity_role_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-26
CREATE INDEX fl_login_provider_tenant ON login_provider(tenant);

--changeset auto.generated:1825492372-27
CREATE INDEX fl_role_domain_role ON role_domain(role);

--changeset auto.generated:1825492372-28
CREATE INDEX fl_role_domain_security_domain ON role_domain(security_domain);

--changeset auto.generated:1825492372-29
CREATE INDEX fl_role_tenant ON role(tenant);

--changeset auto.generated:1825492372-30
CREATE INDEX fl_scope_assignation_security_domains ON scope_assignation(security_domain);

--changeset auto.generated:1825492372-31
CREATE INDEX fl_scope_assignation_security_scope ON scope_assignation(security_scope);

--changeset auto.generated:1825492372-32
CREATE INDEX fl_security_domain_enabled ON security_domain(enabled);

--changeset auto.generated:1825492372-33
CREATE INDEX fl_security_scope_relying_partys ON security_scope(relying_party);

--changeset auto.generated:1825492372-34
CREATE INDEX fl_security_scope_resource ON security_scope(resource);

--changeset auto.generated:1825492372-35
CREATE INDEX fl_security_scope_trusted_clients ON security_scope(trusted_client);

--changeset auto.generated:1825492372-36
CREATE INDEX fl_tenant_relying_party_relying_partys ON tenant_relying_party(relying_party);

--changeset auto.generated:1825492372-37
CREATE INDEX fl_tenant_relying_party_tenants ON tenant_relying_party(tenant);

--changeset auto.generated:1825492372-38
CREATE INDEX fl_tenant_terms_of_use_tenant ON tenant_terms_of_use(tenant);

--changeset auto.generated:1825492372-39
CREATE INDEX fl_tenant_trusted_client_tenant ON tenant_trusted_client(tenant);

--changeset auto.generated:1825492372-40
CREATE INDEX fl_tenant_trusted_client_trusted_client ON tenant_trusted_client(trusted_client);

--changeset auto.generated:1825492372-41
CREATE INDEX fl_trusted_client_code ON trusted_client(code);

--changeset auto.generated:1825492372-42
CREATE INDEX fl_user_accepted_termns_of_use_conditions ON user_accepted_termns_of_use(conditions);

--changeset auto.generated:1825492372-43
CREATE INDEX fl_user_identity_relying_partys ON user_identity(relying_party);

--changeset auto.generated:1825492372-44
CREATE INDEX fl_user_identity_role_roles ON user_identity_role(role);

--changeset auto.generated:1825492372-45
CREATE INDEX fl_user_identity_role_user_identitys ON user_identity_role(user_identity);

--changeset auto.generated:1825492372-46
CREATE INDEX fl_user_identity_trusted_client ON user_identity(trusted_client);

--changeset auto.generated:1825492372-47
CREATE INDEX idx_long_tasks_code_actor ON _long_tasks(code, actor);

--changeset auto.generated:1825492372-48
CREATE INDEX st_login_provider_name_desc ON login_provider(name);

--changeset auto.generated:1825492372-49
CREATE INDEX st_role_name_desc ON role(name);

--changeset auto.generated:1825492372-50
CREATE UNIQUE INDEX uk_login_provider_tenant_name ON login_provider(tenant, name);

--changeset auto.generated:1825492372-51
CREATE UNIQUE INDEX uk_relying_party_api_key_unique ON relying_party(api_key);

--changeset auto.generated:1825492372-52
CREATE UNIQUE INDEX uk_relying_party_code_unique ON relying_party(code);

--changeset auto.generated:1825492372-53
CREATE UNIQUE INDEX uk_role_domain_role_security_domain ON role_domain(role, security_domain);

--changeset auto.generated:1825492372-54
CREATE UNIQUE INDEX uk_role_tenant_name ON role(tenant, name);

--changeset auto.generated:1825492372-55
CREATE UNIQUE INDEX uk_scope_assignation_security_domain_security_scope ON scope_assignation(security_domain, security_scope);

--changeset auto.generated:1825492372-56
CREATE UNIQUE INDEX uk_security_domain_name_unique ON security_domain(name);

--changeset auto.generated:1825492372-57
CREATE UNIQUE INDEX uk_security_scope_resource_scope ON security_scope(resource, scope);

--changeset auto.generated:1825492372-58
CREATE UNIQUE INDEX uk_tenant_config_tenant_unique ON tenant_config(tenant);

--changeset auto.generated:1825492372-59
CREATE UNIQUE INDEX uk_tenant_domain_unique ON tenant(domain);

--changeset auto.generated:1825492372-60
CREATE UNIQUE INDEX uk_tenant_name_unique ON tenant(name);

--changeset auto.generated:1825492372-61
CREATE UNIQUE INDEX uk_tenant_relying_party_tenant_relying_party ON tenant_relying_party(tenant, relying_party);

--changeset auto.generated:1825492372-62
CREATE UNIQUE INDEX uk_tenant_trusted_client_tenant_trusted_client ON tenant_trusted_client(tenant, trusted_client);

--changeset auto.generated:1825492372-63
CREATE UNIQUE INDEX uk_user_identity_role_role_user_identity ON user_identity_role(role, user_identity);

--changeset auto.generated:1825492372-64
ALTER TABLE login_provider ADD CONSTRAINT fk_login_provider_tenant FOREIGN KEY (tenant) REFERENCES tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-65
ALTER TABLE role_domain ADD CONSTRAINT fk_role_domain_role FOREIGN KEY (role) REFERENCES role (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-66
ALTER TABLE role_domain ADD CONSTRAINT fk_role_domain_security_domain FOREIGN KEY (security_domain) REFERENCES security_domain (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-67
ALTER TABLE role ADD CONSTRAINT fk_role_tenant FOREIGN KEY (tenant) REFERENCES tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-68
ALTER TABLE scope_assignation ADD CONSTRAINT fk_scope_assignation_security_domain FOREIGN KEY (security_domain) REFERENCES security_domain (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-69
ALTER TABLE scope_assignation ADD CONSTRAINT fk_scope_assignation_security_scope FOREIGN KEY (security_scope) REFERENCES security_scope (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-70
ALTER TABLE security_scope ADD CONSTRAINT fk_security_scope_relying_party FOREIGN KEY (relying_party) REFERENCES relying_party (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-71
ALTER TABLE security_scope ADD CONSTRAINT fk_security_scope_trusted_client FOREIGN KEY (trusted_client) REFERENCES trusted_client (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-72
ALTER TABLE tenant_config ADD CONSTRAINT fk_tenant_config_tenant FOREIGN KEY (tenant) REFERENCES tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-73
ALTER TABLE tenant_relying_party ADD CONSTRAINT fk_tenant_relying_party_relying_party FOREIGN KEY (relying_party) REFERENCES relying_party (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-74
ALTER TABLE tenant_relying_party ADD CONSTRAINT fk_tenant_relying_party_tenant FOREIGN KEY (tenant) REFERENCES tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-75
ALTER TABLE tenant_terms_of_use ADD CONSTRAINT fk_tenant_terms_of_use_tenant FOREIGN KEY (tenant) REFERENCES tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-76
ALTER TABLE tenant_trusted_client ADD CONSTRAINT fk_tenant_trusted_client_tenant FOREIGN KEY (tenant) REFERENCES tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-77
ALTER TABLE tenant_trusted_client ADD CONSTRAINT fk_tenant_trusted_client_trusted_client FOREIGN KEY (trusted_client) REFERENCES trusted_client (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-78
ALTER TABLE user_accepted_termns_of_use ADD CONSTRAINT fk_user_accepted_termns_of_use_conditions FOREIGN KEY (conditions) REFERENCES tenant_terms_of_use (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-79
ALTER TABLE user_accepted_termns_of_use ADD CONSTRAINT fk_user_accepted_termns_of_use_user FOREIGN KEY ("user") REFERENCES "user" (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-80
ALTER TABLE user_access_temporal_code ADD CONSTRAINT fk_user_access_temporal_code_user FOREIGN KEY ("user") REFERENCES "user" (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-81
ALTER TABLE user_identity ADD CONSTRAINT fk_user_identity_relying_party FOREIGN KEY (relying_party) REFERENCES relying_party (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-82
ALTER TABLE user_identity_role ADD CONSTRAINT fk_user_identity_role_role FOREIGN KEY (role) REFERENCES role (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-83
ALTER TABLE user_identity_role ADD CONSTRAINT fk_user_identity_role_user_identity FOREIGN KEY (user_identity) REFERENCES user_identity (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-84
ALTER TABLE user_identity ADD CONSTRAINT fk_user_identity_trusted_client FOREIGN KEY (trusted_client) REFERENCES trusted_client (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-85
ALTER TABLE user_identity ADD CONSTRAINT fk_user_identity_user FOREIGN KEY ("user") REFERENCES "user" (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-86
ALTER TABLE "user" ADD CONSTRAINT fk_user_tenant FOREIGN KEY (tenant) REFERENCES tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

