# Pendientes de diseño — `features/access`

Fuente: [ANALISIS_DISENO_ACCESS.md](../../src/docs/access/ANALISIS_DISENO_ACCESS.md) (corte 2026-10-20), que incluye los pendientes que siguen abiertos del [análisis anterior](../analisis-diseno-clases.md). Las tareas OIDC están en [TODO-OIDC](../TODO-OIDC/README.md); aquí solo se referencian cuando se solapan (A0.6 ↔ OIDC 2.9).

Cada ID tiene una ficha `issue-<id>-<título>.md`. Casi todo `access` está generado: cuando el campo **Dónde** dice *plantilla* o *generador*, la corrección se hace en el generador y después se regenera; un cambio a mano en un agregado se pierde en la siguiente generación. Marcar una casilla solo cuando el criterio de cierre se haya verificado con pruebas observables. Esfuerzo orientativo: S ≤ 1 día, M 2–4 días, L ≥ 1 semana.

## Fase 0 — Seguridad (bloqueante)

| Hecho | ID | Tarea | Esfuerzo | Dónde | Depende de |
|:---:|:---:|---|:---:|---|---|
| [ ] | [A0.1](issue-A0.1-rbac-fail-closed.md) | RBAC cerrado por defecto | S | Configuración + `common/security` | — |
| [ ] | [A0.2](issue-A0.2-autenticacion-obligatoria-api.md) | Autenticación obligatoria en `/api/*` | S | Configuración | — |
| [ ] | [A0.3](issue-A0.3-politicas-maestros-sin-autorizacion.md) | Políticas de autorización para los maestros desprotegidos | M | Modelo + plantilla de políticas | [A0.4](issue-A0.4-generador-permiso-deny.md), [A0.7](issue-A0.7-tests-denegacion-maestros.md) |
| [ ] | [A0.4](issue-A0.4-generador-permiso-deny.md) | Generador: permiso inicial `deny` sin política declarada | M | Generador (plantilla de caso de uso) | — |
| [ ] | [A0.5](issue-A0.5-visibilidad-actor-anonimo.md) | Visibilidad: el actor anónimo o sin tenant no ve nada | S | Plantilla de políticas de visibilidad | [A0.2](issue-A0.2-autenticacion-obligatoria-api.md) |
| [ ] | [A0.6](issue-A0.6-hash-contrasenas-usuario.md) | Hash de contraseñas de usuario | M–L | `common/security` + `user` + `oauth` | — |
| [ ] | [A0.7](issue-A0.7-tests-denegacion-maestros.md) | Tests de denegación por maestro | M | Tests (generables) | [A0.1](issue-A0.1-rbac-fail-closed.md), [A0.2](issue-A0.2-autenticacion-obligatoria-api.md), [A0.3](issue-A0.3-politicas-maestros-sin-autorizacion.md), [A0.5](issue-A0.5-visibilidad-actor-anonimo.md) |

## Fase 1 — Corrección funcional

| Hecho | ID | Tarea | Esfuerzo | Dónde | Depende de |
|:---:|:---:|---|:---:|---|---|
| [ ] | [A1.1](issue-A1.1-orden-descendente-lt.md) | Paginación descendente con `<` | S | Plantilla DAO | — |
| [ ] | [A1.2](issue-A1.2-slider-cursor-completo.md) | `Slider`: cursor completo desde el último elemento leído | M | `common/algorithms/Slider` + plantilla `*Slider` | [A1.1](issue-A1.1-orden-descendente-lt.md) |
| [ ] | [A1.3](issue-A1.3-auditoria-cache-en-adaptador.md) | Auditoría y caché en las escrituras que no pasan por un caso de uso | M | Plantilla `*CommandRepositoryAdapter` (o casos de uso de sistema) | — |
| [ ] | [A1.4](issue-A1.4-cache-lecturas-consistentes.md) | Caché sin lecturas desactualizadas | S–M | Plantilla `*CacheAdapter` + configuración | [A1.3](issue-A1.3-auditoria-cache-en-adaptador.md) |
| [ ] | [A1.5](issue-A1.5-current-request-actor.md) | Corregir `CurrentRequest.getActor()` | S | `common/infrastructure` | — |

