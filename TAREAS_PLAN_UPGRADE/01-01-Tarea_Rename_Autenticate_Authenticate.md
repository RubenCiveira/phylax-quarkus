# Tarea 01-01 — Renombrar `autenticate` → `authenticate`

> **Fase:** 01 — Refactoring y Deuda Técnica
> **Prioridad:** P0
> **Esfuerzo estimado:** Medio (rename global + verificación de tests)
> **Bloquea:** Ninguna tarea, pero genera ruido en diffs futuros si no se resuelve

---

## Descripción

El typo `autenticate` (sin la primera 'h') está presente en ~40 métodos,
clases, variables y tests de todo el proyecto. Corregirlo elimina confusión
para nuevos desarrolladores y limpia los diffs futuros.

---

## Pasos de implementación

### 1. Identificar todas las ocurrencias

```bash
grep -rn "autenticate\|Autenticate\|AUTENTICATE" \
  src/main/java src/test/java \
  --include="*.java" \
  -l
```

Registrar la lista de ficheros afectados antes de modificar nada.

### 2. Realizar el renombrado global

Usar el refactor de IntelliJ IDEA / rename simbólico o `sed` por fichero.
**Orden recomendado** para evitar romper referencias intermedias:

1. Renombrar interfaces y clases abstractas primero
2. Renombrar implementaciones y adaptadores
3. Renombrar variables y parámetros locales
4. Renombrar métodos de test

**Patrones a corregir:**

| Incorrecto | Correcto |
|-----------|---------|
| `autenticate` | `authenticate` |
| `Autenticate` | `Authenticate` |
| `AUTENTICATE` | `AUTHENTICATE` |
| `autenticateUser` | `authenticateUser` |
| `autenticateWithMfa` | `authenticateWithMfa` |

### 3. Verificar compilación

```bash
mvn compile -q
```

### 4. Verificar tests

```bash
mvn test -Dgroups="oidc-flow"
```

---

## Archivos probablemente afectados

- `src/main/java/net/civeira/phylax/features/oauth/authentication/` — toda la feature
- `src/main/java/net/civeira/phylax/features/oauth/mfa/` — use cases de MFA
- `src/test/java/net/civeira/phylax/testing/oauth/` — tests de integración

---

## Criterios de aceptación

- [ ] `grep -rn "autenticate" src/` devuelve 0 resultados
- [ ] `mvn compile` sin errores
- [ ] `mvn test -Dgroups="oidc-flow"` verde
