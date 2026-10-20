# J-13 — Solicitudes y claims avanzados

**Prioridad:** opcional · **Tareas:** [4.4](../issue-4.4-subject-pairwise.md), [4.6](../issue-4.6-rich-authorization-requests.md), [4.7](../issue-4.7-resource-indicators.md), [4.8](../issue-4.8-claims-authorize.md). **Depende de:** claims J-03 y origen de solicitud J-05.

## Implementación

Para cada extensión aprobada, transportar autorización inmutable de authorize a code/refresh con prohibición de ampliación: `resource` (audiencia permitida), `claims` esenciales/opcionales (consentimiento), `authorization_details` tipado (RAR) y `sub` pairwise estable por sector validado. El mismo `sub` sectorial debe llegar a ID, access, UserInfo y logout cuando corresponda; sin aprobación, flags de discovery desactivados.

**Aceptación:** resource o detalle ajeno rechazados; refresh no amplía privilegios; claim esencial indisponible falla; dos sectores distintos reciben sujetos estables diferentes.

**Referencia PHP:** EP-15 (056, 058, 059, 061). Java ya tiene una `audience` libre que debe restringirse en J-02 antes de añadir resource indicators.
