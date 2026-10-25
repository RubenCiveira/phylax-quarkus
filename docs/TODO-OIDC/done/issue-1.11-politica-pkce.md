# 1.11 — Política PKCE

- **Fase:** 1 · **Esfuerzo:** S · **Estado:** hecha (25-10-2026), [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md).
- **Implementación:** admitir solo `S256`, exigir PKCE a públicos y decidir política para confidenciales.
- **Cierre:** ausencia de challenge, método `plain` y verifier incorrecto rechazados; confidenciales siguen la política declarada.
- **Fuente:** [§5, 1.11](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).

## Qué hace (funcional)

- En authorize, **antes del login**: solo `S256`. Un `code_challenge` sin método (que significa `plain`) o con `plain` es `invalid_request`, igual que un reto mal formado (43–128 caracteres no reservados).
- Obligatorio para clientes públicos y para los que registran `require-pkce`; para el resto de confidenciales, `oauth.pkce.require-for-confidential` (por defecto `false`) lo hace obligatorio para todos.
- La misma política al canjear: un confidencial que exige PKCE no canjea un code sin reto; un verifier para un code sin reto sigue siendo un *downgrade* rechazado (J-01).

## Implementación Java

- `AuthorizationRequestPolicy` (compartida con el push PAR), `ClientRetrieveAdapter.requiresPkce`, `AuthorizationCodeGrant.codeMatches`.
- **Cambio de contrato:** clientes que enviaban `code_challenge` sin `code_challenge_method` reciben ahora `invalid_request` en authorize (antes fallaban más tarde, en el canje).
- **Pruebas:** `AuthorizationRequestPolicyUnitTest`, `AuthorizeRequestFlowTest`, `PkceFlowTest`.
