# 1.1 — Discovery honesto

- **Fase:** 1 · **Esfuerzo:** S · **Estado:** implementada y verificada (27-10-2026), [J-09](../epicas/ep-J-09-discovery-e2e-documentacion.md).
- **Implementación:** cada contributor anuncia solo lo operativo y toma las listas de la misma constante que usa la lógica: métodos de autenticación (`ClientAuthMethod.TOKEN_ENDPOINT`/`CONFIDENTIAL`, con `private_key_jwt` y sin `client_secret_jwt`), claims emitidas (`AuthorizationRequestPolicy.SUPPORTED_CLAIMS`), modos de respuesta (`MODES`, con JARM), algoritmos JARM/UserInfo/JWE/DPoP, `subject_types_supported` con `pairwise` solo si hay sal, scopes estándar (sin el `roles` inexistente) y grants con `token-exchange`. `implicit` se mantiene por los hybrid (decisión de [1.7](issue-1.7-politica-implicit-hybrid.md)); `mfa` no se anuncia (continuación del password grant). mTLS y CIBA siguen sin anunciarse.
- **Cierre:** cada campo nuevo tiene una prueba que ejercita el flujo y comprueba el anuncio.
- **Pruebas:** `ClientAssertionFlowTest`, `JoseResponsesFlowTest`, `PairwiseSubjectFlowTest`, `ClaimsParameterFlowTest`, `AuthorizeResponseContractTest`, `AuthorizeRequestFlowTest`.
- **Fuente:** [§4](../CAPACIDADES_OIDC.md#4-discovery-anunciado-frente-a-implementado).
