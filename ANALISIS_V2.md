# Analisis Arquitectonico V2 - Enfoque Generador (Apps Maestros)

## 1. Resumen de Arquitectura
El proyecto mantiene una arquitectura hexagonal por features, con dominio y aplicacion separados de infraestructura. La organizacion por modulos facilita la consistencia entre entidades y hace viable la generacion automatica. La capa common concentra infraestructura transversal (SQL, proyecciones, batch, excepciones, mail, telemetry), permitiendo a las aplicaciones de tipo maestro reutilizar un armazon estable.

## 2. Observaciones Relevantes para el Generador
- El generador ya impone un patron consistente por entidad, pero genera demasiado boilerplate en mapeos, SQL y trazas.
- El coste de cambiar una entidad es alto porque se dispersa en varios mapeos y repositorios.
- Las cadenas SQL largas son fragiles y dificiles de revisar en equipos.
- El tracing manual invade codigo funcional, especialmente en casos de uso y gateways.
- Falta un criterio riguroso de testing de comportamiento en usecases y endpoints.

## 3. Conclusiones y Recomendaciones (Checklist por Prioridad)

### P0 - Critico (Inmediato)
- **Observabilidad sin boilerplate**: generar `@Observed`/`@ApiObserved` y mover la instrumentacion a interceptores. Usar `Telemetry.addEvent` y `Telemetry.setAttribute` en puntos clave.
- **Validacion de entrada**: generar constraints JSR-380 en DTOs y `@Valid` en controllers.
- **Errores con contexto**: mensajes con uid/tipo en `NotFound`, `OptimistLock`, `NotAllowed`.
- **Separacion generated/manual**: rutas distintas y cabeceras de origen para evitar edits perdidos.

### P1 - Alto Impacto (Corto Plazo)
- **Mapper por entidad**: centralizar conversiones API <-> domain <-> DB en una clase dedicada.
- **SQL por metadata**: generar `Schema` y builders para insert/update/filter/order; eliminar strings largos.
- **Proyecciones unificadas**: una base con factories (create/update/retrieve/list) para reducir duplicados.
- **Tests de usecase (nivel 2)**: mocks sobre gateways y visibilidad, con asserts de comportamiento.

### P2 - Medio Impacto (Medio Plazo)
- **Tests REST IT (nivel 3)**: `@QuarkusTest` + Testcontainers.
- **application-test.yaml** generado con devservices por defecto.
- **ErrorResponse estandar** con `code/message/field/details`.

### P3 - Evolucion (Largo Plazo)
- **ADRs** y guia de onboarding del generador.
- **Template de modulo maestro** para alta de features con patron fijo.
- **OpenAPI single source of truth** generado desde el modelo.

## 4. Impacto Directo en Apps Maestros
La adopcion de estos puntos reduce el tiempo de onboarding, evita divergencias entre modulos y mejora la seguridad del refactoring. Para aplicaciones de tipo maestro, la prioridad debe ser: coherencia del modelo, trazabilidad uniforme y tests de comportamiento antes de refactorizaciones mayores.
