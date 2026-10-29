# Configuración

La configuración tiene tres niveles: la aplicación (ficheros `src/main/resources/config/*.yaml` y variables de entorno), cada tenant (`tenant-config`) y cada cliente (`oauth-client`).

## Aplicación

### OIDC (`config/oidc.yaml`)

| Propiedad | Por defecto | Efecto |
|---|---|---|
| `oidc.base-url` | (de la petición) | Host público para emisor, enlaces y orígenes WebAuthn. Vacío: se toma de la petición o de `X-Forwarded-*` |
| `oidc.jwt.authorization.key-duration` | `7d` (`3d` en el repositorio) | Vigencia de cada clave de firma |
| `oidc.jwt.authorization.key-futures` | `3` (`5` en el repositorio) | Claves futuras que se mantienen generadas |
| `oidc.jwt.authorization.rotation-cron` | `0 */15 * * * ?` | Frecuencia del job de rotación |
| `oidc.jwt.authorization.store-pass`, `store-pair` | — | Almacén de claves |
| `oidc.jwt.expiration.access-token` | (`5m` en el repositorio) | Vida del access token |
| `oidc.jwt.expiration.id-token` | `5m` | Vida del ID token |
| `oidc.jwt.expiration.mfa-token` | (`3m` en el repositorio) | Vida del `mfa_token` del grant `password` |
| `oidc.jwt.access-token.rfc9068` | `false` | Perfil `at+jwt` |
| `oidc.par.flow-ttl` | `10m` | Vida del vínculo `PAR_BINDING` |
| `oidc.pkce.require-for-confidential` | `false` | PKCE obligatorio también para confidenciales |
| `oidc.ui-locales` | `en` | Idiomas soportados para `ui_locales` |
| `oidc.pairwise.salt` (`OIDC_PAIRWISE_SALT`) | — | Sal del `sub` pairwise; sin ella ningún cliente puede ser pairwise. No rotar |

### Seguridad (`config/security.yaml`)

| Propiedad | Efecto |
|---|---|
| `security.digest.peppers` / `current` (`SECURITY_DIGEST_PEPPERS` / `SECURITY_DIGEST_CURRENT`) | *Pepper* del `SecretDigester`. Obligatorio fuera de dev/test |
| `security.secrets.keys` / `current` (`SECURITY_SECRETS_KEYS` / `SECURITY_SECRETS_CURRENT`) | Claves AES-GCM de `KeyRingSecretStorage`. Obligatorio fuera de dev/test |
| `security.acr.single-factor` / `multi-factor` | Valores ACR emitidos (`urn:lugh:acr:1fa` / `urn:lugh:acr:mfa`) |
| `app.rate-limit.*` | Límite de peticiones por IP (capacidad, recarga, caducidad, confianza en `X-Forwarded-For`) |

### Tareas programadas

| Propiedad | Por defecto | Tarea |
|---|---|---|
| `oauth.cleanup.expired.cron` | cada hora | Sesiones, PAR, device, anti-replay y demás caducados |
| `oauth.cleanup.temporal-codes.cron` | 3:00 | Codes de autorización caducados |
| `oauth.cleanup.audit-log.cron` | 4:00 | Registro de auditoría |

### Instalación (`migration/seed/oidc-seed.yaml`)

Solo se usa una vez, con la base de datos vacía (`InstallAccess`): crea el tenant raíz `main`, el usuario `ROOT` (con contraseña temporal), el servidor de recursos `civi-acme-api` y el cliente **público** `civi-acme-ui` con redirect y logout-redirect `http://localhost:4200/*`. Los secretos llegan por variables de entorno (`OIDC_ROOT_PASSWORD`, `OIDC_RELYING_PARTY_API_KEY`, `OIDC_API_KEYS_*`); si faltan, se generan y se muestran una vez en el log. Las claves `oidc.relying-party.*` y `oidc.trusted-client.*` conservan los nombres anteriores al renombrado del modelo.

## Tenant

Campos de `tenant-config` que lee el módulo OIDC:

