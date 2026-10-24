# Pendientes OIDC — phylax-api (Java)

Fuentes: [CAPACIDADES_OIDC.md](../../src/docs/oidc/CAPACIDADES_OIDC.md), §5 (corte 2026-10-20), y el plan PHP `lughauth-api/docs/TODD/` ([índice de épicas](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/epicas/README.md)). Los IDs originales coinciden con el análisis Java; **0.16, 1.15, 1.16, 2.9, 2.10, 3.11 y 4.8–4.10** amplían el plan a partir de las fichas PHP. Una tarea PHP en `done/` solo indica el estado PHP: aquí se decide por el código Java. Cada ID tiene una ficha independiente `issue-<id>-<título>.md`; las épicas usan `ep-J-<id>-<título>.md`. Marcar una casilla solo tras verificarla con pruebas observables. Las fichas implementadas se mueven a [`done/`](done/) (hoy: 0.1, 0.2 y 0.16, a falta de su verificación final; ver cada ficha) y hay que actualizar sus enlaces; las épicas pasan a `epicas/done/` cuando se cierran todas sus fichas. Esfuerzo orientativo: S ≤ 1 día, M 2–4 días, L ≥ 1 semana. Fases 3 y 4: extensiones condicionadas a necesidad. Véase el [plan de épicas Java](epicas/README.md) para dependencias y orden.

**Fichas por fase** (la tabla siguiente conserva la vista de conjunto):

- Seguridad: [0.1](done/issue-0.1-canje-seguro-de-authorization-code.md) · [0.2](done/issue-0.2-refresh-cliente-sesion.md) · [0.3](issue-0.3-audiencias-autorizadas.md) · [0.4](issue-0.4-configuracion-por-cliente.md) · [0.5](issue-0.5-aislamiento-clientes-tenant.md) · [0.6](issue-0.6-logout-clientes-participantes.md) · [0.7](issue-0.7-clave-publica-y-presesion.md) · [0.8](issue-0.8-verificacion-bearer-apis.md) · [0.9](issue-0.9-autenticacion-cliente-unificada.md) · [0.10](issue-0.10-redirect-uri-estricta.md) · [0.11](issue-0.11-state-seguro-secreto-fuera-par.md) · [0.12](issue-0.12-logout-post-confirmacion.md) · [0.13](issue-0.13-ruta-revocacion-no-duplicada.md) · [0.14](issue-0.14-par-vinculado-un-solo-uso.md) · [0.15](issue-0.15-audiencia-tenant-revocacion-bearer.md) · [0.16](done/issue-0.16-seguridad-login-federado.md).
- OIDC Core: [1.1](issue-1.1-discovery-honesto.md) · [1.2](issue-1.2-id-token-conforme.md) · [1.3](issue-1.3-respuestas-autorizacion-errores.md) · [1.4](issue-1.4-errores-cache-token.md) · [1.5](issue-1.5-userinfo-conforme.md) · [1.6](issue-1.6-prompt-consent.md) · [1.7](issue-1.7-politica-implicit-hybrid.md) · [1.8](issue-1.8-code-clientes-confidenciales.md) · [1.9](issue-1.9-introspeccion-todos-grants.md) · [1.10](issue-1.10-tests-negativos-seguridad.md) · [1.11](issue-1.11-politica-pkce.md) · [1.12](issue-1.12-nonce-ligado-autorizacion.md) · [1.13](issue-1.13-claims-proposito-token.md) · [1.14](issue-1.14-hint-sid-logout.md) · [1.15](issue-1.15-scopes-conjuntos-exactos.md) · [1.16](issue-1.16-emision-canje-fuera-tokencontroller.md).
- Funcionalidad parcial: [2.1](issue-2.1-registro-dinamico-clientes.md) · [2.2](issue-2.2-endpoints-invitaciones.md) · [2.3](issue-2.3-politica-par.md) · [2.4](issue-2.4-redirects-logout.md) · [2.5](issue-2.5-grants-inalcanzables-codigo-muerto.md) · [2.6](issue-2.6-generacion-claves-fuera-peticion.md) · [2.7](issue-2.7-hints-authorize.md) · [2.8](issue-2.8-proteccion-secretos.md) · [2.9](issue-2.9-migracion-secretos-cifrado.md) · [2.10](issue-2.10-documentacion-oauth-pruebas.md).
- Extensiones: [3.1](issue-3.1-response-mode-form-post.md) · [3.2](issue-3.2-autenticacion-jwt-cliente.md) · [3.3](issue-3.3-request-objects-firmados.md) · [3.4](issue-3.4-federacion-oidc-generica.md) · [3.5](issue-3.5-perfil-at-jwt.md) · [3.6](issue-3.6-dpop.md) · [3.7](issue-3.7-algoritmos-es256-eddsa.md) · [3.8](issue-3.8-token-exchange.md) · [3.9](issue-3.9-grant-jwt-bearer.md) · [3.10](issue-3.10-hints-acr-localizacion.md) · [3.11](issue-3.11-userinfo-firmado.md).
- Perfil alto: [4.1](issue-4.1-mtls-tokens-certificado.md) · [4.2](issue-4.2-jarm.md) · [4.3](issue-4.3-ciba.md) · [4.4](issue-4.4-subject-pairwise.md) · [4.5](issue-4.5-cifrado-jwe-id-token-userinfo.md) · [4.6](issue-4.6-rich-authorization-requests.md) · [4.7](issue-4.7-resource-indicators.md) · [4.8](issue-4.8-claims-authorize.md) · [4.9](issue-4.9-session-management-iframe.md) · [4.10](issue-4.10-front-channel-logout.md).
- Validación: [V.1](issue-V.1-fixtures-pruebas-http-persistencia.md) · [V.2](issue-V.2-openid-conformance-suite.md) · [V.3](issue-V.3-conformidad-dcr.md).

