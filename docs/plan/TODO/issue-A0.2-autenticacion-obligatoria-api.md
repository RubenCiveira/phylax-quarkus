# A0.2 — Autenticación obligatoria en `/api/*`

- **Fase:** 0 · **Esfuerzo:** S · **Estado:** pendiente · **Dónde:** Configuración.
- **Problema:** Ni `@Authenticated` / `@RolesAllowed` ni `quarkus.http.auth.permission.*`: las interfaces OpenAPI generadas (`/api/access/...`) aceptan peticiones anónimas, que llegan a los casos de uso.
- **Implementación:** Declarar en la configuración una política `authenticated` para `/api/*` y una lista explícita de rutas públicas (`/oauth/openid/*`, `/q/health`, discovery, JWKS…). Alternativa: que la plantilla de la interfaz OpenAPI emita `@Authenticated`, con excepciones marcadas en el modelo.
- **Cierre:** Una petición sin token a cualquier `/api/access/*` devuelve 401. Las rutas públicas declaradas siguen respondiendo. Hay un test que recorre todas las rutas `/api` y comprueba el 401.
- **Fuente:** [§4.1](../../src/docs/access/ANALISIS_DISENO_ACCESS.md#41-crítico--autorización-fail-open-por-diseño).
