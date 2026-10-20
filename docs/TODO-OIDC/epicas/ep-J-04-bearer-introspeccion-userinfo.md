# J-04 — Bearer, introspección y UserInfo

**Prioridad:** crítica · **Tareas:** [0.8](../issue-0.8-verificacion-bearer-apis.md), [0.15](../issue-0.15-audiencia-tenant-revocacion-bearer.md), [1.5](../issue-1.5-userinfo-conforme.md), [1.9](../issue-1.9-introspeccion-todos-grants.md). **Depende de:** tipo/claims J-03 y audiencia/tenant J-02.

## Implementación

Crear política común de access token en el verificador Java, UserInfo e introspección: firma/exp, `iss` canónico de ruta, tenant, `aud` de consumidor, tipo access y JTI no revocado. Si hay caché, revalidar tenant y revocación por petición. Introspección no debe depender solo de la sesión web; UserInfo GET/POST exige `openid`, devuelve `sub` estable y claims reales permitidos por scope (email no es username).

**Aceptación:** ID/refresh/challenge/MFA, bearer de tenant/audiencia ajenos o revocado fallan en API/UserInfo/introspección; revocar tras la primera petición también falla con caché caliente; GET/POST UserInfo equivalentes y 401 con `WWW-Authenticate`.

**Referencia PHP:** EP-06 (026, 028, 035). El middleware PHP también está pendiente; su contrato propuesto se adapta a `TokenJwtCallerPrincipalFactory` y casos de uso Java.
