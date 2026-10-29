# J-02 — Clientes, tenant y secretos

**Avance (24-10-2026):** [0.3](../../done/issue-0.3-audiencias-autorizadas.md) y [0.10](../../done/issue-0.10-redirect-uri-estricta.md) hechas al cerrar [J-01](ep-J-01-token-y-grants-seguros.md).

**Decisión (24-10-2026):** firmante por tenant pospuesto ([2.11](../../pospuestos/issue-2.11-firmante-por-tenant.md)). Todos los tenants firman con un juego de claves global publicado en `/oauth/jwks`; el aislamiento entre tenants se apoya en el `iss` exacto, no en la clave.

**Prioridad:** bloqueante · **Tareas:** [0.3](../../done/issue-0.3-audiencias-autorizadas.md), [0.4](../../done/issue-0.4-configuracion-por-cliente.md), [0.5](../../done/issue-0.5-aislamiento-clientes-tenant.md), [0.10](../../done/issue-0.10-redirect-uri-estricta.md), [2.1](../../done/issue-2.1-registro-dinamico-clientes.md), [2.4](../../done/issue-2.4-redirects-logout.md), [2.8](../../done/issue-2.8-proteccion-secretos.md), [2.9](../../done/issue-2.9-migracion-secretos-cifrado.md). **Desbloquea:** política común de J-01, J-03, J-05 y J-07.

**Estado:** cerrada (26-10-2026). Fichas en [`done/`](../../done/README.md). Modelo regenerado e integración de redirects post-logout verificada con `make test-mariadb`.

## Implementación

1. Decidir cliente tenant o global explícito; diseñar transición de datos de `TrustedClient`, sin consulta solo por `code`. Aplicar grants, response types, auth method, scopes M2M y audiencias de cliente; password nunca por defecto.
2. Restringir redirects a coincidencia exacta (excepción loopback controlada); separar redirects post-logout. Probar tenant A/B y clientes confidenciales/públicos.
3. Revisar verificador no reversible de secretos y clave AES/contraseñas; migrar sin exponer secreto ni romper credenciales existentes. Exponer DCR RFC 7591/7592 solo con initial access token real, tenant y metadata restringidos; actualizar discovery en J-09 tras pruebas.

**Aceptación:** cliente de A no se usa en B; grant/scope/audience/redirect ajenos rechazados; secreto no recuperable en almacenamiento ni lectura DCR; credencial caducada/agotada no registra clientes.

**Referencia PHP:** [EP-02 secretos](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/EP-02-secretos-y-cifrado.md) pendiente y `done/EP-03-clientes-tenant-dcr.md` ya ejecutada en PHP. En Java `ClientRetrieveAdapter` aún carga defaults globales y DCR es 403.

## Lo construido (documentación funcional)

### Qué cliente ve cada tenant

Un cliente es de un tenant o global (`tenant = null`). Desde un tenant se ve su propio cliente o, si no tiene uno con ese código, el global; **nunca el de otro tenant**: no autentica, no obtiene codes ni tokens, no recibe back-channel logout, ni comparte consentimientos o roles. Los creados por DCR nacen en el tenant de la ruta.

### La configuración del cliente manda

| Campo | Efecto | Por defecto |
|---|---|---|
| `grantTypesJson` | Grants en `/token` (también code y refresh); sin `refresh_token` no hay refresh token | code + refresh, **nunca password** |
| `responseTypesJson` | `response_type` en authorize (`unauthorized_client`) | `code` |
| `allowAllScopes` | Cualquier scope en flujos de usuario | solo OIDC estándar |
| `allowedScopesM2m` | Scopes de `client_credentials` (`invalid_scope` si ninguno) | ninguno |
| URIs post-logout | Únicos destinos tras logout (exactos) | ninguno |

### Registro dinámico (RFC 7591/7592)

`POST /connect` con initial access token (del tenant, vigente, con usos; consumo atómico tras validar metadatos), `GET|PUT|DELETE /connect/{id}` con registration access token y solo desde el tenant del cliente. El secreto solo se ve en el alta.

### Secretos

Secretos de cliente y tokens DCR: HMAC + *pepper* versionado, comparación en tiempo constante. Contraseñas: Argon2id. Valores heredados cifrados con AES: migración en bloque al arrancar (`LegacyCredentialMigration`).

### Cambios de contrato

Clientes sin `grantTypesJson` pierden password/client_credentials/device; sin `allowAllScopes`, solo scopes OIDC; `client_credentials` exige `allowedScopesM2m`; authorize exige `response_type`; logout solo redirige a URIs post-logout registradas; DCR exige siempre initial access token.

### Dónde está el código

`client/infrastructure/driven/TrustedClientPolicy`, `ClientRetrieveAdapter`, `PostLogoutRedirectSqlStore`, `DynamicClientAdapter`, `DcrInitialAccessTokenSqlAdapter`; `client/domain/DcrMetadataPolicy`, `DcrRegistrationRules`; `ClientRegisterController`; `bootstrap/install/LegacyCredentialMigration`. Pruebas: `TrustedClientPolicyUnitTest`, `DcrMetadataPolicyUnitTest`, `RegisterClientUsecaseUnitTest`, `BackChannelLogoutDispatcherUnitTest`, `ClientPolicyFlowTest`, `DynamicClientRegistrationMariaDbTest`.
