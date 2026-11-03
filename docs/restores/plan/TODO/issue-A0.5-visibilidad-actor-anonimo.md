# A0.5 — Visibilidad: el actor anónimo o sin tenant no ve nada

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** Plantilla de políticas de visibilidad.
- **Problema:** El actor anónimo tiene tenant `null`. `setTenantAccessible(null)` produce un `Optional` vacío, y el DAO no añade el `WHERE tenant = ?` ([TenantAccessible.java:16](../../src/main/java/net/civeira/phylax/features/access/user/application/policy/TenantAccessible.java#L16)). Resultado probable: el anónimo ve todos los tenants. `InheritTenantExcludingRoot` asigna `TenantReference.of(null)` al crear.
- **Implementación:** En las políticas `TenantAccessible`, `FixTenantExcludingRoot` e `InheritTenantExcludingRoot`: si el actor no está autenticado o no tiene tenant, denegar (o aplicar un filtro imposible) en vez de no filtrar. Valorar que los filtros de visibilidad distingan «sin restricción» de «valor nulo».
- **Cierre:** Hay un test de integración en el que `GET /api/access/users` (y el resto de listados con filtro de tenant) con un actor anónimo o sin `tid` devuelve vacío o 403, nunca datos de otros tenants. Crear sin tenant falla.
- **Dependencias:** [A0.2](issue-A0.2-autenticacion-obligatoria-api.md).
- **Fuente:** [§4.2](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#42-crítico--el-actor-anónimo-desactiva-el-filtro-de-tenant).
