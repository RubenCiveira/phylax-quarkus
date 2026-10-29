# Capacidades OIDC de phylax-api

> **Vigencia (27-10-2026, tarea 2.10).** Este documento es la foto del código al 20-10-2026 y el origen de las fichas de [docs/TODO-OIDC](README.md); no se actualiza. Desde entonces se han cerrado las fases 0 y 1, casi toda la 2 y buena parte de la 3 y la 4: entre otras cosas, DCR está expuesto, PAR y `client_secret_post` están corregidos, `private_key_jwt` funciona y el discovery solo anuncia lo operativo. Para el estado actual: la documentación funcional y técnica en [src/docs/oidc/es](../../src/docs/oidc/es/README.md), rutas y pruebas en [docs/oauth/overview.md](../oauth/overview.md), evidencia por tarea en `docs/TODO-OIDC/done/`. Sigue sin ejecutarse la OpenID Conformance Suite (V.2).

> **Ubicación (28-10-2026).** Movido desde `src/docs/oidc/` al sustituir aquella carpeta por la documentación en español; los análisis comparativos con lughauth-api (`ANALISIS_*.md`) se retiraron y siguen en el historial de git.

> Fecha: 2026-10-20 · Rama `feat/refactor-oidc` · Alcance: `src/main/java/net/civeira/phylax/features/oauth` (373 ficheros) y su dependencia de `access/trustedclient`.
> Método: lectura del código. Los documentos `ANALISIS_DESARROLLOS_OAUTH*.md` de este directorio (comparativa con lughauth-api, PHP) sirven de referencia, y cada afirmación sobre phylax se ha vuelto a comprobar en el código. No se han ejecutado tests ni la OpenID Conformance Suite.

## 1. Resumen

phylax tiene la **superficie** de un proveedor OIDC completo: Authorization Code con PKCE, `prompt`/`max_age`/step-up de ACR, refresh con rotación, password, MFA, device flow, PAR, introspección, revocación, RP-initiated y back-channel logout, TOTP, WebAuthn, magic link, códigos de recuperación MFA, federación con Google y SAML, consentimiento y RGPD.

En **seguridad y conformidad** hay lagunas que bloquean su uso en producción:

1. **El canje del code no está ligado al cliente.** `/token` no autentica al cliente ni comprueba `client_id` ni `redirect_uri`, y el borrado del code no es atómico.
2. **Los clientes no tienen configuración propia.** Todos reciben los mismos grants, scope `*` y la `audience` que pidan. Además son globales, no de un tenant.
3. **El ID token no sigue OIDC Core.** No lleva `sid` ni `amr` correctos, `acr` es un número, el `aud` admite valores arbitrarios, caduca a la vez que el refresh y se emite aunque no se pida `openid`.
4. **El discovery anuncia lo que no existe:** DCR (403), CIBA (403), JAR, `claims`, JWE, `pairwise`, `private_key_jwt`, mTLS, PAR obligatorio e iframe de sesión.

Leyenda: ✅ implementado · ⚠️ parcial o con defectos · ❌ ausente · 📣 solo anunciado en discovery.

## 2. Correcciones a los análisis previos

Los documentos comparativos contienen afirmaciones sobre phylax que el código no confirma:

