# Contrato C — Maestros como artefacto binario versionado

El código generado **no está en los repositorios del cliente**. Los maestros se entregan
como artefactos versionados desde un repositorio de paquetes privado, y los repos del
cliente contienen únicamente código manual más la declaración de dependencia.

```
Backend:  net.civeira.phylax:phylax-api-masters:<versión>     (Maven, repo privado)
SPA:      @civeira/phylax-ui-masters@<versión>                (npm, registry privado)
```

```xml
<dependency>
  <groupId>net.civeira.phylax</groupId>
  <artifactId>phylax-api-masters</artifactId>
  <version>3.14.0</version>
</dependency>
```

Cada versión del artefacto se corresponde 1:1 con un commit etiquetado del repo
`phylax-model`, y se publica con su `sources.jar` / sourcemaps para debugging completo
en el IDE.

## Reglas para el equipo de desarrollo

- No hay nada que proteger: lo generado no es editable porque no está en el árbol de fuentes.
- La personalización usa exclusivamente los puntos de extensión públicos del artefacto
  (observadores CDI `*Check`/`*Enrich`/`*AuthorizationDecision`, políticas, `RbacStore`,
  componentes y rutas Angular inyectables).
- Para cambiar maestros: PR al repo `phylax-model`. La aprobación produce una **nueva versión
  del artefacto**; el cliente actualiza el número de versión cuando decida (los cambios de
  modelo son aditivos salvo aviso de breaking change en el CHANGELOG del artefacto).

## Flujo de regeneración

1. PR del cliente a `phylax-model` → aprobación.
2. El proveedor regenera, ejecuta la batería de tests del artefacto y **publica**
   `phylax-api-masters` y `phylax-ui-masters` con la nueva versión (semver: campo nuevo =
   minor; eliminación o renombrado = major, anunciado).
3. El cliente sube la versión en `pom.xml` / `package.json` en un PR normal suyo. El diff de
   adopción es **una línea**, y el CHANGELOG del artefacto describe qué entra.

## Garantías de autonomía (escrow)

Este contrato concentra el riesgo de dependencia del proveedor, por lo que incluye
obligatoriamente:

1. **Espejo del repositorio de paquetes**: el cliente mantiene un proxy (Nexus/Artifactory/
   Verdaccio) que cachea todas las versiones consumidas. Los builds nunca dependen de la
   disponibilidad del proveedor.
2. **Depósito de fuentes (escrow)**: en cada publicación, las fuentes completas del artefacto
   se depositan en un repositorio git accesible al cliente en condiciones pactadas
   (fin de contrato, cese de actividad). El cliente puede auditarlas en todo momento
   (el `sources.jar` ya las contiene).
3. Cláusula contractual: a la terminación, el cliente recibe las fuentes de la última versión
   y el derecho a mantenerlas; el generador y las plantillas siguen sin ser entregables.

## Incumplimientos

No aplican: la frontera es estructural. El único conflicto posible es semántico
(el cliente necesita un hook que no existe), y se resuelve por la vía del modelo:
los puntos de extensión también se declaran en el XML.

## Valoración

| Pros | Contras |
|------|---------|
| Frontera perfecta: ni el CI tiene que vigilar nada | El cliente no ve las fuentes en su árbol (mitigado por sources.jar, pero la percepción de "caja negra" existe) |
| Adopción de cambios = subir un número de versión; rollback trivial | Exige infraestructura: registry privado + espejo del cliente |
| Versionado semántico explícito de los maestros, con CHANGELOG | Cualquier necesidad no cubierta por un punto de extensión bloquea al cliente hasta la siguiente publicación → el SLA pasa a ser crítico |
| Historia limpia: los repos del cliente solo contienen su trabajo | Puede chocar con requisitos contractuales de "todo el fuente en el repo" — verificar antes de proponer |
| El generador y su salida nunca se exponen como fuentes editables | Debugging y lectura del código generado con un salto extra (decompilado/sources adjuntas) |

**Cuándo proponerlo**: clientes con madurez de plataforma (ya usan registry privado) y
contratos que admitan entrega de fuentes vía escrow en lugar de fuentes en el repo de trabajo.
