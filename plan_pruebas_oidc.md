# Plan de Pruebas Automáticas — Flujos OIDC

## Objetivo

Diseñar una batería de pruebas automáticas que verifiquen los flujos principales del servidor OIDC sin depender de sistemas externos (base de datos, SMTP, TOTP real). Todas las dependencias externas se sustituyen por implementaciones alternativas CDI con datos fijos, ejecutables en CI con `mvn test`.

## Alcance de flujos a probar

| Flujo | Descripción |
|-------|-------------|
| **Login básico** | Credenciales correctas → auth code → token |
| **Credenciales erróneas** | Login fallido, respuesta sin redirect |
| **Primera contraseña** | Login → `newPasswordRequired` → cambio → código |
| **Aceptación de condiciones** | Login → `consentRequired` → aceptación → código |
| **Scopes** | Token incluye los scopes correctos según client + solicitud |
| **Registro de MFA** | Login → `newMfaRequired` → setup → verificación → código |
| **Login con MFA** | Login → `mfaRequired` → OTP correcto → código |
| **Cambio de contraseña** | Flujo /recover con código de recuperación |
| **Refresh token** | Token válido → nuevo token vía refresh |
| **Password grant** | Flujo directo sin navegador vía POST /token |

## Herramientas (todas ya en `pom.xml`)

- **JUnit 5** via `quarkus-junit5` — runner de pruebas
- **RestAssured** via `rest-assured` — cliente HTTP para pruebas de integración
- **Mockito** via `mockito-core` — mocks para pruebas unitarias puras
- **`@QuarkusTest`** — levanta el servidor Quarkus con CDI real para tests de integración
- **`@Alternative @Priority(1)`** — reemplaza beans de producción en el classpath de test

> No se usa ninguna base de datos, broker de mensajes ni servidor SMTP real.

## Separación de tipos de test

Los dos tipos de test tienen necesidades opuestas respecto a los paquetes:

| Tipo | Paquete | Razón |
|------|---------|-------|
| **Unit tests** | `net.civeira.phylax.features.oauth.*` | Mismo paquete que el código fuente para acceder a miembros package-private |
| **Integración / E2E** | `net.civeira.phylax.testing.oauth.*` | Paquete propio para los beans `@Alternative` y la infraestructura de test |

El indexer Jandex de Quarkus escanea **todo** `src/test/java` independientemente del paquete, por lo que los beans `@Alternative @Priority(1)` en `net.civeira.phylax.testing.oauth` son descubiertos automáticamente y sobrescriben los beans de producción en los `@QuarkusTest`. No se necesita configuración adicional de CDI scan.

## Estructura de directorios propuesta

```
src/test/java/
│
│   # ── UNIT TESTS ────────────────────────────────────────────────────────────
│   # Mismo paquete que el código fuente para acceder a miembros package-private.
│   # Solo JUnit 5 + Mockito, sin @QuarkusTest ni CDI.
│
├── net/civeira/phylax/features/oauth/
│   ├── user/application/
│   │   ├── LoginUsecaseTest.java
│   │   ├── ConsentUsecaseTest.java
│   │   └── ChangePasswordUsecaseTest.java
│   ├── scopes/application/
│   │   └── ScopesConsentUsecaseTest.java
│   └── authentication/domain/model/
│       └── AuthenticationResultTest.java   # acceso package-private si fuera necesario
│
│   # ── INTEGRATION / E2E TESTS ──────────────────────────────────────────────
│   # Paquete propio. @QuarkusTest levanta el servidor con CDI completo.
│   # Los beans @Alternative de este paquete sustituyen los beans de producción.
│
└── net/civeira/phylax/testing/oauth/
    ├── fixtures/
    │   └── OidcTestFixtures.java           # Constantes: tenant, clientId, users, passwords
    ├── alt/                                # Beans @Alternative CDI — infraestructura fija
    │   ├── FixedClientStoreGateway.java    # ClientStoreGateway hardcodeado
    │   ├── FixedSessionStore.java          # SessionStoreGateway en memoria
    │   ├── FixedTemporalKeysGateway.java   # TemporalKeysGateway determinista (bypass CSRF/AES)
    │   ├── FixedTokenSigner.java           # TokenSigner RSA en memoria
    │   ├── FixedDecoratePageSpi.java       # DecoratePageSpi que devuelve el HTML tal cual
    │   ├── FixedDelegateGateway.java       # DelegateLoginGateway sin providers
    │   └── NoOpEventNotifier.java          # EventNotifierGateway no-op
    ├── scenario/                           # Bean @Alternative programable por escenario
    │   ├── ScenarioLoginGateway.java       # LoginGateway cuyo comportamiento se configura en @BeforeEach
    │   ├── ScenarioConsentGateway.java     # ConsentGateway configurable
    │   ├── ScenarioMfaGateway.java         # UserMfaGateway configurable
    │   └── ScenarioChangePasswordGateway.java
    ├── client/
    │   └── OidcFlowClient.java             # Helper RestAssured: cookies, CSRF, extracción de forms
    └── flow/                               # Tests @QuarkusTest
        ├── OidcIntegrationTestBase.java    # Base class con @QuarkusTest y helpers comunes
        ├── LoginFlowTest.java
        ├── ConsentFlowTest.java
        ├── MfaFlowTest.java
        ├── NewMfaFlowTest.java
        ├── NewPasswordFlowTest.java
        ├── PasswordGrantTest.java
        ├── RefreshTokenTest.java
        ├── ScopesTest.java
        ├── RecoverPasswordFlowTest.java
        └── EndToEndFlowTest.java
```

