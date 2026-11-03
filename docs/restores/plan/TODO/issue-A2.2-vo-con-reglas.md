# A2.2 — Value objects con reglas y `tryFrom` sin `null`

- **Fase:** 2 · **Esfuerzo:** M–L · **Estado:** pendiente · **Dónde:** Generador (modelo + plantilla VO).
- **Problema:** Los VO solo validan `null` y el tipo (`NameVO` acepta `""` y cualquier longitud), y los 11 validadores de `common/value/validation/validators` no se usan. `tryFrom(…, fails)` devuelve `null` cuando hay error.
- **Implementación:** Que el modelo declare reglas (`not-blank`, longitud máxima alineada con la columna, formato email/URL, rango) y que `tryFrom` las ejecute reutilizando los validadores de `common`. Devolver `nullValue()` en lugar de `null`.
- **Cierre:** `name=""` o con más de 255 caracteres devuelve 400 con `ConstraintFail` en todos los maestros; un email inválido se rechaza. Ningún `tryFrom` devuelve `null`. Los validadores que siguen sin usarse se eliminan.
- **Fuente:** [§4.8](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#48-medio--modelo-de-dominio-anémico-y-vo-sin-reglas), [§2](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#2-estado-de-las-recomendaciones-del-análisis-anterior).
