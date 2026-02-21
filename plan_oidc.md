# Plan de Refactorización: OAuth → Diseño OIDC por Sub-módulos

## Contexto

El paquete actual `features/oauth/` implementa un servidor OAuth 2.0 / OpenID Connect pero con una estructura de sub-módulos que no refleja la separación de responsabilidades descrita en `diseño_oidc.md`. Este plan describe la migración incremental hacia esa arquitectura.

### Estructura actual → Estructura objetivo

```
ACTUAL                          OBJETIVO
───────────────────────         ────────────────────────────────
oauth/
├── token/          ──────────► key/         (gestión RSA + firma JWT)
│                  ╲──────────► authentication/ (builders de token)
├── rbac/           ──────────► rbac/        (sin cambios de fondo)
│                  ╲──────────► scopes/      (consentimiento de scopes - nuevo)
├── delegated/      ──────────► delegated/   (renombrado + alineado)
├── authentication/ ──────────► authentication/ (solo orquestación OIDC)
│                  ╲──────────► session/     (sesiones y códigos temporales)
│                  ╲──────────► user/        (casos de uso de usuario)
│                  ╲──────────► mfa/         (MFA como sub-módulo propio)
│                  ╲──────────► common/      (utilidades compartidas)
└── client/         ──────────► client/      (alineado + ApiKey)
```

---

## Fase 1: Extraer sub-módulo `key/`

**Objetivo:** Separar la gestión del ciclo de vida de claves RSA y la firma JWT en su propio sub-módulo, equivalente al sub-módulo `Key` del diseño.

**Estado actual:** `token/JwtTokenManager.java` mezcla gestión de claves y firma. `token/gateway/TokenStoreGateway.java` define la persistencia. `token/infrastructure/driven/TokenRepositoryJdbcAdapter.java` implementa la persistencia JDBC.

### Tareas

**T1.1 — Crear estructura de paquetes `key/`**
- Crear los paquetes: `key/domain/gateway/`, `key/domain/model/`, `key/application/`, `key/infrastructure/driven/`, `key/infrastructure/driver/rest/`

**T1.2 — Migrar el port de persistencia de claves**
- Mover `token/gateway/TokenStoreGateway.java` → `key/domain/gateway/TokenStoreGateway.java`
- Añadir al port el método `listKeys(tenant)` y `nextKeysExpiration(tenant)` si no existen

**T1.3 — Crear value objects del dominio `Key`**
- Crear `key/domain/model/KeyPair.java`: `kid`, `alg` (RS256), `keyUse` (sig), `privateKey`, `publicKey` (PEM)
- Crear `key/domain/model/KeyConfig.java`: `ttl` (7 días por defecto), `futures` (3 claves futuras)

**T1.4 — Crear el port `TokenSigner`**
- Crear interfaz `key/domain/gateway/TokenSigner.java` con métodos:
  - `sign(tenant, data, expiration): String`
  - `keysAsJwks(tenant): JwkSet`
  - `signKeypass(tenant, data, exp): String`
  - `verifyTokenPayload(tenant, token): Map<String, Object>`
  - `verifiedKeypass(tenant, token): String`
- Este port sustituye al uso directo de `JwtTokenManager` en el resto del código

**T1.5 — Migrar y refactorizar el adaptador de firma**
- Mover `token/JwtTokenManager.java` → `key/infrastructure/driven/JoseTokenSigner.java` implementando `TokenSigner`
- Añadir lógica de auto-rotación: si la expiración más lejana es menor que `futures * ttl`, generar claves nuevas
- Los modelos `KeyInformation.java`, `PublicKeyInformation.java`, `Jks.java` se mueven a `key/domain/model/`

**T1.6 — Migrar el adaptador JDBC de claves**
- Mover `token/infrastructure/driven/TokenRepositoryJdbcAdapter.java` → `key/infrastructure/driven/TokenStoreSqlAdapter.java`
- Implementar `TokenStoreGateway` del nuevo paquete

**T1.7 — Crear el controlador JWKS**
- Crear `key/infrastructure/driver/rest/JwksController.java`
  - `GET /openid/{tenant}/jwks` — devuelve el JWK Set
  - Cachear respuesta 1h con ETag
- Mover esta funcionalidad desde `InformationController` si existe ahí

**T1.8 — Actualizar referencias**
- Actualizar todos los imports en `authentication/` y `token/` que usaban directamente `JwtTokenManager` para usar el port `TokenSigner`
- Eliminar el paquete `token/` una vez que no queden referencias

---

## Fase 2: Extraer sub-módulo `session/`

**Objetivo:** Separar la persistencia de sesiones y códigos temporales en su propio sub-módulo.

**Estado actual:** `SessionStoreGateway` y `TemporalKeysGateway` viven en `authentication/domain/gateway/`. Sus adaptadores JDBC están en `authentication/infrastructure/driven/`.

### Tareas

**T2.1 — Crear estructura de paquetes `session/`**
- Crear: `session/domain/gateway/`, `session/domain/model/`, `session/infrastructure/driven/`

**T2.2 — Crear value objects del dominio `Session`**
- Crear `session/domain/model/SessionInfo.java`: `csid`, `withMfa`, `issuer`, `userId`, `clientId`
- Crear `session/domain/model/TemporalAuthCode.java`: `AuthenticationResult data`, `ClientData client`, `nonce`, `AuthenticationRequest request` — se serializa a JSON para authorization codes

**T2.3 — Migrar el port de sesiones**
- Mover `authentication/domain/gateway/SessionStoreGateway.java` → `session/domain/gateway/SessionStoreGateway.java`
- Asegurar que expone: `loadSession`, `saveSession`, `updateSession`, `deleteSession`

**T2.4 — Migrar el port de claves temporales y auth codes**
- Mover `authentication/domain/gateway/TemporalKeysGateway.java` → `session/domain/gateway/TemporalKeysGateway.java`
- Asegurar que expone claves simétricas rotativas (HS256/AES): `currentKey`, `encrypt`, `verifyCypher`, `verifyToken`
- Asegurar que expone auth codes one-time: `registerTemporalAuthCode` (TTL 3 min), `retrieveTemporalAuthCode`

