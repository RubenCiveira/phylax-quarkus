# Phylax API — Plan de Upgrade: Índice de tareas

> Adaptado del `UPGRADE_PLAN.md` para el proyecto Quarkus/Java.
> Stack: **Quarkus 3.x · Java · CDI · RESTEasy Reactive · SmallRye OpenAPI · Liquibase · MySQL**
> Última revisión: **2026-04-13** — v1.1 (decisión arquitectónica: `UserConsentedScope` en BC Access)

---

## Resumen de fases

| # | Fase | Prioridad | Tareas |
|---|------|-----------|--------|
| 01 | Refactoring y Deuda Técnica | P0 (bloqueante) | 6 |
| 02 | Cumplimiento OIDC Obligatorio | P0-P1 | 6 |
| 03 | Extensiones OAuth 2.0 | P1-P4 | 4 |
| 04 | Autenticación Avanzada | P2-P3 | 4 |
| 05 | API de Gestión BaaS | P1-P3 | 6 |
| 06 | Infraestructura Reactiva | P2-P4 | 4 |
| 07 | Cumplimiento GDPR | P3 | 3 |
| 08 | Developer Experience | P4 | 2 |

**Total: 35 tareas**

---

## Fase 01 — Refactoring y Deuda Técnica

- [01-Fase_Refactoring_Deuda_Tecnica.md](01-Fase_Refactoring_Deuda_Tecnica.md)
- [01-01-Tarea_Rename_Autenticate_Authenticate.md](01-01-Tarea_Rename_Autenticate_Authenticate.md)
- [01-02-Tarea_Eliminar_AuthorizedChallenges_Legacy.md](01-02-Tarea_Eliminar_AuthorizedChallenges_Legacy.md)
- [01-03-Tarea_Colapsar_Gateway_Repository.md](01-03-Tarea_Colapsar_Gateway_Repository.md)
- [01-04-Tarea_Fix_UserMfa_StoreSeed.md](01-04-Tarea_Fix_UserMfa_StoreSeed.md)
- [01-05-Tarea_Añadir_JTI_JWT.md](01-05-Tarea_Añadir_JTI_JWT.md)
- [01-06-Tarea_Cleanup_Jobs_Expirados.md](01-06-Tarea_Cleanup_Jobs_Expirados.md)

## Fase 02 — Cumplimiento OIDC Obligatorio

- [02-Fase_Cumplimiento_OIDC_Obligatorio.md](02-Fase_Cumplimiento_OIDC_Obligatorio.md)
- [02-01-Tarea_PKCE_RFC7636.md](02-01-Tarea_PKCE_RFC7636.md)
- [02-02-Tarea_Token_Introspection_RFC7662.md](02-02-Tarea_Token_Introspection_RFC7662.md)
- [02-03-Tarea_Scope_Consent_Tracking.md](02-03-Tarea_Scope_Consent_Tracking.md)
- [02-04-Tarea_Token_Revocation_RFC7009.md](02-04-Tarea_Token_Revocation_RFC7009.md)
- [02-05-Tarea_Logout_Distribuido.md](02-05-Tarea_Logout_Distribuido.md)
- [02-06-Tarea_OpenAPI_Endpoints_OIDC.md](02-06-Tarea_OpenAPI_Endpoints_OIDC.md)

## Fase 03 — Extensiones OAuth 2.0

- [03-Fase_Extensiones_OAuth2.md](03-Fase_Extensiones_OAuth2.md)
- [03-01-Tarea_PAR_RFC9126.md](03-01-Tarea_PAR_RFC9126.md)
- [03-02-Tarea_Dynamic_Client_Registration_RFC7591.md](03-02-Tarea_Dynamic_Client_Registration_RFC7591.md)
- [03-03-Tarea_Client_Credentials_M2M.md](03-03-Tarea_Client_Credentials_M2M.md)
- [03-04-Tarea_JAR_RFC9101.md](03-04-Tarea_JAR_RFC9101.md)

## Fase 04 — Autenticación Avanzada

- [04-Fase_Autenticacion_Avanzada.md](04-Fase_Autenticacion_Avanzada.md)
- [04-01-Tarea_WebAuthn_Passkeys.md](04-01-Tarea_WebAuthn_Passkeys.md)
- [04-02-Tarea_Magic_Links.md](04-02-Tarea_Magic_Links.md)
- [04-03-Tarea_MFA_SMS_Email.md](04-03-Tarea_MFA_SMS_Email.md)
- [04-04-Tarea_Proveedores_Sociales.md](04-04-Tarea_Proveedores_Sociales.md)

## Fase 05 — API de Gestión BaaS