---

## Fase 1: Infraestructura de test (beans alternativos)

Todos los beans de esta fase van en `net.civeira.phylax.testing.oauth.*` dentro de `src/test/java`. Quarkus los descubre automáticamente vía Jandex en los `@QuarkusTest`.

### T1.1 — Crear `OidcTestFixtures`

`net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures` — constantes compartidas:

```java
package net.civeira.phylax.testing.oauth.fixtures;

public final class OidcTestFixtures {
  public static final String TENANT    = "test-tenant";
  public static final String CLIENT_ID = "test-client";
  public static final String CLIENT_SECRET = "test-secret";
  public static final String REDIRECT_URI  = "http://localhost/callback";
  public static final String USERNAME  = "alice@example.com";
  public static final String PASSWORD  = "correct-password";
  public static final String WRONG_PASSWORD = "wrong-password";
  public static final String SCOPE     = "openid profile email";
  public static final String MFA_CODE  = "123456";
  public static final String RECOVER_CODE = "valid-recover-code";
  private OidcTestFixtures() {}
}
```

### T1.2 — Crear `FixedClientStoreGateway`

`net.civeira.phylax.testing.oauth.alt.FixedClientStoreGateway` — bean `@Alternative @Priority(1) @ApplicationScoped` que implementa `ClientStoreGateway`:

- `loadPublic(tenant, clientId, redirect)` → devuelve `ClientDetails` con `allowedScopes=["*"]`, `allowedGrants=["authorization_code","password","refresh_token","form"]` si `clientId = CLIENT_ID` y `redirect` contiene `localhost/callback`; vacío en otro caso
- `loadPrivate(tenant, clientId, clientSecret)` → mismo cliente si secreto coincide
- `loadPreautorized(tenant, clientId)` → mismo cliente sin verificar secreto

### T1.3 — Crear `FixedTokenSigner`

`net.civeira.phylax.testing.oauth.alt.FixedTokenSigner` — bean `@Alternative @Priority(1) @ApplicationScoped` que implementa `TokenSigner`:

- Genera un par de claves RSA-2048 una sola vez en `@PostConstruct` (guardado en memoria)
- `sign(tenant, builder, expiration)` → firma con clave privada RSA
- `signKeypass(...)` → firma con HMAC-SHA256 usando una clave fija `"test-keypass"`
- `verifyTokenPayload(tenant, token)` → verifica con clave pública y devuelve claims
- `verifiedKeypass(tenant, token)` → verifica HMAC-SHA256
- `keysAsJwks(tenant)` → devuelve el par de claves como JWK Set (permite validar tokens emitidos)

> Permite verificar los tokens emitidos en los tests sin configurar keystores.

### T1.4 — Crear `FixedTemporalKeysGateway`

`net.civeira.phylax.testing.oauth.alt.FixedTemporalKeysGateway` — bean `@Alternative @Priority(1) @ApplicationScoped` que implementa `TemporalKeysGateway`:

- `currentKey()` → devuelve clave AES fija de 32 bytes en hexadecimal (conocida en los tests)
- `encrypt(value)` → devuelve `value` tal cual (sin cifrado real)
- `verifyCypher(value)` → devuelve `Optional.of(value)` (identidad)
- `verifyToken(token)` → si `token` es no nulo y no vacío, devuelve `Optional.of("verified")`; vacío en otro caso
- `registerTemporalAuthCode(code)` → genera UUID, guarda `TemporalAuthCode` en un `ConcurrentHashMap` interno, devuelve el UUID
- `retrieveTemporalAuthCode(code)` → busca en el mapa interno

