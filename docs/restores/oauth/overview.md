# OAuth / OIDC — Overview

Multi-tenant OAuth 2.0 / OpenID Connect authorization server. Every protocol route lives under
`/oauth/openid/{tenant}/…`, and the issuer of a tenant is `{public host}/oauth/openid/{tenant}`.
The public host comes from `X-Forwarded-*` when the server runs behind a proxy.

> **Status (2026-10-27, task 2.10).** This page only lists routes that exist in the code. The
> "Verified by" column names the tests that exercise each one over HTTP; a route without an HTTP
> test says so. **No conformance is claimed:** the OpenID Conformance Suite has not been run yet
> (see [conformance/README.md](../../conformance/README.md) and task V.2). Per-task detail and
> evidence: [docs/TODO-OIDC](../TODO-OIDC/README.md).

---

## Protocol endpoints

| Method | Path (`/oauth/openid/{tenant}/…`) | Controller | Verified by |
|---|---|---|---|
| GET | `.well-known/openid-configuration` (alias `configuration`) | `OpenIdConfigurationController` + `*Contributor` | `AuthorizeRequestFlowTest`, `DynamicClientHttpMariaDbTest`, `JoseResponsesFlowTest` |
| GET | `jwks` (global: `/oauth/jwks`) | `JwksController` | no direct HTTP test; signatures are checked in every flow test |
| GET, POST | `auth` (alias `authorize`) | `AuthorizeHtml` | `AuthorizeRequestFlowTest`, `AuthorizeResponseContractTest`, `ConsentFlowTest`, `PkceFlowTest`, `PromptConsentFlowTest`, `ClaimsParameterFlowTest` |
| POST | `token` | `TokenController` | `TokenEndpointContractTest`, `TokenGrantSecurityTest`, `TokenGrantNegativeSecurityTest`, `TokenEndpointMariaDbTest` |
| GET, POST | `userinfo` | `InformationController` | `BearerContractMariaDbTest`, `JoseResponsesFlowTest`, `PairwiseSubjectFlowTest` |
| POST | `introspect` (alias `introspection`) | `IntrospectionController` | `TokenIntrospectionTest`, `BearerContractMariaDbTest` |
| POST | `revocation` | `TokenRevocationController` | `TokenEndpointContractTest`, `ClientAssertionFlowTest` |
| POST | `par-request` (alias `par`) | `DevicesAccessController` | `AuthorizeRequestFlowTest`, `DeviceAndParMariaDbTest` |
| POST | `device` | `DevicesAccessController` | `DeviceAndParMariaDbTest`, `TokenGrantNegativeSecurityTest` |
| GET, POST | `device/verify` | `DeviceVerificationHtml` | `DeviceAndParMariaDbTest` |
| POST | `connect` (RFC 7591) | `ClientRegisterController` | `DynamicClientHttpMariaDbTest` |
| GET, PUT, DELETE | `connect/{clientId}` (RFC 7592) | `ClientRegisterController` | `DynamicClientHttpMariaDbTest` (GET), `DynamicClientRegistrationMariaDbTest` (all, at use case level) |
| GET, POST | `logout` | `AuthorizeHtml` | `AuthorizeRequestFlowTest`, `ClientPolicyFlowTest`, `SessionLogoutMariaDbTest` |
| GET | `login-status-iframe`, `session-check` | `SessionManagementController` | `SessionStateFlowTest`, `SessionManagementTest` |
| POST | `ciba-auth` | `DevicesAccessController` | always 403, not announced (task 4.3 postponed) |

## User-facing pages

| Path (`/oauth/openid/{tenant}/…`) | Controller | Verified by |
|---|---|---|
| `recover` | `RecoverController` | `RecoverPasswordFlowTest` |
| `register` | `RegisterController` | `RegisterUserFlowTest` |
| `mfa-setup` | `MfaSetupController` | `MfaFlowTest`, `NewMfaFlowTest` |
| `magic-link/request`, `magic-link/verify` | `MagicLinkRequestController`, `MagicLinkVerifyHtml` | no HTTP test |
| `webauthn/{register,authenticate}/{begin,finish}` | `WebAuthnController` | no HTTP test |
| `invitation/accept` | `InvitationController` | no HTTP test; persistence in `InvitationMariaDbTest` |
| `profile`, `me/**` | `ProfileHtmlController`, `ProfileMeController` | no HTTP test |
| `/oauth/openid/-/delegated/authorization/{provider}` | `DelegatedAccessController` | `DelegatedLoginFlowTest` |

---

## What the discovery announces

Each value comes from the constant the logic uses (task 1.1), so the metadata cannot drift from the
behaviour:

| Metadata | Source |
|---|---|
| `id_token_signing_alg_values_supported`, `userinfo_signing_alg_values_supported`, `authorization_signing_alg_values_supported` | `JwtAlgorithms.SERVER_SIGNING` (RS256) |
| `request_object_signing_alg_values_supported`, `token_endpoint_auth_signing_alg_values_supported` | `JwtAlgorithms.CLIENT_SIGNING` (RS256, ES256) |
| `token_endpoint_auth_methods_supported` | `ClientAuthMethod.TOKEN_ENDPOINT` (`client_secret_basic`, `client_secret_post`, `private_key_jwt`, `none`) |
| `*_encryption_alg_values_supported`, `*_encryption_enc_values_supported` | `ClientJwtEncryptionGateway` |
| `subject_types_supported` | `public`, plus `pairwise` when `OIDC_PAIRWISE_SALT` is set |
| `registration_endpoint` | `ClientRegistrationContributor` |

DCR validates the same constants (`DcrMetadataPolicy`), so a client can register only keys and
algorithms the server announces (task V.3).

---

## Dynamic Client Registration

- The tenant enables it with `dynamic-registration-policy` (absent or `disabled`: 403). There is no
  open mode: every registration needs an **initial access token** of the tenant, created by an
  administrator and stored as a digest (task 2.1).
- Accepted metadata: grants and response types the tenant allows, `token_endpoint_auth_method`
  (`client_secret_basic`, `client_secret_post`, `private_key_jwt`, `none`), exact redirect and
  logout URIs, `jwks` (public RSA/EC keys only) **or** `jwks_uri` (HTTPS), the signing and
  encryption algorithms above, `subject_type` (`pairwise` only with the salt and a single redirect
  host), and the flags `backchannel_logout_session_required`,
  `frontchannel_logout_session_required`, `dpop_bound_access_tokens` and
  `require_pushed_authorization_requests`.
- Rejected with `invalid_client_metadata`: `private_key_jwt`, request objects or encryption without
  keys, `sector_identifier_uri` (sector documents are not fetched) and
  `tls_client_certificate_bound_access_tokens` (mTLS postponed, task 4.1). A rejected request spends
  no use of the initial access token.
- The secret is returned only once, and only to `client_secret_*` clients. Reads never return it.
- The initial and registration access tokens are opaque, so `RegistrationAwareJwtAuthMechanism`
  keeps the JWT mechanism of the API away from `connect` routes.

---

## Code navigation

Each sub-package of `features/oauth` documents itself in its `package-info.java`:

| Area | Package |
|---|---|
| Authorization endpoint, login steps, token endpoint, grants | `authentication` |
| Clients, client authentication, DCR | `client` |
| Token signing, verification, introspection, revocation, JWKS | `tokensecurity` |
| Discovery | `oidc` |
| Device grant | `device` |
| Pushed authorization requests | `par` |
| Browser sessions and Session Management | `session` |

Test layers and how to run them: [tests.md](tests.md).
