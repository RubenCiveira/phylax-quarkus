# J-12 — Algoritmos, firma y cifrado avanzados

**Cerrada (27-10-2026).** 3.11, 4.2 y 4.5 hechas sobre las claves RS256 y el JWKS del cliente; 3.7 pospuesta hasta añadir `alg` a `_oauth_keys_storer` ([especificación](../../cambios-modelo-3.7.md)).

**Prioridad:** opcional · **Tareas:** [3.7](../../pospuestos/issue-3.7-algoritmos-es256-eddsa.md), [3.11](../../done/issue-3.11-userinfo-firmado.md), [4.2](../../done/issue-4.2-jarm.md), [4.5](../../done/issue-4.5-cifrado-jwe-id-token-userinfo.md). **Depende de:** firma/rotación J-03, UserInfo J-04 y respuesta J-06.

## Implementación

Solo bajo demanda: negociar por cliente y tipo de artefacto firma ES256/EdDSA, UserInfo firmado, JARM y JWE (ID token/UserInfo). Mantener rotación y ventana de claves por algoritmo; cifrar después de firmar, usando JWKS del RP obtenido por fetcher seguro. Validar metadata de cliente antes de activar la capacidad y habilitar listas de algoritmos en discovery únicamente cuando se prueben.

**Aceptación:** RP de prueba verifica JWT y descifra JWE; token viejo/nuevo verificables en ventana de rotación, algoritmo no registrado rechazado; ninguna variante no operativa anunciada.

**Referencia PHP:** EP-14 (050, 054, 057, 060). Java dispone de RS256 y JWKS por tenant; no replicar migraciones de tabla PHP sin inspeccionar esquema Java.
