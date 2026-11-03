# A1.1 — Paginación descendente con `<`

- **Fase:** 1 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** Plantilla DAO.
- **Problema:** `tryToOrderBy*Desc` usa `SqlOperator.GT` ([RoleJdbcDao.java:613](../../src/main/java/net/civeira/phylax/features/access/role/infrastructure/repository/RoleJdbcDao.java#L613)) en los 8 DAO que tienen orden descendente: la página siguiente devuelve las filas anteriores o ninguna.
- **Implementación:** En la plantilla, usar `LT` sobre la columna de orden en descendente. Revisar también el desempate por `uid` (en descendente, `uid` también debería ir en descendente, o se mantiene ascendente de forma coherente en el `ORDER BY` y en el `WHERE`).
- **Cierre:** Un test recorre todas las páginas en `*_DESC` con nombres repetidos: no hay saltos ni duplicados, y el total coincide con `count`.
- **Fuente:** [§4.4](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#44-alto--paginación-por-cursor-rota-al-ordenar).
