# 2.1 — Registro dinámico de clientes

- **Fase:** 2 · **Esfuerzo:** M · **Estado:** pendiente.
- **Implementación:** exponer RFC 7591/7592 usando los casos de uso existentes; validar `initial_access_token`, tenant, grants y metadata.
- **Cierre:** alta, lectura, modificación y borrado probados; credencial caducada/agotada, otro tenant y metadata inválida rechazados. Publicar endpoint en discovery solo tras implementarlo.
- **Fuente:** [§5, 2.1](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).
