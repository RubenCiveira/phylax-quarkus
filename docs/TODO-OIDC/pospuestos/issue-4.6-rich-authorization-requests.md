# 4.6 — Rich Authorization Requests

- **Fase:** 4 (solo con requisito) · **Estado:** pospuesta (27-10-2026) · **Referencia:** RFC 9396.
- **Motivo:** no hay tipos de `authorization_details` definidos ni una pantalla de consentimiento granular; sin ellos el parámetro solo podría rechazarse. La columna `authorization_details_types` ya se mapea en `ClientDetails` y el recorrido authorize → code → token de [4.7](../done/issue-4.7-resource-indicators.md)/[4.8](../done/issue-4.8-claims-authorize.md) sirve de patrón.
- **Se reabre:** con el primer tipo de detalle aprobado (esquema JSON y su consentimiento).