**T2.5 — Migrar los adaptadores JDBC**
- Mover `authentication/infrastructure/driven/SessionStoreJdbcAdapter.java` → `session/infrastructure/driven/SessionStoreSqlAdapter.java`
- Mover `authentication/infrastructure/driven/TemporalKeysJdbcAdapter.java` → `session/infrastructure/driven/TemporalKeysSqlAdapter.java`
- Documentar las tablas que usan: `_oauth_session`, `_oauth_temporal_keys`, `_oauth_temporal_codes`

**T2.6 — Actualizar referencias en `authentication/`**
- Cambiar imports de `SessionStoreGateway` y `TemporalKeysGateway` en todos los puntos de uso

---

## Fase 3: Crear sub-módulo `mfa/`

**Objetivo:** Aislar la lógica de Multi-Factor Authentication (TOTP) en su propio sub-módulo con dominio, aplicación e infraestructura propios.

**Estado actual:** La MFA está dispersa: `UserMfaConfigSpi` en `authentication/application/spi/`, `MfaGranter` en `authentication/domain/granter/`, `MfaControllerPart` y `NewMfaControllerPart` en `authentication/infrastructure/driver/html/part/`.

### Tareas

**T3.1 — Crear estructura de paquetes `mfa/`**
- Crear: `mfa/domain/gateway/`, `mfa/domain/model/`, `mfa/application/`, `mfa/infrastructure/driven/`

**T3.2 — Crear el port `UserMfaGateway`**
- Crear `mfa/domain/gateway/UserMfaGateway.java` con métodos:
  - `configurationForNewMfa(tenant, username): PublicLoginMfaBuildResponse`
  - `verifyOtp(tenant, username, otp): boolean`
  - `verifyNewOtp(tenant, username, seed, otp): boolean`
  - `storeSeed(tenant, username, seed): void`

**T3.3 — Crear value object `PublicLoginMfaBuildResponse`**
- Crear `mfa/domain/model/PublicLoginMfaBuildResponse.java`: `seed`, `message?`, `image?` (data URI del QR), `url?`

**T3.4 — Crear el application service `UserMfa`**
- Crear `mfa/application/UserMfa.java`: fachada sobre `UserMfaGateway` que expone los métodos del port
- Este es el punto de entrada que usará `MfaControllerPart` y `NewMfaControllerPart`

**T3.5 — Crear el adaptador de MFA**
- Crear `mfa/infrastructure/driven/UserMfaAdapter.java` implementando `UserMfaGateway`
- Integrar con la librería TOTP (la que usa el proyecto actualmente)
- Leer el seed cifrado del usuario via la capa de `access`
- Generar QR code como data URI

**T3.6 — Refactorizar `MfaGranter`**
- Mover `authentication/domain/granter/MfaGranter.java` → `mfa/domain/` o eliminarlo si su lógica pasa al step `UseMfaForm`
- `MfaGranter` delega la verificación del OTP a `UserMfa` en lugar de `UserMfaConfigSpi`

**T3.7 — Actualizar `MfaControllerPart` y `NewMfaControllerPart`**
- Cambiar la inyección de `UserMfaConfigSpi` por `UserMfa` del sub-módulo `mfa/`

### Cambios realizados

- **T3.1 ✅** — Creados paquetes `mfa/domain/gateway/`, `mfa/domain/model/`, `mfa/application/`, `mfa/infrastructure/driven/`
- **T3.2 ✅** — Creado `mfa/domain/gateway/UserMfaGateway.java` con los cuatro métodos del diseño
- **T3.3 ✅** — Creado `mfa/domain/model/PublicLoginMfaBuildResponse.java` (`@Getter @Builder`: `seed`, `image`, `url`, `message`)
- **T3.4 ✅** — Creado `mfa/application/UserMfa.java` (`@ApplicationScoped`) delegando a `UserMfaGateway`
- **T3.5 ✅** — Creado `mfa/infrastructure/driven/UserMfaAdapter.java` con patrón bridge: implementa `UserMfaGateway` delegando a `UserMfaConfigSpi` existente
- **T3.6 ✅** — `MfaGranter` actualizado: inyecta `UserMfa` en lugar de `UserMfaConfigSpi`
- **T3.7 ✅** — `MfaControllerPart` y `NewMfaControllerPart` actualizados para inyectar `UserMfa`

---

## Fase 4: Crear sub-módulo `user/`

**Objetivo:** Centralizar los casos de uso del usuario OIDC (login, consent, registro, cambio de password) con sus ports y use cases propios, separándolos de la orquestación del flujo.

**Estado actual:** Los SPIs `UserLoginSpi`, `UserConsentSpi`, `UserPasswordChangeSpi`, `RecoverPasswordSpi` están en `authentication/application/spi/`. No hay un sub-módulo `user/` independiente.

### Tareas

**T4.1 — Crear estructura de paquetes `user/`**
- Crear: `user/domain/gateway/`, `user/domain/model/`, `user/application/`, `user/infrastructure/driven/`

**T4.2 — Crear `LoginGateway`**
- Crear `user/domain/gateway/LoginGateway.java` (renombrando y ampliando `UserLoginSpi`):
  - `validatedUserData(tenant, username, password, client): AuthenticationResult`
  - `fillPreLoadById(tenant, client, challenges): AuthenticationResult`
  - `fillPreAuthenticated(tenant, client, challenges): AuthenticationResult`

**T4.3 — Crear `ConsentGateway`**
- Crear `user/domain/gateway/ConsentGateway.java` (desde `UserConsentSpi`):
  - `getPendingConsent(tenant, username): String?`
  - `storeAcceptedConsent(tenant, username): void`

