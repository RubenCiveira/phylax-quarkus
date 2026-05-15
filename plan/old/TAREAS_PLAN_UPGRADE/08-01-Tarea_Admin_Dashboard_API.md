# Tarea 08-01 — Admin Dashboard API

> **Fase:** 08 — Developer Experience
> **Prioridad:** P4
> **Esfuerzo estimado:** Alto
> **Prerequisito:** Fases 02, 05 y 06 completas

---

## Descripción

Endpoints de administración que permiten construir un panel de control completo
para gestionar tenants, usuarios y la configuración del sistema. Orientados
a operadores y equipos de soporte técnico.

---

## Endpoints a implementar

### 1. Estadísticas por tenant

```
GET /api/admin/tenants/{uid}/stats
Authorization: Bearer <admin_token>
```

**Response:**
```json
{
  "active_users": 1523,
  "active_sessions": 342,
  "logins_last_24h": 89,
  "failed_logins_last_24h": 12,
  "tokens_issued_last_24h": 156,
  "top_clients": [
    { "client_id": "...", "client_name": "My App", "sessions": 198 }
  ]
}
```

**Implementación:** Consultas agregadas sobre `_oauth_sessions` y `_audit_action`.
Cachear resultados con TTL de 5 minutos para evitar queries pesadas.

### 2. Búsqueda avanzada de usuarios

```
GET /api/admin/tenants/{uid}/users?q=email:juan&status=active&cursor=xxx&limit=50
Authorization: Bearer <admin_token>
```

**Filtros:**
- `q` — búsqueda por email, nombre (full-text)
- `status` — `active`, `disabled`, `pending_mfa`
- `has_mfa` — `true`/`false`
- `created_after`, `created_before`
- `last_login_after`, `last_login_before`
- `cursor`, `limit` — paginación cursor-based

**Proyección de respuesta:**
```json
{
  "users": [
    {
      "uid": "...",
      "email": "...",
      "given_name": "...",
      "family_name": "...",
      "has_mfa": true,
      "active_sessions": 2,
      "last_login_at": "2026-04-13T09:00:00Z",
      "created_at": "2026-01-15T12:00:00Z"
    }
  ],
  "next_cursor": "...",
  "total_estimate": 1523
}
```

### 3. Impersonación de usuario

```
POST /api/admin/tenants/{uid}/users/{userUid}/impersonate
Authorization: Bearer <admin_token>
```

**Seguridad crítica:**
- Requiere scope `admin:impersonate` (scope especial, no heredado)
- El token emitido incluye el claim `act` (RFC 8693 — Token Exchange):
  ```json
  { "sub": "target-user-uuid", "act": { "sub": "admin-user-uuid" } }
  ```
- La impersonación queda registrada en el audit log con máxima prioridad
- TTL del token de impersonación: máximo 1 hora
- No se emite refresh_token para impersonación

```java
public record ImpersonationResponse(
    String accessToken,
    long expiresIn,
    String actorUid,   // admin que impersona
    String subjectUid  // usuario impersonado
) {}
```

### 4. Estado de salud extendido

```
GET /api/admin/system/health-extended
Authorization: Bearer <admin_token>
```

**Response:**
```json
{
  "database": { "status": "up", "latency_ms": 3 },
  "cache": { "status": "up", "hit_ratio": 0.92 },
  "mail": { "status": "up", "queue_size": 5 },
  "webhook_dispatcher": { "status": "up", "pending_deliveries": 12, "abandoned": 0 },
  "scheduler": { "status": "up", "last_run": "2026-04-13T04:00:00Z" },
  "jwt_keys": { "status": "up", "expires_at": "2026-05-13T00:00:00Z" }
}
```

### 5. Estadísticas de rate limiting

```
GET /api/admin/system/rate-limits?tenant_uid=xxx
Authorization: Bearer <admin_token>
```

**Response:**
```json
{
  "endpoints": [
    {
      "path": "/openid/{tenant}/token",
      "requests_last_minute": 145,
      "throttled_last_minute": 3,
      "top_ips": ["1.2.3.4", "5.6.7.8"]
    }
  ]
}
```

---

## Estructura de paquetes

```
src/main/java/net/civeira/phylax/features/oauth/admin/
├── domain/
│   └── AdminStats.java
├── application/
│   └── usecase/
│       ├── GetTenantStatsUseCase.java
│       ├── SearchUsersUseCase.java
│       └── ImpersonateUserUseCase.java
└── infrastructure/
    └── driver/
        └── rest/
            ├── AdminTenantController.java
            └── AdminSystemController.java
```

---

## Criterios de aceptación

- [ ] `GET /api/admin/tenants/{uid}/stats` devuelve KPIs con caché de 5 minutos
- [ ] `GET /api/admin/tenants/{uid}/users` con búsqueda y filtros
- [ ] `POST /api/admin/.../impersonate` emite token con claim `act` (RFC 8693)
- [ ] Impersonación registrada en audit log
- [ ] `GET /api/admin/system/health-extended` incluye todos los subsistemas
- [ ] `GET /api/admin/system/rate-limits` disponible
- [ ] Scope `admin:impersonate` separado del scope `admin`
- [ ] Todos los endpoints con OpenAPI docs
