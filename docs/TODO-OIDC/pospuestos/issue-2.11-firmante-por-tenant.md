# 2.11 — Firmante por tenant

- **Fase:** 2 · **Estado:** pospuesta (24-10-2026, decidida al abordar [J-02](../epicas/ep-J-02-clientes-tenant-secretos.md)).
- **Decisión:** todos los tenants firman con un único juego de claves global y común (`JoseTokenSigner.GLOBAL_KEY_RING`, fila `tenant = '_global'` de `_oauth_keys_storer`). El emisor sigue siendo por tenant (`<host>/oauth/openid/<tenant>`) y es lo que aísla los tenants: toda verificación interna exige el `iss` exacto del tenant de la ruta.
- **Motivo:** que los demás microservicios validen los tokens de forma sencilla con SmallRye JWT estándar: `mp.jwt.verify.publickey.location` admite una sola ubicación fija, y con claves por tenant cada servicio necesitaría un resolver dinámico (`quarkus-oidc` con `TenantConfigResolver` o una `JWTCallerPrincipalFactory` propia). La JWKS común se publica en `/oauth/jwks`; `/oauth/openid/<tenant>/jwks` devuelve las mismas claves.
- **Consecuencias:**
  - La firma ya no distingue tenants: un servicio que deba aceptar solo un tenant tiene que comprobar `iss` (`mp.jwt.verify.issuer`) o `tid`. Un servicio sin esa comprobación acepta tokens de cualquier tenant.
  - Al desplegar, los tokens vigentes firmados con claves de tenant dejan de verificarse (access 5 min, refresh 20 min): los usuarios vuelven a iniciar sesión. Las filas de claves por tenant quedan sin uso hasta que se borren.
  - Comprometer la clave afecta a todos los tenants; la rotación sigue siendo la existente (`key-duration`, `futures-key`).
- **Se reabre:** si un tenant exige claves propias (aislamiento criptográfico, BYOK o rotación independiente). Entonces habrá que documentar para los servicios consumidores la validación dinámica por emisor (issuer → JWKS del tenant).
- **Diseño previsto al reabrir:** volver a pasar el tenant a `TokenStoreGateway` desde `JoseTokenSigner`, con la JWKS global como unión transitoria durante la migración, y anunciar en discovery el `jwks_uri` del tenant.