> El bypass de `verifyToken` permite que el campo `csid` de los formularios HTML pase la verificación sin necesidad de ejecutar JS en el cliente.

### T1.5 — Crear `FixedSessionStore`

`net.civeira.phylax.testing.oauth.alt.FixedSessionStore` — bean `@Alternative @Priority(1) @ApplicationScoped` que implementa `SessionStoreGateway`:

- Estado en `ConcurrentHashMap<String, SessionInfo>`
- `saveSession(state, client, grant, data, csid)` → guarda bajo `state`
- `loadSession(state)` → busca en el mapa
- `updateSession(newState, oldState)` → copia entrada
- `deleteSession(state)` → elimina entrada
- Expone método `clear()` anotado con `@BeforeEach`-compatible para limpiar entre tests

### T1.6 — Crear `FixedDecoratePageSpi`

`net.civeira.phylax.testing.oauth.alt.FixedDecoratePageSpi` — bean `@Alternative @Priority(1) @ApplicationScoped` que implementa `DecoratePageSpi`:

- `getFullPage(title, content, locale)` → devuelve `content` directamente, sin envolver en HTML completo
- Permite que los tests busquen campos concretos en la respuesta HTML sin preocuparse por el template global

### T1.7 — Crear `FixedDelegateGateway` y `NoOpEventNotifier`

En `net.civeira.phylax.testing.oauth.alt`:
- `FixedDelegateGateway` implementa `DelegateLoginGateway`: todos los métodos retornan vacío / listas vacías
- `NoOpEventNotifier` implementa `EventNotifierGateway`: método `notify(...)` no hace nada

### T1.8 — Crear beans `Scenario*Gateway` programables

En lugar de una clase por escenario (que proliferaría), cada gateway de dominio que varía entre tests se implementa como un bean programable con `Supplier` configurable en `@BeforeEach`:

```java
// net.civeira.phylax.testing.oauth.scenario.ScenarioLoginGateway
@Alternative @Priority(1) @ApplicationScoped
public class ScenarioLoginGateway implements LoginGateway {

  private Supplier<AuthenticationResult> onValidate = () -> AuthenticationResult.right(defaultData());
  private Supplier<AuthenticationResult> onPreAuth  = () -> AuthenticationResult.right(defaultData());

  /** Configurable desde los tests: gateway.whenValidate(() -> AuthenticationResult.mfaRequired(...)) */
  public void whenValidate(Supplier<AuthenticationResult> behavior) { this.onValidate = behavior; }
  public void whenPreAuth(Supplier<AuthenticationResult> behavior)  { this.onPreAuth  = behavior; }
  public void reset() { onValidate = () -> AuthenticationResult.right(defaultData()); onPreAuth = onValidate; }

  @Override public AuthenticationResult validateUserData(...)       { return onValidate.get(); }
  @Override public AuthenticationResult validatePreAuthenticated(...){ return onPreAuth.get(); }

  private static AuthenticationData defaultData() { /* usuario alice, tenant test-tenant */ }
}
```

Crear de la misma forma: `ScenarioConsentGateway`, `ScenarioMfaGateway`, `ScenarioChangePasswordGateway`.

### T1.9 — Crear `OidcFlowClient`

`net.civeira.phylax.testing.oauth.client.OidcFlowClient` — helper que envuelve RestAssured:

```java
public class OidcFlowClient {
  // GET /oauth/openid/{tenant}/auth?client_id=...&redirect_uri=...&response_type=code&...
  public Response startAuthFlow(String tenant, String clientId, String redirect, String scope);

  // POST /oauth/openid/{tenant}/auth con campo "csid"="test-csid" y credentials
  // La contraseña se envía en texto plano porque FixedTemporalKeysGateway.decrypt() es identidad
  public Response submitLogin(String tenant, String username, String password, String preSessionCookie);

  // POST /oauth/openid/{tenant}/auth con step=mfa, mfa_code=...
  public Response submitMfa(String tenant, String mfaCode, String preSessionCookie);

  // POST /oauth/openid/{tenant}/auth con step=consent, consent=on
  public Response submitConsent(String tenant, String preSessionCookie);

  // POST /oauth/openid/{tenant}/auth con step=new_pass, old_pass=..., new_pass=...
  public Response submitNewPass(String tenant, String oldPass, String newPass, String preSessionCookie);

  // POST /oauth/openid/{tenant}/auth con step=new-mfa-method, mfa_type=totp
  public Response submitNewMfaSetup(String tenant, String mfaType, String preSessionCookie);

  // POST /oauth/openid/{tenant}/auth con step=new-mfa-verify, mfa_code=...
  public Response submitNewMfaVerify(String tenant, String mfaCode, String preSessionCookie);

  // Extrae el parámetro "code" de la URL de redirect en una respuesta 302
  public String extractAuthCode(Response response);

  // Extrae la cookie PRE_SESSION_ID de la respuesta
  public String extractPreSessionCookie(Response response);

  // POST /oauth/openid/{tenant}/token grant_type=authorization_code
  public Response exchangeCode(String tenant, String code, String redirectUri, String clientId);

  // POST /oauth/openid/{tenant}/token grant_type=password
  public Response passwordGrant(String tenant, String clientId, String secret, String username, String password, String scope);

  // POST /oauth/openid/{tenant}/token grant_type=refresh_token
  public Response refreshToken(String tenant, String refreshToken, String clientId, String secret);
}
```

