# 1.10 — Tests negativos de seguridad

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** parcial (24-10-2026): code y refresh cubiertos por HTTP y su concurrencia verificada contra MariaDB ([V.1](issue-V.1-fixtures-pruebas-http-persistencia.md)); faltan audiencia ajena (espera a [0.3](issue-0.3-audiencias-autorizadas.md)) y redirect estricto (espera a [0.10](issue-0.10-redirect-uri-estricta.md)).
- **Implementación:** pruebas HTTP + persistencia de code ajeno/doble canje, refresh robado/concurrente, audiencia ajena y redirect no registrado.
- **Cierre:** los casos negativos fallan correctamente y los positivos continúan funcionando, incluidos los escenarios concurrentes.
- **Dependencias:** [0.1](done/issue-0.1-canje-seguro-de-authorization-code.md), [0.2](done/issue-0.2-refresh-cliente-sesion.md), [0.3](issue-0.3-audiencias-autorizadas.md) y [0.10](issue-0.10-redirect-uri-estricta.md).
- **Fuente:** [§5, 1.10](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).

## Qué se garantiza hoy (funcional)

| Ataque | Resultado comprobado |
|---|---|
| Canjear dos veces el mismo code | El segundo recibe `invalid_grant` |
| Canjear un code en 8 peticiones simultáneas | Exactamente una obtiene tokens; el resto `invalid_grant` |
| Canjear el code de otro cliente, aun autenticándose bien | `invalid_grant`, y el dueño legítimo aún puede canjearlo |
| Canjear el code en otro tenant donde existe el mismo `client_id` | `invalid_grant`, el code sigue válido en su tenant |
| `/authorize` con `redirect_uri` no registrada | No se emite code ni se redirige a ella |
| Usar el refresh de otro cliente | `invalid_grant`, y el dueño puede seguir rotándolo |
| Usar un refresh en otro tenant | `invalid_grant` (comprobación de `iss`) |
| Rotar un refresh en 8 peticiones simultáneas | Exactamente una gana y la reutilización revoca la familia: también el refresh recién emitido al ganador queda inválido |

Además, en `TokenGrantSecurityTest` (0.1/0.2): redirect ajena, secreto erróneo, público sin PKCE, ampliar scope, refresh tras logout y replay secuencial.

## Implementación Java (24-10-2026)

- `TokenGrantNegativeSecurityTest` (nuevo), con un segundo tenant (`other-tenant`) y un segundo cliente confidencial (`web-client`) en `FixedClientStoreGateway`.
- Los escenarios concurrentes HTTP corren contra `FixedTemporalKeysGateway` (`ConcurrentHashMap.remove`) y `FixedTokenRevocationGateway` (`Set.add`): demuestran que el caso de uso no deja ventana entre comprobar y consumir. Que el `DELETE` con filas afectadas y el `INSERT IGNORE` lo garantizan en MariaDB lo prueban `TemporalKeysSqlAdapterMariaDbTest` y `TokenRevocationSqlAdapterMariaDbTest` ([V.1](issue-V.1-fixtures-pruebas-http-persistencia.md)).
- El perfil de test eleva el límite de peticiones por IP (`app.rate-limit.*`), que estas pruebas agotaban.
- **Corregido al verificar en MariaDB:** el barrido de `revokeAllForUser` no filtraba por tenant (y por la PK de `_oauth_revoked_jti` hacía que la siguiente rotación legítima del otro tenant se tomase por replay) ni revocaba los access token; ahora filtra por el uid del tenant de la ruta e incluye los JTI de acceso.
- **Pendiente:** audiencia ajena en code/password/device tras 0.3; comodines de redirect tras 0.10.
