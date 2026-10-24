# 2.1 — Registro dinámico de clientes

- **Fase:** 2 · **Esfuerzo:** M · **Estado:** pendiente.
- **Decisión heredada (PHP EP-03):** el initial access token es una entidad propia `dcr-initial-access-token` con digest (no un JWT `dcr:register`), emitida por un admin (de tenant, para su tenant; root, indicando tenant) y devuelta en claro una sola vez; consumo atómico de usos antes de crear el cliente y después de validar los metadatos. Grants por defecto `authorization_code` + `refresh_token`; `password` exige permiso explícito; métodos de autenticación = los del tenant ∩ los que el token endpoint verifica.
- **Implementación:** exponer RFC 7591/7592 usando los casos de uso existentes; validar `initial_access_token`, tenant, grants y metadata.
- **Cierre:** alta, lectura, modificación y borrado probados; credencial caducada/agotada, otro tenant y metadata inválida rechazados. Publicar endpoint en discovery solo tras implementarlo.
- **Fuente:** [§5, 2.1](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).
