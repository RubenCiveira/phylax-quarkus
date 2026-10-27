# 3.7 — Algoritmos ES256 y EdDSA

- **Fase:** 3 (según necesidad) · **Estado:** pospuesta (27-10-2026) · **Referencia:** RFC 7518 / 8037.
- **Motivo:** `_oauth_keys_storer` no tiene columna de algoritmo: las claves son todas RSA. Requiere regenerar el modelo con la [especificación](../cambios-modelo-3.7.md). La columna `id_token_signed_response_alg` del cliente ya existe.
- **Diseño previsto:** rotación y ventana por algoritmo, JWKS con todas las claves vivas, firma según el algoritmo registrado por el cliente y anuncio solo de los probados.
- **Se reabre:** con un RP que exija ES256/EdDSA, tras regenerar el modelo.
