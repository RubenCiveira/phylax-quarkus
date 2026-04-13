# Tarea 03-02 — Dynamic Client Registration (RFC 7591 / RFC 7592)

> **Fase:** 03 — Extensiones OAuth 2.0
> **RFC:** 7591 (registro), 7592 (gestión)
> **Prioridad:** P3
> **Esfuerzo estimado:** Medio
> **Prerequisito:** Ninguno específico

---

## Descripción

Permite que los Relying Parties se registren programáticamente sin intervención
del administrador. Especialmente útil en plataformas multi-tenant donde cada
cliente configura su propia integración de forma autoservicio.

---

## Endpoints

```
POST   /openid/{tenant}/register           # Registrar nuevo cliente (RFC 7591)
GET    /openid/{tenant}/register/{uid}     # Leer configuración (RFC 7592)
PUT    /openid/{tenant}/register/{uid}     # Actualizar cliente (RFC 7592)
DELETE /openid/{tenant}/register/{uid}     # Eliminar cliente (RFC 7592)
```

---

## Pasos de implementación

### 1. Política de acceso (configurable por tenant)

Tres modos:
- `open`: cualquiera puede registrar (no recomendado en producción)
- `token`: requiere un `initial_access_token` emitido por el admin
- `admin_only`: solo admins pueden crear clientes (registro dinámico deshabilitado)

```sql
-- changeset phylax-dev:add-dynamic-registration-config
ALTER TABLE access_tenant
  ADD COLUMN dynamic_client_registration VARCHAR(20) DEFAULT 'admin_only',
  ADD COLUMN initial_access_token_hash   VARCHAR(64) NULL;
```

### 2. Endpoint `POST /openid/{tenant}/register`

**Request body (JSON):**
```json
{
  "client_name": "My SPA",
  "redirect_uris": ["https://app.example.com/callback"],
  "grant_types": ["authorization_code", "refresh_token"],
  "response_types": ["code"],
  "token_endpoint_auth_method": "none",
  "scope": "openid email profile",
  "logo_uri": "https://app.example.com/logo.png",
  "backchannel_logout_uri": "https://app.example.com/logout"
}
```

**Response (201 Created):**
```json
{
  "client_id": "uuid-generado",
  "client_secret": "secreto-generado-una-sola-vez",
  "client_id_issued_at": 1234567890,
  "client_secret_expires_at": 0,
  "registration_access_token": "token-para-gestionar-este-cliente",
  "registration_client_uri": "https://auth.example.com/openid/tenant/register/uuid-generado"
}
```

### 3. Implementación del use case `RegisterClientUseCase`

```java
public class RegisterClientUseCase {
    public ClientRegistrationResult execute(ClientRegistrationRequest request, String tenantSlug) {
        // 1. Verificar política de tenant
        // 2. Validar redirect_uris (no wildcard para code flow)
        // 3. Validar grant_types contra response_types
        // 4. Crear RelyingParty con UUID nuevo
        // 5. Generar client_secret (si no es public client)
        //    - Almacenar solo el hash BCrypt
        // 6. Generar registration_access_token (para RFC 7592)
        //    - Almacenar solo el hash
        // 7. Devolver incluyendo client_secret en texto claro (única vez)
    }
}
```

### 4. Endpoints RFC 7592 — `GET/PUT/DELETE`

Autenticados con el `registration_access_token` devuelto al registrar:
```
Authorization: Bearer <registration_access_token>
```

- `GET` → devuelve la configuración del cliente (sin `client_secret`)
- `PUT` → actualiza (mismo esquema que POST, sin cambiar `client_id`)
- `DELETE` → elimina el cliente y revoca todos sus tokens activos

### 5. Validaciones de seguridad

- `redirect_uris` no deben contener fragmentos (`#`)
- `redirect_uris` con `localhost` solo permitidos en modo dev
- `client_credentials` grant no permite `redirect_uris`
- Límite de clientes por tenant configurable

### 6. Tests de integración

- Registro con política `open` → 201 con client_id y secret ✓
- Registro con política `token` sin token → 401 ✗
- Registro con `token_endpoint_auth_method: none` → no se genera secret ✓
- GET → devuelve configuración sin secret ✓
- PUT → actualiza `redirect_uris` ✓
- DELETE → cliente eliminado, tokens revocados ✓

---

## Criterios de aceptación

- [ ] 4 endpoints RFC 7591/7592 funcionando
- [ ] Política de registro configurable por tenant (`open`, `token`, `admin_only`)
- [ ] `client_secret` almacenado como hash BCrypt, devuelto en claro solo al registrar
- [ ] `registration_access_token` requerido para GET/PUT/DELETE
- [ ] Validaciones de `redirect_uris` (no fragmentos, no wildcards)
- [ ] 6 tests de integración
