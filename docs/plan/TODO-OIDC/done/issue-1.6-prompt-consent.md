# 1.6 — Prompt consent

- **Fase:** 1 · **Esfuerzo:** S · **Estado:** implementada y verificada (27-10-2026), [J-08](../epicas/done/ep-J-08-login-consentimiento-producto.md).
- **Implementación:** `prompt` se trata como conjunto (`AuthRequest.prompts()`); `none` combinado con otro valor es `invalid_request`. Con `consent`, `StepOutcomeHandler` convierte un éxito sin el reto `CLIENT_CONSENT` en la pantalla de consentimiento con **todos** los scopes pedidos (`ScopeApprovalUsecase.renewedApprovals`), también sobre una sesión viva (`AuthorizeHtml.doRenewConsent`, como el step-up). Los scopes desmarcados se retiran del consentimiento guardado (`withdrawScopes`, comportamiento PHP 031). El botón «Deny» devuelve `access_denied` con `state` (antes volvía al login). La solicitud reducida conserva todos sus parámetros (`toBuilder`) y los scopes se escapan en el HTML.
- **Cierre:** prueba con consentimiento previo, aceptación parcial, denegación y sesión existente.
- **Pruebas:** `PromptConsentFlowTest`, `EndToEndFlowTest`, `ScopeConsentFlowTest`.
- **Fuente:** [§5, 1.6](../CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
