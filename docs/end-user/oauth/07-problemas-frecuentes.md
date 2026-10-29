# Problemas frecuentes

## Login

| Síntoma | Causa probable | Solución |
|---|---|---|
| Página de error del servidor en lugar de la pantalla de login | La `redirect_uri` no coincide exactamente con una registrada, o el cliente no existe, está deshabilitado o es de otro tenant | Revisar `allowed-login-redirects` (barra final, puerto, query) y que el cliente esté habilitado en ese tenant |
| La aplicación recibe `unauthorized_client` | El `response_type` o el grant no está registrado en el cliente | Revisar `grant-types-json` y `response-types-json` |
| La aplicación recibe `invalid_target` | Pide una `audience` que no está en `allowed-audiences` | Añadir el código del servidor de recursos al cliente |
| La aplicación recibe `invalid_scope` | Pide un scope no estándar sin `allow-all-scopes`, o `client_credentials` sin `client-credentials-scopes` | Marcar `allow-all-scopes` o rellenar los scopes de servicio |
| La aplicación recibe `invalid_request` mencionando PKCE | Cliente público sin `code_challenge` | La aplicación debe usar PKCE con S256 |
| Nadie puede entrar en un tenant | Tenant deshabilitado | Acción *enable* en el tenant |
| Un usuario concreto no puede entrar con la contraseña correcta | Cuenta deshabilitada, o bloqueada tras intentos fallidos | *enable* o *unlock* en la ficha del usuario |
| El usuario tiene que volver a poner la contraseña muy a menudo | `session-sso-ttl-seconds` bajo | Subirlo en la configuración del tenant |
| La aplicación pierde la sesión cada pocos minutos | El tenant no tiene configuración y los refresh tokens duran lo mismo que el access token | Crear la configuración del tenant |
| El login no funciona en un navegador automatizado o sin JavaScript | El formulario cifra la contraseña con JavaScript | Usar un navegador con JavaScript |

## Login federado

El motivo del rechazo queda en el log del servidor: `Delegated login refused for tenant {tenant} and provider {nombre}: {motivo}`.

| Motivo o síntoma | Causa | Solución |
|---|---|---|
| El botón del proveedor no aparece | Proveedor deshabilitado, de un tipo no implementado (`GITHUB`, `MICROSOFT`, `APPLE`, `OIDC`) o SAML sin entity ID, URL de SSO o certificado | Habilitarlo y completar los campos; usar `GOOGLE` o `SAML` |
| `redirect_uri_mismatch` en Google | La URL de retorno registrada en Google no coincide | Registrar exactamente `https://{host}/oauth/openid/-/delegated/authorization/{nombre}` |
| «Invalid or expired login» | Pasaron más de 10 minutos, se volvió atrás en el navegador o el nombre del proveedor no coincide con la URL | Empezar el login de nuevo |
| `unverified_email` | El proveedor no afirma un email verificado | Verificar el email en el proveedor, o en SAML enviar el atributo de email |
| `account_link_not_allowed` | La política del proveedor no permite enlazar esa cuenta (`manual`, o `none` con una cuenta local ajena) | Crear el enlace a mano o cambiar la política |
| `missing_subject` | La respuesta no trae identificador estable | En SAML, configurar un NameID persistente |
| Entra pero no tiene permisos | Cuenta nueva sin roles, o creada deshabilitada | Habilitarla y asignarle roles |
| SAML: falla tras meses funcionando | El IdP rotó su certificado | Actualizar `saml-idp-cert` |

## Tokens y APIs

| Síntoma | Causa | Solución |
|---|---|---|
| La API responde 401 `invalid_token` | El token no lleva su audiencia, es de otro tenant, caducó o fue revocado | Que el cliente pida `audience={código}`; revisar `iss` y `exp` |
| La API de la aplicación anfitriona rechaza todos los tokens | `mp.jwt.verify.audiences` o `mp.jwt.audiences` no coinciden con el código de su servidor de recursos | Fijar las dos con ese código (ver [Instalación](../../operations/oauth/01-instalacion.md#ajustes-de-la-aplicación)) |
| El token no trae los roles esperados | El rol está asignado a otro servidor o cliente, o el token no lleva esa audiencia | Revisar la asignación (ver [Roles](06-usuarios-roles-e-invitaciones.md#roles)); los cambios se ven al renovar el token |
| La API externa no valida la firma | Caché de claves antigua | Volver a descargar `/oauth/jwks` cuando aparezca un `kid` desconocido |
| `invalid_client` en `/token` | Secreto erróneo, método distinto del registrado, o credenciales enviadas a la vez en cabecera y cuerpo | Usar un solo método, el registrado; si se perdió el secreto, poner uno nuevo |
| `/introspect` responde 401 | El cliente que llama no tiene `introspection-allowed` o es público | Usar un cliente confidencial con `introspection-allowed` |
| Un token pairwise no funciona en la administración | Los clientes `pairwise` no sirven para la API de administración | Usar `subject-type = public` para el frontal de administración |

## Registro dinámico

| Respuesta | Causa |
|---|---|
| 403 | El tenant tiene `dynamic-registration-policy = disabled` |
| 401 `invalid_token` | Initial access token desconocido, caducado, revocado o sin usos |
| 400 `invalid_client_metadata` | Metadatos no admitidos (grant no permitido por el tenant, redirect no HTTPS, `jwks` y `jwks_uri` a la vez…). No gasta el uso del token |
