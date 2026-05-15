# Functional Extension — Feature Flags per Tenant

> Extracted from OAUTH_PLAN.md (was PLAN-33). Not part of the OAuth/OIDC implementation scope.

---

## Description

Enable/disable product features per tenant without redeployment. Supports rollout
percentage (deterministic hash-based bucketing) and optional conditions JSON for
targeting specific user segments or environments.

## Motivation

- Gradual rollouts and A/B testing without code deploys
- Beta feature management per tenant
- Integrates with tenant configuration already in `features/access/`

## Data model

New table `access_feature_flag`:
```sql
CREATE TABLE access_feature_flag (
  uid                VARCHAR(36)   NOT NULL,
  tenant_id          VARCHAR(36)   NOT NULL,
  flag_key           VARCHAR(100)  NOT NULL,
  enabled            TINYINT(1)    NOT NULL DEFAULT 0,
  rollout_percentage INT           NOT NULL DEFAULT 100,  -- 0-100
  conditions_json    TEXT          NULL,
  description        TEXT          NULL,
  updated_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (uid),
  UNIQUE KEY uq_flag_tenant_key (tenant_id, flag_key)
);
```

Deterministic rollout: `Math.abs(userUid.hashCode()) % 100 < rollout_percentage`.

## API

```
GET    /api/admin/tenants/{uid}/feature-flags
PUT    /api/admin/tenants/{uid}/feature-flags/{key}
DELETE /api/admin/tenants/{uid}/feature-flags/{key}
```

## Dependencies

- No dependency on OAuth/OIDC protocol
- Lives in `features/access/featureflags/` bounded context
