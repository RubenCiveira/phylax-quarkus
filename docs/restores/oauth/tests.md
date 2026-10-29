# OAuth / OIDC — Tests

Three layers, from fastest to closest to production. Each test class says in its Javadoc which
task (`docs/TODO-OIDC`) it verifies; this page does not keep a per-test catalogue.

| Layer | Where | What is real | Run |
|---|---|---|---|
| Unit | `src/test/java/.../features/oauth/**/*UnitTest` | Domain policies and use cases with Mockito | `mvn test` |
| HTTP flow (`@Tag("oidc-flow")`) | `integrationtest/features/oauth/flow` | Every Quarkus endpoint; login, clients, keys, codes, sessions and revocations are in-memory doubles (`alt/`, `scenario/`) | `mvn test -Dgroups=oidc-flow` |
| MariaDB (`@Tag("mariadb")`) | `integrationtest/persistence/*MariaDbTest` | Production SQL adapters on a throwaway `phylax_v1_*` schema; the `@QuarkusTest` ones also go through HTTP | `make test-mariadb` |

The MariaDB layer is **skipped, not failed**, without `V1_MARIADB_URL` (server URL without a
database), `V1_MARIADB_USER` and `V1_MARIADB_PASSWORD`. `make test-mariadb` takes them from `.env`;
the user must be allowed to create and drop databases. In CI (`.github/workflows/ci.yml`) a MariaDB
service backs both the test profile and the harness, so `mvn test` runs the three layers.

## Fixtures

- `OidcTestFixtures`: two tenants (`test-tenant`, `other-tenant`), a legacy client that is both
  confidential and public (`test-client`), a confidential web client (`web-client`), a public SPA
  (`spa-client`), a `private_key_jwt` client (`jwt-client`), one exact redirect and one user.
- `FixedClientStoreGateway`: the clients above, adjustable per test (grants, scopes, PAR, JWKS,
  pairwise).
- `ScenarioLoginGateway` and the other `scenario/` gateways: login, consent, MFA and delegated login
  driven by the test. The login double always opens sessions in `test-tenant`.
- `MariaDbFlowProfile`: excludes the doubles of codes, revocations, sessions, token lookup and
  pushed requests, so those stores are the SQL ones. Device codes and DCR never had doubles.
  `MariaDbSchema` creates and drops the schema; `Concurrently.run(n, call)` releases `n` threads at
  once, each with its own connection.

## What the MariaDB layer proves

| Test | Guarantee |
|---|---|
| `TokenEndpointMariaDbTest` | One winner among concurrent code exchanges and refresh rotations; codes and refresh tokens of another client, tenant or redirect are rejected without being spent |
| `TemporalKeysSqlAdapterMariaDbTest`, `TokenRevocationSqlAdapterMariaDbTest` | Atomic code and JTI consumption; family revocation scoped to its tenant |
| `BearerContractMariaDbTest` | Bearer consumers (introspection, UserInfo) share revocation state; another tenant or purpose never introspects as active |
| `SessionLogoutMariaDbTest` | Logout revokes only the session's own grants; front- and back-channel reach only participating clients |
| `DeviceAndParMariaDbTest` | Device approval needs the form token and a session of the tenant; an approved device code is redeemed once and only in its tenant; a pushed request is consumed once and only in its tenant |
| `DynamicClientRegistrationMariaDbTest`, `DynamicClientHttpMariaDbTest` | DCR credentials of the tenant only, atomic use count, metadata errors that spend nothing, keys and algorithms stored in the runtime columns, no secret on reads |
| `InvitationMariaDbTest`, `RecoveryCodeMariaDbTest` | Single use under concurrency |

## Not covered yet

- HTTP tests for magic link, WebAuthn, invitation acceptance and the profile pages.
- Login and clients through the real adapters in the HTTP + MariaDB layer (still doubles).
- A controlled clock and fixed keys shared with the PHP suite (task V.1).
- The OpenID Conformance Suite: harness in [conformance/](../../conformance/README.md), not run yet
  (task V.2).
