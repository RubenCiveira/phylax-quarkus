# Tarea 07-03 — Gestión de consentimientos (GDPR Art. 7) — Página unificada

> **Fase:** 07 — Cumplimiento GDPR
> **Artículo GDPR:** 7 (consentimiento), 13 (transparencia)
> **Prioridad:** P3
> **Esfuerzo estimado:** Medio
> **Prerequisito:** 02-03 (BC `userconsentedscopes` creado), `UserAcceptedTermnsOfUse` existente

---

## Descripción

Página HTML unificada donde el usuario puede ver y gestionar **todos sus
consentimientos** en un solo lugar:

1. **Términos y condiciones** aceptados (BC `UserAcceptedTermnsOfUse` — ya existe)
2. **Scopes OAuth concedidos** por cliente (BC `UserConsentedScope` — tarea 02-03)

El GDPR exige que el usuario pueda retirar el consentimiento con la misma
facilidad con que lo otorgó (Art. 7.3). Esta página es la interfaz para ello.

**Esta tarea no crea nuevos BCs.** Orquesta los dos ya existentes en un
driver HTML dentro de `userconsentedscopes/infrastructure/driver/html/`.

---

## Ruta y controller

```
GET  /account/{tenant}/consents              → pantalla principal
POST /account/{tenant}/consents/revoke-client → revocar todos los scopes de un cliente
```

El controller vive en:
`features/access/userconsentedscopes/infrastructure/driver/html/ConsentManagementController.java`

> **Separación importante:** esta ruta está **fuera del flujo de autorización**.
> El formulario de consentimiento en-flujo (`/openid/{tenant}/authorize` → paso
> `scopes-consent`) sigue viviendo en `features/oauth/authentication/`.
> Son dos drivers distintos para dos momentos distintos.

---

## Implementación del controller

```java
@Path("/account/{tenant}/consents")
@ApplicationScoped
public class ConsentManagementController {

    @Inject
    ListConsentedScopesUseCase listScopes;

    @Inject
    RevokeScopeConsentUseCase revokeScopes;

    @Inject
    UserAcceptedTermnsOfUseReadRepositoryGateway termsGateway;

    @Inject
    @Location("consent-management")
    Template consentManagementTemplate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showConsents(
        @PathParam("tenant") String tenant,
        @Context SecurityContext security
    ) {
        UUID userUid = extractUserUid(security);

        // Scopes agrupados por cliente
        Map<ClientRef, Set<ScopeGrantDto>> scopesByClient =
            listScopes.execute(userUid, tenant)
                .stream()
                .collect(groupingBy(
                    c -> new ClientRef(c.clientUid(), c.clientName()),
                    mapping(c -> new ScopeGrantDto(c.scope(), c.grantDate()), toSet())
                ));

        // Términos aceptados
        List<UserAcceptedTermnsOfUse> acceptedTerms =
            termsGateway.findByUser(userUid, tenant);

        return consentManagementTemplate
            .data("scopesByClient", scopesByClient)
            .data("acceptedTerms", acceptedTerms)
            .data("tenant", tenant);
    }

    @POST
    @Path("/revoke-client")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response revokeClient(
        @PathParam("tenant") String tenant,
        @FormParam("client_uid") UUID clientUid,
        @Context SecurityContext security
    ) {
        UUID userUid = extractUserUid(security);
        revokeScopes.executeForClient(userUid, clientUid, tenant);
        // Redirect-after-POST
        return Response.seeOther(URI.create("/account/" + tenant + "/consents")).build();
    }
}
```

---

## Template Qute — `consent-management.html`

```html
<!DOCTYPE html>
<html>
<head><title>Mis consentimientos</title></head>
<body>
  <h1>Mis consentimientos</h1>

  <!-- SECCIÓN 1: Términos y condiciones -->
  <section>
    <h2>Términos y condiciones aceptados</h2>
    {#if acceptedTerms.isEmpty}
      <p>No has aceptado ningún documento todavía.</p>
    {#else}
      <ul>
        {#for term in acceptedTerms}
          <li>
            <strong>{term.conditionsTitle}</strong>
            — aceptado el {term.acceptDate.format("dd/MM/yyyy")}
          </li>
        {/for}
      </ul>
    {/if}
  </section>

  <!-- SECCIÓN 2: Aplicaciones autorizadas -->
  <section>
    <h2>Aplicaciones autorizadas</h2>
    {#if scopesByClient.isEmpty}
      <p>No has autorizado ninguna aplicación todavía.</p>
    {#else}
      {#for entry in scopesByClient.entrySet}
        <div class="client-consent">
          <h3>{entry.key.name}</h3>
          <ul>
            {#for grant in entry.value}
              <li>
                <code>{grant.scope}</code>
                <span class="date">desde {grant.grantDate.format("dd/MM/yyyy")}</span>
              </li>
            {/for}
          </ul>
          <form method="POST" action="/account/{tenant}/consents/revoke-client">
            <input type="hidden" name="client_uid" value="{entry.key.uid}">
            <button type="submit" onclick="return confirm('¿Revocar todo acceso a {entry.key.name}?')">
              Revocar todo acceso
            </button>
          </form>
        </div>
      {/for}
    {/if}
  </section>
</body>
</html>
```

---

## Nota sobre consentimientos GDPR adicionales (marketing, analytics…)

Si en el futuro se quiere gestionar también consentimientos de propósito
GDPR (marketing, analytics, etc.), hay dos opciones:

**Opción A — BC separado `UserGdprConsent`** (análogo a `UserConsentedScope`):
- Misma estructura pero con campo `purpose` en lugar de `client`+`scope`
- Se añade una tercera sección a esta misma página
- Más limpio si los propósitos GDPR tienen lifecycle propio (versiones de texto, etc.)

**Opción B — Extender `UserConsentedScope`** con un tipo enum `consentType`:
- No recomendado: mezcla semántica OAuth (client/scope) con semántica GDPR (purpose)

La recomendación es **Opción A** cuando llegue el momento. Esta tarea deja
la puerta abierta añadiendo una tercera sección en el template.

---

## Acceso a la página

La página requiere que el usuario esté autenticado. Añadir al link de "Mi cuenta"
en la UI del authorize flow tras el login exitoso.

Opcionalmente, enlazar desde la pantalla de logout: "Antes de irte, puedes
revisar qué aplicaciones tienen acceso a tu cuenta →".

---

## Tests de integración

- `GET /account/{tenant}/consents` con usuario autenticado → renderiza secciones ✓
- Página muestra scopes agrupados por cliente ✓
- Página muestra términos aceptados ✓
- `POST /revoke-client` → scopes revocados, redirect a la misma página ✓
- Tras revocar, el cliente ya no aparece en la lista ✓
- Sin autenticación → redirect al login ✓

---

## Criterios de aceptación

- [ ] `GET /account/{tenant}/consents` renderiza las dos secciones con datos reales
- [ ] Scopes agrupados por cliente con nombre y fecha de concesión
- [ ] Términos aceptados con fecha de aceptación
- [ ] `POST /revoke-client` revoca todos los scopes del cliente (Redirect-after-POST)
- [ ] Tras revocar, el authorize flow volverá a pedir consent al siguiente login
- [ ] `UserConsentedScopeDeleteEvent` publicado al revocar (para webhooks y audit)
- [ ] Acceso protegido — requiere sesión autenticada del usuario
- [ ] 6 tests de integración
