# Épicas OIDC para phylax-api (Java)

Adaptación del [plan PHP](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/epicas/README.md) al [backlog Java](../README.md) y al [análisis del código Java](../../../src/docs/oidc/CAPACIDADES_OIDC.md). Las fichas individuales son la unidad de cierre; cada una tiene **una épica propietaria**. Las rutas, clases y modelos PHP son referencias de contrato, nunca instrucciones para copiar su arquitectura ni prueba de que Java ya lo implemente. El `done/` del plan PHP significa hecho en PHP, no en Java; las fichas Java implementadas están en [`../done/`](../done/README.md), y las descartadas o pospuestas en [`../pospuestos/`](../pospuestos/README.md). Prioridades basadas en la seguridad Java; extensiones bajo demanda.

| Épica | Propietaria de tareas Java | Prioridad | Depende de | Referencia PHP |
|---|---|---|---|---|
| [J-01 Token y grants seguros](ep-J-01-token-y-grants-seguros.md) | 0.1, 0.2, 0.9, 0.13, 1.4, 1.8, 1.10, 1.16 | bloqueante | J-02 política cliente | 017, 019, 020 hechos; EP-05, 064 |
| [J-02 Clientes, tenant y secretos](ep-J-02-clientes-tenant-secretos.md) | 0.3, 0.4, 0.5, 0.10, 2.1, 2.4, 2.8, 2.9 | bloqueante | — | EP-02; EP-03 hecha en PHP |
| [J-03 Emisión y claims](ep-J-03-emision-claims-proposito.md) | 0.7, 1.2, 1.13, 1.15, 2.6, 3.5 | alta | J-02, J-01 | EP-04 hecha; EP-05 |
| [J-04 Bearer, introspección y UserInfo](ep-J-04-bearer-introspeccion-userinfo.md) | 0.8, 0.15, 1.5, 1.9 | crítica | J-03 tipo; J-02 audiencia | EP-06 |
| [J-05 Solicitudes authorize, PAR y JAR](ep-J-05-solicitudes-authorize-par-jar.md) | 0.11, 0.14, 1.11, 2.3, 2.7, 3.3, 3.10 | alta; extensiones opc. | J-02 | EP-07; 034, 046, 047, 068 |
| [J-06 Respuestas authorize](ep-J-06-respuestas-authorize.md) | 1.3, 1.7, 1.12, 3.1 | alta; form_post opc. | J-03, J-05 | EP-08 |
| [J-07 Sesión y logout](ep-J-07-sesion-sso-logout.md) | 0.6, 0.12, 1.14, 4.9, 4.10 | alta; iframes opc. | J-02, J-03 | EP-09; 021 hecha en PHP |
| [J-08 Login, federación y grants de producto](ep-J-08-login-consentimiento-producto.md) | 0.16, 1.6, 2.2, 2.5, 3.8, 4.3 (3.4 descartada) | **crítica (0.16)**; media el resto; extensiones opc. | J-02, J-04 (0.16: solo 0.5) | EP-10, 072, EP-12 |
| [J-09 Discovery, E2E y documentación](ep-J-09-discovery-e2e-documentacion.md) | 1.1, 2.10, V.1, V.2, V.3 | inmediata/paralela y cierre | V.2 tras tareas obligatorias J-01–J-08 | EP-01 hecha, EP-16 |
| [J-10 Aserciones JWT](ep-J-10-aserciones-jwt-cliente-grant.md) | 3.2 (3.9 pospuesta) | opcional | J-02, J-05 | EP-11 |
| [J-11 Tokens ligados](ep-J-11-tokens-ligados-clave-certificado.md) | 3.6, 4.1 | opcional | J-03–J-05 | EP-13 |
| [J-12 JOSE avanzado](ep-J-12-algoritmos-firma-cifrado.md) | 3.7, 3.11, 4.2, 4.5 | opcional | J-03, J-04, J-06 | EP-14 |
| [J-13 Claims/autorización avanzada](ep-J-13-solicitudes-claims-avanzados.md) | 4.4, 4.6, 4.7, 4.8 | opcional | J-03, J-05 | EP-15 |

## Orden recomendado

1. **De inmediato:** arnés [V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md), discovery [1.1](../issue-1.1-discovery-honesto.md) y diseño de clientes [J-02](ep-J-02-clientes-tenant-secretos.md). Corregir [0.1](../done/issue-0.1-canje-seguro-de-authorization-code.md), [0.2](../done/issue-0.2-refresh-cliente-sesion.md) y el login federado [0.16](../done/issue-0.16-seguridad-login-federado.md) sin esperar a terminar todo J-02.
2. **Bloqueo de seguridad:** J-01 + J-02, con pruebas concurrentes reales. Adelantar J-04 (revocación) al estar disponible la política de tokens.
3. **Conformidad básica:** J-03 → J-05 → J-06; J-07 en paralelo una vez fijados `sid` y clientes participantes. J-08 completa consentimiento y funcionalidades ya empezadas.
4. **Cierre:** J-09 verifica en HTTP + base de datos, corre OpenID Conformance Suite y actualiza documentación. No esperar a esta ola para escribir pruebas de cada épica.
5. **Bajo demanda:** J-10–J-13 y las tareas opcionales de J-05–J-08; sin caso de uso aprobado, mantenerlas ausentes del discovery.

## Ajustes frente a PHP

- **Sincronización 23-10-2026:** PHP cerró EP-01 a EP-11. Sus decisiones de producto y contrato (secretos opción A, solo `private_key_jwt`, clientes globales, audiencia «al menos una», implicit retirado, 040/051 descartadas, 041/062/069 pospuestas) están en [Decisiones heredadas de PHP](../README.md#decisiones-heredadas-de-php) y en cada ficha. La ficha PHP 072 dio lugar a [0.16](../done/issue-0.16-seguridad-login-federado.md): el login federado Java tiene fallos equivalentes, más graves (la cuenta se elige por `name`).
- **Objetivo de equivalencia:** el mismo comportamiento observable por HTTP y discovery en PHP y Java. Se mide con la batería compartida de [V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md) ejecutada contra los dos servidores y con OpenID Conformance Suite en ambos (J-09 / EP-16).

- La firma RS256/JWKS, MFA con recuperación, `max_age`/step-up y administración de sesiones existen en Java: no se reabren como tareas completas; se prueban donde impactan el contrato.
- Java ya tiene `ClaimsMapper`/`JwtTokenBuilder`; J-03 consolida su uso, no introduce un `TokenFactory` paralelo por copiar EP-05.
- Java ya rechaza PKCE `plain` y da precedencia al PAR almacenado; J-05 cierra las validaciones y casos negativos restantes, no reproduce el bug PHP de query sobre PAR.
- PHP tiene DCR/aislamiento y refresh familiar en su código; sus fichas `done/` sirven de criterios para J-01/J-02. PHP aún tiene tareas pendientes; no se asume conformidad PHP.
- La estimación S/M/L en [el índice Java](../README.md) no equivale a la del plan PHP. Cualquier migración de datos se diseña sobre el esquema Java antes de codificarla.
