# A3.2 — Sacar los `*ProjectionDescriptor` de los agregados

- **Fase:** 3 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** Generador.
- **Problema:** Los ciclos `tenant ↔ user`, `relyingparty ↔ role`… solo se deben a `infrastructure/bootstrap/*ProjectionDescriptor`, que importan descriptores de otros agregados y de `document` / `notification`.
- **Implementación:** Generar los descriptores con dependencias cruzadas en `bootstrap/` (composition root), o registrarlos por CDI sin imports directos entre agregados.
- **Cierre:** Ningún paquete de agregado de `access` importa otro agregado solo para proyecciones. El grafo de §3.3 queda sin ciclos.
- **Fuente:** [§3.3](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#33-grafo-de-dependencias-entre-agregados).
