package net.civeira.phylax.bootstrap.install;

import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.migration.Migrations;
import net.civeira.phylax.features.notification.smtpoutboundconfig.domain.SmtpOutboundConfig;
import net.civeira.phylax.features.notification.smtpoutboundconfig.domain.SmtpOutboundConfigChangeSet;
import net.civeira.phylax.features.notification.smtpoutboundconfig.domain.gateway.SmtpOutboundConfigFilter;
import net.civeira.phylax.features.notification.smtpoutboundconfig.domain.gateway.SmtpOutboundConfigReadRepositoryGateway;
import net.civeira.phylax.features.notification.smtpoutboundconfig.domain.gateway.SmtpOutboundConfigWriteRepositoryGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class InstallNotification {

  private final SmtpOutboundConfigReadRepositoryGateway smtpConfigsRead;
  private final SmtpOutboundConfigWriteRepositoryGateway smtpConfigs;

  @Transactional
  void installOnStartup(
      @Observes @Priority(Migrations.POST_MIGRATION_PHASE_PRIORITY) final StartupEvent ev) {
    install();
  }

  public void install() {
    if (smtpConfigsRead.count(SmtpOutboundConfigFilter.builder().build()) > 0) {
      return;
    }
    SmtpOutboundConfigChangeSet change = new SmtpOutboundConfigChangeSet().newUid()
        .host(env("MAILER_HOST", "smtp.example.com"))
        .port(envInt("MAILER_PORT", 587))
        .useTls(Boolean.parseBoolean(env("MAILER_START_TLS", "true")))
        .login(env("MAILER_USERNAME", "noreply@example.com"))
        .passwordPlain(env("MAILER_PASSWORD", "change-me"))
        .senderName(env("MAILER_FROM_NAME", "Phylax"))
        .senderEmail(env("MAILER_FROM_EMAIL", "noreply@example.com"))
        .timeout(30)
        .maxRetries(3)
        .retryDelay(60)
        .rateLimit(100);
    smtpConfigs.create(SmtpOutboundConfig.create(change));
  }

  private static String env(String key, String fallback) {
    String value = System.getenv(key);
    return value == null || value.isBlank() ? fallback : value;
  }

  private static int envInt(String key, int fallback) {
    String value = System.getenv(key);
    if (value == null || value.isBlank()) {
      return fallback;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException ex) {
      return fallback;
    }
  }
}
