# 0.16 — Seguridad del login federado (SAML, Google y `state`)

- **Fase:** 0 · **Esfuerzo:** M–L · **Estado:** implementada (23-10-2026), pendiente de verificación manual con un IdP SAML y Google reales. **Prioridad:** crítica (adelantar a la ola 1).
- **Origen:** equivalente Java de la ficha PHP [072](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/072-seguridad-login-federado.md), que apareció al descartar la federación OIDC genérica ([3.4](../issue-3.4-federacion-oidc-generica.md)). Revisado sobre el código Java el 23-10-2026.

## Evidencia en Java

1. **La cuenta local se elige solo por `name`.** [DelegatedAccessAuthValidatorAdapter](../../../src/main/java/net/civeira/phylax/features/oauth/delegatelogin/infrastructure/driven/DelegatedAccessAuthValidatorAdapter.java) busca `UserFilter.name(codeInfo.getName())` en el tenant y, si existe y está habilitada, entra en ella sin mirar qué proveedor creó la cuenta ni si tiene contraseña. Es peor que el nOAuth de PHP: el `name` no es ni un email verificado.
   - **Google** ([GoogleDelegatedAccessProvider](../../../src/main/java/net/civeira/phylax/features/oauth/delegatelogin/domain/provider/GoogleDelegatedAccessProvider.java)): `name` = nombre + apellidos sin espacios, y `email` = `name + "@gmail.com"`. Cualquier cuenta de Google cuyo nombre visible coincida con un usuario local (`admin`, por ejemplo) entra en esa cuenta. No usa el `sub` de Google ni `verified_email`, aunque lo deserializa.
   - **SAML** ([SamlDelegatedAccessProvider](../../../src/main/java/net/civeira/phylax/features/oauth/delegatelogin/domain/provider/SamlDelegatedAccessProvider.java)): `name` sale del atributo `.../claims/name` (nombre visible que controla el IdP) o del `NameID`, y el `NameID` se toma también como email.
2. **Validación SAML incompleta.** Verifica la firma del primer `Signature` del documento y lee la primera `Assertion`, sin comprobar que la firma cubre esa aserción (*signature wrapping*). Solo registra `ID` en la raíz, así que una aserción firmada por sí sola no se puede validar. No comprueba `Issuer`, `InResponseTo`, `Destination` ni `Recipient`. La audiencia esperada es el **nombre del proveedor**, no el entity ID del SP. `NotBefore`/`NotOnOrAfter` vacíos lanzan `DateTimeParseException` en lugar de un rechazo controlado. La `AuthnRequest` usa un `ID` aleatorio que no se guarda: cualquier respuesta firmada por el IdP para este SP vale, también las no solicitadas.
3. **`state` sin vínculo con el navegador.** La vuelta se apoya en las cookies `back`/`from` que escribe JavaScript ([DelegatedAccessController](../../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/DelegatedAccessController.java)) y en un `code` temporal que llega por query a `/delegated-auth` (`DelegatedAuthCallbackController` (eliminado)). Nada liga el callback a la pre-sesión que inició el login, así que es posible el login CSRF. Además, el `user-code` almacenado se resuelve sin consumirlo ([DelegateLogin](../../../src/main/java/net/civeira/phylax/features/oauth/delegatelogin/application/DelegateLogin.java)`::resolveUsername`), y la variante sin proveedor no comprueba cuál lo emitió.
4. **XSS reflejado.** [DelegatedStep](../../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/step/DelegatedStep.java)`::doBackLoginForm` concatena `code` y `provider` de la query en el HTML sin escapar, y `DelegatedAccessController::writeLogin` hace lo mismo con los parámetros, la URL de destino, el título y el mensaje.

**Impacto:** toma de cualquier cuenta de un tenant con un proveedor federado activo, incluidas las cuentas con contraseña y las de administración.

## Implementación (contrato PHP 072, adaptado a Java)

- **Identidades enlazadas por proveedor.** Entidad nueva `user-federated-identity` (`user`, `login-provider`, `subject`; único `(login-provider, subject)`) en el modelo de Access, con su CRUD de administración y el listener «enlace dentro de un tenant». El `subject` es el identificador estable del IdP (`sub` de Google y `NameID` persistente en SAML), nunca el nombre ni el email.
- **Resolución de la cuenta:**
  1. sin `subject`: rechazo;
  2. sujeto ya enlazado a una cuenta del tenant con este proveedor: entra en ella;
  3. política `manual`: solo valen los enlaces que ya existan;
  4. email no verificado por el proveedor: rechazo;
  5. hay una cuenta local con ese email: enlace solo si la política lo permite y la cuenta no es de administración ni tiene contraseña (o con confirmación del usuario);
  6. si no existe, alta con el proveedor y enlace.
  Nunca se busca por `name`.
