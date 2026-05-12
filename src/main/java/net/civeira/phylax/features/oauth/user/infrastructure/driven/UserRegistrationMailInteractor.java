package net.civeira.phylax.features.oauth.user.infrastructure.driven;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.document.template.domain.TemplateChannelOptions;
import net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationCommand;
import net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationUseCase;
import net.civeira.phylax.features.oauth.user.domain.RegistrationNotificationData;
import net.civeira.phylax.features.oauth.user.domain.gateway.UserRegistrationMailGateway;

/**
 * Sends user registration verification emails by enqueuing a templated notification.
 *
 * <p>
 * Uses template code {@code user.register} with variables {@code user.name} and
 * {@code registration.activateUrl}.
 * </p>
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationMailInteractor implements UserRegistrationMailGateway {

  private final EnqueueNotificationUseCase enqueue;

  @Override
  public void sendRegistrationVerification(RegistrationNotificationData data) {
    try {
      enqueue.enqueue(EnqueueNotificationCommand.builder().templateCode("user.register")
          .channel(TemplateChannelOptions.MAIL).tenant(data.tenant()).recipient(data.email())
          .variables(java.util.Map.<String, Object>of("user", java.util.Map.of("name", data.name()),
              "registration", java.util.Map.of("activateUrl", data.activateUrl())))
          .urgent(false).build());
    } catch (Exception ex) {
      log.warn("Unable to enqueue registration email for {}: {}", data.email(), ex.getMessage());
    }
  }
}
