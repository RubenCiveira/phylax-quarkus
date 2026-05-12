package net.civeira.phylax.features.oauth.user.application.listener;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.document.template.domain.TemplateChannelOptions;
import net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationCommand;
import net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationUseCase;
import net.civeira.phylax.features.oauth.authentication.domain.event.LoginSucceededEvent;

/**
 * Sends an admin login-alert notification after every successful authentication.
 *
 * <p>
 * Delivery is best-effort: exceptions are logged and swallowed so that a notification failure never
 * breaks the login flow.
 * </p>
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class NotifyLogin {

  private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  private final EnqueueNotificationUseCase enqueue;

  public void onLoginSucceeded(@Observes LoginSucceededEvent event) {
    try {
      enqueue.enqueue(EnqueueNotificationCommand.builder().templateCode("user.login")
          .channel(TemplateChannelOptions.MAIL).recipient(event.getUsername())
          .variables(Map.<String, Object>of("user", Map.of("name", event.getUsername()), "login",
              Map.of("date", event.getTime().atOffset(ZoneOffset.UTC).format(ISO))))
          .urgent(true).build());
    } catch (Exception ex) {
      log.warn("Unable to enqueue login notification for user={}: {}", event.getUsername(),
          ex.getMessage());
    }
  }
}
