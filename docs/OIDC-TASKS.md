# Phylax — OIDC Tasks

> Tareas de desarrollo derivadas del análisis de [OIDC.md](./OIDC.md) contra el código fuente actual.
> Versión: 2026-03-17

---

## Estado global

| Feature | % | Prioridad |
|---------|---|-----------|
| PKCE (RFC 7636) | 50% | Alta |
| Token Revocation (RFC 7009) | 0% | Alta |
| Token Introspection (RFC 7762) | 0% | Alta |
| Refresh Token Rotation | 0% | Alta |
| Userinfo endpoint | 25% | Alta |
| Discovery completeness | 85% | Alta |
| Logout / End Session | 75% | Media |
| prompt / max_age / login_hint | 25% | Media |
| Audit Logging | 25% | Media |
| Step-up Authentication (ACR) | 50% | Media |
| Session SSO | 40% | Media |
| Client Credentials grant | 0% | Media |
| Consent versioning | 0% | Media |
| `at_hash` / `c_hash` en id_token | 75% | Baja |
| Back-channel Logout | 0% | Baja |
| Device Authorization Grant | 0% | Baja |
| Email verification | 10% | Baja |
| Client Registration dinámica | 0% | Baja |

---

## Prioridad Alta

---

### TASK-01 — PKCE: completar almacenamiento y enforcement

**Estado:** 50% — La validación existe en `AuthenticationController` pero `TemporalAuthCode` no almacena los campos PKCE.

**Problema concreto:**
- `TemporalAuthCode.java` carece de campos `codeChallenge` y `codeChallengeMethod`
- Por tanto el `AuthenticationController` no puede comparar nada en el token exchange
- La discovery no anuncia `code_challenge_methods_supported`
- No hay enforcement por cliente (clientes públicos deberían requerir PKCE obligatoriamente)

**Archivos afectados:**
- `src/main/java/.../oauth/session/domain/TemporalAuthCode.java` — añadir campos
- `src/main/java/.../oauth/authentication/infrastructure/driver/html/FrontAcessController.java` — capturar y propagar `code_challenge` / `code_challenge_method` al crear el `TemporalAuthCode`
- `src/main/java/.../oauth/authentication/infrastructure/driver/rest/AuthenticationController.java` — leer `code_challenge` del `TemporalAuthCode` recuperado y ejecutar la verificación SHA-256
- `src/main/java/.../oauth/client/domain/ClientDetails.java` — añadir flag `requirePkce` (o derivarlo de `protectedWithSecret=false`)
- `src/main/java/.../oauth/oidc/infrastructure/driver/rest/OpenIdConfigurationController.java` — añadir `.codeChallengMethodsSupported(List.of("S256"))`

**Tareas concretas:**
1. Añadir `@Builder.Default Optional<String> codeChallenge` y `codeChallengeMethod` a `TemporalAuthCode`
2. En `FrontAcessController` (parte de gestión del auth request), extraer `code_challenge` de `AuthRequest` y pasarlo al `TemporalAuthCode`
3. En `AuthenticationController.tokenExchange()`:
   - Recuperar `TemporalAuthCode` (ya existente)
   - Si `codeChallenge` presente: exigir `code_verifier` en el body
   - Computar `BASE64URL(SHA256(code_verifier))` y comparar
   - Rechazar con `invalid_grant` si no coincide o si `code_verifier` ausente cuando era requerido
4. Si `client.protectedWithSecret == false` y no viene `code_challenge` → rechazar en `/authorize` con `invalid_request`
5. Añadir `"S256"` a `codeChallengMethodsSupported` en discovery
6. Rechazar `code_challenge_method=plain`

---

### TASK-02 — Token Revocation (RFC 7009)

**Estado:** 0% — El endpoint `/revocation` actual solo borra cookies de pre-sesión, no tokens JWT.

**Problema concreto:**
- No existe blacklist de tokens
- `TokenStoreGateway` no tiene métodos de revocación
- No se verifica revocación al validar tokens
- Al hacer logout, los access/refresh tokens quedan activos

**Archivos afectados:**
- `src/main/java/.../oauth/token/domain/gateway/TokenStoreGateway.java` — añadir métodos
- `src/main/java/.../oauth/token/infrastructure/driven/TokenStoreSqlAdapter.java` — implementar
- `src/main/java/.../oauth/authentication/infrastructure/driver/html/FrontAcessController.java` — separar del endpoint `/revocation` de cookies o moverlo
- Nuevo: `src/main/java/.../oauth/token/infrastructure/driver/rest/TokenRevocationController.java`
- `src/main/java/.../oauth/token/application/JwtTokenBuilder.java` — añadir verificación de revocación

