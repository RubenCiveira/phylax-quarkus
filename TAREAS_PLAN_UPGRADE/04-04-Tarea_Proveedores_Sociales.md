# Tarea 04-04 — Proveedores de Login Social Adicionales

> **Fase:** 04 — Autenticación Avanzada
> **Prioridad:** P3
> **Esfuerzo estimado:** Medio (por proveedor)
> **Prerequisito:** Feature `delegated` existente (Google ya implementado)

---

## Descripción

Actualmente solo Google OAuth está implementado en el feature `delegated`.
Añadir nuevos proveedores implementando la interfaz `OAuthProviderAdapter`
(o equivalente) del dominio delegated.

---

## Arquitectura existente

El feature `delegated` en:
`src/main/java/net/civeira/phylax/features/oauth/delegated/`

Tiene:
- `domain/provider/` — interfaces del dominio
- `infrastructure/driven/` — adaptadores concretos (Google, etc.)

**Patrón a seguir:** cada nuevo proveedor es un adaptador en
`infrastructure/driven/` que implementa la interfaz de dominio.

---

## Proveedores a implementar

### Prioridad Alta

#### GitHub OAuth

```java
@ApplicationScoped
public class GitHubOAuthAdapter implements OAuthProviderAdapter {

    private static final String AUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String USER_API  = "https://api.github.com/user";

    @Override
    public String getProviderId() { return "github"; }

    @Override
    public String buildAuthorizationUrl(String clientId, String redirectUri,
                                         String state, String scope) {
        return AUTH_URL + "?client_id=" + clientId
            + "&redirect_uri=" + redirectUri
            + "&state=" + state
            + "&scope=user:email";
    }

    @Override
    public ExternalUserProfile exchangeCodeForProfile(String code, String redirectUri,
                                                       TenantProviderConfig config) {
        // 1. POST a TOKEN_URL para obtener access_token
        // 2. GET a USER_API con el access_token
        // 3. GET a https://api.github.com/user/emails si email es null (privado)
        // 4. Mapear a ExternalUserProfile
    }
}
```

#### Microsoft / Azure AD (OIDC estándar)

Microsoft expone un endpoint OIDC estándar, por lo que puede implementarse
como un proveedor OIDC genérico:

```java
@ApplicationScoped
public class MicrosoftOAuthAdapter implements OAuthProviderAdapter {
    // Usa el endpoint discovery:
    // https://login.microsoftonline.com/{tenant}/.well-known/openid-configuration
    // Soporta multi-tenant Azure AD con tenant_id configurable
}
```

### Prioridad Media

#### Apple Sign In

Apple requiere JWT firmado con ES256 como `client_secret`. Es el más complejo
de implementar entre los proveedores comunes.

```java
@ApplicationScoped
public class AppleSignInAdapter implements OAuthProviderAdapter {
    // POST https://appleid.apple.com/auth/token
    // client_secret = JWT firmado con la clave privada de Apple Developer
    // Retorna id_token con el email en lugar de un user API
}
```

#### SAML 2.0 genérico

Para integraciones enterprise (Okta, Azure AD via SAML, etc.):

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.opensaml</groupId>
    <artifactId>opensaml-saml-impl</artifactId>
    <version>4.3.0</version>
</dependency>
```

---

## Configuración por tenant

Cada proveedor necesita credenciales configuradas por tenant:

```sql
-- changeset phylax-dev:add-provider-config-json
ALTER TABLE access_delegated_provider
  ADD COLUMN provider_type VARCHAR(20) NOT NULL DEFAULT 'google',
  ADD COLUMN config_json   TEXT        NULL COMMENT 'JSON con client_id, client_secret, etc.';
```

**Estructura `config_json` por proveedor:**

```json
// GitHub
{ "client_id": "...", "client_secret": "..." }

// Microsoft
{ "client_id": "...", "client_secret": "...", "tenant_id": "common" }

// Apple
{ "team_id": "...", "key_id": "...", "private_key_p8": "..." }
```

---

## Assets de iconos sociales

Los iconos ya existen en `META-INF/oauth/assets/socials/`:
- `github.png` ✓ (ya existe)
- `outlook.png` ✓ (para Microsoft)
- `apple.png` ✓
- `saml.png` ✓

Solo hay que habilitar su uso en los templates de login.

---

## Tests de integración

Para cada proveedor, mockear el endpoint de token y user API:

```java
@QuarkusTest
class GitHubOAuthFlowTest {
    // Usar WireMock para simular GitHub OAuth endpoints
    @Test void delegatedLogin_withGitHub_createsSession() { ... }
    @Test void delegatedLogin_withGitHub_linksExistingUser() { ... }
}
```

---

## Criterios de aceptación

- [ ] `GitHubOAuthAdapter` implementado y configurado por tenant
- [ ] `MicrosoftOAuthAdapter` implementado (multi-tenant Azure)
- [ ] `AppleSignInAdapter` implementado con JWT firmado
- [ ] `SamlProviderAdapter` implementado (básico: SSO iniciado por SP)
- [ ] Iconos sociales visibles en el template de login según config del tenant
- [ ] Tests de integración con mocks para GitHub y Microsoft
- [ ] Config JSON por tenant en `access_delegated_provider`
