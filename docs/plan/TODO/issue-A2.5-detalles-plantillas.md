# A2.5 — Detalles de las plantillas (RBAC, `version`, scope CDI)

- **Fase:** 2 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** Generador.
- **Problema:** [RoleRbacRegister.java:37-49](../../src/main/java/net/civeira/phylax/features/access/role/infrastructure/bootstrap/RoleRbacRegister.java#L37-L49): todas las acciones tienen `description("list")`, y `name` de `role` se describe como «The user name to identify on the login screen». `*CreateInput` acepta `version` del cliente. Las clases `policy` no declaran scope CDI.
- **Implementación:** En la plantilla RBAC, generar la descripción por acción y por campo desde el modelo. Ignorar `version` (y valorar `uid`) en la creación. Emitir `@ApplicationScoped` en las políticas.
- **Cierre:** El RBAC externo recibe descripciones correctas. Crear con `version=99` guarda la versión 1. Todas las políticas tienen un scope explícito.
- **Fuente:** [§4.9](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#49-bajo--detalles-de-las-plantillas).