**Tareas concretas:**
1. Añadir a `TokenStoreGateway`:
   ```java
   void revokeToken(String jti, Instant expiresAt);
   boolean isRevoked(String jti);
   void revokeAllForUser(String userId, String clientId); // logout
   ```
2. Implementar en `TokenStoreSqlAdapter` con tabla `revoked_token (jti, revoked_at, expires_at)`
3. Crear `POST /oauth/openid/{tenant}/token/revoke` (RFC 7009):
   - Autenticar cliente (client_secret_basic o client_secret_post)
   - Aceptar `token` + `token_type_hint` (access_token | refresh_token)
   - Parsear JWT, extraer `jti` y `exp`
   - Llamar `revokeToken(jti, exp)`
   - Siempre responder `200 OK` (incluso si token no existe — RFC mandates)
4. En `JwtTokenBuilder.verify()` (o en el filter de seguridad): comprobar `isRevoked(jti)` tras verificar firma
5. Actualizar discovery: añadir `revocationEndpoint` y `revocationEndpointAuthMethodsSupported`
6. Al hacer logout (`/logout`): llamar `revokeAllForUser(userId, clientId)`

---

### TASK-03 — Token Introspection (RFC 7762)

**Estado:** 0% — El endpoint existe pero devuelve 403. Solo accesible para resource servers autorizados.

**Problema concreto:**
- El endpoint `POST /introspect` devuelve `403 Forbidden`
- No hay mecanismo de registro de resource servers autorizados
- No se verifica revocación (depende de TASK-02)

**Archivos afectados:**
- `src/main/java/.../oauth/authentication/infrastructure/driver/rest/AuthenticationController.java` — implementar o extraer a controlador propio
- Dependencia: TASK-02 (revocación)

**Tareas concretas:**
1. Definir cómo se autentican los resource servers: opción simple → `client_secret_basic` con clientes que tengan el scope `introspect` o un flag `isResourceServer=true` en `ClientDetails`
2. Implementar `POST /oauth/openid/{tenant}/introspect`:
   - Autenticar resource server
   - Parsear el token enviado (sin lanzar excepción si inválido)
   - Si válido, no expirado y no revocado → responder con claims activos
   - En cualquier otro caso → `{"active": false}`
3. Respuesta activa mínima: `active, sub, client_id, scope, exp, iat, iss, jti, token_type`
4. Cabeceras: `Content-Type: application/json`, `Cache-Control: no-store`

---

### TASK-04 — Refresh Token Rotation

**Estado:** 0% — `RefreshGranter` verifica el token pero no rota ni detecta reutilización.

**Problema concreto:**
- El refresh token puede usarse indefinidamente mientras no expire
- No hay detección de token theft por double-use
- Dependencia parcial con TASK-02 (revocación)

**Archivos afectados:**
- `src/main/java/.../oauth/authentication/application/granter/RefreshGranter.java` — añadir rotación
- `src/main/java/.../oauth/token/application/JwtTokenBuilder.java` — emitir nuevo refresh token
- Dependencia: TASK-02 para revocar el refresh token usado

**Tareas concretas:**
1. Tras verificar y usar un refresh token en `RefreshGranter`:
   - Revocar el `jti` del refresh token usado (`TokenStoreGateway.revokeToken()`)
   - Emitir nuevo refresh token con nuevo `jti` (family tracking opcional)
2. Si se detecta un refresh token ya revocado siendo reutilizado:
   - Log de auditoría: posible token theft
   - Revocar **todos** los tokens del usuario/cliente (sesión comprometida)
   - Responder `invalid_grant`
3. Añadir claim `rfti` (refresh token family id) al refresh token para rastrear la cadena

---

### TASK-05 — Userinfo endpoint completo

**Estado:** 25% — Devuelve solo `sub`, `name`, `issuer`. No filtra por scopes ni devuelve claims estándar.

**Problema concreto:**
- `InformationController` no verifica los scopes del access token
- No devuelve `email`, `email_verified`, `phone`, `address`, `preferred_username`, `picture`
- No hay mapeo entre scopes y claims

