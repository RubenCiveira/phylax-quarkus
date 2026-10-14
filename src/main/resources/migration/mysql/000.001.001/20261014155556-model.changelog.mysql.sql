-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
ALTER TABLE access_tenant_login_provider ADD saml_idp_cert VARCHAR(255) NULL;