**T4.4 — Crear `ChangePasswordGateway`**
- Crear `user/domain/gateway/ChangePasswordGateway.java` (unificando `UserPasswordChangeSpi` y `RecoverPasswordSpi`):
  - `requestForChange(url, tenant, username): void`
  - `allowRecover(tenant): boolean`
  - `validateChangeRequest(tenant, code, newPass): String?` (devuelve username)
  - `forceUpdatePassword(tenant, username, oldPass, newPass): boolean`

**T4.5 — Crear `RegisterUserGateway`**
- Crear `user/domain/gateway/RegisterUserGateway.java`:
  - `allowRegister(tenant): boolean`
  - `getRegisterConsent(tenant): String?`
  - `requestForRegister(url, tenant, email, password): void`
  - `verifyRegister(tenant, code): String?` (devuelve username)

**T4.6 — Crear el value object `PublicLoginAuthResponse`**
- Crear `user/domain/model/PublicLoginAuthResponse.java`: `tenant`, `authData[]` (claims del access token), `authExpiration`, `idData[]` (claims del id_token), `idExpiration`, `sessionId`, `sessionExpiration`
- Este VO es el resultado final de una autenticación exitosa, equivalente al actual `AutorizationToken` o `AuthenticationResult` exitoso

**T4.7 — Crear los application use cases**
- Crear `user/application/LoginUsecase.java`: envuelve `LoginGateway`, despacha evento tras login válido
- Crear `user/application/ConsentUsecase.java`: pasarela sobre `ConsentGateway`
- Crear `user/application/ChangePasswordUsecase.java`: pasarela sobre `ChangePasswordGateway`
- Crear `user/application/RegisterUserUsecase.java`: pasarela sobre `RegisterUserGateway`

**T4.8 — Crear adaptadores en `user/infrastructure/`**
- Crear `user/infrastructure/driven/LoginAdapter.java` implementando `LoginGateway`
  - Secuencia de checks: `checkTenant → checkUser → checkPassword → checkTemporalPassword → checkMfaRequired → checkMfa → checkTerms → checkScopesConsent → markLoginOk`
  - Gestión de intentos fallidos (3 max → bloqueo 6h)
  - Carga de roles por audience (TrustedClient/RelyingParty + roles globales)
  - Extraer esta lógica de `PasswordGranter` y `UserLoginSpi` actuales
- Crear `user/infrastructure/driven/ConsentAdapter.java` implementando `ConsentGateway`
- Crear `user/infrastructure/driven/ChangePasswordAdapter.java` implementando `ChangePasswordGateway`
- Crear `user/infrastructure/driven/RegisterUserAdapter.java` implementando `RegisterUserGateway`

**T4.9 — Eliminar SPIs obsoletos**
- Eliminar `authentication/application/spi/UserLoginSpi.java`
- Eliminar `authentication/application/spi/UserConsentSpi.java`
- Eliminar `authentication/application/spi/UserPasswordChangeSpi.java`
- Eliminar `authentication/application/spi/RecoverPasswordSpi.java`
- Actualizar todos los puntos de uso

### Cambios realizados

- **T4.1 ✅** — Creados paquetes `user/domain/gateway/`, `user/domain/model/`, `user/application/`, `user/infrastructure/driven/`
- **T4.2 ✅** — Creado `user/domain/gateway/LoginGateway.java` con `validatedUserData`, `fillPreLoadById`, `fillPreAuthenticated`
- **T4.3 ✅** — Creado `user/domain/gateway/ConsentGateway.java` con `getPendingConsent`, `storeAcceptedConsent`
- **T4.4 ✅** — Creado `user/domain/gateway/ChangePasswordGateway.java` unificando password change + recover
- **T4.5 ✅** — Creado `user/domain/gateway/RegisterUserGateway.java`
- **T4.7 ✅** — Creados: `user/application/LoginUsecase.java`, `ConsentUsecase.java`, `ChangePasswordUsecase.java`, `RegisterUserUsecase.java`
- **T4.8 ✅** — Creados adaptadores bridge: `LoginAdapter.java`, `ConsentAdapter.java`, `ChangePasswordAdapter.java`, `RegisterUserAdapter.java` (cada uno envuelve su SPI correspondiente)
- **T4.9 ⏸** — SPIs originales conservados (necesarios para implementaciones externas); se eliminan cuando dejen de tener implementadores activos
- Actualizados `AuthenticationController`, `DelegatedAccessGranter`, `FrontAcessController`, `RefreshGranter` para inyectar `LoginUsecase` en lugar de `UserLoginSpi`
- Corregido bug de nombres de método: `validateUserData` → `validatedUserData`, `validatePreAuthenticated` → `fillPreAuthenticated`

---

## Fase 5: Crear sub-módulo `scopes/`

**Objetivo:** Añadir el soporte de consentimiento granular de scopes OAuth como sub-módulo independiente. Separar del RBAC actual (que gestiona recursos/roles) el flujo de consentimiento de scopes por usuario+cliente.

**Estado actual:** `rbac/` gestiona recursos y roles. No existe un sub-módulo de consentimiento de scopes. El diseño requiere `ScopesConsentGateway` y `ScopePermission`.

### Tareas

**T5.1 — Crear estructura de paquetes `scopes/`**
- Crear: `scopes/domain/gateway/`, `scopes/domain/model/`, `scopes/application/`, `scopes/infrastructure/driven/`

**T5.2 — Crear el value object `ScopePermission`**
- Crear `scopes/domain/model/ScopePermission.java`: `scope`, `required`, `label?`, `description?`

**T5.3 — Crear el port `ScopesConsentGateway`**
- Crear `scopes/domain/gateway/ScopesConsentGateway.java`:
  - `pendingScopes(tenant, username, clientId, scopes[]): List<ScopePermission>`
  - `storeAcceptedScopes(tenant, username, clientId, scopes[]): void`

**T5.4 — Crear `ScopesConsentUsecase`**
- Crear `scopes/application/ScopesConsentUsecase.java`:
  - Normaliza scopes (split por espacios, dedup)
  - Delega a `ScopesConsentGateway`