**Leyenda de estado:** `[ ]` pendiente · `[x]` hecho y verificado en Java · `—` descartada o pospuesta (no se implementa sin caso de uso).

## Decisiones heredadas de PHP

Sincronizado con el plan PHP el 23-10-2026 (EP-01 a EP-11 cerradas allí). Son decisiones de producto y de contrato que Java adopta para tener el mismo comportamiento observable; la implementación sigue siendo la de Java. El detalle está en cada ficha.

| Decisión | Fichas Java | Origen PHP |
|---|---|---|
| Secreto de cliente: verificador HMAC + *pepper* (opción A), no recuperable; re-hash de contraseñas en bloque | 2.8, 2.9 | EP-02 |
| Solo `private_key_jwt`; sin `client_secret_jwt`; un JWT de cliente vale para un solo uso (aserción o JAR) | 3.2, 3.3 | EP-11 |
| Cliente `tenant = null` = global, gestionable solo por root; el del tenant gana al global; DCR nace con el tenant de la ruta | 0.5, 2.1 | EP-03 |
| Initial access token de DCR como entidad propia con digest, emitido por admin y mostrado una vez | 2.1, V.3 | EP-03 |
| Audiencia «al menos una»; `aud`, `tenant` y `jti` obligatorios; un único verificador para filtro, UserInfo e introspección | 0.8, 0.15, 1.5, 1.9 | EP-06 |
| Implicit retirado; hybrid solo en `fragment`/`form_post`; `nonce` obligatorio con `id_token` | 1.7, 1.12, 3.1 | EP-08 |
| `sid` público aleatorio, distinto de la cookie, en ID/refresh/logout token y front-channel | 1.2, 1.14, 4.10 | EP-05 |
| `select_account` = `prompt=login`; vida SSO por tenant; `prompt=none` nunca toca la cookie | 1.6, 3.10 | EP-09 (031, 067) |
| Grant MFA no interactivo descartado (code + PKCE o device) | — | 040 |
| Federación OIDC genérica descartada; SSO corporativo por SAML | 3.4 | 051 |
| Login federado endurecido (identidad enlazada, SAML estricto, `state` de un solo uso) | 0.16 | 072 |
| Pospuestas: API administrativa de sesiones, iframe de Session Management, grant JWT bearer | 4.9, 3.9 | 041, 062, 069 |

**Al sincronizar de nuevo:** revisar `done/` y `pospuestos/` de PHP, añadir aquí cada decisión nueva y reflejarla en su ficha. Las tareas abiertas en PHP (EP-12 a EP-16) siguen siendo referencia, no decisión.

## Fase 0 — Seguridad (bloqueante)

