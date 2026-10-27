# 4.8 — Claims solicitados en authorize

- **Fase:** 4 · **Estado:** implementada y verificada (27-10-2026), [J-13](../epicas/done/ep-J-13-solicitudes-claims-avanzados.md).
- **Implementación:** `ClaimsRequest` valida el parámetro (objeto JSON con `id_token`/`userinfo`, `essential` booleano, `values` array); malformado, con una claim esencial que el servidor no emite o con una esencial que los scopes pedidos no liberan (p. ej. `email` sin scope `email`) → `invalid_request`; las voluntarias fuera de los scopes se omiten. Tras el login, una esencial que **el usuario no tiene** (p. ej. `given_name` sin perfil) termina la solicitud con `access_denied` antes de emitir code o tokens (`OidcResponseBuilder`). Viaja en el code; el ID token del endpoint de token añade las claims pedidas que **ya liberan los scopes concedidos** (`UserClaimsGateway`, compartido con UserInfo): el parámetro nunca amplía el consentimiento, y los ID tokens de refresh no las llevan. `claims_parameter_supported: true`.
- **Pendiente real:** el miembro `userinfo` solo se valida (UserInfo ya devuelve lo que cubren los scopes); `value`/`values` no filtran.
- **Pruebas:** `ClaimsParameterFlowTest` (incluye esencial fuera de scope rechazada y cubierta aceptada).
- **Límite:** las esenciales se comprueban contra los scopes pedidos (authorize) y contra los datos del usuario con los scopes concedidos (tras el consentimiento); `value`/`values` no filtran.
- **Plan PHP:** [061](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/061-claims-parameter.md).
