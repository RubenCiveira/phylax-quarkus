# Tarea 01-03 — Colapsar capa Gateway → Repository (eliminar indirección redundante)

> **Fase:** 01 — Refactoring y Deuda Técnica
> **Prioridad:** P0
> **Esfuerzo estimado:** Alto
> **Bloquea:** Simplicidad del hexágono para implementar Introspection y Token Revocation

---

## Descripción

En el proyecto coexisten dos capas de persistencia: los `*Gateway` del dominio
(puerto de salida) y los `*Repository` de infraestructura. En algunos sub-features
hay una indirección innecesaria donde el Gateway simplemente delega al Repository
sin añadir lógica. La propuesta es colapsar ambas capas cuando no hay transformación.

---

## Patrón objetivo

**Antes (redundante):**
```
UseCase → GatewayPort (interfaz dominio)
              ↓
         GatewayImpl (infraestructura) → RepositoryImpl → DB
```

**Después (colapsado):**
```
UseCase → GatewayPort (interfaz dominio)
              ↓
         RepositoryImpl (implementa el port directamente) → DB
```

---

## Pasos de implementación

### 1. Auditar los gateways actuales

Para cada sub-feature en `src/main/java/net/civeira/phylax/features/oauth/`:

```
authentication/domain/gateway/
token/domain/gateway/ (si existe)
user/domain/gateway/
mfa/domain/gateway/
key/domain/gateway/
rbac/domain/gateway/
```

Clasificar cada gateway como:
- **A eliminar**: si la impl solo llama al repository sin transformación
- **A mantener**: si añade caché, lógica de mapping compleja, o composición

### 2. Para cada gateway a eliminar

1. Hacer que la clase `*Repository` existente implemente directamente la interfaz `*Gateway`
2. Actualizar los bindings CDI (si hay `@ApplicationScoped` en la impl del gateway)
3. Eliminar la clase `*GatewayImpl` intermedia
4. Verificar que los tests de integración de ese sub-feature siguen verdes

### 3. Actualizar los alt-beans de test

Los `src/test/java/net/civeira/phylax/testing/oauth/alt/` pueden tener
alternativas que implementen el gateway antiguo. Actualizarlos para implementar
directamente la interfaz del port.

### 4. Compilar y testear de forma incremental

Hacer el colapso feature por feature, no todo a la vez.

---

## Notas de arquitectura

- El port de dominio (`*Gateway`) debe **conservarse** — es el puerto hexagonal.
  Solo se elimina la capa intermedia `*GatewayImpl` que no añadía valor.
- Si hay lógica de caché (Redis/Infinispan), mantener la clase intermedia
  con nombre descriptivo: `CachedUserRepository` en lugar de `UserGatewayImpl`.

---

## Criterios de aceptación

- [ ] Cero clases `*GatewayImpl` que sean meros pass-through al repository
- [ ] `mvn compile` sin errores
- [ ] `mvn test -Dgroups="oidc-flow"` verde
- [ ] Revisión de arquitectura: ningún use case importa clases de infraestructura
