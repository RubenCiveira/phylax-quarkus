# Instalación y administradores

## Primer arranque

Con la base de datos vacía, la aplicación crea una sola vez los datos mínimos a partir de `migration/seed/oidc-seed.yaml`:

| Qué | Valor de referencia | Para qué |
|---|---|---|
| Tenant raíz | `main` | El tenant de administración global |
| Usuario root | `ROOT`, contraseña temporal | Primer administrador; debe cambiar la contraseña en su primer login |
| Roles | `ADMIN`, `ROOT`, `IAM`, asignados a `ROOT` para todas las audiencias (llegan al token en minúsculas) | Permisos de administración |
| Servidor de recursos | `civi-acme-api` | La API de la aplicación |
| Cliente OAuth | `civi-acme-ui`: global, público con PKCE, todos los scopes, audiencia `civi-acme-api`, redirect y logout `http://localhost:4200/*` | El frontal de administración en local |
| Grupos | `root` para `ROOT` en el servidor de recursos y en el cliente | Claim `groups` |
| API keys | `collector` y `sync` | Integraciones máquina (hoy sin validación, ver [Límites](../funcional/09-limites-y-pendientes.md#opciones-del-modelo-sin-efecto)) |

Cada aplicación debe ajustar esos nombres en su `oidc-seed.yaml` antes de la primera instalación (por ejemplo `net-inventory-api` y `net-inventory-ui`, con las URLs reales del frontal).

La contraseña de `ROOT` se da con la variable `OIDC_ROOT_PASSWORD`. Si falta, se genera una aleatoria y **se muestra una sola vez en el log** del arranque: hay que copiarla en ese momento.

Antes de la primera instalación en un entorno real, quien despliega debe fijar los secretos del servidor (`SECURITY_DIGEST_PEPPERS`, `SECURITY_SECRETS_KEYS`…). Si se cambian después, los secretos de clientes y tokens ya guardados dejan de validar. Ver [Configuración](../tecnica/07-configuracion.md).

## Tipos de administrador

| Quién | Cómo se reconoce | Qué puede hacer |
|---|---|---|
| **Administrador root** | Usuario del tenant raíz con el rol `admin` (su token lleva el scope `platform:global_access`) | Todo, en todos los tenants: crear y habilitar tenants, gestionar clientes OAuth, servidores de recursos y API keys, ver los proveedores federados de cualquier tenant |
| **Administrador de tenant** | Usuario de un tenant con el rol `admin` | Usuarios, roles, configuración, términos, propósitos RGPD y proveedores de **su** tenant; initial access tokens de DCR; invitaciones |

**Solo root** gestiona clientes OAuth y servidores de recursos. Un administrador de tenant que necesite registrar una aplicación sin root tiene que hacerlo por registro dinámico (ver [Clientes OAuth](05-clientes-oauth.md#registro-dinámico)).

> **Atención.** El modelo solo exige el rol `admin` para clientes OAuth, servidores de recursos, API keys, tenants (escritura) e initial access tokens. Usuarios, roles, asignaciones de roles, proveedores, configuración y términos **no comprueban rol**: cualquier usuario autenticado del tenant puede modificarlos dentro de su tenant (pendiente en el modelo, marcado `FIXME`). Mientras no se corrija, no hay que dar acceso al frontal de administración a usuarios que no sean administradores.

## Entrar al frontal de administración

El frontal es un cliente OAuth más: redirige a `/oauth/openid/{tenant}/authorize` del tenant en el que se entra. Para administrar todos los tenants hay que entrar por el tenant raíz (`main`) con una cuenta root.
