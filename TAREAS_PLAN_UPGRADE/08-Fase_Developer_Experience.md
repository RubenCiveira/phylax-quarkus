# Fase 08 — Developer Experience

> **Prioridad:** P4
> **Prerequisito:** Fases 02, 05 y 06 completadas (para tener APIs a documentar/consumir)
> **Fecha objetivo:** Sprint 13+

---

## Descripción

Esta fase mejora la experiencia de los desarrolladores que construyen sobre
Phylax como plataforma BaaS. Incluye las APIs de administración avanzadas
y los SDKs cliente que reducen el tiempo de integración.

---

## Tareas

| # | Tarea | Impacto | Esfuerzo |
|---|-------|---------|----------|
| 08-01 | Admin Dashboard API | Medio | Alto |
| 08-02 | SDKs cliente (JS/TS + Java) | Alto | Alto |

---

## Criterios de aceptación de fase

- [ ] Admin API: estadísticas, búsqueda avanzada de usuarios e impersonación
- [ ] JS/TS SDK publicado con `signIn()`, `signOut()`, `getSession()` + PKCE nativo
- [ ] Java/Quarkus client library para consumir el Management API
- [ ] Spec OpenAPI exportable para generación automática de SDKs
