# Tarea 05-03 — Sistema de Invitaciones

> **Fase:** 05 — API de Gestión BaaS
> **Prioridad:** P2
> **Esfuerzo estimado:** Medio
> **Prerequisito:** Sistema de notificaciones existente

---

## Descripción

Permite invitar usuarios a un tenant sin que tengan cuenta previa.
El usuario recibe un email con un enlace para activar su cuenta y
unirse al tenant con un rol predefinido. Feature base de cualquier BaaS.

---

## Pasos de implementación

### 1. Tabla `access_user_invitation`

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-user-invitation-table
CREATE TABLE access_user_invitation (
  uid          VARCHAR(36)  NOT NULL,
  tenant_id    VARCHAR(36)  NOT NULL,
  email        VARCHAR(255) NOT NULL,
  role_uid     VARCHAR(36)  NULL,
  invited_by   VARCHAR(36)  NOT NULL COMMENT 'user_uid del que invitó',
  token_hash   VARCHAR(64)  NOT NULL COMMENT 'SHA-256 del token de aceptación',
  expires_at   TIMESTAMP    NOT NULL,
  accepted_at  TIMESTAMP    NULL,
  cancelled_at TIMESTAMP    NULL,
  created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT PK_ACCESS_USER_INVITATION PRIMARY KEY (uid),
  INDEX idx_invitation_tenant_email (tenant_id, email),
  INDEX idx_invitation_token (token_hash)
);
```

### 2. Dominio — `UserInvitation` y eventos

```java
public record UserInvitation(
    UUID uid,
    String tenantId,
    String email,
    Optional<UUID> roleUid,
    UUID invitedBy,
    Instant expiresAt,
    Optional<Instant> acceptedAt,
    Optional<Instant> cancelledAt
) {
    public boolean isPending() {
        return acceptedAt.isEmpty()
            && cancelledAt.isEmpty()
            && Instant.now().isBefore(expiresAt);
    }
}

public record InvitationSentEvent(UUID invitationUid, String email, String tenantId) {}
public record InvitationAcceptedEvent(UUID invitationUid, UUID newUserUid) {}
```

### 3. Port de salida — `InvitationGateway`

```java
public interface InvitationGateway {
    UserInvitation save(UserInvitation invitation);
    Optional<UserInvitation> findByToken(String tokenHash);
    List<UserInvitation> findPendingByTenant(String tenantId, Pageable pageable);
    void cancel(UUID invitationUid);
    void markAccepted(UUID invitationUid, UUID acceptingUserUid);
}
```

### 4. Use cases

**`CreateInvitationUseCase`:**
1. Verificar que el solicitante tiene permiso de admin en el tenant
2. Verificar que no hay invitación pendiente para ese email en el tenant
3. Generar token: `UUID.randomUUID()` (256 bits de entropía)
4. Persistir hash SHA-256 del token (nunca el token en claro)
5. TTL: 7 días configurable por tenant
6. Enviar email via `notification/outbox` con el enlace de aceptación
7. Publicar `InvitationSentEvent`

**`AcceptInvitationUseCase`:**
1. Buscar la invitación por hash del token
2. Verificar que está pendiente y no ha expirado
3. Si el usuario ya existe en el sistema (otro tenant) → añadir al tenant con el rol
4. Si el usuario no existe → crear nuevo usuario con el email + contraseña que elija
5. Marcar invitación como aceptada
6. Crear sesión OIDC → redirigir con authorization code
7. Publicar `InvitationAcceptedEvent`

### 5. Endpoints

```java
@Path("/api/access/invitations")
@Tag(name = "Invitations")
public class InvitationController {

    @POST
    @Operation(summary = "Create and send an invitation")
    public Response createInvitation(CreateInvitationRequest request) { ... }
    // Body: { email, role_uid?, expires_in_days? }

    @GET
    @Operation(summary = "List pending invitations")
    public CursorPage<InvitationDto> listInvitations(
        @QueryParam("cursor") String cursor,
        @QueryParam("limit") @DefaultValue("20") int limit
    ) { ... }

    @DELETE
    @Path("/{uid}")
    @Operation(summary = "Cancel an invitation")
    public Response cancel(@PathParam("uid") UUID uid) { ... }
}

// Endpoint público (sin auth) para aceptar
@Path("/openid/{tenant}/invitation/accept")
public class InvitationAcceptController {

    @GET
    public Response showAcceptForm(@QueryParam("token") String token) { ... }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response acceptInvitation(
        @FormParam("token") String token,
        @FormParam("password") String password,
        @FormParam("given_name") String givenName
    ) { ... }
}
```

### 6. Plantilla de email de invitación

Crear en el sistema de notificaciones:
- Template: `email/invitation.html`
- Variables: `inviterName`, `tenantName`, `acceptUrl`, `expiresAt`

### 7. Tests de integración

- Crear invitación → email enviado con token ✓
- Aceptar invitación (usuario nuevo) → cuenta creada, sesión OIDC ✓
- Aceptar invitación (usuario existente) → añadido al tenant ✓
- Token expirado → error en aceptación ✗
- Cancelar invitación → aceptar falla ✗
- Listar invitaciones → solo las pendientes del tenant ✓

---

## Criterios de aceptación

- [ ] Tabla `access_user_invitation` con migración
- [ ] `POST /api/access/invitations` crea invitación y envía email
- [ ] `GET /api/access/invitations` lista invitaciones pendientes (paginado)
- [ ] `DELETE /api/access/invitations/{uid}` cancela la invitación
- [ ] `GET/POST /openid/{tenant}/invitation/accept` acepta y crea sesión OIDC
- [ ] Token almacenado como hash SHA-256 (nunca en claro)
- [ ] TTL de 7 días configurable por tenant
- [ ] 6 tests de integración
