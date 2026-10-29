# A2.1 — Eventos de dominio después del commit y límites transaccionales

- **Fase:** 2 · **Esfuerzo:** M · **Estado:** pendiente · **Dónde:** Plantilla `*EventDispatcher` + casos de uso + `oauth`.
- **Problema:** Los eventos se lanzan con `Event.fire` dentro de la transacción y antes del commit. `@Transactional` está en el controller REST ([RoleCreateController.java:35](../../src/main/java/net/civeira/phylax/features/access/role/infrastructure/driver/rest/RoleCreateController.java#L35)), no en el caso de uso. `NotifyCreate` y `NotifyRecover` están vacíos.
- **Implementación:** Decidir y documentar si los eventos son internos y síncronos o si se publican con `TransactionPhase.AFTER_SUCCESS` / outbox (el patrón del módulo `notification`). Mover `@Transactional` al caso de uso, para que batch, importación y `oauth` tengan los mismos límites. Borrar o implementar los listeners vacíos.
- **Cierre:** Un rollback no produce efectos externos (correo, notificación). Los casos de uso invocados fuera de REST son transaccionales. No quedan listeners vacíos.
- **Fuente:** [§4.7](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#47-medio--eventos-de-dominio-síncronos-y-sin-consumidores).
