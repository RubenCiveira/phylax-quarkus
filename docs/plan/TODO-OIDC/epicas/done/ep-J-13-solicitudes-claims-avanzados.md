# J-13 — Solicitudes y claims avanzados

**Cerrada (27-10-2026).** 4.4, 4.7 y 4.8 hechas; 4.6 RAR pospuesta sin tipos de detalle aprobados. El `sub` sectorial llega a ID token, access token (sin `uid`), UserInfo, introspección y logout token; solo el refresh token, que no sale de este servidor, conserva el username. Una claim esencial que no cubren los scopes o que el usuario no tiene termina la solicitud. **Límite de 4.8:** `value`/`values` no filtran.

**Prioridad:** opcional · **Tareas:** [4.4](../../done/issue-4.4-subject-pairwise.md), [4.6](../../pospuestos/issue-4.6-rich-authorization-requests.md), [4.7](../../done/issue-4.7-resource-indicators.md), [4.8](../../done/issue-4.8-claims-authorize.md). **Depende de:** claims J-03 y origen de solicitud J-05.

## Implementación

Para cada extensión aprobada, transportar autorización inmutable de authorize a code/refresh con prohibición de ampliación: `resource` (audiencia permitida), `claims` esenciales/opcionales (consentimiento), `authorization_details` tipado (RAR) y `sub` pairwise estable por sector validado. El mismo `sub` sectorial debe llegar a ID, access, UserInfo y logout cuando corresponda; sin aprobación, flags de discovery desactivados.

**Aceptación:** resource o detalle ajeno rechazados; refresh no amplía privilegios; claim esencial indisponible falla; dos sectores distintos reciben sujetos estables diferentes.

**Referencia PHP:** EP-15 (056, 058, 059, 061). Java ya tiene una `audience` libre que debe restringirse en J-02 antes de añadir resource indicators.
