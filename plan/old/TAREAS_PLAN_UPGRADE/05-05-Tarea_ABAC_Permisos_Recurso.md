# Tarea 05-05 — Permisos a nivel de recurso — ABAC

> **Fase:** 05 — API de Gestión BaaS
> **Prioridad:** P3
> **Esfuerzo estimado:** Alto
> **Prerequisito:** 05-04 (Organizaciones — para usar `org` como sujeto)

---

## Descripción

Extiende el RBAC (roles planos) existente con ABAC (Attribute-Based Access Control),
permitiendo permisos con granularidad de recurso individual. Ejemplo: el usuario A
puede editar el documento D pero no borrarlo.

El motor es compatible con `allow`/`deny` explícito, con `deny` teniendo precedencia.

---

## Pasos de implementación

### 1. Tabla `access_resource_policy`

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-resource-policy-table
CREATE TABLE access_resource_policy (
  uid           VARCHAR(36)  NOT NULL,
  tenant_id     VARCHAR(36)  NOT NULL,
  subject_uid   VARCHAR(36)  NOT NULL COMMENT 'user_uid, org_uid o role_uid',
  subject_type  VARCHAR(20)  NOT NULL COMMENT 'user, org, role',
  resource_type VARCHAR(100) NOT NULL COMMENT 'document, project, etc.',
  resource_uid  VARCHAR(36)  NOT NULL,
  action        VARCHAR(100) NOT NULL COMMENT 'read, write, delete, etc.',
  effect        VARCHAR(10)  NOT NULL DEFAULT 'allow' COMMENT 'allow o deny',
  created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT PK_ACCESS_RESOURCE_POLICY PRIMARY KEY (uid),
  INDEX idx_policy_subject (tenant_id, subject_uid, subject_type),
  INDEX idx_policy_resource (tenant_id, resource_type, resource_uid)
);
```

### 2. Value objects del dominio ABAC

```java
public record ResourcePolicy(
    UUID uid,
    String tenantId,
    SubjectRef subject,
    ResourceRef resource,
    String action,
    PolicyEffect effect
) {}

public record SubjectRef(UUID subjectUid, SubjectType type) {
    public enum SubjectType { USER, ORG, ROLE }
}

public record ResourceRef(String resourceType, UUID resourceUid) {}

public enum PolicyEffect { ALLOW, DENY }

public record PolicyEvaluationRequest(
    SubjectRef subject,
    ResourceRef resource,
    String action,
    String tenantId
) {}

public record PolicyEvaluationResult(boolean allowed, String reason) {}
```

### 3. Motor de evaluación — `PolicyEvaluationEngine`

```java
@ApplicationScoped
public class PolicyEvaluationEngine {

    @Inject
    ResourcePolicyGateway policyGateway;

    @Inject
    OrganizationGateway orgGateway;

    public PolicyEvaluationResult evaluate(PolicyEvaluationRequest request) {
        // 1. Recopilar todos los sujetos: user + sus org_uids + sus role_uids
        Set<SubjectRef> subjects = resolveSubjects(request.subject(), request.tenantId());

        // 2. Buscar todas las políticas aplicables
        List<ResourcePolicy> policies = policyGateway
            .findApplicable(subjects, request.resource(), request.action(), request.tenantId());

        // 3. DENY explícito tiene precedencia sobre cualquier ALLOW
        boolean anyDeny  = policies.stream().anyMatch(p -> p.effect() == PolicyEffect.DENY);
        boolean anyAllow = policies.stream().anyMatch(p -> p.effect() == PolicyEffect.ALLOW);

        if (anyDeny) {
            return new PolicyEvaluationResult(false, "Explicit DENY policy applies");
        }
        if (anyAllow) {
            return new PolicyEvaluationResult(true, "ALLOW policy found");
        }
        return new PolicyEvaluationResult(false, "No applicable ALLOW policy");
    }

    private Set<SubjectRef> resolveSubjects(SubjectRef user, String tenantId) {
        Set<SubjectRef> subjects = new HashSet<>();
        subjects.add(user);
        // Añadir orgs del usuario
        orgGateway.findByMember(user.subjectUid(), tenantId)
            .forEach(org -> subjects.add(new SubjectRef(org.uid(), SubjectType.ORG)));
        // Añadir roles del usuario (del RBAC existente)
        // ...
        return subjects;
    }
}
```

### 4. Endpoints de gestión de políticas

```
POST   /api/access/policies              # Crear política
GET    /api/access/policies              # Listar (con filtros)
DELETE /api/access/policies/{uid}        # Eliminar política

POST   /api/access/policies/evaluate     # Evaluar permiso
```

**Endpoint de evaluación:**

```java
@POST
@Path("/api/access/policies/evaluate")
@Operation(summary = "Evaluate ABAC policy for a subject-resource-action triple")
public PolicyEvaluationResult evaluate(PolicyEvaluationRequest request) {
    return policyEvaluationEngine.evaluate(request);
}
```

**Request body:**
```json
{
  "subject": { "uid": "user-uuid", "type": "user" },
  "resource": { "type": "document", "uid": "doc-uuid" },
  "action": "edit",
  "tenant_id": "tenant-uuid"
}
```

**Response:**
```json
{ "allowed": true, "reason": "ALLOW policy found" }
```

### 5. Integración con el audit log

Cada evaluación de política debe registrarse en el audit trail:

```java
// Tras evaluate()
auditGateway.log(AuditEntry.policyEvaluation(
    request.subject(), request.resource(), request.action(),
    result.allowed(), tenantId));
```

### 6. Tests de integración

- ALLOW explícita → `allowed: true` ✓
- DENY explícita → `allowed: false` aunque haya ALLOW ✓
- Sin políticas → `allowed: false` (default-deny) ✓
- Política a nivel de org → hereda al miembro ✓
- Política a nivel de rol → hereda al usuario con ese rol ✓

---

## Criterios de aceptación

- [ ] Tabla `access_resource_policy` con migración
- [ ] Motor `PolicyEvaluationEngine` con regla DENY-wins
- [ ] Resolución de sujetos transitivos (user → orgs → roles)
- [ ] `POST /api/access/policies/evaluate` funciona con los 3 tipos de sujeto
- [ ] CRUD de políticas vía API
- [ ] Evaluaciones registradas en audit log
- [ ] 5 tests de integración
