# J-10 — Aserciones JWT de cliente y grant

**Prioridad:** opcional · **Tareas:** [3.2](../issue-3.2-autenticacion-jwt-cliente.md), [3.9](../issue-3.9-grant-jwt-bearer.md). **Depende de:** método cliente J-02, resolver/JWKS y anti-replay J-05.

## Implementación

Extender autenticación común de J-01 con `private_key_jwt` registrado por cliente; `iss=sub=client_id`, `aud` endpoint del tenant, firma JWKS, `exp` y `jti` de un solo uso. Decidir por separado `client_secret_jwt`: no compatible sin más con secretos almacenados solo como hash; requiere diseño explícito. JWT bearer RFC 7523 solo con emisor y caso de uso de confianza aprobados; separar propósito `client_assertion` de `jwt_bearer` en almacén anti-replay.

**Aceptación:** firma ajena, método no registrado, expiración, otra audiencia y replay fallan; una aserción de cliente no funciona como grant; discovery anuncia solo métodos concedidos.

**Referencia PHP:** EP-11 (044, 069). No habilitar `client_secret_jwt` por copiar un valor de metadata PHP.
