# 🧩 {{project-name}}

Una aplicación moderna en Java construida con [Quarkus](https://quarkus.io/) y siguiendo una filosofía API-first.

> 🇬🇧 [Read this documentation in English](../../README.md)

---

## 🚀 Funcionalidades

- 🔧 Desarrollado con **Java 17**, **Maven** y **Quarkus**
- 🧪 Incluye tests unitarios y de mutación
- 🧬 Desarrollo API-first con OpenAPI
- 🧹 Formato de código basado en la guía de estilo de Google
- 📘 Documentación escrita en AsciiDoc
- 🧰 Flujo de trabajo con Makefile
- 🔄 Versionado semántico y estructura GitFlow

---

## 🔧 Primeros Pasos

### Configuración del Entorno

Requisitos:

- Java 17+ ([Recomendado SDKMAN](https://sdkman.io/))
- Maven (incluido mediante `./mvnw`)
- GraalVM (opcional)

Clona el repositorio y ejecuta:

```bash
./mvnw quarkus:dev
```

> Consulta `.env.example` para las variables de entorno necesarias.

### Generación de APIs

```bash
./mvnw generate-sources
```

---

## 📘 Documentación de la API

- Especificación OpenAPI: `src/main/resources/META-INF/api.yaml`
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Colección Postman: `docs/api-clients/postman-collection.json`

---

## 🧪 Testing

```bash
make test       # Ejecuta tests rápidos
make verify     # Ejecuta tests de mutación
```

---

## 🌱 Contribuir

¡Las contribuciones son bienvenidas!
Por favor revisa [`CONTRIBUTING_es.md`](./docs/CONTRIBUTING_es.md) para guías de estilo y flujo de desarrollo.

---

## 📦 Versiones

Este proyecto utiliza [Versionado Semántico](https://semver.org/lang/es/):

- `MAJOR` — Cambios incompatibles
- `MINOR` — Nuevas funcionalidades compatibles
- `PATCH` — Correcciones y mejoras

Historial de versiones en [`CHANGELOG.md`](./CHANGELOG.md).

---

## 🌐 Entornos Disponibles

- **Develop**: https://dev.example.com  
- **Staging**: https://staging.example.com  
- **Demo**: https://demo.example.com

Consulta [`TEAM_es.md`](./docs/TEAM_es.md) para contactos o credenciales de acceso.

---

## 📄 Licencia

Consulta [`LICENSE_es`](./LICENSE_es)

