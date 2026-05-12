package net.civeira.phylax.features.oauth.user.infrastructure.driven;

import jakarta.enterprise.inject.Vetoed;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.user.domain.RecoveryNotificationData;
import net.civeira.phylax.features.oauth.user.domain.gateway.PasswordRecoveryMailGateway;

/**
 * Stub adapter for password recovery mail delivery.
 *
 * <p>
 * Marked {@code @Vetoed} — the real adapter lives in the notification infrastructure and is wired
 * via CDI. Replace with an implementation that enqueues via
 * {@link net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationUseCase}.
 * </p>
 */
@Vetoed
@Slf4j
public class PasswordRecoveryMailAdapter implements PasswordRecoveryMailGateway {

  @Override
  public void sendPasswordRecovery(RecoveryNotificationData data) {
    log.warn("PasswordRecoveryMailAdapter is a stub — recovery email not sent to {}", data.email());
  }
}
