-- liquibase formatted sql

-- changeset auto.generated:1825492372-1
CREATE TABLE _audit_observe_observed_alert (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_OBSERVE_OBSERVED_ALERT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-2
CREATE TABLE _audit_observe_observed_alert_rule (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_OBSERVE_OBSERVED_ALERT_RULE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-3
CREATE TABLE _audit_observe_observed_fingerprint_rule (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_OBSERVE_OBSERVED_FINGERPRINT_RULE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-4
CREATE TABLE _audit_observe_observed_issue_alias (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_OBSERVE_OBSERVED_ISSUE_ALIAS PRIMARY KEY (id));

-- changeset auto.generated:1825492372-5
CREATE TABLE _audit_observe_observed_issue_comment (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_OBSERVE_OBSERVED_ISSUE_COMMENT PRIMARY KEY (id));

-- changeset auto.generated:1825492372-6
CREATE TABLE _audit_observe_observed_probe (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_OBSERVE_OBSERVED_PROBE PRIMARY KEY (id));

-- changeset auto.generated:1825492372-7
CREATE TABLE _audit_observe_observed_slo (id CHAR(36) NOT NULL, operation VARCHAR(50) NOT NULL, usecase VARCHAR(100) NOT NULL, trace_id VARCHAR(100) NOT NULL, span_id VARCHAR(100) NOT NULL, entity_id VARCHAR(100) NOT NULL, old_values TEXT NULL, new_values TEXT NULL, performed_by VARCHAR(100) NOT NULL, tenant VARCHAR(100) NOT NULL, timestamp datetime NOT NULL, source_request TEXT NULL, remote_address VARCHAR(100) NULL, remote_application VARCHAR(100) NULL, remote_device VARCHAR(100) NULL, claims TEXT NULL, CONSTRAINT PK__AUDIT_OBSERVE_OBSERVED_SLO PRIMARY KEY (id));

-- changeset auto.generated:1825492372-8
CREATE TABLE observe_observed_alert (uid VARCHAR(255) NOT NULL, version INT NOT NULL, fired_at timestamp NOT NULL, last_notified_at timestamp DEFAULT NULL NULL, link VARCHAR(255) NULL, problem_key VARCHAR(255) NOT NULL, resolved_at timestamp DEFAULT NULL NULL, status VARCHAR(255) NOT NULL, summary VARCHAR(255) NOT NULL, rule VARCHAR(255) NOT NULL, CONSTRAINT PK_OBSERVE_OBSERVED_ALERT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-9
CREATE TABLE observe_observed_alert_rule (uid VARCHAR(255) NOT NULL, version INT NOT NULL, channel VARCHAR(255) NOT NULL, cooldown_minutes INT NOT NULL, enabled BIT NOT NULL, min_level VARCHAR(255) NOT NULL, muted_until timestamp DEFAULT NULL NULL, name VARCHAR(255) NOT NULL, target VARCHAR(255) NULL, threshold INT NOT NULL, `trigger` VARCHAR(255) NOT NULL, window_minutes INT NOT NULL, origin VARCHAR(255) NULL, CONSTRAINT PK_OBSERVE_OBSERVED_ALERT_RULE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-10
CREATE TABLE observe_observed_fingerprint_rule (uid VARCHAR(255) NOT NULL, version INT NOT NULL, effect VARCHAR(255) NOT NULL, enabled BIT NOT NULL, field VARCHAR(255) NOT NULL, group_key VARCHAR(255) NULL, name VARCHAR(255) NOT NULL, pattern VARCHAR(255) NOT NULL, origin VARCHAR(255) NULL, CONSTRAINT PK_OBSERVE_OBSERVED_FINGERPRINT_RULE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-11
CREATE TABLE observe_observed_issue_alias (uid VARCHAR(255) NOT NULL, version INT NOT NULL, fingerprint VARCHAR(255) NOT NULL, issue VARCHAR(255) NOT NULL, origin VARCHAR(255) NOT NULL, CONSTRAINT PK_OBSERVE_OBSERVED_ISSUE_ALIAS PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-12
CREATE TABLE observe_observed_issue_comment (uid VARCHAR(255) NOT NULL, version INT NOT NULL, author VARCHAR(255) NOT NULL, author_name VARCHAR(255) NOT NULL, created_at timestamp NOT NULL, text LONGTEXT NOT NULL, issue VARCHAR(255) NOT NULL, CONSTRAINT PK_OBSERVE_OBSERVED_ISSUE_COMMENT PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-13
CREATE TABLE observe_observed_probe (uid VARCHAR(255) NOT NULL, version INT NOT NULL, consecutive_failures INT DEFAULT NULL NULL, enabled BIT NOT NULL, expected_status INT NOT NULL, expected_text VARCHAR(255) NULL, failures_to_alert INT NOT NULL, interval_seconds INT NOT NULL, last_checked_at timestamp DEFAULT NULL NULL, last_error VARCHAR(255) NULL, last_latency_ms INT DEFAULT NULL NULL, last_status VARCHAR(255) NULL, method VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, timeout_ms INT NOT NULL, tls_expires_at timestamp DEFAULT NULL NULL, tls_warn_days INT NOT NULL, url VARCHAR(255) NOT NULL, origin VARCHAR(255) NULL, CONSTRAINT PK_OBSERVE_OBSERVED_PROBE PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-14
CREATE TABLE observe_observed_slo (uid VARCHAR(255) NOT NULL, version INT NOT NULL, enabled BIT NOT NULL, kind VARCHAR(255) NOT NULL, latency_ms INT DEFAULT NULL NULL, name VARCHAR(255) NOT NULL, objective INT NOT NULL, route VARCHAR(255) NULL, window_days INT NOT NULL, origin VARCHAR(255) NOT NULL, CONSTRAINT PK_OBSERVE_OBSERVED_SLO PRIMARY KEY (uid));

-- changeset auto.generated:1825492372-15
ALTER TABLE observe_observed_issue ADD assignee VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-16
ALTER TABLE observe_observed_issue ADD assignee_name VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-17
ALTER TABLE observe_observed_issue ADD first_release VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-18
ALTER TABLE observe_observed_issue ADD ignore_until timestamp DEFAULT null NULL;

-- changeset auto.generated:1825492372-19
ALTER TABLE observe_observed_source ADD expected_every_minutes INT DEFAULT null NULL;

-- changeset auto.generated:1825492372-20
ALTER TABLE observe_observed_issue ADD ignore_until_count INT DEFAULT null NULL;

-- changeset auto.generated:1825492372-21
ALTER TABLE observe_observed_issue ADD kind VARCHAR(255) NOT NULL;

-- changeset auto.generated:1825492372-22
ALTER TABLE observe_observed_issue ADD merged_into VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-23
ALTER TABLE observe_observed_issue ADD tenants INT NOT NULL;

-- changeset auto.generated:1825492372-24
ALTER TABLE observe_observed_issue ADD tenants_sketch LONGTEXT NULL;

-- changeset auto.generated:1825492372-25
ALTER TABLE observe_observed_issue ADD tracker_url VARCHAR(255) NULL;

-- changeset auto.generated:1825492372-26
ALTER TABLE observe_observed_issue ADD users INT NOT NULL;

-- changeset auto.generated:1825492372-27
ALTER TABLE observe_observed_issue ADD users_sketch LONGTEXT NULL;

-- changeset auto.generated:1825492372-28
CREATE INDEX FL_OBSERVED_ALERT_PROBLEM_KEY ON observe_observed_alert(problem_key);

-- changeset auto.generated:1825492372-29
CREATE INDEX FL_OBSERVED_ALERT_RULE ON observe_observed_alert(rule);

-- changeset auto.generated:1825492372-30
CREATE INDEX FL_OBSERVED_ALERT_RULE_ORIGIN ON observe_observed_alert_rule(origin);

-- changeset auto.generated:1825492372-31
CREATE INDEX FL_OBSERVED_ALERT_STATUS ON observe_observed_alert(status);

-- changeset auto.generated:1825492372-32
CREATE INDEX FL_OBSERVED_ALERT_SUMMARY ON observe_observed_alert(summary);

-- changeset auto.generated:1825492372-33
CREATE INDEX FL_OBSERVED_FINGERPRINT_RULE_ORIGIN ON observe_observed_fingerprint_rule(origin);

-- changeset auto.generated:1825492372-34
CREATE INDEX FL_OBSERVED_ISSUE_ALIAS_FINGERPRINT ON observe_observed_issue_alias(fingerprint);

-- changeset auto.generated:1825492372-35
CREATE INDEX FL_OBSERVED_ISSUE_ALIAS_ISSUE ON observe_observed_issue_alias(issue);

-- changeset auto.generated:1825492372-36
CREATE INDEX FL_OBSERVED_ISSUE_ALIAS_ORIGINS ON observe_observed_issue_alias(origin);

-- changeset auto.generated:1825492372-37
CREATE INDEX FL_OBSERVED_ISSUE_COMMENT_AUTHOR_NAME ON observe_observed_issue_comment(author_name);

-- changeset auto.generated:1825492372-38
CREATE INDEX FL_OBSERVED_ISSUE_COMMENT_ISSUE ON observe_observed_issue_comment(issue);

-- changeset auto.generated:1825492372-39
CREATE INDEX FL_OBSERVED_ISSUE_KIND ON observe_observed_issue(kind);

-- changeset auto.generated:1825492372-40
CREATE INDEX FL_OBSERVED_PROBE_NAME ON observe_observed_probe(name);

-- changeset auto.generated:1825492372-41
CREATE INDEX FL_OBSERVED_PROBE_ORIGIN ON observe_observed_probe(origin);

-- changeset auto.generated:1825492372-42
CREATE INDEX FL_OBSERVED_SLO_NAME ON observe_observed_slo(name);

-- changeset auto.generated:1825492372-43
CREATE INDEX FL_OBSERVED_SLO_ORIGIN ON observe_observed_slo(origin);

-- changeset auto.generated:1825492372-44
CREATE INDEX ST_OBSERVED_ALERT_RULE_NAME_ASC ON observe_observed_alert_rule(name);

-- changeset auto.generated:1825492372-45
CREATE INDEX ST_OBSERVED_ALERT_RULE_NAME_DESC ON observe_observed_alert_rule(name DESC);

-- changeset auto.generated:1825492372-46
CREATE INDEX ST_OBSERVED_ALERT_SUMMARY_DESC ON observe_observed_alert(summary DESC);

-- changeset auto.generated:1825492372-47
CREATE INDEX ST_OBSERVED_FINGERPRINT_RULE_NAME_ASC ON observe_observed_fingerprint_rule(name);

-- changeset auto.generated:1825492372-48
CREATE INDEX ST_OBSERVED_FINGERPRINT_RULE_NAME_DESC ON observe_observed_fingerprint_rule(name DESC);

-- changeset auto.generated:1825492372-49
CREATE INDEX ST_OBSERVED_ISSUE_ALIAS_FINGERPRINT_DESC ON observe_observed_issue_alias(fingerprint DESC);

-- changeset auto.generated:1825492372-50
CREATE INDEX ST_OBSERVED_ISSUE_COMMENT_AUTHOR_NAME_DESC ON observe_observed_issue_comment(author_name DESC);

-- changeset auto.generated:1825492372-51
CREATE INDEX ST_OBSERVED_PROBE_NAME_DESC ON observe_observed_probe(name DESC);

-- changeset auto.generated:1825492372-52
CREATE INDEX ST_OBSERVED_SLO_NAME_DESC ON observe_observed_slo(name DESC);

-- changeset auto.generated:1825492372-53
CREATE UNIQUE INDEX UK_OBSERVED_ISSUE_ALIAS_ORIGIN_FINGERPRINT ON observe_observed_issue_alias(origin, fingerprint);

-- changeset auto.generated:1825492372-54
CREATE INDEX idx_audit_entity_id ON _audit_observe_observed_alert(entity_id);

-- changeset auto.generated:1825492372-55
CREATE INDEX idx_audit_entity_id ON _audit_observe_observed_alert_rule(entity_id);

-- changeset auto.generated:1825492372-56
CREATE INDEX idx_audit_entity_id ON _audit_observe_observed_fingerprint_rule(entity_id);

-- changeset auto.generated:1825492372-57
CREATE INDEX idx_audit_entity_id ON _audit_observe_observed_issue_alias(entity_id);

-- changeset auto.generated:1825492372-58
CREATE INDEX idx_audit_entity_id ON _audit_observe_observed_issue_comment(entity_id);

-- changeset auto.generated:1825492372-59
CREATE INDEX idx_audit_entity_id ON _audit_observe_observed_probe(entity_id);

-- changeset auto.generated:1825492372-60
CREATE INDEX idx_audit_entity_id ON _audit_observe_observed_slo(entity_id);

-- changeset auto.generated:1825492372-61
CREATE INDEX idx_audit_timestamp ON _audit_observe_observed_alert(timestamp DESC);

-- changeset auto.generated:1825492372-62
CREATE INDEX idx_audit_timestamp ON _audit_observe_observed_alert_rule(timestamp DESC);

-- changeset auto.generated:1825492372-63
CREATE INDEX idx_audit_timestamp ON _audit_observe_observed_fingerprint_rule(timestamp DESC);

-- changeset auto.generated:1825492372-64
CREATE INDEX idx_audit_timestamp ON _audit_observe_observed_issue_alias(timestamp DESC);

-- changeset auto.generated:1825492372-65
CREATE INDEX idx_audit_timestamp ON _audit_observe_observed_issue_comment(timestamp DESC);

-- changeset auto.generated:1825492372-66
CREATE INDEX idx_audit_timestamp ON _audit_observe_observed_probe(timestamp DESC);

-- changeset auto.generated:1825492372-67
CREATE INDEX idx_audit_timestamp ON _audit_observe_observed_slo(timestamp DESC);

-- changeset auto.generated:1825492372-68
CREATE INDEX idx_audit_user ON _audit_observe_observed_alert(performed_by);

-- changeset auto.generated:1825492372-69
CREATE INDEX idx_audit_user ON _audit_observe_observed_alert_rule(performed_by);

-- changeset auto.generated:1825492372-70
CREATE INDEX idx_audit_user ON _audit_observe_observed_fingerprint_rule(performed_by);

-- changeset auto.generated:1825492372-71
CREATE INDEX idx_audit_user ON _audit_observe_observed_issue_alias(performed_by);

-- changeset auto.generated:1825492372-72
CREATE INDEX idx_audit_user ON _audit_observe_observed_issue_comment(performed_by);

-- changeset auto.generated:1825492372-73
CREATE INDEX idx_audit_user ON _audit_observe_observed_probe(performed_by);

-- changeset auto.generated:1825492372-74
CREATE INDEX idx_audit_user ON _audit_observe_observed_slo(performed_by);

-- changeset auto.generated:1825492372-75
ALTER TABLE observe_observed_alert ADD CONSTRAINT FK_OBSERVE_OBSERVED_ALERT_RULE FOREIGN KEY (rule) REFERENCES observe_observed_alert_rule (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-76
ALTER TABLE observe_observed_alert_rule ADD CONSTRAINT FK_OBSERVE_OBSERVED_ALERT_RULE_ORIGIN FOREIGN KEY (origin) REFERENCES observe_observed_source (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-77
ALTER TABLE observe_observed_fingerprint_rule ADD CONSTRAINT FK_OBSERVE_OBSERVED_FINGERPRINT_RULE_ORIGIN FOREIGN KEY (origin) REFERENCES observe_observed_source (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-78
ALTER TABLE observe_observed_issue_alias ADD CONSTRAINT FK_OBSERVE_OBSERVED_ISSUE_ALIAS_ISSUE FOREIGN KEY (issue) REFERENCES observe_observed_issue (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-79
ALTER TABLE observe_observed_issue_alias ADD CONSTRAINT FK_OBSERVE_OBSERVED_ISSUE_ALIAS_ORIGIN FOREIGN KEY (origin) REFERENCES observe_observed_source (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-80
ALTER TABLE observe_observed_issue_comment ADD CONSTRAINT FK_OBSERVE_OBSERVED_ISSUE_COMMENT_ISSUE FOREIGN KEY (issue) REFERENCES observe_observed_issue (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-81
ALTER TABLE observe_observed_probe ADD CONSTRAINT FK_OBSERVE_OBSERVED_PROBE_ORIGIN FOREIGN KEY (origin) REFERENCES observe_observed_source (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset auto.generated:1825492372-82
ALTER TABLE observe_observed_slo ADD CONSTRAINT FK_OBSERVE_OBSERVED_SLO_ORIGIN FOREIGN KEY (origin) REFERENCES observe_observed_source (uid) ON UPDATE RESTRICT ON DELETE RESTRICT;

