# J-10 — Aserciones JWT de cliente y grant

**Cerrada (27-10-2026).** 3.2 hecha: `private_key_jwt` con `jti` compartido con JAR (propósito `client_jwt`); 3.9 sigue pospuesta. Pendiente real: `jwks_uri` real y alta por DCR.

**Prioridad:** opcional · **Tareas:** [3.2](../../done/issue-3.2-autenticacion-jwt-cliente.md), [3.9](../../pospuestos/issue-3.9-grant-jwt-bearer.md) (pospuesta, como PHP 069). **Depende de:** método cliente J-02, resolver/JWKS y anti-replay J-05.

## Implementación

Extender autenticación común de J-01 con `private_key_jwt` registrado por cliente; `iss=sub=client_id`, `aud` endpoint del tenant, firma JWKS, `exp` y `jti` de un solo uso. `client_secret_jwt` no se implementa (decisión heredada de PHP EP-02/EP-11: con la opción A el servidor no conoce el secreto). Un JWT firmado por el cliente sirve para un solo uso: el `jti` se consume también bajo el propósito de JAR, y una aserción con `response_type` se rechaza. JWT bearer RFC 7523 solo con emisor y caso de uso de confianza aprobados; separar propósito `client_assertion` de `jwt_bearer` en almacén anti-replay.

**Aceptación:** firma ajena, método no registrado, expiración, otra audiencia y replay fallan; una aserción de cliente no funciona como grant; discovery anuncia solo métodos concedidos.

**Referencia PHP:** EP-11 (044, 069). Ni PHP ni Java anuncian `client_secret_jwt`.
