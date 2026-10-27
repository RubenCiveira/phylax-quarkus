# 3.11 — UserInfo firmado

- **Fase:** 3 (solo con RP que lo requiera) · **Estado:** pendiente · **Referencia:** OIDC Core §5.3.2.
- **Implementación:** permitir configurar algoritmo de firma por cliente y responder JWT (`application/jwt`) con `iss`, `aud`, `sub` y claims concedidos; reutilizar el mismo filtro de [1.5](done/issue-1.5-userinfo-conforme.md), sin ampliar scopes.
- **Cierre:** RP autorizado verifica firma y claims; cliente no configurado sigue recibiendo JSON; discovery anuncia solo algoritmos operativos.
- **Plan PHP:** [060-userinfo-firmado.md](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/060-userinfo-firmado.md).