| Campo | Efecto | Quién lo lee |
|---|---|---|
| `magic-link-expiry-minutes` | Caducidad del enlace mágico (desactivado, ver abajo) | `RequestMagicLinkUsecase` |
| `webauthn-rp-id`, `webauthn-rp-name` | Identidad del *relying party* WebAuthn; por defecto, host público y nombre del tenant | `WebAuthnController` |
| `session-sso-ttl-seconds` | Vida de la sesión SSO (3600 por defecto) | `AuthorizeHtml`, `OidcResponseBuilder` |
| `refresh-token-ttl-seconds` | Vida de cada refresh token (sin configuración del tenant, la del access token) | `ClaimsMapper` |
| `refresh-token-max-lifetime-seconds` | Vida absoluta de una familia de refresh (30 días si no hay configuración) | `ClaimsMapper` |
| `allow-password-grant` | Permite registrar el grant `password` por DCR | `DynamicClientAdapter` |
| `dynamic-registration-policy` | Activa DCR (ausente o `disabled`: desactivado) | `DynamicClientAdapter` |
| `dcr-allowed-grant-types`, `dcr-allowed-auth-methods` | Lo que se puede registrar por DCR | `DynamicClientAdapter` |
| `invitation-expiry-days` | Caducidad de las invitaciones | `InvitationCreateUsecase`, `InvitationResendUsecase` |

**Sin efecto** (ningún código los lee, o el adaptador de producción devuelve un valor fijo): `display-name`, `force-mfa`, `password-recovery-enabled` (`ChangePasswordAdapter.allowRecover` → `true`), `self-registration-enabled` y `auto-approve-registered-users` (`RegisterUserAdapter` muestra el enlace y cancela el alta), `require-email-verification`, `magic-link-enabled` (`MagicLinkEnabledAdapter` → `false`), `webauthn-enabled`, `acr-values-supported`, `require-pushed-authorization-requests` y las `*-email-template`. La lista funcional está en [Límites](../funcional/09-limites-y-pendientes.md#opciones-del-modelo-sin-efecto).

Otras entidades por tenant: `tenant-terms-of-use` (términos), `tenant-login-provider` (proveedores federados; `DelegatedAccessAuthValidatorAdapter` solo construye `GOOGLE`, con `public-key`/`private-key` como client ID y secreto, y `SAML`, con `saml-idp-entity-id`, `saml-idp-sso-url`, `saml-idp-cert` y el entity ID del SP en el `entity_id` del JSON de `metadata` o, si falta, el nombre del proveedor) y `consent-purpose` (propósitos RGPD). La guía para rellenarlas está en el [manual de administración](../user-manual/README.md).

## Cliente

Campos de `oauth-client` que aplica `OauthClientPolicy` al construir `ClientDetails`. La descripción funcional está en [Clientes](../funcional/02-clientes.md#configuración-que-se-aplica).

| Grupo | Campos |
|---|---|
| Identidad | `code` (= `client_id`), `client-name`, `enabled`, `tenant` (vacío = global), `dynamically-registered`, `registered-at` |
| Autenticación | `token-endpoint-auth-method`, `secret-oauth` (digest), `token-endpoint-auth-signing-alg`, `jwks-json`, `jwks-uri` |
| Flujo | `grant-types-json`, `response-types-json`, `require-pkce`, `require-pushed-authorization-requests`, `request-object-signing-alg` |
| Redirecciones y audiencias | `allowed-login-redirects`, `allowed-logout-redirects`, `allowed-audiences` |
| Scopes | `allow-all-scopes`, `client-credentials-scopes`, `client-credentials-token-ttl-seconds` |
| Tokens | `subject-type`, `sector-identifier-uri`, `dpop-bound-access-tokens`, `id-token-signed-response-alg`, `id-token-encrypted-response-alg/enc`, `userinfo-signed-response-alg`, `userinfo-encrypted-response-alg/enc`, `authorization-signed-response-alg` |
| Logout | `backchannel-logout-uri`, `backchannel-logout-session-required`, `frontchannel-logout-uri`, `frontchannel-logout-session-required` |
| Introspección | `introspection-allowed` |
| DCR | `registration-access-token` (digest), `client-uri`, `logo-uri`, `policy-uri`, `tos-uri` |
| Sin efecto hoy | `default-acr-values`, `default-max-age`, `authorization-details-types`, `backchannel-token-delivery-mode`, `backchannel-client-notification-endpoint`, `tls-client-auth-subject-dn`, `tls-client-certificate-bound-access-tokens` (CIBA, RAR y mTLS pospuestos) |
