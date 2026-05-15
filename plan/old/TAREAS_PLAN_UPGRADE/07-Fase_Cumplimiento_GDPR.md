# Fase 07 — Cumplimiento GDPR / CCPA

> **Prioridad:** P3
> **Prerequisito:** Fase 05 completada (perfil ampliado, sesiones, invitaciones)
> **Fecha objetivo:** Sprint 10-12

---

## Descripción

Esta fase implementa los requisitos legales del Reglamento General de
Protección de Datos (GDPR) y California Consumer Privacy Act (CCPA).
Son especialmente importantes si el servidor gestiona datos de ciudadanos
europeos o californianos.

**Riesgo legal de no implementar:** multas de hasta el 4% del volumen de
negocio anual global (GDPR Art. 83).

---

## Tareas

| # | Tarea | Artículo GDPR | Esfuerzo |
|---|-------|--------------|---------|
| 07-01 | Exportación de datos de usuario | Art. 15 / Art. 20 | Medio |
| 07-02 | Derecho al olvido | Art. 17 | Medio |
| 07-03 | Gestión de consentimientos | Art. 7 | Medio |

---

## Dependencias internas

```
05-02 (Perfil ampliado)   → 07-01 (Exportación incluye perfil)
05-06 (PAT)               → 07-01 (Exportación incluye API keys)
02-03 (Scope Consent)     → 07-03 (Gestión de consentimientos es extensión)
```

---

## Notas legales / técnicas

- La exportación debe entregarse en **formato portable** (JSON o ZIP)
- El borrado de datos puede ser **anonimización** en lugar de borrado físico
  cuando los datos son necesarios para el audit trail
- Los consentimientos deben registrar la **versión** del texto aceptado
- Conservar registros de solicitudes de borrado por motivos de auditoría

---

## Criterios de aceptación de fase

- [ ] `POST /api/me/data-export` genera un archivo ZIP con todos los datos del usuario
- [ ] `DELETE /api/me/account` inicia el flujo de borrado con verificación por email
- [ ] El borrado anonimiza en lugar de borrar donde hay requisito de auditoría
- [ ] `GET/PUT /api/me/consents` gestiona consentimientos de procesamiento
- [ ] Todos los endpoints documentados con OpenAPI
- [ ] Tests de integración para cada flujo
