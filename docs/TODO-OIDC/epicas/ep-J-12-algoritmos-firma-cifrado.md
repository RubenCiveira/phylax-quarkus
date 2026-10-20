# J-12 — Algoritmos, firma y cifrado avanzados

**Prioridad:** opcional · **Tareas:** [3.7](../issue-3.7-algoritmos-es256-eddsa.md), [3.11](../issue-3.11-userinfo-firmado.md), [4.2](../issue-4.2-jarm.md), [4.5](../issue-4.5-cifrado-jwe-id-token-userinfo.md). **Depende de:** firma/rotación J-03, UserInfo J-04 y respuesta J-06.

## Implementación

Solo bajo demanda: negociar por cliente y tipo de artefacto firma ES256/EdDSA, UserInfo firmado, JARM y JWE (ID token/UserInfo). Mantener rotación y ventana de claves por algoritmo; cifrar después de firmar, usando JWKS del RP obtenido por fetcher seguro. Validar metadata de cliente antes de activar la capacidad y habilitar listas de algoritmos en discovery únicamente cuando se prueben.

**Aceptación:** RP de prueba verifica JWT y descifra JWE; token viejo/nuevo verificables en ventana de rotación, algoritmo no registrado rechazado; ninguna variante no operativa anunciada.

**Referencia PHP:** EP-14 (050, 054, 057, 060). Java dispone de RS256 y JWKS por tenant; no replicar migraciones de tabla PHP sin inspeccionar esquema Java.