**T5.5 — Crear adaptador (stub inicial)**
- Crear `scopes/infrastructure/driven/ScopesConsentAdapter.java` implementando `ScopesConsentGateway`
- Implementación inicial: `pendingScopes` devuelve lista vacía, `storeAcceptedScopes` es no-op
- Documentar con TODO para implementación completa con persistencia real

**T5.6 — Integrar `ScopesConsentForm` en `authentication/`**
- En el flujo de autenticación (`LoginAdapter.checkScopesConsent`), llamar a `ScopesConsentUsecase.pendingScopes`
- El step `ScopesConsentForm` usará `ScopesConsentUsecase`

### Cambios realizados

- **T5.1 ✅** — Creados paquetes `scopes/domain/gateway/`, `scopes/domain/model/`, `scopes/application/`, `scopes/infrastructure/driven/`
- **T5.2 ✅** — Creado `scopes/domain/model/ScopePermission.java` (`@Getter @Builder`: `scope`, `required`, `label`, `description`)
- **T5.3 ✅** — Creado `scopes/domain/gateway/ScopesConsentGateway.java` con `pendingScopes` y `storeAcceptedScopes`
- **T5.4 ✅** — Creado `scopes/application/ScopesConsentUsecase.java` (`@ApplicationScoped`) con normalización de scopes (split por espacios, dedup)
- **T5.5 ✅** — Creado `scopes/infrastructure/driven/ScopesConsentAdapter.java` (stub: `pendingScopes` devuelve lista vacía, `storeAcceptedScopes` es no-op; marcado con TODO)
- **T5.6 ⏸** — Integración en `LoginAdapter` pendiente (requiere reescritura completa del adaptador)

---

## Fase 6: Alinear sub-módulo `delegated/`

**Objetivo:** Renombrar y reorganizar `delegated/` para alinear los nombres de clases y la estructura interna con el diseño del sub-módulo `DelegateLogin`.

**Estado actual:** `delegated/` existe con `DelegatedAccessExternalProvider`, `DelegatedAccessDescriptor`, `DelegatedRequestDetails`, `DelegatedStoreGateway`, `GoogleDelegatedAccessProvider`, `SamlDelegatedAccessProvider`, `DelegatedAccessAuthValidatorSpi`.

### Tareas

**T6.1 — Renombrar modelos de dominio**
- Renombrar `DelegatedAccessDescriptor` → `DelegatedProviderDescription` (añadir campo `automatic`)
- Renombrar `DelegatedRequestDetails` → `DelegatedLoginEndpoint` (añadir campo `method`: GET/POST)
- Alinear `DelegatedAccessExternalProvider` con la interfaz `DelegatedLoginProvider` del diseño:
  - Método `info(): DelegatedProviderDescription`
  - Método `delegatedUrl(redirect, state): DelegatedLoginEndpoint`
  - Método `authorize(redirect, request): DelegatedUserData?`
- Crear `DelegatedUserData.java`: `code`, `name`, `email`

**T6.2 — Renombrar el port**
- Renombrar `DelegatedStoreGateway` → `DelegateLoginGateway` añadiendo métodos:
  - `save(tenant, client, user, providerId): AuthenticationResult`
  - `providers(tenant): List<DelegatedLoginProvider>`
  - `getProvider(tenant, id): DelegatedLoginProvider`

**T6.3 — Crear `DelegateLogin` application service**
- Crear `delegated/application/DelegateLogin.java`:
  - `providers(tenant): List<DelegatedLoginProvider>`
  - `getTargetEndpoint(tenant, providerId, redirect, state): DelegatedLoginEndpoint`
  - `validateRedirection(tenant, client, providerId, request): AuthenticationResult`

**T6.4 — Actualizar el adaptador JDBC**
- Actualizar `DelegateRepositoryJdbcAdapter.java` → `DelegateLoginAdapter.java` implementando `DelegateLoginGateway`
- El `save` debe crear al usuario automáticamente si no existe (vía access feature)

**T6.5 — Actualizar referencias en `authentication/`**
- Actualizar `DelegatedControllerPart` y `DelegatedAccessController` para usar `DelegateLogin` application service

### Cambios realizados

- **T6.1 ✅** — Creado `delegated/domain/model/DelegatedLoginEndpoint.java` (`@Getter @Builder`: `provider`, `externalUrl`, `method`) sustituyendo a `DelegatedRequestDetails`
- **T6.2 ✅** — Creado `delegated/domain/gateway/DelegateLoginGateway.java`: port unificado que combina `DelegatedStoreGateway` + `DelegatedAccessAuthValidatorSpi` (`loadToken`, `saveToken`, `providers`, `retrieveUsername`)
- **T6.3 ✅** — Creado `delegated/application/DelegateLogin.java` (`@ApplicationScoped`): servicio de aplicación con `providers`, `getRequestInfo`, `processResponse`, `saveToken`, `resolveUsername(request, code, provider)` y overload sin provider `resolveUsername(request, code)` (para granters sin provider explícito)
- **T6.4 ✅** — Creado `delegated/infrastructure/driven/DelegateLoginAdapter.java`: adapter bridge que implementa `DelegateLoginGateway` delegando en `DelegatedStoreGateway` + `DelegatedAccessAuthValidatorSpi`
- **T6.5 ✅** — Actualizados:
  - `DelegatedControllerPart`: inyecta `DelegateLogin` (antes: `DelegatedStoreGateway + DelegatedAccessAuthValidatorSpi`)
  - `DelegatedAccessController`: inyecta `DelegateLogin`, usa `DelegatedLoginEndpoint`
  - `FrontAcessController`: inyecta `DelegateLogin` en lugar de `DelegatedAccessAuthValidatorSpi`
  - `DelegatedAccessGranter`: inyecta `DelegateLogin`, usa overload `resolveUsername(request, code)` sin provider
- Corregido bug potencial: el overload sin provider evita `t.getProvider().equals(null)` que lanzaría NPE

---

