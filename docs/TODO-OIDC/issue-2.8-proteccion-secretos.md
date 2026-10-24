# 2.8 — Protección de secretos

- **Fase:** 2 · **Esfuerzo:** M · **Estado:** pendiente.
- **Decisión heredada (PHP EP-02):** comparación en tiempo constante y secretos no recuperables con la opción A (véase [2.9](issue-2.9-migracion-secretos-cifrado.md)); DCR devuelve el secreto en claro solo en el alta.
- **Implementación:** definir comparación temporalmente segura del secreto de cliente y evitar exposición posterior al alta DCR; revisar protección de contraseñas de usuario. La migración del formato almacenado es [2.9](issue-2.9-migracion-secretos-cifrado.md).
- **Cierre:** secreto válido funciona, uno incorrecto falla sin comparación insegura y una lectura DCR no vuelve a revelar secreto tras alta.
- **Dependencias:** coordinar con [0.9](issue-0.9-autenticacion-cliente-unificada.md) y [2.1](issue-2.1-registro-dinamico-clientes.md).
- **Fuente:** [§5, 2.8](../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-2--completar-lo-que-ya-está-a-medias).
