# 2.6 — Generación de claves fuera de la petición

- **Fase:** 2 · **Esfuerzo:** S · **Estado:** pendiente.
- **Implementación:** generar/rotar claves mediante job en lugar de hacerlo en firma o JWKS.
- **Cierre:** pruebas de rotación, disponibilidad y publicación de `kid` sin generación síncrona durante la petición.
- **Fuente:** [§5, 2.6](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).
