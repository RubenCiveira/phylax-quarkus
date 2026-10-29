# A2.4 — Violación de FK traducida a error de dominio

- **Fase:** 2 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** `common/infrastructure/sql` + exception mappers.
- **Problema:** `UncheckedSqlException` solo mapea las claves duplicadas. Borrar una entidad referenciada (p. ej. una relying party con roles) falla por FK y responde 500. Tampoco hay observers de `*DeleteCheck` con reglas de negocio.
- **Implementación:** Detectar la violación de integridad referencial (MySQL 1451/1452, SQLState 23000) y convertirla en una `ConstraintException` → 409 con el recurso afectado. De forma opcional, que el generador emita `*DeleteCheck` cuando el modelo declare dependencias que deben bloquear el borrado.
- **Cierre:** Borrar una relying party con roles o un tenant con usuarios devuelve 409 con un mensaje legible, nunca 500.
- **Fuente:** [§4.8](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#48-medio--modelo-de-dominio-anémico-y-vo-sin-reglas).
