# 🧩 Phylax

A modern Java application built with [Quarkus](https://quarkus.io/) and following an API-first philosophy.

> 🇪🇸 [Leer esta documentación en español](../es/README.md)

---

## 🚀 Features

- 🔧 Built with **Java 17**, **Maven**, and **Quarkus**
- 🧪 Includes unit and mutation testing
- 🧬 API-first development using OpenAPI
- 🧹 Code formatting based on Google Java Style
- 📘 Documentation written in AsciiDoc
- 🧰 Makefile-based developer workflow
- 🔄 Semantic Versioning and GitFlow structure

---

## 🔧 Getting Started

### Environment Setup

You'll need:

- Java 17+ ([SDKMAN recommended](https://sdkman.io/))
- Maven (included via `./mvnw` wrapper)
- GraalVM (optional)

Clone the project and run:

```bash
./mvnw quarkus:dev
```

> See `.env.example` for environment variables.

### API Generation

```bash
./mvnw generate-sources
```

---

## 📘 API Documentation

- OpenAPI spec: `src/main/resources/META-INF/api.yaml`
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Postman collection: `docs/api-clients/postman-collection.json`

---

## 🧪 Testing

```bash
make test       # Run fast unit tests
make verify     # Run mutation tests
```

---

## 🌱 Contributing

We welcome contributions!
Please see [`CONTRIBUTING.md`](./CONTRIBUTING.md) for style guides and development workflows.

---

## 📦 Releases

This project uses [Semantic Versioning](https://semver.org/):

- `MAJOR` — Breaking changes
- `MINOR` — Backward-compatible features
- `PATCH` — Fixes and improvements

Release history can be found in [`CHANGELOG.md`](./CHANGELOG.md).

---

## 🔐 Environments

- **Develop**: https://dev.example.com  
- **Staging**: https://staging.example.com  
- **Demo**: https://demo.example.com

See [`TEAM.md`](./TEAM.md) for contact details or credentials access.

---

## 📄 License

See [`LICENSE`](./LICENSE)

