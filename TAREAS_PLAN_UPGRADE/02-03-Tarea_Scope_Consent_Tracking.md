# Tarea 02-03 — Scope Consent Tracking (completar implementación)

> **Fase:** 02 — Cumplimiento OIDC Obligatorio
> **Prioridad:** P0
> **Esfuerzo estimado:** Medio
> **Prerequisito:** Ninguno (independiente)

---

## Descripción

El módulo de scopes actualmente es un stub que siempre devuelve `[]` (ningún
scope consentido) y no persiste las decisiones del usuario. Esto hace que
el formulario de consentimiento aparezca en cada login, rompiendo la experiencia
de usuario en aplicaciones que usan OIDC.

---

## Decisión arquitectónica

**La persistencia del consentimiento NO vive en `features/oauth/`.**

El consentimiento es un dato del usuario (ownership de Access), no del protocolo
(ownership de OIDC). Siguiendo el precedente exacto de `UserAcceptedTermnsOfUse`,
se crea un bounded context propio en `features/access/userconsentedscopes/`.

El módulo `features/oauth/oidc/scopes/` pasa a ser una **capa anti-corrupción**
que delega en los use cases del BC Access. El protocolo OIDC no posee ni la tabla
ni el modelo — solo consulta y registra decisiones a través del ACL adapter.

```
features/access/userconsentedscopes/   ← modelo, persistencia, CRUD, eventos
features/oauth/oidc/scopes/            ← protocolo, delega al BC anterior via ACL
```

---

## Paso 1 — Bounded context `features/access/userconsentedscopes/`

### Modelo de dominio

El agregado es análogo a `UserAcceptedTermnsOfUse`:

| Campo | Tipo (VO) | Notas |
|-------|-----------|-------|
| `uid` | `UidVO` | PK UUID |
| `user` | `UserVO` | FK → `access_user` |
| `client` | `ClientVO` | FK → `access_relying_party` |
| `scope` | `ScopeVO` | String libre (`"openid"`, `"email"`…) |
| `grantDate` | `GrantDateVO` | `Instant` — cuándo se concedió |
| `version` | `VersionVO` | Optimistic locking |

**Una fila por scope concedido.** Esto permite:
- Conceder `openid email` en el primer login y `profile` en el segundo sin repedir los primeros
- Revocar un scope individual
- Query "¿tiene el usuario X concedido el scope Y para el cliente Z?" → `EXISTS` sobre la PK natural

**No hay `revokedAt` nullable.** La semántica es: existe la fila = concedido, no existe = revocado.
El `delete` del agregado dispara `UserConsentedScopeDeleteEvent`.

### Estructura de paquetes

```
features/access/userconsentedscopes/
├── domain/
│   ├── UserConsentedScope.java
│   ├── UserConsentedScopeChangeSet.java
│   ├── UserConsentedScopeRef.java
│   ├── UserConsentedScopeReference.java
│   ├── event/
│   │   ├── UserConsentedScopeEvent.java
│   │   ├── UserConsentedScopeCreateEvent.java
│   │   └── UserConsentedScopeDeleteEvent.java
│   ├── gateway/
│   │   ├── UserConsentedScopeFilter.java
│   │   ├── UserConsentedScopeCursor.java
│   │   ├── UserConsentedScopeReadRepositoryGateway.java
│   │   └── UserConsentedScopeWriteRepositoryGateway.java
│   └── valueobject/
│       ├── UidVO.java / UidValueHolder.java
│       ├── UserVO.java / UserValueHolder.java
│       ├── ClientVO.java / ClientValueHolder.java
│       ├── ScopeVO.java / ScopeValueHolder.java
│       └── GrantDateVO.java / GrantDateValueHolder.java
├── application/
│   └── usecase/
│       ├── create/   — GrantScopeConsentUseCase
│       ├── delete/   — RevokeScopeConsentUseCase
│       ├── list/     — ListConsentedScopesUseCase
│       └── retrieve/ — CheckScopeConsentUseCase
└── infrastructure/
    ├── driven/
    │   ├── UserConsentedScopeReadGatewayAdapter.java
    │   └── UserConsentedScopeWriteGatewayAdapter.java
    ├── event/
    │   └── UserConsentedScopeEventDispatcher.java
    ├── repository/
    │   ├── UserConsentedScopeRepository.java
    │   └── UserConsentedScopeSlider.java
    └── driver/
        └── html/
            └── ConsentManagementController.java   ← ver Paso 3
```

