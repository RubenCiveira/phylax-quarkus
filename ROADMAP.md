# 🗺️ Hoja de Ruta

Este documento describe la dirección de {{project-name}}: las capacidades que tenemos previsto explorar y las mejoras que nos gustaría incorporar en el futuro.

Es una declaración de intenciones, no un compromiso. Los elementos pueden cambiar de orden, reformularse o descartarse según evolucione el proyecto, y ninguno tiene una fecha de entrega garantizada.

---

## 📌 Cómo Leer este Documento

- **Qué hay aquí:** ideas y líneas de trabajo futuras que todavía no se han decidido ni planificado.
- **Qué no hay aquí:**
  - Decisiones técnicas sobre elementos concretos, incluidas las pospuestas: consulta los [registros de decisiones de arquitectura](./docs/adr/).
  - Trabajo pendiente y problemas conocidos: consulta las [issues](./docs/work/issues/).
  - Cambios ya entregados: consulta el [CHANGELOG](./CHANGELOG.md).

Cuando se aborda un elemento, sale de este documento y se convierte en una issue, en un ADR o en ambos.

---

## 💬 Sugerencias

Si quieres proponer una nueva línea de trabajo, abre una issue describiendo el problema que resuelve y a quién beneficiaría. Consulta [CONTRIBUTING](./CONTRIBUTING.md) para más detalles.

---

<!-- Añade los elementos de la hoja de ruta debajo de esta línea. -->

## 🔐 Gestor de identidades (OIDC)

Líneas pospuestas o pendientes del servidor OAuth 2.0 / OpenID Connect. Lo que ya funciona está en la [documentación funcional](./docs/functional/oauth/01-vision-general.md) y sus límites actuales en [Límites y pendientes](./docs/functional/oauth/09-limites-y-pendientes.md).

### Validación

- **OpenID Conformance Suite.** Ejecutar los perfiles *Basic OP*, *Config OP*, *RP-Initiated Logout* y *Back-Channel Logout* con el arnés de `src/test/conformance/`, que está escrito pero nunca se ha ejecutado. Al hacerlo: fijar `SUITE_REF` a una etiqueta, guardar los resultados, corregir los fallos y decidir si el workflow corre en cada PR o solo bajo demanda. Riesgos ya identificados: el login cifra la contraseña y firma el formulario con JavaScript (si el navegador automatizado no lo ejecuta, el login falla); los clientes registrados por DCR pueden ver la pantalla de scopes; hay que confiar la CA interna de Caddy en la suite y el certificado de la suite en la JVM (back-channel logout). Hasta entonces no se declara conformidad.
- **Perfil *Dynamic OP*.** Bloqueado por una decisión: la suite registra clientes sin initial access token y el servidor lo exige siempre (no hay modo abierto). Opciones: aceptar y documentar que el registro falla; un modo `open` por tenant solo para entornos de conformidad; o comprobar si la versión de la suite admite un initial access token configurado.
- **Batería compartida con la versión PHP.** Extender la capa HTTP + MariaDB a login y clientes reales (hoy son dobles), compartir reloj controlado y claves fijas con la batería de lughauth-api y ver en verde el CI (`.github/workflows/ci.yml`, que puede necesitar más variables de `.env`).
- **Pruebas contra sistemas reales.** IdP SAML (firma de respuesta y de aserción) y Google; RP con `jwks_uri` para `private_key_jwt`, request objects, JWE, JARM y UserInfo firmado; servidores de recursos externos que validen DPoP; PAR en un navegador real; varias instancias rotando claves a la vez.

### Firma y criptografía

