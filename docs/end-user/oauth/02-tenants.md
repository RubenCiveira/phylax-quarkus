# Tenants

Un tenant es una organización aislada: sus usuarios, su configuración de login y su propio emisor de tokens. Los tokens de un tenant no sirven en otro. Lo crea un administrador root.

## Crear el tenant

Menú de tenants (API `POST /api/access/tenants`).

| Campo | Qué poner | Notas |
|---|---|---|
| **Nombre** (`name`) | Identificador corto en minúsculas, sin espacios: `acme`, `cliente-norte` | Obligatorio y único. **Forma parte de las URLs**: el emisor del tenant es `https://{host}/oauth/openid/{nombre}`. Cambiarlo después invalida los tokens emitidos y obliga a reconfigurar las aplicaciones y los proveedores federados |
| **Dominio** (`domain`) | Sufijo de dominio de la organización: `acme.com` | Obligatorio y único. Hoy no lo usa el login |
| **Raíz** (`root`) | Desmarcado | Solo el tenant raíz de la instalación. Sus usuarios reciben `platform:global_access` |

El tenant nace **deshabilitado**. Mientras lo esté, nadie puede iniciar sesión en él. Para habilitarlo o deshabilitarlo se usan las acciones *enable* / *disable* (`POST /api/access/tenants/{uid}/enable`).

Desde la ficha del tenant cuelgan tres apartados: **configuración**, **condiciones** (términos de uso) y **proveedores** (solo visible para root).

## Configuración del tenant

Un registro de `tenant-config` por tenant (subformulario *config* o `/api/access/tenants-config`). La columna «Por defecto» es el valor con el que nace el registro; si el tenant no tiene configuración, se indica aparte. **Conviene crearla siempre**: sin ella, los refresh tokens duran lo mismo que el access token.

### Sesión y tokens

| Campo | Qué poner | Por defecto |
|---|---|---|
| `session-sso-ttl-seconds` | Cuánto dura la sesión del navegador antes de pedir de nuevo la contraseña | `3600` (1 h); igual sin configuración |
| `refresh-token-ttl-seconds` | Vida de cada refresh token | `2592000` (30 días); sin configuración, **la vida del access token** (`oidc.jwt.expiration.access-token`, 5 min en la aplicación de referencia) |
| `refresh-token-max-lifetime-seconds` | Vida máxima de una cadena de refresh: aunque se rote, la sesión de la aplicación caduca al llegar aquí | `7776000` (90 días); sin configuración o con `0`, 30 días |

Valores orientativos: una herramienta interna con sesiones de jornada laboral, `session-sso-ttl-seconds = 28800`; una aplicación con datos sensibles, `3600` o menos y una vida máxima de refresh de unos días.

### Invitaciones y passkeys

| Campo | Qué poner | Por defecto |
|---|---|---|
| `invitation-expiry-days` | Días de validez del enlace de invitación | `7` |
| `webauthn-rp-id` | Dominio en el que funcionan las passkeys: el host público del login o un dominio padre (`login.acme.com` o `acme.com`) | Host público |
| `webauthn-rp-name` | Nombre que ve el usuario al crear la passkey | Nombre del tenant |

Cambiar `webauthn-rp-id` cuando ya hay passkeys registradas las deja inservibles.

### Registro dinámico de clientes (DCR)

Permite que las aplicaciones se registren solas con un *initial access token* (ver [Clientes OAuth](05-clientes-oauth.md#registro-dinámico)).

| Campo | Qué poner | Por defecto |
|---|---|---|
| `dynamic-registration-policy` | `token_required` para activarlo; `disabled` para desactivarlo. `open` se comporta como `token_required` | `disabled` |
| `dcr-allowed-grant-types` | Lista JSON de grants registrables: `["authorization_code","refresh_token"]` | Esa misma lista |
| `dcr-allowed-auth-methods` | Lista JSON de métodos de autenticación: `["client_secret_basic","private_key_jwt","none"]` | Esa misma lista |
| `allow-password-grant` | Marcar solo si se aceptan aplicaciones que reciban la contraseña del usuario | Desmarcado |

### Campos sin efecto

Los siguientes campos se pueden rellenar, pero **la versión actual no los usa**. Dejarlos con su valor por defecto:

| Campo | Comportamiento real |
|---|---|
| `display-name` | No se muestra en ninguna pantalla |
| `force-mfa` | No obliga al segundo factor. Para exigirlo, activar el segundo factor en cada usuario o que la aplicación pida `acr_values=urn:lugh:acr:mfa` |
| `self-registration-enabled`, `auto-approve-registered-users` | El enlace «Registrarse» aparece siempre y el alta nunca se completa. Para dar de alta usuarios, usar invitaciones |
| `password-recovery-enabled` | La recuperación de contraseña está siempre disponible |
| `magic-link-enabled`, `magic-link-expiry-minutes` | El enlace mágico está desactivado en todos los tenants |
| `webauthn-enabled` | Las passkeys están siempre disponibles |
| `require-email-verification` | No se exige email verificado |
| `acr-values-supported`, `require-pushed-authorization-requests` | Los niveles ACR son globales y PAR se exige por cliente |
| `*-email-template` | Los correos no usan estas plantillas |

## Términos de uso

Apartado *condiciones* (`/api/access/tenants-terms-of-use`). Cada registro es una versión de los términos que el usuario debe aceptar en su siguiente login.

| Campo | Qué poner |
|---|---|
| `text` | El texto que se muestra al usuario. Obligatorio |
| `document` | PDF u otro documento asociado (opcional) |
| `activation-date` | Fecha desde la que se exige. **Sin fecha, nunca se muestra** |
| `resource-server` | Hoy no se tiene en cuenta: todos los términos del tenant se piden en cualquier aplicación |

Hay que habilitar el registro con la acción *enable*. Para publicar una versión nueva, crear otro registro con su fecha de activación: los usuarios que aceptaron la anterior tendrán que aceptar la nueva.

## Propósitos RGPD

`/api/access/consent-purposes`. Usos de datos que el usuario acepta o rechaza uno a uno durante el login.

| Campo | Qué poner |
|---|---|
| `key` | Identificador estable: `marketing-email` |
| `title`, `description` | Lo que lee el usuario |
| `required` | Marcado si el uso es imprescindible para el servicio |
| `activation-date` | Desde cuándo se pide. Obligatoria |

## Initial access tokens de DCR

`/api/access/dcr-initial-access-tokens` (rol `admin`). Solo hacen falta si el tenant tiene DCR activo.

| Campo | Qué poner |
|---|---|
| `description` | Para quién es: «Integración del proveedor X» |
| `token-hash` | El token en claro, generado aparte con un generador de secretos (32 bytes aleatorios en base64url). Solo se guarda su huella y no se puede consultar después: **copiarlo antes de guardar** y entregarlo por un canal seguro |
| `expires-at` | Caducidad. Que sea corta (horas o días) |
| `max-uses` | Cuántos clientes se pueden registrar con él. Por defecto `1` |
| `allowed-grant-types` | Lista JSON que sustituye a `dcr-allowed-grant-types` del tenant para este token (opcional) |
| `revoked-at` | Rellenar para anularlo antes de tiempo |
