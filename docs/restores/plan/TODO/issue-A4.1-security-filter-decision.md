# A4.1 — Decidir el destino de `SecurityFilter`

- **Fase:** 4 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** `bootstrap/security`.
- **Problema:** [SecurityFilter.java:25-27](../../src/main/java/net/civeira/phylax/bootstrap/security/SecurityFilter.java#L25-L27) mantiene `@Provider`, `@Priority` y `@ApplicationScoped` comentados: el filtro y sus analizadores se compilan pero no se ejecutan.
- **Implementación:** Reactivarlo detrás de un flag (`app.security.injection-analysis.enabled`), al principio en modo solo métrica; o eliminar el filtro y los analizadores. Registrar la decisión en un ADR.
- **Cierre:** Existe un ADR. Si se reactiva, hay un test con una carga de inyección que incrementa la métrica (o bloquea, según el modo). Si se elimina, no queda código muerto.
- **Fuente:** [§2](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#2-estado-de-las-recomendaciones-del-análisis-anterior).
