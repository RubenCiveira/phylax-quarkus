# 0.11 — State seguro y secreto fuera de PAR

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** pendiente.
- **Implementación:** codificar `state` al redirigir en authorize y logout; excluir `client_secret` de los parámetros persistidos en PAR.
- **Cierre:** `state` con caracteres reservados vuelve intacto; el secreto no aparece en el registro de PAR.
- **Fuente:** [§5, 0.11](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).
