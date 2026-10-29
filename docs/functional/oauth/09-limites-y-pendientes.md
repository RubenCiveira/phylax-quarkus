# Límites y pendientes

Qué no hace el gestor de identidades, por decisión o porque está pendiente. Lo que está pospuesto, con su motivo y cuándo se reabriría, está en la [hoja de ruta](../../../ROADMAP.md).

## Validación pendiente

Ninguna capacidad de esta documentación está certificada: la **OpenID Conformance Suite** no se ha ejecutado nunca (el arnés está en `src/test/conformance/`), así que no se declara conformidad. Lo documentado está cubierto por las pruebas automáticas del repositorio, salvo donde se indica lo contrario. Tampoco se ha probado contra sistemas reales (IdP SAML y Google, RP con `jwks_uri`, servidores de recursos que validen DPoP). Ver [Pruebas](../../technical/contexts/oauth/08-pruebas.md).

## Descartado o pospuesto

Lo pospuesto se retoma según las condiciones de la [hoja de ruta](../../../ROADMAP.md).

| Capacidad | Decisión |
|---|---|
| Flujo implicit puro (`token`, `id_token`) | Retirado (OAuth 2.1). Se mantienen los hybrid |
| `client_secret_jwt` | Descartado: los secretos no son recuperables, solo `private_key_jwt` |
| Grant `delegated` propietario | Sustituido por token exchange |
| Federación OIDC genérica, Microsoft, Apple, GitHub | Descartada; el SSO corporativo va por SAML. Se reabre con un cliente que aporte su IdP OIDC |
| Firma por tenant | Pospuesta: un juego de claves global para que los microservicios validen con un JWKS fijo |
| ES256 / EdDSA en la firma del servidor | Pospuesto; el cambio de modelo está preparado |
| Grant JWT bearer (RFC 7523) | Pospuesto hasta tener caso de uso |
| mTLS y tokens ligados a certificado | Pospuesto; DPoP cubre el caso |
| CIBA | Pospuesto; `/ciba-auth` responde siempre 403 y no se anuncia |
| Rich Authorization Requests (RAR) | Pospuesto |
| Selector de cuentas | No existe: `select_account` equivale a `prompt=login` |
| `claims_locales` | No se ofrece |
| Verificación de email | Pendiente: `require-email-verification` no tiene efecto y ningún paso del login la exige |
| Registro de auditoría | Pendiente: la tabla `_oauth_audit_log` existe, pero ningún código escribe en ella |
| Webhooks de eventos de autenticación | Pendiente, después del registro de auditoría |
| API keys personales (`user-personal-api-key`) | Pendiente: hay CRUD de administración, pero ni autoservicio ni autenticación con ellas |

## Opciones del modelo sin efecto

El modelo ofrece campos que la administración deja rellenar pero que el código no consulta. Quien administra un tenant debe saber que cambiarlos no altera nada.

| Campo | Comportamiento real |
|---|---|
| `tenant-config.self-registration-enabled`, `auto-approve-registered-users` | El enlace de registro aparece siempre y el alta nunca se completa (`RegisterUserAdapter` cancela) |
| `tenant-config.password-recovery-enabled` | La recuperación de contraseña se ofrece siempre |
| `tenant-config.magic-link-enabled` | El enlace mágico está desactivado en todos los tenants (`MagicLinkEnabledAdapter`) |
| `tenant-config.force-mfa` | No obliga al segundo factor; solo lo hacen la cuenta del usuario o `acr_values` |
| `tenant-config.webauthn-enabled` | Las passkeys están disponibles en todos los tenants |
| `tenant-config.require-email-verification` | No se exige email verificado |
| `tenant-config.display-name` | No se muestra; la etiqueta del TOTP es el nombre de usuario |
| `tenant-config.*-email-template` | Los correos no usan estas plantillas |
| `tenant-config.acr-values-supported`, `require-pushed-authorization-requests` | Los niveles ACR salen de `security.acr.*` y PAR solo se exige por cliente |
| `tenant-login-provider` de tipo `GITHUB`, `MICROSOFT`, `APPLE`, `OIDC` y los campos `oidc-*`, `saml-idp-metadata-url` | Solo se ofrecen proveedores `GOOGLE` y `SAML`; el resto no aparece en el login |
| `resource-server.api-key` | Obligatorio en el modelo, pero no se usa para autenticar nada |
| `api-key-client` y `POST /oauth/api-key` | `ApiKeyStoreAdapter` no busca la clave: el endpoint nunca valida ninguna |
| `oauth-client.default-acr-values`, `default-max-age`, `authorization-details-types`, CIBA y mTLS | Pospuestos (ver arriba) |

## Límites conocidos

- **Bloqueo por intentos** (fallo): se activa en el cuarto fallo y su comprobación está invertida; tras vencer, la cuenta queda rechazada hasta un *unlock* manual. Ver [Inicio de sesión](03-inicio-de-sesion.md#bloqueo-por-intentos).
- **Aviso de login** (fallo): cada autenticación correcta, también cada refresh, envía un correo a una dirección fija en el código, no configurable. Ver [Inicio de sesión](03-inicio-de-sesion.md#correos).
- **QR del TOTP**: la cuenta aparece como `{usuario} at ` con emisor `no`, sin el nombre del tenant.
- **Revocación**: no comprueba que el token sea del cliente, por eso exige cliente confidencial. Revocar un refresh no revoca su familia.
- **Reutilización de un code**: el segundo canje falla, pero los tokens del primero no se revocan.
- **`amr`** vacío en login federado y por enlace mágico (cuenta como un factor).
- **Enlace de cuentas federadas**: con la política `verified-email`, un proveedor que afirme un email verificado entra en la cuenta local con ese email, también en una de administración. Usar `manual` o `none` en tenants con cuentas privilegiadas.
- **Roles en las APIs internas**: el claim `roles` es un mapa por audiencia, pero la aplicación anfitriona junta los roles de todas las audiencias del token al comprobar permisos.
- **Audiencia de las APIs internas**: hay dos propiedades. `mp.jwt.verify.audiences` (en `security.yaml`) se aplica a todas las rutas; `mp.jwt.audiences`, que no está en ningún fichero y vale `phylax-api` por defecto, se aplica además a las rutas `api/{tenant}/…`. Una aplicación anfitriona debe fijar las dos con el código de su servidor de recursos.
- **Step-up** de una sesión cuyo usuario no tiene TOTP muestra el formulario de TOTP, no el alta.
- **Parámetro `claims`**: el miembro `userinfo` solo se valida y `value`/`values` no filtran.
- **DPoP**: sin `dpop_jkt` en authorize ni `DPoP-Nonce`; los servidores de recursos externos validan la prueba ellos mismos.
- **`sector_identifier_uri`** no se descarga ni se verifica; DCR lo rechaza.
- **Invitaciones**: no aplican los roles ni inician sesión al aceptar; falta la plantilla de correo `user.invitation`.
- **Initial access token de DCR**: se crea con el CRUD de administración; no hay una acción que lo genere y lo muestre una sola vez. Tras rotar el *pepper* hay que reemitir los initial y registration access tokens.
- **`jwks_uri` remoto**: riesgo residual de DNS *rebinding* entre la resolución y la conexión.
- **Sin pruebas HTTP**: enlace mágico, WebAuthn, aceptación de invitaciones, páginas de perfil y JWKS (este último se ejercita en todas las firmas).

## Antes de desplegar

Los secretos, ajustes y comprobaciones previos al despliegue están en la guía de [Instalación](../../operations/oauth/01-instalacion.md).
