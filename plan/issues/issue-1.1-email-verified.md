# Issue 1.1 — `email-verified` wiring into the OIDC flow

**Model section:** 1.1  
**OAUTH_PLAN:** PLAN-10 (Email Verification After Registration)  
**Wave:** 1

## Architectural constraint

> No se crean nuevos casos de uso en `features/access/`. La lógica va en
> `features/oauth/` usando los gateways de Access directamente, o como listener
> `@Observes` sobre eventos del dominio Access.

## Contexto funcional

El campo `email_verified` es necesario solo cuando el email del usuario no ha sido
comprobado previamente. El canal de llegada del usuario determina su estado inicial:

| Canal de registro | `email_verified` al crear | Acción necesaria |
|---|---|---|
| Invitación (link en email) | `true` — el clic en el link prueba el email | Marcar en el listener de aceptación (ver paso 2) |
| Registro por formulario | `false` | Enviar link de verificación + step obligatorio |
| Login social / delegado (Google, SAML, OIDC) | según claim `email_verified` del proveedor | Mapear desde el token externo |
| Creado por admin via API | `false` | Notificar por email o verificación manual (futuro) |

## Current state

- `access_user.email_verified BIT DEFAULT 0` column exists in DB ✅.
- Domain: `EmailVerifiedVO`, `EmailVerifiedValueHolder`, `UserVerifyEmailEvent` ✅.
- `UserWriteRepositoryGateway` + `UserReadRepositoryGateway` inyectables ✅.
- `UserLoginService.userToGrant` ejecuta checks en cadena: `checkPassword` → `checkFirstPass`
  (temporal password) → `checkMfa` → `checkTerms` → `checkScopesConsent`.
- **Falta**: `checkEmailVerification` en esa cadena + step handler + link de verificación.

## What to implement

### 1. Check en `UserLoginService` — igual que `checkFirstPass` para temporal password

`features/oauth/user/infrastructure/UserLoginService.java`

Añadir un nuevo check en la cadena de `userToGrant`, entre `checkFirstPass` y `checkMfa`:

```java
List.of(
    () -> checkPassword(request, user, password),
    () -> checkFirstPass(request, user),
    () -> checkEmailVerification(request, user),   // ← nuevo
    () -> checkMfa(request, user, mode),
    () -> checkTerms(request, user),
    () -> checkScopesConsent(request, user)
)
```

Implementación del check:

```java
private Optional<AuthenticationResult> checkEmailVerification(AuthRequest request, User user) {
    if (!user.isEmailVerified() && tenantConfig.isRequireEmailVerification(request.getTenant())) {
        return Optional.of(
            AuthenticationResult.emailVerificationRequired(request.getTenant(), user.getName()));
    }
    return Optional.empty();
}
```

`AuthenticationResult.emailVerificationRequired(...)` — añadir a `AuthenticationResult` siguiendo
el mismo patrón que `newPasswordRequired`.

### 2. Marcar `email_verified = true` en la aceptación de invitación

`features/oauth/` — listener `@Observes` sobre el evento de aceptación de invitación
(localizar el evento de dominio del BC `userinvitation` que se dispara al aceptar):

```java
@ApplicationScoped
public class InvitationAcceptedObserver {
    @Inject UserWriteRepositoryGateway users;

    void onAccepted(@Observes UserInvitationAcceptEvent event) {
        // El usuario aceptó la invitación → el email queda verificado implícitamente
        String userUid = event.getPayload().getUser().getUid();
        users.find(UserFilter.builder().uid(userUid).build())
            .ifPresent(user -> users.update(user,
                new UserChangeSet().withEmailVerified(true)));
    }
}
```

### 3. Mapear `email_verified` del proveedor externo en login social

`features/oauth/delegatelogin/` — al crear o actualizar el usuario tras un login delegado,
propagar el campo `email_verified` que devuelve el proveedor:

```java
// En el adapter que procesa el callback del proveedor externo:
boolean providerEmailVerified = externalUserInfo.isEmailVerified();  // del ID token del proveedor
users.update(user, new UserChangeSet().withEmailVerified(providerEmailVerified));
```

### 4. Step handler `VERIFY_EMAIL` en el flujo OAuth

`features/oauth/authentication/infrastructure/driver/html/step/VerifyEmailStep.java`

- `GET` → muestra página "Revisa tu bandeja de entrada" con botón "Reenviar email".
- `POST /resend` → genera un nuevo token temporal (via `UserAccessTemporalCodeWriteRepositoryGateway`),
  envía el email de verificación.
- El link de verificación incluye el contexto de sesión OAuth:
  `/{tenant}/oidc/verify-email?token={token}&session={sessionId}`

### 5. Controlador de verificación del link

`features/oauth/authentication/infrastructure/driver/html/EmailVerificationController.java`

```
GET /{tenant}/oidc/verify-email?token={token}&session={sessionId}
```

1. Consume el token temporal via `UserAccessTemporalCodeReadRepositoryGateway`.
2. Actualiza `email_verified = true` via `UserWriteRepositoryGateway`.
3. Si `session` está presente y es válida: redirige de vuelta al flujo OAuth
   (`/{tenant}/oidc/authorize?...` con los parámetros originales) — el check
   `checkEmailVerification` ya no bloqueará porque el flag está a `true`.
4. Si no hay sesión activa: muestra página de confirmación con link al login.

### 6. Emisión del claim `email_verified` en tokens

`features/oauth/tokensecurity/application/JwtTokenBuilder.java`:
- Scope `email` → añadir `email_verified: boolean` en ID token y respuesta userinfo.
- Fuente: `LoginGateway` o `ProfileGateway` — el que ya carga la fila del usuario.

## Dependencies

- `UserAccessTemporalCodeWriteRepositoryGateway` — para generar el token de verificación.
- Evento de aceptación de `userinvitation` BC — para el listener del paso 2.
- Issue 1.5 (tenant config) — `requireEmailVerification` flag.
- PLAN-05 (Userinfo) — consume `email_verified` claim.

## Files to create / modify

| Action | File |
|--------|------|
| **Modify** | `oauth/user/infrastructure/UserLoginService.java` — añadir `checkEmailVerification` en la cadena |
| **Modify** | `oauth/authentication/domain/AuthenticationResult.java` — añadir `emailVerificationRequired` |
| **Create** | `oauth/authentication/infrastructure/driver/html/step/VerifyEmailStep.java` |
| **Create** | `oauth/authentication/infrastructure/driver/html/EmailVerificationController.java` |
| **Create** | `oauth/authentication/application/InvitationAcceptedObserver.java` — marcar verified en invitaciones |
| **Modify** | `oauth/delegatelogin/` — mapear `email_verified` del proveedor externo |
| **Modify** | `oauth/tokensecurity/application/JwtTokenBuilder.java` — claim `email_verified` |
| **Modify** | `messages/oauth.yaml` — añadir claves `email.verification.pending`, `email.verification.resent` |
