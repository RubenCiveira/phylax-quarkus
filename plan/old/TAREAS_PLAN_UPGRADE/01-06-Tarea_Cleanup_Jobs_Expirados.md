# Tarea 01-06 — Cleanup jobs para `_oauth_temporal_codes` y `_oauth_sessions` expirados

[x] Done

> **Fase:** 01 — Refactoring y Deuda Técnica
> **Prioridad:** P0
> **Esfuerzo estimado:** Bajo
> **Bloquea:** Performance a largo plazo (DB bloat)

---

## Descripción

Las tablas `_oauth_temporal_codes` y `_oauth_sessions` acumulan filas expiradas
indefinidamente porque no hay ningún job de limpieza. A medida que el sistema
escale, esto degradará las queries de lookup por `expiration`.

---

## Pasos de implementación

### 1. Crear el Scheduled Job con Quarkus Scheduler

Crear clase en:
`src/main/java/net/civeira/phylax/features/oauth/maintenance/`

```java
package net.civeira.phylax.features.oauth.maintenance;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import io.agroal.api.AgroalDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Instant;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OAuthExpiredRecordsCleaner {

    private static final Logger log = Logger.getLogger(OAuthExpiredRecordsCleaner.class);

    @Inject
    AgroalDataSource dataSource;

    @Scheduled(cron = "0 0 3 * * ?")  // 3:00 AM todos los días
    @Transactional
    public void cleanExpiredTemporalCodes() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM _oauth_temporal_codes WHERE expiration < ?")) {
            ps.setObject(1, Instant.now());
            int deleted = ps.executeUpdate();
            log.infof("Cleaned %d expired temporal codes", deleted);
        } catch (Exception e) {
            log.errorf(e, "Error cleaning expired temporal codes");
        }
    }

    @Scheduled(cron = "0 30 3 * * ?")  // 3:30 AM todos los días
    @Transactional
    public void cleanExpiredSessions() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM _oauth_sessions WHERE expiration < ?")) {
            ps.setObject(1, Instant.now());
            int deleted = ps.executeUpdate();
            log.infof("Cleaned %d expired sessions", deleted);
        } catch (Exception e) {
            log.errorf(e, "Error cleaning expired sessions");
        }
    }
}
```

### 2. Verificar que Quarkus Scheduler está en el classpath

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-scheduler</artifactId>
</dependency>
```

### 3. Configurar la expresión cron en `application.properties` (opcional)

Para permitir override por entorno:

```properties
# application.properties
oauth.cleanup.temporal-codes.cron=0 0 3 * * ?
oauth.cleanup.sessions.cron=0 30 3 * * ?
```

Y usar `@Scheduled(cron = "{oauth.cleanup.temporal-codes.cron}")`.

### 4. Añadir índice sobre `expiration` si no existe

```sql
-- liquibase formatted sql

-- changeset phylax-dev:idx-temporal-codes-expiration
CREATE INDEX idx_oauth_temporal_codes_expiration
  ON _oauth_temporal_codes (expiration);

-- changeset phylax-dev:idx-sessions-expiration
CREATE INDEX idx_oauth_sessions_expiration
  ON _oauth_sessions (expiration);
```

### 5. Test del job

En tests de integración (con `@QuarkusTest`):

```java
@Test
void cleanupJobDeletesExpiredRecords() {
    // Insertar registro expirado
    // Ejecutar directamente el método del job
    cleaner.cleanExpiredTemporalCodes();
    // Verificar que fue borrado
}
```

---

## Criterios de aceptación

- [x] `OAuthExpiredRecordsCleaner` existe y está anotado con `@ApplicationScoped`
- [x] Dos métodos `@Scheduled`: uno para `_oauth_temporal_codes`, otro para `_oauth_sessions`
- [x] Índices sobre columna `expiration` en ambas tablas (migración Liquibase)
- [x] Test que verifica el comportamiento del cleanup
- [x] Log de registros eliminados en cada ejecución
