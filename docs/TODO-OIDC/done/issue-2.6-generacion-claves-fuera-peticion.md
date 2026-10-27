# 2.6 — Generación de claves fuera de la petición

- **Fase:** 2 · **Esfuerzo:** S · **Estado:** hecha (25-10-2026), [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md).
- **Implementación:** generar/rotar claves mediante job en lugar de hacerlo en firma o JWKS.
- **Cierre:** pruebas de rotación, disponibilidad y publicación de `kid` sin generación síncrona durante la petición.
- **Fuente:** [§5, 2.6](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).

## Qué hace (funcional)

- Firmar y publicar `/oauth/jwks` solo leen las claves guardadas; ninguna petición genera una clave RSA de 4096 bits.
- `SigningKeyRotationJob` genera las claves al arrancar (tras las migraciones) y cada 15 minutos (`oidc.jwt.authorization.rotation-cron`) para mantener `key-futures` claves por delante. Si falla, lo registra y reintenta: las claves vigentes siguen sirviendo.
- Dos instancias que roten a la vez pueden añadir claves de más: se publican y caducan como las demás.
- **Pruebas:** `JoseTokenSignerUnitTest` (sin claves la firma falla en vez de generar; tras rotar, el `kid` se publica; un token sigue verificando tras otra rotación).
