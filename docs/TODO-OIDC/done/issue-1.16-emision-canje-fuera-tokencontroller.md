# 1.16 — Emisión y canje fuera del TokenController

- **Fase:** 1 · **Esfuerzo:** M–L · **Estado:** hecha (24-10-2026).
- **Implementación:** caracterizar flujos code, refresh, device, password y client_credentials antes de refactorizar. Llevar validación/canje y refresh a casos de uso o granters coherentes con los existentes; mantener `ClaimsMapper` y `JwtTokenBuilder` como punto único de emisión por tipo. Evitar duplicar emisión en controller y respuesta HTML.
- **Cierre:** mismos contratos HTTP positivos durante la extracción, rutas negativas de [0.1](issue-0.1-canje-seguro-de-authorization-code.md)/[0.2](issue-0.2-refresh-cliente-sesion.md) centralizadas, sin `RefreshGranter` inalcanzable ([2.5](issue-2.5-grants-inalcanzables-codigo-muerto.md)).
- **Plan PHP:** [038-extraer-emision-tokens.md](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/038-extraer-emision-tokens.md); adaptar la idea, **no** crear un segundo mapper ya existente en Java.

## Qué hace (funcional)

Sin cambio visible para un cliente que ya funcionaba: los mismos grants emiten los mismos tokens. Lo que cambia es dónde vive cada regla, para que cada grant tenga un único sitio donde se decide si emite.

```
POST /token ─► TokenController (HTTP: formulario, credenciales, cabeceras, JSON)
                 └─► IssueTokenUsecase
                       1. grant_type presente y conocido
                       2. ClientAuthenticator (política según el grant)
                       3a. authorization_code ─► AuthorizationCodeGrant ─┐
                       3b. refresh_token      ─► RefreshTokenGrant      ─┤
                       3c. password, mfa, client_credentials, device     │
                           └─► grant permitido + TokenGranter            │
                                                                         ▼
                                              TokenIssuer ─► JwtTokenBuilder/ClaimsMapper
                                              (emite y liga los JTI a la sesión)
```

## Implementación Java (24-10-2026)

- **Nuevo paquete** `features/oauth/authentication/application/token`:
  - `IssueTokenUsecase`: orden de comprobaciones y elección de política de cliente; traduce el resultado de los `TokenGranter` (credenciales rechazadas → `invalid_grant`, MFA pendiente → `mfa_required`).
  - `AuthorizationCodeGrant`: el canje de 0.1 (`codeMatches`, consumo atómico) y 1.8.
  - `RefreshTokenGrant`: la rotación de 0.2 (familia, sesión, scope, consumo atómico del JTI, revocación ante replay).
  - `TokenIssuer`: antiguo `emitAndBindToken`; registra los JTI en la sesión del code o sustituye los del refresh rotado.
  - `TokenGrantRequest` y `TokenParams`: petición ya parseada y troceo común de parámetros y scopes.
- `TokenController` queda en ~80 líneas: construye la petición, llama al caso de uso y serializa con `no-store`. Se borran los DTO muertos (`ChPassRequestDto`, `RecoverPassRequestDto`, `RegisterUserRequestDto`).
- **Eliminado:** `RefreshGranter` (inalcanzable: el controller resolvía `refresh_token` antes de consultar granters) y `verifyRefresh`, que solo él usaba.
- **Cambio de orden en refresh:** ahora se autentica al cliente antes de verificar la firma del token (antes al revés). Un refresh falso con cliente erróneo da `invalid_client` en lugar de `invalid_grant`; ninguno gasta el token.
- **Defensa añadida:** `TokenVerifier::verifyRefreshInfo` exige que el `iss` del refresh sea el del tenant de la ruta. Antes solo lo impedía que cada tenant firmara con claves distintas.
- La emisión HTML (`OidcResponseBuilder`, implicit/hybrid) quedó cubierta por [J-06](../epicas/done/ep-J-06-respuestas-authorize.md).
- **Pruebas:** toda la batería OAuth existente (contratos positivos sin cambios salvo los de [1.4](issue-1.4-errores-cache-token.md)) más `TokenEndpointContractTest`, `ConfidentialCodeFlowTest` y `TokenGrantNegativeSecurityTest`.
