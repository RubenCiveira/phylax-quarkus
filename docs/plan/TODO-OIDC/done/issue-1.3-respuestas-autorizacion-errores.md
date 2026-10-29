# 1.3 — Respuestas de autorización y errores

- **Fase:** 1 · **Esfuerzo:** S · **Estado:** implementada y verificada (27-10-2026).
- **Implementación:** usar query para code y fragment para flujos con tokens; incluir `state`, `iss` y `error_description` en errores y `iss` en code.
- **Cierre:** pruebas de éxito/error en cada modo con `state` que contenga caracteres reservados.
- **Pruebas:** `AuthorizeResponseContractTest`, `AuthorizeRequestFlowTest` (state reservado, issuer, query/fragment y errores).
- **Fuente:** [§5, 1.3](../CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
