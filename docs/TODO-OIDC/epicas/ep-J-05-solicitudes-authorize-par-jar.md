# J-05 — Origen y política de solicitudes authorize

**Prioridad:** alta para PAR/PKCE · **Tareas:** [0.11](../issue-0.11-state-seguro-secreto-fuera-par.md), [0.14](../issue-0.14-par-vinculado-un-solo-uso.md), [1.11](../issue-1.11-politica-pkce.md), [2.3](../issue-2.3-politica-par.md), [2.7](../issue-2.7-hints-authorize.md), [3.3](../issue-3.3-request-objects-firmados.md) opcional JAR, [3.10](../issue-3.10-hints-acr-localizacion.md) opcional hints/locales. **Depende de:** J-02; coordinar `AuthorizeHtml` con J-06.

## Implementación

1. Extraer resolución de parámetros: PAR guardado prevalece, solo `client_id` exterior idéntico; push ligado al cliente autenticado, sin secreto almacenado, redirect validado al push y `request_uri` de uso único. PAR obligatorio por cliente si se configura; no anunciar obligación de todo el tenant sin imponerla globalmente.
2. PKCE `S256` obligatorio a públicos, política configurable de confidenciales tanto al crear como al canjear code; preservar `login_hint`/`id_token_hint` verificables.
3. Si se aprueba JAR, precedencia documentada PAR/JAR/query, `iss`/`aud`/tiempos/`jti` anti-replay y fetcher de JWKS remoto seguro (HTTPS, sin IP privada/loopback, timeout, límites, caché); compartirlo con J-10/federación.

**Aceptación:** query no altera PAR, `request_uri` repetido o cliente ajeno falla; `plain` y downgrade rechazados; JAR no se anuncia sin validación y tests de replay/SSRF.

**Referencia PHP:** EP-07 (034, 046, 047, 068). A diferencia de PHP, Java ya da precedencia al PAR y rechaza `plain`; completar validaciones faltantes.