### T1.10 — Crear base class `OidcIntegrationTestBase`

`net.civeira.phylax.testing.oauth.flow.OidcIntegrationTestBase` — clase abstracta con `@QuarkusTest` que incluye:
- Constantes de `OidcTestFixtures`
- Instancia de `OidcFlowClient`
- `@BeforeEach` que limpia `FixedSessionStore` y `FixedTemporalKeysGateway`
- Helper `assertTokenClaims(String jwt)` que valida estructura del JWT usando `FixedTokenSigner.keysAsJwks()`

---

## Fase 2: Pruebas unitarias de application services

Los tests de esta fase van en el **mismo paquete que el código fuente** (dentro de `src/test/java`) para acceder a miembros package-private si fuese necesario. No usan `@QuarkusTest` ni CDI: son JUnit 5 puro con Mockito.

### T2.1 — `LoginUsecaseTest`

`net.civeira.phylax.features.oauth.user.application.LoginUsecaseTest` — JUnit 5 + Mockito puro:

- **T2.1.1** `validatedUserData_happy_path` — `LoginGateway.validateUserData()` devuelve `right(data)` → `LoginUsecase.validatedUserData()` devuelve `right`
- **T2.1.2** `validatedUserData_wrong_credentials` — `LoginGateway` devuelve `wrongCredential` → resultado es `wrong` con `WrongCredentialsException`
- **T2.1.3** `validatedUserData_unknown_user` — `LoginGateway` devuelve `unknownName` → `UnknownUserException`
- **T2.1.4** `fillPreAuthenticated_happy_path` — `LoginGateway.validatePreAuthenticated()` devuelve `right` → resultado es `right`
- **T2.1.5** `fillPreAuthenticated_consent_required` — `LoginGateway` devuelve `consentRequired` → `ConsentRequiredException`
- **T2.1.6** `fillPreAuthenticated_mfa_required` — `LoginGateway` devuelve `mfaRequired` → `MfaRequiredException`
- **T2.1.7** `fillPreAuthenticated_new_password_required` — devuelve `newPasswordRequired` → `NewPasswordRequiredException`

### T2.2 — `ConsentUsecaseTest`

`net.civeira.phylax.features.oauth.user.application.ConsentUsecaseTest`:

- **T2.2.1** `getPendingConsent_returns_text` — `ConsentGateway.getPendingConsent()` devuelve `Optional.of("terms text")` → `ConsentUsecase` devuelve el texto
- **T2.2.2** `getPendingConsent_empty` — `ConsentGateway` devuelve `Optional.empty()` → `ConsentUsecase` devuelve vacío
- **T2.2.3** `storeAcceptedConsent_delegates` — verifica que `ConsentUsecase.storeAcceptedConsent()` llama a `ConsentGateway.storeAcceptedConsent()` con el tenant y username correctos

### T2.3 — `ChangePasswordUsecaseTest`

`net.civeira.phylax.features.oauth.user.application.ChangePasswordUsecaseTest`:

- **T2.3.1** `forceUpdatePassword_success` — `ChangePasswordGateway.forceUpdatePassword()` devuelve `true` → `ChangePasswordUsecase` devuelve `true`
- **T2.3.2** `forceUpdatePassword_failure` — `ChangePasswordGateway` devuelve `false` → `ChangePasswordUsecase` devuelve `false`
- **T2.3.3** `validateChangeRequest_valid` — `ChangePasswordGateway.validateChangeRequest()` devuelve `Optional.of(username)` → `ChangePasswordUsecase` devuelve el username
- **T2.3.4** `validateChangeRequest_invalid` — devuelve `Optional.empty()` → `ChangePasswordUsecase` devuelve vacío
- **T2.3.5** `allowRecover_delegates` — verifica delegación a `ChangePasswordGateway.allowRecover()`

