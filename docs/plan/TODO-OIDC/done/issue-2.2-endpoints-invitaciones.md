# 2.2 — Endpoints de invitaciones

- **Fase:** 2 · **Esfuerzo:** S–M · **Estado:** implementada y verificada (27-10-2026), [J-08](../epicas/done/ep-J-08-login-consentimiento-producto.md).
- **Hallazgo:** los adaptadores eran stubs (no buscaban, cancelaban, reenviaban ni creaban usuario) y la búsqueda por token usaba SHA-256 mientras la entidad guarda el HMAC de `SecretDigester`: ningún enlace habría funcionado.
- **Implementación:** `InvitationController`: `POST api/{tenant}/invitations`, `DELETE api/{tenant}/invitations/{uid}`, `POST api/{tenant}/invitations/{uid}/resend` para un `admin` del tenant o root (`InvitationAdminPolicy`); `GET`/`POST oauth/openid/{tenant}/invitation/accept` con el formulario de contraseña. Adaptadores JDBC: búsqueda por digest y tenant; cancelar/reenviar solo una invitación pendiente del tenant (el reenvío invalida el enlace anterior y usa la caducidad del tenant); aceptación en una transacción con `FOR UPDATE` que crea el usuario (habilitado, email verificado) y marca la invitación. Correo por la cola de notificaciones (plantilla `user.invitation`).
- **Pendiente real:** dar de alta la plantilla `user.invitation`; los `roles` de la invitación no se aplican (los asigna el administrador); el auto-login tras aceptar sigue sin implementar (`UserInvitationSessionAdapter`).
- **Pruebas:** `InvitationMariaDbTest` (tenant, 8 aceptaciones concurrentes → una cuenta, caducada, cuenta existente, cancelación y reenvío), `InvitationAdminPolicyUnitTest`.
- **Fuente:** [§5, 2.2](../CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).
