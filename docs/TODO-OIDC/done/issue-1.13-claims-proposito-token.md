# 1.13 — Claims según propósito del token

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** hecha (25-10-2026), [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md).
- **Implementación:** reflejar `auth_time`, `acr` y `amr` alcanzados; emitir `at_hash`/`c_hash` solo cuando correspondan y separar claims ID/access.
- **Cierre:** claims por flujo comprobados, sin contaminación entre tipos; `sub` de UserInfo coincide con ID token.
- **Dependencias:** [1.2](issue-1.2-id-token-conforme.md), [1.5](issue-1.5-userinfo-conforme.md) y [1.7](issue-1.7-politica-implicit-hybrid.md).
- **Fuente:** [§5, 1.13](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).

## Qué hace (funcional)

| Claim | Access token | ID token | Refresh token |
|---|---|---|---|
| `auth_time` | login real | login real | login real (para rotar) |
| `amr` / `acr` | reales / derivado de `amr` | reales / derivado | `amr` (para rotar) |
| `sid` | — | sí | sí |
| `at_hash` | — | si la respuesta lleva access token | — |
| `c_hash` | — | solo híbrido | — |
| roles, scope, `client_id` | sí | no | scope concedido |

- **`acr`** (decisión PHP 067): dos niveles derivados del `amr`, configurables con `security.acr.single-factor` / `security.acr.multi-factor` (por defecto `urn:lugh:acr:1fa` / `urn:lugh:acr:mfa`, iguales que PHP). Sustituye al `acr` numérico (`0`, `1`, `2`).
- Un canje de code o un refresh ya no cuentan como autenticación: conservan `auth_time`, `amr` y `sid` del login original (antes ponían `auth_time = ahora` y `acr = 0`).
- Los pasos del login acumulan los métodos probados en la pre-sesión (`ChallengesState.amr`): `pwd` tras la contraseña, `otp`/`mfa` tras el TOTP, los de la sesión en el step-up.
- `client_credentials` no lleva datos de autenticación de usuario.

## Implementación Java

- `AuthenticationData.amr`/`sid`, `AuthenticationMode.amr()`, `ChallengesState.withAmr`/`provenAmr`, `AcrPolicy`, `ClaimsMapper` (`accessToken`, `refreshToken`, `idToken`), `AuthorizationCodeGrant` y `RefreshTokenGrant`.
- **Límite:** un login federado o por enlace mágico no aporta `amr` propio (queda vacío y `acr` de un factor).
- **Pruebas:** `IdTokenClaimsFlowTest`, `ClaimsMapperUnitTest`, `AcrPolicyUnitTest`, `ChallengesStateUnitTest`. `sub` de UserInfo frente al ID token se comprueba en [1.5](issue-1.5-userinfo-conforme.md) (J-04).
