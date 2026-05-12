package net.civeira.phylax.features.oauth.user.infrastructure.driven;

import jakarta.enterprise.inject.Vetoed;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.user.domain.RegistrationNotificationData;
import net.civeira.phylax.features.oauth.user.domain.gateway.UserRegistrationMailGateway;

/**
 * Stub adapter for user registration verification mail delivery.
 *
 * <p>
 * Marked {@code @Vetoed} — the real adapter lives in the notification infrastructure and is wired
 * via CDI. Replace with an implementation that enqueues via
 * {@link net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationUseCase}.
 * </p>
 */
@Vetoed
@Slf4j
public class UserRegistrationMailAdapter implements UserRegistrationMailGateway {

  @Override
  public void sendRegistrationVerification(RegistrationNotificationData data) {
    log.warn("UserRegistrationMailAdapter is a stub — registration email not sent to {}",
        data.email());
  }
}
