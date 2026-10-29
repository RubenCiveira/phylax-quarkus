# 1.8 — Code flow para clientes confidenciales

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** hecha (24-10-2026).
- **Implementación:** permitir authorize/code a web apps confidenciales y exigir autenticación en canje conforme a su método registrado.
- **Cierre:** code válido canjeado por cliente confidencial; credencial ausente/incorrecta y otro cliente rechazados.
- **Dependencias:** [0.1](issue-0.1-canje-seguro-de-authorization-code.md), [0.9](issue-0.9-autenticacion-cliente-unificada.md) y política [1.11](issue-1.11-politica-pkce.md).
- **Fuente:** [§5, 1.8](../CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).

## Qué hace (funcional)

Una aplicación web con backend (cliente confidencial, sin acceso público) puede usar el flujo *authorization code* estándar:

1. Redirige al usuario a `/authorize` con su `client_id` y una `redirect_uri` registrada. PKCE es opcional para ella salvo que tenga `requirePkce`.
2. Tras el login recibe el `code` en la `redirect_uri`.
3. Lo canjea en `/token` autenticándose **con el método que registró** (`client_secret_basic` o `client_secret_post`), enviando la misma `redirect_uri`.

| Canje | Resultado |
|---|---|
| Método registrado y secreto correcto | 200 con access, refresh e ID token |
| Sin credenciales, con otro método o secreto erróneo | 401 `invalid_client`; el code **no se gasta** |
| Otro cliente (aunque se autentique bien) | 400 `invalid_grant`; el code no se gasta |
| Sin PKCE y como público | 400 `invalid_grant` (un público siempre necesita PKCE) |

## Implementación Java (24-10-2026)

- `ClientStoreGateway::loadPublic` pasa a `loadForAuthorization`: acepta cualquier cliente habilitado con la redirección registrada, no solo los `publicAllow`. Devuelve `protectedWithSecret = !acceso público` y `requirePkce = acceso público || requirePkce`; la autenticación real ocurre en el canje. Lo usan authorize, registro, recuperación y magic link.
- `AuthorizationCodeGrant` emite con el cliente **autenticado en el canje**, no con el `ClientDetails` congelado en el code.
- La política PKCE de confidenciales sigue siendo la de [0.1](issue-0.1-canje-seguro-de-authorization-code.md): sin challenge solo canjea un confidencial autenticado y sin `code_verifier`; [1.11](issue-1.11-politica-pkce.md) decidirá si se exige PKCE también a ellos.
- **Pruebas:** `ConfidentialCodeFlowTest` (canje y refresh con Basic, sin credenciales sin gastar el code, `client_secret_post` no registrado, secreto erróneo, code de otro cliente, SPA pública con PKCE).
