# Contratos de colaboración con código generado

Phylax se construye en dos niveles: los **maestros** (CRUD de entidades: backend Quarkus +
SPA Angular) se generan desde un modelo XML, y los **flujos de negocio** se desarrollan a mano.
El generador es una herramienta propietaria del proveedor y **no es un entregable**: lo que se
entrega es el código resultante, que es propiedad del cliente y compila sin ninguna herramienta
especial.

Estos documentos describen cuatro formas alternativas de organizar la colaboración entre el
equipo de desarrollo (cliente) y el proveedor del generador. Todas comparten el mismo
**protocolo de cambios de modelo** (descrito en cada contrato) y difieren en **cómo se
materializa y protege el código generado**.

| Contrato | Frontera de lo generado | Regla para el desarrollador | Complejidad |
|----------|------------------------|------------------------------|-------------|
| [A — Marcas en fichero](contrato-a-marcas-en-fichero.md) | Marcas `@Generated` + manifiesto de hashes | "No edites ficheros marcados" | Media |
| [B — Source root separado](contrato-b-source-root-separado.md) | Directorio `src/main/generated` | "No toques ese directorio" | Baja |
| [C — Artefacto binario](contrato-c-artefacto-binario.md) | Dependencia Maven/npm versionada | "No puedes tocarlo aunque quieras" | Baja (para el cliente) |
| [D — Disciplina git](contrato-d-disciplina-git.md) | Rama `generated` + checkpoints | "Lo generado solo cambia en commits del generador" | Alta |

## Invariantes comunes a todos los contratos

1. **El modelo XML es la única fuente de verdad de los maestros.** Cambios de entidades, campos
   u operaciones se piden mediante PR al repositorio `phylax-model` (validado por XSD).
2. **SLA de regeneración**: el proveedor regenera y entrega en un máximo de 2 días laborables
   tras la aprobación del PR de modelo.
3. **La regeneración sobreescribe sin miramientos.** Cualquier edición manual dentro de la
   frontera generada se pierde en la siguiente regeneración y la pérdida es responsabilidad de
   quien editó.
4. **Puntos de extensión**: la personalización se hace en código manual mediante los mecanismos
   previstos (observadores CDI `*Check` / `*Enrich` / `*AuthorizationDecision`, políticas en
   `application/policy`, componentes y acciones custom en la SPA), nunca editando lo generado.
5. **Formato canónico**: la configuración del formatter está commiteada y el CI la exige en cada
   PR. El generador emite código ya formateado, de modo que el formato nunca produce diffs.
6. **Autonomía del cliente garantizada**: los builds nunca dependen del generador ni de servicios
   del proveedor. Si el proveedor desaparece, el peor caso es mantener los maestros a mano.

## Qué opinar

Al revisar estos contratos, las preguntas relevantes son:

- ¿Qué frontera resulta más clara para un desarrollador que llega nuevo al proyecto?
- ¿Qué coste de fricción diaria (IDE, navegación, debugging) es aceptable?
- ¿Qué mecanismo de detección de incumplimientos genera menos discusiones?
- ¿Cómo encaja cada opción con los requisitos contractuales de entrega de fuentes (escrow)?
