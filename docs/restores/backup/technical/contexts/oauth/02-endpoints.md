# Catálogo de endpoints

Rutas que existen en el código, con su controlador y las pruebas HTTP que las ejercitan. Una ruta sin prueba HTTP lo indica.

## Protocolo (`/oauth/openid/{tenant}/…`)

| Método | Ruta | Controlador | Pruebas HTTP |
|---|---|---|---|
| GET | `.well-known/openid-configuration` (alias `configuration`) | `OpenIdConfigurationController` + `*Contributor` | `AuthorizeRequestFlowTest`, `DynamicClientHttpMariaDbTest`, `JoseResponsesFlowTest` |
| GET | `jwks` (global: `/oauth/jwks`) | `JwksController` | Sin prueba directa; las firmas se verifican en todas las pruebas de flujo |
| GET, POST | `authorize` (alias `auth`) | `AuthorizeHtml` | `AuthorizeRequestFlowTest`, `AuthorizeResponseContractTest`, `ConsentFlowTest`, `PkceFlowTest`, `PromptConsentFlowTest`, `ClaimsParameterFlowTest` |
| POST | `revoke` | `AuthorizeHtml.revokePreSession` (abandona el login) | `SessionManagementTest` |
| POST | `token` | `TokenController` | `TokenEndpointContractTest`, `TokenGrantSecurityTest`, `TokenGrantNegativeSecurityTest`, `TokenEndpointMariaDbTest`, `TokenExchangeFlowTest`, `DpopFlowTest` |
| GET, POST | `userinfo` | `InformationController` | `BearerContractMariaDbTest`, `JoseResponsesFlowTest`, `PairwiseSubjectFlowTest` |
| POST | `introspect` (alias `introspection`) | `IntrospectionController` | `TokenIntrospectionTest`, `BearerContractMariaDbTest` |
| POST | `revocation` | `TokenRevocationController` (RFC 7009) | `TokenEndpointContractTest`, `ClientAssertionFlowTest` |
| POST | `par` (alias `par-request`) | `DevicesAccessController` | `AuthorizeRequestFlowTest`, `DeviceAndParMariaDbTest` |
| POST | `device` | `DevicesAccessController` | `DeviceAndParMariaDbTest`, `TokenGrantNegativeSecurityTest` |
| GET, POST | `device/verify` | `DeviceVerificationHtml` | `DeviceAndParMariaDbTest` |
| POST | `ciba-auth` | `DevicesAccessController` | Siempre 403; no se anuncia |
| POST | `connect` (RFC 7591) | `ClientRegisterController` | `DynamicClientHttpMariaDbTest` |
| GET, PUT, DELETE | `connect/{clientId}` (RFC 7592) | `ClientRegisterController` | `DynamicClientHttpMariaDbTest` (GET), `DynamicClientRegistrationMariaDbTest` (todas, a nivel de caso de uso) |
| GET, POST | `logout` | `AuthorizeHtml` | `AuthorizeRequestFlowTest`, `ClientPolicyFlowTest`, `SessionLogoutMariaDbTest` |
| GET | `login-status-iframe`, `session-check` | `SessionManagementController` | `SessionStateFlowTest`, `SessionManagementTest` |

## Páginas de usuario (`/oauth/openid/{tenant}/…`)

| Ruta | Controlador | Pruebas HTTP |
|---|---|---|
| `recover` | `RecoverController` | `RecoverPasswordFlowTest` |
| `register` | `RegisterController` | `RegisterUserFlowTest` |
| `mfa-setup` | `MfaSetupController` | `MfaFlowTest`, `NewMfaFlowTest` |
| `magic-link/request`, `magic-link/verify` | `MagicLinkRequestController`, `MagicLinkVerifyHtml` | Sin prueba HTTP |
| `webauthn/{register,authenticate}/{begin,finish}` | `WebAuthnController` | Sin prueba HTTP |
| `invitation/accept` | `InvitationController` | Sin prueba HTTP; persistencia en `InvitationMariaDbTest` |
| `profile`, `me/**` | `ProfileHtmlController`, `ProfileMeController` | Sin prueba HTTP |

## Fuera del prefijo de tenant

| Ruta | Controlador | Uso |
|---|---|---|
| `GET`/`POST /oauth/openid/-/delegated/authorization/{provider}` | `DelegatedAccessController` | Callback de Google y ACS de SAML; reenvía al authorize del tenant (`DelegatedLoginFlowTest`) |
| `GET /oauth/jwks` | `JwksController` | JWKS global |
| `POST /oauth/api-key` | `ApiKeyController` | Validación de API keys |
| `/api/{tenant}/invitations[/{uid}[/resend]]` | `InvitationController` | Administración de invitaciones |
| `/api/{tenant}/access/users/{userUid}/sessions[/{sessionId}]` | `UserSessionController` | Administración de sesiones |

## Discovery

`OpenIdConfigurationController` reúne las aportaciones de cada `DiscoveryContributor`:

| Contributor | Paquete | Aporta |
|---|---|---|
| `AuthorizationEndpointContributor` | `authentication` | Authorize, token, response types/modes, grants, scopes, claims, `prompt`, ACR, `ui_locales`, PKCE, JAR, `subject_types_supported` |
| `UserinfoContributor` | `profile` | UserInfo y sus algoritmos de firma/cifrado |
| `TokenSecurityContributor` | `tokensecurity` | JWKS, introspección, revocación, métodos de autenticación, algoritmos, DPoP |
| `ParContributor` | `par` | PAR y `require_pushed_authorization_requests: false` |
| `DeviceContributor` | `device` | Device authorization endpoint |
| `SessionContributor` | `session` | `end_session_endpoint`, `check_session_iframe`, back- y front-channel |
| `ClientRegistrationContributor` | `client` | `registration_endpoint` |
| `MtlsDiscoveryContributor` | `oidc` | Nada mientras mTLS esté pospuesto |

Las listas salen de las constantes que usa la lógica (`JwtAlgorithms.SERVER_SIGNING`/`CLIENT_SIGNING`, `ClientAuthMethod.TOKEN_ENDPOINT`, `ClientJwtEncryptionGateway`, `AuthorizationRequestPolicy`), y DCR valida contra las mismas.

## Autenticación de las rutas `connect`

El initial access token y el registration access token son opacos. `RegistrationAwareJwtAuthMechanism` delega en el mecanismo JWT de Quarkus salvo en `/connect` y `/connect/{clientId}`; sin él, la autenticación proactiva los rechazaba con un 401 vacío antes de llegar al controlador.
