# Servidores de recursos

Un servidor de recursos es una **API que acepta los tokens** del gestor de identidades: la API de la propia aplicación (`net-inventory-api`) o la de un servicio aparte. Registrarlo sirve para dos cosas:

- Su **código** es la audiencia (`aud`) de los tokens destinados a esa API.
- Los **roles** de los usuarios se definen y se asignan por servidor de recursos.

Los servidores de recursos son **globales** (no pertenecen a un tenant) y solo los gestiona un administrador root (`/api/access/resource-servers`).

## Registrar el servidor

| Campo | Qué poner | Notas |
|---|---|---|
| **Código** (`code`) | Identificador estable de la API: `net-inventory-api` | Obligatorio y único. Es el valor que llevará `aud`, el que piden los clientes y el que la API comprueba. No cambiarlo una vez en uso |
| `api-key` | Un valor aleatorio largo | Obligatorio y único en el modelo, pero **hoy no se usa** para nada |

Se habilita con la acción *enable*. **Deshabilitarlo no impide** que se sigan emitiendo tokens para él: para cortar el acceso a una API hay que quitar su código de las audiencias de los clientes.

## Definir sus roles

Menú de roles (`/api/access/roles`):

| Campo | Qué poner |
|---|---|
| `name` | Nombre del rol: `admin`, `operator`, `viewer`. Llega al token **en minúsculas** |
| `resource-server` | El servidor al que pertenece. Vacío: rol general, para todas las APIs |

El nombre debe ser único dentro de cada servidor. La asignación a usuarios se explica en [Usuarios, roles e invitaciones](06-usuarios-roles-e-invitaciones.md#roles).

## Permitir que un cliente pida tokens para él

En cada cliente OAuth que deba llamar a la API, añadir el código en **audiencias permitidas** (`allowed-audiences`). Después, la aplicación cliente pide la audiencia en `/authorize` o en `/token`:

```
GET /oauth/openid/acme/authorize?client_id=net-inventory-ui&audience=net-inventory-api&scope=openid&...
```

Varias audiencias van separadas por comas (`audience=net-inventory-api,billing-api`). El propio cliente se añade siempre: sin el parámetro `audience`, el token solo lleva el código del cliente y la API lo rechazará. Pedir una audiencia que no está en la lista da el error `invalid_target`. (El parámetro `resource` es para audiencias en forma de URI absoluta.)

## Qué recibe la API

Un access token es un JWT firmado (RS256) como este:

```json
{
  "iss": "https://login.acme.com/oauth/openid/acme",
  "sub": "ana.garcia",
  "aud": ["net-inventory-api", "net-inventory-ui"],
  "client_id": "net-inventory-ui",
  "tid": "8d1c…",
  "scope": ["openid", "profile"],
  "roles": { "net-inventory-api": ["operator"] },
  "exp": 1793350800,
  "jti": "5f0e…"
}
```

- `iss` identifica el tenant: `…/oauth/openid/{tenant}`.
- `roles` es un **mapa por audiencia**: cada API debe leer solo la entrada de su propio código.
- Los grupos (`user-group-membership`) **no llegan** al token.

## Validar los tokens en una API externa

1. Descargar las claves de `https://{host}/oauth/jwks`. Son las mismas para todos los tenants y rotan: refrescar la caché cuando llegue un `kid` desconocido.
2. Comprobar la firma, `exp` y que `aud` contiene el código del servidor.
3. Comprobar que `iss` es el emisor de un tenant aceptado. Una API de un solo tenant compara con un valor fijo; una multi-tenant debe decidir qué tenants admite.
4. Leer los roles de `roles["{código}"]`.

Un token revocado sigue pareciendo válido hasta su `exp` (5 minutos por defecto). Si la API necesita enterarse de las revocaciones al momento, debe usar la **introspección**: registrar para la API un cliente OAuth confidencial con `introspection-allowed` marcado y llamar a `POST /oauth/openid/{tenant}/introspect` con sus credenciales.

## Si la API es la de la aplicación anfitriona

La propia aplicación valida los tokens sin código adicional, pero su configuración debe apuntar a su servidor de recursos: `mp.jwt.verify.audiences` y `mp.jwt.audiences` con el mismo código (ver [Instalación](../../operations/oauth/01-instalacion.md#ajustes-de-la-aplicación)). Ojo: la aplicación junta los roles de **todas** las audiencias del token al comprobar permisos.
