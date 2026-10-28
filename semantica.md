# Plan de renombrado semántico del modelo `multitenant-oidc`

Estado (28-10-2026): **fases 0–4 aplicadas en el modelo y regeneradas en una sola pasada**; código a mano de phylax-api adaptado. Pendiente: migración de datos (D1), plantilla PHP ([docs/TODO-OIDC/plantilla-php-renombrado-modelo.md](docs/TODO-OIDC/plantilla-php-renombrado-modelo.md)), `multitenant-rbac.xml` y los avisos de la sección «Tras la regeneración».

## Tras la regeneración (28-10-2026)

Decisiones tomadas en el modelo: D2 → separar (`user-login-throttle`, `user-mfa-enrollment`, `user-verification-code` con `purpose`, una fila por usuario y propósito que se vacía al consumirse); D3 → (b) eliminar `public-allow`; D4 → ahora.

Código a mano:
- `VerificationCodeService` (nuevo): emite y consume los códigos de `user-verification-code`; comprueba la caducidad y vacía el código al aceptarlo. Lo usan `ChangePasswordAdapter` (recuperación) y `AccountDeletionAdapter` (borrado). Cierra el fallo anterior: el código de recuperación no caducaba ni se invalidaba (`reset-password-recover` vaciaba la semilla MFA) y era el mismo campo que el de borrado.
- `TrustedClientPolicy` → `OauthClientPolicy`; `ClientDetails.isResourceServer` → `introspectionAllowed`; `allowRegister(...)` de los gateways de registro → `selfRegistrationEnabled(...)`.
- Cliente del install: pasa a público (`token_endpoint_auth_method=none`, sin secreto); desaparece `oidc.trusted-client.secret` de los seeds.
- Las claves del seed (`oidc.relying-party.*`, `oidc.trusted-client.*`) **no** se han renombrado: cambiarlas rompe los ficheros externos de los despliegues.

Avisos sobre el modelo:
- `tenant-login-provider.disable`: `<set property="enabled" value="false	" />` lleva un tabulador.
- `tenant-login-provider.enabled` tiene `default="false"`: los proveedores nuevos nacen desactivados (antes `disabled=false`, activos).
- `user-verification-code`: no hay filtro por `purpose` (el servicio lista las filas del usuario y filtra en memoria); `purpose` no es obligatorio; las acciones se llaman `generated-verification` y `reset-register-verification`, aunque sirven para todos los propósitos; la descripción del índice dice «There cannot be one purpose…».
- `user-mfa-enrollment.expired-at` debería ser `expires-at` (es una caducidad futura), como en `user-verification-code`.
- `uses/multitenant-rbac.xml` sigue referenciando `relying-party` y `trusted-client`.

Migraciones generadas (`20261028120138`, `20261028121229`): crean las tablas y columnas nuevas vacías y dejan las antiguas. Además de copiar datos (D1), hay columnas antiguas `NOT NULL` sin valor por defecto que harán fallar los `INSERT` del código nuevo en una base existente: `access_user_accepted_terms_of_use.conditions` y `access_tenant_login_provider.source`. Y la nueva `access_tenant_login_provider.provider_type NOT NULL` / `access_user_accepted_terms_of_use.terms_of_use NOT NULL` se añaden sin valor por defecto sobre tablas con filas. `enabled` de los proveedores debe migrarse como `NOT disabled`.

Objetivo: que los nombres de entidades y propiedades digan lo que guardan y no choquen con el vocabulario de OAuth/OIDC, antes de que haya más código y más despliegues que dependan de ellos.

## Cómo se ejecuta cada cambio

El modelo vive en `civi-generator/src/main/resources/uses/multitenant-oidc.xml`, compartido por las plantillas Quarkus **y PHP** (`template/php/traits/*oidc*`). Cada cambio sigue el mismo ciclo:

1. **Modelo:** editar `multitenant-oidc.xml`.
2. **Regenerar** phylax-api y phylax-ui desde `phylax-model` (lo hace el usuario).
3. **Migración de datos** (ver abajo), en un changeset escrito a mano y separado del generado.
4. **Código a mano:** ajustar las referencias en `features/oauth`, `bootstrap/install`, SQL directo (`*SqlAdapter`, `DynamicClientAdapter`…) y pruebas.
5. **Plantilla Quarkus:** `binary/sync-oidc-template.py --apply` (y `--include-new --only …` para clases nuevas; `integrationtest/persistence` y los YAML, a mano con el mismo `render`).
6. **Plantilla PHP:** el mismo renombrado en `template/php/traits/*oidc*` (no hay script de sync).
7. **Verificar:** `mvn test`, `make test-mariadb`, `sync-oidc-template.py --check`.

