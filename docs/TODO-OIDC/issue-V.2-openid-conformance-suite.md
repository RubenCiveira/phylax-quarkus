# V.2 — OpenID Conformance Suite

- **Fase:** validación tras fases 0 y 1 · **Estado:** en curso (27-10-2026): arnés preparado, **nunca ejecutado**.
- **Implementación:** ejecutar perfiles *Basic OP*, *Config OP*, *RP-Initiated Logout* y *Back-Channel Logout* e integrarlos en CI.
- **Cierre:** resultados reproducibles y fallos corregidos antes de afirmar conformidad.
- **Dependencias:** fases 0 y 1, especialmente [1.1](done/issue-1.1-discovery-honesto.md).
- **Fuente:** [§5, validación](CAPACIDADES_OIDC.md#validación).

## Qué hay

[`conformance/`](../../conformance/README.md), con `make conformance` y el workflow manual `.github/workflows/conformance.yml`:

- `docker-compose.yml`: MariaDB, phylax (imagen JVM) con `oidc-seed.yaml` (tenant raíz `main`, `ROOT` sin contraseña temporal) y Caddy con TLS interno en `https://op.phylax.test:9443`.
- `run.sh`: clona y levanta la suite (`SUITE_REF`), activa DCR en `main`, crea un initial access token, registra por DCR los clientes estáticos de los planes (dos `client_secret_basic` y uno `client_secret_post`, con las URIs de callback, post-logout, back-channel y front-channel de la suite), rellena `plans/*.json` y ejecuta los planes con `run-test-plan.py`.
- Planes: `oidcc-basic-certification-test-plan`, `oidcc-config-certification-test-plan`, `oidcc-rp-initiated-logout-certification-test-plan`, `oidcc-backchannel-rp-initiated-logout-certification-test-plan`; *Dynamic OP* (V.3) solo con `RUN_DYNAMIC=1`.

## Por qué no se ha ejecutado

El 27-10-2026 el demonio de Docker no estaba disponible en la máquina de desarrollo, y la suite necesita sus contenedores (MongoDB, servidor y nginx). El arnés está escrito contra la documentación pública de la suite y **no se ha probado**: las variantes de los planes, las URIs `…/test/a/phylax/…` y la sintaxis de `browser` deben confirmarse en la primera ejecución.

## Riesgos ya identificados

- El formulario de login cifra la contraseña y firma `csid` en JavaScript; si el HtmlUnit de la suite no lo ejecuta, el login automatizado fallará.
- Los clientes DCR pueden ver la pantalla de scopes (sin `allowAllScopes`): hay una tarea `Consent` opcional.
- TLS: la CA interna de Caddy en la suite y el certificado de la suite en la JVM de phylax (back-channel logout).

## Pendiente

- Primera ejecución, fijar `SUITE_REF` a una etiqueta, guardar los resultados y corregir los fallos.
- Decidir si el workflow corre en cada PR o solo bajo demanda (hoy, bajo demanda).
