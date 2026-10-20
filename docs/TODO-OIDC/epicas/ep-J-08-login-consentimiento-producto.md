# J-08 — Login, consentimiento y flujos de producto

**Prioridad:** media para consentimiento/invitaciones · **Tareas:** [1.6](../issue-1.6-prompt-consent.md), [2.2](../issue-2.2-endpoints-invitaciones.md), [2.5](../issue-2.5-grants-inalcanzables-codigo-muerto.md), [3.4](../issue-3.4-federacion-oidc-generica.md), [3.8](../issue-3.8-token-exchange.md), [4.3](../issue-4.3-ciba.md) opcionales según uso. **Depende de:** política J-02 y validación J-04 para extensiones.

## Implementación

`prompt=consent` fuerza decisión aunque haya consentimientos previos; exponer los casos de uso de invitaciones. Decidir `delegated`: habilitarlo con política o retirarlo; si se necesita delegación entre servicios, priorizar token exchange RFC 8693 con actor y límites de scope/audiencia. Federación OIDC genérica con JWKS seguro, `state`/`nonce` y vinculación explícita de cuentas; CIBA solo si hay canal fuera de banda y política aprobada. No duplicar el uso de recovery MFA: ya existe en Java; comprobar carrera de consumo como prueba de regresión.

**Aceptación:** consentimiento se renueva a petición; invitaciones autenticadas/de un solo uso; cada extensión aprobada tiene política, casos negativos, grant habilitado por cliente y discovery coherente. Sin aprobación, no se anuncia.

**Referencia PHP:** EP-10 (039, 040, 051) y EP-12 (043, 052, 055). En PHP 039 está pendiente, en Java `RecoverMfaStep` consume códigos ya.
