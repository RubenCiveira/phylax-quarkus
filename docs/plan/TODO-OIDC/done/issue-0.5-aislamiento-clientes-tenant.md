# 0.5 — Aislamiento de clientes por tenant

- **Fase:** 0 · **Esfuerzo:** M–L · **Estado:** hecha (25-10-2026), [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md).
- **Decisión heredada (PHP EP-03):** cliente con `tenant = null` = global, visible desde todos los tenants y gestionable solo por root; si un tenant tiene un cliente con el mismo `code` que uno global, gana el del tenant; un cliente de otro tenant nunca se devuelve. Los clientes de DCR nacen con el tenant de la ruta. Verificar sobre el esquema Java si hace falta migración.
- **Implementación:** decidir entre cliente de tenant o cliente global con asignación explícita; filtrar carga y back-channel conforme a la política.
- **Cierre:** un cliente asignado a A no opera en B; tests de carga, emisión y notificaciones entre dos tenants.
- **Fuente:** [§5, 0.5](../CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Qué hace (funcional)

Un cliente pertenece a un tenant o es global (`tenant = null`, decisión PHP EP-03):

- Desde un tenant se ve su propio cliente o, si no tiene uno con ese código, el global. **Un cliente de otro tenant no existe** para él: ni autentica, ni obtiene codes o tokens, ni recibe back-channel logout.
- El tenant debe existir y estar habilitado (se elimina el atajo que daba por bueno `main` sin consultarlo).
- Los clientes de DCR nacen en el tenant de la ruta ([2.1](issue-2.1-registro-dinamico-clientes.md)); los creados por administración nacen globales salvo que root les asigne tenant.

No hace falta migración: el esquema ya tenía `access_trusted_client.tenant` y los clientes existentes, sin tenant, siguen siendo globales.

## Implementación Java

- `ClientRetrieveAdapter` resuelve el tenant y elige con `TrustedClientPolicy.select` (propio > global > nada).
- La misma regla en `BackChannelLogoutDispatcher.recipients`, en el consentimiento de scopes (`ScopesConsentAdapter`, que además buscaba al **usuario** por nombre en cualquier tenant) y en el cálculo de roles por audiencia (`UserLoginService`).
- **Pruebas:** `TrustedClientPolicyUnitTest` (A/B/global), `BackChannelLogoutDispatcherUnitTest`, `DynamicClientRegistrationMariaDbTest` (DCR solo desde su tenant).
- **Cubierto fuera de la ficha:** limitar el logout a los clientes participantes de la sesión es [0.6](issue-0.6-logout-clientes-participantes.md) (J-07).
