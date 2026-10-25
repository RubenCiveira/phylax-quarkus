# 0.10 — Redirect URI estricta

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** hecha (24-10-2026), adelantada desde [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md) para cerrar [1.10](issue-1.10-tests-negativos-seguridad.md).
- **Implementación:** eliminar `*` y prefijos `/*`; limitar cualquier excepción explícita al puerto loopback permitido.
- **Cierre:** coincidencia exacta registrada aceptada; prefijos y destinos no registrados rechazados.
- **Fuente:** [§5, 0.10](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Qué hace (funcional)

La `redirect_uri` de una petición de autorización debe ser **exactamente** una de las registradas para el cliente: mismo esquema, host, puerto, ruta y query. No hay prefijos ni comodines.

Única excepción, para desarrollo local: una URI **loopback** `http` registrada con `/*` al final (`http://localhost:4200/*`, `http://127.0.0.1/app/*`, `http://[::1]/*`). Entonces se acepta una ruta dentro de la registrada siempre que:

- el esquema sea `http` y el host el mismo loopback;
- el puerto sea el registrado, o cualquiera si se registró sin puerto (RFC 8252 §7.3, apps nativas);
- no lleve userinfo (`http://localhost:4200@evil.example/`), fragmento ni segmentos `..`.

| Registrada | Pedida | Resultado |
|---|---|---|
| `https://app.example.com/callback` | la misma | aceptada |
| `https://app.example.com/callback` | `…/callback/otra`, `…/callback?next=…`, `http://…` | rechazada |
| `https://app.example.com/*` o `*` | cualquiera | rechazada (comodín fuera de loopback) |
| `http://localhost:4200/*` | `http://localhost:4200/callback` | aceptada |
| `http://localhost:4200/*` | `http://localhost:4201/`, `https://localhost:4200/`, `http://localhost:4200@evil.example/` | rechazada |

Rechazada significa que no se emite code ni se redirige a esa URI: se muestra la página «Client not allowed».

## Implementación Java (24-10-2026)

- `RedirectUriPolicy` (dominio de `client`) concentra la regla; `ClientRetrieveAdapter` la usa al cargar el cliente de authorize, registro, recuperación y magic link, y registra un aviso cuando una redirección no coincide.
- **Datos:** el cliente que siembra la instalación (`oidc.trusted-client.redirect: http://localhost:4200/*`) sigue funcionando por la excepción loopback. Cualquier cliente con `*` o `https://…/*` deja de autorizar y debe registrar sus URIs exactas: revisar `access_trusted_client_allowed_redirects` antes de desplegar.
- **Fuera de alcance:** `post_logout_redirect_uri` sigue la comparación propia de `AuthorizeHtml` (con comodines) y se resuelve en [2.4](issue-2.4-redirects-logout.md).
- **Pruebas:** `RedirectUriPolicyUnitTest` (exacta, prefijos, query, host parecido, comodines, loopback con userinfo, puerto, esquema, fragmento y `..`); el doble `FixedClientStoreGateway` aplica la misma política y `TokenGrantNegativeSecurityTest` comprueba por HTTP que redirects no registrados o casi iguales no obtienen code.
