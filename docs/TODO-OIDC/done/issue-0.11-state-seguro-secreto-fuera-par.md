# 0.11 — State seguro y secreto fuera de PAR

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** hecha (25-10-2026), [J-05](../epicas/done/ep-J-05-solicitudes-authorize-par-jar.md).
- **Implementación:** codificar `state` al redirigir en authorize y logout; excluir `client_secret` de los parámetros persistidos en PAR.
- **Cierre:** `state` con caracteres reservados vuelve intacto; el secreto no aparece en el registro de PAR.
- **Fuente:** [§5, 0.11](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Resultado

- `state` se codifica (`URLEncoder`) en la respuesta con code, en la híbrida, en las redirecciones de error y en el logout; es opcional: sin `state` no se añade nada (antes la ausencia lanzaba una excepción).
- El push PAR nunca guarda `client_secret`, `client_assertion` ni `client_assertion_type`: los filtra el controlador y otra vez el caso de uso.
- **Pruebas:** `AuthorizeRequestFlowTest` (`a b&c=d/é` vuelve intacto; push de un confidencial sin secreto almacenado), `ClientPolicyFlowTest` (logout).