**Archivos afectados:**
- `src/main/java/.../oauth/authentication/infrastructure/driver/rest/InformationController.java`
- `src/main/java/.../oauth/user/domain/gateway/LoginGateway.java` — puede necesitar método `getUserClaims()`
- `src/main/java/.../oauth/authentication/domain/AuthenticationData.java` — verificar qué claims se transportan

**Tareas concretas:**
1. Verificar access token entrante: firma, expiración, revocación (TASK-02)
2. Extraer `sub`, `scope`, `client_id` del access token
3. Cargar datos del usuario según scopes presentes:
   - `openid` → `sub` (obligatorio siempre)
   - `profile` → `name`, `given_name`, `family_name`, `preferred_username`, `picture`, `updated_at`
   - `email` → `email`, `email_verified`
   - `phone` → `phone_number`, `phone_number_verified`
4. El `sub` devuelto DEBE coincidir con el `sub` del access token
5. Content-Type: `application/json` (o `application/jwt` si `userinfo_signed_response_alg` configurado)
6. Errores: `401 WWW-Authenticate: Bearer error="invalid_token"` si token inválido

---

### TASK-06 — Discovery: campos faltantes

**Estado:** 85% — Faltan `code_challenge_methods_supported`, grant types incompletos, `prompt_values_supported`, ACR values incompletos.

**Archivo afectado:**
- `src/main/java/.../oauth/oidc/infrastructure/driver/rest/OpenIdConfigurationController.java`

**Tareas concretas:**
1. Añadir `.codeChallengMethodsSupported(List.of("S256"))` (PKCE)
2. Corregir `grantTypesSupported`: añadir `"authorization_code"`, `"client_credentials"` (cuando implementados)
3. Añadir `promptValuesSupported: ["none", "login", "consent", "select_account"]`
4. Corregir `acrValuesSupported`: añadir `"2"` (MFA) que ya se usa en el código pero no se anuncia
5. Cuando TASK-02 esté listo: añadir `revocationEndpoint` y `revocationEndpointAuthMethodsSupported`
6. Cuando TASK-03 esté listo: confirmar `introspectionEndpoint`
7. Cuando logout esté completo (TASK-08): confirmar `endSessionEndpoint`

---

## Prioridad Media

---

### TASK-07 — prompt / max_age / login_hint

**Estado:** 25% — Solo `prompt` parcialmente. `max_age` y `login_hint` no se parsean.

**Archivos afectados:**
- `src/main/java/.../oauth/authentication/domain/AuthRequest.java` — añadir campos
- `src/main/java/.../oauth/authentication/infrastructure/driver/html/FrontAcessController.java` — leer y actuar

**Tareas concretas:**

**`max_age`:**
1. Añadir `Optional<Long> maxAge` a `AuthRequest`
2. En `FrontAcessController`, al cargar sesión SSO existente: si `auth_time + max_age < now()` → invalidar sesión SSO y forzar re-login
3. Incluir `auth_time` en `SessionInfo` para poder comparar

**`login_hint`:**
1. Añadir `Optional<String> loginHint` a `AuthRequest`
2. En el render del formulario de login: pasar `loginHint` al template como valor inicial del campo username
3. Nunca omitir validación por tener `login_hint` (solo pre-rellena UI)

**`prompt`:**
1. `prompt=login` → ignorar sesión SSO existente, forzar re-login (borrar cookie de sesión al inicio del flujo)
2. `prompt=consent` → forzar pantalla de consent aunque ya esté aceptado
3. `prompt=select_account` → si hay sesiones múltiples, mostrar selector (o forzar login si no implementado)
4. `prompt=none` → verificar sesión SSO: si válida emitir código directamente; si no → redirect con `login_required` o `interaction_required`; NUNCA renderizar UI

---

### TASK-08 — Logout / End Session completo

**Estado:** 75% — La sesión del servidor se borra pero los tokens siguen activos y no hay notificación a RPs.

**Archivos afectados:**
- `src/main/java/.../oauth/authentication/infrastructure/driver/html/FrontAcessController.java` — mejorar endpoint `/logout`
- Dependencia: TASK-02 (para revocar tokens en logout)

