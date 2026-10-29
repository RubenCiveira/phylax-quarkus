# 2.8 — Protección de secretos

- **Fase:** 2 · **Esfuerzo:** M · **Estado:** hecha (25-10-2026), [J-02](../epicas/done/ep-J-02-clientes-tenant-secretos.md).
- **Decisión heredada (PHP EP-02):** comparación en tiempo constante y secretos no recuperables con la opción A (véase [2.9](issue-2.9-migracion-secretos-cifrado.md)); DCR devuelve el secreto en claro solo en el alta.
- **Implementación:** definir comparación temporalmente segura del secreto de cliente y evitar exposición posterior al alta DCR; revisar protección de contraseñas de usuario. La migración del formato almacenado es [2.9](issue-2.9-migracion-secretos-cifrado.md).
- **Cierre:** secreto válido funciona, uno incorrecto falla sin comparación insegura y una lectura DCR no vuelve a revelar secreto tras alta.
- **Dependencias:** coordinar con [0.9](issue-0.9-autenticacion-cliente-unificada.md) y [2.1](issue-2.1-registro-dinamico-clientes.md).
- **Fuente:** [§5, 2.8](../CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).

## Resultado de la revisión

- **Secreto de cliente:** el maestro generado ya guarda un verificador HMAC-SHA256 con *pepper* versionado (`SecretDigester`, `hmac-sha256:<id>:<hex>`) y compara con `MessageDigest.isEqual` (tiempo constante). El *pepper* no tiene valor por defecto fuera de dev/test.
- **DCR:** el secreto y el registration access token solo aparecen en la respuesta del alta; se guardan como digest y ninguna lectura los devuelve (probado en `DynamicClientRegistrationMariaDbTest`).
- **Contraseñas de usuario:** Argon2id con parámetros PHC compatibles con PHP (`PasswordHasher`); la ficha A0.6 del backlog de maestros describe el estado anterior.
- La migración de valores heredados es [2.9](issue-2.9-migracion-secretos-cifrado.md).