## Fase 7: Crear sub-módulo `common/`

**Objetivo:** Centralizar utilidades compartidas entre sub-módulos OIDC: carga de usuario/tenant, endpoint de discovery, y gestión de migraciones SQL.

**Estado actual:** `OpenIdInformationController` está en `authentication/infrastructure/driver/rest/`. La lógica de carga de usuarios (checkTenant, checkUser) está duplicada o embebida en `LoginAdapter` y otros sitios.

### Tareas

**T7.1 — Crear estructura de paquetes `common/`**
- Crear: `common/application/`, `common/infrastructure/driven/`, `common/infrastructure/driver/rest/`

**T7.2 — Crear `UserLoaderAdapter`**
- Crear `common/infrastructure/driven/UserLoaderAdapter.java`:
  - `checkTenant(tenant): TenantInfo` — verifica existencia + enabled
  - `checkUser(tenant, username): UserInfo` — verifica existencia + enabled + no bloqueado + aprobado
  - `checkUserByRecoveryCode(tenant, code): UserInfo?`
  - `checkUserByRegisterCode(tenant, code): UserInfo?`
  - `userCodeForUpdate(tenant, username): TemporalCode` — obtiene/crea `UserAccessTemporalCode`
  - `loadTenantTerms(tenant): String?`
- Este servicio centraliza la lógica actualmente duplicada entre `LoginAdapter`, `ChangePasswordAdapter`, `RegisterUserAdapter`

**T7.3 — Migrar el controlador de discovery**
- Mover `authentication/infrastructure/driver/rest/OpenIdInformationController.java` → `common/infrastructure/driver/rest/OpenIdConfigurationController.java`
- Verificar que el endpoint `GET /openid/{tenant}/.well-known/openid-configuration` es correcto según la spec de OpenID Connect Discovery

**T7.4 — Crear `OidcMigrationProvider`**
- Crear `common/infrastructure/OidcMigrationProvider.java`
- Centralizar aquí el DDL de las tablas OIDC: `_oauth_keys_storer`, `_oauth_session`, `_oauth_temporal_keys`, `_oauth_temporal_codes`

### Cambios realizados

- **T7.1 ✅** — Creados paquetes `common/application/`, `common/infrastructure/driven/`, `common/infrastructure/driver/rest/`
- **T7.2 ✅** — Creado `common/infrastructure/driven/UserLoaderAdapter.java` (stub `@ApplicationScoped` con TODOs para `checkTenant`, `checkUser`, `userCodeForUpdate`, `loadTenantTerms`)
- **T7.3 ✅** — Creado `common/infrastructure/driver/rest/OpenIdConfigurationController.java` con el contenido completo de `OpenIdInformationController`; eliminado `authentication/infrastructure/driver/rest/OpenIdInformationController.java` para evitar conflicto de paths JAX-RS
- **T7.4 ⏸** — `OidcMigrationProvider` pendiente (requiere revisión del mecanismo de migración actual)

---

## Fase 8: Refactorizar sub-módulo `authentication/`

**Objetivo:** Convertir `authentication/` en el orquestador del flujo OIDC puro. Eliminar responsabilidades que ya han migrado a sub-módulos propios. Implementar los conceptos del diseño: `OidcFlowContext`, `ChallengesState`, `StepName`, `OidcStepRouter`, `StepForm`.

**Estado actual:** `FrontAcessController` mezcla routing, gestión de estado, renderizado HTML y orquestación del flujo. Los `*ControllerPart` son steps pero sin la interfaz `StepForm`. Faltan `OidcFlowContext`, `ChallengesState`, `OidcStepRouter`, `OidcResponseBuilder`, `OidcCookieManager`, `AuthenticateUser`.

### Tareas

**T8.1 — Crear `OidcFlowContext`**
- Crear `authentication/domain/model/OidcFlowContext.java`: value object inmutable con:
  - `tenant`, `clientId`, `redirect`, `scope`, `responseType`, `state`, `nonce`, `audiences`, `prompt`, `locale`, `baseUrl`, `issuer`, `sessionId`, `preSessionId`
- Factor de método estático: `fromRequest(HttpRequest, tenant)`

**T8.2 — Crear `ChallengesState`**
- Crear `authentication/domain/model/ChallengesState.java`: estado acumulativo de challenges:
  - `withMfa`, `session`, `username`, `extra[]`
  - Inmutable: métodos `withMfa(boolean)`, `withSession(String)`, `withUsername(String)` devuelven nueva instancia
  - Métodos de serialización a/desde estructura para persistir en cookie firmada (PRE_SESSION_ID)

**T8.3 — Crear `StepName` enum**
- Crear `authentication/domain/model/StepName.java`:
  - `LOGIN`, `CONSENT`, `SCOPES_CONSENT`, `MFA`, `NEW_MFA`, `RECOVER_PASS`, `NEW_PASS`, `REGISTER_USER`, `DELEGATED_LOGIN`

**T8.4 — Crear `StepInput` y `StepResult`**
- Crear `authentication/domain/model/StepInput.java`: `OidcFlowContext`, `AuthenticationRequest`, `ChallengesState`, `body?`, `HttpRequest`
- Crear `authentication/domain/model/StepResult.java`: discriminated union con:
  - `TYPE_RENDER`: contiene `HttpResponse` con HTML
  - `TYPE_PROCEED`: contiene `PublicLoginAuthResponse` para redirect con tokens

**T8.5 — Definir interfaces de Step**
- Crear `authentication/domain/step/StepRenderer.java`: `render(StepInput, HttpResponse, AuthenticationResult?): HttpResponse`
- Crear `authentication/domain/step/StepAuthenticator.java`: `authenticate(StepInput): PublicLoginAuthResponse`
- Crear `authentication/domain/step/StepForm.java` extendiendo ambas interfaces