- **Google:** usar el ID token (OIDC) o `/v3/userinfo`, con `sub` y `email_verified` tratados como booleano estricto, y PKCE + `nonce`.
- **SAML:** validación estricta con una librería mantenida (OpenSAML o equivalente), sin el parser propio. Debe comprobar la firma de la respuesta o de la aserción **usada**, `Issuer` = entity ID del IdP, `Audience` = entity ID del SP, `Destination`/`Recipient` = URL del callback, la ventana de validez e `InResponseTo` = ID de la `AuthnRequest` derivado del `state`. Se rechazan las respuestas no solicitadas. Un proveedor sin entity ID o sin certificado deja de ofrecerse.
- **`state` ligado a la pre-sesión:** 32 bytes aleatorios guardados en `ChallengesState` con el proveedor, el tenant y el cliente, y comparados en tiempo constante antes de llamar al proveedor. **Un solo uso en servidor:** gastarlo en el almacén anti-replay (propósito `delegated_login`, que compartirá con J-05/J-10).
- **Callback:** GET y POST (binding HTTP-POST de SAML) responden con un formulario autoenviado al `/authorize` del tenant, campos escapados y CSP por hash; sin `state` válido, 400. Los formularios del flujo delegado se construyen con escape HTML, no por concatenación.

## Cierre

- Pruebas HTTP + persistencia para cada fallo: aserción SAML sin firmar, firmada por otro certificado, con *signature wrapping*, de otro emisor o audiencia, no solicitada o repetida; usuario de Google con nombre o email de una cuenta local existente; callback sin `state`, con `state` de otro navegador o repetido; `code`/`provider` con HTML en la query.
- Enlace de un proveedor de otro tenant rechazado; dos primeros accesos concurrentes del mismo sujeto crean un único enlace.

- **Dependencias:** [0.5](../issue-0.5-aislamiento-clientes-tenant.md) (tenant del proveedor); almacén anti-replay de [J-05](../epicas/ep-J-05-solicitudes-authorize-par-jar.md) (si llega antes, se crea aquí).

## Implementación Java (23-10-2026)

- **Flujo nuevo.** `step=delegated-login` ([DelegatedStep](../../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/step/DelegatedStep.java)) crea un login pendiente ([PendingDelegation](../../../src/main/java/net/civeira/phylax/features/oauth/delegatelogin/domain/PendingDelegation.java)) con tenant, cliente, proveedor, URL de vuelta construida en el servidor, verificador PKCE e ID de `AuthnRequest`. Lo guarda en `_oauth_delegated_state` bajo el SHA-256 del `state` (10 min), pone la cookie `DELEGATED_BINDING` (HttpOnly, Secure, `SameSite=Lax`, ruta del tenant) y redirige al proveedor. El callback `/oauth/openid/-/delegated/authorization/{provider}` ([DelegatedAccessController](../../../src/main/java/net/civeira/phylax/features/oauth/authentication/infrastructure/driver/html/DelegatedAccessController.java)) solo busca el login pendiente para saber dónde volver (400 si no existe) y reenvía la respuesta escapada, por POST, al `/authorize` del tenant. Allí `step=query-delegated` lo **consume** (borrado atómico) y exige que coincidan la cookie (tiempo constante), el tenant, el cliente y el proveedor **antes** de llamar al proveedor.
- **Rutas retiradas:** `/{tenant}/delegated/redirect/{provider}`, `/{tenant}/delegated/token/{provider}` y `/{tenant}/delegated-auth`, junto con las cookies `back`, `from` y `post-auth-target` que escribía JavaScript. Con ellas desaparece una fuga del código: `back` se elegía en la query, así que un enlace podía mandar el `user-code` de la víctima a un dominio ajeno.
- **Grant `delegated` eliminado** (`DelegatedAccessGranter`): no estaba en los grants por defecto y canjeaba el `user-code` sin vínculo con el navegador. Cierra la parte `delegated` de [2.5](../issue-2.5-grants-inalcanzables-codigo-muerto.md).
- **SAML** ([SamlResponseValidator](../../../src/main/java/net/civeira/phylax/features/oauth/delegatelogin/domain/provider/SamlResponseValidator.java)): las mismas comprobaciones que php-saml en modo estricto, sobre XML DSig del JDK con validación segura, **sin dependencia nueva** (OpenSAML no está en Maven Central y complica la imagen nativa). Comprueba:
  - parser sin DOCTYPE ni entidades externas;
  - IDs únicos;
  - exactamente una aserción sin cifrar, hija directa de la respuesta;
  - firma envolvente de la respuesta o de esa aserción, con una sola referencia a su elemento y solo las transformaciones *enveloped* y *exc-c14n*;
  - estado `Success`;
  - `InResponseTo` (respuesta y confirmación) igual al ID derivado del `state`, así que las respuestas no solicitadas se rechazan;
  - `Destination` y `Recipient` iguales a la URL del callback;
  - emisor igual al entity ID del IdP;
  - todas las `AudienceRestriction` con el entity ID del SP;
  - ventanas de validez con 2 min de margen.

  La `AuthnRequest` usa ahora el binding HTTP-Redirect correcto (DEFLATE) y lleva el `state` como `RelayState`. Configuración: `saml-idp-sso-url`, `saml-idp-entity-id` y `saml-idp-cert`. Un proveedor sin alguno de los tres deja de ofrecerse. El entity ID del SP es `entity_id` del JSON de `metadata`, o el nombre del proveedor (como en PHP).