**Tareas concretas:**
1. Validar `id_token_hint` si se proporciona (verificar firma, extraer `sub` y `sid`)
2. Validar `post_logout_redirect_uri` contra lista blanca del cliente (si no, ignorar y mostrar página genérica)
3. Al hacer logout: llamar `TokenStoreGateway.revokeAllForUser(userId, clientId)` (TASK-02)
4. Borrar `SessionInfo` de BD (`sessionStore.deleteSession(cookie)` ya existe)
5. Borrar cookie `AUTH_SESSION_ID`
6. Si `post_logout_redirect_uri` válida: redirect con `state` si fue enviado
7. Si no `post_logout_redirect_uri`: mostrar página de confirmación de logout
8. Completar `check_session_iframe` (actualmente devuelve `<h1>Check</h1>`)
9. `backchannel_logout` — ver TASK-12

---

### TASK-09 — Audit Logging

**Estado:** 25% — Solo eventos de login/fallo via CDI events.

**Archivos afectados:**
- `src/main/java/.../oauth/authentication/infrastructure/driven/EventNotifierAppEventAdapter.java` — extender
- Posiblemente nueva tabla de auditoría o sink de eventos

**Tareas concretas:**
1. Definir interfaz `AuditGateway` con métodos para cada evento auditado
2. Implementar eventos faltantes:
   - `loginSuccess(userId, clientId, ip, acr, sessionId)`
   - `loginFailed(username, ip, reason)` — sin revelar si usuario existe
   - `accountLocked(userId, ip, until)`
   - `mfaVerified(userId, clientId)`
   - `mfaFailed(userId, ip)`
   - `tokenIssued(userId, clientId, grantType, scopes, jti)`
   - `tokenRevoked(jti, revokedBy)`
   - `passwordChanged(userId, reason)` — forced/user/recovery
   - `consentAccepted(userId, clientId, scopes, version)`
   - `logoutPerformed(userId, sessionId)`
   - `delegatedLogin(userId, provider, externalId)`
3. Persistencia: tabla `audit_log` o evento en bus (Kafka/CDI)

---

### TASK-10 — Step-up Authentication (ACR enforcement)

**Estado:** 50% — Los ACR values están definidos y se incluyen en tokens, pero no se verifica ni se fuerza step-up.

**Archivos afectados:**
- `src/main/java/.../oauth/authentication/infrastructure/driver/html/FrontAcessController.java`
- `src/main/java/.../oauth/session/domain/SessionInfo.java` — asegurar que `acr` está en sesión

**Tareas concretas:**
1. Parsear `acr_values` en `AuthRequest` (valor space-separated, ordenado por preferencia del cliente)
2. Al cargar sesión SSO existente: comparar `session.acr` con el `acr_values` solicitado mínimo
3. Si `session.acr < required_acr`: iniciar step-up
   - Si required ACR=1 (password) pero sesión es ACR=0 (solo cookie): forzar login
   - Si required ACR=2 (MFA) pero sesión es ACR=1 (password): pedir OTP sin reiniciar sesión
4. Guardar `acr_values` requerido en sesión durante el step-up para continuar el flujo al completar
5. Anunciar `"2"` en `acrValuesSupported` del discovery (TASK-06)

---

### TASK-11 — Session SSO

**Estado:** 40% — Se verifica sesión existente pero no hay SSO completo cross-client.

**Archivos afectados:**
- `src/main/java/.../oauth/session/domain/SessionInfo.java`
- `src/main/java/.../oauth/authentication/infrastructure/driver/html/FrontAcessController.java`

**Tareas concretas:**
1. `SessionInfo` debe almacenar `auth_time` (timestamp de autenticación real, no de creación de sesión)
2. La cookie SSO debe ser independiente del flujo de autorización actual (hoy parece estar ligada a un `state` específico)
3. Al recibir un nuevo `/authorize`: buscar sesión SSO del usuario (no solo del flujo actual)
4. Si sesión SSO válida (y `prompt` lo permite, y `max_age` no ha expirado): saltar login y continuar a consent/scopes
5. TTL configurable (sliding window vs absolute)
6. Soporte para múltiples clientes en la misma sesión SSO (para back-channel logout posterior)

---

### TASK-12 — Client Credentials Grant

**Estado:** 0% — No existe `ClientCredentialsGranter`.

**Archivos afectados:**
- Nueva clase: `src/main/java/.../oauth/authentication/application/granter/ClientCredentialsGranter.java`
- `src/main/java/.../oauth/token/application/JwtTokenBuilder.java` — emitir token sin `sub` de usuario o con `sub=client_id`

**Tareas concretas:**
1. Crear `ClientCredentialsGranter` que implemente `TokenGranter`:
   - `canHandle("client_credentials")`
   - Verificar que el cliente es confidencial
   - Verificar que el cliente tiene `client_credentials` en `allowedGrants`
   - Validar scopes solicitados contra `allowedScopes`
   - `sub = client_id`
