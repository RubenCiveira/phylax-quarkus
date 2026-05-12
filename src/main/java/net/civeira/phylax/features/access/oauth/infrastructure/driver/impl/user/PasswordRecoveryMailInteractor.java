package net.civeira.phylax.features.access.oauth.infrastructure.driver.impl.user;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.document.template.domain.TemplateChannelOptions;
import net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationCommand;
import net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationUseCase;
import net.civeira.phylax.features.oauth.user.domain.RecoveryNotificationData;
import net.civeira.phylax.features.oauth.user.domain.gateway.PasswordRecoveryMailGateway;

/**
 * Sends password recovery emails by enqueuing a templated notification.
 *
 * <p>
 * Uses template code {@code user.password-recovery} with variables {@code user.name},
 * {@code recovery.url}, and {@code recovery.expiresAt}.
 * </p>
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryMailInteractor implements PasswordRecoveryMailGateway {

  private final EnqueueNotificationUseCase enqueue;

  @Override
  public void sendPasswordRecovery(RecoveryNotificationData data) {
    try {
      enqueue
          .enqueue(EnqueueNotificationCommand.builder().templateCode("user.password-recovery")
              .channel(TemplateChannelOptions.MAIL).tenant(data.tenant()).recipient(data.email())
              .variables(java.util.Map.<String, Object>of("user",
                  java.util.Map.of("name", data.name()), "recovery", java.util.Map.of("url",
                      data.recoveryUrl(), "expiresAt", data.expiresAt().toString())))
              .urgent(false).build());
    } catch (Exception ex) {
      log.warn("Unable to enqueue password recovery email for {}: {}", data.email(),
          ex.getMessage());
    }
  }
}
