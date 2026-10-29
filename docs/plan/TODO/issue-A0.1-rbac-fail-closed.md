# A0.1 — RBAC cerrado por defecto

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** Configuración + `common/security`.
- **Problema:** `app.security.rbac.default-allow: true` y `mp.rbac.model` comentado en [security.yaml](../../src/main/resources/config/security.yaml): sin `RbacStore` activo, [Rbac.checkAllow](../../src/main/java/net/civeira/phylax/common/security/Rbac.java#L106) devuelve `allow` para cualquier recurso y acción.
- **Implementación:** Cambiar el valor por defecto a `default-allow: false`. Fuera del perfil `dev`, abortar el arranque si no hay ningún `RbacStore` activo, o registrar un error explícito y denegar. Documentar cómo activar el RBAC (`phylax` / `keycloak`) en el README de despliegue.
- **Cierre:** Con la configuración por defecto, una operación sin permiso RBAC devuelve 403. En `prod`, arrancar sin store activo falla con un mensaje claro. En `dev` se puede seguir permitiendo, de forma explícita.
- **Fuente:** [§4.1](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#41-crítico--autorización-fail-open-por-diseño).
