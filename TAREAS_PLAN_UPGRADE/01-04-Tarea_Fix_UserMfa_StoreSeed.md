# Tarea 01-04 — Corregir orden de parámetros en `UserMfa.storeSeed()`

> **Fase:** 01 — Refactoring y Deuda Técnica
> **Prioridad:** P0
> **Esfuerzo estimado:** Bajo
> **Bloquea:** Correctitud del flujo de activación de MFA TOTP

---

## Descripción

El método `UserMfa.storeSeed()` tiene los parámetros en orden incorrecto,
lo que puede provocar que el `uid` del usuario y la `seed` TOTP se guarden
invertidos, rompiendo silenciosamente la autenticación MFA.

---

## Pasos de implementación

### 1. Localizar el método

```bash
grep -rn "storeSeed" src/main/java --include="*.java"
```

Clase esperada: `src/main/java/net/civeira/phylax/features/oauth/mfa/domain/`

### 2. Verificar la firma actual vs. uso

Comparar la firma del método con todos sus call sites. Si la firma es:
```java
public void storeSeed(String seed, UUID userUid)
```
...pero los callers pasan `(userUid, seed)`, el bug está en la firma.

### 3. Corregir la firma y actualizar todos los callers

Opción A — corregir la firma del método (renombrar parámetros para que coincidan):
```java
public void storeSeed(UUID userUid, String seed)
```

Opción B — corregir los callers si la firma es la correcta.

Elegir la opción que minimice el cambio, priorizando que la semántica
sea obvia: `userUid` antes que `seed`.

### 4. Verificar cobertura de test

Asegurarse de que el test de activación MFA (`MfaActivationFlowTest` o similar)
comprueba que el seed almacenado coincide con el esperado, no solo que no lanza excepción.

### 5. Compilar y testear

```bash
mvn test -Dgroups="oidc-flow"
```

---

## Criterios de aceptación

- [ ] La firma de `storeSeed()` tiene `userUid` como primer parámetro y `seed` como segundo
- [ ] Todos los callers coinciden con la firma
- [ ] Test que valida que el seed guardado es el seed correcto (no invertido)
- [ ] `mvn test` verde
