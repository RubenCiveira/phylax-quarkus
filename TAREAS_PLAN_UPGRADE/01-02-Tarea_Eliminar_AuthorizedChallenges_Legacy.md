# Tarea 01-02 — Eliminar `AuthorizedChallenges` legacy y conversiones `toLegacy()`

> **Fase:** 01 — Refactoring y Deuda Técnica
> **Prioridad:** P0
> **Esfuerzo estimado:** Medio
> **Bloquea:** Claridad del dominio para nuevas features de autenticación

---

## Descripción

Existen conversiones `toLegacy()` y la clase/record `AuthorizedChallenges`
que representan la evolución histórica del modelo de autenticación. Este código
ya no es el camino principal y añade indirección innecesaria en el flujo.

---

## Pasos de implementación

### 1. Localizar todo el código legacy

```bash
grep -rn "toLegacy\|AuthorizedChallenges\|LegacyAuth" \
  src/main/java --include="*.java"
```

### 2. Entender el grafo de dependencias

Trazar quién llama a `toLegacy()` y si el receptor puede consumir
el tipo moderno directamente. Usar el diagrama de calls de IntelliJ.

### 3. Migrar los consumidores

Para cada llamada a `toLegacy()`:
- Reemplazar por el tipo moderno equivalente
- Si el consumidor está en infraestructura (REST controller, gateway), adaptar
  el mapeo directamente sin conversión intermedia

### 4. Eliminar la clase `AuthorizedChallenges`

Una vez que no hay referencias, eliminar:
- La clase/record legacy
- Los métodos `toLegacy()` en entidades de dominio
- Cualquier import residual

### 5. Verificar integridad

```bash
mvn compile -q
mvn test -Dgroups="oidc-flow"
```

---

## Criterios de aceptación

- [ ] `grep -rn "toLegacy\|AuthorizedChallenges" src/main/java` devuelve 0 resultados
- [ ] `mvn compile` sin errores
- [ ] Tests OIDC verdes