| Hecho | ID | Tarea y criterio de cierre | Esfuerzo |
|:---:|:---:|---|:---:|
| [ ] | 0.1 | Ligar el canje de `authorization_code` a cliente autenticado, `client_id`, tenant y `redirect_uri`; consumo atómico y revocación ante replay. Probar code ajeno y dos canjes concurrentes. | M |
| [ ] | 0.2 | Autenticar al cliente al refrescar, comprobar sesión vigente y limitar scopes a los originales; consumir el JTI antes de emitir y revocar ante replay. Probar refresh robado y concurrente. | M |
| [ ] | 0.3 | Validar `audience` contra lo permitido para cliente/tenant en authorize, password y device; rechazar audiencias ajenas. | S |
| [ ] | 0.4 | Aplicar `grantTypesJson`, `responseTypesJson`, `allowAllScopes`, `allowedScopesM2m` y `tokenEndpointAuthMethod` de `TrustedClient`; desactivar `password` por defecto. Probar permitido y denegado por cliente. | M |
| [ ] | 0.5 | Definir si los clientes son de tenant o globales con asignación explícita; impedir su uso fuera de los tenants permitidos. Probar aislamiento A/B. | M–L |
| [ ] | 0.6 | Registrar clientes participantes por sesión y limitarles el back-channel logout; probar que no se notifica a otros clientes ni tenants. Depende de 0.5. | M |
| [ ] | 0.7 | Eliminar `withKeyId(privateKey)` de `signKeypass` y verificar pre-sesiones por `scope=challenge`; probar que otros tipos de token no sirven. | S |
| [ ] | 0.8 | En APIs bearer, validar issuer, propósito/tipo y revocación; rechazar ID, refresh, MFA y challenge aunque la firma sea válida. | S–M |
| [ ] | 0.9 | Centralizar autenticación de clientes Basic/post para token, device, PAR, introspect y revocation; comparar secretos en tiempo constante y devolver errores controlados. | M |
| [ ] | 0.10 | Eliminar comodines `*` y `/*` de `redirect_uri`, salvo política explícita de puerto loopback; probar URI no registrados. | S |
| [ ] | 0.11 | Codificar `state` en authorize/logout y no persistir `client_secret` en PAR; probar caracteres especiales y ausencia del secreto almacenado. | S |
| [ ] | 0.12 | Leer campos del formulario en `POST /logout` y pedir confirmación cuando no haya `id_token_hint`; probar cierre involuntario. | S |
| [ ] | 0.13 | Separar la ruta de revocación RFC 7009 de la de pre-sesión; probar que ambas operaciones llegan al controlador correcto. | S |
| [ ] | 0.14 | En PAR, exigir `client_id` del cliente autenticado y consumo único de `request_uri`; probar discrepancias, conflictos y reutilización. Completa 0.11; 2.3 amplía la política. | M |
| [ ] | 0.15 | Validar audiencia y tenant en bearer/UserInfo y rechazar JTI revocados aun con caché positiva; probar token de otro resource server o tenant. Amplía 0.8. | M |
| [ ] | 0.16 | Login federado: elegir la cuenta por identidad enlazada `(proveedor, sub)` y nunca por `name`; SAML estricto (firma de la aserción usada, emisor, audiencia del SP, destino, `InResponseTo`); `state` ligado a la pre-sesión y de un solo uso; escapar el HTML del callback. Probar aserción falsificada, usuario de Google con nombre de una cuenta local y callback repetido. **Crítica.** | M–L |

## Fase 1 — OIDC Core y discovery