2. Emitir solo `access_token` (sin `id_token`, sin `refresh_token`)
3. Añadir `"client_credentials"` a `grantTypesSupported` en discovery
4. En `AuthenticationController`: registrar el nuevo granter

---

### TASK-13 — Consent versioning

**Estado:** 0% — `PendingConsent` y `ConsentGateway` no tienen versión.

**Archivos afectados:**
- `src/main/java/.../oauth/user/domain/PendingConsent.java`
- `src/main/java/.../oauth/user/domain/gateway/ConsentGateway.java`
- `src/main/java/.../oauth/user/infrastructure/driven/` — adaptador de BD

**Tareas concretas:**
1. Añadir `version` (String o semver) a `PendingConsent`
2. La tabla de T&C de la BD debe tener campo `version` y texto por versión
3. `ConsentGateway.getPendingConsent()`: devolver consent pendiente si la versión registrada del usuario difiere de la versión actual
4. `ConsentGateway.storeAcceptedConsent()`: almacenar también la `version` aceptada, timestamp e IP
5. No re-solicitar si la versión no ha cambiado desde la última aceptación

---

## Prioridad Baja

---

### TASK-14 — `c_hash` en id_token (Hybrid Flow)

**Estado:** 75% — `at_hash` y `s_hash` implementados. Falta `c_hash`.

**Archivos afectados:**
- `src/main/java/.../oauth/token/application/JwtTokenBuilder.java`

**Tareas concretas:**
1. En `buildIdToken()`: si `response_type` contiene `code`, computar `c_hash = LEFT128(BASE64URL(SHA256(code)))`
2. Incluir como claim `c_hash` en el `id_token`
3. Solo relevante cuando se implemente Hybrid Flow

---

### TASK-15 — Back-channel Logout (OIDC Back-Channel Logout 1.0)

**Estado:** 0% — Anunciado en discovery pero no implementado.

**Archivos afectados:**
- `src/main/java/.../oauth/client/domain/ClientDetails.java` — añadir `backchannelLogoutUri`
- Nueva infraestructura: servicio de notificación asíncrona
- Dependencia: TASK-08 (logout)

**Tareas concretas:**
1. Añadir `backchannelLogoutUri` y `backchannelLogoutSessionRequired` a `ClientDetails` y al registro de clientes
2. Al hacer logout (TASK-08): obtener todos los clientes con sesión activa para el usuario
3. Para cada cliente con `backchannelLogoutUri`:
   - Construir `logout_token` JWT: `sub`, `sid`, `iss`, `aud`, `iat`, `jti`, `events: {"http://schemas.openid.net/event/backchannel-logout": {}}`
   - HTTP POST asíncrono a `backchannelLogoutUri` con `application/x-www-form-urlencoded`, campo `logout_token`
   - No bloquear el logout del usuario por fallos en notificaciones

---

### TASK-16 — Device Authorization Grant (RFC 8628)

**Estado:** 0% — `DevicesAccessController` devuelve 403.

**Archivos afectados:**
- `src/main/java/.../oauth/authentication/infrastructure/driver/rest/DevicesAccessController.java`
- `src/main/java/.../oauth/authentication/infrastructure/driver/html/FrontAcessController.java` — UI para `verification_uri`
- Necesita almacén de device codes (puede extender `TemporalKeysGateway`)

**Tareas concretas:**
1. `POST /device_authorization`:
   - Validar cliente
   - Generar `device_code` (UUID) y `user_code` (8 chars mayúsculas, fácil de leer)
   - Almacenar par con TTL (600s), estado=`pending`
   - Responder `{device_code, user_code, verification_uri, expires_in:600, interval:5}`
2. UI en `verification_uri`: formulario para introducir `user_code`, seguido del flujo de login normal
3. Polling en `POST /token` con `grant_type=urn:ietf:params:oauth:grant-type:device_code`:
   - `authorization_pending` → usuario aún no ha completado login
   - `slow_down` → cliente está haciendo polling demasiado rápido
   - `expired_token` → TTL expirado
   - Éxito → emitir tokens
4. Al completar login en UI: marcar device_code como `approved` con `AuthenticationData`

---

### TASK-17 — Email verification en registro

**Estado:** 10% — Infraestructura de registro existe, pero `email_verified` no llega al token.

