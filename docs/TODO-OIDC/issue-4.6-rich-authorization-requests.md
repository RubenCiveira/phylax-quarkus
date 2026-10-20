# 4.6 — Rich Authorization Requests

- **Fase:** 4 (solo con requisito) · **Estado:** pendiente · **Referencia:** RFC 9396.
- **Implementación:** validar `authorization_details` y acotarlo a permisos consentidos y política de cliente.
- **Cierre:** detalles válidos se conservan en tokens; detalle no concedido o mal formado se rechaza.
- **Fuente:** [§5, fase 4](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-4--perfil-alto-fapi-20-solo-si-hay-requisito).
