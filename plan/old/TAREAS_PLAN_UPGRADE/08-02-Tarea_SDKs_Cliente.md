# Tarea 08-02 — SDKs cliente

> **Fase:** 08 — Developer Experience
> **Prioridad:** P4
> **Esfuerzo estimado:** Alto
> **Prerequisito:** 02-06 (OpenAPI completo), 02-01 (PKCE implementado)

---

## Descripción

SDKs oficiales que reducen el tiempo de integración con Phylax para los
desarrolladores que construyen aplicaciones sobre esta plataforma BaaS.

---

## SDK 1 — JavaScript / TypeScript (`@phylax/client`)

### Funcionalidades mínimas

```typescript
// Inicialización
const phylax = new PhylaxClient({
  issuer: 'https://auth.example.com/openid/my-tenant',
  clientId: 'my-app-client-id',
  redirectUri: 'https://app.example.com/callback',
  scopes: ['openid', 'email', 'profile'],
});

// Login — usa PKCE por defecto
await phylax.signIn();

// Obtener sesión actual
const session = await phylax.getSession();
// { user: { sub, email, ... }, accessToken, expiresAt }

// Obtener token de acceso (renovado automáticamente si está expirado)
const token = await phylax.getAccessToken();

// Logout
await phylax.signOut({ redirectUri: 'https://app.example.com' });

// Verificar si está autenticado
const isAuth = phylax.isAuthenticated();
```

### Estructura del paquete

```
packages/phylax-client/
├── src/
│   ├── PhylaxClient.ts        # Clase principal
│   ├── pkce.ts                # Generación de code_challenge S256
│   ├── token-storage.ts       # Almacenamiento seguro (sessionStorage por defecto)
│   ├── session-manager.ts     # Gestión de sesión y renovación automática
│   ├── adapters/
│   │   ├── react/
│   │   │   ├── PhylaxProvider.tsx
│   │   │   ├── useSession.ts
│   │   │   └── usePhylax.ts
│   │   └── vue/
│   │       ├── PhylaxPlugin.ts
│   │       └── usePhylax.ts
│   └── management/
│       └── ManagementApiClient.ts  # Para el Management API desde frontend
├── package.json
└── tsconfig.json
```

### Consideraciones de seguridad

- PKCE S256 siempre activado — no hay opción de desactivarlo para clientes públicos
- Tokens almacenados en `sessionStorage` (no `localStorage`) por defecto
- `state` aleatorio en cada authorize request (anti-CSRF)
- Renovación de token silenciosa via hidden iframe o refresh_token

---

## SDK 2 — Java / Quarkus (`phylax-java-client`)

### Funcionalidades mínimas

```java
// Para microservicios que consumen el Management API
@ApplicationScoped
public class PhylaxManagementClient {

    @Inject
    @PhylaxConfig
    PhylaxClientConfig config;  // clientId, clientSecret, issuer

    // Client Credentials automático
    public CompletionStage<UserProfile> getUserProfile(UUID userUid) { ... }
    public CompletionStage<List<ActiveSession>> getUserSessions(UUID userUid) { ... }
    public CompletionStage<PolicyEvaluationResult> evaluatePolicy(
        PolicyEvaluationRequest request) { ... }
    public CompletionStage<Boolean> isTokenValid(String token) { ... }
}

// Quarkus extension para proteger endpoints con Phylax JWT
quarkus.security.oidc.auth-server-url=https://auth.example.com/openid/my-tenant
quarkus.security.oidc.client-id=my-resource-server
```

### Estructura del paquete

```
phylax-java-client/
├── src/main/java/net/civeira/phylax/client/
│   ├── PhylaxClientConfig.java         # @ConfigMapping
│   ├── management/
│   │   ├── ManagementApiClient.java    # Interface del cliente
│   │   └── ManagementApiClientImpl.java # Impl con Vert.x WebClient
│   ├── auth/
│   │   ├── ClientCredentialsProvider.java  # Obtiene y cachea tokens M2M
│   │   └── JwksVerifier.java               # Verificación de JWT con JWKS caching
│   └── policy/
│       └── PhylaxPolicyEnforcer.java   # Evaluación ABAC
└── pom.xml
```

---

## SDK 3 — Generación automática desde OpenAPI

Una vez que la spec OpenAPI esté completa (02-06), generar SDKs automáticamente:

```bash
# En el CI/CD, tras cada release:
mvn quarkus:dev &
curl http://localhost:8080/q/openapi -o phylax-api-spec.yaml

# Generar cliente TypeScript
npx @openapitools/openapi-generator-cli generate \
  -i phylax-api-spec.yaml \
  -g typescript-fetch \
  -o packages/phylax-management-client

# Generar cliente Java
java -jar openapi-generator.jar generate \
  -i phylax-api-spec.yaml \
  -g java \
  --library=vertx \
  -o phylax-management-java-client
```

---

## Publicación

| SDK | Repositorio | Package registry |
|-----|-------------|-----------------|
| JS/TS | GitHub Packages | `npm install @phylax/client` |
| Java | Maven Central o GitHub Packages | `io.phylax:phylax-client` |
| TS Management (generado) | GitHub Packages | `npm install @phylax/management-client` |

---

## Criterios de aceptación

- [ ] `@phylax/client` npm package con `signIn()`, `signOut()`, `getSession()`, `getAccessToken()`
- [ ] PKCE S256 activado por defecto en el JS SDK
- [ ] Adaptadores React (`useSession`, `usePhylax`) y Vue (`usePhylax`)
- [ ] `phylax-java-client` Maven artifact con `ManagementApiClient`
- [ ] Client Credentials automático y cacheado en el Java SDK
- [ ] Generación automática de clients desde spec OpenAPI en CI/CD
- [ ] README de cada SDK con ejemplos de uso en 5 minutos
