--liquibase formatted sql

--changeset auto.generated:1825492372-1
CREATE TABLE access_security_domain (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, enabled BOOLEAN, level INTEGER NOT NULL, manage_all BOOLEAN, name VARCHAR(255) NOT NULL, read_all BOOLEAN, write_all BOOLEAN, CONSTRAINT access_security_domain_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-2
CREATE TABLE access_security_scope (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, enabled BOOLEAN, kind VARCHAR(255), resource VARCHAR(255) NOT NULL, scope VARCHAR(255) NOT NULL, visibility VARCHAR(255), relying_party VARCHAR(255), trusted_client VARCHAR(255), CONSTRAINT access_security_scope_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-3
CREATE TABLE access_tenant (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, access_to_all_applications BOOLEAN, domain VARCHAR(255) NOT NULL, enabled BOOLEAN NOT NULL, name VARCHAR(255) NOT NULL, CONSTRAINT access_tenant_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-4
CREATE TABLE access_tenant_config (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, force_mfa BOOLEAN NOT NULL, inner_label VARCHAR(255), tenant VARCHAR(255) NOT NULL, CONSTRAINT access_tenant_config_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-5
CREATE TABLE access_tenant_relying_party (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, relying_party VARCHAR(255) NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT access_tenant_relying_party_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-6
CREATE TABLE access_tenant_terms_of_use (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, activation_date TIMESTAMP WITHOUT TIME ZONE, attached VARCHAR(255), text TEXT NOT NULL, tenant VARCHAR(255) NOT NULL, CONSTRAINT access_tenant_terms_of_use_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-7
CREATE TABLE access_tenant_trusted_client (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, tenant VARCHAR(255) NOT NULL, trusted_client VARCHAR(255) NOT NULL, CONSTRAINT access_tenant_trusted_client_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-8
CREATE TABLE access_trusted_client (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, allowed_redirects VARCHAR(255), code VARCHAR(255) NOT NULL, enabled BOOLEAN, public_allow BOOLEAN NOT NULL, secret_oauth VARCHAR(255), CONSTRAINT access_trusted_client_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-9
CREATE TABLE access_user (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, blocked_until TIMESTAMP WITHOUT TIME ZONE, email VARCHAR(255), enabled BOOLEAN, language VARCHAR(255), name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, provider VARCHAR(255), second_factor_seed VARCHAR(255), temporal_password BOOLEAN, use_second_factors BOOLEAN, tenant VARCHAR(255), CONSTRAINT access_user_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-10
CREATE TABLE access_user_accepted_termns_of_use (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, accept_date TIMESTAMP WITHOUT TIME ZONE, conditions VARCHAR(255) NOT NULL, "user" VARCHAR(255) NOT NULL, CONSTRAINT access_user_accepted_termns_of_use_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-11
CREATE TABLE access_user_access_temporal_code (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, failed_login_attempts INTEGER, recovery_code VARCHAR(255), recovery_code_expiration TIMESTAMP WITHOUT TIME ZONE, temp_second_factor_seed VARCHAR(255), temp_second_factor_seed_expiration TIMESTAMP WITHOUT TIME ZONE, "user" VARCHAR(255) NOT NULL, CONSTRAINT access_user_access_temporal_code_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-12
CREATE TABLE access_user_identity (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, relying_party VARCHAR(255), trusted_client VARCHAR(255), "user" VARCHAR(255) NOT NULL, CONSTRAINT access_user_identity_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-13
CREATE TABLE access_user_identity_role (uid VARCHAR(255) NOT NULL, version INTEGER NOT NULL, role VARCHAR(255) NOT NULL, user_identity VARCHAR(255) NOT NULL, CONSTRAINT access_user_identity_role_pkey PRIMARY KEY (uid));

--changeset auto.generated:1825492372-14
CREATE INDEX fl_login_provider_tenant ON access_login_provider(tenant);

--changeset auto.generated:1825492372-15
CREATE INDEX fl_role_domain_role ON access_role_domain(role);

--changeset auto.generated:1825492372-16
CREATE INDEX fl_role_domain_security_domain ON access_role_domain(security_domain);

--changeset auto.generated:1825492372-17
CREATE INDEX fl_role_tenant ON access_role(tenant);

--changeset auto.generated:1825492372-18
CREATE INDEX fl_scope_assignation_security_domains ON access_scope_assignation(security_domain);

--changeset auto.generated:1825492372-19
CREATE INDEX fl_scope_assignation_security_scope ON access_scope_assignation(security_scope);

--changeset auto.generated:1825492372-20
CREATE INDEX fl_security_domain_enabled ON access_security_domain(enabled);

--changeset auto.generated:1825492372-21
CREATE INDEX fl_security_scope_relying_partys ON access_security_scope(relying_party);

--changeset auto.generated:1825492372-22
CREATE INDEX fl_security_scope_resource ON access_security_scope(resource);

--changeset auto.generated:1825492372-23
CREATE INDEX fl_security_scope_trusted_clients ON access_security_scope(trusted_client);

--changeset auto.generated:1825492372-24
CREATE INDEX fl_tenant_relying_party_relying_partys ON access_tenant_relying_party(relying_party);

--changeset auto.generated:1825492372-25
CREATE INDEX fl_tenant_relying_party_tenants ON access_tenant_relying_party(tenant);

--changeset auto.generated:1825492372-26
CREATE INDEX fl_tenant_terms_of_use_tenant ON access_tenant_terms_of_use(tenant);

--changeset auto.generated:1825492372-27
CREATE INDEX fl_tenant_trusted_client_tenant ON access_tenant_trusted_client(tenant);

--changeset auto.generated:1825492372-28
CREATE INDEX fl_tenant_trusted_client_trusted_client ON access_tenant_trusted_client(trusted_client);

--changeset auto.generated:1825492372-29
CREATE INDEX fl_trusted_client_code ON access_trusted_client(code);

--changeset auto.generated:1825492372-30
CREATE INDEX fl_user_accepted_termns_of_use_conditions ON access_user_accepted_termns_of_use(conditions);

--changeset auto.generated:1825492372-31
CREATE INDEX fl_user_identity_relying_partys ON access_user_identity(relying_party);

--changeset auto.generated:1825492372-32
CREATE INDEX fl_user_identity_role_roles ON access_user_identity_role(role);

--changeset auto.generated:1825492372-33
CREATE INDEX fl_user_identity_role_user_identitys ON access_user_identity_role(user_identity);

--changeset auto.generated:1825492372-34
CREATE INDEX fl_user_identity_trusted_client ON access_user_identity(trusted_client);

--changeset auto.generated:1825492372-35
CREATE INDEX fl_user_tenant ON access_user(tenant);

--changeset auto.generated:1825492372-36
CREATE INDEX idx_long_tasks_code_actor ON _long_tasks(code, actor);

--changeset auto.generated:1825492372-37
CREATE INDEX st_login_provider_name_desc ON access_login_provider(name);

--changeset auto.generated:1825492372-38
CREATE INDEX st_role_name_desc ON access_role(name);

--changeset auto.generated:1825492372-39
CREATE INDEX st_user_name_desc ON access_user(name);

--changeset auto.generated:1825492372-40
CREATE UNIQUE INDEX uk_login_provider_tenant_name ON access_login_provider(tenant, name);

--changeset auto.generated:1825492372-41
CREATE UNIQUE INDEX uk_relying_party_api_key_unique ON access_relying_party(api_key);

--changeset auto.generated:1825492372-42
CREATE UNIQUE INDEX uk_relying_party_code_unique ON access_relying_party(code);

--changeset auto.generated:1825492372-43
CREATE UNIQUE INDEX uk_role_domain_role_security_domain ON access_role_domain(role, security_domain);

--changeset auto.generated:1825492372-44
CREATE UNIQUE INDEX uk_role_tenant_name ON access_role(tenant, name);

--changeset auto.generated:1825492372-45
CREATE UNIQUE INDEX uk_scope_assignation_security_domain_security_scope ON access_scope_assignation(security_domain, security_scope);

--changeset auto.generated:1825492372-46
CREATE UNIQUE INDEX uk_security_domain_name_unique ON access_security_domain(name);

--changeset auto.generated:1825492372-47
CREATE UNIQUE INDEX uk_security_scope_resource_scope ON access_security_scope(resource, scope);

--changeset auto.generated:1825492372-48
CREATE UNIQUE INDEX uk_tenant_config_tenant_unique ON access_tenant_config(tenant);

--changeset auto.generated:1825492372-49
CREATE UNIQUE INDEX uk_tenant_domain_unique ON access_tenant(domain);

--changeset auto.generated:1825492372-50
CREATE UNIQUE INDEX uk_tenant_name_unique ON access_tenant(name);

--changeset auto.generated:1825492372-51
CREATE UNIQUE INDEX uk_tenant_relying_party_tenant_relying_party ON access_tenant_relying_party(tenant, relying_party);

--changeset auto.generated:1825492372-52
CREATE UNIQUE INDEX uk_tenant_trusted_client_tenant_trusted_client ON access_tenant_trusted_client(tenant, trusted_client);

--changeset auto.generated:1825492372-53
CREATE UNIQUE INDEX uk_user_identity_role_role_user_identity ON access_user_identity_role(role, user_identity);

--changeset auto.generated:1825492372-54
CREATE UNIQUE INDEX uk_user_tenant_name ON access_user(tenant, name);

--changeset auto.generated:1825492372-55
ALTER TABLE access_login_provider ADD CONSTRAINT fk_access_login_provider_tenant FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-56
ALTER TABLE access_role_domain ADD CONSTRAINT fk_access_role_domain_role FOREIGN KEY (role) REFERENCES access_role (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-57
ALTER TABLE access_role_domain ADD CONSTRAINT fk_access_role_domain_security_domain FOREIGN KEY (security_domain) REFERENCES access_security_domain (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-58
ALTER TABLE access_role ADD CONSTRAINT fk_access_role_tenant FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-59
ALTER TABLE access_scope_assignation ADD CONSTRAINT fk_access_scope_assignation_security_domain FOREIGN KEY (security_domain) REFERENCES access_security_domain (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-60
ALTER TABLE access_scope_assignation ADD CONSTRAINT fk_access_scope_assignation_security_scope FOREIGN KEY (security_scope) REFERENCES access_security_scope (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-61
ALTER TABLE access_security_scope ADD CONSTRAINT fk_access_security_scope_relying_party FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-62
ALTER TABLE access_security_scope ADD CONSTRAINT fk_access_security_scope_trusted_client FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-63
ALTER TABLE access_tenant_config ADD CONSTRAINT fk_access_tenant_config_tenant FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-64
ALTER TABLE access_tenant_relying_party ADD CONSTRAINT fk_access_tenant_relying_party_relying_party FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-65
ALTER TABLE access_tenant_relying_party ADD CONSTRAINT fk_access_tenant_relying_party_tenant FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-66
ALTER TABLE access_tenant_terms_of_use ADD CONSTRAINT fk_access_tenant_terms_of_use_tenant FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-67
ALTER TABLE access_tenant_trusted_client ADD CONSTRAINT fk_access_tenant_trusted_client_tenant FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-68
ALTER TABLE access_tenant_trusted_client ADD CONSTRAINT fk_access_tenant_trusted_client_trusted_client FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-69
ALTER TABLE access_user_accepted_termns_of_use ADD CONSTRAINT fk_access_user_accepted_termns_of_use_conditions FOREIGN KEY (conditions) REFERENCES access_tenant_terms_of_use (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-70
ALTER TABLE access_user_accepted_termns_of_use ADD CONSTRAINT fk_access_user_accepted_termns_of_use_user FOREIGN KEY ("user") REFERENCES access_user (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-71
ALTER TABLE access_user_access_temporal_code ADD CONSTRAINT fk_access_user_access_temporal_code_user FOREIGN KEY ("user") REFERENCES access_user (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-72
ALTER TABLE access_user_identity ADD CONSTRAINT fk_access_user_identity_relying_party FOREIGN KEY (relying_party) REFERENCES access_relying_party (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-73
ALTER TABLE access_user_identity_role ADD CONSTRAINT fk_access_user_identity_role_role FOREIGN KEY (role) REFERENCES access_role (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-74
ALTER TABLE access_user_identity_role ADD CONSTRAINT fk_access_user_identity_role_user_identity FOREIGN KEY (user_identity) REFERENCES access_user_identity (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-75
ALTER TABLE access_user_identity ADD CONSTRAINT fk_access_user_identity_trusted_client FOREIGN KEY (trusted_client) REFERENCES access_trusted_client (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-76
ALTER TABLE access_user_identity ADD CONSTRAINT fk_access_user_identity_user FOREIGN KEY ("user") REFERENCES access_user (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset auto.generated:1825492372-77
ALTER TABLE access_user ADD CONSTRAINT fk_access_user_tenant FOREIGN KEY (tenant) REFERENCES access_tenant (uid) ON UPDATE NO ACTION ON DELETE NO ACTION;

