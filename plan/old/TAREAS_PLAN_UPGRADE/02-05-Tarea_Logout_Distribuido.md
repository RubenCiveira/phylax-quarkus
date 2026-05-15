# Tarea 02-05 — Logout distribuido — Front-Channel y Back-Channel (OIDC Session Management)

> **Fase:** 02 — Cumplimiento OIDC Obligatorio
> **Spec:** OIDC Back-Channel Logout 1.0 / Front-Channel Logout 1.0
> **Prioridad:** P1
> **Esfuerzo estimado:** Medio
> **Prerequisito:** 02-04 (Token Revocation)

---

## Descripción

El logout actual limpia cookies localmente pero no notifica a los Relying Parties
(RPs) que tenían sesiones activas. Un servidor OIDC de producción debe propagar
el logout a todos los clientes que emitieron tokens en esa sesión.

---

## Pasos de implementación

### 1. Añadir `backchannel_logout_uri` y `frontchannel_logout_uri` al registro de clientes

**Migración Liquibase:**

```sql
-- liquibase formatted sql

-- changeset phylax-dev:add-logout-uris-to-client
ALTER TABLE access_relying_party
  ADD COLUMN backchannel_logout_uri   TEXT NULL,
  ADD COLUMN frontchannel_logout_uri  TEXT NULL,
  ADD COLUMN frontchannel_session_required TINYINT(1) DEFAULT 0;
```

Actualizar el dominio de `RelyingParty` para incluir estos campos.

### 2. Back-Channel Logout

**Qué hacer al hacer logout:**

1. Obtener todos los RPs con sesión activa para ese usuario/sesión
2. Para cada RP con `backchannel_logout_uri` configurado:
   a. Construir un `logout_token` JWT firmado:
      ```json
      {
        "iss": "https://auth.example.com/openid/tenant",
        "sub": "user-uuid",
        "aud": ["client-id"],
        "iat": 1234567890,
        "jti": "unique-logout-jti",
        "events": { "http://schemas.openid.net/event/backchannel-logout": {} },
        "sid": "session-id"
      }
      ```
   b. Enviar `POST {backchannel_logout_uri}` con `logout_token=<jwt>` (form-encoded)
   c. Hacer el envío de forma asíncrona y no bloquear el logout del usuario

**Implementación asíncrona con Quarkus (Mutiny + Vert.x HTTP Client):**

```java
@ApplicationScoped
public class BackChannelLogoutDispatcher {

    @Inject
    Vertx vertx;

    public void notifyRelyingParty(String backchannelUri, String logoutToken) {
        WebClient client = WebClient.create(vertx);
        client.postAbs(backchannelUri)
            .sendForm(MultiMap.caseInsensitiveMultiMap()
                .add("logout_token", logoutToken))
            .subscribe().with(
                resp -> log.infof("Back-channel logout OK: %d", resp.statusCode()),
                err  -> log.warnf("Back-channel logout failed: %s", err.getMessage())
            );
    }
}
```

### 3. Front-Channel Logout

Al renderizar la página de logout:
- Consultar los RPs con sesión activa que tengan `frontchannel_logout_uri`
- Incluir en el HTML un iframe por cada RP:

```html
<!-- En la plantilla de logout (Qute template) -->
{#for rp in relyingParties}
  <iframe src="{rp.frontchannelLogoutUri}?iss={issuer}&sid={sessionId}"
          style="display:none" width="0" height="0"></iframe>
{/for}
```

### 4. Anunciar en `.well-known`

```json
{
  "backchannel_logout_supported": true,
  "backchannel_logout_session_supported": true,
  "frontchannel_logout_supported": true,
  "frontchannel_logout_session_supported": true
}
```

### 5. Tests de integración

- Logout con RP registrado con `backchannel_logout_uri` → se invoca el endpoint del RP
- Logout sin RPs registrados → 200 sin errores
- Back-channel URI no disponible → logout se completa igualmente (fallo silencioso)

---

## Criterios de aceptación

- [ ] `backchannel_logout_uri` y `frontchannel_logout_uri` en el modelo de `RelyingParty`
- [ ] `BackChannelLogoutDispatcher` envía `logout_token` a cada RP asíncronamente
- [ ] La página de logout incluye iframes para front-channel
- [ ] Fallo de back-channel no bloquea el logout del usuario
- [ ] `.well-known` anuncia soporte de back/front-channel logout
- [ ] Tests de integración con mock de RP endpoint