### T2.4 — `ScopesConsentUsecaseTest`

`net.civeira.phylax.features.oauth.scopes.application.ScopesConsentUsecaseTest`:

- **T2.4.1** `pendingScopes_normalizes_and_deduplicates` — entrada `"openid openid profile"` → resultado `["openid", "profile"]`
- **T2.4.2** `pendingScopes_empty_scope_string` — entrada `""` → resultado `[]`
- **T2.4.3** `storeAcceptedScopes_delegates` — verifica delegación a `ScopesConsentGateway`

---

## Fase 3: Tests de integración — Flujo de login básico

Todos los tests de esta fase son `@QuarkusTest` con un bean `@Alternative LoginGateway` que devuelve `right(data)` para el usuario `alice@example.com` con password `correct-password`.

### T3.1 — `LoginFlowTest.loginForm_isRendered`

```
GET /oauth/openid/test-tenant/auth
  ?client_id=test-client&redirect_uri=http://localhost/callback
  &response_type=code&scope=openid profile&state=test-state
```

- HTTP 200
- Body contiene `<form` y `name="username"`
- Cookie `AUTH_SESSION_ID` limpiada (value null)

### T3.2 — `LoginFlowTest.wrongCredentials_staysOnForm`

```
POST /oauth/openid/test-tenant/auth
  username=alice@example.com, password=wrong, csid=test-csid
```

Con bean `LoginGateway` que devuelve `wrongCredential`.

- HTTP 200 (no redirect)
- Body contiene el formulario de login de nuevo (sin redirect)

### T3.3 — `LoginFlowTest.correctCredentials_redirectsWithCode`

```
POST /oauth/openid/test-tenant/auth
  username=alice@example.com, password=correct-password, csid=test-csid
```

- HTTP 302
- `Location` header contiene `http://localhost/callback?code=...&state=test-state`
- Cookie `AUTH_SESSION_ID` establecida

### T3.4 — `LoginFlowTest.authCode_isOneTimeUse`

- Obtener `code` del paso T3.3
- `POST /oauth/openid/test-tenant/token` con `code` → 200
- Reintentar mismo `code` → 401

### T3.5 — `LoginFlowTest.existingSession_skipLoginForm`

- Completar T3.3 → obtener `AUTH_SESSION_ID`
- `GET /oauth/openid/test-tenant/auth` con la cookie → 200 mostrando formulario de confirmación de sesión (no el login form)
- `POST` con `csid` válido → 302 con nuevo code

---

## Fase 4: Tests de integración — Primera contraseña (newPasswordRequired)

Bean `LoginGateway` configurable:
- Primera llamada a `validateUserData` → `newPasswordRequired`
- Segunda llamada a `fillPreAuthenticated` (después del cambio) → `right(data)`

Bean `ChangePasswordGateway`:
- `forceUpdatePassword(tenant, username, "old-pass", "new-pass")` → `true`

### T4.1 — `NewPasswordFlowTest.newPasswordRequired_showsChangeForm`

```
POST /auth con credenciales de alice → step=new_pass devuelto
```

- HTTP 200
- Body contiene formulario de cambio de contraseña (campo `old_pass`, `new_pass`)
- Cookie `PRE_SESSION_ID` establecida (contiene JWT con username + challenge `FRESH_PASSWORD`)

### T4.2 — `NewPasswordFlowTest.newPass_wrongOldPass_staysOnForm`

Con `ChangePasswordGateway.forceUpdatePassword()` devolviendo `false`:

```
POST /auth step=new_pass, old_pass=wrong, new_pass=newpass123
```

- HTTP 200 (permanece en el formulario de cambio)
- Body contiene mensaje de error

### T4.3 — `NewPasswordFlowTest.newPass_correctData_completesFlow`

```
POST /auth step=new_pass, old_pass=correct-password, new_pass=New@Password1, csid=test-csid
```

- HTTP 302
- `Location` contiene `code=` → flujo completado correctamente
- `ChangePasswordGateway.forceUpdatePassword()` fue llamado con los parámetros correctos

---

## Fase 5: Tests de integración — Aceptación de condiciones (consentRequired)

Bean `LoginGateway`:
- `validateUserData` → `consentRequired`
- `fillPreAuthenticated` (post-consent) → `right(data)`

