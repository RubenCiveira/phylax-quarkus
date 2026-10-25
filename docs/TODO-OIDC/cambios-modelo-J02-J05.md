# Cambios de modelo para J-02, J-03 y J-05

Petición de regeneración de los maestros (`phylax-model`, plantilla `multitenant-oidc`). Los nombres coinciden con los que ya genera la misma plantilla en PHP (`lughauth-model`), salvo el punto 1, que PHP no tiene. La entidad `dcr-initial-access-token` ([2.1](done/issue-2.1-registro-dinamico-clientes.md)) **ya existe** en Java con el digest de `SecretDigester`; solo se recomienda ocultar `uses` en la fachada (calculado), porque el token se emite con la acción manual `issue`. El código Java escrito a mano en `features/oauth` usa exactamente estos nombres de tabla y columna mediante JDBC, así que funciona en cuanto exista el esquema; hasta entonces las rutas que dependen de él fallan de forma controlada (ver «Comportamiento sin esquema»).

| # | Cambio | Ficha | Tipo |
|---|---|---|---|
| 1 | Composición `allowed-post-logout-redirects` en `trusted-client` | [2.4](done/issue-2.4-redirects-logout.md) | Maestro (`access_*`) |
| 2 | Columna `_oauth_session.sid` (**opcional**) | [1.14](issue-1.14-hint-sid-logout.md) | Estado OAuth (`_oauth_*`) |
| 3 | Tabla `_oauth_replay_jti` | [3.3](done/issue-3.3-request-objects-firmados.md), [3.2](issue-3.2-autenticacion-jwt-cliente.md) | Estado OAuth (`_oauth_*`) |

## 1. Composición `allowed-post-logout-redirects` en `trusted-client`

Igual que `allowed-redirects`, en una tabla propia. Separa los destinos post-logout (OIDC RP-Initiated Logout §2, `post_logout_redirect_uris`) de los redirects del login.

```sql
CREATE TABLE access_trusted_client_allowed_post_logout_redirect (
  uid     VARCHAR(255) NOT NULL,
  version INT          NOT NULL,
  url     VARCHAR(255) NOT NULL,
  client  VARCHAR(255) NOT NULL,   -- FK access_trusted_client(uid), borrado en cascada como allowed-redirects
  CONSTRAINT PK_ACCESS_TRUSTED_CLIENT_ALLOWED_POST_LOGOUT_REDIRECT PRIMARY KEY (uid)
);
CREATE INDEX FL_TRUSTED_CLIENT_ALLOWED_POST_LOGOUT_REDIRECT_CLIENT
  ON access_trusted_client_allowed_post_logout_redirect(client);
```

**Migración de datos:** ninguna. Un cliente sin URIs post-logout no puede redirigir tras el logout (se muestra la página de sesión cerrada); los que lo necesiten deben registrarlas. Es un cambio de contrato documentado en 2.4.

## 2. Columna `_oauth_session.sid` (opcional)

J-03 guarda el `sid` público dentro de `_oauth_session.auth_data`, que sobrevive a la rotación de la cookie, así que **no bloquea nada**. La columna solo hace falta si J-07 necesita buscar sesiones por `sid` (logout por `sid` de un `id_token_hint`):

```sql
ALTER TABLE _oauth_session ADD COLUMN sid VARCHAR(64) NULL;
CREATE UNIQUE INDEX UK_OAUTH_SESSION_SID ON _oauth_session(sid);
```

## 3. Tabla `_oauth_replay_jti`

Registro de un solo uso para JWT de cliente (request objects JAR; después, `private_key_jwt`). La clave primaria compuesta hace atómico el «primer uso gana».

```sql
CREATE TABLE _oauth_replay_jti (
  jti        VARCHAR(255) NOT NULL,
  tenant_id  VARCHAR(36)  NOT NULL,
  client_id  VARCHAR(250) NOT NULL,
  purpose    VARCHAR(32)  NOT NULL,   -- request_object | client_assertion
  expires_at datetime     NOT NULL,
  CONSTRAINT PK__OAUTH_REPLAY_JTI PRIMARY KEY (tenant_id, client_id, purpose, jti)
);
CREATE INDEX IDX_OAUTH_REPLAY_JTI_EXPIRES ON _oauth_replay_jti(expires_at);
```

PHP usa `ENUM` para `purpose`; aquí basta `VARCHAR(32)` (los valores los fija el código). `OAuthCleanupScheduler` borra las filas vencidas.

## Comportamiento sin esquema

Hasta regenerar:

- **Post-logout:** sin la tabla, ninguna URI post-logout se acepta (fallo cerrado).
- **JAR:** sin la tabla, todo request object se rechaza (fallo cerrado).
