# Implementadas

Fichas implementadas en Java y cubiertas por pruebas automáticas (unitarias y HTTP con dobles en memoria). La última columna recoge la verificación contra sistemas reales que queda por hacer; no bloquea el cierre, pero hay que hacerla antes de desplegar.

| Ficha | Épica | Verificación real pendiente |
|---|---|---|
| [0.1 Canje seguro de authorization code](issue-0.1-canje-seguro-de-authorization-code.md) | [J-01](../epicas/ep-J-01-token-y-grants-seguros.md) | Hecha (24-10-2026) en el arnés [V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md). Queda revocar los tokens del primer canje ante un segundo intento |
| [0.2 Refresh ligado al cliente y a la sesión](issue-0.2-refresh-cliente-sesion.md) | [J-01](../epicas/ep-J-01-token-y-grants-seguros.md) | Hecha (24-10-2026) en el arnés [V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md), con la corrección del barrido por tenant |
| [0.9 Autenticación de cliente unificada](issue-0.9-autenticacion-cliente-unificada.md) | [J-01](../epicas/ep-J-01-token-y-grants-seguros.md) | Device y PAR con un cliente público y uno confidencial reales (el perfil de test los excluye); clientes existentes cuyo método registrado no coincida con el que usan |
| [0.13 Ruta de revocación no duplicada](issue-0.13-ruta-revocacion-no-duplicada.md) | [J-01](../epicas/ep-J-01-token-y-grants-seguros.md) | Confirmar que la UI de login usa `/revoke` y no `/revocation` |
| [1.4 Errores y caché de token](issue-1.4-errores-cache-token.md) | [J-01](../epicas/ep-J-01-token-y-grants-seguros.md) | Avisar a integradores de `password` (400 `invalid_grant`) y `mfa_required` (403) |
| [1.8 Code flow para clientes confidenciales](issue-1.8-code-clientes-confidenciales.md) | [J-01](../epicas/ep-J-01-token-y-grants-seguros.md) | Login real de una web app confidencial sin `publicAllow` |
| [1.16 Emisión y canje fuera del TokenController](issue-1.16-emision-canje-fuera-tokencontroller.md) | [J-01](../epicas/ep-J-01-token-y-grants-seguros.md) | — (refactorización cubierta por la batería HTTP) |
| [0.16 Seguridad del login federado](issue-0.16-seguridad-login-federado.md) | [J-08](../epicas/ep-J-08-login-consentimiento-producto.md) | Login real con un IdP SAML (firma de respuesta y de aserción) y con Google; migrar la configuración SAML de los proveedores existentes |
