# Contrato D — Disciplina git pura (rama `generated` + checkpoints)

No hay separación física ni manifiesto: el código generado y el manual conviven en
`src/main/java` tal como están hoy. La frontera se define **en el historial de git**:
lo generado solo puede cambiar en commits del generador, que viven en una rama propia
y se integran mediante merges con ancestro común.

```
phylax-model:  m1 ──── m2 ───────────── m3
                │       │                │
generated:     g1 ───── g2 ───────────── g3        ← solo commits del generador (checkpoints)
                 \        \                \
main:       ──────●──h1──●──h2──h3──h4─────●──     ← merges de generated + commits humanos
```

- Rama `generated`: cada commit es un snapshot prístino de la salida del generador,
  con autor `Phylax Generator <gen@civeira.net>`, mensaje `chore(gen): model <hash>` y
  tag `gen/<n>`. Incluye un fichero de inventario `.phylax/generated-files.txt` con la
  lista de rutas que el generador posee en ese snapshot.
- Rama `main`: trabajo del cliente + merges periódicos de `generated`.
- La misma estructura se replica en `phylax-ui`, con tags emparejados por versión de modelo.

## Reglas para el equipo de desarrollo

1. Ningún commit humano modifica rutas listadas en el inventario del último checkpoint.
2. Nadie commitea en la rama `generated` (protegida; solo el bot/identidad del generador).
3. Los merges de `generated` a `main` se integran tal cual: un conflicto **nunca** se
   resuelve conservando la versión manual de un fichero del inventario.
4. Cambios de maestros: PR al repo `phylax-model`.

## Verificación (CI)

En cada PR a `main`:

```bash
LAST_GEN=$(git describe --tags --match 'gen/*' --abbrev=0 origin/generated)
# Ficheros tocados por el PR que pertenecen al inventario del último checkpoint
git diff --name-only origin/main...HEAD \
  | grep -xF -f <(git show "$LAST_GEN:.phylax/generated-files.txt") \
  && { echo "El PR modifica ficheros generados"; exit 1; } || true
```

Tras cada merge de regeneración:

```bash
# El contenido de las rutas del inventario en main debe ser idéntico al checkpoint
git diff --quiet "$LAST_GEN" HEAD -- $(git show "$LAST_GEN:.phylax/generated-files.txt" | tr '\n' ' ')
```

## Flujo de regeneración

1. PR del cliente a `phylax-model` → aprobación.
2. El proveedor verifica `main` del cliente: árbol limpio, `spotless:check` en verde,
   inventario intacto respecto a `gen/<n>`. Si falla → rebote automático con el diff.
3. Sobre la rama `generated` (es decir, sobre el snapshot anterior): regenerar, commitear,
   tag `gen/<n+1>`. El fichero de inventario actualizado borra los huérfanos.
4. El proveedor abre PR de merge `generated → main` en ambos repos. **Git hace three-way
   merge real** porque `gen/<n>` es ancestro común: todo lo mecánico se auto-resuelve y los
   conflictos marcan exactamente el código manual afectado por el cambio de modelo.
5. El cliente resuelve sus conflictos (solo en su código) y mergea. CI post-merge verifica
   inventario == checkpoint.

## Recuperación tras una etapa sin disciplina

Es el contrato que mejor se recupera de un periodo de incumplimiento: aunque el cliente haya
editado lo generado durante meses, `git diff gen/<n>..main -- <inventario>` produce la lista
exacta de la deriva, y el merge desde `gen/<n+1>` convierte cada divergencia en un conflicto
explícito en lugar de en una sobreescritura silenciosa. Ningún otro contrato conserva tanto
contexto para el arbitraje.

## Valoración

| Pros | Contras |
|------|---------|
| Cero cambios en la estructura del proyecto | Es el contrato más difícil de explicar: exige entender ramas, ancestros y merges — la regla no se ve mirando el árbol de ficheros |
| El merge three-way hace el trabajo de reconciliación; los conflictos son la lista de tareas | El CI es el más elaborado (inventario + dos tipos de check) |
| Mejor trazabilidad: cada divergencia tiene commit, autor y fecha | Depende de que la rama `generated` esté protegida de verdad; un force-push malintencionado complica el arbitraje (mitigable: réplica de tags en el repo del proveedor) |
| La recuperación tras incumplimientos masivos es ordenada, no destructiva | La edición accidental sigue siendo fácil de *intentar* (el fichero está ahí, editable); solo el CI la frena |

**Cuándo proponerlo**: equipos con cultura git sólida, o como complemento de los contratos
A/B (la rama `generated` y los checkpoints son compatibles con ambos y mejoran cualquier
recuperación).
