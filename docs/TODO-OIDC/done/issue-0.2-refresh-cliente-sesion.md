# 0.2 — Refresh ligado al cliente y a la sesión

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** hecha (23-10-2026); concurrencia y revocación de familia verificadas contra MariaDB el 24-10-2026 (`TokenRevocationSqlAdapterMariaDbTest`, [V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md)).
- **Implementación:** autenticar al cliente, verificar que la sesión siga viva y limitar los scopes solicitados a los originales. Consumir el JTI de forma atómica antes de emitir el siguiente refresh; revocar la familia y sesión afectada ante replay. Registrar `fid` y expiración absoluta `fexp` para que la rotación no prolongue indefinidamente el grant; decidir y documentar qué grants sin sesión pueden recibir refresh y cómo se revocan.
- **Cierre:** refresh robado, sesión cerrada, scope/audiencia ampliados, familia vencida y dos canjes concurrentes rechazados según corresponda; un único vencedor y replay revoca la familia. No revocar tokens de otros usuarios o tenants.
- **Plan PHP:** `done/019-refresh-cliente-y-sesion.md` y `done/020-rotar-refresh-y-detectar-replay.md` sirven de contrato, no de implementación Java.
- **Dependencias:** [0.9](issue-0.9-autenticacion-cliente-unificada.md) y [0.5](issue-0.5-aislamiento-clientes-tenant.md).
- **Fuente:** [§5, 0.2](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Implementación Java (23-10-2026)

- **Familia en el token:** el refresh lleva `fid`, `fexp` (caducidad absoluta: `refresh-token-max-lifetime-seconds` del tenant, 30 días por defecto), `ses` (familia de una sesión interactiva) y `granted_scope`. La rotación hereda `fid`/`fexp` y el `exp` es `min(ahora + ttl, fexp)` ([RefreshFamily](../../../src/main/java/net/civeira/phylax/features/oauth/tokensecurity/application/RefreshFamily.java)).
- **Orden en `TokenController::refresh`:**
  1. firma;
  2. cliente autenticado e igual al `azp`;
  3. JTI ya consumido o revocado: reutilización;
  4. familia revocada o vencida;
  5. sesión viva si `ses` (`SessionStoreGateway::isRefreshSessionActive`, unión de `_oauth_session_token` y `_oauth_session`);
  6. el scope pedido debe estar dentro del concedido (`invalid_scope`), y sin scope se conserva el concedido;
  7. el usuario sigue habilitado;
  8. **consumo atómico** (`TokenRevocationGateway::consumeJti`, `INSERT IGNORE` sobre la PK de `_oauth_revoked_jti`);
  9. emisión.

  Una petición rechazada antes del paso 8 no gasta el refresh.
- **Reutilización:** revoca la familia (el propio `fid` se inserta en `_oauth_revoked_jti` hasta `fexp`) y, con `revokeAllForUser`, los access y refresh de las sesiones interactivas del usuario con ese cliente en ese tenant (corregido el 24-10-2026: antes solo revocaba los refresh y sin filtrar por tenant), y responde `invalid_grant`. Sin periodo de gracia: dos refresh simultáneos cuentan como reutilización, igual que en PHP.
- **Corregido:** `TokenVerifier::verifyRefreshInfo` descartaba los JTI revocados, así que la rama de reutilización del controlador nunca se ejecutaba. Además, el refresh no limitaba el scope (se podía ampliar a cualquiera permitido al cliente) ni autenticaba al cliente.
- **Sin migración:** se reutilizan `_oauth_revoked_jti` y `_oauth_session_token`. Los refresh emitidos antes del despliegue no tienen `fid` y se rechazan: hay que volver a iniciar sesión.
- **Pruebas:** `TokenGrantSecurityTest` (rotación con la misma familia y `fexp`, reutilización que revoca la familia, ampliar y estrechar scope, sin cliente, secreto erróneo sin gastar el refresh, sesión interactiva tras logout) y `RefreshTokenTest` adaptado.
- **Actualización (24-10-2026, [1.16](issue-1.16-emision-canje-fuera-tokencontroller.md)):** la rotación vive en `RefreshTokenGrant`. El cliente se autentica ahora **antes** de verificar la firma (pasos 1 y 2 invertidos), con su método registrado ([0.9](issue-0.9-autenticacion-cliente-unificada.md)); y `verifyRefreshInfo` exige el `iss` del tenant de la ruta.
