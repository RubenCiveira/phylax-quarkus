# OpenID Conformance Suite — phylax-api (V.2, V.3)

**Estado (27-10-2026): arnés preparado, nunca ejecutado.** Nada de esta carpeta acredita conformidad mientras no haya resultados en `out/` revisados. Ficha: [V.2](../docs/TODO-OIDC/issue-V.2-openid-conformance-suite.md).

## Qué ejecuta

| Plan de la suite | Variante | Ficha |
|---|---|---|
| `oidcc-basic-certification-test-plan` | `server_metadata=discovery`, `client_registration=static_client` | V.2 |
| `oidcc-config-certification-test-plan` | — | V.2 |
| `oidcc-rp-initiated-logout-certification-test-plan` | `response_type=code`, `static_client` | V.2 |
| `oidcc-backchannel-rp-initiated-logout-certification-test-plan` | `response_type=code`, `static_client` | V.2 |
| `oidcc-dynamic-certification-test-plan` (solo con `RUN_DYNAMIC=1`) | `response_type=code` | V.3 |

## Cómo

```sh
make conformance                 # o ./conformance/run.sh
KEEP_UP=1 SUITE_REF=<etiqueta> ./conformance/run.sh
```

En GitHub: workflow **conformance** (manual). Requisitos locales: Docker con compose v2, `git`, `mvn`, `curl`, `jq`, `envsubst`, `openssl` y `python3` con las dependencias de `scripts/run-test-plan.py` de la suite. Hace falta resolver `localhost.emobix.co.uk` y `op.phylax.test` a `127.0.0.1` (el primero ya resuelve así en DNS público).

`run.sh`:

1. Clona la suite (`SUITE_REF`, por defecto `master`; **fijar una etiqueta** para que los resultados sean reproducibles), la compila y la levanta con `suite-override.yml`, que hace que su servidor resuelva el OP por el host.
2. Levanta el OP (`docker-compose.yml`): MariaDB, phylax con `oidc-seed.yaml` (tenant raíz `main`, usuario `ROOT` sin contraseña temporal) y Caddy con TLS interno en `https://op.phylax.test:9443`. El issuer es `https://op.phylax.test:9443/oauth/openid/main`.
3. Activa DCR en `main` y crea un initial access token (digest calculado como `SecretDigester`).
4. Registra por DCR los tres clientes estáticos de los planes (dos `client_secret_basic` y uno `client_secret_post`) con las URIs de callback, post-logout, back-channel y front-channel de la suite, y rellena `plans/*.json`.
5. Ejecuta los planes con `run-test-plan.py` y exporta los resultados a `out/`.

## Riesgos conocidos antes de la primera ejecución

- **Login automatizado:** el formulario cifra la contraseña en JavaScript (`type_password` → `password`) y firma `csid`; si el HtmlUnit de la suite no ejecuta ese código, el login fallará y habrá que ajustar `browser` en `plans/*.json` o añadir un modo de prueba.
- **Consentimiento:** los clientes DCR no tienen `allowAllScopes`, así que puede aparecer la pantalla de scopes; la tarea `Consent` es opcional y su XPath sale de `ScopeConsentStep`.
- **TLS:** Caddy usa su CA interna; si la suite valida el certificado del OP, hay que añadir esa CA a su almacén de confianza.
- **Back-channel logout:** phylax llama a `https://localhost.emobix.co.uk:8443/...`, que en el contenedor apunta al host (`extra_hosts`). `BackChannelLogoutDispatcher` usa el `HttpClient` de Java, que valida TLS: si la JVM del contenedor no confía en el certificado del nginx de la suite, el plan de back-channel fallará por entorno, no por protocolo.
- **Rutas de la suite:** las URIs `…/test/a/phylax/{callback,post_logout_redirect,backchannel_logout,frontchannel_logout}` siguen la convención del alias; comprobarlas en la página de cada plan.
- **Dynamic OP (V.3):** la suite registra clientes **sin** initial access token, y phylax lo exige siempre (decisión heredada de PHP EP-03, sin modo `open`). Este plan fallará en el registro mientras esa decisión no cambie; por eso no se ejecuta por defecto.