### Migración Liquibase

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-user-consented-scope-table
CREATE TABLE access_user_consented_scope (
  uid        VARCHAR(36)  NOT NULL,
  user_uid   VARCHAR(36)  NOT NULL,
  client_uid VARCHAR(36)  NOT NULL,
  scope      VARCHAR(100) NOT NULL,
  grant_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  version    INT          NOT NULL DEFAULT 0,
  CONSTRAINT PK_ACCESS_USER_CONSENTED_SCOPE PRIMARY KEY (uid),
  UNIQUE KEY uq_user_client_scope (user_uid, client_uid, scope),
  INDEX idx_ucs_user_client (user_uid, client_uid)
);
```

La clave natural `(user_uid, client_uid, scope)` es única — garantiza que no
existan duplicados sin lógica adicional en el use case.

---

## Paso 2 — ACL adapter en `features/oauth/oidc/scopes/`

El gateway de OIDC Scopes pasa a ser un adaptador anti-corrupción que delega
en los use cases del BC Access. El módulo de protocolo no accede a la DB directamente.

```java
// features/oauth/oidc/scopes/infrastructure/driven/ScopesConsentAclAdapter.java
@ApplicationScoped
public class ScopesConsentAclAdapter implements ScopesConsentGateway {

    @Inject
    CheckScopeConsentUseCase checkConsent;

    @Inject
    GrantScopeConsentUseCase grantConsent;

    @Inject
    RevokeScopeConsentUseCase revokeConsent;

    @Override
    public Set<String> findGrantedScopes(UUID userUid, UUID clientUid, String tenantId) {
        return checkConsent.execute(userUid, clientUid, tenantId);
    }

    @Override
    public void grant(UUID userUid, UUID clientUid, String tenantId, String scope) {
        grantConsent.execute(userUid, clientUid, tenantId, scope);
    }

    @Override
    public void revokeAll(UUID userUid, UUID clientUid, String tenantId) {
        revokeConsent.executeForClient(userUid, clientUid, tenantId);
    }
}
```

---

## Paso 3 — Lógica en el authorize flow (solo el formulario en-flujo)

**El formulario de consentimiento durante el authorize flow se queda en
`features/oauth/authentication/` — es parte del wizard OIDC, no de la página
de gestión.**

Modificar el paso `scopes-consent` del authorize:

```
ANTES: Siempre mostrar el formulario

DESPUÉS:
  pendingScopes = checkConsent.execute(user, client, requestedScopes) → diferencia
  if (pendingScopes.isEmpty()) → saltar al siguiente paso sin mostrar formulario
  else → mostrar formulario solo con los scopes pendientes
```

Al submit del formulario:
```
Para cada scope aprobado: grantConsent.execute(user, client, tenantId, scope)
```

---

## Paso 4 — Página HTML de gestión (`/account/{tenant}/consents`)

Ver detalles completos en **tarea 07-03** (Gestión de consentimientos), que
unifica esta página con la gestión de términos de uso.

Resumen de responsabilidades del `ConsentManagementController`:
- Carga `UserConsentedScopeReadRepositoryGateway` → agrupa por `clientUid`
- Carga `UserAcceptedTermnsOfUseReadRepositoryGateway` → sección de T&C
- Botón "Revocar todo acceso" de un cliente → `revokeConsent.executeForClient()`

---

## Tests de integración

- Primera vez: formulario de consent se muestra con todos los scopes ✓
- Segunda vez mismo cliente/scopes: formulario NO se muestra ✓
- Scopes adicionales: formulario se muestra solo con los nuevos ✓
- `GET /account/{tenant}/consents`: muestra los scopes concedidos agrupados por cliente ✓
- Revocar cliente desde la página de gestión: formulario vuelve a aparecer ✓

---

## Criterios de aceptación

- [ ] BC `features/access/userconsentedscopes/` creado con el patrón estándar
- [ ] Tabla `access_user_consented_scope` con migración Liquibase
- [ ] `ScopesConsentAclAdapter` en OIDC delega a use cases de Access
- [ ] Formulario en authorize flow solo muestra los scopes NO concedidos previamente
- [ ] `GET /account/{tenant}/consents` renderiza scopes concedidos + T&C aceptados
- [ ] Revocación desde la página de gestión funciona
- [ ] `UserConsentedScopeCreateEvent` y `UserConsentedScopeDeleteEvent` publicados
- [ ] 5 tests de integración
