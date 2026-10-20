# 2.9 — Migración de secretos y cifrado en reposo

- **Fase:** 2 (adelantar si se habilita DCR) · **Esfuerzo:** L · **Estado:** pendiente.
- **Implementación:** decidir formato no reversible para secreto OAuth y transición de registros cifrados existentes sin interrumpir autenticación; mantener verificación temporalmente segura. Revisar origen/rotación de la clave AES y esquema de contraseñas de usuario, con migración o rehash documentado.
- **Cierre:** clientes antiguos y nuevos autentican durante despliegue, luego los secretos no son recuperables ni reaparecen en GET de DCR; pruebas de migración y contraseña errónea. No reutilizar el cifrado reversible como verificador.
- **Dependencias:** [0.9](issue-0.9-autenticacion-cliente-unificada.md), [2.8](issue-2.8-proteccion-secretos.md), [2.1](issue-2.1-registro-dinamico-clientes.md).
- **Plan PHP:** [EP-02 secretos y cifrado](/opt/homebrew/var/www/dev/LughAuth/lughauth-api/docs/TODD/epicas/EP-02-secretos-y-cifrado.md) (063, 071); el esquema y la clave Java deben decidirse con el modelo Java.