Bean `ConsentGateway`:
- `getPendingConsent(tenant, username, locale)` → `Optional.of("Texto de los términos de uso...")`
- `storeAcceptedConsent(tenant, username)` → no-op (registrado para verificar llamada)

### T5.1 — `ConsentFlowTest.consentRequired_showsConsentForm`

- POST /auth con credenciales → HTTP 200
- Body contiene `name="consent"` (el checkbox de aceptación)
- Body contiene el texto de los términos

### T5.2 — `ConsentFlowTest.consent_notAccepted_staysOnForm`

```
POST /auth step=consent, consent=off (sin marcar)
```

- HTTP 200 (permanece en el formulario de consentimiento)

### T5.3 — `ConsentFlowTest.consent_accepted_completesFlow`

```
POST /auth step=consent, consent=on, csid=test-csid
```

- HTTP 302 con `code=`
- `ConsentGateway.storeAcceptedConsent()` fue invocado con `(TENANT, USERNAME)`

---

## Fase 6: Tests de integración — MFA requerido

Bean `LoginGateway`:
- `validateUserData` → `mfaRequired`
- `fillPreAuthenticated` (post-MFA) → `right(data)`

Bean `UserMfaGateway`:
- `verifyOtp(tenant, username, "123456")` → `true`
- `verifyOtp(tenant, username, cualquier otro código)` → `false`

### T6.1 — `MfaFlowTest.mfaRequired_showsMfaForm`

- POST /auth con credenciales → HTTP 200
- Body contiene `name="mfa_code"` y `name="step"` con valor `"mfa"`
- Cookie `PRE_SESSION_ID` contiene username + challenge `MFA`

### T6.2 — `MfaFlowTest.mfa_wrongCode_staysOnForm`

```
POST /auth step=mfa, mfa_code=000000, PRE_SESSION_ID cookie
```

- HTTP 200
- Body contiene formulario MFA con mensaje de error

### T6.3 — `MfaFlowTest.mfa_correctCode_completesFlow`

```
POST /auth step=mfa, mfa_code=123456, PRE_SESSION_ID cookie, csid=test-csid
```

- HTTP 302 con `code=`
- `UserMfaGateway.verifyOtp()` llamado con `(TENANT, USERNAME, "123456")`

---

## Fase 7: Tests de integración — Registro de nuevo MFA

Bean `LoginGateway`:
- `validateUserData` → `newMfaRequired`
- `fillPreAuthenticated` (post-MFA-setup) → `right(data)`

Bean `UserMfaGateway`:
- `configurationForNewMfa(tenant, username, locale)` → `PublicLoginMfaBuildResponse` con seed, QR URL, mensaje
- `verifyNewOtp(tenant, username, "123456")` → `true`
- `storeSeed(tenant, username, seed)` → no-op (registrado para verificar llamada)

### T7.1 — `NewMfaFlowTest.newMfaRequired_showsMfaSetupPage`

- POST /auth con credenciales → HTTP 200 (o redirect al selector de MFA)
- Body contiene enlace/botón hacia `/oauth/openid/{tenant}/mfa-setup`

### T7.2 — `NewMfaFlowTest.mfaSetupSelector_isRendered`

```
GET /oauth/openid/test-tenant/mfa-setup con PRE_SESSION_ID
```

- HTTP 200
- Body contiene opciones de tipo MFA (ej. TOTP)

### T7.3 — `NewMfaFlowTest.newMfa_verify_wrongCode_staysOnForm`

- `verifyNewOtp(...)` → `false` para código incorrecto
- HTTP 200 con error

### T7.4 — `NewMfaFlowTest.newMfa_verify_correctCode_completesFlow`

```
POST /auth step=new-mfa-verify, mfa_code=123456, PRE_SESSION_ID, csid=test-csid
```

- HTTP 302 con `code=`
- `UserMfaGateway.storeSeed()` fue llamado
- `UserMfaGateway.verifyNewOtp()` llamado con `(TENANT, USERNAME, "123456")`

---

## Fase 8: Tests de integración — Endpoint de token

### T8.1 — `PasswordGrantTest.passwordGrant_correctCredentials_returnsToken`

```
POST /oauth/openid/test-tenant/token
  grant_type=password
  username=alice@example.com
  password=correct-password
  scope=openid profile
  Authorization: Basic base64(test-client:test-secret)
```

- HTTP 200
- Body es JSON con `access_token`, `refresh_token`, `token_type=Bearer`, `expires_in`
- `access_token` es un JWT válido firmado por `FixedTokenSigner`

