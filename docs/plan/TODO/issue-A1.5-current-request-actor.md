# A1.5 — Corregir `CurrentRequest.getActor()`

- **Fase:** 1 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** `common/infrastructure`.
- **Problema:** [CurrentRequest.java:255](../../src/main/java/net/civeira/phylax/common/infrastructure/CurrentRequest.java#L255) usa `claim` (el `tid`) en vez de `claimWithName`, lo que lanza NPE sin `tid`, y el mapa no se usa. Sin `tid`, el tenant pasa a ser el issuer, que es una URL ([línea 249](../../src/main/java/net/civeira/phylax/common/infrastructure/CurrentRequest.java#L249)). `uid` = `sub` = username, mientras que el uid real va en el claim `uid`.
- **Implementación:** Corregir la variable o eliminar el mapa sin uso. Sin `tid`, dejar el tenant vacío (que A0.5 tratará como denegación) en lugar de usar el issuer. Usar el claim `uid` como identidad del actor y mantener el `sub` como nombre.
- **Cierre:** Hay tests con un token con `tid`, sin `tid` y con `uid` distinto de `sub`: no hay NPE, el tenant es correcto o vacío, y la auditoría registra el uid real.
- **Fuente:** [§4.2](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#42-crítico--el-actor-anónimo-desactiva-el-filtro-de-tenant).
