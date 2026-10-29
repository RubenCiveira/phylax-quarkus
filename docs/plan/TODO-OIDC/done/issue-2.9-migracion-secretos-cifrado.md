# 2.9 — Migración de secretos y cifrado en reposo

- **Fase:** 2 (adelantar si se habilita DCR) · **Esfuerzo:** L · **Estado:** hecha (25-10-2026), [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md).
- **Decisión heredada (PHP EP-02):** opción A de 063: verificador HMAC + *pepper* para todos los secretos de cliente (por eso [3.2](issue-3.2-autenticacion-jwt-cliente.md) solo implementa `private_key_jwt`); re-hash de contraseñas de usuario **en bloque**, no perezoso. La ubicación de la clave AES y del *pepper* (variable de entorno o secreto montado) y el versionado del *pepper* se deciden para el despliegue Java.
- **Implementación:** decidir formato no reversible para secreto OAuth y transición de registros cifrados existentes sin interrumpir autenticación; mantener verificación temporalmente segura. Revisar origen/rotación de la clave AES y esquema de contraseñas de usuario, con migración o rehash documentado.
- **Cierre:** clientes antiguos y nuevos autentican durante despliegue, luego los secretos no son recuperables ni reaparecen en GET de DCR; pruebas de migración y contraseña errónea. No reutilizar el cifrado reversible como verificador.
- **Dependencias:** [0.9](issue-0.9-autenticacion-cliente-unificada.md), [2.8](issue-2.8-proteccion-secretos.md), [2.1](issue-2.1-registro-dinamico-clientes.md).
- **Plan PHP:** [EP-02 secretos y cifrado](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/EP-02-secretos-y-cifrado.md) (063, 071); el esquema y la clave Java deben decidirse con el modelo Java.

## Qué hace (funcional)

Al arrancar, justo después de las migraciones de esquema, `LegacyCredentialMigration` convierte **en bloque** las credenciales que aún estén cifradas de forma reversible (`AesCipherService.encryptForAll`):

- `access_trusted_client.secret_oauth` sin prefijo `hmac-sha256:` → se descifra una vez y se guarda su digest.
- `access_user.password` no vacía y sin prefijo `$argon2id$` → se descifra y se rehashea con Argon2id.

Es idempotente (tras migrar, las consultas no encuentran nada) y no expone valores en logs. Lo que no se puede descifrar se deja y se registra: ese cliente o usuario necesita una credencial nueva. Clientes y usuarios antiguos siguen autenticando durante el despliegue.

## Decisiones de despliegue Java

- *Pepper*: `SECURITY_DIGEST_PEPPERS` (`id:secreto,…`) y `SECURITY_DIGEST_CURRENT`; rotar = añadir un id nuevo y hacerlo actual. Un digest con un *pepper* retirado deja de verificar, así que se retira cuando no quedan valores suyos.
- Clave AES (`app.security.encryption.key`): solo se necesita para esta migración y para datos que deben ser reversibles (semillas TOTP usan `security.secrets`).
- **Límite:** los lookups por digest (initial access token, registration access token) usan el *pepper* actual; tras rotar, esos tokens deben reemitirse.