### T8.2 — `PasswordGrantTest.passwordGrant_wrongCredentials_returns401`

```
POST /oauth/openid/test-tenant/token
  grant_type=password, password=wrong
```

- HTTP 401

### T8.3 — `PasswordGrantTest.passwordGrant_unknownClient_returns401`

```
POST /oauth/openid/test-tenant/token
  grant_type=password, client_id=unknown-client
```

- HTTP 401

### T8.4 — `RefreshTokenTest.refreshToken_valid_returnsNewToken`

- Primero obtener `refresh_token` vía password grant (T8.1)
- Luego:
  ```
  POST /oauth/openid/test-tenant/token
    grant_type=refresh_token
    refresh_token=<token del paso anterior>
    Authorization: Basic base64(test-client:test-secret)
  ```
- HTTP 200 con nuevo `access_token`

### T8.5 — `RefreshTokenTest.refreshToken_invalid_returns401`

```
POST /oauth/openid/test-tenant/token
  grant_type=refresh_token
  refresh_token=not-a-valid-token
```

- HTTP 401

### T8.6 — `AuthCodeExchangeTest.authCode_exchange_returnsToken`

- Completar flujo HTML (Fase 3, T3.3) → obtener `code`
- POST /oauth/openid/test-tenant/token con `grant_type=authorization_code`, `code=...`, `code_verifier=test-verifier` (si PKCE activo)
- HTTP 200 con `access_token`, `id_token`, `refresh_token`
- `id_token` es un JWT válido con `sub=alice@example.com`, `aud=test-client`

---

## Fase 9: Tests de integración — Scopes en tokens

### T9.1 — `ScopesTest.passwordGrant_requestedScopesPresentInToken`

```
POST /token grant_type=password scope="openid profile"
```

- `access_token` JWT contiene claim `scope=["openid","profile"]`

### T9.2 — `ScopesTest.passwordGrant_scopeFilteredByClientConfig`

Bean `ClientStoreGateway` con `allowedScopes=["openid","profile"]` (sin `*`):

```
POST /token grant_type=password scope="openid profile email"
```

- `access_token` contiene `scope=["openid","profile"]` (email filtrado)

### T9.3 — `ScopesTest.passwordGrant_wildcardClient_allScopesGranted`

Bean `ClientStoreGateway` con `allowedScopes=["*"]`:

```
POST /token grant_type=password scope="openid profile email custom.scope"
```

- `access_token` contiene `scope=["openid","profile","email","custom.scope"]`

### T9.4 — `ScopesTest.refreshToken_preservesScopes`

- Obtener token con `scope="openid profile"` vía password grant
- Hacer refresh
- Nuevo `access_token` contiene los mismos scopes

### T9.5 — `ScopesTest.authCodeFlow_scopesFromRequest`

- Iniciar flujo HTML con `scope=openid profile` en la URL
- Completar login → code
- Intercambiar code → token
- Verificar que `scope` en el JWT coincide

---

## Fase 10: Tests de integración — Cambio de contraseña via /recover

Bean `ChangePasswordGateway`:
- `allowRecover(tenant)` → `true`
- `requestForChange(urlBase, tenant, username)` → no-op
- `validateChangeRequest(tenant, RECOVER_CODE, "newPass1!")` → `Optional.of(USERNAME)`
- `validateChangeRequest(tenant, "bad-code", cualquier_pass)` → `Optional.empty()`

### T10.1 — `RecoverPasswordFlowTest.showRecoverForm_isRendered`

```
GET /oauth/openid/test-tenant/auth POST con step=show-recover
```

- HTTP 200
- Body contiene formulario de solicitud de recuperación

### T10.2 — `RecoverPasswordFlowTest.recoverPage_withCode_isRendered`

```
GET /oauth/openid/test-tenant/recover?username=alice&recovercode=valid-recover-code
```

- HTTP 200
- Body contiene formulario para introducir nueva contraseña

### T10.3 — `RecoverPasswordFlowTest.recover_validCode_changesPassword`

```
POST /oauth/openid/test-tenant/recover?username=alice
  recovercode=valid-recover-code, newPassword=newPass1!
```

- HTTP 302 (redirect al login u otro destino configurado)
- `ChangePasswordGateway.validateChangeRequest()` llamado con los parámetros correctos

### T10.4 — `RecoverPasswordFlowTest.recover_invalidCode_showsError`

```
POST /oauth/openid/test-tenant/recover?username=alice
  recovercode=bad-code, newPassword=newPass1!
```

- HTTP 200 con formulario de error (no redirect)

