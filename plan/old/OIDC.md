# Phylax — Modelo de Autenticación OIDC/OAuth2

> Documento de referencia para el desarrollo y madurez del servidor de identidad.
> Versión: 2026-03-17

---

## Tabla de contenidos

1. [Objetivos del servidor de identidad](#1-objetivos)
2. [Flujos OAuth 2.0 / OIDC implementados o planificados](#2-flujos-oauth20--oidc)
3. [Autenticación HTML (authorization endpoint)](#3-autenticación-html)
   - 3.1 Login con contraseña
   - 3.2 MFA (TOTP)
   - 3.3 Forzar cambio de contraseña
   - 3.4 Recuperación de contraseña
   - 3.5 Registro de usuario
   - 3.6 Aceptación de condiciones (consent)
   - 3.7 Aceptación de scopes (scope consent)
   - 3.8 Login delegado (OAuth social / SAML)
4. [Token endpoint y grant types](#4-token-endpoint-y-grant-types)
5. [Endpoints estándar OIDC](#5-endpoints-estándar-oidc)
6. [Gestión de sesiones](#6-gestión-de-sesiones)
7. [Seguridad del flujo](#7-seguridad-del-flujo)
8. [Gestión de clientes (Client Management)](#8-gestión-de-clientes)
9. [Gestión de tokens (Token Management)](#9-gestión-de-tokens)
10. [Protección de la cuenta](#10-protección-de-la-cuenta)
11. [Features pendientes o parcialmente implementadas](#11-features-pendientes-o-parcialmente-implementadas)
12. [Checklist de conformidad OIDC Core 1.0](#12-checklist-de-conformidad-oidc-core-10)
13. [Modelo de datos relevante](#13-modelo-de-datos-relevante)
14. [Errores estándar y mapeo de excepciones](#14-errores-estándar-y-mapeo-de-excepciones)

---

## 1. Objetivos

El servidor de identidad Phylax actúa como **Authorization Server** y **OpenID Provider (OP)** conforme al estándar [OpenID Connect Core 1.0](https://openid.net/specs/openid-connect-core-1_0.html) sobre OAuth 2.0 (RFC 6749).

### Objetivos funcionales

| # | Objetivo | Estado |
|---|----------|--------|
| F-01 | Autenticación de usuarios con credenciales propias | Implementado |
| F-02 | Autenticación multifactor TOTP (RFC 6238) | Implementado |
| F-03 | Enrollment / registro de dispositivo MFA | Implementado |
| F-04 | Forzar cambio de contraseña en primer login o por política | Implementado |
| F-05 | Recuperación de contraseña mediante código temporal | Implementado |
| F-06 | Registro de nuevos usuarios (auto-registro con o sin aprobación) | Implementado |
| F-07 | Aceptación de condiciones del servicio (user consent / ToS) | Implementado |
| F-08 | Aceptación granular de scopes por aplicación cliente | Implementado |
| F-09 | Login delegado a proveedores externos (Google, SAML) | Implementado |
| F-10 | Emisión de access token (JWT), id_token y refresh token | Implementado |
| F-11 | Authorization Code Flow | Implementado |
| F-12 | PKCE (RFC 7636) para clientes públicos | **Pendiente** |
| F-13 | Client Credentials grant (machine-to-machine) | Parcial (API Keys) |
| F-14 | Device Authorization Grant (RFC 8628) | Parcial |
| F-15 | Refresh token rotation | **Pendiente** |
| F-16 | Token revocation (RFC 7009) | Parcial |
| F-17 | Token introspection (RFC 7662) | **Pendiente** |
| F-18 | Userinfo endpoint | Parcial |
| F-19 | RP-Initiated Logout (OIDC Session Management) | **Pendiente** |
| F-20 | Front-channel / Back-channel logout | **Pendiente** |
| F-21 | Step-up authentication (ACR) | **Pendiente** |
| F-22 | Dynamic Client Registration (RFC 7591) | Parcial |
| F-23 | JWKS endpoint | Implementado |
| F-24 | OpenID Discovery (`.well-known/openid-configuration`) | Implementado |
| F-25 | Rate limiting y protección brute-force | Implementado (BucketService) |
| F-26 | Bloqueo y desbloqueo de cuenta | Implementado |

---

## 2. Flujos OAuth 2.0 / OIDC

### 2.1 Authorization Code Flow (RFC 6749 §4.1 + OIDC Core §3.1)

El flujo más seguro y recomendado. Usado por aplicaciones web con backend.

```
Browser           Phylax (OP)            Client App (RP)         Resource Server
  |                   |                       |                        |
  |-- GET /authorize ->|                       |                        |
  |   (client_id,     |                       |                        |
  |    redirect_uri,  |                       |                        |
  |    scope, state,  |                       |                        |
  |    nonce)         |                       |                        |
  |                   |-- [login UI steps] -->|                        |
  |<-- redirect ------| code=<code>           |                        |
  |   ?code=X&state=Y |   &state=Y            |                        |
  |                   |                       |                        |
  |                   |<-- POST /token -------|                        |
  |                   |   (code, client_id,   |                        |
  |                   |    client_secret,     |                        |
  |                   |    redirect_uri)      |                        |
  |                   |-- {access_token, ---->|                        |
  |                   |    id_token,          |                        |
  |                   |    refresh_token}     |                        |
  |                   |                       |-- Bearer access_token ->|
```

**Parámetros de entrada** (`GET /authorize`):

| Parámetro | Obligatorio | Validaciones |
|-----------|-------------|--------------|
| `response_type` | Sí | Debe ser `code`. Si contiene `token` o `id_token`, es Hybrid Flow |
| `client_id` | Sí | Debe existir en el registro de clientes |
| `redirect_uri` | Sí | Debe estar en la lista blanca del cliente |
| `scope` | Sí | Debe contener `openid` para flujo OIDC. Scopes adicionales validados contra `allowedScopes` del cliente |
| `state` | Recomendado | Opaco; se devuelve sin modificar en el redirect. Protege contra CSRF |
| `nonce` | Obligatorio para OIDC | Incluido en `id_token.nonce`. Previene replay attacks |
| `code_challenge` | Obligatorio si PKCE requerido | Base64url de SHA-256 del `code_verifier` |
| `code_challenge_method` | Condicional | Solo `S256` aceptado. `plain` rechazado |
| `prompt` | Opcional | `none`, `login`, `consent`, `select_account` |
| `max_age` | Opcional | Fuerza re-autenticación si la sesión tiene más de N segundos |
| `acr_values` | Opcional | Solicita nivel de autenticación mínimo (`0`=session, `1`=password, `2`=mfa) |
| `login_hint` | Opcional | Pre-rellena el campo username |
| `id_token_hint` | Condicional | Requerido en `prompt=none` |
| `ui_locales` | Opcional | Locale para la UI de login |

**Respuesta de error** (redirect con `error=`):

| Error | Cuándo |
|-------|--------|
| `invalid_request` | Parámetros faltantes o malformados |
| `unauthorized_client` | El cliente no tiene permiso para este grant type |
| `access_denied` | Usuario rechazó el consent o acceso denegado |
| `unsupported_response_type` | `response_type` no soportado |
| `invalid_scope` | Scope solicitado no permitido para el cliente |
| `server_error` | Error interno |
| `temporarily_unavailable` | Rate limit o servicio no disponible |
| `interaction_required` | Se necesita interacción pero `prompt=none` |
| `login_required` | No hay sesión válida y `prompt=none` |
| `consent_required` | Se necesita consent y `prompt=none` |

---

### 2.2 Authorization Code Flow + PKCE (RFC 7636)

**Estado: Pendiente de implementación.**

Obligatorio para:
- Single-Page Applications (SPA)
- Aplicaciones móviles / nativas
- Cualquier cliente que no pueda guardar un secreto

**Extensiones al flujo estándar:**

```
// 1. Cliente genera verifier y challenge
code_verifier = random(32-96 bytes), base64url encoded
code_challenge = BASE64URL(SHA256(ASCII(code_verifier)))

// 2. GET /authorize agrega:
&code_challenge=<challenge>
&code_challenge_method=S256

// 3. POST /token agrega:
&code_verifier=<verifier>
// El servidor verifica: SHA256(code_verifier) == code_challenge almacenado
```

**Validaciones requeridas:**
- Almacenar `code_challenge` y `code_challenge_method` en `TemporalAuthCode`
- En token exchange: recalcular hash y comparar con el almacenado
- Rechazar si el cliente es confidencial y envía PKCE pero no debería, o viceversa (configurable por política)
- `plain` como método NO debe aceptarse (solo `S256`)

---

### 2.3 Client Credentials Grant (RFC 6749 §4.4)

Para comunicación **machine-to-machine** sin usuario humano.

```
POST /token
  grant_type=client_credentials
  &client_id=<id>
  &client_secret=<secret>
  &scope=<scope>
```

**Validaciones:**
- El cliente debe ser confidencial (`protectedWithSecret=true`)
- El cliente debe tener `client_credentials` en `allowedGrants`
- No emitir `id_token` (no hay `sub` de usuario)
- El `access_token` lleva `sub=client_id`, sin `refresh_token`
- Scopes validados contra `allowedScopes` del cliente

**Estado:** Cubierto parcialmente mediante API Keys. Falta soporte en `TokenGranter` estándar.

---

### 2.4 Refresh Token Grant (RFC 6749 §6)

```
POST /token
  grant_type=refresh_token
  &refresh_token=<token>
  &client_id=<id>
  &client_secret=<secret>   // si cliente confidencial
  &scope=<scope>             // opcional, reducir scopes
```

**Validaciones:**
- Verificar firma JWT del refresh token
- Verificar que no esté revocado (TokenStoreGateway)
- Verificar que el cliente coincida con el que emitió el token
- Verificar expiración del refresh token
- Si `scope` se especifica: debe ser subconjunto de scopes originales
- **Refresh token rotation:** emitir nuevo refresh token e invalidar el anterior — previene token theft

**Estado:** `RefreshGranter` existe. Rotation pendiente.

---

### 2.5 Resource Owner Password Credentials (ROPC) — RFC 6749 §4.3

```
POST /token
  grant_type=password
  &username=<user>
  &password=<pass>
  &scope=<scope>
  &client_id=<id>
```

> ⚠️ **Deprecado en OAuth 2.1.** Usar solo para migración legacy. No permite MFA interactivo.

**Validaciones:**
- Solo para clientes de confianza
- No iniciar flujo de MFA interactivo (si MFA requerido, rechazar con `mfa_required`)
- Proteger con rate limiting estricto

---

### 2.6 Device Authorization Grant (RFC 8628)

Para dispositivos sin navegador (TV, CLI tools, IoT).

```
// Fase 1: Solicitar código de dispositivo
POST /device_authorization
  client_id=<id>
  &scope=<scope>
→ { device_code, user_code, verification_uri, expires_in, interval }

// Fase 2: Usuario visita verification_uri e introduce user_code

// Fase 3: Device hace polling
POST /token
  grant_type=urn:ietf:params:oauth:grant-type:device_code
  &device_code=<device_code>
  &client_id=<id>
→ { access_token, ... } | { error: authorization_pending | slow_down | expired_token }
```

**Estado:** `DevicesAccessController` existe, implementación incompleta.

---

### 2.7 Hybrid Flow (OIDC Core §3.3)

`response_type=code id_token` o `code token`. El código y un token se emiten directamente en la respuesta del authorize. Poco usado. **Estado: No implementado.**

---

## 3. Autenticación HTML

### 3.1 Login con contraseña

**Flujo:**
1. Renderizar formulario con campos: `username`, `password`, `csid` (CSRF token cifrado)
2. Descifrar y validar `csid` antes de procesar
3. Cargar usuario: si no existe → `UnknownUserException`
4. Verificar que el usuario esté habilitado y aprobado
5. Verificar que no esté bloqueado (`blocked_until`)
6. Comparar contraseña (hash bcrypt/argon2 recomendado)
7. Si fallo: incrementar contador de intentos; si supera umbral → `UserLocked`
8. Si éxito: evaluar si se necesita MFA, cambio de contraseña, consent

**Validaciones requeridas:**
- `username` y `password` no vacíos
- Rate limiting por IP y por username (BucketService)
- Tiempo de respuesta constante (prevenir timing attack en enumeración de usuarios)
- Log de auditoría: intentos fallidos, bloqueos, logins exitosos
- Soporte `login_hint`: pre-rellenar username (solo render, no bypass)

---

### 3.2 MFA — Verificación TOTP

**Flujo:**
1. Usuario autenticado con contraseña pero `withMfa=false` en sesión → `MfaRequiredException`
2. Renderizar formulario OTP con campo `otp`, `csid`
3. Verificar TOTP: ventana de ±1 step (30s), verificar que el código no se reutilice (replay protection)
4. Si válido: actualizar sesión con `withMfa=true`, continuar flujo
5. Si inválido: contador de reintentos, posible bloqueo tras N fallos

**Validaciones requeridas:**
- `otp` exactamente 6 dígitos numéricos
- Verificación con tolerancia de tiempo (clock skew ±30s)
- Anti-replay: almacenar el último código verificado por usuario, rechazar reutilización
- Rate limiting específico para OTP (más estricto: 5 intentos/5 min)

---

### 3.3 Enrollment de MFA (NewMfa)

**Flujo:**
1. Usuario sin `second_factor_seed` → `NewMfaRequiredException`
2. Generar seed TOTP aleatorio (20 bytes, base32)
3. Renderizar página con QR code (URI `otpauth://totp/...`) y seed manual
4. Usuario introduce primer OTP para confirmar
5. Validar OTP contra el seed generado (aún no almacenado)
6. Si válido: persistir seed → flujo continúa con MFA activo

**Validaciones requeridas:**
- El seed se genera fresh por cada intento; no persistir hasta verificación
- El QR debe incluir: `issuer=Phylax`, `account=<username>`, `secret=<seed>`
- Algoritmo: SHA-1 (compatibilidad), 6 dígitos, período 30s

---

### 3.4 Forzar cambio de contraseña (NewPass)

**Flujo:**
1. Usuario marcado con `temporal_password` o política de expiración → `NewPasswordRequiredException`
2. Renderizar formulario: `new_password`, `confirm_password`, `csid`
3. Validar política de contraseñas
4. Hash y persistir
5. Continuar flujo de autenticación normal

**Validaciones de política de contraseñas:**
- Longitud mínima configurable (default 12)
- Al menos: 1 mayúscula, 1 minúscula, 1 dígito, 1 símbolo
- No reutilizar las últimas N contraseñas (historial)
- No puede ser igual a la actual
- No puede ser username o email
- Breach database check (Have I Been Pwned API — opcional)

---

### 3.5 Recuperación de contraseña (RecoverPass)

**Flujo:**
1. Usuario hace clic en "¿Olvidaste tu contraseña?" en el formulario de login
2. Renderizar formulario: campo `email`, `csid`
3. Generar código temporal de un solo uso (OTP alfanumérico, 6-8 chars)
4. Almacenar código con TTL (15 min), vinculado al email/username
5. Enviar email con código o enlace (conector de email)
6. Renderizar formulario de introducción de código + nueva contraseña
7. Validar código (one-time, expirado invalida), aplicar nueva contraseña

**Validaciones:**
- Siempre responder con éxito aparente aunque el email no exista (prevenir enumeración)
- Rate limiting por email/IP para envíos
- El código expira en 15 min (configurable)
- El código es de un solo uso: se elimina al verificar (`retrieveTemporalAuthCode` usa `remove()`)
- Logs de auditoría: solicitud, uso correcto, uso incorrecto

**Flujo alternativo — enlace mágico:**
- En lugar de código, generar JWT firmado con TTL incluido en claim `exp`
- Enlace: `GET /recover?token=<jwt>`
- Ventaja: no requiere introducir código manualmente

---

### 3.6 Aceptación de condiciones de servicio (User Consent)

**Flujo:**
1. `ConsentGateway.getPendingConsent()` detecta que el usuario no ha aceptado las T&C para el `relyingParty` (audience/resource server)
2. Renderizar página con texto de condiciones y botones "Aceptar" / "Cancelar"
3. Si acepta: `ConsentGateway.storeAcceptedConsent()`, continuar flujo
4. Si cancela: `AuthenticationException` → redirect con `error=access_denied`

**Validaciones:**
- El texto de condiciones es específico por `tenant` + `relyingParty` + `locale`
- Re-solicitar aceptación si cambia la versión de T&C (versionado de consent)
- Registrar timestamp de aceptación, versión del documento y IP (auditoría legal)

---

### 3.7 Aceptación de scopes (Scope Consent)

**Flujo:**
1. `ScopesConsentGateway.pendingScopes()` devuelve lista de `ScopePermission` no aceptados
2. Renderizar pantalla OAuth clásica: nombre de app, lista de permisos con label/descripción
3. Scopes `required=true` se muestran pero no son desmarcables
4. Usuario acepta o cancela
5. Si acepta: `ScopesConsentGateway.storeAcceptedScopes()`, continuar
6. Si cancela: redirect con `error=access_denied`

**Validaciones:**
- No mostrar pantalla si todos los scopes ya están aceptados
- Scopes `required` siempre se incluyen aunque el usuario no haya consentido explícitamente
- Re-solicitar si se añaden nuevos scopes al cliente (delta de scopes)
- Almacenar: username, clientId, scope, timestamp

---

### 3.8 Login delegado (Federated/Social)

**Flujo:**
1. Renderizar botones de proveedores disponibles (`DelegateLogin.providers()`)
2. Usuario selecciona proveedor → `GET /delegated/<provider_id>`
3. Construir request al proveedor (`DelegatedAccessExternalProvider.request()`)
4. Redirigir al proveedor externo
5. Callback: `DelegatedAccessController` procesa respuesta
6. Extraer `UserData` (email, name, code externo)
7. Mapear a usuario local:
   - Si existe usuario con ese email/provider: autenticar
   - Si no existe y auto-registro permitido: crear usuario
   - Si no existe y auto-registro no permitido: error
8. Crear sesión local y continuar flujo OIDC normal

**Validaciones:**
- Verificar `state` anti-CSRF en callbacks OAuth
- Verificar firma / id_token del proveedor externo antes de confiar
- Verificar que el email esté verificado en el proveedor externo
- Account linking: si mismo email existe con diferente proveedor → política configurable (link, error, nueva cuenta)
- Los tokens del proveedor externo NO se exponen; solo se usa para autenticación inicial

---

## 4. Token endpoint y grant types

**URL:** `POST /token`
**Content-Type:** `application/x-www-form-urlencoded`

### 4.1 Autenticación del cliente en el token endpoint

Métodos soportados (según `token_endpoint_auth_methods_supported` en discovery):

| Método | Descripción |
|--------|-------------|
| `client_secret_basic` | `Authorization: Basic base64(client_id:client_secret)` — Recomendado |
| `client_secret_post` | `client_id` + `client_secret` en body |
| `none` | Sin secreto — solo para clientes públicos (PKCE obligatorio) |
| `private_key_jwt` | JWT firmado con clave privada del cliente — Alta seguridad (futuro) |

**Validaciones comunes:**
- Client ID siempre requerido
- Para clientes confidenciales: verificar secreto (hash bcrypt, no plain text)
- Rate limiting en token endpoint (prevenir brute force de client_secret)
- Respuesta siempre con `Cache-Control: no-store`, `Pragma: no-cache`

### 4.2 Respuesta de token exitosa

```json
{
  "access_token": "eyJ...",
  "token_type": "Bearer",
  "expires_in": 3600,
  "refresh_token": "eyJ...",
  "id_token": "eyJ...",
  "scope": "openid profile email"
}
```

### 4.3 Respuesta de error

```json
{
  "error": "invalid_grant",
  "error_description": "Authorization code expired or already used"
}
```

Códigos de error estándar: `invalid_request`, `invalid_client`, `invalid_grant`, `unauthorized_client`, `unsupported_grant_type`, `invalid_scope`.

---

## 5. Endpoints estándar OIDC

### 5.1 OpenID Discovery (`.well-known/openid-configuration`)

**URL:** `GET /.well-known/openid-configuration`

Debe incluir todos los campos obligatorios de [OIDC Discovery 1.0](https://openid.net/specs/openid-connect-discovery-1_0.html):

```json
{
  "issuer": "https://phylax.example.com",
  "authorization_endpoint": "https://phylax.example.com/authorize",
  "token_endpoint": "https://phylax.example.com/token",
  "userinfo_endpoint": "https://phylax.example.com/userinfo",
  "jwks_uri": "https://phylax.example.com/.well-known/jwks.json",
  "registration_endpoint": "https://phylax.example.com/register",
  "scopes_supported": ["openid", "profile", "email", "phone", "address", "offline_access"],
  "response_types_supported": ["code"],
  "grant_types_supported": ["authorization_code", "refresh_token", "client_credentials", "password", "urn:ietf:params:oauth:grant-type:device_code"],
  "subject_types_supported": ["public"],
  "id_token_signing_alg_values_supported": ["RS256"],
  "token_endpoint_auth_methods_supported": ["client_secret_basic", "client_secret_post", "none"],
  "claims_supported": ["sub", "iss", "aud", "exp", "iat", "auth_time", "nonce", "acr", "email", "email_verified", "name", "preferred_username", "roles"],
  "code_challenge_methods_supported": ["S256"],
  "claims_parameter_supported": false,
  "request_parameter_supported": false,
  "end_session_endpoint": "https://phylax.example.com/logout",
  "check_session_iframe": "https://phylax.example.com/session/check"
}
```

---

### 5.2 JWKS endpoint

**URL:** `GET /.well-known/jwks.json`

```json
{
  "keys": [
    {
      "kty": "RSA",
      "use": "sig",
      "alg": "RS256",
      "kid": "2024-01",
      "n": "...",
      "e": "AQAB"
    }
  ]
}
```

**Validaciones / operaciones:**
- Múltiples claves activas para rotación sin downtime
- `kid` (Key ID) incluido en header del JWT para que el verificador seleccione la clave correcta
- Rotación de claves: periodo recomendado 90 días
- Claves antiguas: mantener N días tras rotación (tokens emitidos antes siguen siendo válidos)
- Solo exponer la clave pública; nunca la privada

---

### 5.3 Userinfo endpoint

**URL:** `GET /userinfo` (o `POST`)
**Auth:** `Authorization: Bearer <access_token>`

Devuelve claims del usuario según scopes en el access token:

| Scope | Claims devueltos |
|-------|-----------------|
| `openid` | `sub` (obligatorio) |
| `profile` | `name`, `given_name`, `family_name`, `preferred_username`, `picture`, `updated_at` |
| `email` | `email`, `email_verified` |
| `phone` | `phone_number`, `phone_number_verified` |
| `address` | `address` (objeto) |

**Validaciones:**
- Verificar firma y expiración del access token
- Verificar que el token no esté revocado
- El `sub` devuelto debe coincidir con el `sub` del access token
- Respuesta firmada como JWT si cliente lo solicitó en `userinfo_signed_response_alg`

---

### 5.4 Token Revocation endpoint (RFC 7009)

**URL:** `POST /revoke`
**Estado: Pendiente / Parcial**

```
POST /revoke
  token=<token>
  &token_type_hint=refresh_token  // opcional
  &client_id=<id>
  &client_secret=<secret>
```

**Comportamiento:**
- Siempre responde `200 OK` aunque el token no exista (prevenir enumeración)
- Si es refresh token: revocar + revocar todos los access tokens derivados
- Si es access token: añadir a revocation list (TTL = expiración del token)
- Validar que el token pertenece al cliente solicitante

---

### 5.5 Token Introspection endpoint (RFC 7762)

**URL:** `POST /introspect`
**Estado: Pendiente**

Solo accesible para resource servers autorizados (no para clientes finales).

```
POST /introspect
  token=<token>
  &token_type_hint=access_token
  Authorization: Basic <resource_server_credentials>
```

**Respuesta activa:**
```json
{
  "active": true,
  "sub": "user@example.com",
  "client_id": "myapp",
  "scope": "openid profile",
  "exp": 1735689600,
  "iat": 1735686000,
  "iss": "https://phylax.example.com",
  "jti": "abc123",
  "token_type": "Bearer"
}
```

**Respuesta inactiva:**
```json
{ "active": false }
```

---

### 5.6 End Session / Logout (OIDC RP-Initiated Logout)

**URL:** `GET /logout`
**Estado: Pendiente**

```
GET /logout
  ?id_token_hint=<id_token>
  &post_logout_redirect_uri=https://app.example.com/logout
  &state=<opaque>
  &client_id=<id>
```

**Flujo:**
1. Validar `id_token_hint`: verificar firma, extraer `sub` y `sid`
2. Invalidar sesión del servidor (borrar `SessionInfo`)
3. Revocar refresh tokens del usuario/cliente
4. Renderizar página de confirmación de logout (si no `id_token_hint` o si requiere confirmación)
5. Opcionalmente notificar a otros clientes de la sesión (Front-channel / Back-channel logout)
6. Redirigir a `post_logout_redirect_uri` si está en lista blanca del cliente

---

### 5.7 Front-Channel Logout (OIDC Front-Channel Logout 1.0)

**Estado: Pendiente**

El OP incluye un `<iframe>` por cada RP en la sesión, cada uno con su `frontchannel_logout_uri`.
Las iframes se cargan en el navegador del usuario, triggering logout en cada RP simultáneamente.

---

### 5.8 Back-Channel Logout (OIDC Back-Channel Logout 1.0)

**Estado: Pendiente**

El OP realiza HTTP POST directo al `backchannel_logout_uri` de cada RP registrado.
Más fiable que front-channel (no depende del navegador).

Payload: JWT `logout_token` con `sub`, `sid`, `iss`, `aud`, `iat`, `jti`, claim `"events": {"http://schemas.openid.net/event/backchannel-logout": {}}`.

---

## 6. Gestión de sesiones

### 6.1 Sesión del Authorization Server

Phylax mantiene sesión propia (cookie `AUTH_SESSION_ID`) independiente de las sesiones de las aplicaciones cliente.

```
SessionInfo {
  csid:           String      // challenge session id (token CSRF)
  userId:         String      // username autenticado
  clientId:       String      // cliente que inició el flujo
  grant:          String      // tipo de grant
  withMfa:        boolean     // si se completó MFA
  issuer:         String      // tenant/issuer
  validationData: AuthenticationData // claims, roles, scopes para el token
}
```

**Política de sesión:**
- TTL de sesión de autenticación: 10 min (tiempo para completar el flujo)
- TTL de sesión SSO: configurable, default 8h
- Renovar sesión SSO en cada uso activo (sliding window)
- Sesión almacenada en BD (SessionStoreSqlAdapter), no en cookie
- La cookie solo contiene el ID de sesión (opaco)
- Cookie flags: `HttpOnly`, `Secure`, `SameSite=Lax`

### 6.2 Parámetros `prompt` y su efecto en sesión

| `prompt` | Comportamiento |
|----------|---------------|
| `none` | No mostrar UI. Si sesión válida → emitir código. Si no → `login_required` o `interaction_required` |
| `login` | Forzar nuevo login aunque haya sesión SSO |
| `consent` | Forzar pantalla de consent aunque ya se haya aceptado |
| `select_account` | Mostrar selector de cuenta si hay múltiples sesiones |

### 6.3 Parámetro `max_age`

Si `auth_time` (almacenado en sesión) + `max_age` < `now()`:
- Redirigir al login forzando re-autenticación
- Incluir `auth_time` en `id_token`

### 6.4 Claim `acr` (Authentication Context Class Reference)

| Valor | Significado | Cuándo se emite |
|-------|-------------|-----------------|
| `0` | Session-based auth (sin credenciales en este flujo) | SSO cookie válida |
| `1` | Password authentication | Login con contraseña |
| `2` | MFA (password + TOTP) | Login con contraseña + OTP verificado |

Si el cliente solicita `acr_values=2` y la sesión actual tiene `acr=1`:
- Iniciar step-up authentication (solicitar MFA) — **Pendiente**
- Si no es posible → `access_denied`

---

## 7. Seguridad del flujo

### 7.1 CSRF Protection (CSID)

Cada formulario HTML incluye un `csid` (challenge session id) generado y firmado por `SecureHtmlBuilder`.

- Generado en el render del formulario
- Verificado en el POST antes de cualquier procesamiento
- Firmado con clave temporal (TemporalKeysGateway)
- Invalida el formulario si se recarga (previene double-submit)

### 7.2 State parameter

El parámetro `state` en el authorization request:
- Generado por el cliente (RP), no por el OP
- Devuelto sin modificar en el redirect
- El cliente debe verificar que coincide con el generado
- Phylax lo almacena en sesión y lo valida en el redirect

### 7.3 Nonce

El parámetro `nonce` en el authorization request (OIDC):
- Almacenado en `TemporalAuthCode`
- Incluido en el `id_token` como claim `nonce`
- El cliente debe verificar que el `nonce` del `id_token` coincide con el enviado
- Previene replay attacks de id_tokens

### 7.4 Token Security

- **Algoritmo de firma:** RS256 (RSA-SHA256) con clave 2048+ bits
- **Algoritmo alternativo:** ES256 (ECDSA P-256) — más eficiente, recomendado
- **`jti` (JWT ID):** UUID único por token. Permite detección de replay.
- **Expiración `access_token`:** 1 hora (configurable por cliente)
- **Expiración `refresh_token`:** 30 días (configurable), absolute o sliding
- **Expiración `id_token`:** igual que access_token

### 7.5 TLS y cabeceras de seguridad

- TLS 1.2+ obligatorio en producción (TLS 1.3 recomendado)
- HSTS con `max-age` largo
- `Content-Security-Policy` en páginas HTML
- `X-Frame-Options: DENY` (prevenir clickjacking)
- `X-Content-Type-Options: nosniff`
- `Referrer-Policy: no-referrer` (no filtrar state/code por Referer)

### 7.6 Rate Limiting (BucketService)

Límites recomendados por operación:

| Endpoint / Operación | Límite sugerido |
|----------------------|----------------|
| Login intento | 10/min por IP, 5/min por username |
| Token endpoint | 60/min por cliente |
| OTP verification | 5/5min por usuario |
| Password recovery email | 3/hora por email, 10/hora por IP |
| Register | 5/hora por IP |
| Introspect / Revoke | 120/min por cliente |

### 7.7 Audit Log

Eventos a registrar para seguridad y cumplimiento:

| Evento | Datos a registrar |
|--------|------------------|
| Login exitoso | timestamp, userId, clientId, IP, acr, sessionId |
| Login fallido | timestamp, username_intento, IP, motivo |
| Bloqueo de cuenta | timestamp, userId, IP, duración |
| MFA verificado | timestamp, userId, clientId |
| MFA fallido | timestamp, userId, IP |
| Token emitido | timestamp, userId, clientId, grant_type, scopes, jti |
| Token revocado | timestamp, jti, revocador |
| Password cambiada | timestamp, userId, motivo (forced/user/recovery) |
| Consent aceptado | timestamp, userId, clientId, scopes, versión T&C |
| Logout | timestamp, userId, sessionId |
| Delegated login | timestamp, userId, provider, externalId |

---

## 8. Gestión de clientes

### 8.1 Tipos de cliente

| Tipo | `protectedWithSecret` | Grants permitidos | PKCE |
|------|-----------------------|-------------------|------|
| Confidencial (server-side) | true | authorization_code, refresh_token, client_credentials | Opcional |
| Público (SPA/móvil) | false | authorization_code, refresh_token | **Obligatorio** |
| De confianza (first-party) | true | authorization_code, password, refresh_token | - |
| Pre-autorizado | - | Sin pantalla de consent | - |

### 8.2 Registro de cliente (Dynamic Registration — RFC 7591)

**Estado:** `ClientRegisterController` parcialmente implementado.

```json
POST /register
{
  "client_name": "My App",
  "redirect_uris": ["https://app.example.com/callback"],
  "grant_types": ["authorization_code", "refresh_token"],
  "response_types": ["code"],
  "token_endpoint_auth_method": "client_secret_basic",
  "scope": "openid profile email",
  "logo_uri": "https://app.example.com/logo.png",
  "backchannel_logout_uri": "https://app.example.com/logout"
}
```

**Validaciones:**
- `redirect_uris` obligatorio; URLs deben ser HTTPS (excepto localhost en dev)
- No permitir wildcards en `redirect_uris` para clientes confidenciales
- `grant_types` coherente con `token_endpoint_auth_method`
- Si registro abierto: generar `client_id` y `client_secret` automáticamente
- Si registro restringido: requiere `initial_access_token`

### 8.3 Validación de redirect_uri

- Comparación exacta (incluyendo query string y trailing slash)
- No aceptar fragments (`#`)
- No aceptar URLs con credenciales embebidas (`http://user:pass@...`)
- `localhost` solo en entorno de desarrollo

---

## 9. Gestión de tokens

### 9.1 Claims del ID Token (OIDC Core §2)

Claims obligatorios:

| Claim | Tipo | Descripción |
|-------|------|-------------|
| `iss` | URL | Issuer identifier (= URL del OP) |
| `sub` | String | Subject (username o ID de usuario, estable, no reutilizable) |
| `aud` | String/Array | Audience (client_id del RP) |
| `exp` | NumericDate | Expiración |
| `iat` | NumericDate | Issued at |

Claims condicionales:

| Claim | Condición |
|-------|-----------|
| `auth_time` | Siempre recomendado; obligatorio si `max_age` en request |
| `nonce` | Obligatorio si `nonce` en authorization request |
| `acr` | Incluir si `acr_values` en request |
| `at_hash` | Obligatorio en Hybrid Flow con `id_token` directo |
| `c_hash` | Obligatorio en Hybrid Flow con `code` |

### 9.2 Claims del Access Token

Claims recomendados para interoperabilidad:

```json
{
  "iss": "https://phylax.example.com",
  "sub": "user@example.com",
  "aud": ["https://api.example.com"],
  "azp": "myclient",
  "exp": 1735689600,
  "iat": 1735686000,
  "jti": "unique-token-id",
  "scope": "openid profile email",
  "roles": ["admin", "user"],
  "acr": "2"
}
```

### 9.3 Rotación de claves de firma

1. Generar nueva clave (N días antes de rotación)
2. Publicar nueva clave en JWKS (con `kid` nuevo)
3. Empezar a firmar nuevos tokens con nueva clave
4. Mantener clave antigua en JWKS durante TTL máximo de tokens (`exp` máximo actual)
5. Retirar clave antigua del JWKS

### 9.4 Refresh Token Rotation

Al usar un refresh token:
1. Emitir nuevo refresh token
2. Invalidar el refresh token usado
3. Si el mismo refresh token se usa dos veces: **asumir robo** → revocar toda la familia de tokens del usuario/cliente

---

## 10. Protección de la cuenta

### 10.1 Bloqueo por intentos fallidos

Eventos del dominio: `UserLocked` / `UserUnlocked`

| Condición | Acción |
|-----------|--------|
| N intentos fallidos (configurable, default 5) | Bloquear cuenta hasta timestamp (`blocked_until`) |
| Admin desbloquea manualmente | `UserUnlocked` |
| Tiempo de bloqueo expirado | Auto-desbloqueo en siguiente intento |
| Intento durante bloqueo | Extender bloqueo (progressive backoff) |

### 10.2 Estados de usuario

| Estado | Puede autenticarse |
|--------|--------------------|
| `enabled=true, approved=ACCEPTED, blocked_until=null/pasado` | Sí |
| `enabled=false` | No — cuenta deshabilitada |
| `approved=PENDING` | No — pendiente de aprobación |
| `approved=REJECTED` | No — acceso rechazado |
| `blocked_until > now()` | No — cuenta temporalmente bloqueada |

### 10.3 Seguridad de contraseñas

- **Hash:** BCrypt (cost 12+) o Argon2id (recomendado para nuevas implementaciones)
- **Salt:** Automático en BCrypt/Argon2; nunca reutilizar
- **Temporal password:** campo separado `temporal_password`, limpiado al cambiar
- **Historial:** almacenar hashes de últimas N contraseñas para prevenir reutilización

---

## 11. Features pendientes o parcialmente implementadas

### P-01: PKCE (RFC 7636) — **Prioridad Alta**

- Almacenar `code_challenge` y `code_challenge_method` en `TemporalAuthCode`
- En token exchange: verificar `SHA256(code_verifier) == stored_challenge`
- Hacer PKCE obligatorio para clientes públicos (`protectedWithSecret=false`)

### P-02: Token Introspection (RFC 7662) — **Prioridad Alta**

- Nuevo endpoint `POST /introspect`
- Autenticado por resource servers (no por clientes finales)
- Verificar JWT, expiración, revocation list

### P-03: Refresh Token Rotation — **Prioridad Alta**

- En `RefreshGranter`: emitir nuevo refresh token, invalidar el anterior
- Detectar reutilización: si refresh token ya invalidado → revocar familia completa

### P-04: RP-Initiated Logout — **Prioridad Media**

- Endpoint `GET /logout`
- Validar `id_token_hint`, invalidar sesión, redirigir a `post_logout_redirect_uri`

### P-05: Back-Channel Logout — **Prioridad Media**

- Al cerrar sesión, notificar a todos los RPs registrados con sesión activa
- HTTP POST asíncrono al `backchannel_logout_uri` con `logout_token` JWT

### P-06: Step-up Authentication — **Prioridad Media**

- Si `acr_values` solicitado > `acr` de sesión actual: solicitar step-up sin iniciar flujo nuevo
- Guardar estado de sesión parcial durante step-up

### P-07: Client Credentials Grant completo — **Prioridad Media**

- Soporte estándar en `TokenGranter` (más allá de API Keys)
- Sin `id_token`, sin `refresh_token`
- `sub` = `client_id`

### P-08: Userinfo endpoint completo — **Prioridad Baja**

- Verificar access token entrante
- Mapear claims según scopes incluidos en el token
- Soporte `application/jwt` en respuesta (signed userinfo)

### P-09: Device Authorization Grant completo — **Prioridad Baja**

- Completar `DevicesAccessController`
- Polling con `authorization_pending`, `slow_down`, `expired_token`
- UI para introducir `user_code` en `verification_uri`

### P-10: Consent versionado — **Prioridad Baja**

- Añadir campo `version` al registro de T&C
- Re-solicitar consent cuando la versión cambia

### P-11: Email verification en registro — **Prioridad Media**

- Tras registro: enviar email con enlace de verificación
- Marcar `email_verified=false` hasta verificación
- Bloquear login hasta verificación si política lo requiere

### P-12: Pushed Authorization Requests / PAR (RFC 9126) — **Prioridad Baja**

- Endpoint `POST /par` que devuelve `request_uri`
- El cliente usa `request_uri` en lugar de parámetros en `GET /authorize`
- Evita exponer parámetros sensibles en URLs de browser

### P-13: DPoP / Sender-Constrained Tokens (RFC 9449) — **Prioridad Baja**

- Tokens vinculados a la clave pública del cliente
- Previene uso de tokens robados

---

## 12. Checklist de conformidad OIDC Core 1.0

### Obligatorio (MUST)

- [x] `response_type=code` soportado
- [x] `scope=openid` procesado
- [x] `id_token` emitido con claims mínimos (`iss`, `sub`, `aud`, `exp`, `iat`)
- [x] `nonce` incluido en `id_token` si enviado en request
- [x] JWKS endpoint expuesto
- [x] Discovery endpoint expuesto
- [ ] `id_token` verificable contra JWKS
- [ ] `userinfo` endpoint responde con `sub` correcto
- [ ] `auth_time` incluido cuando se requiere
- [ ] Manejo correcto de `prompt=none`
- [ ] Error `login_required` cuando no hay sesión y `prompt=none`

### Recomendado (SHOULD)

- [x] `acr` en `id_token`
- [ ] `at_hash` en `id_token`
- [ ] `sub` es pairwise o pseudo-anónimo (configurable)
- [ ] Soporte `ui_locales`
- [ ] Soporte `login_hint`
- [ ] Soporte `max_age`

---

## 13. Modelo de datos relevante

### Entidades principales

```
User
├── uid: UUID
├── email: String (unique per tenant)
├── password: String (hashed)
├── temporal_password: String (hashed, nullable)
├── name: String
├── second_factor_seed: String (base32, nullable)
├── use_second_factors: boolean
├── enabled: boolean
├── approved: enum {ACCEPTED, REJECTED, PENDING}
├── blocked_until: Timestamp (nullable)
├── tenant: String
└── provider: String (nullable — delegated login provider)

OAuthClient
├── client_id: String
├── client_secret: String (hashed, nullable)
├── allowed_scopes: List<String>
├── allowed_grants: List<String>
├── allowed_redirect_uris: List<String>
├── protected_with_secret: boolean
├── tenant: String
└── backchannel_logout_uri: String (nullable)

Session (AuthSession)
├── session_id: String (opaque, stored in cookie)
├── csid: String (CSRF challenge)
├── user_id: String
├── client_id: String
├── grant: String
├── with_mfa: boolean
├── issuer: String
├── auth_time: Timestamp
├── acr: String
└── claims_data: JSON (AuthenticationData serialized)

TemporalAuthCode
├── code_id: String (random, one-time)
├── authentication_data: JSON (AuthenticationData)
├── client_details: JSON (ClientDetails)
├── nonce: String (nullable)
├── request: JSON (AuthRequest)
├── code_challenge: String (nullable — PKCE)
├── code_challenge_method: String (nullable — PKCE)
└── expires_at: Timestamp

ConsentRecord
├── tenant: String
├── user_id: String
├── relying_party: String
├── accepted_at: Timestamp
└── version: String

ScopeConsentRecord
├── tenant: String
├── user_id: String
├── client_id: String
├── scope: String
└── accepted_at: Timestamp

RevokedToken
├── jti: String
├── revoked_at: Timestamp
└── expires_at: Timestamp (para limpieza)
```

---

## 14. Errores estándar y mapeo de excepciones

### Mapeo de excepciones de dominio

| Excepción | Contexto | Respuesta |
|-----------|----------|-----------|
| `UnknownUserException` | Login HTML | Renderizar login con error genérico ("Credenciales incorrectas") |
| `WrongCredentialsException` | Login HTML | Ídem — no distinguir usuario/contraseña |
| `MfaRequiredException` | Login HTML | Redirigir a paso MFA |
| `NewMfaRequiredException` | Login HTML | Redirigir a enrollment MFA |
| `NewPasswordRequiredException` | Login HTML | Redirigir a cambio de contraseña |
| `ConsentRequiredException` | Login HTML | Redirigir a página de consent |
| `ClientScopeConsentRequiredException` | Login HTML | Redirigir a scope consent |
| `NotAllowedAccessUserException` | Login HTML | Renderizar error de acceso denegado |
| `UnknownUserException` | Token endpoint (ROPC) | `{"error":"invalid_grant"}` |
| `WrongCredentialsException` | Token endpoint | `{"error":"invalid_grant"}` |
| Sesión no encontrada | `/authorize` | Re-renderizar login |
| `null` session_id en cookie | `/authorize` | Iniciar flujo nuevo (no NPE) |
| Client no encontrado | `/authorize` | Renderizar error (no redirect — redirect_uri no verificada) |
| redirect_uri inválida | `/authorize` | Renderizar error (no redirect) |
| Parámetros inválidos | Token endpoint | `{"error":"invalid_request"}` |
| Client auth fallida | Token endpoint | `{"error":"invalid_client"}`, HTTP 401 |

> **Nota:** Nunca redirigir con `error=` si `redirect_uri` no es de confianza — el atacante podría usar el redirect para exfiltrar el error.

---

*Este documento debe actualizarse conforme se implementen nuevas features o cambien los requisitos.*
