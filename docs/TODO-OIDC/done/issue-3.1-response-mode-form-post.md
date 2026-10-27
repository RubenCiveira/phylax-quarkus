# 3.1 — Response mode form_post

- **Fase:** 3 · **Estado:** implementada y verificada (27-10-2026) · **Referencia:** OAuth 2.0 Form Post.
- **Implementación:** añadir formulario autoenviado con parámetros escapados en éxito y error.
- **Cierre:** pruebas de contenido, codificación y redirección de code y flujos habilitados; anunciar modo solo tras validarlo.
- **Pruebas:** `AuthorizeResponseContractTest` (code/hybrid, error, escaping, CSP y discovery).
- **Fuente:** [§5, 3.1](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
