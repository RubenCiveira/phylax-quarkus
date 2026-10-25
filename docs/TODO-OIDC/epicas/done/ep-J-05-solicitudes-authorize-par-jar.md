# J-05 — Origen y política de solicitudes authorize

**Prioridad:** alta para PAR/PKCE · **Tareas:** [0.11](../../done/issue-0.11-state-seguro-secreto-fuera-par.md), [0.14](../../done/issue-0.14-par-vinculado-un-solo-uso.md), [1.11](../../done/issue-1.11-politica-pkce.md), [2.3](../../done/issue-2.3-politica-par.md), [2.7](../../done/issue-2.7-hints-authorize.md), [3.3](../../done/issue-3.3-request-objects-firmados.md) opcional JAR, [3.10](../../done/issue-3.10-hints-acr-localizacion.md) opcional hints/locales. **Depende de:** J-02; coordinar `AuthorizeHtml` con J-06.

**Estado:** cerrada (25-10-2026), incluidas las opcionales 3.3 y 3.10. Fichas en [`done/`](../../done/README.md). JAR rechaza todo request object hasta [regenerar el modelo](../../cambios-modelo-J02-J05.md) (tabla anti-replay).

## Implementación

1. Extraer resolución de parámetros: PAR guardado prevalece, solo `client_id` exterior idéntico; push ligado al cliente autenticado, sin secreto almacenado, redirect validado al push y `request_uri` de uso único. PAR obligatorio por cliente si se configura; no anunciar obligación de todo el tenant sin imponerla globalmente.
2. PKCE `S256` obligatorio a públicos, política configurable de confidenciales tanto al crear como al canjear code; preservar `login_hint`/`id_token_hint` verificables.
3. Si se aprueba JAR, precedencia documentada PAR/JAR/query, `iss`/`aud`/tiempos/`jti` anti-replay y fetcher de JWKS remoto seguro (HTTPS, sin IP privada/loopback, timeout, límites, caché); compartirlo con J-10/federación.

**Aceptación:** query no altera PAR, `request_uri` repetido o cliente ajeno falla; `plain` y downgrade rechazados; JAR no se anuncia sin validación y tests de replay/SSRF.

**Referencia PHP:** EP-07 (034, 046, 047, 068). A diferencia de PHP, Java ya da precedencia al PAR y rechaza `plain`; completar validaciones faltantes.

## Lo construido (documentación funcional)

### Origen de los parámetros (precedencia)

1. `request_uri` de PAR: solo lo guardado; `client_id` exterior idéntico; un solo uso ligado al navegador (`PAR_BINDING`).
2. `request` (JAR por valor): verificado y convertido en PAR consumido; 303 al `request_uri`.
3. `request_uri` por referencia: `request_uri_not_supported`.
4. La query.

### Política común (authorize y push PAR)

`response_type` registrado, audiencias, PKCE solo `S256` (obligatorio a públicos, a los que lo registran y, si se configura, a todos los confidenciales; también al canjear), PAR obligatorio por cliente. El push admite públicos, liga `client_id`, rechaza `request_uri` y nunca guarda secretos.

### Hints y nivel

`login_hint` escapado (≤256); `id_token_hint` de este tenant y cliente (caducado vale) o `invalid_request`; otro usuario no reutiliza la sesión. `acr_values` por niveles con step-up o `interaction_required`; sin enlace mágico si se pide MFA; `ui_locales` con idiomas soportados.

### JAR

RS256/ES256 con claves del cliente (en línea o `jwks_uri` con fetcher anti-SSRF), `iss`/`aud`/`exp`/`nbf`/vida ≤1 h, `jti` de un solo uso.

Código: `AuthorizationRequestResolver`, `AuthorizationRequestPolicy`, `ResolveParRequestUsecase`, `PushAuthorizationUsecase`, `RequestObjectVerifier`, `JwksSignatureAdapter`, `RemoteJwksFetcher`, `JwtReplaySqlAdapter`. Pruebas: `AuthorizeRequestFlowTest`, `AuthorizationRequestPolicyUnitTest`, `RequestObjectVerifierUnitTest`, `JwksSignatureAdapterUnitTest`, `RemoteJwksFetcherUnitTest`.