| Hecho | ID | Tarea y criterio de cierre | Esfuerzo |
|:---:|:---:|---|:---:|
| [ ] | 1.1 | Ajustar contributors de discovery a rutas/capacidades reales (§4 de la fuente): retirar DCR, CIBA, iframe, JAR/`claims`, `pairwise`, JWE/JARM, mTLS y auth JWT mientras no funcionen; no exigir PAR falsamente, ajustar response types, métodos de autenticación, claims y grants. Probar metadata frente a endpoints. | S |
| [ ] | 1.2 | Emitir ID token solo con `openid`, `aud` del cliente, TTL corto propio, `sid` de sesión y `amr` array; omitir `nonce` vacío. Probar claims y caducidad. | M |
| [ ] | 1.3 | Usar modo correcto en errores de autorización, preservar `state`, añadir `iss` y `error_description`; incluir `iss` también en respuesta code. Probar éxito/error y caracteres especiales. | S |
| [ ] | 1.4 | Devolver errores OAuth estándar en `/token` (`invalid_grant`, `invalid_client`, etc.), `WWW-Authenticate` cuando corresponda y `Cache-Control: no-store`; probar respuestas negativas. | S |
| [ ] | 1.5 | Añadir POST UserInfo, email real, `email_verified` y `address` según scope; validar access tokens sin depender de sesión interactiva. Probar scopes y `sub` consistente. | S–M |
| [ ] | 1.6 | Hacer que `prompt=consent` fuerce una nueva decisión de scopes; probar aun con consentimiento previo. | S |
| [ ] | 1.7 | Retirar implicit (decidido en PHP EP-08) y conservar los hybrid emitiendo solo los artefactos pedidos, con hashes y `fragment`; alinear discovery y tests. | S–M |
| [ ] | 1.8 | Admitir clientes confidenciales en authorization code, con autenticación en token y política PKCE definida; probar web app con secreto. Depende de 0.1 y 0.9. | M |
| [ ] | 1.9 | Validar firma, tipo, issuer y revocación en introspección para todos los grants; probar `active` de password y client_credentials y después de revocar. | M |
| [ ] | 1.10 | Cubrir casos negativos de code ajeno/doble canje, refresh robado/concurrente, audience ajena y redirect no registrado con pruebas HTTP + persistencia. | M |
| [ ] | 1.11 | Mantener PKCE `S256`, exigirlo a públicos y definir política para confidenciales; probar ausencia, `plain` y verifier erróneo. | S |
| [ ] | 1.12 | Ligar `nonce` al code original, exigirlo en implicit/hybrid si se conservan y omitir el claim si no se envía; probar omisión y discordancia. | S |
| [ ] | 1.13 | Separar claims por tipo: `auth_time`/`acr`/`amr` reales y hashes solo cuando correspondan; comprobar `sub` de UserInfo igual al del ID token. | M |
| [ ] | 1.14 | Validar `id_token_hint` frente a issuer/RP/sesión y usar `sid` público estable, distinto de la cookie, en ID/logout token; probar logout y revocación entre tenants. Depende de 0.5, 0.6 y 1.2. | M |
| [ ] | 1.15 | Tratar scopes como elementos exactos en autorización, refresh, emisión y UserInfo; `myprofile` no debe conceder `profile`. | S–M |
| [ ] | 1.16 | Extraer del `TokenController` el canje/refresh y la emisión a casos de uso, reutilizando `ClaimsMapper`/`JwtTokenBuilder`; caracterizar antes todos los grants. | M–L |

## Fase 2 — Funcionalidad parcial

| Hecho | ID | Tarea y criterio de cierre | Esfuerzo |
|:---:|:---:|---|:---:|
| [ ] | 2.1 | Exponer DCR RFC 7591/7592 sobre los casos de uso existentes; validar caducidad/usos de `initial_access_token`, grants, metadata y tenant. Anunciarlo solo cuando funcione. | M |
| [ ] | 2.2 | Exponer create/accept/cancel/resend de invitaciones y probar autorización, caducidad y uso único. | S–M |
| [ ] | 2.3 | Admitir PAR de clientes públicos, validar redirect/solicitud en el push y permitir exigirlo por cliente; alinear discovery. Depende de 0.14. | S |
| [ ] | 2.4 | Mantener `post_logout_redirect_uris` separadas de redirects de login; rechazar las no registradas. | S |
| [ ] | 2.5 | Decidir habilitar o eliminar `delegated` y consolidar/eliminar `RefreshGranter`; ajustar grants anunciados y probar las rutas que permanezcan. | S |
| [ ] | 2.6 | Generar claves en un job fuera de JWKS/firma; probar disponibilidad y rotación sin generación síncrona en la petición. | S |
| [ ] | 2.7 | Aplicar `login_hint` e `id_token_hint` válidos en authorize; probar hint inválido y `prompt=none`. | S |
| [ ] | 2.8 | Revisar hash/comparación temporalmente segura de secretos y su exposición posterior al alta DCR; revisar protección de contraseñas de usuario. Probar que lectura DCR no devuelve el secreto. | M |
| [ ] | 2.9 | Migrar secretos de cliente de cifrado reversible a verificador no reversible; revisar clave AES y contraseñas de usuario con migración compatible. | L |
| [ ] | 2.10 | Actualizar guías OAuth, ejemplos y discovery al cerrar tareas verificadas; retirar afirmaciones de capacidades no probadas. | S–M |

## Fase 3 — Extensiones (según necesidad)

