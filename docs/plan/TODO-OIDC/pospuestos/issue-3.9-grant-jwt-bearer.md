# 3.9 — Grant JWT bearer

- **Fase:** 3 (solo con caso de uso) · **Estado:** pospuesta (heredada de PHP EP-11).
- **Motivo:** los sistemas externos usan API key o `private_key_jwt` ([3.2](../done/issue-3.2-autenticacion-jwt-cliente.md)). Se reabre con el primer sistema externo con identidad de plataforma, o junto con token exchange ([3.8](../done/issue-3.8-token-exchange.md)). Mientras tanto no se anuncia en discovery.
- **Diseño previsto:** emisores de confianza por tenant, reglas de claims con roles y `sub` del sistema, y anti-replay por emisor sobre el almacén de 3.2.
- **Cierre:** assertion autorizada aceptada; emisor o JTI no confiable y replay rechazados.
- **Plan PHP:** [069](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/pospuestos/069-grant-jwt-bearer.md).
- **Fuente:** [§5, 3.9](../CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).
