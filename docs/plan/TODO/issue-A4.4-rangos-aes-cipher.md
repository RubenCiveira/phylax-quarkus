# A4.4 — Rangos de `AesCipherService.generateSecureRandomPassword`

- **Fase:** 4 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** `common/crypto`.
- **Problema:** `SecureRandom.ints(count, origin, bound)` tiene `bound` exclusivo ([AesCipherService.java:269-292](../../src/main/java/net/civeira/phylax/common/crypto/AesCipherService.java#L269-L292)): nunca genera `9`, `Z` ni `z`. Las contraseñas generadas tienen 8 caracteres.
- **Implementación:** Usar los límites `58`, `91` y `123` (y revisar el rango de caracteres especiales). Subir la longitud por defecto a 16, ya que son secretos generados por el sistema (seed, API keys).
- **Cierre:** Un test estadístico (p. ej. 10.000 generaciones) produce todos los caracteres de cada clase. La longitud por defecto es ≥ 16.
- **Fuente:** [§2](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#2-estado-de-las-recomendaciones-del-análisis-anterior).