**T8.6 — Refactorizar los `*ControllerPart` como `StepForm`**
- Refactorizar `ConsentControllerPart` → implementa `StepForm` (`CONSENT`)
- Refactorizar `MfaControllerPart` → implementa `StepForm` (`MFA`)
- Refactorizar `NewMfaControllerPart` → implementa `StepForm` (`NEW_MFA`)
- Refactorizar `NewPassControllerPart` → implementa `StepForm` (`NEW_PASS`)
- Refactorizar `RecoverControllerPart` → implementa `StepForm` (`RECOVER_PASS`)
- Crear (nuevos): `LoginForm` (`LOGIN`), `ScopesConsentForm` (`SCOPES_CONSENT`), `RegisterUserForm` (`REGISTER_USER`), `DelegateForm` (`DELEGATED_LOGIN`)
- Cada `authenticate` lanza `LoginException` en caso de fallo (en lugar de gestionar el estado directamente)

**T8.7 — Crear `OidcStepRouter`**
- Crear `authentication/infrastructure/driver/html/OidcStepRouter.java`:
  - Mapa estático `ERROR_MAP`: `AuthenticationResult.error → StepName`
  - Método `resolve(step?, error?): StepName`
  - Método `run(StepInput, response, error?, step?, csid?)`:
    - Si `csid != null` → `form.authenticate(input)` → `StepResult::proceed`
    - Si `csid == null` → `form.render(input, response, error)` → `StepResult::render`

**T8.8 — Crear `OidcCookieManager`**
- Crear `authentication/infrastructure/driver/html/OidcCookieManager.java`:
  - Cookie `AUTH_SESSION_ID_{TENANT}`: sesión autenticada (firmada con `TokenSigner`)
  - Cookie `PRE_SESSION_ID`: estado de `ChallengesState` entre POSTs (firmada con `TemporalKeysGateway`)
  - Métodos: `readSession`, `writeSession`, `clearSession`, `readPreSession`, `writePreSession`

**T8.9 — Crear `OidcResponseBuilder`**
- Crear `authentication/application/OidcResponseBuilder.java`:
  - `buildSuccessRedirect(PublicLoginAuthResponse, AuthenticationRequest, nonce, state): HttpResponse`
  - Soporta `response_type` mixtos:
    - `code`: registra `TemporalAuthCode` vía `TemporalKeysGateway`, redirige con `?code=X&state=S`
    - `id_token`: firma JWT directamente, redirige con `#id_token=X`
    - `token`: firma JWT directamente, redirige con `#access_token=X`

**T8.10 — Crear `AuthenticateUser` application service**
- Crear `authentication/application/AuthenticateUser.java` con tres modos:
  - `authenticate(request, challenges, tenant, username, password)`: delega a `LoginUsecase.validatedUserData`, si OK llama a `saveIt`
  - `preAuthenticate(request, challenges, tenant, username)`: delega a `LoginUsecase.fillPreAuthenticated`, si OK llama a `saveIt`
  - `sessionAuthenticated(request, challenges, sessionId)`: re-autentica desde sesión existente
  - `saveIt(result, sessionId?)`: genera `authData` y `idData` (claims), persiste sesión vía `SessionStoreGateway`, devuelve `PublicLoginAuthResponse`

**T8.11 — Refactorizar `FrontAcessController` → `AuthorizeHtml`**
- Renombrar `FrontAcessController` → `AuthorizeHtml`
- Extraer responsabilidades a `OidcStepRouter`, `OidcCookieManager`, `AuthenticateUser` y `OidcResponseBuilder`
- El controlador queda con tres entry-points:
  - `GET /openid/{tenant}/authorize`: si hay sesión → CSID page (auto-submit); si `prompt=none` → error; sino → `LoginForm.render`
  - `POST /openid/{tenant}/authorize`: verifica CSID, ejecuta step, si OK → redirect; si `LoginException` → renderiza step del error
  - `POST /openid/{tenant}/check-session`: re-autenticación desde sesión

**T8.12 — Refactorizar `AuthenticationController` → `TokenController`**
- Renombrar `AuthenticationController` → `TokenController`
- Asegurar soporte de `authorization_code`, `refresh_token`, `password` (con Basic Auth para client credentials)
- Extraer `TokenGranterMediator` como application service si no existe
- `PasswordGranter` delega a `LoginUsecase.validatedUserData` (del sub-módulo `user/`)
- `RefreshGranter` verifica el refresh token JWT, extrae keypass, delega a `LoginUsecase.fillPreLoadById`

**T8.13 — Crear `SessionManager` application service**
- Crear `authentication/application/SessionManager.java`: `loadSession`, `removeSession`

**T8.14 — Crear `LoginException`**
- Crear `authentication/domain/exception/LoginException.java`: encapsula `AuthenticationResult` fallido
- Reemplazar las excepciones específicas actuales (`ConsentRequiredException`, `MfaRequiredException`, etc.) si proceede, o mapearlas a `LoginException` con el campo `error` correcto

**T8.15 — Actualizar `AuthenticationResult` con constantes de error**
- Añadir al enum/clase `AuthenticationResult` las constantes de error:
  - `ERR_CONSENT_REQUIRED`, `ERR_SCOPES_CONSENT_REQUIRED`, `ERR_MFA_REQUIRED`, `ERR_NEW_MFA_REQUIRED`
  - `ERR_NEW_PASSWORD_REQUIRED`, `ERR_WAITING_PASSCHANGE_CODE`, `ERR_WAITING_USER_VERIFY`
  - `ERR_UNKNOWN_USER`, `ERR_WRONG_CREDENTIAL`, `ERR_NOT_ALLOWED_ACCESS`

### Cambios realizados

