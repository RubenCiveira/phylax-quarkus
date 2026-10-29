# Contrato B — Source root separado para lo generado

Todo el código generado vive bajo un único directorio por repositorio, registrado como
source root adicional del build. El código manual nunca entra ahí; el generador nunca
escribe fuera de ahí.

```
phylax-api/
├── src/main/java/          ← código manual (flujos de negocio, extensiones)
├── src/main/generated/     ← código generado (maestros, shared generado, bootstrap generado)
└── src/main/resources/

phylax-ui/
├── src/app/                 ← código manual
└── src/app/generated/       ← código generado (model, repository, components, actions, routes)
```

Registro del root en Maven (ya configurado, se incluye como referencia):

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>build-helper-maven-plugin</artifactId>
  <executions>
    <execution>
      <id>add-generated-sources</id>
      <phase>generate-sources</phase>
      <goals><goal>add-source</goal></goals>
      <configuration>
        <sources><source>src/main/generated</source></sources>
      </configuration>
    </execution>
  </executions>
</plugin>
```

Los paquetes Java pueden repartirse entre ambos roots: la extensión manual de un agregado
generado vive en el mismo paquete lógico, pero bajo `src/main/java`.

## Reglas para el equipo de desarrollo

Una sola regla:

> **Ningún commit humano toca `src/main/generated` (backend) ni `src/app/generated` (SPA).**

Todo lo demás es código vuestro. Para cambiar maestros: PR al repo `phylax-model`.

## Verificación

- `CODEOWNERS`:

  ```
  /src/main/generated/  @proveedor-generador
  /src/app/generated/   @proveedor-generador
  ```

- CI en cada PR (no requiere manifiesto ni herramienta especial):

  ```bash
  LAST_GEN=$(git describe --tags --match 'gen/*' --abbrev=0)
  git diff --quiet "$LAST_GEN"..HEAD -- src/main/generated \
    || { echo "src/main/generated alterado desde $LAST_GEN"; exit 1; }
  ```

- Formato: `mvn spotless:check` sobre `src/main/java`; lo generado ya sale formateado.

## Flujo de regeneración

1. PR del cliente a `phylax-model` (validación XSD en CI) → aprobación del proveedor.
2. El proveedor crea rama desde el último tag `gen/<n>`, regenera dentro de
   `src/main/generated` y `src/app/generated`, commitea con autor del generador y referencia
   al hash del modelo. Tag `gen/<n+1>` en ambos repos, emparejado con el commit del modelo.
3. El proveedor publica la rama; **el cliente la mergea en su `main`** y resuelve los
   conflictos de su lado (solo pueden existir en código manual que dependa de APIs generadas
   que hayan cambiado).
4. CI post-merge: el contenido de los directorios generados debe ser **byte a byte** el del
   tag `gen/<n+1>` (no basta "sin cambios desde el último commit": impide que un conflicto se
   "resuelva" conservando ediciones manuales).

## Incumplimientos

El CI los hace casi imposibles de cometer sin querer. Un push forzado o una manipulación del
historial que esquive el check se detecta en la verificación previa a cada regeneración
(árbol limpio + generado == último checkpoint) y se devuelve con el diff como evidencia.

## Valoración

| Pros | Contras |
|------|---------|
| La regla cabe en una frase; imposible incumplirla "sin darse cuenta" | Cambia la estructura actual del proyecto (migración inicial de ~1.800 ficheros backend + ~850 SPA) |
| CI trivial: un `git diff` por directorio, sin manifiesto | Dos roots para el mismo paquete lógico: navegar de la entidad generada a su extensión manual requiere un salto |
| El IDE muestra la frontera (roots distintos, iconos distintos) | Estéticamente discutible («¿por qué hay dos src?») — coste de explicación a cada incorporación |
| Borrado de huérfanos trivial: el generador posee el directorio completo | |

**Nota**: este es el contrato recomendado por defecto. Es el que tiene la mejor relación
entre claridad de la regla y coste de vigilancia.
