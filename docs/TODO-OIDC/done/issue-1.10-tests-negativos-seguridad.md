# 1.10 — Tests negativos de seguridad

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** hecha (24-10-2026).
- **Implementación:** pruebas HTTP + persistencia de code ajeno/doble canje, refresh robado/concurrente, audiencia ajena y redirect no registrado.
- **Cierre:** los casos negativos fallan correctamente y los positivos continúan funcionando, incluidos los escenarios concurrentes.
- **Dependencias:** [0.1](issue-0.1-canje-seguro-de-authorization-code.md), [0.2](issue-0.2-refresh-cliente-sesion.md), [0.3](issue-0.3-audiencias-autorizadas.md) y [0.10](issue-0.10-redirect-uri-estricta.md).
- **Fuente:** [§5, 1.10](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).

## Qué se garantiza (funcional)

| Ataque | Resultado comprobado | HTTP con dobles | HTTP + MariaDB |
|---|---|:---:|:---:|
| Canjear dos veces el mismo code | El segundo recibe `invalid_grant` | ✓ | ✓ |
| Canjear un code en 8 peticiones simultáneas | Una sola obtiene tokens; la fila del code desaparece | ✓ | ✓ |
| Canjear el code de otro cliente, en otro tenant o con otra redirect | `invalid_grant` y el code sigue en la base de datos para su dueño | ✓ | ✓ |
| Usar el refresh de otro cliente o en otro tenant | `invalid_grant` y el JTI no se consume | ✓ | ✓ |
| Rotar un refresh en 8 peticiones simultáneas | Una sola gana; el reuso revoca la familia (fila en `_oauth_revoked_jti`) y el refresh del ganador | ✓ | ✓ |
| Refresh de una sesión terminada | `invalid_grant` | ✓ | ✓ |
| Audiencia ajena en authorize, password, client_credentials y `/device` | `invalid_target`, sin validar credenciales del usuario | ✓ | — |
| Code o refresh que intentan ampliar la audiencia | Se conserva la original | ✓ | — |
| Redirect no registrada, con prefijo, query o `..` | Ni code ni redirección | ✓ (+ unitario) | — |

Además, en `TokenGrantSecurityTest` (0.1/0.2): secreto erróneo, público sin PKCE, ampliar scope y replay secuencial.

## Implementación Java (24-10-2026)

- **HTTP con dobles** (`mvn test`): `TokenGrantNegativeSecurityTest`, con dos tenants y tres clientes en `FixedClientStoreGateway`, que aplica la `RedirectUriPolicy` de producción y audiencias registradas.
- **HTTP + MariaDB** (`make test-mariadb`, [V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md)): `TokenEndpointMariaDbTest` repite las carreras y los casos ajenos de code y refresh a través de `POST /token`, con los almacenes de codes, JTI consumidos/revocados y sesiones en un esquema MariaDB desechable, y comprueba las filas. Lo completan las pruebas de adaptadores `TemporalKeysSqlAdapterMariaDbTest` y `TokenRevocationSqlAdapterMariaDbTest`.
- Audiencia y redirect se deciden antes de tocar la persistencia, por eso no tienen variante MariaDB.
- **Corregido al verificar en MariaDB:** el barrido de `revokeAllForUser` no filtraba por tenant ni revocaba access tokens.
- El perfil de test eleva el límite de peticiones por IP (`app.rate-limit.*`), que estas pruebas agotaban.