- [05-Fase_API_Gestion_BaaS.md](05-Fase_API_Gestion_BaaS.md)
- [05-01-Tarea_Sesiones_Activas_API.md](05-01-Tarea_Sesiones_Activas_API.md)
- [05-02-Tarea_Perfil_Usuario_Ampliado.md](05-02-Tarea_Perfil_Usuario_Ampliado.md)
- [05-03-Tarea_Sistema_Invitaciones.md](05-03-Tarea_Sistema_Invitaciones.md)
- [05-04-Tarea_Organizaciones_Jerarquicas.md](05-04-Tarea_Organizaciones_Jerarquicas.md)
- [05-05-Tarea_ABAC_Permisos_Recurso.md](05-05-Tarea_ABAC_Permisos_Recurso.md)
- [05-06-Tarea_Personal_Access_Tokens.md](05-06-Tarea_Personal_Access_Tokens.md)

## Fase 06 — Infraestructura Reactiva

- [06-Fase_Infraestructura_Reactiva.md](06-Fase_Infraestructura_Reactiva.md)
- [06-01-Tarea_Sistema_Webhooks.md](06-01-Tarea_Sistema_Webhooks.md)
- [06-02-Tarea_Event_Streaming_SSE.md](06-02-Tarea_Event_Streaming_SSE.md)
- [06-03-Tarea_Feature_Flags.md](06-03-Tarea_Feature_Flags.md)
- [06-04-Tarea_Audit_Log_API.md](06-04-Tarea_Audit_Log_API.md)

## Fase 07 — Cumplimiento GDPR

- [07-Fase_Cumplimiento_GDPR.md](07-Fase_Cumplimiento_GDPR.md)
- [07-01-Tarea_Exportacion_Datos_GDPR.md](07-01-Tarea_Exportacion_Datos_GDPR.md)
- [07-02-Tarea_Derecho_Al_Olvido.md](07-02-Tarea_Derecho_Al_Olvido.md)
- [07-03-Tarea_Gestion_Consentimientos.md](07-03-Tarea_Gestion_Consentimientos.md)

## Fase 08 — Developer Experience

- [08-Fase_Developer_Experience.md](08-Fase_Developer_Experience.md)
- [08-01-Tarea_Admin_Dashboard_API.md](08-01-Tarea_Admin_Dashboard_API.md)
- [08-02-Tarea_SDKs_Cliente.md](08-02-Tarea_SDKs_Cliente.md)

---

## Dependencias entre fases

```
Fase 01 (Refactoring)
  └──→ Fase 02 (OIDC Obligatorio)
         ├──→ Fase 03 (Extensiones OAuth)
         ├──→ Fase 04 (Autenticación Avanzada) — independiente en su mayoría
         ├──→ Fase 05 (API Gestión BaaS)
         │       └──→ Fase 07 (GDPR)
         └──→ Fase 06 (Infraestructura Reactiva)
                └──→ Fase 08 (Developer Experience)
```

---

## Decisiones arquitectónicas registradas

### ADR-01 — `UserConsentedScope` vive en `features/access/`, no en `features/oauth/`

**Contexto:** La tarea 02-03 (Scope Consent Tracking) necesita persistir las
decisiones de consentimiento de scopes OAuth del usuario.

**Decisión:** Se crea `features/access/userconsentedscopes/` como bounded context
propio, análogo exacto a `features/access/useracceptedtermnsofuse/`. El módulo
`features/oauth/oidc/scopes/` actúa como capa anti-corrupción (ACL adapter) que
delega en los use cases del BC Access.

**Motivo:** La persistencia del consentimiento es un dato del usuario (ownership
de Access), no del protocolo (ownership de OIDC). El protocolo no debe poseer
la tabla ni el modelo.

**Consecuencias:**
- `ScopesConsentGateway` en OIDC pasa a ser `ScopesConsentAclAdapter`
- La página de gestión en `/account/{tenant}/consents` (tarea 07-03) orquesta
  `UserConsentedScope` + `UserAcceptedTermnsOfUse` en una vista unificada
- El formulario en-flujo del authorize sigue en `features/oauth/authentication/`

**Scopes como:** strings libres (`"openid"`, `"email"`, `"profile"`…), sin FK a tabla
de scopes. No hay BC `TenantScope`.

---

## Convenciones del proyecto (Quarkus/Java)

| Elemento | Patrón |
|----------|--------|
| Paquete base | `net.civeira.phylax.features.oauth.{feature}/` |
| Capas | `domain/`, `application/usecase/`, `infrastructure/driver/rest/`, `infrastructure/driven/` |
| Migraciones DB | `src/main/resources/db/migration/mysql/{version}/{timestamp}-{desc}.sql` (Liquibase) |
| Anotaciones OpenAPI | `@Operation`, `@APIResponse` (SmallRye / MicroProfile) |
| Eventos de dominio | CDI `Event<T>` asíncronos (`@ObservesAsync`) |
| Scheduler | `@Scheduled` (Quarkus Scheduler) |
| Caché | `@CacheResult` (Quarkus Cache / Caffeine) |
| Tests | `@QuarkusTest` + RestAssured + grupos `oidc-flow` |
| Ejecución de tests | `mvn test -Dgroups="oidc-flow"` |
