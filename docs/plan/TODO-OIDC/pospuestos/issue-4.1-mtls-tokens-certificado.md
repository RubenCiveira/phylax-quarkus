# 4.1 — mTLS y tokens ligados a certificado

- **Fase:** 4 (solo con requisito) · **Estado:** pospuesta (27-10-2026) · **Referencia:** RFC 8705.
- **Motivo:** necesita una terminación TLS de confianza que reenvíe el certificado del cliente; sin ella, aceptar una cabecera de proxy sería falsificable. DPoP ([3.6](../done/issue-3.6-dpop.md)) cubre el vínculo de tokens sin infraestructura. El modelo ya tiene `tls_client_auth_subject_dn` y `tls_client_certificate_bound_access_tokens`; `MtlsDiscoveryContributor` no anuncia nada.
- **Se reabre:** con un perfil FAPI o RP que lo exija y la decisión de infraestructura (proxy de confianza y aliases `mtls_endpoint_aliases`).
