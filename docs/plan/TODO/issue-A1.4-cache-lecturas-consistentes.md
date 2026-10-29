# A1.4 — Caché sin lecturas desactualizadas

- **Fase:** 1 · **Esfuerzo:** S–M · **Estado:** pendiente · **Dónde:** Plantilla `*CacheAdapter` + configuración.
- **Problema:** `retrieve` no consulta `pendingWrites` (solo `store` lo hace), y `cache.get(key, _ -> null)` ([RoleCacheAdapter.java:68](../../src/main/java/net/civeira/phylax/features/access/role/infrastructure/driven/RoleCacheAdapter.java#L68)) puede guardar el *miss*. Caffeine es local (10 min): con más de una réplica, la invalidación no se propaga.
- **Implementación:** En `retrieve`, no servir desde la caché mientras haya *pending writes* de la entidad. Usar `getIfPresent`, sin *loader* nulo. Decidir la estrategia multi-réplica: caché distribuida (Redis/Infinispan), invalidación por mensaje o desactivar la caché de listas, y documentarla.
- **Cierre:** Tras una escritura (también desde `oauth`), la lectura siguiente no devuelve el valor anterior. Con dos instancias, una escritura en una no deja datos viejos en la otra más allá del TTL acordado.
- **Dependencias:** [A1.3](issue-A1.3-auditoria-cache-en-adaptador.md).
- **Fuente:** [§4.6](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#46-medio--caché-local-con-lecturas-desactualizadas).
