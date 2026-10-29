# Functional Extension — Hierarchical Organizations and Groups

> Extracted from OAUTH_PLAN.md (was PLAN-33). Not part of the OAuth/OIDC implementation scope.
> This is a standalone product feature that belongs in `features/access/`.

---

## Description

Multi-level organization model: tenants contain organizations, organizations contain
groups, groups contain users. Enables enterprise directory-like RBAC where permissions
are inherited down the hierarchy.

## Motivation

- Enterprise customers need sub-tenant organization structures (departments, teams)
- Enables role and permission inheritance without per-user grants at every level
- Foundation for PLAN-34 (ABAC) if that is implemented later

## Scope

New bounded contexts in `features/access/`:
- `organization` — org aggregate, hierarchy (parent/child orgs), member management
- `group` — group aggregate, org membership, user membership

Data model additions:
- `access_organization (uid, tenant, parent_org, name, ...)`
- `access_group (uid, tenant, org, name, ...)`
- `access_group_member (group, user, joined_at)`
- `access_user_role_assignment` extended with optional `org` and `group` scope

## Dependencies

- No dependency on OAuth/OIDC protocol
- Must be complete before ABAC (see `ABAC.md`) if that is ever scheduled
- Large scope — plan as a dedicated project/sprint

## Open questions

- Maximum hierarchy depth (flat org + groups, or arbitrary nesting)?
- Permission inheritance model: additive (union) or scoped (most-specific wins)?
- API surface: management API only, or also user-facing org switcher UI?
