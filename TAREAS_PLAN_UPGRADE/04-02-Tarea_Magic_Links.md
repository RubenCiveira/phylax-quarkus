# Tarea 04-02 — Magic Links / Passwordless Email Login

> **Fase:** 04 — Autenticación Avanzada
> **Prioridad:** P2
> **Esfuerzo estimado:** Bajo
> **Prerequisito:** Sistema de notificaciones existente (`features/notification`)

---

## Descripción

El usuario recibe un enlace de un solo uso por email que lo autentica
directamente sin contraseña. Útil como método alternativo o como fallback
cuando el usuario olvidó su contraseña pero no quiere usar el flujo de
recuperación completo.

La infraestructura de `_security_magic_links` ya existe en la DB.
Esta tarea la integra con el authorize flow OIDC.

---

## Estado actual

La tabla `_security_magic_links` ya existe con columnas:
`token`, `path`, `jwt_token`, `actor_json`, `source_json`,
`current_reads`, `max_reads`, `expiration`.

---

## Pasos de implementación

### 1. Endpoint de solicitud del magic link

```
POST /openid/{tenant}/magic-link/request
Content-Type: application/x-www-form-urlencoded

email=usuario@example.com&redirect_uri=https://app.example.com/callback&client_id=xxx
```

**Use case `RequestMagicLinkUseCase`:**
1. Verificar que el email corresponde a un usuario activo en el tenant
2. Generar un token seguro: `UUID.randomUUID()` + firma HMAC del `tenant_id`
3. Calcular TTL: 15 minutos
4. Persistir en `_security_magic_links` con `max_reads=1`
5. Almacenar `{ tenant, client_id, redirect_uri, scope, state }` como `source_json`
6. Enviar email via `features/notification/outbox`
7. Devolver 200 sin revelar si el email existe o no (anti-enumeration)

### 2. Endpoint de verificación

```
GET /openid/{tenant}/magic-link/verify?token=xxx
```

**Use case `VerifyMagicLinkUseCase`:**
1. Buscar el token en `_security_magic_links`
2. Verificar que no ha expirado y `current_reads < max_reads`
3. Incrementar `current_reads` (uso atómico para evitar race conditions)
4. Extraer `{ tenant, client_id, redirect_uri, scope, state }` de `source_json`
5. Crear sesión OIDC autenticada para el usuario
6. Redirigir al `redirect_uri` con el authorization code (igual que authorize normal)

### 3. Plantilla de email

Crear plantilla en el sistema de notificaciones existente:
`src/main/resources/templates/email/magic-link.html` (Qute template)

```html
<p>Haz clic en el enlace para acceder a tu cuenta:</p>
<a href="{magicLinkUrl}">Acceder sin contraseña</a>
<p>Este enlace expira en 15 minutos y solo puede usarse una vez.</p>
```

### 4. Configurar por tenant

```sql
-- changeset phylax-dev:magic-link-tenant-config
ALTER TABLE access_tenant_config
  ADD COLUMN magic_link_enabled   TINYINT(1) DEFAULT 0,
  ADD COLUMN magic_link_ttl_min   INT DEFAULT 15;
```

### 5. Integración en la UI del authorize

Añadir en la página de login un enlace "Acceder por email":

```html
<!-- En la plantilla login.html (Qute) -->
{#if tenant.magicLinkEnabled}
<a href="/openid/{tenant}/magic-link/request-form">
  Acceder sin contraseña →
</a>
{/if}
```

### 6. Tests de integración

- Solicitar magic link → email enviado (verificar outbox) ✓
- Verificar token válido → sesión OIDC creada, redirect con code ✓
- Verificar token expirado → error ✗
- Verificar token usado dos veces → error en el segundo intento ✗
- Email no registrado → 200 igualmente (anti-enumeration) ✓

---

## Criterios de aceptación

- [ ] `POST /openid/{tenant}/magic-link/request` envía email con enlace
- [ ] `GET /openid/{tenant}/magic-link/verify?token=xxx` crea sesión OIDC
- [ ] Token de un solo uso (segunda verificación falla)
- [ ] TTL de 15 minutos configurable por tenant
- [ ] Feature configurable por tenant (habilitado/deshabilitado)
- [ ] Anti-enumeration: siempre 200 aunque el email no exista
- [ ] 5 tests de integración