**Archivos afectados:**
- `src/main/java/.../features/access/user/domain/` — añadir `emailVerified` a `User`
- `src/main/java/.../oauth/token/application/JwtTokenBuilder.java` — incluir claim `email_verified`
- `src/main/java/.../oauth/user/domain/gateway/RegisterUserGateway.java` — verificar workflow

**Tareas concretas:**
1. Añadir `emailVerified: boolean` al agregado `User` (value object `EmailVerifiedVO`)
2. El flujo de registro envía email con código/enlace: marcar `emailVerified=false` hasta verificación
3. `email_verified` claim en `id_token` y en respuesta de userinfo (scope `email`)
4. Política configurable: ¿bloquear login hasta verificar email?

---

### TASK-18 — Client Registration dinámica (RFC 7591)

**Estado:** 0% — `ClientRegisterController` devuelve 403.

**Archivos afectados:**
- `src/main/java/.../oauth/client/infrastructure/driver/rest/ClientRegisterController.java`
- `src/main/java/.../oauth/client/domain/` — ampliar `ClientDetails`
- `src/main/java/.../oauth/client/domain/gateway/ClientStoreGateway.java` — añadir `save()`

**Tareas concretas:**
1. Definir política de registro: abierto (cualquiera) vs. restringido (`initial_access_token`)
2. Validaciones en POST `/register`:
   - `redirect_uris` obligatorio y con HTTPS (excepto localhost)
   - `grant_types` coherente con `token_endpoint_auth_method`
   - Sin wildcards en redirect URIs para clientes confidenciales
3. Respuesta: `client_id`, `client_secret` (si confidencial), `registration_access_token`, `registration_client_uri`
4. Soporte `GET/PUT /register/{client_id}` con `registration_access_token`

---

## Deuda técnica identificada

### DT-01 — Separar endpoint de revocación de cookies

El endpoint `POST /oauth/openid/{tenant}/revocation` en `FrontAcessController` actualmente borra la **cookie de pre-sesión**, no tokens JWT. Cuando se implemente TASK-02, este endpoint debe ser independiente (nuevo controlador REST en `/token/revoke`) o renombrado claramente para evitar confusión.

**Archivo:** `FrontAcessController.java` — método `revocation()` (~línea 556)

---

### DT-02 — Discovery anuncia endpoints no implementados

El discovery (`OpenIdConfigurationController`) anuncia:
- `backchannelAuthenticationEndpoint` → `DevicesAccessController` devuelve 403
- `introspectionEndpoint` → `AuthenticationController` devuelve 403
- `backchannelLogoutSupported(true)` → no implementado

Esto puede romper RPs que intenten usar estas features. Opciones:
- Eliminar de discovery hasta implementar
- Devolver error descriptivo en lugar de 403 genérico

**Archivo:** `OpenIdConfigurationController.java` y `DevicesAccessController.java`

---

### DT-03 — MFA (ACR=2) no anunciado en discovery

`acrValuesSupported` solo declara `["0", "1"]` pero el código usa y emite `"2"` para MFA.

**Archivo:** `OpenIdConfigurationController.java` — corregir en TASK-06.

---

### DT-04 — `allowdedGrant` typo en ClientDetails

`ClientDetails.java` tiene el método `allowdedGrant()` con typo en `d` extra.

**Archivo:** `src/main/java/.../oauth/client/domain/ClientDetails.java`

---

## Orden de implementación sugerido

```
Sprint 1 (base de seguridad):
  TASK-01 (PKCE)
  TASK-02 (Revocation)
  TASK-04 (Refresh rotation)

Sprint 2 (completar endpoints):
  TASK-03 (Introspection)
  TASK-05 (Userinfo)
  TASK-06 (Discovery fixes)

Sprint 3 (flujos avanzados):
  TASK-07 (prompt/max_age/login_hint)
  TASK-08 (Logout completo)
  TASK-10 (Step-up ACR)
  TASK-11 (SSO)

Sprint 4 (grants adicionales y auditoría):
  TASK-09 (Audit log)
  TASK-12 (Client Credentials)
  TASK-13 (Consent versioning)

Sprint 5 (features avanzadas):
  TASK-14 (c_hash)
  TASK-15 (Back-channel logout)
  TASK-16 (Device flow)
  TASK-17 (Email verified)
  TASK-18 (Dynamic registration)

En paralelo (deuda técnica):
  DT-01, DT-02, DT-03, DT-04
```
