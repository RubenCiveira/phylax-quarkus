# Functional Extension — ABAC: Resource-Level Permissions

> Extracted from OAUTH_PLAN.md (was PLAN-34). Not part of the OAuth/OIDC implementation scope.
> This is a standalone product feature that belongs in `features/access/`.

---

## Description

Attribute-Based Access Control allowing applications to assign and check permissions
scoped to specific resource instances (e.g., `document:42:read`, `project:7:admin`).
Extends the current role-based model with fine-grained resource-scoped grants.

## Motivation

- Applications need per-resource authorization without baking it into their own DB
- Enables `POST /api/access/permissions/check` as a policy decision point
- Reduces boilerplate authorization logic in client applications

## Scope

New bounded context in `features/access/`:
- `resource-permission` — grant/revoke/check permissions by (subject, resource_type, resource_id, action)

Core operations:
- `GrantPermission(subject, resource_type, resource_id, action)`
- `RevokePermission(subject, resource_type, resource_id, action)`
- `CheckPermission(subject, resource_type, resource_id, action) → boolean`
- `ListPermissions(subject)` / `ListSubjects(resource)`

Data model:
- `access_resource_permission (uid, tenant, subject_type, subject_uid, resource_type, resource_uid, action, granted_at)`
- Index on `(tenant, subject_uid, resource_type, resource_uid, action)` for O(1) checks

## Dependencies

- Requires ORGANIZATIONS.md to be complete if org-scoped subjects are needed
- No dependency on OAuth/OIDC protocol
- Large scope — plan as a dedicated project/sprint after organizations are stable

## Open questions

- Subject types: user only, or also groups and API clients?
- Wildcard actions: `document:*:read` (all documents of type) or strict per-resource only?
- Token integration: include resource permissions as JWT claims (high-cardinality risk)?
