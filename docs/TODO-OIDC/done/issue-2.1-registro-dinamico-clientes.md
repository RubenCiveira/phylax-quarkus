# 2.1 — Registro dinámico de clientes

- **Fase:** 2 · **Esfuerzo:** M · **Estado:** hecha (25-10-2026), [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md).
- **Decisión heredada (PHP EP-03):** el initial access token es una entidad propia `dcr-initial-access-token` con digest (no un JWT `dcr:register`), emitida por un admin (de tenant, para su tenant; root, indicando tenant) y devuelta en claro una sola vez; consumo atómico de usos antes de crear el cliente y después de validar los metadatos. Grants por defecto `authorization_code` + `refresh_token`; `password` exige permiso explícito; métodos de autenticación = los del tenant ∩ los que el token endpoint verifica.
- **Implementación:** exponer RFC 7591/7592 usando los casos de uso existentes; validar `initial_access_token`, tenant, grants y metadata.
- **Cierre:** alta, lectura, modificación y borrado probados; credencial caducada/agotada, otro tenant y metadata inválida rechazados. Publicar endpoint en discovery solo tras implementarlo.
- **Fuente:** [§5, 2.1](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).

## Qué hace (funcional)

| Endpoint | Credencial | Resultado |
|---|---|---|
| `POST /oauth/openid/{tenant}/connect` (RFC 7591) | `Authorization: Bearer <initial access token>` | 201 con `client_id`, `client_secret` (solo confidenciales), `registration_access_token`, `registration_client_uri` y metadatos normalizados |
| `GET /oauth/openid/{tenant}/connect/{clientId}` (RFC 7592) | `Bearer <registration access token>` | 200 con metadatos, **nunca el secreto** |
| `PUT …/connect/{clientId}` | ídem | 200; el `client_id` del cuerpo debe coincidir; no se cambia secreto ni método de autenticación |
| `DELETE …/connect/{clientId}` | ídem | 204 |

- **Tenant:** `dynamic-registration-policy` ausente o `disabled` → 403 `access_denied`; cualquier otro valor activa DCR, **siempre** con initial access token (el modo `open` deja de existir).
- **Initial access token:** entidad generada `dcr-initial-access-token` (digest `SecretDigester`). Debe ser del tenant, no revocado, no caducado y con usos. Fallo: 401 `invalid_token` con descripción propia. Los metadatos se validan **antes** de gastar el uso, y el uso se gasta con un `UPDATE` condicional (dos altas simultáneas con `max_uses = 1`: una gana).
- **Metadatos** (`DcrMetadataPolicy`): grants conocidos y permitidos (`allowed_grant_types` del token prevalece sobre `dcr-allowed-grant-types`; `password` exige además `allow-password-grant`); solo `response_type=code`; métodos `client_secret_basic`, `client_secret_post` o `none` cruzados con los del tenant; un cliente `none` no pide `client_credentials` ni `password`; redirects exactos (HTTPS, HTTP solo loopback, o esquema privado de app nativa), sin fragmento ni comodín; URIs de logout e información HTTPS.
- El cliente nace en el tenant de la ruta, habilitado, sin `allowAllScopes`; uno público permite acceso público y exige PKCE.

## Implementación Java

- Dominio: `DcrRegistrationRules`, `DcrMetadataPolicy`, `DcrInitialAccessToken`, puerto `DcrInitialAccessTokenGateway`; `DynamicClientException` con estado HTTP.
- Infraestructura: `DcrInitialAccessTokenSqlAdapter`, `DynamicClientAdapter` reescrito (antes insertaba sin tenant, sin las columnas `NOT NULL` y con el método en minúsculas), `ClientRegisterController` (antes 403 fijo).
- **Pruebas:** `DcrMetadataPolicyUnitTest`, `RegisterClientUsecaseUnitTest`, `DynamicClientRegistrationMariaDbTest` (MariaDB real).
- **Pendiente:** acción `issue` que genere el token y lo muestre una vez (hoy el admin lo crea con el CRUD generado, que guarda su digest); `post_logout_redirect_uris` se persiste en cuanto exista la tabla de [cambios de modelo](../cambios-modelo-J02-J05.md); discovery y *Dynamic OP* en J-09 ([V.3](../issue-V.3-conformidad-dcr.md)).
