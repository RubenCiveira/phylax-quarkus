package net.civeira.phylax.features.mimo;

import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.access.tenant.domain.TenantReference;
import net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationCommand;
import net.civeira.phylax.features.notification.outbox.application.usecase.enqueue.EnqueueNotificationUseCase;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationMode;
import net.civeira.phylax.features.oauth.authentication.domain.event.LoginSucceededEvent;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class LoginMail {

  private static final String TEMPLATE_CODE = "OAUTH_LOGIN_NOTIFICATION";
  private static final String CHANNEL = "MAIL";

  private final EnqueueNotificationUseCase enqueueNotification;

  public void onLogin(@Observes LoginSucceededEvent login) {
    if (login.getMode() != AuthenticationMode.PASSWORD
        && login.getMode() != AuthenticationMode.MFA) {
      return;
    }
    try {
      EnqueueNotificationCommand cmd =
          EnqueueNotificationCommand.builder().templateCode(TEMPLATE_CODE).channel(CHANNEL)
              .tenant(TenantReference.of(login.getTenant())).recipient("rubenciveira@gmail.com")// (login.getUsername())
              .variables(Map.of("user", Map.of("name", login.getUsername()), "login",
                  Map.of("time", login.getTime().toString())))
              .build();
      enqueueNotification.enqueue(cmd);
    } catch (Exception e) {
      log.warn("Failed to enqueue login notification for user={}", login.getUsername(), e);
    }
  }
}
