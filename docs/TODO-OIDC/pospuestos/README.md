# Pospuestas y descartadas

Fichas que no se implementan sin un caso de uso aprobado. Cada una indica el motivo y cuándo se reabriría. Al reabrir una, se devuelve a la raíz de `TODO-OIDC/` y se actualizan sus enlaces y su épica.

| Ficha | Estado | Motivo | Se reabre | Origen PHP |
|---|---|---|---|---|
| [2.11 Firmante por tenant](issue-2.11-firmante-por-tenant.md) | pospuesta | Un juego de claves global y común para que los microservicios validen con una JWKS fija; el emisor sigue siendo por tenant | Cuando un tenant exija claves propias (aislamiento criptográfico, BYOK o rotación independiente) | — (PHP cambia location e issuer dinámicamente) |
| [3.4 Federación OIDC genérica](issue-3.4-federacion-oidc-generica.md) | descartada | Sin demanda de IdPs OIDC por tenant; SSO corporativo por SAML | Con un cliente que aporte su propio IdP OIDC | [051](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/done/051-federacion-oidc-generica.md) |
| [3.9 Grant JWT bearer](issue-3.9-grant-jwt-bearer.md) | pospuesta | Los sistemas externos usan API key o `private_key_jwt` | Con el primer sistema externo con identidad de plataforma, o junto con token exchange (3.8) | [069](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/pospuestos/069-grant-jwt-bearer.md) |

La [4.9 Session Management iframe](../done/issue-4.9-session-management-iframe.md) se reabrió y quedó implementada en Java; PHP conserva su decisión de posponerla.
