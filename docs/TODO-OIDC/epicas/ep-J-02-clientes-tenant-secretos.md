# J-02 — Clientes, tenant y secretos

**Prioridad:** bloqueante · **Tareas:** [0.3](../issue-0.3-audiencias-autorizadas.md), [0.4](../issue-0.4-configuracion-por-cliente.md), [0.5](../issue-0.5-aislamiento-clientes-tenant.md), [0.10](../issue-0.10-redirect-uri-estricta.md), [2.1](../issue-2.1-registro-dinamico-clientes.md), [2.4](../issue-2.4-redirects-logout.md), [2.8](../issue-2.8-proteccion-secretos.md), [2.9](../issue-2.9-migracion-secretos-cifrado.md). **Desbloquea:** política común de J-01, J-03, J-05 y J-07.

## Implementación

1. Decidir cliente tenant o global explícito; diseñar transición de datos de `TrustedClient`, sin consulta solo por `code`. Aplicar grants, response types, auth method, scopes M2M y audiencias de cliente; password nunca por defecto.
2. Restringir redirects a coincidencia exacta (excepción loopback controlada); separar redirects post-logout. Probar tenant A/B y clientes confidenciales/públicos.
3. Revisar verificador no reversible de secretos y clave AES/contraseñas; migrar sin exponer secreto ni romper credenciales existentes. Exponer DCR RFC 7591/7592 solo con initial access token real, tenant y metadata restringidos; actualizar discovery en J-09 tras pruebas.

**Aceptación:** cliente de A no se usa en B; grant/scope/audience/redirect ajenos rechazados; secreto no recuperable en almacenamiento ni lectura DCR; credencial caducada/agotada no registra clientes.

**Referencia PHP:** [EP-02 secretos](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/EP-02-secretos-y-cifrado.md) pendiente y `done/EP-03-clientes-tenant-dcr.md` ya ejecutada en PHP. En Java `ClientRetrieveAdapter` aún carga defaults globales y DCR es 403.
