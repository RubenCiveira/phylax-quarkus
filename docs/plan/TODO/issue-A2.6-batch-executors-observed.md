# A2.6 — `*InBatchExecutor` sin spans manuales

- **Fase:** 2 · **Esfuerzo:** M · **Estado:** pendiente · **Dónde:** Plantilla batch.
- **Problema:** 32 clases de `application` importan `io.opentelemetry.*` y `io.quarkus.arc.Unremovable`, y abren spans a mano con el mismo bloque `try/catch/finally`.
- **Implementación:** Sustituir los spans por `@Observed` y apoyarse en `common/batch` (steppers genéricos) en lugar de una clase por agregado. Si `@Unremovable` es imprescindible, mover el executor a `infrastructure`.
- **Cierre:** `*/application` no importa `io.opentelemetry` ni `io.quarkus` (comprobado por A3.1). Las trazas del batch se mantienen.
- **Fuente:** [§2](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#2-estado-de-las-recomendaciones-del-análisis-anterior).
