# J-01 — Token y grants seguros

**Prioridad:** bloqueante · **Tareas:** [0.1](../issue-0.1-canje-seguro-de-authorization-code.md), [0.2](../issue-0.2-refresh-cliente-sesion.md), [0.9](../issue-0.9-autenticacion-cliente-unificada.md), [0.13](../issue-0.13-ruta-revocacion-no-duplicada.md), [1.4](../issue-1.4-errores-cache-token.md), [1.8](../issue-1.8-code-clientes-confidenciales.md), [1.10](../issue-1.10-tests-negativos-seguridad.md), [1.16](../issue-1.16-emision-canje-fuera-tokencontroller.md). **Depende de:** política de [J-02](ep-J-02-clientes-tenant-secretos.md) para cliente/grant/tenant; la seguridad del code y refresh empieza antes de terminar toda la épica.

## Implementación

1. Caracterizar flujos existentes; centralizar Basic/post y métodos registrados, sin confundir cliente público con confidencial. Habilitar code confidencial solo con canje autenticado.
2. Code: validar cliente, tenant, redirect y PKCE, consumir con operación atómica. Refresh: validar cliente, sesión, scope y audiencia originales; rotar JTI atómicamente con familia/expiración absoluta y revocar familia ante replay. Definir el tratamiento de grants sin sesión.
3. Consolidar canje y refresh en casos de uso/granters, usando la emisión Java existente; resolver ruta duplicada de revocación. Errores RFC 6749 y cabeceras anticaché en éxito/error.

**Aceptación:** dos canjes concurrentes de code o refresh producen un vencedor; code/refresh de otro cliente/tenant o sesión terminada no emiten tokens; errors y headers verificables por HTTP+BD. [V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md) conserva el arnés.

**Referencia PHP:** `done/017-canje-code-atomico.md`, `done/019-refresh-cliente-y-sesion.md`, `done/020-rotar-refresh-y-detectar-replay.md`, EP-05 (038, 064). Java difiere: `TokenController` resuelve code/refresh en línea y hay `RefreshGranter` inalcanzable.