### Riesgo transversal: el generador no migra datos

La regeneración de `allowed-login-redirects` produjo `20261028091704-model.changelog.mysql.sql`, que **crea la tabla nueva vacía** y deja la antigua huérfana. Con propiedades pasará lo mismo: columna nueva con el valor por defecto. Por tanto, cada cambio con datos existentes necesita:

```sql
-- tabla renombrada
INSERT INTO <nueva> (<cols>) SELECT <cols> FROM <antigua>;
DROP TABLE <antigua>;
-- propiedad renombrada
UPDATE <tabla> SET <nueva> = <antigua>;
ALTER TABLE <tabla> DROP COLUMN <antigua>;
```

**Decisión pendiente (D1):** dónde viven esos changesets para que la regeneración no los pise (¿un fichero `…-data.changelog.mysql.sql` en `000.001.001` referenciado desde `changelog.txt`, que también se regenera?). Si no hay ningún despliegue con datos, basta con limpiar el modelo y no hace falta migrar nada.

---

## Fase 0 — Cerrar el precedente (`allowed-login-redirects`)

| | |
|---|---|
| Estado | Modelo regenerado, código a mano y plantilla Quarkus actualizados |
| Falta | Migración de datos `access_trusted_client_allowed_redirect` → `access_trusted_client_allowed_login_redirect` y `DROP` de la antigua; plantilla PHP |

## Fase 1 — Propiedades (bajo coste)

Solo renombran columnas; ninguna cambia de comportamiento salvo `disabled` (se invierte).

| Entidad | Actual | Propuesto | Motivo | Migración |
|---|---|---|---|---|
| trusted-client | `back-channel-logout-uri` | `backchannel-logout-uri` | Nombre del metadato OIDC (Back-Channel Logout §2.2), el que DCR recibe y devuelve | copiar |
| trusted-client | `back-channel-logout-session-required` | `backchannel-logout-session-required` | Ídem | copiar |
| trusted-client | `front-channel-logout-uri` | `frontchannel-logout-uri` | Ídem (Front-Channel Logout §2) | copiar |
| trusted-client | `front-channel-logout-session-required` | `frontchannel-logout-session-required` | Ídem | copiar |
| trusted-client | `registration-access` | `registration-access-token` | Es el digest del *registration access token* (RFC 7592) | copiar |
| trusted-client | `allowed_scopes_m2m` | `client-credentials-scopes` | Único con guiones bajos en el XML; el generador lo convierte en `m_2m` | copiar |
| trusted-client | `m2m_token_ttl_seconds` | `client-credentials-token-ttl-seconds` | Ídem | copiar |
| tenant-config | `inner-label` | `display-name` | Su descripción: etiqueta de la aplicación para los usuarios | copiar |
| tenant-config | `allow-register` | `self-registration-enabled` | Permite el autorregistro | copiar |
| tenant-config | `enable-register-users` | `auto-approve-registered-users` | **No es un duplicado:** en PHP decide si el usuario autorregistrado queda `ACCEPTED` o `PENDING` | copiar |
| tenant-config | `allow-recover-pass` | `password-recovery-enabled` | Consistencia con el resto de flags `*-enabled` | copiar |
| tenant-config | `welcome-email`, `registered-email`, `disabled-user-email`, `enabled-user-email`, `recover-pass-email` | `*-email-template` (p. ej. `welcome-email-template`, `password-recovery-email-template`) | Son plantillas (blob), no direcciones ni flags | copiar |
| user | `approve` | `approval-status` | Es un estado (`UNVERIFIED`, `PENDING`, `ACCEPTED`, `REJECTED`), no una acción | copiar |
| user | `provider` | `federated-provider` | Proveedor con el que se identificó, no el actual | copiar |
| tenant-terms-of-use | `attached` | `document` | Binario adjunto a los términos | copiar |
| user-accepted-terms-of-use | `conditions` | `terms-of-use` | Referencia a `tenant-terms-of-use` | copiar |
| tenant-login-provider | `source` | `provider-type` | Valores `GOOGLE`, `GITHUB`, `SAML`, `OIDC`… | copiar |
| tenant-login-provider | `disabled` | `enabled` | Único negativo del modelo; **se invierte** (`enabled = NOT disabled`) y hay que ajustar las acciones `enable`/`disable` de la fachada | invertir |