- **Google:** OIDC con `state` y PKCE S256. Canjea el código por formulario con `code_verifier` y lee `sub`, `email` y `email_verified` (booleano estricto) de `openidconnect.googleapis.com/v1/userinfo`.
- **Cuentas** ([DelegatedAccessAuthValidatorAdapter](../../../src/main/java/net/civeira/phylax/features/oauth/delegatelogin/infrastructure/driven/DelegatedAccessAuthValidatorAdapter.java)): reglas de PHP 072 sobre la entidad generada `user-federated-identity` ([FederatedIdentityAdapter](../../../src/main/java/net/civeira/phylax/features/oauth/delegatelogin/infrastructure/driven/FederatedIdentityAdapter.java)). Con `NotUniqueException`, gana el enlace concurrente. [FederatedIdentityWithinOneTenant](../../../src/main/java/net/civeira/phylax/features/oauth/delegatelogin/application/listener/FederatedIdentityWithinOneTenant.java) rechaza (`different-tenant`) enlaces con un proveedor de otro tenant. Las cuentas nuevas se llaman como su email verificado y llevan una contraseña aleatoria.
- **Pruebas:** `SamlResponseValidatorUnitTest` (firmas reales: aserción y respuesta válidas, sin firmar, otra clave, manipulada, *signature wrapping*, referencia desplazada, no solicitada, otra petición, emisor, audiencia, destino, caducada, estado, DOCTYPE, comentario dentro del NameID), `DelegateLoginUnitTest`, `DelegatedAccessAuthValidatorAdapterUnitTest`, `GoogleDelegatedAccessProviderUnitTest` y `DelegatedLoginFlowTest` (HTTP: cookie y `state`, callback escapado, `state` desconocido, otro navegador, sin cookie, segundo uso, respuesta rechazada).

## Consecuencias de despliegue

- **SAML:** hay que rellenar `saml-idp-entity-id`, `saml-idp-sso-url` y `saml-idp-cert` en cada proveedor. Antes se usaban `public-key` como URL de SSO, `private-key` como emisor y `certificate`. El IdP debe aceptar el SP con el entity ID nuevo y el ACS `/oauth/openid/-/delegated/authorization/{nombre}`.
- **Logins en curso** al desplegar: fallan una vez y el usuario vuelve a pulsar el proveedor.
- **Cuentas federadas antiguas:** se crearon con `passwordPlain("")`, es decir, con el hash de la contraseña vacía. Hay que comprobar si el login por contraseña acepta una vacía y, en todo caso, poner una contraseña aleatoria a los usuarios con `provider` informado. Estas cuentas tampoco tienen enlace: con `verified-email` se enlazan en el primer acceso por su email; con `none`, solo si son del tipo del único proveedor activo.
- **Plantillas:** el código de `features/oauth` se genera desde las plantillas Quarkus de civi-generator. Hay que llevar estos cambios con `binary/sync-oidc-template.py --apply --include-new` antes de volver a regenerar, o se pierden.
