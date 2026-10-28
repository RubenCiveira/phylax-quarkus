-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE INDEX FL_OAUTH_CLIENT_WITH_BACKCHANNEL_LOGOUT_URL ON access_oauth_client(backchannel_logout_uri);

-- changeset auto.generated:1825492372-2
CREATE INDEX FL_OAUTH_CLIENT_WITH_FRONTCHANNEL_LOGOUT_URL ON access_oauth_client(frontchannel_logout_uri);

