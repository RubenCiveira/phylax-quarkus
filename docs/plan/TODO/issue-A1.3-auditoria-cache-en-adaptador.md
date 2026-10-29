# A1.3 — Auditoría y caché en las escrituras que no pasan por un caso de uso

- **Fase:** 1 · **Esfuerzo:** M · **Estado:** pendiente · **Dónde:** Plantilla `*CommandRepositoryAdapter` (o casos de uso de sistema).
- **Problema:** `oauth` escribe directamente en los `*CommandRepositoryGateway` de `user` y de los 8 agregados internos (contraseña, bloqueo, MFA, passkeys, códigos de recuperación, consentimientos). Esas escrituras no se auditan ni actualizan la caché, porque las dos cosas solo se hacen en los casos de uso.
- **Implementación:** Elegir el contrato: **(b, recomendada)** mover la auditoría y la invalidación de caché al `CommandRepositoryAdapter`, para que se apliquen desde cualquier llamador (casos de uso, batch, `oauth`), con un `OperationContext` de sistema cuando no haya actor; o **(a)** generar para los agregados internos casos de uso «de sistema» sin API REST y hacer que `oauth` los use. Documentar la decisión en un ADR.
- **Cierre:** Cambiar la contraseña o activar MFA desde `oauth` deja un registro en `_audit_access_user`, y el listado de usuarios refleja el cambio inmediatamente. No hay doble auditoría en el camino REST.
- **Fuente:** [§4.5](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#45-alto--oauth-se-salta-la-capa-de-aplicación-de-access).
