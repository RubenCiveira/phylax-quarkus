package net.civeira.phylax.features.access.notify.application.policy;

import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.mail.EmailMessage;
import net.civeira.phylax.common.infrastructure.mail.EmailService;
import net.civeira.phylax.features.access.user.domain.event.UserCreateEvent;

@RequiredArgsConstructor
public class LoginNotify {
  private final EmailService mailer;

  public void onUserCreate(@Observes UserCreateEvent created) {
    mailer.sendMessage(EmailMessage.builder().targetName("Ruben Civeira")
        .targetAddress("rubenciveira@gmail.com").subject("New user").content("User created")
        .htmlContent("<h1>User created</h1>").sendedObserver(() -> {
          System.err.println("MAIL SENDED");
        }).failObserver(ex -> {
          ex.printStackTrace();
        }).build());
  }
}
