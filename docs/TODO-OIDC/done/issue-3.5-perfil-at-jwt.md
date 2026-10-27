# 3.5 — Perfil at+jwt

- **Fase:** 3 (según necesidad) · **Estado:** hecha (25-10-2026), [J-03](../epicas/done/ep-J-03-emision-claims-proposito.md), **desactivada por defecto** · **Referencia:** RFC 9068.
- **Implementación:** emitir `typ: at+jwt` en cabecera y claims de access token según perfil.
- **Cierre:** resource server acepta access token de audiencia propia y rechaza propósito/audiencia incorrectos.
- **Fuente:** [§5, 3.5](../../../src/docs/oidc/CAPACIDADES_OIDC.md#fase-3--extensiones-de-un-idp-moderno).

## Qué hace (funcional)

Con `oauth.jwt.access-token.rfc9068=true` el access token lleva cabecera `typ: at+jwt` y `scope` como cadena separada por espacios; siempre lleva `iss`, `sub`, `aud`, `exp`, `iat`, `jti` y `client_id` (y `auth_time`/`acr`/`amr` en grants de usuario). Así un servidor de recursos distingue un access token de cualquier otro JWT firmado con las mismas claves.

- Desactivado por defecto: los consumidores externos que leen `scope` como array deben adaptarse antes. Los internos (`TokenVerifier`, UserInfo) ya aceptan ambos formatos.
- El rechazo por propósito o audiencia en los servidores de recursos quedó cubierto por [0.8](issue-0.8-verificacion-bearer-apis.md) y [0.15](issue-0.15-audiencia-tenant-revocacion-bearer.md) (J-04).
- **Pruebas:** `ClaimsMapperUnitTest` (perfil activado y heredado).
