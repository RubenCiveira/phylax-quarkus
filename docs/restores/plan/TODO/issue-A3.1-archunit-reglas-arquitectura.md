# A3.1 — Test de arquitectura (ArchUnit)

- **Fase:** 3 · **Esfuerzo:** M · **Estado:** pendiente · **Dónde:** Tests.
- **Problema:** Las reglas que hoy se cumplen (dominio sin frameworks, `application` sin `infrastructure`, `common` sin features) no están protegidas por ningún test.
- **Implementación:** Añadir ArchUnit con las siguientes reglas: `..domain..` sin `jakarta|io.quarkus|io.opentelemetry|java.sql`; `..application..` sin `..infrastructure..`; `common` sin `features`; grafo permitido entre agregados de `access` (§3.3); sin ciclos de paquetes fuera de `bootstrap`.
- **Cierre:** El CI ejecuta el test; si se introduce una dependencia prohibida, el build falla con un mensaje claro.
- **Dependencias:** [A2.6](issue-A2.6-batch-executors-observed.md), [A3.2](issue-A3.2-projection-descriptors-bootstrap.md).
- **Fuente:** [§3.3](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#33-grafo-de-dependencias-entre-agregados).
