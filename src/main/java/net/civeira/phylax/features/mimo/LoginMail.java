package net.civeira.phylax.features.mimo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.mail.EmailMessage;
import net.civeira.phylax.common.infrastructure.mail.EmailService;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationMode;
import net.civeira.phylax.features.oauth.authentication.domain.event.LoginSucceededEvent;

@ApplicationScoped
@RequiredArgsConstructor
public class LoginMail {
  private final EmailService mailer;

  public void onLogin(@Observes LoginSucceededEvent login) {
    System.err.println("LOGIN----" + login.getUsername());
    System.err.println("WITH: " + login.getMode());
    if (login.getMode() == AuthenticationMode.PASSWORD
        || login.getMode() == AuthenticationMode.MFA) {
      EmailMessage message = EmailMessage.builder().targetAddress("rubenciveira@gmail.com")
          .targetName("Ruben").subject("Login").content("Hay un login")
          .htmlContent("<h1>Hay un login</h1><p>Un usuario hizo login</p>").build();
      mailer.delaySendMessage(message);
    }
  }
}
