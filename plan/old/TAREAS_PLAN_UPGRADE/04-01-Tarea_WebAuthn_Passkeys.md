# Tarea 04-01 — WebAuthn / Passkeys (FIDO2)

> **Fase:** 04 — Autenticación Avanzada
> **Estándar:** FIDO2 / W3C WebAuthn Level 2
> **Prioridad:** P2
> **Esfuerzo estimado:** Alto
> **Prerequisito:** Fase 02 completada

---

## Descripción

WebAuthn es el estándar de autenticación sin contraseña más seguro disponible.
Elimina ataques de phishing y credential stuffing. Los navegadores modernos
lo soportan nativamente. Los Passkeys son la implementación sincronizada
en la nube de WebAuthn (Apple, Google, Microsoft).

---

## Pasos de implementación

### 1. Dependencia Java para WebAuthn

```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.yubico</groupId>
    <artifactId>webauthn-server-core</artifactId>
    <version>2.5.0</version>
</dependency>
```

La librería `webauthn-server-core` de Yubico es la más robusta y mantenida
para Java. Soporta CBOR, attestation y assertion completos.

### 2. Tabla `access_user_webauthn_credential`

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-webauthn-credentials-table
CREATE TABLE access_user_webauthn_credential (
  uid           VARCHAR(36)   NOT NULL,
  user_uid      VARCHAR(36)   NOT NULL,
  tenant_id     VARCHAR(36)   NOT NULL,
  credential_id VARCHAR(512)  NOT NULL COMMENT 'Base64url encoded credential ID',
  public_key    TEXT          NOT NULL COMMENT 'CBOR encoded public key (Base64url)',
  sign_count    BIGINT        NOT NULL DEFAULT 0,
  aaguid        VARCHAR(36)   NULL,
  device_name   VARCHAR(255)  NULL,
  created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_used_at  TIMESTAMP     NULL,
  CONSTRAINT PK_ACCESS_USER_WEBAUTHN_CREDENTIAL PRIMARY KEY (uid),
  UNIQUE KEY uq_credential_id (credential_id),
  INDEX idx_webauthn_user (user_uid, tenant_id)
);
```

### 3. Flujo de registro

**Endpoints:**
```
POST /openid/{tenant}/webauthn/register/begin
  → Devuelve PublicKeyCredentialCreationOptions (JSON)
  → Almacena el challenge en sesión temporal

POST /openid/{tenant}/webauthn/register/finish
  Body: { credential: PublicKeyCredentialJSON }
  → Verifica attestation
  → Almacena en access_user_webauthn_credential
```

**Implementación con Yubico:**
```java
@ApplicationScoped
public class WebAuthnRegistrationService {

    private final RelyingParty relyingParty;

    public PublicKeyCredentialCreationOptions beginRegistration(User user, String tenant) {
        StartRegistrationOptions options = StartRegistrationOptions.builder()
            .user(UserIdentity.builder()
                .name(user.email())
                .displayName(user.displayName())
                .id(ByteArray.fromHex(user.uid().toString().replace("-", "")))
                .build())
            .build();
        return relyingParty.startRegistration(options);
        // Almacenar el PublicKeyCredentialCreationOptions en _oauth_temporal_codes
    }

    public void finishRegistration(String userUid, PublicKeyCredentialJSON credential) {
        FinishRegistrationOptions options = FinishRegistrationOptions.builder()
            .request(storedRequest)
            .response(PublicKeyCredential.parseRegistrationResponseJson(credential))
            .build();
        RegistrationResult result = relyingParty.finishRegistration(options);
        // Persistir result.getKeyId(), result.getPublicKeyCose(), result.getSignatureCount()
    }
}
```

### 4. Flujo de autenticación

**Endpoints:**
```
POST /openid/{tenant}/webauthn/authenticate/begin
  → Devuelve PublicKeyCredentialRequestOptions
  → Almacena el challenge

POST /openid/{tenant}/webauthn/authenticate/finish
  Body: { credential: PublicKeyCredentialJSON }
  → Verifica assertion
  → Actualiza sign_count
  → Crea sesión OIDC (igual que login con password)
```

### 5. Integrar como paso en el authorize flow

Añadir paso `webauthn` en la máquina de estados del authorize,
configurable por tenant:

```yaml
# En TenantConfig
authentication_steps:
  - identifier    # email/username
  - webauthn      # si tiene credenciales WebAuthn registradas
  - mfa           # fallback a TOTP si no hay WebAuthn
```

### 6. Frontend — JavaScript WebAuthn API

Añadir en los assets OIDC (`META-INF/oauth/assets/webauthn.js`):

```javascript
// Registro
async function registerPasskey(creationOptions) {
    const credential = await navigator.credentials.create({ publicKey: creationOptions });
    // Enviar al endpoint finish
}

// Autenticación
async function authenticateWithPasskey(requestOptions) {
    const assertion = await navigator.credentials.get({ publicKey: requestOptions });
    // Enviar al endpoint finish
}
```

### 7. Tests de integración

Los tests de WebAuthn requieren un authenticator virtual. Usar
`com.yubico:webauthn-server-attestation` con un soft authenticator para tests.

---

## Criterios de aceptación

- [ ] `POST /openid/{tenant}/webauthn/register/begin` devuelve `PublicKeyCredentialCreationOptions`
- [ ] `POST /openid/{tenant}/webauthn/register/finish` persiste la credencial
- [ ] `POST /openid/{tenant}/webauthn/authenticate/begin` devuelve challenge
- [ ] `POST /openid/{tenant}/webauthn/authenticate/finish` verifica y crea sesión OIDC
- [ ] `sign_count` se actualiza en cada uso (anti-clonación)
- [ ] El paso `webauthn` es configurable por tenant
- [ ] JS helper en `META-INF/oauth/assets/webauthn.js`
