# Cambios de modelo para J-02, J-03 y J-05 (regenerados)

El modelo regenerado ya incluye los tres cambios en `src/main/resources/migration/mysql/000.001.001/20261025211629-model.changelog.mysql.sql`. La composición generada se llama `allowed-logout-redirects`, con tabla `access_trusted_client_allowed_logout_redirect`; el adaptador Java usa ese nombre. La entidad `dcr-initial-access-token` ([2.1](done/issue-2.1-registro-dinamico-clientes.md)) ya existía.

| # | Cambio | Ficha | Tipo |
|---|---|---|---|
| 1 | Composición `allowed-logout-redirects` en `trusted-client` | [2.4](done/issue-2.4-redirects-logout.md) | Maestro (`access_*`) |
| 2 | Columna `_oauth_session.sid` (**generada, aún no usada por J-03**) | [1.14](issue-1.14-hint-sid-logout.md) | Estado OAuth (`_oauth_*`) |
| 3 | Tabla `_oauth_replay_jti` | [3.3](done/issue-3.3-request-objects-firmados.md), [3.2](issue-3.2-autenticacion-jwt-cliente.md) | Estado OAuth (`_oauth_*`) |

## 1. Composición `allowed-logout-redirects` en `trusted-client`

Igual que `allowed-redirects`, en una tabla propia. Separa los destinos post-logout (OIDC RP-Initiated Logout §2, `post_logout_redirect_uris`) de los redirects del login.

```sql
CREATE TABLE access_trusted_client_allowed_logout_redirect (
  uid     VARCHAR(255) NOT NULL,
  version INT          NOT NULL,
  url     VARCHAR(255) NOT NULL,
  client  VARCHAR(255) NOT NULL,   -- FK access_trusted_client(uid), ON DELETE RESTRICT
  CONSTRAINT PK_ACCESS_TRUSTED_CLIENT_ALLOWED_LOGOUT_REDIRECT PRIMARY KEY (uid)
);
CREATE INDEX FL_TRUSTED_CLIENT_ALLOWED_LOGOUT_REDIRECT_CLIENTS
  ON access_trusted_client_allowed_logout_redirect(client);
```

La FK generada usa `ON DELETE RESTRICT`: DCR elimina las URIs antes de borrar el cliente. **Migración de datos:** ninguna. Los clientes que necesiten volver a su aplicación tras el logout deben registrar la URI en la composición.

## 2. Columna `_oauth_session.sid` (opcional)

J-03 guarda el `sid` público dentro de `_oauth_session.auth_data`, que sobrevive a la rotación de la cookie. La columna también fue generada; J-07 podrá usarla si necesita buscar sesiones por `sid`:

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
  CONSTRAINT PK__OAUTH_REPLAY_JTI PRIMARY KEY (jti, tenant_id, client_id, purpose)
);
CREATE INDEX IDX_OAUTH_REPLAY_JTI_EXPIRES ON _oauth_replay_jti(expires_at);
```

PHP usa `ENUM` para `purpose`; aquí basta `VARCHAR(32)` (los valores los fija el código). `OAuthCleanupScheduler` borra las filas vencidas.

## Comportamiento antes de aplicar las migraciones

- **Post-logout:** sin la tabla, ninguna URI post-logout se acepta y DCR no confirma un alta con URIs que no pudo persistir.
- **JAR:** sin la tabla, todo request object devuelve `invalid_request_object` (fallo cerrado).
