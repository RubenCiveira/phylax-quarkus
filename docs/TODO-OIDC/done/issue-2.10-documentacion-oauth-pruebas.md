# 2.10 — Documentación OAuth alineada con pruebas

- **Fase:** 2 / cierre transversal · **Esfuerzo:** S–M · **Estado:** hecha (27-10-2026), [J-09](../epicas/ep-J-09-discovery-e2e-documentacion.md). Repasar tras la primera ejecución de [V.2](../issue-V.2-openid-conformance-suite.md).
- **Implementación:** revisar guías y ejemplos del repositorio Java al cerrar cada épica; rutas por tenant, discovery, grants, seguridad y clientes reflejan únicamente comportamientos demostrados por pruebas.
- **Cierre:** cada funcionalidad anunciada/documentada enlaza a una ruta y prueba; se corrigen textos obsoletos sin declarar conformidad antes de [V.2](../issue-V.2-openid-conformance-suite.md).
- **Plan PHP:** [066-actualizar-documentacion-oauth.md](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/066-actualizar-documentacion-oauth.md), adaptado a `src/docs/oidc` y `docs/oauth` Java.

## Qué se ha hecho

| Documento | Cambio |
|---|---|
| [docs/oauth/overview.md](../../oauth/overview.md) | Reescrito: catálogo de las rutas que existen, con su controlador y las pruebas HTTP que las verifican; las que no tienen prueba HTTP lo dicen (enlace mágico, WebAuthn, invitaciones, perfil, JWKS); de dónde sale cada valor del discovery; reglas de DCR. Aviso explícito de que no se declara conformidad |
| [docs/oauth/tests.md](../../oauth/tests.md) | Reescrito: las tres capas (unitaria, flujo HTTP con dobles, MariaDB), cómo se ejecutan, qué garantiza cada prueba MariaDB y qué falta. Sustituye un catálogo de 62 pruebas de un paquete que ya no existe |
| `docs/oauth/architecture.md`, `controller-parts.md`, `flows.md` | Marcados como **históricos**: describen `FrontAcessController`, `ControllerPart` y `features/access/oauth`, que ya no existen. No se han borrado |
| [src/docs/oidc/CAPACIDADES_OIDC.md](../../../src/docs/oidc/CAPACIDADES_OIDC.md) | Nota de vigencia: es la foto del 20-10-2026 y el origen de las fichas, no el estado actual |
| `client/package-info.java` | Rutas reales de DCR (`/connect`), `JwtAlgorithms`, `DcrExtensionMetadata` y el mecanismo de autenticación de DCR |

## Pendiente

- Decidir si se reescriben o se borran las tres guías históricas.
- `docs/oauth/curl-matrix.sh` no se ha revisado.
- Repasar `overview.md` cuando haya resultados de la suite y pruebas HTTP de las rutas que hoy no las tienen.
