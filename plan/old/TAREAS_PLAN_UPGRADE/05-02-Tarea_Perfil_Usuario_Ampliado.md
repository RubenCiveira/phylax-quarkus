# Tarea 05-02 — Management API — Perfil de usuario ampliado

> **Fase:** 05 — API de Gestión BaaS
> **Prioridad:** P1
> **Esfuerzo estimado:** Bajo
> **Prerequisito:** Ninguno específico

---

## Descripción

El usuario actual solo almacena `email`, `password`, `mfa_seed` y `temporal_password`.
Los claims estándar de OIDC Core (`given_name`, `family_name`, `picture`, etc.) no están
modelados, por lo que el endpoint `userinfo` no puede devolverlos aunque el scope
`profile` sea solicitado.

---

## Cambios necesarios

### 1. Tabla `access_user_profile`

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-user-profile-table
CREATE TABLE access_user_profile (
  user_uid       VARCHAR(36)   NOT NULL,
  tenant_id      VARCHAR(36)   NOT NULL,
  given_name     VARCHAR(255)  NULL,
  family_name    VARCHAR(255)  NULL,
  display_name   VARCHAR(255)  NULL,
  phone_number   VARCHAR(50)   NULL,
  phone_verified TINYINT(1)    DEFAULT 0,
  picture_url    TEXT          NULL,
  locale         VARCHAR(10)   NULL COMMENT 'BCP 47, e.g. es-ES',
  zoneinfo       VARCHAR(100)  NULL COMMENT 'IANA tz, e.g. Europe/Madrid',
  updated_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT PK_ACCESS_USER_PROFILE PRIMARY KEY (user_uid),
  INDEX idx_user_profile_tenant (tenant_id)
);
```

### 2. Value object `UserProfile`

```java
public record UserProfile(
    UUID userUid,
    String tenantId,
    Optional<String> givenName,
    Optional<String> familyName,
    Optional<String> displayName,
    Optional<String> phoneNumber,
    boolean phoneVerified,
    Optional<String> pictureUrl,
    Optional<String> locale,
    Optional<String> zoneinfo
) {
    public String fullName() {
        return Stream.of(givenName, familyName)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.joining(" "));
    }
}
```

### 3. Port de salida — `UserProfileGateway`

```java
public interface UserProfileGateway {
    Optional<UserProfile> findByUserUid(UUID userUid, String tenantId);
    void save(UserProfile profile);
}
```

### 4. Actualizar el endpoint `userinfo`

En el controller existente `GET /openid/{tenant}/userinfo`:

```java
// Si scope contiene 'profile':
if (scopes.contains("profile")) {
    userProfileGateway.findByUserUid(userId, tenantId)
        .ifPresent(profile -> {
            profile.givenName().ifPresent(v  -> claims.put("given_name", v));
            profile.familyName().ifPresent(v -> claims.put("family_name", v));
            profile.pictureUrl().ifPresent(v -> claims.put("picture", v));
            profile.locale().ifPresent(v     -> claims.put("locale", v));
            profile.zoneinfo().ifPresent(v   -> claims.put("zoneinfo", v));
        });
}

// Si scope contiene 'phone':
if (scopes.contains("phone")) {
    userProfileGateway.findByUserUid(userId, tenantId)
        .ifPresent(profile -> {
            profile.phoneNumber().ifPresent(v -> claims.put("phone_number", v));
            claims.put("phone_number_verified", profile.phoneVerified());
        });
}
```

### 5. Endpoints de gestión de perfil

```
GET  /api/access/users/{uid}/profile    # Leer perfil completo
PUT  /api/access/users/{uid}/profile    # Actualizar perfil
GET  /api/me/profile                    # Perfil del usuario autenticado
PUT  /api/me/profile                    # Actualizar propio perfil
```

**Request body (PUT):**
```json
{
  "given_name": "Rubén",
  "family_name": "Civeira",
  "display_name": "Rubén C.",
  "phone_number": "+34600000000",
  "picture_url": "https://cdn.example.com/avatar/uuid.jpg",
  "locale": "es-ES",
  "zoneinfo": "Europe/Madrid"
}
```

### 6. Integración en el registro de usuario

Al registrar un nuevo usuario mediante el authorize flow, crear una fila
vacía en `access_user_profile` para garantizar integridad referencial.

### 7. Tests de integración

- `userinfo` con scope `profile` devuelve `given_name`, `family_name` ✓
- `userinfo` sin scope `profile` no devuelve claims de perfil ✓
- `PUT /api/me/profile` actualiza y `GET /api/me/profile` refleja el cambio ✓
- `PUT /api/access/users/{uid}/profile` con token de admin funciona ✓
- `PUT /api/access/users/{uid}/profile` con token de otro usuario → 403 ✗

---

## Criterios de aceptación

- [ ] Tabla `access_user_profile` con migración Liquibase
- [ ] `userinfo` devuelve claims OIDC estándar del scope `profile` y `phone`
- [ ] `GET /PUT /api/me/profile` para el propio usuario
- [ ] `GET /PUT /api/access/users/{uid}/profile` para admins
- [ ] Fila en `access_user_profile` creada al registrar usuario
- [ ] 5 tests de integración
