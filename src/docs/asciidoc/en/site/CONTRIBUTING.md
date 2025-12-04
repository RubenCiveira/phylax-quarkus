# 🤝 Contributing to the Project

Thank you for your interest in contributing!

If you haven't already, come find us in [Slack group](https://slack.com/intl/es-es). We want you working on things you're excited about.

Here are some important resources:

- [Team](./TEAM.md): tells you who we are,
- [Our readmap](./ROADMAP.md): Are our objectives. 
- [Issues](http://pivotaltracker.com/projects/): is our day-to-day project management space.
- [Bugs](https://participatorypolitics.lighthouseapp.com/projects/): is where we report it

## 📚 Table of Contents

- [🔧 Environment setup](#environment-setup)
- [🧪 Testing](#testing)
- [📦 Semantic Versioning](#semantic-versioning)
- [🌱 Submitting changes with Git](#submitting-changes-with-git)
- [🧑‍💻 Code Conventions](#code-conventions)
- [🌐 Available Environments](#available-environments)
- [🚀 Usefull Commands](#useful-commands)


> 🇪🇸 [Leer esta guía en español](../es/CONTRIBUTING.md)


## 🔧 Environment setup

This project is built with:

- **Java**: version 17+
- **Quarkus**: as the main framework
- **Maven**: as build system
- **GraalVM** (optional): for native image builds and faster local dev

The project need some .env files. Each developer should create it and keep away from the repository:

- A `.env` file at the project root to configure local connections for the application.
- A `.make/.env` file to configure secrets, tokens, or environment overrides for make commands.

### 🛠️ Setup instructions

To launch the project locally in dev mode:

```bash
./mvnw quarkus:dev
```

To build the project:

```bash
./mvnw clean install
```

To generate sources from OpenAPI:

```bash
./mvnw generate-sources
```

> It is recommended to use [SDKMAN](https://sdkman.io/) to manage Java and Maven versions easily.

## 🧪 Testing

Automated tests help us maintain code quality and prevent regressions.

To run the tests locally:

```bash
# A fast unit test
make test
# A deep unit test
make verify
```

Or use the corresponding commands:

```bash
# A fast unit test
mvn clean verify -Pcoveraga
# A deep unit test
mvn clean verify -Pmutation
```

> Please ensure tests pass **before opening a pull request**.

If your contribution includes new functionality or fixes, consider adding appropriate unit or integration tests.


## 📦 Semantic Versioning

We follow [Semantic Versioning 2.0.0](https://semver.org/):

- `MAJOR`: breaking changes
- `MINOR`: new backward-compatible features
- `PATCH`: backward-compatible fixes

## 🌱 Submitting changes with Git

This project uses a simplified [GitFlow](https://nvie.com/posts/a-successful-git-branching-model/) structure, and follow [Conventional commit](https://www.conventionalcommits.org/en/v1.0.0/):

- `main`: production-ready code
- `develop`: ongoing integration
- `feature/name`: new features
- `hotfix/name`: emergency fixes
- `release/x.y.z-rc.n`: release candidates
- `release/x.y.z`: final releases

Each commit must follow this format:

```bash
<type>(<scope>): description
```

Valid types:

- `feat`: new feature
- `fix`: bug fix
- `chore`: maintenance tasks
- `docs`: documentation
- `style`: code style (no logic changes)
- `refactor`: code restructuring
- `perf`: performance improvements
- `test`: test additions or changes
- `build`: build system configuration
- `ci`: continuous integration
- `revert`: revert commits
- `remove`: feature removals

```bash
feat(api): add OIDC authentication
fix(ui): fix button alignment
chore: update dependencies
```

### 🚀 Available Commands for Git

- Create a hook to validate conventional commit

> There is a folder on .make/templates/_git with hooks including commit-msg to validate conventional commits.
>
> ```bash
> make init
> ```

- Start a feature:

> ```bash
> git checkout develop
> git checkout -b feature/name
> # or using Make
> touch .env
> make start-feature-name
> ```

- Finish a feature:

> If develop branch on the forge is unprotected:
> 
> ```bash
> git checkout develop
> git merge --no-ff feature/name
> git branch -d feature/name
> # or using Make
> make finish-feature-name
> ```
> 
> If develop branch on the forge is protected, then you need to make a pull request.

- Start a hotfix:

> ```bash
> git checkout main
> git checkout -b hotfix/name
> # or using Make
> make start-hotfix-name
> ```

- Finish a hotfix:

> If develop and main branches on the forge are unprotected:
> 
> ```bash
> git checkout main
> git merge --no-ff hotfix/name
> git tag vX.Y.Z
> git checkout develop
> git merge --no-ff hotfix/name
> # or using Make
> make finish-hotfix-name
> ```
> 
> If develop branch or main branch on the forge are protected, then you need to make a pull request for each branch, and tag.

- Start a release candidate:

> ```bash
> # Retrieve next version for the rc
> git checkout -b release/x.y.z-rc.n
> # Update project version to x.y.z-rc.n
> # update changelog
> git add . && git commit -m "chore(release): x.y.z-rc.n"
> # or using Make
> make start-rc
> ```
> 
> Release candidate must be finished via pull request, not manually.

- Start a final release:

> ```bash
> # Retrieve next version for the rc
> git checkout -b release/x.y.z
> # Update project version to x.y.z
> # update changelog
> git add . && git commit -m "chore(release): x.y.z"
> # or using Make
> make start-release
> ```
> 
> Releases must be finished via pull request, not manually.

## 🧑‍💻 Code Conventions

This is a **Java project** that follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

### API-First Philosophy

This project follows an **API-First** approach:

1. The OpenAPI (Swagger) specification must be updated first to reflect any new endpoints or changes.
2. The API interfaces are generated from the OpenAPI file using the Maven build (`mvn generate-sources`).
3. Controllers should **extend the generated interfaces** to ensure consistency and value.validation.
4. Any changes to the contract must be reviewed carefully and documented.
5. Do not modify generated code directly — always work through the OpenAPI definition.

> Maintaining strict adherence to the OpenAPI contract ensures compatibility, traceability, and well-documented services.


### Formatting
- Code formatting is enforced via an Eclipse plugin using the Google style XML configuration.
- Before committing, ensure your code is formatted correctly to avoid formatting-related review comments.
- Some IDEs (IntelliJ, Eclipse) can import the formatting profile directly.

### Documentation
- AsciiDoc files are used to document features, entities, and manual test guides.
- All documentation is stored under:
  ```
  src/main/docs/
  ```
- Contributions to documentation are welcome and appreciated. Please keep it consistent and up-to-date with the code.

## 📘 API Documentation

This project is API-centric. To assist in understanding and testing the API, the following resources are available:

### OpenAPI / Swagger
- The OpenAPI specification is located at:
  ```
  src/main/resources/META-INF/api.yaml
  ```
- If running locally, the Swagger UI may be accessible at:
  ```
  http://localhost:8080/swagger-ui.html
  ```

### Postman Collection
- The Postman collection for testing the API can be found at:
  ```
  docs/api-clients/postman-collection.json
  ```

> Keeping these files up to date with your changes is appreciated.


## 🌐 Available Environments

These environments are deployed continuously for validation and demonstration purposes:

> 🔐 To access these environments, please check with the team. See [Team.md](./TEAM.md) for contact info or secure credential repositories.

**Develop**  
> URL: https://dev.example.com  
> Description: Latest pushed changes from `develop` branch.

**Staging**  
> URL: https://staging.example.com  
> Description: Pre-release validation environment.

**Demo**  
> URL: https://demo.example.com  
> Description: Public-facing demo or internal business review.



## 🚀 Useful Commands

### Init
```bash
make init
```

### Rebuild changelog:
```bash
make generate-changelog
```


