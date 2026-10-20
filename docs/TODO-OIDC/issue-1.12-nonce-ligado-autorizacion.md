# 1.12 — Nonce ligado a la autorización

- **Fase:** 1 · **Esfuerzo:** S · **Estado:** pendiente.
- **Implementación:** conservar el nonce original del code; exigirlo cuando el flujo lo requiera y no emitir claim vacío.
- **Cierre:** nonce correcto propagado, ausente omitido en code y omisión/discordancia rechazadas en implicit/hybrid si siguen disponibles.
- **Dependencias:** decisión [1.7](issue-1.7-politica-implicit-hybrid.md).
- **Fuente:** [§5, 1.12](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-1--conformidad-oidc-core-y-discovery-honesto).
