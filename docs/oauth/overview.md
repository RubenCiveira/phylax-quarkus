# OAuth / OIDC — Bounded context overview

## What does this module do?

Implements a multi-tenant **OAuth 2.0 / OIDC authorization server**. It exposes the endpoints
required for client applications (_relying parties_) to authenticate users and obtain tokens
via the Authorization Code flow (with PKCE).

This module is the only part of the system that owns the concepts of "authentication session"
and "pending challenge". All other parts of the platform consume the resulting tokens without
interacting with this module.

---

## Key concepts

| Concept | Description |
|---------|-------------|
| **Tenant** | Organization using the platform. Each tenant has its own users, clients and configuration. Appears in all paths: `/oauth/openid/{tenant}/...` |
| **Relying Party (RP)** | Application requesting authentication. Corresponds to the OAuth `client_id`. A token can have multiple audiences (RPs). |
| **Challenge** (`AuthenticationChallege`) | Additional requirement the user must satisfy before receiving a token: `PASSWORD`, `MFA`, `FRESH_PASSWORD`, `USE_CONSENT`, `CLIENT_CONSENT`. |
| **Pre-session** | `PRE_SESSION_ID` cookie that carries intermediate state between steps (pending challenges, username). Exists only while there are unresolved challenges. |
| **Auth session** | `AUTH_SESSION_ID` cookie representing an already-authenticated user session (allows re-use without re-login). |
| **Authorization code** | One-time code sent in the final redirect. The client exchanges it for tokens at `/token`. |

---

## Public endpoints

The module is spread across four controllers:

### `FrontAcessController` — Browser-facing HTML flows

| Method | Path | Description |
|--------|------|-------------|
| GET | `/oauth/openid/{tenant}/auth` | Starts the flow or shows the login form |
| POST | `/oauth/openid/{tenant}/auth` | Processes credentials and all intermediate challenge steps |
| GET | `/oauth/openid/{tenant}/recover` | Shows the new-password form (with recovery code pre-filled from URL) |
| POST | `/oauth/openid/{tenant}/recover` | Applies the password change using the recovery code |
| GET | `/oauth/openid/{tenant}/register` | Shows the email-verification form (with code pre-filled from URL) |
| POST | `/oauth/openid/{tenant}/register` | Verifies the registration code and completes registration |
| POST | `/oauth/openid/{tenant}/revocation` | Revokes an active token |
| GET | `/oauth/openid/{tenant}/logout` | Logs the user out |
| GET | `/oauth/openid/{tenant}/delegated-auth` | Callback from an external login provider |
| GET | `/oauth/openid/{tenant}/mfa-setup` | MFA method selector page |
| GET | `/oauth/openid/{tenant}/me` | Returns information about the authenticated user (HTML) |
| GET | `/oauth/openid/{tenant}/login-status-iframe` | Silent session check iframe |

### `AuthenticationController` — REST / token endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/oauth/openid/{tenant}/token` | Exchanges an authorization code (or other grants) for tokens |
| POST | `/oauth/openid/{tenant}/introspect` | Token introspection |

### `InformationController`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/oauth/openid/{tenant}/userinfo` | Returns claims for the authenticated user (OIDC UserInfo) |

### `DevicesAccessController` — Flows not yet implemented

| Method | Path | Description |
|--------|------|-------------|
| POST | `/oauth/openid/{tenant}/device` | Device Authorization Grant (returns 405) |
| POST | `/oauth/openid/{tenant}/par-request` | Pushed Authorization Request (returns 405) |
| POST | `/oauth/openid/{tenant}/ciba-auth` | CIBA (returns 405) |

### `DelegatedAccessController` — External provider token exchange

| Method | Path | Description |
|--------|------|-------------|
| POST | `/oauth/openid/{tenant}/delegated/token/{provider}` | Token exchange for delegated login providers |

---

## Two-module split

The code is divided into two packages with distinct responsibilities:

```
features/oauth/           → OIDC protocol layer (database-agnostic)
  authentication/         → HTTP flow, controllers, domain objects
  user/                   → User ports (gateway interfaces)
  client/                 → OAuth client ports

features/access/oauth/    → Concrete implementation (integrates with the database)
  application/            → Business domain usecases and services
  infrastructure/         → Interactors (port adapters)
```

`features/oauth/` defines **what** is needed (interfaces/ports) without knowing **how** data is
stored. `features/access/oauth/` provides the **how** by connecting to repositories, encryption,
notifications, etc.

This separation allows swapping the access implementation without touching the OIDC protocol logic.

---

## Code navigation

| I want to understand... | Start here |
|-------------------------|------------|
| The full browser-facing flow | [`FrontAcessController`](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/FrontAcessController.java) |
| How each challenge is resolved | [`part/`](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/part/) — see [controller-parts.md](controller-parts.md) |
| Token issuance and grants | [`AuthenticationController`](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/rest/AuthenticationController.java) |
| How credentials are validated | [`UserLoginUsecase`](../../src/main/java/net/civeira/phylax/features/access/oauth/application/usecase/UserLoginUsecase.java) |
| How an OAuth client is loaded | [`VerifyTrustedClientUsecase`](../../src/main/java/net/civeira/phylax/features/access/oauth/application/usecase/VerifyTrustedClientUsecase.java) |
| Port contracts | [`user/domain/gateway/`](../../src/main/java/net/civeira/phylax/features/oauth/user/domain/gateway/) and [`client/domain/gateway/`](../../src/main/java/net/civeira/phylax/features/oauth/client/domain/gateway/) |
| Flow sequence diagrams | [flows.md](flows.md) |
| Layered architecture | [architecture.md](architecture.md) |
| ControllerPart catalog | [controller-parts.md](controller-parts.md) |

---

## Integration tests

The OIDC flow tests use `@QuarkusTest` with CDI `@Alternative @Priority(1)` to replace real
implementations with configurable in-memory gateways.

```
src/test/java/net/civeira/phylax/testing/oauth/
  scenario/   → Alternative gateways (ScenarioLoginGateway, ScenarioConsentGateway, ...)
  alt/        → Alternative infrastructure beans (sessions, keys, cipher)
  client/     → OidcFlowClient — RestAssured helper for tests
  flow/       → Tests: LoginFlowTest, ConsentFlowTest, MfaFlowTest, ...
```

Run with:
```bash
mvn test -Dgroups="oidc-flow"
```
