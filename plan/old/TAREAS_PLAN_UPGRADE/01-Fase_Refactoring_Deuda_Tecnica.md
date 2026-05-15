# Fase 01 — Refactoring y Deuda Técnica

> **Prioridad:** P0 — Prerequisito bloqueante para el resto del roadmap
> **Estado:** Done
> **Fecha objetivo:** Sprint actual

---

## Descripción

Esta fase agrupa las tareas de refactoring y deuda técnica que son prerequisito
para implementar correctamente las fases siguientes. Deben completarse **antes**
de cualquier nueva feature para evitar propagar los problemas actuales.

---

## Tareas

| # | Tarea | Impacto | Esfuerzo |
|---|-------|---------|----------|
| 01-01 | Renombrar `autenticate` → `authenticate` | Calidad / DX | Medio |
| 01-02 | Eliminar `AuthorizedChallenges` legacy y conversiones `toLegacy()` | Mantenibilidad | Medio |
| 01-03 | Colapsar capa Gateway → Repository | Complejidad | Alto |
| 01-04 | Corregir orden de parámetros en `UserMfa.storeSeed()` | Corrección | Bajo |
| 01-05 | Añadir `jti` a todos los JWT emitidos | Seguridad | Bajo |
| 01-06 | Cleanup jobs para códigos y sesiones expirados | Performance | Bajo |

---

## Dependencias de salida

- **Fase 02** (PKCE, Introspection, Token Revocation) requiere `jti` en JWT (01-05)
- **Fase 02** (Token Revocation, Introspection) requiere Gateway→Repository estabilizado (01-03)
- Todos los tests de integración OIDC dependen de que el typo `autenticate` esté corregido (01-01)

---

## Criterios de aceptación de fase

- [ ] Cero referencias a `autenticate` (con typo) en código fuente y tests
- [ ] Cero referencias a `AuthorizedChallenges` ni `toLegacy()` en código de producción
- [ ] Todos los JWT emitidos contienen el claim `jti`
- [ ] Quartz/Scheduler ejecuta cleanup de `_oauth_temporal_codes` y `_oauth_sessions` expirados
- [ ] Todos los tests de integración OIDC pasan (`mvn test -Dgroups="oidc-flow"`)
