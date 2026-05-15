# Fase 02 — Cumplimiento OIDC Obligatorio

> **Prioridad:** P0-P1 — Necesario para que el servidor sea plenamente conforme
> **Prerequisito:** Fase 01 completada (especialmente 01-05 JTI)
> **Fecha objetivo:** Sprint 2-3

---

## Descripción

Esta fase completa el cumplimiento con las especificaciones OIDC Core 1.0,
OAuth 2.0 y los RFCs relacionados que el servidor actualmente no implementa
o implementa parcialmente.

Sin estas features el servidor **no puede considerarse un OIDC Provider** de
producción conforme al estándar, lo que impide su uso como BaaS con garantías.

---

## Tareas

| # | Tarea | RFC / Spec | Impacto | Esfuerzo |
|---|-------|-----------|---------|----------|
| 02-01 | PKCE — Proof Key for Code Exchange | RFC 7636 | Crítico | Bajo |
| 02-02 | Token Introspection | RFC 7662 | Alto | Bajo |
| 02-03 | Scope Consent Tracking (completar) | OIDC Core | Alto | Medio |
| 02-04 | Token Revocation completa | RFC 7009 | Alto | Bajo |
| 02-05 | Logout distribuido Front/Back-Channel | OIDC Logout 1.0 | Alto | Medio |
| 02-06 | OpenAPI/Swagger en endpoints OIDC | — | Medio | Bajo |

---

## Dependencias internas

```
01-05 (JTI en JWT)
  └──→ 02-02 (Introspection usa JTI para lookup)
  └──→ 02-04 (Revocation usa JTI como denylist)
         └──→ 02-05 (Logout distribuido revoca tokens de todos los RPs)

02-03 (Scope Consent) es independiente — puede hacerse en paralelo con 02-01
02-06 (OpenAPI) puede hacerse en paralelo con el resto
```

---

## Notas de arquitectura

Todos los endpoints nuevos de esta fase deben:
1. Estar bajo la ruta `/openid/{tenant}/...` para respetar el aislamiento de tenant
2. Anunciarse en `GET /openid/{tenant}/.well-known/openid-configuration`
3. Seguir el patrón hexagonal: nuevo sub-package en `features/oauth/{feature}/`
   con capas `domain/`, `application/`, `infrastructure/`
4. Incluir anotaciones `@Operation` y `@APIResponse` (SmallRye OpenAPI) desde el primer commit
5. Tener tests de integración en `src/test/java/net/civeira/phylax/testing/oauth/flow/`

---

## Criterios de aceptación de fase

- [ ] PKCE implementado; clientes públicos pueden completar el Authorization Code Flow con `S256`
- [ ] `POST /openid/{tenant}/introspect` devuelve respuesta RFC 7662 correcta
- [ ] Scope consent se persiste en DB y no se solicita de nuevo si ya fue concedido
- [ ] `POST /openid/{tenant}/revoke` invalida el token en cascada
- [ ] Back-channel logout notifica a los RPs registrados
- [ ] Todos los endpoints OIDC aparecen en `/q/openapi`
- [ ] `mvn test -Dgroups="oidc-flow"` verde
