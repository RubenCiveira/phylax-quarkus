# J-09 — Discovery, E2E y documentación

**Prioridad:** inmediata (arnés/discovery) y de cierre (suite) · **Tareas:** [1.1](../issue-1.1-discovery-honesto.md), [2.10](../issue-2.10-documentacion-oauth-pruebas.md), [V.1](../issue-V.1-fixtures-pruebas-http-persistencia.md), [V.2](../issue-V.2-openid-conformance-suite.md), [V.3](../issue-V.3-conformidad-dcr.md). **Depende de:** V.2 tras el núcleo obligatorio de J-01–J-08; los opcionales no bloquean la conformidad básica. V.3 tras DCR J-02.

## Implementación

1. Derivar los contributors del discovery de capacidades operativas: eliminar anuncios de stubs y publicar grants/métodos/algoritmos realmente admitidos. Probar por tenant ruta + lógica + metadata, no solo la cadena de discovery.
2. Crear fixtures HTTP+persistencia con dos tenants, clientes público/confidencial, redirects, claves, reloj y sesiones. Cada épica entrega sus casos concurrentes/negativos; V.1 cubre flujos encadenados code, refresh, device, DCR, PAR y logout.
3. OpenID Conformance Suite Basic/Config/RP-initiated/Back-channel; Dynamic tras DCR. Integrar ejecución reproducible en CI. Actualizar guías OAuth Java solo después de verificar cada contrato.

**Aceptación:** no se anuncia ruta 403/stub ni algoritmo no soportado; suite y tests repetibles, documentación coincide con rutas, requisitos y perfiles comprobados.

**Referencia PHP:** `done/EP-01-discovery-honesto.md` y EP-16 (042, 066). Sus fixtures conceptuales son compartibles; no copiar archivos ni declarar conformidad PHP como Java.
