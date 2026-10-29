# A4.2 — Autenticación del colector OTLP

- **Fase:** 4 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** `bootstrap/telemetry/collector`.
- **Problema:** `/v1/logs` y `/v1/traces` no exigen credencial, aunque el seed crea una API key con scope `collect`. Cualquiera puede inyectar telemetría o llenar el almacenamiento.
- **Implementación:** Exigir la API key con scope `collect` (o un bearer con ese scope) en los recursos OTLP, y aplicar un límite de tamaño y de frecuencia.
- **Cierre:** Sin credencial: 401. Con una key sin scope `collect`: 403. Con la key correcta: se acepta. Hay tests de los tres casos.
- **Dependencias:** [A0.2](issue-A0.2-autenticacion-obligatoria-api.md).
- **Fuente:** [§2](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#2-estado-de-las-recomendaciones-del-análisis-anterior).
