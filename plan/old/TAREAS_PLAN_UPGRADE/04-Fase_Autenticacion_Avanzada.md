# Fase 04 — Autenticación Avanzada

> **Prioridad:** P2-P3
> **Prerequisito:** Fase 02 completada
> **Fecha objetivo:** Sprint 6-8

---

## Descripción

Esta fase añade métodos de autenticación alternativos y modernos. Cada feature
es independiente entre sí y puede implementarse en el orden que dicten las
prioridades del producto.

El objetivo es convertir el servidor en una plataforma de autenticación
completa que soporte los estándares más modernos (FIDO2/WebAuthn, passwordless)
y los proveedores sociales más comunes.

---

## Tareas

| # | Tarea | Estándar | Impacto | Esfuerzo |
|---|-------|---------|---------|----------|
| 04-01 | WebAuthn / Passkeys (FIDO2) | FIDO2 / W3C | Alto | Alto |
| 04-02 | Magic Links / Passwordless Email | — | Medio | Bajo |
| 04-03 | MFA por SMS y OTP por Email | — | Medio | Medio |
| 04-04 | Proveedores de login social adicionales | OAuth 2.0 | Medio | Medio |

---

## Dependencias internas

Todas las tareas de esta fase son independientes entre sí.
Cada una añade un nuevo paso al authorize flow o un nuevo método MFA.

---

## Notas de arquitectura

- Cada nuevo método de autenticación debe implementar una interfaz de dominio:
  - Métodos MFA: `MfaProvider` (similar a la interfaz TOTP existente)
  - Proveedores sociales: `OAuthProviderAdapter` (patrón del `delegated` feature)
- Los pasos del authorize flow son configurables por tenant en `access_tenant_config`
- Los templates HTML (Qute) son multi-tenant — añadir nuevo paso como nueva plantilla

---

## Criterios de aceptación de fase

- [ ] WebAuthn: registro y autenticación completados end-to-end
- [ ] Magic Links: se envían por email y autentican al usuario en un click
- [ ] MFA por email y SMS disponibles como opciones adicionales a TOTP
- [ ] Al menos 2 proveedores sociales adicionales (GitHub + Microsoft)
- [ ] Todos los nuevos métodos son configurables por tenant
- [ ] Tests de integración para cada feature
