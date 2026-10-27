# J-11 — Tokens ligados a clave o certificado

**Cerrada (27-10-2026).** 3.6 DPoP hecha (emisión, refresh, UserInfo, introspección y rechazo como Bearer); 4.1 mTLS pospuesta hasta tener terminación TLS de confianza; discovery sigue sin anunciar mTLS.

**Prioridad:** opcional (FAPI o RP que lo exija) · **Tareas:** [3.6](../../done/issue-3.6-dpop.md), [4.1](../../pospuestos/issue-4.1-mtls-tokens-certificado.md). **Depende de:** claims J-03, consumidor J-04 y anti-replay J-05.

## Implementación

Decidir soporte DPoP/mTLS antes de anunciarlo. Diseñar vínculo `cnf` desde authorize/code hasta refresh y verificación en cada resource server. DPoP comprueba firma, `htu`/`htm`, `iat` y `jti` de un solo uso; mTLS verifica certificado de origen confiable, no una cabecera arbitraria de proxy, y `cnf.x5t#S256`. Publicar aliases mTLS reales.

**Aceptación:** token ligado falla sin prueba, con otra clave/certificado o replay; refresh mantiene el vínculo. Discovery coincide con métodos probados.

**Referencia PHP:** EP-13 (048, 053). En Java los anuncios actuales mTLS son prematuros y deben retirarse en J-09 hasta implementar.
