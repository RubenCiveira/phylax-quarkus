# 2.5 — Grants inalcanzables y código muerto

- **Fase:** 2 · **Esfuerzo:** S · **Estado:** cerrada (27-10-2026), [J-08](../epicas/done/ep-J-08-login-consentimiento-producto.md).
- **Decisión:** no hay grant `delegated` propietario: la delegación entre servicios es token exchange RFC 8693 ([3.8](issue-3.8-token-exchange.md)), como recomienda PHP 043. El `RefreshGranter` duplicado ya lo eliminó [1.16](issue-1.16-emision-canje-fuera-tokencontroller.md).
- **Cierre:** todos los grants anunciados se resuelven en `IssueTokenUsecase` y tienen prueba; `mfa` sigue operativo como continuación del password grant pero no se anuncia ([1.1](issue-1.1-discovery-honesto.md)).
- **Fuente:** [§5, 2.5](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).
