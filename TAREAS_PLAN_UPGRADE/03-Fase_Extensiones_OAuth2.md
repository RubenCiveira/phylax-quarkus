# Fase 03 — Extensiones OAuth 2.0 de Alto Valor

> **Prioridad:** P1-P2
> **Prerequisito:** Fase 02 completada (especialmente 02-01 PKCE para PAR)
> **Fecha objetivo:** Sprint 4-5

---

## Descripción

Esta fase añade extensiones OAuth 2.0 que incrementan significativamente
la seguridad y las capacidades del servidor. Son especialmente relevantes
para soportar clientes enterprise y escenarios M2M (Machine-to-Machine).

---

## Tareas

| # | Tarea | RFC | Impacto | Esfuerzo |
|---|-------|-----|---------|----------|
| 03-01 | PAR — Pushed Authorization Requests | RFC 9126 | Medio | Medio |
| 03-02 | Dynamic Client Registration | RFC 7591/7592 | Medio | Medio |
| 03-03 | Client Credentials Grant M2M (completar) | RFC 6749 | Alto | Bajo |
| 03-04 | JAR — JWT Secured Authorization Request | RFC 9101 | Bajo | Medio |

---

## Dependencias internas

```
02-01 (PKCE) ──→ 03-01 (PAR — desplaza el challenge al servidor)
03-03 (Client Credentials) es independiente — puede hacerse antes si se necesita M2M urgente
03-04 (JAR) requiere 03-01 (PAR) para implementación completa
```

---

## Criterios de aceptación de fase

- [ ] `POST /openid/{tenant}/par` devuelve `request_uri` y el authorize lo acepta
- [ ] `POST/GET/PUT/DELETE /openid/{tenant}/register` funcionan con política configurable
- [ ] `grant_type=client_credentials` emite tokens con `sub=client_id` sin usuario
- [ ] Parámetro `request` (JWT firmado) se acepta en `/authorize`
- [ ] Todos los endpoints nuevos documentados en OpenAPI
- [ ] Tests de integración para cada feature
