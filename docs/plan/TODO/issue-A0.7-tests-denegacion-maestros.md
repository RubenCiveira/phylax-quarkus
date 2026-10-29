# A0.7 — Tests de denegación por maestro

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** pendiente · **Dónde:** Tests (generables).
- **Problema:** Ninguno de los 429 tests de `access` prueba accesos denegados ni filtros de tenant extremo a extremo.
- **Implementación:** Escribir una batería HTTP generada por maestro y operación con cuatro actores: anónimo, usuario de tenant A, admin de tenant B y root. Comprobar el código de estado y que no se filtran filas de otros tenants. Añadirla al CI.
- **Cierre:** La batería falla en el código actual (lo que demuestra §4.1 y §4.2) y pasa después de A0.1–A0.5. Las políticas nuevas incluyen sus casos.
- **Dependencias:** [A0.1](issue-A0.1-rbac-fail-closed.md), [A0.2](issue-A0.2-autenticacion-obligatoria-api.md), [A0.3](issue-A0.3-politicas-maestros-sin-autorizacion.md), [A0.5](issue-A0.5-visibilidad-actor-anonimo.md).
- **Fuente:** [§4.1](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#41-crítico--autorización-fail-open-por-diseño), [§4.2](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#42-crítico--el-actor-anónimo-desactiva-el-filtro-de-tenant).