---

## Fase 11: Tests de integración — Flujo combinado (end-to-end)

Estos tests ejercitan múltiples challenges encadenados para verificar que el estado de la pre-sesión (`PRE_SESSION_ID`) se mantiene correctamente a través de los steps.

### T11.1 — `EndToEndFlowTest.login_then_consent_then_mfa`

- `LoginGateway.validateUserData()` → `consentRequired`
- Tras consentimiento, `LoginGateway.fillPreAuthenticated()` → `mfaRequired`
- Tras MFA, `LoginGateway.fillPreAuthenticated()` → `right(data)`

Pasos:
1. POST /auth con credenciales → 200 (consent form)
2. POST /auth step=consent, consent=on → 200 (mfa form)
3. POST /auth step=mfa, mfa_code=123456, csid=test-csid → 302 con code

Verificar que los challenges se acumulan correctamente en el JWT de `PRE_SESSION_ID`.

### T11.2 — `EndToEndFlowTest.login_then_newPass_then_consent`

- `validateUserData` → `newPasswordRequired`
- `fillPreAuthenticated` (1a vez, post-newPass) → `consentRequired`
- `fillPreAuthenticated` (2a vez, post-consent) → `right(data)`

1. POST /auth → 200 (new_pass form)
2. POST /auth step=new_pass → 200 (consent form)
3. POST /auth step=consent, consent=on, csid=test-csid → 302 con code

---

## Configuración adicional necesaria

### Gestión de beans alternativos por test

Los beans `@Alternative @Priority(1)` de `net.civeira.phylax.testing.oauth.alt.*` se aplican a **todos** los `@QuarkusTest`. Los beans de `scenario/` son programables: en cada test class se inyectan con `@Inject` y se configuran en `@BeforeEach` / `@AfterEach`:

```java
// En OidcIntegrationTestBase o en cada clase de test de integración:
@Inject ScenarioLoginGateway loginGateway;
@Inject ScenarioConsentGateway consentGateway;

@BeforeEach
void reset() {
  loginGateway.reset();      // vuelve al comportamiento por defecto (right(defaultData))
  consentGateway.reset();
}
```

En cada test concreto:
```java
@Test
void mfaRequired_showsMfaForm() {
  loginGateway.whenValidate(() -> AuthenticationResult.mfaRequired(TENANT, USERNAME));
  // ... lanzar petición HTTP y verificar
}
```

Este patrón evita la proliferación de clases separadas por escenario y mantiene el contexto CDI activo durante todo el ciclo de vida del test (Quarkus levanta el servidor una sola vez por clase de test por defecto).

### Archivo de configuración para tests

Crear `src/test/resources/application.properties` (o `application-test.properties`) con:

```properties
# Desactivar datasource real
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:test

# JWT expirations cortas para tests
oauth.jwt.expiration.access-token=PT5M
oauth.jwt.expiration.mfa-token=PT5M

# Desactivar envío de emails
quarkus.mailer.mock=true
```

### Aislar AES/cifrado de contraseñas

`SecureHtmlBuilder.decrypt(value)` usa `TemporalKeysGateway.currentKey()` + `AesCipherService`.
Con `FixedTemporalKeysGateway.encrypt(v)` devolviendo `v` como identidad y `FixedTemporalKeysGateway.currentKey()` devolviendo una clave conocida, se puede precomputar el valor cifrado en los tests O hacer que `AesCipherService` sea también un bean alternativo con modo identidad en tests.

---

## Orden de implementación recomendado

```
Fase 1 (infraestructura) → T2 (unit tests) → T3 (login básico) → T8 (token endpoint)
→ T9 (scopes) → T4 (nueva password) → T5 (consent) → T6 (MFA) → T7 (nuevo MFA)
→ T10 (recover) → T11 (end-to-end combinados)
```

Comenzar por los tests del endpoint `/token` (Fase 8) ya que no requieren el bypass de CSRF/JS y permiten validar rápidamente el JWT builder y los gateways de sesión.

---

## Criterios de éxito

- `mvn test` ejecuta todos los tests sin errores en CI (sin servicios externos)
- Cobertura de los estados `right`, `wrongCredential`, `mfaRequired`, `newMfaRequired`, `newPasswordRequired`, `consentRequired`
- Cada JWT emitido es verificable con la clave pública de `FixedTokenSigner`
- Los flujos multi-step preservan correctamente el `PRE_SESSION_ID` entre peticiones
- Los scopes del token coinciden exactamente con la intersección de scopes solicitados y los `allowedScopes` del cliente
