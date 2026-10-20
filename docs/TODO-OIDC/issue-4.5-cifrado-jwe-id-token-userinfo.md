# 4.5 — Cifrado JWE para ID token y UserInfo

- **Fase:** 4 (solo con requisito) · **Estado:** pendiente.
- **Implementación:** cifrar por RP configurado con claves y algoritmos permitidos; separar entrega JSON de respuesta cifrada.
- **Cierre:** RP destinatario descifra; cliente ajeno no puede hacerlo; discovery anuncia solo algoritmos reales.
- **Fuente:** [§5, fase 4](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-4--perfil-alto-fapi-20-solo-si-hay-requisito).
