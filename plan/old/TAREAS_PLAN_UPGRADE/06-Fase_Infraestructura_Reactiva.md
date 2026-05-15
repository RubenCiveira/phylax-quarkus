# Fase 06 — Infraestructura Reactiva y Extensibilidad

> **Prioridad:** P2-P4
> **Prerequisito:** Fase 02 (eventos de token), Fase 05 (eventos de usuario/sesión)
> **Fecha objetivo:** Sprint 9-12

---

## Descripción

Esta fase añade la infraestructura que permite a las aplicaciones cliente
reaccionar a eventos del servidor en tiempo real, configurar comportamientos
por tenant sin despliegues, y acceder al audit trail desde sus propias
aplicaciones.

Convierte el servidor en una plataforma reactiva y extensible, no solo
un proveedor de identidad pasivo.

---

## Tareas

| # | Tarea | Impacto | Esfuerzo | Prioridad |
|---|-------|---------|----------|-----------|
| 06-01 | Sistema de Webhooks | Alto | Medio | P2 |
| 06-02 | Event Streaming — SSE con Mutiny | Bajo | Alto | P4 |
| 06-03 | Feature Flags por Tenant | Medio | Medio | P3 |
| 06-04 | Audit Log API pública | Medio | Bajo | P3 |

---

## Dependencias internas

```
06-01 (Webhooks) ──→ 06-02 (Event Streaming — ambos consumen el mismo bus de eventos)
06-04 (Audit Log API) es independiente — puede hacerse antes si se necesita
06-03 (Feature Flags) es completamente independiente
```

---

## Notas de arquitectura

- Los webhooks y el streaming SSE deben consumir el mismo bus de eventos CDI
  para garantizar consistencia. No duplicar la lógica de publicación.
- Usar `Quarkus Vert.x Event Bus` o CDI `Event<T>` asíncronos para desacoplar
  el dominio del dispatcher.
- Los Feature Flags deben ser leídos con caché local (Infinispan o caffeine)
  para evitar queries a DB en cada request.

---

## Criterios de aceptación de fase

- [ ] Webhooks: eventos `user.*`, `token.*`, `session.*` entregados con firma HMAC
- [ ] Webhooks: reintentos exponenciales en fallo
- [ ] SSE: `GET /api/stream/events` emite eventos en tiempo real
- [ ] Feature Flags: `GET /api/flags?keys=...` por tenant con caché
- [ ] Audit Log API: `GET /api/access/audit-log` con filtros y paginación cursor
- [ ] Tests de integración para cada feature
