# Fase 05 — API de Gestión de Identidad (BaaS Core)

> **Prioridad:** P1-P3
> **Prerequisito:** Fase 02 (para sesiones y tokens), Fase 01 (para perfil)
> **Fecha objetivo:** Sprint 5-9

---

## Descripción

Esta fase implementa las APIs de gestión que convierten el servidor en un
**Backend-as-a-Service** real. Los desarrolladores pueden gestionar identidades,
sesiones, organizaciones y permisos desde sus backends sin pasar por el
authorize flow OIDC.

Todas las APIs de gestión se exponen bajo `/api/access/` y requieren
autenticación con un Bearer token con scopes de administración.

---

## Tareas

| # | Tarea | Impacto | Esfuerzo | Prioridad |
|---|-------|---------|----------|-----------|
| 05-01 | Management API — Sesiones activas | Alto | Bajo | P1 |
| 05-02 | Management API — Perfil usuario ampliado | Alto | Bajo | P1 |
| 05-03 | Sistema de Invitaciones | Alto | Medio | P2 |
| 05-04 | Organizaciones y Grupos jerárquicos | Alto | Alto | P3 |
| 05-05 | Permisos a nivel de recurso — ABAC | Alto | Alto | P3 |
| 05-06 | API Keys para usuarios (Personal Access Tokens) | Alto | Medio | P2 |

---

## Dependencias internas

```
02-04 (Token Revocation) → 05-01 (Sesiones: revocar sesión = revocar tokens)
05-02 (Perfil) → 07-01 (GDPR Exportación incluye perfil)
05-03 (Invitaciones) + 05-06 (PAT) → 05-04 (Organizaciones)
05-04 (Organizaciones) → 05-05 (ABAC)
```

---

## Notas de arquitectura

- Todas las rutas bajo `/api/access/` requieren scope `access:admin` o `access:manage`
- El `tenant_id` se extrae del token Bearer, no de la URL (seguridad)
- Cada use case es nuevo sub-feature en `features/oauth/{feature}/`
- La paginación es cursor-based para todas las colecciones

---

## Criterios de aceptación de fase

- [ ] `/api/access/users/{uid}/sessions` lista y permite revocar sesiones individuales
- [ ] `/api/access/users/{uid}/profile` permite leer y actualizar perfil OIDC completo
- [ ] `/api/access/invitations` crea y gestiona invitaciones por email
- [ ] `/api/access/organizations` CRUD con jerarquía padre-hijo
- [ ] `/api/access/policies/evaluate` evalúa permisos ABAC
- [ ] `/api/me/api-keys` CRUD de Personal Access Tokens
- [ ] Todos los endpoints documentados con OpenAPI
