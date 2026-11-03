# A0.3 — Políticas de autorización para los maestros desprotegidos

- **Fase:** 0 · **Esfuerzo:** M · **Estado:** pendiente · **Dónde:** Modelo + plantilla de políticas.
- **Problema:** `role`, `userroleassignment`, `usergroupmembership`, `user`, `consentpurpose`, `tenantconfig`, `tenantloginprovider` y `tenanttermsofuse` no tienen ningún observer de `*AuthorizationDecision`. Solo filtran visibilidad y fijan el tenant, así que cualquier usuario autenticado del tenant puede escribir. En `userroleassignment` y `usergroupmembership` eso permite **asignarse roles a uno mismo**. `role` no tiene ni siquiera filtro de visibilidad.
- **Implementación:** Definir en el modelo una política por operación: `OnlyForRoot` para `role`, y un rol `tenant-admin` (o equivalente) para el resto de escrituras, más lectura propia cuando proceda (`user` → su propio perfil). Prohibir explícitamente asignar roles o grupos al propio actor, salvo a root. Regenerar.
- **Cierre:** Para cada maestro hay tests de un usuario normal (403 en las escrituras), de un `tenant-admin` de otro tenant (403 o 404) y de root (200). Un usuario no puede crear un `userroleassignment` para sí mismo.
- **Dependencias:** [A0.4](issue-A0.4-generador-permiso-deny.md), [A0.7](issue-A0.7-tests-denegacion-maestros.md).
- **Fuente:** [§4.1](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#41-crítico--autorización-fail-open-por-diseño).