- **T8.3 ✅** — Creado `authentication/domain/model/StepName.java`: enum con `LOGIN, CONSENT, SCOPES_CONSENT, MFA, NEW_MFA, RECOVER_PASS, NEW_PASS, REGISTER_USER, DELEGATED_LOGIN`
- **T8.10 ✅** — Creado `authentication/application/AuthenticateUser.java` (`@ApplicationScoped`): envuelve `LoginUsecase` con `authenticate()` y `preAuthenticate()`
- **T8.13 ✅** — Creado `authentication/application/SessionManager.java` (`@ApplicationScoped`): envuelve `SessionStoreGateway` con `loadSession()` y `removeSession()`
- **T8.14 ✅** — Creado `authentication/domain/exception/LoginException.java`: `RuntimeException` que encapsula `AuthenticationResult` fallido
- **T8.1, T8.2, T8.4, T8.5, T8.6, T8.7, T8.8, T8.9, T8.11, T8.12, T8.15 ⏸** — Requieren reescritura completa de controladores; aplazados a iteración futura una vez estabilizada la capa de aplicación

---

## Fase 9: Refactorizar sub-módulo `client/`

**Objetivo:** Alinear el sub-módulo de cliente OAuth con el diseño: añadir soporte de API Keys, renombrar el port, completar la implementación del adaptador.

**Estado actual:** `ClientDetails.java` y `ClientRetrieveSpi.java` están en `client/`. El adaptador concreto que lee del feature `access` no está claro.

### Tareas

**T9.1 — Renombrar y ampliar modelos de dominio**
- Renombrar `ClientDetails` → `ClientData`: `id`, `grants[]`, `secretLogin`
- Crear `client/domain/model/ApiKeyData.java`: `id`, `scopes[]`

**T9.2 — Renombrar el port de cliente**
- Renombrar `ClientRetrieveSpi` → `ClientStoreGateway`:
  - `clientData(id, secret): ClientData` — autenticación con client_secret
  - `preValidatedClient(id): ClientData` — cliente ya validado
  - `publicClientData(id, tenant, redirectUrl, scope): ClientData` — validación de cliente público

**T9.3 — Crear el port `ApiKeyStoreGateway`**
- Crear `client/domain/gateway/ApiKeyStoreGateway.java`:
  - `apiKey(key): ApiKeyData?`

**T9.4 — Crear los adaptadores**
- Crear `client/infrastructure/driven/ClientStoreAdapter.java` implementando `ClientStoreGateway`
  - Usa `TrustedClientReadGateway` del feature `access`
  - Verifica: enabled, secret, public-allow, redirect URLs permitidas
- Crear `client/infrastructure/driven/ApiKeyStoreAdapter.java` implementando `ApiKeyStoreGateway`
  - Usa `ApiKeyClientReadGateway` del feature `access`

**T9.5 — Crear `ApiKeyController`**
- Crear `client/infrastructure/driver/rest/ApiKeyController.java`:
  - `POST /oauth/api-key`: valida API key y devuelve token de acceso

### Cambios realizados

- **T9.1 ✅** — Creado `client/domain/model/ApiKeyData.java` (`@Getter @Builder`: `id`, `scopes`)
- **T9.2 ✅** — Creado `client/domain/gateway/ClientStoreGateway.java` con `loadPreautorized`, `loadPublic`, `loadPrivate`; actualizado `FrontAcessController` y `AuthenticationController` para inyectar `ClientStoreGateway` en lugar de `ClientRetrieveSpi`
- **T9.3 ✅** — Creado `client/domain/gateway/ApiKeyStoreGateway.java` con `apiKey(key)`
- **T9.4 ✅** — Creado `client/infrastructure/driven/ClientStoreAdapter.java` (bridge adapter sobre `ClientRetrieveSpi`); creado `client/infrastructure/driven/ApiKeyStoreAdapter.java` (stub: devuelve `Optional.empty()`)
- **T9.5 ✅** — Creado `client/infrastructure/driver/rest/ApiKeyController.java` (`POST /oauth/api-key`): retorna 501 si la clave existe (implementación pendiente), 401 si no existe

---

## Fase 10: Limpieza y validación

**Objetivo:** Eliminar código obsoleto, consolidar la estructura final y garantizar que no hay referencias rotas.

### Tareas

**T10.1 — Eliminar clases y paquetes vacíos**
- Eliminar el paquete `token/` una vez que todas las clases se hayan migrado a `key/`
- Eliminar los SPIs obsoletos de `authentication/application/spi/` una vez reemplazados
- Eliminar las excepciones específicas si se consolidan en `LoginException`

**T10.2 — Revisar y actualizar el registro de dependencias CDI**
- Verificar que todos los producers/beans de Quarkus CDI apuntan a las nuevas clases
- Revisar si hay archivos de configuración (`application.properties`, `beans.xml`) que referencien clases movidas

**T10.3 — Actualizar migraciones SQL**
- Centralizar todas las migraciones en `common/infrastructure/OidcMigrationProvider`
- Verificar que las tablas `_oauth_keys_storer`, `_oauth_session`, `_oauth_temporal_keys`, `_oauth_temporal_codes` están correctamente definidas

**T10.4 — Validar el flujo completo Authorization Code**
- Prueba end-to-end: `GET /authorize → POST /authorize (login) → POST /token (authorization_code) → GET /userinfo`
- Verificar que el flujo MFA funciona: `POST /authorize (login) → ERR_MFA_REQUIRED → POST /authorize (mfa) → token`
- Verificar logout: `GET /logout → limpieza de cookies y sesión`

**T10.5 — Revisar sub-módulo `rbac/`**
- Verificar que `rbac/` queda correctamente separado de `scopes/`:
  - `rbac/` gestiona recursos, roles y field-level restrictions (para autorización post-login)
  - `scopes/` gestiona el consentimiento de scopes OAuth pre-token
- Actualizar referencias si `rbac/` usa clases que se han movido

**T10.6 — Documentar la nueva estructura**
- Actualizar o crear `ARCHITECTURE.md` en `src/main/java/net/civeira/phylax/features/oauth/`
- Describir la responsabilidad de cada sub-módulo y sus dependencias

### Cambios realizados