| Afirmación previa | Comprobación |
|---|---|
| «phylax publica la clave privada en `kid`» (`signKeypass`) | **El riesgo existe, pero hoy no se materializa.** [JoseTokenSigner.java:151](../../src/main/java/net/civeira/phylax/features/oauth/tokensecurity/infrastructure/driven/JoseTokenSigner.java#L151) llama a `withKeyId(privateKey)`, pero `JWTCreator.sign()` de java-jwt 4.3.0 sobrescribe el `kid` con `RSAKeyProvider.getPrivateKeyId()`, es decir, con el id público de la clave. La línea es engañosa y peligrosa ante cualquier cambio de librería o de provider: hay que eliminarla. |
| «phylax toma las audiencias del refresh token» / «audiencias controladas ✅» | Solo en el refresh. En `/authorize`, password y device, el parámetro `audience` se acepta sin validar ([AuthRequest.java:136](../../src/main/java/net/civeira/phylax/features/oauth/authentication/domain/AuthRequest.java#L136)) y además calcula roles para esa audiencia. |
| «DCR ⚠️ (acepta cualquier `initial_access_token`)» | **DCR no está expuesto.** Existen los casos de uso (`RegisterClientUsecase`, `Read/Update/DeleteDynamicClientUsecase`), pero el único endpoint, `POST /connect`, devuelve 403 ([ClientRegisterController.java](../../src/main/java/net/civeira/phylax/features/oauth/client/infrastructure/driver/rest/ClientRegisterController.java)). |
| «Refresh vinculado al cliente ✅» | ⚠️ El cliente sale del `azp` del propio refresh y **no se autentica** ([TokenController.java:182-211](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/rest/TokenController.java#L182-L211)). Un refresh robado de un cliente confidencial se puede canjear sin el secreto. |
| «ID token con `sid` y `amr` ✅» | ❌ El ID token no lleva `sid`. `session_state` es un UUID aleatorio sin relación con la sesión, y `amr` solo aparece en el access token con el mismo valor que `acr`. |
| «`client_secret_post` ✅» | ❌ en `/token`: el secreto solo se lee de `Authorization: Basic`. Con `client_id` en el cuerpo, `loadPrivate(…, null)` provoca un `NullPointerException` (500). En introspección, revocación y PAR sí se admite. |

## 3. Matriz de capacidades

### 3.1 Endpoints

Base: `/oauth/openid/{tenant}/…`

| Endpoint | Estado | Observaciones |
|---|:-:|---|
| `GET /.well-known/openid-configuration`, `/configuration` | ⚠️ | Construido por *contributors* por módulo (buen diseño), pero sobreanuncia (§4) |
| `GET /jwks` | ✅ | Claves RSA-4096 por tenant con `kid`; rotación cada 7 días y 3 claves futuras pre-generadas |
| `GET/POST /auth`, `/authorize` | ⚠️ | Ver §3.2 |
| `POST /token` | ⚠️ | Ver §3.3 |
| `GET /userinfo` | ⚠️ | Solo GET; ver §3.2 |
| `POST /introspect`, `/introspection` | ⚠️ | Exige cliente confidencial marcado como resource server; solo reconoce tokens ligados a una sesión interactiva |
| `POST /revocation` | ⚠️ | **Ruta duplicada**: `AuthorizeHtml.revoke` (borra la pre-sesión por cookie) y `TokenRevocationController` (RFC 7009) declaran el mismo `POST`. Hoy se diferencian por `@Consumes`. `/revoke` es el alias de la pre-sesión, no de RFC 7009 |
| `GET/POST /logout` | ⚠️ | Ver §3.5 |
| `POST /par-request`, `/par` | ⚠️ | Ver §3.4 |
| `POST /device`, `GET/POST /device/verify` | ✅ | RFC 8628 con `verification_uri_complete` |
| `POST /connect` (DCR) | 📣 | Devuelve 403 |
| `POST /ciba-auth` | 📣 | Devuelve 403 |
| `GET /login-status-iframe`, `/check-session` | 📣 | Devuelve `<h1>Check</h1>` |
| `/register`, `/recover`, `/mfa-setup` | ✅ | Registro, recuperación de contraseña y alta de MFA |
| `/delegated/*`, `/delegated-auth` | ✅ | Federación (§3.6) |
| `/webauthn/{register,authenticate}/{begin,finish}` | ✅ | Passkeys |
| `/magic-link/request`, `/magic-link/verify` | ✅ | |
| `/me/*`, `/profile` | ✅ | Perfil, contraseña, MFA, sesiones, códigos de recuperación, consentimientos, passkeys, exportación y borrado de cuenta |
| `/api/{tenant}/access/users/{uid}/sessions` | ✅ | Administración de sesiones de usuario |
| `/oauth/api-key` | ✅ | API keys de cliente |

### 3.2 OIDC Core: autorización, ID token y UserInfo

| Capacidad | Estado | Evidencia | Qué falta |
|---|:-:|---|---|
| Authorization Code + PKCE | ⚠️ | `/authorize` solo admite clientes `publicAllow`, y para ellos PKCE es obligatorio ([ClientRetrieveAdapter.java:96](../../src/main/java/net/civeira/phylax/features/oauth/client/infrastructure/driven/ClientRetrieveAdapter.java#L96)) | Admitir clientes confidenciales en el flujo code; ligar el canje al cliente (§3.3) |
| PKCE solo `S256` | ✅ | `plain` rechazado en authorize ([OidcResponseBuilder.java:73](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/OidcResponseBuilder.java#L73)) y en `/token` | — |
| Validación de `redirect_uri` | ⚠️ | Coincidencia exacta, pero se aceptan `*` y prefijos `…/*` ([ClientRetrieveAdapter.java:114](../../src/main/java/net/civeira/phylax/features/oauth/client/infrastructure/driven/ClientRetrieveAdapter.java#L114)) | Solo coincidencia exacta (comodín de puerto únicamente en loopback) |
| `response_type` validado contra el cliente | ❌ | `responseTypesJson` existe en `TrustedClient` y no se usa | Validarlo y rechazar con `unsupported_response_type` |
| `state` | ⚠️ | Obligatorio (`orElseThrow` → página de error), concatenado sin `URLEncoder` ([OidcResponseBuilder.java:97](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/OidcResponseBuilder.java#L97)) | Opcional según la especificación; codificarlo |
| `nonce` | ⚠️ | Se copia al ID token; si falta, se emite `nonce: ""` ([JwtTokenBuilder.java:147](../../src/main/java/net/civeira/phylax/features/oauth/tokensecurity/application/JwtTokenBuilder.java#L147)) | Omitir el claim cuando no se envía; exigirlo en implicit/hybrid |
| `prompt=none` | ✅ | `login_required` / `interaction_required` | — |
| `prompt=login`, `select_account` | ✅ | `FORCE_REAUTH_PROMPTS` ([AuthorizeHtml.java:91](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/AuthorizeHtml.java#L91)) | `select_account` solo fuerza el login; no hay selector de cuentas |
| `prompt=consent` | ❌ | Anunciado, no tratado | Forzar la pantalla de consentimiento de scopes |
| `max_age` + TTL de SSO por tenant | ✅ | `resolveSession` ([AuthorizeHtml.java:449-468](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/AuthorizeHtml.java#L449-L468)) | — |
| `acr_values` / step-up a MFA | ⚠️ | Niveles numéricos `0/1/2` | Valores URI o documentados; devolver el `acr` alcanzado |
| `login_hint` | ⚠️ | Se parsea | Prerrellenar el usuario |
| `id_token_hint` en `/authorize` | ❌ | Solo se usa en logout | Validar el hint y `prompt=none` con él |
| `ui_locales`, `claims_locales` | ❌ | El idioma sale de `Accept-Language` | — |
| Parámetro `claims` | 📣 | `claimsParameterSupported(true)` sin implementación | Implementar o retirar del discovery |
| Request object (JAR, `request` / `request_uri` externo) | 📣 | No se procesan | Implementar (RFC 9101) o retirar |
| `iss` en la respuesta (RFC 9207) | ⚠️ | Solo en el fragmento implicit/hybrid; no en `code` ni en errores | Añadirlo en todas las respuestas |
| Errores de autorización | ❌ | Siempre `#error=` en el fragmento, sin `state`, `iss` ni `error_description` ([OidcResponseBuilder.java:135](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/OidcResponseBuilder.java#L135)) | Query para `code`, fragmento para implicit/hybrid, con `state` |
| Implicit / hybrid | ⚠️ | Todo va en el fragmento y `c_hash` se calcula, pero siempre se devuelven `id_token` y `access_token`, sea cual sea el `response_type` | Emitir solo lo pedido; o retirar implicit (OAuth 2.1) |
| `response_mode=form_post` | ❌ | — | Implementar (lo usan muchos RP) |
| ID token: `iss`, `sub`, `aud`, `exp`, `iat`, `auth_time`, `at_hash`, `c_hash` | ✅ | [ClaimsMapper.java:88-130](../../src/main/java/net/civeira/phylax/features/oauth/tokensecurity/application/ClaimsMapper.java#L88-L130) | — |
| ID token: `aud` | ⚠️ | `client_id` más cualquier `audience` pedida | `aud` = solo el cliente (más `azp`) |
| ID token: `acr` / `amr` | ⚠️ | `acr` = `"0"/"1"/"2"`; `amr` no está en el ID token | `amr` como array (`pwd`, `otp`, `hwk`, `fed`…) |
| ID token: `sid` | ❌ | No se emite; `session_state` es un UUID aleatorio | Emitir el id real de sesión (necesario para logout) |
| ID token: caducidad | ⚠️ | Firmado con la caducidad del refresh ([JwtTokenBuilder.java:73](../../src/main/java/net/civeira/phylax/features/oauth/tokensecurity/application/JwtTokenBuilder.java#L73)) | TTL propio y corto |
| ID token solo con scope `openid` | ❌ | Se emite en toda llamada con `AuthRequest`, también en password | Emitirlo solo si se solicita `openid` |
| ID token con claims de perfil | ❌ | Lleva `roles` y `groups`, pero no `email` ni `name` | Claims por scope en el ID token cuando no se pide access token (`response_type=id_token`) |
| UserInfo por scope | ⚠️ | `profile`, `phone`; `email` = **el username** ([InformationController.java:115](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/rest/InformationController.java#L115)) | Email real, `email_verified`, `address` |
| UserInfo por POST | ❌ | Solo `@GET` | OIDC Core §5.3.1 |
| UserInfo con tokens no interactivos | ❌ | Se valida por introspección, que solo conoce tokens ligados a una sesión de `/authorize` | Validar la firma, el tipo y la revocación sin depender de la sesión |
| UserInfo firmado / cifrado | 📣 | Responde JSON plano | Retirar del discovery |

### 3.3 Grants y ciclo de vida de los tokens

| Capacidad | Estado | Evidencia | Qué falta |
|---|:-:|---|---|
| `authorization_code`: autenticación del cliente | ❌ | El canje solo exige `code` + `code_verifier` ([TokenController.java:154-181](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/rest/TokenController.java#L154-L181)) | Autenticar a los confidenciales; exigir el `client_id` del code |
| `authorization_code`: `redirect_uri` igual al de authorize | ❌ | No se compara | RFC 6749 §4.1.3 |
| Code de un solo uso | ⚠️ | `SELECT` y después `DELETE` en sentencias separadas ([TemporalKeysSqlAdapter.java:169-181](../../src/main/java/net/civeira/phylax/features/oauth/session/infrastructure/driven/TemporalKeysSqlAdapter.java#L169-L181)) | `DELETE … RETURNING` o bloqueo; si se reutiliza, revocar los tokens emitidos |
| `refresh_token`: rotación + detección de reutilización | ⚠️ | Rota y revoca el JTI anterior, y ante una reutilización llama a `revokeAllForUser`. Pero revoca **después** de emitir, sin atomicidad (dos refrescos concurrentes salen bien) | Revocación atómica previa a la emisión |
| `refresh_token`: autenticación del cliente | ❌ | El cliente se toma del `azp` sin secreto | Exigir el secreto a los confidenciales |
| `refresh_token`: comprobar que la sesión sigue viva | ❌ | Solo se mira la revocación del JTI | Invalidar el refresh al cerrar o expirar la sesión |
| `refresh_token`: scopes | ❌ | El `scope` del cuerpo se concede sin acotarlo a los originales (scope `*`) | `scope` ⊆ scopes concedidos originalmente |
| `password` (ROPC) | ⚠️ | Habilitado para **todos** los clientes (`DEFAULT_GRANTERS`); el `AuthRequest` se construye con la query, no con el cuerpo | Restringir por cliente o eliminar (OAuth 2.1) |
| `client_credentials` | ⚠️ | Solo con Basic; TTL M2M por cliente | `client_secret_post`; scopes M2M por cliente (`allowedScopesM2m` existe y no se usa) |
| `mfa` (`mfa_token` + OTP) | ✅ | `MfaGranter` | — |
| `device_code` | ✅ | `DeviceCodeGranter` | — |
| `delegated` | ❌ | `DelegatedAccessGranter` existe, pero `"delegated"` no está en `DEFAULT_GRANTERS`: es inalcanzable | Decidir si se habilita o se borra |
| `RefreshGranter` | ❌ | Código muerto: el refresh se resuelve antes, en el controller | Unificar o borrar |
| Token exchange (RFC 8693), JWT bearer (RFC 7523) | ❌ | — | Fase 3 |
| Respuestas de error OAuth en `/token` | ❌ | Casi todo es `401` sin cuerpo; faltan `invalid_grant`, `invalid_client` y `WWW-Authenticate` | Errores RFC 6749 §5.2 |
| `Cache-Control: no-store` en `/token` | ❌ | — | Añadirlo |
| Access token: `typ` | ⚠️ | `typ` va como **claim** (`Bearer`, `ID`), no en la cabecera | `typ: at+jwt` en la cabecera (RFC 9068) |
| Separación por tipo de token | ⚠️ | Por el claim `scope` (`refresh`, `mfa`, `challenge`); el principal de los resource servers no la comprueba | Rechazar como bearer los ID/refresh/mfa/challenge |
| Revocación aplicada a bearer en APIs | ❌ | [TokenJwtCallerPrincipalFactory.java:207](../../src/main/java/net/civeira/phylax/features/oauth/tokensecurity/infrastructure/driver/TokenJwtCallerPrincipalFactory.java#L207) solo verifica la firma, sin issuer ni lista de revocados | Consultar la revocación o acortar el TTL y documentarlo |
| Firma | ⚠️ | Solo RS256; RSA-4096 generada de forma síncrona dentro de la petición de firma o de JWKS ([JoseTokenSigner.java:416](../../src/main/java/net/civeira/phylax/features/oauth/tokensecurity/infrastructure/driven/JoseTokenSigner.java#L416)) | Generación en job; ES256/EdDSA |
| Introspección (RFC 7662) | ⚠️ | Solo tokens ligados a una sesión interactiva; los de password o client_credentials salen `active: false` | Introspección por firma + lista de revocados |
| Revocación (RFC 7009) | ✅ | Autenticada, con `token_type_hint` | Resolver la ruta duplicada |

### 3.4 Clientes y solicitudes avanzadas

| Capacidad | Estado | Evidencia | Qué falta |
|---|:-:|---|---|
| Clientes por tenant | ❌ | `TrustedClientFilter.code(clientId)` sin tenant: un cliente vale en cualquier tenant habilitado ([ClientRetrieveAdapter.java:63](../../src/main/java/net/civeira/phylax/features/oauth/client/infrastructure/driven/ClientRetrieveAdapter.java#L63)) | Asociar el cliente al tenant o documentarlo como global y filtrar |
| Configuración por cliente | ❌ | Grants = `DEFAULT_GRANTERS` y scopes = `*` para todos ([ClientRetrieveAdapter.java:28-30](../../src/main/java/net/civeira/phylax/features/oauth/client/infrastructure/driven/ClientRetrieveAdapter.java#L28-L30)); se ignoran `grantTypesJson`, `responseTypesJson`, `tokenEndpointAuthMethod`, `allowAllScopes` y `allowedScopesM2m` | Leer y aplicar los atributos que ya existen en `TrustedClient` |
| `post_logout_redirect_uris` propias | ❌ | Se reutilizan los `allowedRedirects` ([ClientRetrieveAdapter.java:144](../../src/main/java/net/civeira/phylax/features/oauth/client/infrastructure/driven/ClientRetrieveAdapter.java#L144)) | Lista separada |
| `client_secret_basic` | ✅ | | Comparación en tiempo constante; hash en vez de cifrado reversible ([ClientRetrieveAdapter.java:127](../../src/main/java/net/civeira/phylax/features/oauth/client/infrastructure/driven/ClientRetrieveAdapter.java#L127)) |
| `client_secret_post` | ⚠️ | Sí en introspect, revocation y PAR; no en `/token` (500) | Unificar la autenticación del cliente en un único servicio |
| `private_key_jwt`, `client_secret_jwt`, `tls_client_auth` | 📣 | Anunciados en introspección y revocación | Fase 3 o retirar |
| DCR (RFC 7591/7592) | 📣 | Casos de uso sin endpoint; `/connect` → 403 | Exponerlos con `initial_access_token` validado y grants restringidos |
| PAR (RFC 9126) | ⚠️ | Los parámetros del PAR prevalecen ✅ ([AuthorizeHtml.java:638-649](../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/AuthorizeHtml.java#L638-L649)). Pero solo admite clientes confidenciales, **guarda `client_secret` entre los parámetros**, no comprueba que el `client_id` del cuerpo sea el autenticado, valida el `redirect_uri` en authorize y no en el push, y no es obligatorio aunque se anuncie `require_pushed_authorization_requests: true` | Corregir lo anterior; PAR obligatorio configurable por cliente |
| JARM, RAR (RFC 9396), resource indicators (RFC 8707), DPoP (RFC 9449), mTLS (RFC 8705) | ❌ / 📣 | mTLS y JARM anunciados | Fases 3–4 |
| CIBA | 📣 | 403 | Retirar del discovery |

### 3.5 Sesión y logout

| Capacidad | Estado | Evidencia | Qué falta |
|---|:-:|---|---|
| Sesión SSO con cookie y TTL por tenant | ✅ | `OidcCookieManager`, `TenantConfig.sessionSsoTtlSeconds` | — |
| RP-initiated logout | ⚠️ | GET y POST, `id_token_hint`, `post_logout_redirect_uri` validado y `state`. Pero en POST los parámetros se leen de la **query**, no del formulario; `state` va sin codificar; y **no pide confirmación** sin `id_token_hint` (CSRF de logout) | Leer el formulario; pantalla de confirmación; `logout_hint` |
| Back-channel logout | ⚠️ | Asíncrono ✅. Pero notifica a **todos** los clientes con URL de back-channel, de todos los tenants ([BackChannelLogoutDispatcher.java:60](../../src/main/java/net/civeira/phylax/features/oauth/authentication/application/BackChannelLogoutDispatcher.java#L60)), y el `sid` enviado no coincide con nada del ID token | Notificar solo a los clientes de la sesión (guardar los `client_id` por sesión); `sid` coherente con el ID token; `typ: logout+jwt` |
| Front-channel logout | ❌ | Desactivado en el discovery (es honesto) | Opcional |
| Session Management (iframe `check_session`) | 📣 | Stub | Retirar del discovery o implementar |
| Revocación de tokens al cerrar sesión | ✅ | `revokeAllForUser` por usuario y cliente | Revocar también los de otros clientes de la misma sesión |
| Administración de sesiones (admin y autoservicio) | ✅ | `UserSessionController`, `/me/sessions` | — |
| Limpieza programada | ✅ | `OAuthCleanupScheduler`, crons de codes y sesiones | — |

### 3.6 Autenticación de usuario, consentimiento y RGPD

| Capacidad | Estado | Observaciones |
|---|:-:|---|
| Login, registro y recuperación de contraseña | ✅ | `LoginStep`, `RegistrationStep`, `RecoverStep`, `NewPassStep` |
| MFA TOTP (alta y verificación) | ✅ | `OtpMfaService`, `NewMfaStep`, `MfaStep` |
| Códigos de recuperación MFA (generación y **uso en el login**) | ✅ | `MfaRecoveryService`, `RecoverMfaStep` |
| WebAuthn / passkeys | ✅ | Registro, autenticación y gestión en el perfil |
| Magic link | ✅ | `MagicLinkStep` + endpoints |
| Federación OIDC/OAuth | ⚠️ | Solo Google (`GoogleDelegatedAccessProvider`). Faltan Microsoft, Apple, GitHub y un **proveedor OIDC genérico** (discovery + JWKS) |
| Federación SAML 2 | ✅ | `SamlDelegatedAccessProvider` |
| Invitaciones de usuario | ⚠️ | Casos de uso (`create`, `accept`, `cancel`, `resend`) sin ningún endpoint que los use |
| Consentimiento de scopes por cliente | ✅ | `ScopeConsentStep`; revocable desde `/me/consents` |
| Términos de uso / consentimiento RGPD | ✅ | `ConsentStep`, `TermsOfUseConsentUsecase`, `GdprConsentUsecase` |
| Exportación y borrado de datos | ✅ | `/me/data-export`, `/me/delete-account` |
| Rate limiting / bloqueo por intentos | ? | No verificado en el módulo `oauth` |
| Temas por tenant | ✅ | Módulo `theme` |

### 3.7 Calidad del módulo

| Aspecto | Valoración |
|---|---|
| Tests | 53 ficheros bajo `src/test/.../oauth`, en su mayoría flujos end-to-end. **Ninguno** cubre los casos negativos de §3.3 (code ajeno, doble canje, refresh robado o concurrente, audiencia ajena) |
| Emisión de tokens | Bien separada (`ClaimsMapper`, `JwtTokenBuilder`, `TokenVerifier`), pero `TokenController` resuelve el code y el refresh en línea y duplica la lógica de los granters |
| Autenticación de cliente | Se repite en 5 controllers (token, device, PAR, introspect, revocation), cada uno con reglas distintas |
| Discovery | Patrón *contributor* correcto; los valores anunciados no salen de lo implementado |

## 4. Discovery: anunciado frente a implementado

| Anunciado | Contributor | Realidad | Acción |
|---|---|---|---|
| `registration_endpoint: …/connect` | `ClientRegistrationContributor` | 403 | Retirar hasta exponer DCR |
| `backchannel_authentication_endpoint` (CIBA) y modos `poll`/`ping` | `AuthorizationEndpointContributor` | 403 | Retirar |
| `check_session_iframe` | `SessionContributor` | Stub | Retirar |
| `require_pushed_authorization_requests: true` | `ParContributor` | No se exige | `false` o hacerlo configurable |
| `request_parameter_supported`, `request_uri_parameter_supported`, `request_object_*` | `AuthorizationEndpointContributor` | JAR no implementado | Retirar |
| `claims_parameter_supported: true` | ídem | No se procesa | Retirar |
| `subject_types_supported: pairwise` | ídem | Solo `public` | Retirar `pairwise` |
| `authorization_*` (JARM), `id_token_encryption_*`, `userinfo_encryption_*`, `userinfo_signing_alg_values_supported` | ídem / `TokenSecurityContributor` | Sin JWE ni JARM; UserInfo sin firmar | Retirar |
| `prompt_values_supported: consent` | ídem | No tratado | Implementar o retirar |
| `grant_types_supported` sin `delegated`, con `password` y `mfa` | ídem | Habilitados para todos los clientes | Derivarlos de la configuración |
| `response_types_supported` (8 tipos) | ídem | Implicit/hybrid defectuosos | Limitarlo a `code` hasta corregirlo |
| `token_endpoint_auth_methods_supported` | — | No se publica | Publicar `client_secret_basic` (y `post` cuando funcione) |
| `introspection/revocation_endpoint_auth_methods_supported`: `private_key_jwt`, `client_secret_jwt`, `tls_client_auth` | `TokenSecurityContributor` | Solo secreto | Dejar `client_secret_basic` y `client_secret_post` |
| `tls_client_certificate_bound_access_tokens: true` y `mtls_endpoint_aliases` | `TokenSecurityContributor`, `MtlsDiscoveryContributor` | Sin mTLS; el alias `…/registration` ni siquiera existe | Retirar |
| `claims_supported` | `AuthorizationEndpointContributor` | No incluye `sid`, `amr`, `email_verified`, `roles`, `groups`, `tid`… | Generarlo a partir de `ClaimsMapper` |
| `acr_values_supported: 0, 1, 2` | ídem | Coherente con el código | Documentar el significado de cada nivel |

## 5. Qué hay que implementar

Esfuerzo: S ≤ 1 día · M 2–4 días · L ≥ 1 semana.

Comparación con `lughauth-api/docs/Caracteriscitas_ODIC.md` (corte PHP del 20-10-2026): las filas «PHP actual» son contratos de referencia, no pruebas de conformidad ni evidencia de que Java los cumpla. Las fichas PHP en `TODD/done/` no son pendientes de Java; las extensiones marcadas «opcionales» dependen de una decisión de producto. Las tareas siguientes recogen las diferencias observables y los criterios que faltaban en la planificación Java.

### Fase 0 — Seguridad (bloqueante)

| # | Tarea | Esfuerzo |
|---|---|:-:|
| 0.1 | `/token` + `authorization_code`: autenticar al cliente confidencial, exigir el `client_id` y el `redirect_uri` del code, canje atómico y revocación en cascada si se reutiliza | M |
| 0.2 | `/token` + `refresh_token`: autenticar al cliente, comprobar que la sesión está viva, `scope` ⊆ original y revocación atómica antes de emitir | M |
| 0.3 | Validar `audience` contra una lista del cliente o del tenant (authorize, password, device) | S |
| 0.4 | `ClientRetrieveAdapter`: aplicar `grantTypesJson`, `responseTypesJson`, scopes por cliente y `tokenEndpointAuthMethod`; quitar `password` de los grants por defecto | M |
| 0.5 | Decidir el modelo de cliente por tenant y filtrar en la carga de clientes y en el back-channel | M–L |
| 0.6 | Back-channel: notificar solo a los clientes que participaron en la sesión | M |
| 0.7 | Eliminar `withKeyId(privateKey)` de `signKeypass` y basar la verificación de la pre-sesión en el claim `scope=challenge` | S |
| 0.8 | Bearer en las APIs: validar `iss`, el tipo de token (no aceptar refresh/ID/mfa/challenge) y la revocación | S–M |
| 0.9 | Servicio único de autenticación de cliente (Basic + post) para token, device, PAR, introspect y revocation; comparación en tiempo constante | M |
| 0.10 | `redirect_uri`: quitar `*` y los prefijos `/*` (o limitarlos a loopback en desarrollo) | S |
| 0.11 | Codificar `state` en las redirecciones de authorize y logout; no guardar `client_secret` en los parámetros del PAR | S |
| 0.12 | Confirmación de logout sin `id_token_hint`; leer el formulario en `POST /logout` | S |
| 0.13 | Resolver la ruta `POST /revocation` duplicada (renombrar la de la pre-sesión) | S |
| 0.14 | Endurecer PAR: exigir que el `client_id` de la solicitud coincida con el autenticado y consumir cada `request_uri` una sola vez; probar conflictos y reutilización (complementa 0.11 y 2.3) | M |
| 0.15 | En bearer y UserInfo comprobar `aud` y tenant además de `iss`/tipo, y rechazar JTI revocados incluso si hay caché positiva (amplía 0.8) | M |

### Fase 1 — Conformidad OIDC Core y discovery honesto

| # | Tarea | Esfuerzo |
|---|---|:-:|
| 1.1 | Depurar los *contributors* según §4 para que solo anuncien lo implementado | S |
| 1.2 | ID token: `sid` correlacionado con la sesión (véase 1.14), `amr` como array, `aud` = cliente, TTL propio, sin `nonce` vacío, emitido solo con `openid` | M |
| 1.3 | Errores de autorización en el `response_mode` correcto con `state`, `iss` y `error_description`; `iss` también en la respuesta `code` | S |
| 1.4 | Errores OAuth estándar y `Cache-Control: no-store` en `/token` | S |
| 1.5 | UserInfo por POST, email real con `email_verified`, `address`, y validación sin depender de la sesión | S–M |
| 1.6 | `prompt=consent` | S |
| 1.7 | Implicit/hybrid: emitir solo los artefactos pedidos o retirarlos | S–M |
| 1.8 | Admitir clientes confidenciales en el flujo code (web apps con secreto) | M |
| 1.9 | Introspección por firma + revocación, válida para todos los grants | M |
| 1.10 | Tests negativos de seguridad (code ajeno, doble canje, refresh robado o concurrente, audiencia ajena, redirect no registrado) | M |
| 1.11 | PKCE: conservar solo `S256`, exigirlo a públicos y definir la política para confidenciales; probar challenge ausente, método rechazado y verifier incorrecto | S |
| 1.12 | `nonce`: ligarlo al code original y exigirlo donde corresponda (implicit/hybrid si se mantienen); probar omisión y discordancia sin emitir `nonce: ""` | S |
| 1.13 | Separar claims por tipo de token: ID token con `auth_time`, `acr` y `amr` reales, hashes solo cuando procedan; access token sin claims exclusivos del ID token; comprobar `sub` de UserInfo = `sub` del ID token | M |
| 1.14 | Logout: validar `id_token_hint` contra issuer, RP y sesión; usar `sid` público estable en ID token y logout token, separado del identificador de cookie; probar notificaciones y revocación entre tenants | M |

### Fase 2 — Completar lo que ya está a medias

| # | Tarea | Esfuerzo |
|---|---|:-:|
| 2.1 | Exponer DCR (RFC 7591/7592) sobre los casos de uso existentes, con `initial_access_token` validado | M |
| 2.2 | Exponer los endpoints de invitaciones | S–M |
| 2.3 | PAR: aceptar clientes públicos, validar en el push y permitir exigirlo por cliente | S |
| 2.4 | `post_logout_redirect_uris` propios del cliente | S |
| 2.5 | Habilitar o eliminar el grant `delegated`; eliminar `RefreshGranter` o mover a él la lógica de refresh | S |
| 2.6 | Generación de claves en un job, fuera de la ruta de firma | S |
| 2.7 | `login_hint` y `id_token_hint` en `/authorize` | S |
| 2.8 | Revisar almacenamiento/comparación del secreto de cliente (hash no reversible y comparación temporalmente segura), evitando su exposición en DCR después del alta; revisar también la protección de contraseñas de usuario | M |

### Fase 3 — Extensiones de un IdP moderno

| # | Tarea | Especificación |
|---|---|---|
| 3.1 | `response_mode=form_post` | OAuth 2.0 Form Post |
| 3.2 | `private_key_jwt` / `client_secret_jwt` (aprovechando `jwksUri` / `jwksJson` de `TrustedClient`) | RFC 7523, OIDC Core §9 |
| 3.3 | Request objects firmados (JAR); en lughauth hay una implementación de referencia | RFC 9101 |
| 3.4 | Proveedor federado OIDC genérico, más Microsoft, Apple y GitHub | — |
| 3.5 | Access token `at+jwt` | RFC 9068 |
| 3.6 | DPoP | RFC 9449 |
| 3.7 | ES256 / EdDSA | RFC 7518 / 8037 |
| 3.8 | Token exchange | RFC 8693 |
| 3.9 | Grants JWT bearer y política de confianza/anti-replay, solo si hay caso de uso | RFC 7523 |
| 3.10 | Hints, ACR y localización: `select_account` real, `acr_values` documentados y `ui_locales`/`claims_locales` si se ofrecen | OIDC Core §3.1.2.1 |

### Fase 4 — Perfil alto (FAPI 2.0), solo si hay requisito

mTLS y tokens ligados a certificado (RFC 8705), JARM, CIBA, `pairwise`, ID token y UserInfo cifrados (JWE), RAR (RFC 9396) y resource indicators (RFC 8707).

### Validación

Al terminar las fases 0 y 1, pasar la **OpenID Conformance Suite** (perfiles *Basic OP*, *Config OP*, *RP-Initiated Logout* y *Back-Channel Logout*) e integrarla en el CI.

Preparar fixtures semánticos compartidos con PHP (dos tenants, cliente público y confidencial, redirects válidos e inválidos, sesión, claves y reloj) y pruebas HTTP + persistencia para: canje único y concurrente del code, replay concurrente del refresh, aislamiento entre tenants, política de grants, polling/expiración/denegación de device, revocación en bearer/UserInfo, PAR de un solo uso y logout limitado a los clientes participantes. Contrastarlos con la matriz PHP sin asumir que sus rutas o contratos parciales ya son conformes. Si se expone DCR, añadir el perfil *Dynamic OP* de la suite y probar credenciales de registro caducadas o agotadas, metadatos inválidos y aislamiento por tenant.
