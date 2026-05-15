# Tarea 05-04 — Organizaciones y Grupos jerárquicos

> **Fase:** 05 — API de Gestión BaaS
> **Prioridad:** P3
> **Esfuerzo estimado:** Alto
> **Prerequisito:** 05-03 (Invitaciones), 05-06 (Personal Access Tokens)

---

## Descripción

Permite estructurar los usuarios de un tenant en organizaciones con jerarquía
padre-hijo. Los permisos pueden heredarse de la organización padre. El `org_id`
se incluye en el JWT si el scope `organizations` es concedido.

---

## Pasos de implementación

### 1. Tablas

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-organization-table
CREATE TABLE access_organization (
  uid        VARCHAR(36)  NOT NULL,
  tenant_id  VARCHAR(36)  NOT NULL,
  name       VARCHAR(255) NOT NULL,
  slug       VARCHAR(100) NOT NULL,
  parent_uid VARCHAR(36)  NULL,
  metadata   TEXT         NULL COMMENT 'JSON de metadatos adicionales',
  created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT PK_ACCESS_ORGANIZATION PRIMARY KEY (uid),
  UNIQUE KEY uq_org_slug_tenant (tenant_id, slug),
  INDEX idx_org_tenant (tenant_id),
  INDEX idx_org_parent (parent_uid)
);

-- changeset phylax-dev:create-organization-member-table
CREATE TABLE access_organization_member (
  org_uid   VARCHAR(36) NOT NULL,
  user_uid  VARCHAR(36) NOT NULL,
  role_uid  VARCHAR(36) NULL,
  joined_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (org_uid, user_uid),
  INDEX idx_org_member_user (user_uid)
);
```

### 2. Value object `Organization`

```java
public record Organization(
    UUID uid,
    String tenantId,
    String name,
    String slug,
    Optional<UUID> parentUid,
    Map<String, Object> metadata,
    Instant createdAt
) {}

public record OrganizationMember(
    UUID orgUid,
    UUID userUid,
    Optional<UUID> roleUid,
    Instant joinedAt
) {}
```

### 3. Port de salida — `OrganizationGateway`

```java
public interface OrganizationGateway {
    Organization save(Organization org);
    Optional<Organization> findByUid(UUID uid, String tenantId);
    Optional<Organization> findBySlug(String slug, String tenantId);
    List<Organization> findChildren(UUID parentUid, String tenantId);
    void addMember(OrganizationMember member);
    void removeMember(UUID orgUid, UUID userUid);
    List<Organization> findByMember(UUID userUid, String tenantId);
    boolean isMember(UUID orgUid, UUID userUid);
}
```

### 4. Herencia de permisos

Al evaluar si un usuario tiene permiso en una organización,
recorrer el árbol hasta la raíz:

```java
public boolean hasPermissionInOrg(UUID userUid, UUID orgUid, String permission) {
    // BFS/DFS por el árbol hacia arriba
    Optional<Organization> current = orgGateway.findByUid(orgUid, tenantId);
    while (current.isPresent()) {
        if (isMemberWithPermission(userUid, current.get().uid(), permission)) {
            return true;
        }
        current = current.get().parentUid()
            .flatMap(pid -> orgGateway.findByUid(pid, tenantId));
    }
    return false;
}
```

### 5. Claim `org_id` en el JWT

Si el scope `organizations` está concedido y el usuario pertenece
a organizaciones, incluir en el access token:

```json
{
  "sub": "user-uuid",
  "org_id": "org-uuid",
  "org_name": "Mi Empresa",
  "orgs": ["org-uuid-1", "org-uuid-2"]
}
```

La organización activa puede elegirse con parámetro `organization`
en el authorize request.

### 6. Endpoints CRUD

```
POST   /api/access/organizations              # Crear organización
GET    /api/access/organizations              # Listar (árbol del tenant)
GET    /api/access/organizations/{uid}        # Detalle
PUT    /api/access/organizations/{uid}        # Actualizar nombre/metadata
DELETE /api/access/organizations/{uid}        # Eliminar (solo si no tiene hijos)

POST   /api/access/organizations/{uid}/members           # Añadir miembro
DELETE /api/access/organizations/{uid}/members/{userUid} # Eliminar miembro
GET    /api/access/organizations/{uid}/members           # Listar miembros
```

### 7. Tests de integración

- Crear organización raíz → 201 ✓
- Crear organización hija → devuelve `parent_uid` ✓
- Añadir miembro → aparece en `GET /members` ✓
- Token con scope `organizations` incluye `org_id` ✓
- Eliminar organización con hijos → 409 Conflict ✗
- Herencia: miembro del padre tiene acceso en hijo ✓

---

## Criterios de aceptación

- [ ] Tablas `access_organization` y `access_organization_member` con migraciones
- [ ] CRUD completo de organizaciones
- [ ] Gestión de miembros (añadir/eliminar/listar)
- [ ] Herencia de permisos por árbol hacia la raíz
- [ ] Claim `org_id` en JWT cuando scope `organizations` está concedido
- [ ] Prevención de eliminación de orgs con hijos activos
- [ ] 6 tests de integración
