# J-08 — Login, federación, consentimiento y flujos de producto

**Cerrada (27-10-2026).** 0.16, 1.6, 2.2, 2.5 y 3.8 hechas y probadas; `delegated` queda sustituido por token exchange; CIBA (4.3) pospuesta sin canal fuera de banda; la regresión de recovery MFA (PHP 039) se cerró tras la revisión del 27-10-2026: el consumo pasa a un `UPDATE … WHERE used_at IS NULL` (`RecoveryCodeConsumptionSqlAdapter`), probado con 16 intentos simultáneos en `RecoveryCodeMariaDbTest` (se ejecuta con `make test-mariadb`; sin `V1_MARIADB_URL` se omite). **Reservas que no bloquean el cierre pero deben resolverse antes de desplegar:** 2.2 sin plantilla `user.invitation`, sin aplicar los roles de la invitación ni auto-login; 0.16 valida SAML con XML DSig del JDK en vez de una librería mantenida (decisión de la ficha 0.16).

**Prioridad:** crítica para [0.16](../../done/issue-0.16-seguridad-login-federado.md) (adelantar a la ola 1); media para consentimiento e invitaciones · **Tareas:** [0.16](../../done/issue-0.16-seguridad-login-federado.md), [1.6](../../done/issue-1.6-prompt-consent.md), [2.2](../../done/issue-2.2-endpoints-invitaciones.md), [2.5](../../done/issue-2.5-grants-inalcanzables-codigo-muerto.md); opcionales según uso [3.8](../../done/issue-3.8-token-exchange.md) y [4.3](../../pospuestos/issue-4.3-ciba.md). [3.4](../../pospuestos/issue-3.4-federacion-oidc-generica.md) está descartada. **Depende de:** política J-02 y validación J-04 para extensiones; 0.16 solo necesita el tenant del proveedor ([0.5](../../done/issue-0.5-aislamiento-clientes-tenant.md)).

## Implementación

1. **Login federado (0.16):** identidades enlazadas `(proveedor, sub)`, sin resolver nunca la cuenta por `name`; SAML estricto con una librería mantenida; `state` ligado a la pre-sesión y de un solo uso; callback por GET y POST con HTML escapado. Diseña el almacén anti-replay si J-05 no lo ha creado aún.
2. `prompt=consent` fuerza una nueva decisión aunque haya consentimientos previos, con todos los scopes pedidos; un scope opcional sin marcar se retira de la aprobación anterior (comportamiento PHP 031).
3. Exponer los casos de uso de invitaciones.
4. Decidir `delegated`: habilitarlo con política o retirarlo; si se necesita delegación entre servicios, priorizar token exchange RFC 8693 con actor y límites de scope/audiencia.
5. CIBA solo si hay canal fuera de banda y política aprobada.

No duplicar el consumo de recovery MFA: ya existe en Java (`RecoverMfaStep`); añadir como regresión la prueba de PHP 039 (un código vale una vez y, de dos intentos concurrentes, solo uno gana).

**Aceptación:** 0.16 cerrada con sus pruebas negativas; consentimiento renovado a petición; invitaciones autenticadas y de un solo uso; cada extensión aprobada tiene política, casos negativos, grant habilitado por cliente y discovery coherente. Sin aprobación, no se anuncia.

**Referencia PHP:** [EP-10](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/EP-10-login-mfa-y-federacion.md) (039 hecha; 040 y 051 descartadas), [072](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/072-seguridad-login-federado.md) y [EP-12](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/epicas/EP-12-grants-decision-producto.md) (043, 052, 055, pendientes de decisión).
