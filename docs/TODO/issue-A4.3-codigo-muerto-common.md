# A4.3 — Eliminar código muerto de `common` y `bootstrap`

- **Fase:** 4 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** Manual.
- **Problema:** Siguen existiendo `bootstrap/sql/MigrationsManager` (duplicado de `common/infrastructure/migration/MigrationsManager`) y `common/value/InMemoryCache` (sin referencias).
- **Implementación:** Borrar los duplicados después de comprobar que no tienen referencias. Los validadores de `common` se resuelven en A2.2 (integrarlos o eliminarlos).
- **Cierre:** Compila y los tests pasan. `grep` no encuentra referencias a las clases eliminadas.
- **Dependencias:** [A2.2](issue-A2.2-vo-con-reglas.md).
- **Fuente:** [§2](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#2-estado-de-las-recomendaciones-del-análisis-anterior).
