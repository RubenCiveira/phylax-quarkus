# A0.4 — Generador: permiso inicial `deny` sin política declarada

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** pendiente · **Dónde:** Generador (plantilla de caso de uso).
- **Problema:** Cada caso de uso arranca con `Permission.allow("Allow to … by default")` ([RoleCreateUsecase.java:122](../../src/main/java/net/civeira/phylax/features/access/role/application/usecase/create/RoleCreateUsecase.java#L122)). Si a una operación nueva le falta su política, queda abierta sin que nadie lo detecte.
- **Implementación:** La plantilla debe emitir `Permission.deny("No policy declared")` cuando el modelo no declara política para esa operación, o el generador debe fallar si falta. Que el modelo admita una marca `public`/`authenticated-only` cuando la apertura sea intencionada.
- **Cierre:** Al regenerar con una operación sin política, la operación responde 403 (o la generación falla). Las operaciones con política se comportan igual que antes.
- **Fuente:** [§4.1](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#41-crítico--autorización-fail-open-por-diseño).