- **Firma por tenant.** Hoy todos los tenants firman con un juego de claves global (`/oauth/jwks`) para que los microservicios validen con una ubicación fija de SmallRye JWT; el aislamiento lo da el emisor. Se reabre si un tenant exige claves propias (aislamiento criptográfico, BYOK o rotación independiente). Diseño previsto: volver a pasar el tenant a `TokenStoreGateway` desde `JoseTokenSigner`, publicar la JWKS global como unión transitoria durante la migración, anunciar el `jwks_uri` del tenant y documentar para los servicios consumidores la validación dinámica emisor → JWKS.
- **ES256 y EdDSA en la firma del servidor.** Las claves de firma no guardan algoritmo. Se reabre con un RP que lo exija, tras regenerar el modelo con `_oauth_keys_storer.alg` (`RS256`, `ES256` o `EdDSA`; las filas existentes quedan `RS256`) y un índice `(tenant, alg, expiration)`. Diseño previsto: rotación y ventana por algoritmo, JWKS con todas las claves vivas, firma según el algoritmo registrado por el cliente y anuncio solo de los probados.
- **mTLS y tokens ligados a certificado (RFC 8705).** Requiere una terminación TLS de confianza que reenvíe el certificado del cliente; DPoP ya cubre el vínculo de tokens. Se reabre con un perfil FAPI o un RP que lo exija, junto con la decisión de proxy y `mtls_endpoint_aliases`. El modelo ya tiene `tls-client-auth-subject-dn` y `tls-client-certificate-bound-access-tokens`.

### Grants y solicitudes

- **Grant JWT bearer (RFC 7523).** Los sistemas externos usan API key o `private_key_jwt`. Se reabre con el primer sistema externo con identidad de plataforma. Diseño previsto: emisores de confianza por tenant, reglas de claims con roles y `sub` del sistema, y anti-replay por emisor sobre el almacén de `private_key_jwt`.
- **CIBA.** No hay canal de aprobación fuera de banda ni tabla de solicitudes; `/ciba-auth` responde 403 y no se anuncia. Se reabre con un canal aprobado (app móvil o push), especificando la tabla para regenerar el modelo.
- **Rich Authorization Requests (RFC 9396).** No hay tipos de `authorization_details` ni consentimiento granular. Se reabre con el primer tipo aprobado (esquema JSON y su pantalla de consentimiento); el recorrido authorize → code → token de resource indicators y `claims` sirve de patrón.
- **`sector_identifier_uri` verificado.** Descargarlo con la protección SSRF de `RemoteJwksFetcher` si algún cliente pairwise necesita varios hosts de redirección. Hoy DCR lo rechaza.

### Login y cuentas

- **Federación OIDC genérica (Microsoft, Apple, GitHub…).** Descartada mientras no haya demanda: el SSO corporativo va por SAML. Se reabre con un cliente que aporte su propio IdP OIDC. Diseño previsto: RP genérico con discovery, JWKS, code + PKCE, `state` y `nonce`, verificación del ID token y enlace explícito de cuentas por `(proveedor, sub)`, como el de Google y SAML.
- **Verificación de email.** `require-email-verification` no tiene efecto. Diseño previsto: una comprobación más en la cadena de retos del login (tras la contraseña temporal), un paso de verificación con enlace de un solo uso, el email marcado como verificado al aceptar una invitación (ya ocurre) y el `email_verified` del proveedor en el login federado.
- **API keys personales.** La entidad `user-personal-api-key` tiene CRUD de administración, pero no hay gestión de autoservicio desde el perfil ni una vía de autenticación que acepte la clave. Diseño previsto: gestión en el área de perfil, autenticación con la clave (guardada como digest) y scopes limitados a la intersección con los del usuario.
- **Initial access token mostrado una vez.** Hoy el administrador genera el token aparte y guarda su valor en el CRUD; falta una acción que lo genere y lo muestre una sola vez.

### Eventos e integración

- **Registro de auditoría.** La tabla `_oauth_audit_log` existe, pero ningún código escribe en ella. Diseño previsto: un puerto de auditoría con login correcto y fallido, bloqueo de cuenta, MFA, emisión y revocación de tokens, cambio de contraseña, consentimientos y logout.
- **Webhooks de eventos de autenticación.** Notificaciones HTTP salientes (`user.login`, `token.issued`, `token.revoked`…) por tenant, firmadas con HMAC-SHA256 y con reintentos. Se aborda después del registro de auditoría, que fija la taxonomía de eventos.