## Fase 2 — Modelo y plantillas

| Hecho | ID | Tarea | Esfuerzo | Dónde | Depende de |
|:---:|:---:|---|:---:|---|---|
| [ ] | [A2.1](issue-A2.1-eventos-after-success.md) | Eventos de dominio después del commit y límites transaccionales | M | Plantilla `*EventDispatcher` + casos de uso + `oauth` | — |
| [ ] | [A2.2](issue-A2.2-vo-con-reglas.md) | Value objects con reglas y `tryFrom` sin `null` | M–L | Generador (modelo + plantilla VO) | — |
| [ ] | [A2.3](issue-A2.3-builders-restringidos.md) | Construcción de entidades solo por fábricas | S | Plantilla de entidad | — |
| [ ] | [A2.4](issue-A2.4-violacion-fk-409.md) | Violación de FK traducida a error de dominio | S | `common/infrastructure/sql` + exception mappers | — |
| [ ] | [A2.5](issue-A2.5-detalles-plantillas.md) | Detalles de las plantillas (RBAC, `version`, scope CDI) | S | Generador | — |
| [ ] | [A2.6](issue-A2.6-batch-executors-observed.md) | `*InBatchExecutor` sin spans manuales | M | Plantilla batch | — |

## Fase 3 — Arquitectura (continuo)

| Hecho | ID | Tarea | Esfuerzo | Dónde | Depende de |
|:---:|:---:|---|:---:|---|---|
| [ ] | [A3.1](issue-A3.1-archunit-reglas-arquitectura.md) | Test de arquitectura (ArchUnit) | M | Tests | [A2.6](issue-A2.6-batch-executors-observed.md), [A3.2](issue-A3.2-projection-descriptors-bootstrap.md) |
| [ ] | [A3.2](issue-A3.2-projection-descriptors-bootstrap.md) | Sacar los `*ProjectionDescriptor` de los agregados | S | Generador | — |

## Fase 4 — Pendientes del análisis anterior (`bootstrap` / `common`)

| Hecho | ID | Tarea | Esfuerzo | Dónde | Depende de |
|:---:|:---:|---|:---:|---|---|
| [ ] | [A4.1](issue-A4.1-security-filter-decision.md) | Decidir el destino de `SecurityFilter` | S | `bootstrap/security` | — |
| [ ] | [A4.2](issue-A4.2-colector-otlp-autenticado.md) | Autenticación del colector OTLP | S | `bootstrap/telemetry/collector` | [A0.2](issue-A0.2-autenticacion-obligatoria-api.md) |
| [ ] | [A4.3](issue-A4.3-codigo-muerto-common.md) | Eliminar código muerto de `common` y `bootstrap` | S | Manual | [A2.2](issue-A2.2-vo-con-reglas.md) |
| [ ] | [A4.4](issue-A4.4-rangos-aes-cipher.md) | Rangos de `AesCipherService.generateSecureRandomPassword` | S | `common/crypto` | — |

## Orden recomendado

1. **Ya:** A0.1 + A0.2 (configuración, minutos de trabajo) y A0.7 en paralelo, para demostrar el problema y detectar regresiones.
2. **Bloqueante:** A0.4 → A0.3 (generador y luego modelo de políticas), A0.5 y A1.5; A0.6 coordinado con OIDC 2.9.
3. **Corrección:** A1.1 → A1.2 (paginación), A1.3 → A1.4 (auditoría y caché).
4. **Siguiente iteración del generador:** A2.1–A2.6, agrupados en una sola regeneración.
5. **Continuo:** A3.2 → A3.1 (ArchUnit), con las reglas ya cumplidas.
6. **Higiene:** A4.1–A4.4 en cualquier momento.