Impacto aproximado (ficheros a revisar): código a mano en phylax-api < 20 por propiedad, phylax-ui ≤ 5 (generado), plantillas PHP 3–28 (p. ej. `UserApproveOptions` en 28). Los mayores: `approve`, `conditions`, `m2m`, `registration-access`, `disabled`.

## Fase 2 — Entidades de usuario

| Actual | Propuesto | Motivo |
|---|---|---|
| `user-access-temporal-code` | `user-credential-state` | Mezcla intentos fallidos de login, códigos de recuperación y registro y semilla MFA provisional; no es «un código» |
| `user-consented-scopes` | `user-scope-consent` | Una fila por decisión; plurales incoherentes |
| `user-consent-purposes` | `user-purpose-consent` | Ídem |

Migración: copiar tabla y `DROP`. Alternativa para `user-access-temporal-code`: separar en entidades por propósito (decisión **D2**).

## Fase 3 — `public-allow`

La duplicidad está confirmada: `publicAllowed()` devuelve `public_allow` **o** `token_endpoint_auth_method == none` ([ClientRetrieveAdapter.java:120–126](src/main/java/net/civeira/phylax/features/oauth/client/infrastructure/driven/ClientRetrieveAdapter.java#L120)). Renombrarlo a `allow-public-client` aclara el booleano, pero no elimina las dos fuentes de verdad.

Eliminarlo no es inmediato: hoy permite que un cliente **confidencial** (`client_secret_*`) use también el flujo público. Lo usan el cliente del install (`civi-acme-ui`: `public-allow=true` con `client_secret_post`) y el cliente legado de las pruebas (`test-client`).

**Decisión D3:**
- (a) Renombrar a `allow-public-client` y documentar que amplía `none`.
- (b) Eliminarlo: los clientes híbridos pasan a dos clientes, o el install crea el SPA con `none`; migración `UPDATE … SET token_endpoint_auth_method='none' WHERE public_allow AND …` según el caso.

Recomendación: (b) para clientes nuevos (OAuth 2.1 desaconseja clientes que sean públicos y confidenciales a la vez), empezando por el install.

## Fase 4 — Entidades principales (alto coste)

| Actual | Propuesto | Motivo |
|---|---|---|
| `relying-party` | `resource-server` (o `protected-api`) | En OIDC *Relying Party* es el **cliente**; aquí es la API protegida (tiene `api-key`, de ella cuelgan los roles, su `code` es la audiencia) |
| `trusted-client` | `client` (o `oauth-client`) | Contiene **todos** los clientes, también los de terceros registrados por DCR (`dynamically-registered`); «trusted» sugiere cliente propio sin consentimiento |
| `trusted-client.is-resource-server` | `introspection-allowed` | Su descripción: puede llamar a `/introspect`. Con `resource-server` como entidad, el nombre actual chocaría |

Impacto: rutas y campos de la API de administración (`access/relying-parties`, `access/trusted-clients`), phylax-ui (80 y 64 ficheros), referencias en `role`, `tenant-terms-of-use`, `user-role-assignment`, `user-group-membership`, `user-consented-scopes`, `trusted-client-allowed-*`; plantillas PHP (15 y 34) y Quarkus (7 y 20). Los permisos RBAC generados cambian de nombre: los roles existentes que los referencien necesitan migración.

**Decisión D4:** hacerla ahora o dejarla para cuando se coordine con el panel y con el gemelo PHP. Si se hace, `is-resource-server` debe ir en la misma regeneración.

## Fuera de alcance

Tablas internas `_oauth_*` (`_oauth_temporal_codes` son códigos de autorización, `_oauth_temporal_keys` y `_oauth_keys_storer` claves de firma): sus nombres también son vagos, pero no salen en la API ni en el panel. Quedan para otra ronda.

## Decisiones pendientes

| # | Decisión |
|---|---|
| D1 | ¿Hay despliegues con datos? Si sí, dónde van los changesets de datos escritos a mano |
| D2 | `user-access-temporal-code`: renombrar o separar |
| D3 | `public-allow`: renombrar o eliminar |
| D4 | Fase 4 ahora o más adelante |
| D5 | Nombres concretos: confirmar o cambiar los de cada tabla |

## Orden propuesto

Fase 0 → Fase 1 (una regeneración) → Fase 2 → Fase 3 → Fase 4. Cada fase es una regeneración y un commit, con `mvn test` y `make test-mariadb` en verde y la plantilla sin diferencias.
