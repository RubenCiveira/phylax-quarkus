# A2.3 — Construcción de entidades solo por fábricas

- **Fase:** 2 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** Plantilla de entidad.
- **Problema:** `@Builder` y `@With` públicos en las entidades permiten crear o modificar instancias sin pasar por el constructor que acumula `ConstraintFailList` y sin registrar eventos.
- **Implementación:** Restringir `@Builder` y `@With` (`access = AccessLevel.PACKAGE`), o limitarlos a un *mapper* de infraestructura que rehidrate desde la base de datos. Dejar `create` / `update` / transiciones como única API pública de cambio.
- **Cierre:** Fuera del paquete de dominio y de la rehidratación del DAO no se puede construir una entidad con el builder (compila con error). Los tests siguen pasando.
- **Fuente:** [§4.8](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#48-medio--modelo-de-dominio-anémico-y-vo-sin-reglas).
