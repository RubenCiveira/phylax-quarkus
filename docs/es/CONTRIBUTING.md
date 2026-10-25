# 🤝 Contribuyendo al Proyecto

¡Gracias por tu interés en contribuir!

Si aún no lo has hecho, únete a nuestro [grupo de Slack](https://slack.com/intl/es-es). Queremos que trabajes en lo que más te entusiasma.

Aquí tienes algunos recursos importantes:

- [Equipo](TEAM.md): quiénes somos,
- [Hoja de ruta](./ROADMAP.md): nuestros objetivos.
- [Issues](http://pivotaltracker.com/projects/): gestión diaria del proyecto.
- [Errores](https://participatorypolitics.lighthouseapp.com/projects/): dónde reportarlos.

## 📚 Tabla de Contenidos

- [🔧 Preparación del entorno](#preparacion-del-entorno)
- [🧪 Testing](#testing)
- [📦 Versionado Semántico](#versionado-semantico)
- [🌱 Envío de cambios con Git](#envio-de-cambios-con-git)
- [🧑‍💻 Convenciones de Código](#convenciones-de-codigo)
- [🌐 Entornos Disponibles](#entornos-disponibles)
- [🚀 Comandos Útiles](#comandos-utiles)

> 🇬🇧 [Read this guide in English](../../CONTRIBUTING.md)

## 🔧 Preparacion del entorno

Este proyecto está construido con:

- **Java**: versión 17+
- **Quarkus**: como framework principal
- **Maven**: como sistema de construcción
- **GraalVM** (opcional): para builds nativos y desarrollo más rápido

El proyecto requiere algunos archivos `.env`. Cada desarrollador debe crearlos y no incluirlos en el repositorio:

- Un archivo `.env` en la raíz del proyecto para configurar conexiones locales.
- Un archivo `.make/.env` para definir secretos, tokens o configuraciones para comandos de make.

### 🛠️ Instrucciones de configuración

Para lanzar el proyecto en modo desarrollo:

```bash
./mvnw quarkus:dev
```

Para compilar el proyecto:

```bash
./mvnw clean install
```

Para generar interfaces desde OpenAPI:

```bash
./mvnw generate-sources
```

> Se recomienda usar [SDKMAN](https://sdkman.io/) para gestionar versiones de Java y Maven.

## 🧪 Testing

Los tests automatizados nos ayudan a mantener la calidad del código y evitar regresiones.

Para ejecutar los tests localmente:

```bash
# Tests rápidos
make test
# Tests más profundos
make verify
```

O usando los comandos directos:

```bash
mvn clean verify -Pcoveraga
mvn clean verify -Pmutation
```

> Asegúrate de que los tests pasan **antes de abrir un pull request**.

Si tu contribución incluye nueva funcionalidad o correcciones, considera añadir los tests correspondientes.

## 📦 Versionado Semantico

Seguimos el estándar [Semantic Versioning 2.0.0](https://semver.org/lang/es/):

- `MAJOR`: cambios incompatibles
- `MINOR`: nuevas funcionalidades compatibles
- `PATCH`: correcciones compatibles

## 🌱 Envio de cambios con Git

Este proyecto usa una versión simplificada de [GitFlow](https://nvie.com/posts/a-successful-git-branching-model/), y sigue el estándar de [Commits Convencionales](https://www.conventionalcommits.org/es/v1.0.0/):

- `main`: código listo para producción
- `develop`: integración continua
- `feature/nombre`: nuevas funcionalidades
- `hotfix/nombre`: correcciones urgentes
- `release/x.y.z-rc.n`: candidatos a release
- `release/x.y.z`: versiones finales

Formato de commit:

```bash
<tipo>(<ámbito>): descripción
```

Tipos válidos:

- `feat`: nueva funcionalidad
- `fix`: corrección de errores
- `chore`: tareas de mantenimiento
- `docs`: documentación
- `style`: estilo (sin cambios en lógica)
- `refactor`: reestructuración de código
- `perf`: mejoras de rendimiento
- `test`: tests
- `build`: configuración del sistema de construcción
- `ci`: integración continua
- `revert`: revertir cambios
- `remove`: eliminaciones

Ejemplos:
```bash
feat(api): añadir autenticación OIDC
fix(ui): corregir alineación de botón
chore: actualizar dependencias
```

### 🚀 Comandos disponibles

- Crear hook para validar conventional commit:

```bash
make init
```

- Iniciar una feature:

```bash
git checkout develop
git checkout -b feature/nombre
# o usando Make
touch .env
make start-feature-nombre
```

- Finalizar una feature (si develop no está protegido):

```bash
git checkout develop
git merge --no-ff feature/nombre
git branch -d feature/nombre
# o usando Make
make finish-feature-nombre
```

- Iniciar un hotfix:

```bash
git checkout main
git checkout -b hotfix/nombre
# o usando Make
make start-hotfix-nombre
```

- Finalizar un hotfix (si main y develop no están protegidos):

```bash
git checkout main
git merge --no-ff hotfix/nombre
git tag vX.Y.Z
git checkout develop
git merge --no-ff hotfix/nombre
# o usando Make
make finish-hotfix-nombre
```

- Iniciar un release candidate:

```bash
git checkout -b release/x.y.z-rc.n
# Actualizar versión y changelog
git add . && git commit -m "chore(release): x.y.z-rc.n"
# o usando Make
make start-rc
```

- Iniciar un release final:

```bash
git checkout -b release/x.y.z
# Actualizar versión y changelog
git add . && git commit -m "chore(release): x.y.z"
# o usando Make
make start-release
```

## 🧑‍💻 Convenciones de Codigo

Este es un proyecto **Java** que sigue la [Guía de Estilo de Google](https://google.github.io/styleguide/javaguide.html).

### Filosofía API-First

Este proyecto sigue un enfoque **API-First**:

1. Primero se actualiza la especificación OpenAPI (Swagger).
2. Las interfaces se generan con `mvn generate-sources`.
3. Los controladores deben extender dichas interfaces generadas.
4. Todo cambio debe documentarse y revisarse.
5. Nunca modifiques el código generado directamente.

### Formato
- El formato del código se valida con un plugin de Eclipse.
- Usa la configuración XML de estilo Google.
- Algunos IDEs (IntelliJ, Eclipse) permiten importar el perfil de formato.

### Documentación
- Se usa AsciiDoc para documentar entidades, funcionalidades y pruebas manuales.
- Se encuentra en:
  ```
  src/main/docs/
  ```

## 📘 Documentacion de la API

Este proyecto está centrado en la API. Puedes utilizar los siguientes recursos:

### OpenAPI / Swagger
- Especificación OpenAPI:
  ```
  src/main/resources/META-INF/api.yaml
  ```
- Swagger UI (en local):
  ```
  http://localhost:8080/swagger-ui.html
  ```

### Coleccion de pruebas
- Colección de pruebas de la API:
  ```
  docs/api-clients/postman-collection.json
  ```

> Por favor, mantén estas colecciones actualizadas.

## 🌐 Entornos Disponibles

Estos entornos están desplegados continuamente para validación y demostraciones:

> 🔐 Para obtener credenciales, consulta al equipo. Revisa [TEAM.md](TEAM.md) para más información.

**Develop**  
> URL: https://dev.example.com  
> Descripción: Últimos cambios de la rama `develop`.

**Staging**  
> URL: https://staging.example.com  
> Descripción: Validación previa al release.

**Demo**  
> URL: https://demo.example.com  
> Descripción: Demostración pública o revisión interna.

## 🚀 Comandos Utiles

### Inicialización
```bash
make init
```

### Regenerar changelog
```bash
make generate-changelog
```

