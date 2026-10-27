# 3.11 — UserInfo firmado

- **Fase:** 3 · **Estado:** implementada y verificada (27-10-2026), [J-12](../epicas/done/ep-J-12-algoritmos-firma-cifrado.md) · **Referencia:** OIDC Core §5.3.2.
- **Implementación:** con `userinfo_signed_response_alg` (solo RS256, las claves del servidor) UserInfo responde `application/jwt` con `iss`, `aud` = cliente y las mismas claims del filtro de [1.5](issue-1.5-userinfo-conforme.md); un algoritmo no soportado da 500 `server_error`, nunca JSON en claro. Cliente sin configuración: JSON. Se combina con el cifrado de [4.5](issue-4.5-cifrado-jwe-id-token-userinfo.md).
- **Pruebas:** `JoseResponsesFlowTest`.
- **Plan PHP:** [060](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/060-userinfo-firmado.md).
