# Implementadas

Fichas implementadas en Java y cubiertas por pruebas automáticas (unitarias y HTTP con dobles en memoria). La última columna recoge la verificación contra sistemas reales que queda por hacer; no bloquea el cierre, pero hay que hacerla antes de desplegar.

| Ficha | Épica | Verificación real pendiente |
|---|---|---|
| [0.1 Canje seguro de authorization code](issue-0.1-canje-seguro-de-authorization-code.md) | [J-01](../epicas/done/ep-J-01-token-y-grants-seguros.md) | Hecha (24-10-2026) en el arnés [V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md). Queda revocar los tokens del primer canje ante un segundo intento |
| [0.2 Refresh ligado al cliente y a la sesión](issue-0.2-refresh-cliente-sesion.md) | [J-01](../epicas/done/ep-J-01-token-y-grants-seguros.md) | Hecha (24-10-2026) en el arnés [V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md), con la corrección del barrido por tenant |
| [0.9 Autenticación de cliente unificada](issue-0.9-autenticacion-cliente-unificada.md) | [J-01](../epicas/done/ep-J-01-token-y-grants-seguros.md) | Device y PAR con un cliente público y uno confidencial reales (el perfil de test los excluye); clientes existentes cuyo método registrado no coincida con el que usan |
| [0.13 Ruta de revocación no duplicada](issue-0.13-ruta-revocacion-no-duplicada.md) | [J-01](../epicas/done/ep-J-01-token-y-grants-seguros.md) | Confirmar que la UI de login usa `/revoke` y no `/revocation` |
| [1.4 Errores y caché de token](issue-1.4-errores-cache-token.md) | [J-01](../epicas/done/ep-J-01-token-y-grants-seguros.md) | Avisar a integradores de `password` (400 `invalid_grant`) y `mfa_required` (403) |
| [1.8 Code flow para clientes confidenciales](issue-1.8-code-clientes-confidenciales.md) | [J-01](../epicas/done/ep-J-01-token-y-grants-seguros.md) | Login real de una web app confidencial sin `publicAllow` |
| [1.16 Emisión y canje fuera del TokenController](issue-1.16-emision-canje-fuera-tokencontroller.md) | [J-01](../epicas/done/ep-J-01-token-y-grants-seguros.md) | — (refactorización cubierta por la batería HTTP) |
| [0.3 Audiencias autorizadas](issue-0.3-audiencias-autorizadas.md) | [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md) (cerrada en J-01) | Registrar en `allowedAudiences` las audiencias que ya piden los clientes existentes (antes se ignoraban en el cuerpo de `/token`) |
| [0.10 Redirect URI estricta](issue-0.10-redirect-uri-estricta.md) | [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md) (cerrada en J-01) | Revisar redirects registrados con `*` o `/*` fuera de loopback antes de desplegar |
| [1.10 Tests negativos de seguridad](issue-1.10-tests-negativos-seguridad.md) | [J-01](../epicas/done/ep-J-01-token-y-grants-seguros.md) | — (HTTP + MariaDB con `make test-mariadb`) |
| [0.16 Seguridad del login federado](issue-0.16-seguridad-login-federado.md) | [J-08](../epicas/ep-J-08-login-consentimiento-producto.md) | Login real con un IdP SAML (firma de respuesta y de aserción) y con Google; migrar la configuración SAML de los proveedores existentes |
| [0.4 Configuración efectiva por cliente](issue-0.4-configuracion-por-cliente.md) | [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md) | Clientes existentes sin `grantTypesJson` o `allowAllScopes`: revisar qué grants y scopes necesitan antes de desplegar |
| [0.5 Aislamiento de clientes por tenant](issue-0.5-aislamiento-clientes-tenant.md) | [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md) | Asignar tenant a los clientes que no deban ser globales |
| [2.1 Registro dinámico de clientes](issue-2.1-registro-dinamico-clientes.md) | [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md) | Acción `issue` del initial access token; persistir `post_logout_redirect_uris` tras regenerar el modelo; *Dynamic OP* (V.3) |
| [2.4 Redirects propios de logout](issue-2.4-redirects-logout.md) | [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md) | **Regenerar el modelo** (tabla post-logout) y registrar las URIs post-logout de cada cliente |
| [2.8 Protección de secretos](issue-2.8-proteccion-secretos.md) | [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md) | — |
| [2.9 Migración de secretos y cifrado en reposo](issue-2.9-migracion-secretos-cifrado.md) | [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md) | Arranque contra una base con credenciales heredadas (revisar log de la migración) |
| [0.7 Clave pública y pre-sesión](issue-0.7-clave-publica-y-presesion.md) | [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md) | **Rotar las claves de firma** (expuestas en cookies de pre-sesión) |
| [1.2 ID token básico conforme](issue-1.2-id-token-conforme.md) | [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md) | Conformance *Basic OP* (J-09) |
| [1.13 Claims según propósito del token](issue-1.13-claims-proposito-token.md) | [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md) | `amr` de login federado y enlace mágico |
| [1.15 Scopes como conjuntos exactos](issue-1.15-scopes-conjuntos-exactos.md) | [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md) | — |
| [2.6 Generación de claves fuera de la petición](issue-2.6-generacion-claves-fuera-peticion.md) | [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md) | Varias instancias rotando a la vez |
| [3.5 Perfil at+jwt](issue-3.5-perfil-at-jwt.md) | [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md) | Adaptar consumidores externos antes de activar `oauth.jwt.access-token.rfc9068` |
| [0.11 State seguro y secreto fuera de PAR](issue-0.11-state-seguro-secreto-fuera-par.md) | [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md) | — |
| [0.14 PAR vinculado y de un solo uso](issue-0.14-par-vinculado-un-solo-uso.md) | [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md) | PAR real con un navegador (cookie `PAR_BINDING` con `Secure`) |
| [1.11 Política PKCE](issue-1.11-politica-pkce.md) | [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md) | Avisar a clientes que envían `code_challenge` sin método |
| [2.3 Completar política de PAR](issue-2.3-politica-par.md) | [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md) | Device/PAR con clientes públicos reales |
| [2.7 Hints en authorize](issue-2.7-hints-authorize.md) | [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md) | — |
| [3.3 Request objects firmados](issue-3.3-request-objects-firmados.md) | [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md) | **Regenerar el modelo** (`_oauth_replay_jti`); `jwks_uri` real de un cliente |
| [3.10 Hints, ACR y localización](issue-3.10-hints-acr-localizacion.md) | [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md) | APIs deben emitir el `acr` URN (J-04); alta de TOTP en step-up |
