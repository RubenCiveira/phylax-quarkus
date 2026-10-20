# 4.8 — Claims solicitados en authorize

- **Fase:** 4 (solo con requisito) · **Estado:** pendiente.
- **Implementación:** validar parámetro `claims` para ID token/UserInfo, distinguir esenciales de opcionales, respetar scopes/consentimiento y evitar ampliación en refresh. Si no se aprueba, conservar `claims_parameter_supported: false` ([1.1](issue-1.1-discovery-honesto.md)).
- **Cierre:** claim esencial no disponible falla; opcional ausente no amplía datos; request malformada rechazada y metadata veraz.
- **Plan PHP:** [061-claims-parameter.md](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/061-claims-parameter.md).
