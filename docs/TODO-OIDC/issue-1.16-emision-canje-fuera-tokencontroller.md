# 1.16 — Emisión y canje fuera del TokenController

- **Fase:** 1 · **Esfuerzo:** M–L · **Estado:** pendiente.
- **Implementación:** caracterizar flujos code, refresh, device, password y client_credentials antes de refactorizar. Llevar validación/canje y refresh a casos de uso o granters coherentes con los existentes; mantener `ClaimsMapper` y `JwtTokenBuilder` como punto único de emisión por tipo. Evitar duplicar emisión en controller y respuesta HTML.
- **Cierre:** mismos contratos HTTP positivos durante la extracción, rutas negativas de [0.1](issue-0.1-canje-seguro-de-authorization-code.md)/[0.2](issue-0.2-refresh-cliente-sesion.md) centralizadas, sin `RefreshGranter` inalcanzable ([2.5](issue-2.5-grants-inalcanzables-codigo-muerto.md)).
- **Plan PHP:** [038-extraer-emision-tokens.md](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/038-extraer-emision-tokens.md); adaptar la idea, **no** crear un segundo mapper ya existente en Java.
