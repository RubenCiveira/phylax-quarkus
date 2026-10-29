# A1.2 — `Slider`: cursor completo desde el último elemento leído

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** pendiente · **Dónde:** `common/algorithms/Slider` + plantilla `*Slider`.
- **Problema:** [RoleSlider.java:54-55](../../src/main/java/net/civeira/phylax/features/access/role/infrastructure/repository/RoleSlider.java#L54-L55) solo actualiza `sinceUid`: al ordenar por nombre, la consulta de relleno usa `uid > X` y probablemente duplica filas en la última página. [Slider.java:127-131](../../src/main/java/net/civeira/phylax/common/algorithms/Slider.java#L127-L131) pagina desde el último elemento **aceptado**, lo que provocará `IndexOutOfBoundsException` (lote sin visibles) o un bucle infinito (cola descartada) cuando haya políticas `*AccessibleCheck`.
- **Implementación:** Pasar a `next(...)` el último elemento **leído**, no el último aceptado. En la plantilla `*Slider`, actualizar todas las columnas del cursor según el orden (`withSinceName`, `withSinceUid`…). Proteger contra lotes vacíos y añadir un tope de iteraciones.
- **Cierre:** Tests unitarios de `Slider` con un predicado que rechaza el primer lote entero y con otro que rechaza la cola. Test de integración del listado ordenado por nombre, en ascendente y descendente, con la última página parcial: sin duplicados.
- **Dependencias:** [A1.1](issue-A1.1-orden-descendente-lt.md).
- **Fuente:** [§4.4](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#44-alto--paginación-por-cursor-rota-al-ordenar).
