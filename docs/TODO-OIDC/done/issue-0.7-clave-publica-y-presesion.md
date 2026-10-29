# 0.7 — Clave pública y pre-sesión

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** hecha (25-10-2026), [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md).
- **Implementación:** quitar `withKeyId(privateKey)` de `signKeypass` y verificar pre-sesiones mediante `scope=challenge`.
- **Cierre:** `kid` solo identifica clave pública y los tokens de otros propósitos no pasan como pre-sesión.
- **Fuente:** [§5, 0.7](../CAPACIDADES_OIDC.md#fase-0--seguridad-bloqueante).

## Qué se corrigió

**Fallo crítico:** `signKeypass` ponía la **clave privada de firma** (PKCS#8 en base64) en la cabecera `kid` de los JWT de pre-sesión, y ese JWT viaja en la cookie `PRE_SESSION_ID` a cualquier navegador que empiece un login. Cualquiera podía leer la clave y firmar tokens de todos los tenants.

- Se elimina `signKeypass` del puerto `TokenSigner`: la pre-sesión se firma con `sign`, cuyo `kid` es el identificador público publicado en `/oauth/jwks`.
- La pre-sesión solo se acepta en `verifyChalleger`, que exige `scope = [challenge]` exacto y emisor del tenant.
- Las cookies emitidas antes del cambio siguen verificando (se prueba con todas las claves si el `kid` no coincide) hasta caducar.
- **Antes de utilizar claves heredadas fuera de desarrollo:** las claves antiguas han estado expuestas. Hay que retirarlas y generar claves nuevas (vaciar `_oauth_keys_storer` del anillo global con la aplicación parada; el job de [2.6](issue-2.6-generacion-claves-fuera-peticion.md) las regenera al arrancar). En estos entornos de desarrollo no hay sesiones ni tokens de producción que preservar.
- **Pruebas:** `JoseTokenSignerUnitTest` (el `kid` está en el JWKS y no es material de clave).