| Hecho | ID | Tarea y criterio de cierre | Referencia |
|:---:|:---:|---|---|
| [ ] | 3.1 | Añadir `response_mode=form_post` con formulario escapado y tests de éxito/error. | OAuth Form Post |
| [ ] | 3.2 | Autenticar clientes con `private_key_jwt` (sin `client_secret_jwt`, por la opción A de 2.9), validando firma, claims y replay compartido con JAR; anunciar solo métodos operativos. | RFC 7523, OIDC Core §9 |
| [ ] | 3.3 | Procesar JAR firmado (`request`/`request_uri`) con JWKS, claims y anti-replay; anunciarlo tras las pruebas. | RFC 9101 |
| — | 3.4 | **Descartada** (PHP 051, 22-10-2026); se reabre con un cliente que aporte su IdP OIDC. ~~Añadir proveedor federado OIDC genérico (discovery, JWKS, validación y vínculo de cuentas); adaptar Microsoft, Apple y GitHub según requisito.~~ | OIDC Core |
| [ ] | 3.5 | Emitir access token con cabecera `typ: at+jwt` y claims RFC 9068; probar rechazo de otro propósito/audiencia. | RFC 9068 |
| [ ] | 3.6 | Incorporar DPoP en emisión y consumo, con prueba de posesión y replay. | RFC 9449 |
| [ ] | 3.7 | Añadir ES256/EdDSA si se necesitan, con claves, JWKS, verificación y metadata coherentes. | RFC 7518 / 8037 |
| [ ] | 3.8 | Implementar token exchange con política de actores, audiencia, scopes y confianza; probar denegación de escaladas. | RFC 8693 |
| — | 3.9 | **Pospuesta** (PHP 069). Implementar JWT bearer solo con caso de uso aprobado; validar emisor, audiencia y anti-replay. | RFC 7523 |
| [ ] | 3.10 | Definir selector `select_account`, documentar ACR y aplicar `ui_locales`/`claims_locales` si se ofrecen; probar step-up y selección. | OIDC Core |
| [ ] | 3.11 | UserInfo firmado por cliente con `iss`/`aud` y claims consentidas; anunciar solo si funciona. | OIDC Core §5.3.2 |

## Fase 4 — Perfil alto (solo con requisito)

| Hecho | ID | Tarea y criterio de cierre |
|:---:|:---:|---|
| [ ] | 4.1 | mTLS y access tokens ligados a certificado: verificar certificado en emisión y consumo; anunciar endpoints/aliases reales (RFC 8705). |
| [ ] | 4.2 | JARM: firmar respuestas de autorización y validar modos y claims (OIDC JARM). |
| [ ] | 4.3 | CIBA: implementar solicitud, aprobación y modos anunciados; eliminar el 403 antes de publicarlo. |
| [ ] | 4.4 | `sub` pairwise: derivación estable por sector y aislamiento entre RP. |
| [ ] | 4.5 | JWE: cifrar ID token y UserInfo para clientes configurados y publicar algoritmos reales. |
| [ ] | 4.6 | RAR: validar `authorization_details` y limitarlo a lo concedido (RFC 9396). |
| [ ] | 4.7 | Resource indicators: validar `resource` y restringir audiencias en emisión/consumo (RFC 8707). |
| [ ] | 4.8 | Parámetro `claims`: validar claims esenciales/opcionales y respetar consentimiento y scopes antes de volver a anunciarlo. |
| [ ] | 4.9 | Iframe OIDC Session Management **pospuesto** (PHP 062): retirar ya la ruta stub y su anuncio; implementarlo con `session_state` derivado del `sid` público solo si un RP lo necesita. |
| [ ] | 4.10 | Front-channel logout: iframes solo para RP de la sesión, con `iss`/`sid` cuando el cliente lo requiera. |

## Validación transversal

- [ ] V.1 Crear fixtures compartibles con PHP: dos tenants, clientes público/confidencial, redirects válidos/inválidos, sesiones, claves y reloj controlado; cubrir code/refresh concurrentes, grants, device, PAR, revocación, UserInfo y logout con HTTP + persistencia.
- [ ] V.2 Tras fases 0 y 1, ejecutar OpenID Conformance Suite (*Basic OP*, *Config OP*, *RP-Initiated Logout*, *Back-Channel Logout*) e integrarla en CI; corregir fallos antes de declarar conformidad.
- [ ] V.3 Si se expone DCR (2.1), añadir *Dynamic OP* y probar credenciales de registro caducadas/agotadas, metadata inválida y aislamiento entre tenants.
