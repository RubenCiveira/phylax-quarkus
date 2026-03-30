package net.civeira.phylax.features.mimo;

import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.access.user.domain.User;
import net.civeira.phylax.features.access.user.domain.gateway.UserReadRepositoryGateway;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.event.UserAccessTemporalCodeGeneratePasswordRecoverEvent;
import net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationCommand;
import net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationUseCase;

/**
 * Enqueues a password-recovery email whenever a recovery flow is initiated.
 *
 * <p>
 * Observes {@link UserAccessTemporalCodeGeneratePasswordRecoverEvent}, which is fired by the domain
 * after the recovery code is persisted. The event carries all data required to build the
 * notification without any additional lookups.
 *
 * <p>
 * Variables consumed by the {@code OAUTH_PASSWORD_RECOVERY} template:
 * <ul>
 * <li>{@code {{user.name}}} – display name of the user requesting recovery</li>
 * <li>{@code {{recovery.link}}} – one-time URL to reset the password</li>
 * <li>{@code {{expiry.minutes}}} – validity period of the link in minutes</li>
 * </ul>
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryMail {

  private static final String TEMPLATE_CODE = "OAUTH_PASSWORD_RECOVERY";
  private static final String CHANNEL = "MAIL";
  /** Matches {@code PasswordRecoverUsecase.RECOVER_TIME} (12 h). */
  private static final long EXPIRY_MINUTES = 12 * 60;

  private final EnqueueNotificationUseCase enqueueNotification;
  private final UserReadRepositoryGateway users;

  public void onPasswordRecoveryRequested(
      @Observes UserAccessTemporalCodeGeneratePasswordRecoverEvent event) {
    User user = users.resolve(event.getPayload().getUser());
    String recipient = user.getEmail().orElse(null);
    if (recipient == null || recipient.isBlank()) {
      log.debug("Skipping recovery email: user has no email address");
      return;
    }
    EnqueueNotificationCommand cmd = EnqueueNotificationCommand.builder()
        .templateCode(TEMPLATE_CODE).channel(CHANNEL).tenant(user.getTenant()).recipient(recipient)
        .variables(Map.of("user", Map.of("name", user.getName()), "recovery",
            Map.of("link", event.getRecoveryLink() + event.getPayload().getRecoveryCode().orElse("-")), "expiry",
            Map.of("minutes", String.valueOf(EXPIRY_MINUTES))))
        .urgent(true).build();
    enqueueNotification.enqueue(cmd);
  }
}
