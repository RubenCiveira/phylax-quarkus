# Integración como trait

El gestor de identidades no es una aplicación con nombre propio, sino un conjunto de *traits* de civi-generator. Cualquier aplicación generada (por ejemplo, un inventario de equipos de red o una herramienta de facturación) lo incorpora y obtiene su propio servidor OIDC, sus pantallas de login y la administración de tenants, usuarios y clientes.

Este repositorio es la **aplicación de referencia**: aquí se desarrolla y se prueba el código, que luego vuelve a las plantillas. Por eso los paquetes se llaman `net.civeira.phylax`; en otra aplicación serán su paquete base (`{{base}}` en la plantilla).

## Piezas

| Pieza | Dónde | Qué aporta |
|---|---|---|
| Modelo `multitenant-oidc` | `civi-generator/src/main/resources/uses/multitenant-oidc.xml` | Entidades del grupo `access` (`tenant`, `tenant-config`, `oauth-client`, `resource-server`, `user`, `role`…), sus permisos y pantallas. Arrastra `multitenant-documents` y `multitenant-notification` (plantillas y envío de correos) |
| Trait `oidc` | `template/quarkus/traits/oidc` | `features/oauth`: dominio, casos de uso, controladores, pantallas, migraciones `_oauth_*` y pruebas de flujo |
| Trait `multitenant-oidc` | `template/quarkus/traits/multitenant-oidc` | Los adaptadores *driven* de `features/oauth` que leen y escriben las entidades multi-tenant de `access` |
| Contenedor | `template/quarkus/container` | `common` y `bootstrap`: seguridad, `CurrentRequest`, `Actor`, migraciones al arrancar |
| Trait PHP | `template/php/traits/_x_oidc` | La versión PHP (lughauth), alimentada por el mismo modelo |

`binary/sync-oidc-template.py` decide a qué capa va cada fichero (ver su cabecera) y sustituye `net.civeira.phylax` por `{{base}}`.

## Incorporarlo a una aplicación

El modelo de la aplicación anfitriona declara el uso y añade sus propias entidades:

```xml
<work-project version="2.6.0" name="com.example.netinventory" label="net-inventory" lang="es">
  <conf name="back-model" value="quarkus-mini" />
  <conf name="back-folder" value="../net-inventory-api" />
  <conf name="front-model" value="angular-18" />
  <conf name="front-folder" value="../net-inventory-ui" />

  <use link="multitenant-oidc" />

  <entity name="network-device" id="uid" generator="uuid" display="hostname" group="inventory">
    <property name="uid" type="xs:string" unique="true" required="true" />
    <property name="tenant" type="tns:tenant" required="true" />
    <property name="hostname" type="xs:string" required="true" />
    <facade type="crud" path="inventory/devices">
      <allow for="write" name="#inventory-admin" match="hasRole('admin')" />
    </facade>
  </entity>
</work-project>
```

El modelo de la aplicación de referencia (`phylax-model/model.xml`) es solo eso: `<use link="multitenant-oidc" />` sin entidades propias.

Lo que obtiene la aplicación anfitriona:

- Las rutas del protocolo bajo `/oauth/openid/{tenant}/…` y el JWKS global en `/oauth/jwks`.
- La API de administración bajo `/api/access/*` y las pantallas del frontal generado.
- Las migraciones de las tablas `access_*` y `_oauth_*`, y el *seed* de instalación.
- La verificación de bearer en sus propias APIs (`TokenJwtCallerPrincipalFactory`, `ApiBearerTenantFilter`) y el `Actor` con usuario, roles, grupos y scopes en cada petición.

## Multi-tenancy de las entidades propias

Una entidad con una propiedad `tns:tenant` hereda las reglas `for-childs` de la entidad `tenant`:

- Para un usuario sin `platform:global_access`, el tenant se fija desde el claim `tenant` del token, tanto al leer (filtro fijo) como al escribir (fórmula `inherit-tenant-excluding-root`).
- Un usuario del tenant raíz (con `platform:global_access`) ve y escribe todos los tenants.

Los permisos de las fachadas se expresan con `hasRole(...)` y `hasScope(...)` sobre el `Actor`. Los roles salen del claim `roles` del token, que es un mapa por audiencia; `CurrentRequest` **junta los roles de todas las audiencias** (ver [Límites](../funcional/09-limites-y-pendientes.md#límites-conocidos)).

## Qué debe ajustar cada aplicación

| Ajuste | Dónde | Por qué |
|---|---|---|
| Audiencia de su API | `mp.jwt.verify.audiences` **y** `mp.jwt.audiences` | Deben valer el código del servidor de recursos de la aplicación. `mp.jwt.audiences` no está en ningún fichero y su valor por defecto es `phylax-api` (`ApiBearerTenantFilter`) |
| Datos de instalación | `migration/seed/oidc-seed.yaml` (`oidc.tenant`, `oidc.relying-party.code`, `oidc.trusted-client.code`, redirects) | El seed de referencia crea `main`, `civi-acme-api` y `civi-acme-ui` con redirect a `http://localhost:4200/*`. El código del servidor de recursos debe coincidir con la audiencia anterior |
| Secretos | `SECURITY_DIGEST_*`, `SECURITY_SECRETS_*`, `OIDC_PAIRWISE_SALT`, `OIDC_ROOT_PASSWORD` | Sin valor por defecto fuera de dev/test |
| Host público | `oidc.base-url` | Emisor, enlaces de correo y WebAuthn |
| Valores ACR | `security.acr.*` | Por defecto `urn:lugh:acr:1fa` y `urn:lugh:acr:mfa` |
| Nombres de base de datos | `config/database.yaml` | Los comentarios de referencia usan `phylax_quarkus` |

`mp.rbac.model=phylax` (clase `common/security/impl/rbac/Phylax`) no forma parte del gestor de identidades: es un cliente opcional de un servidor de permisos externo, desactivado en `security.yaml`.

## Ciclo de desarrollo

1. Se cambia el código en la aplicación de referencia y se prueba aquí.
2. `binary/sync-oidc-template.py --apply` lleva los cambios a las plantillas (`--include-new` para clases nuevas, revisando que no sean propias de esta aplicación; `--check` detecta diferencias en CI).
3. Un cambio de esquema se hace en `multitenant-oidc.xml` y se regenera: nunca se editan a mano las clases generadas de `access` ni las migraciones.
4. Las aplicaciones anfitrionas reciben el cambio al regenerarse.

Las clases de `features/oauth` marcadas como generadas se sobrescriben al regenerar; un cambio local que no se haya sincronizado con la plantilla se pierde.
