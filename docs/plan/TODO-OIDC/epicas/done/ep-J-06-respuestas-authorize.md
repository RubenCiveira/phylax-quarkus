# J-06 — Respuestas authorize, errores y nonce

**Prioridad:** alta · **Tareas:** [1.3](../../done/issue-1.3-respuestas-autorizacion-errores.md), [1.7](../../done/issue-1.7-politica-implicit-hybrid.md), [1.12](../../done/issue-1.12-nonce-ligado-autorizacion.md), [3.1](../../done/issue-3.1-response-mode-form-post.md). **Depende de:** claims/hashes J-03 y solicitud validada J-05.

**Estado:** cerrada (27-10-2026), incluido `form_post`. `AuthorizeResponseContractTest` comprueba code/hybrid, hash, nonce, issuer y state, error y éxito en form_post con escaping y CSP.

## Implementación

Centralizar construcción de redirecciones en `OidcResponseBuilder`: query para code, fragment para token/ID y `form_post` únicamente si se aprueba; codificar parámetros preservando query previa. `state` si vino en solicitud e `iss` canónico en éxito/error redirigible; error local para redirect no válida. Decidir retirada de implicit/hybrid o emisión exclusiva de artefactos pedidos; nonce obligatorio en flujos front-channel con ID, correlacionado con code. `form_post` debe escapar campos y usar CSP apropiada.

**Aceptación:** `state` con caracteres especiales no altera parámetros, issuer coincide con discovery, `code id_token` solo emite lo pedido y con hashes correctos si se conserva; errores usan modo correcto y no redirigen a URI no registrada.

**Referencia PHP:** EP-08 (027, 033, 037, 070, 045 opcional). Decidir implicit en Java por sus clientes reales, no por el estado del PHP.
