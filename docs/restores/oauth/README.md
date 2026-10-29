# Documentación del gestor de identidades

El gestor de identidades es un servidor de autorización OAuth 2.0 / OpenID Connect multi-tenant que se incorpora como *trait* de civi-generator a cualquier aplicación generada: un inventario de equipos de red, una herramienta de facturación… La aplicación que lo incorpora (la **aplicación anfitriona**) obtiene así su propio gestor de usuarios. Este repositorio es la aplicación de referencia donde se desarrolla.

Esta carpeta describe el comportamiento del código de la rama `feat/refactor-oidc` a 29-10-2026.

> **Sobre la conformidad.** Ninguna capacidad descrita aquí está certificada: la OpenID Conformance Suite aún no se ha ejecutado (tarea V.2). Lo que se documenta está cubierto por pruebas automáticas del propio repositorio, salvo donde se indica lo contrario.

## Manual de administración

Para quien configura el gestor en una aplicación: qué rellenar y en qué orden. Empieza en el [índice del manual](user-manual/README.md).

| Guía | Contenido |
|---|---|
| [Instalación y administradores](user-manual/01-instalacion-y-administradores.md) | Primer arranque, cuenta root, tipos de administrador |
| [Tenants](user-manual/02-tenants.md) | Alta del tenant, configuración, términos, propósitos RGPD, tokens de DCR |
| [Proveedores federados](user-manual/03-proveedores-federados.md) | Google y SAML paso a paso, enlace de cuentas |
| [Servidores de recursos](user-manual/04-servidores-de-recursos.md) | Registrar una API, sus roles y cómo valida los tokens |
| [Clientes OAuth](user-manual/05-clientes-oauth.md) | Recetas para SPA, web, servicio, dispositivo e introspección; registro dinámico |
| [Usuarios, roles e invitaciones](user-manual/06-usuarios-roles-e-invitaciones.md) | Altas, mantenimiento de cuentas y asignación de roles |
| [Problemas frecuentes](user-manual/07-problemas-frecuentes.md) | Síntomas, causas y soluciones |

## Documentación funcional

Para quien integra una aplicación, administra un tenant o decide sobre el producto: qué hace el servidor y con qué reglas.

| Documento | Contenido |
|---|---|
| [Visión general](funcional/01-vision-general.md) | Conceptos (tenant, cliente, servidor de recursos, sesión), rutas y estado |
| [Clientes](funcional/02-clientes.md) | Tipos de cliente, configuración, autenticación del cliente y registro dinámico (DCR) |
| [Inicio de sesión](funcional/03-inicio-de-sesion.md) | Pantallas del login: contraseña, MFA, consentimientos, recuperación, registro, enlace mágico, login federado |
| [Solicitud de autorización](funcional/04-autorizacion.md) | Parámetros de `/authorize`, PKCE, PAR, request objects, modos de respuesta y errores |
| [Tokens y grants](funcional/05-tokens-y-grants.md) | Endpoint `/token`, grants, refresh, token exchange, device flow, contenido de cada token |
| [Consumo de tokens](funcional/06-consumo-de-tokens.md) | UserInfo, introspección, revocación y validación de bearer en las APIs |
| [Sesión y logout](funcional/07-sesion-y-logout.md) | SSO, `prompt`/`max_age`, step-up, logout RP-initiated, back- y front-channel, iframe de sesión |
| [Cuenta del usuario](funcional/08-cuenta-de-usuario.md) | Perfil, sesiones, passkeys, consentimientos, RGPD e invitaciones |
| [Límites y pendientes](funcional/09-limites-y-pendientes.md) | Lo que no se ofrece, opciones del modelo sin efecto, fallos conocidos y lo que falta verificar |

## Documentación técnica

Para quien mantiene el código: dónde vive cada regla y cómo encajan las piezas.

| Documento | Contenido |
|---|---|
| [Arquitectura](tecnica/01-arquitectura.md) | Paquetes de `features/oauth`, capas, relación con `features/access` y generación por plantillas |
| [Catálogo de endpoints](tecnica/02-endpoints.md) | Rutas, controladores y pruebas que las verifican |
| [Flujo de autorización](tecnica/03-flujo-authorize.md) | `AuthorizeHtml`, `OidcStepRouter`, pasos, pre-sesión y cookies |
| [Endpoint de token](tecnica/04-endpoint-token.md) | `IssueTokenUsecase`, grants, emisión y verificación de tokens |
| [Claves, firma y secretos](tecnica/05-claves-y-secretos.md) | Anillo de claves, rotación, claves temporales, digest con *pepper* y cifrado |
| [Persistencia](tecnica/06-persistencia.md) | Tablas `_oauth_*` y entidades de `access` que usa el módulo |
| [Configuración](tecnica/07-configuracion.md) | Propiedades de la aplicación, del tenant y del cliente |
| [Pruebas](tecnica/08-pruebas.md) | Capas de prueba, dobles, MariaDB y arnés de conformidad |
| [Integración como trait](tecnica/09-integracion-como-trait.md) | Traits de civi-generator, cómo se incorpora a otra aplicación y qué debe ajustar |

## Otras fuentes

- [docs/TODO-OIDC](../../../../docs/TODO-OIDC/README.md): plan de trabajo por fichas; cada ficha cerrada en `done/` recoge la decisión y las pruebas que la verifican.
- [docs/oauth](../../../../docs/oauth/overview.md): catálogo de rutas en inglés y guía de pruebas (las páginas `architecture`, `flows` y `controller-parts` son históricas).
- [conformance/](../../../../conformance/README.md): arnés de la OpenID Conformance Suite.