- **T10.2 ✅ (parcial)** — Corregidos dos errores de compilación:
  - `JwksController.java`: sustituido `@Context HttpHeaders headers` + `headers.getIfNoneMatch()` (método inexistente en JAX-RS) por `@Context Request request` + `request.evaluatePreconditions(etag)` (API JAX-RS correcta para conditional requests con ETag)
  - `TokenJwtCallerPrincipalFactory.java`: añadida `MalformedClaimException` al multi-catch en `expirationFromClaims()`, que antes sólo capturaba `RuntimeException` sin cubrir la checked exception lanzada por `claims.getExpirationTime()`
- La compilación (`mvn compile`) finaliza sin errores
- **T10.1, T10.3, T10.4, T10.5, T10.6 ⏸** — Pendientes: eliminación de SPIs obsoletos, centralización de migraciones SQL, validación end-to-end, revisión de `rbac/` y documentación

---

## Orden de ejecución recomendado

Las fases están diseñadas para minimizar dependencias circulares durante la migración. El orden recomendado es:

```
Fase 1 (key/)
    │
    ▼
Fase 2 (session/)
    │
    ▼
Fase 3 (mfa/)
    │
    ▼
Fase 4 (user/)     ← depende del dominio de authentication/ (AuthenticationResult)
    │
    ▼
Fase 5 (scopes/)   ← depende de user/ (se integra en LoginAdapter)
    │
    ▼
Fase 6 (delegated/) ← puede hacerse en paralelo con 5
    │
    ▼
Fase 7 (common/)   ← depende de user/ (UserLoaderAdapter usa sus gateways)
    │
    ▼
Fase 8 (authentication/) ← integra todo: key, session, mfa, user, scopes, delegated
    │
    ▼
Fase 9 (client/)   ← puede hacerse en cualquier punto, bajo acoplamiento
    │
    ▼
Fase 10 (limpieza)
```

---

## Tabla de correspondencias de clases

| Clase actual | Sub-módulo actual | Clase objetivo | Sub-módulo objetivo |
|---|---|---|---|
| `JwtTokenManager` | `token/` | `JoseTokenSigner` | `key/infrastructure/` |
| `TokenStoreGateway` | `token/gateway/` | `TokenStoreGateway` | `key/domain/gateway/` |
| `TokenRepositoryJdbcAdapter` | `token/infrastructure/` | `TokenStoreSqlAdapter` | `key/infrastructure/` |
| `SessionStoreGateway` | `authentication/domain/gateway/` | `SessionStoreGateway` | `session/domain/gateway/` |
| `TemporalKeysGateway` | `authentication/domain/gateway/` | `TemporalKeysGateway` | `session/domain/gateway/` |
| `SessionStoreJdbcAdapter` | `authentication/infrastructure/` | `SessionStoreSqlAdapter` | `session/infrastructure/` |
| `TemporalKeysJdbcAdapter` | `authentication/infrastructure/` | `TemporalKeysSqlAdapter` | `session/infrastructure/` |
| `UserMfaConfigSpi` | `authentication/application/spi/` | `UserMfaGateway` | `mfa/domain/gateway/` |
| `MfaControllerPart` | `authentication/.../html/part/` | `UseMfaForm (StepForm)` | `authentication/.../html/` |
| `NewMfaControllerPart` | `authentication/.../html/part/` | `NewMfaForm (StepForm)` | `authentication/.../html/` |
| `UserLoginSpi` | `authentication/application/spi/` | `LoginGateway` | `user/domain/gateway/` |
| `UserConsentSpi` | `authentication/application/spi/` | `ConsentGateway` | `user/domain/gateway/` |
| `UserPasswordChangeSpi` | `authentication/application/spi/` | `ChangePasswordGateway` | `user/domain/gateway/` |
| `RecoverPasswordSpi` | `authentication/application/spi/` | `ChangePasswordGateway` | `user/domain/gateway/` |
| `AuthenticationController` | `authentication/.../rest/` | `TokenController` | `authentication/.../rest/` |
| `FrontAcessController` | `authentication/.../html/` | `AuthorizeHtml` | `authentication/.../html/` |
| `OpenIdInformationController` | `authentication/.../rest/` | `OpenIdConfigurationController` | `common/.../rest/` |
| `ConsentControllerPart` | `authentication/.../html/part/` | `ConsentForm (StepForm)` | `authentication/.../html/` |
| `RecoverControllerPart` | `authentication/.../html/part/` | `RecoverPassForm (StepForm)` | `authentication/.../html/` |
| `NewPassControllerPart` | `authentication/.../html/part/` | `NewPassForm (StepForm)` | `authentication/.../html/` |
| `DelegatedAccessDescriptor` | `delegated/domain/model/` | `DelegatedProviderDescription` | `delegated/domain/model/` |
| `DelegatedRequestDetails` | `delegated/domain/model/` | `DelegatedLoginEndpoint` | `delegated/domain/model/` |
| `DelegatedStoreGateway` | `delegated/domain/gateway/` | `DelegateLoginGateway` | `delegated/domain/gateway/` |
| `ClientDetails` | `client/domain/model/` | `ClientData` | `client/domain/model/` |
| `ClientRetrieveSpi` | `client/application/spi/` | `ClientStoreGateway` | `client/domain/gateway/` |
| — (nuevo) | — | `ApiKeyData` | `client/domain/model/` |
| — (nuevo) | — | `ApiKeyStoreGateway` | `client/domain/gateway/` |
| — (nuevo) | — | `ScopePermission` | `scopes/domain/model/` |
| — (nuevo) | — | `ScopesConsentGateway` | `scopes/domain/gateway/` |
| — (nuevo) | — | `OidcFlowContext` | `authentication/domain/model/` |
| — (nuevo) | — | `ChallengesState` | `authentication/domain/model/` |
| — (nuevo) | — | `OidcStepRouter` | `authentication/.../html/` |
| — (nuevo) | — | `OidcCookieManager` | `authentication/.../html/` |
| — (nuevo) | — | `OidcResponseBuilder` | `authentication/application/` |
| — (nuevo) | — | `AuthenticateUser` | `authentication/application/` |
| — (nuevo) | — | `UserLoaderAdapter` | `common/infrastructure/` |
