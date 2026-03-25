package net.civeira.phylax.features.mimo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.mail.EmailMessage;
import net.civeira.phylax.common.infrastructure.mail.EmailService;
import net.civeira.phylax.common.infrastructure.mail.MailConfiguration;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData;

@ApplicationScoped
@RequiredArgsConstructor
public class LoginMail {
  private final EmailService mailer;


  public void onLogin(@Observes AuthenticationData login) {
    System.err.println("LOGIN----" + login.getUsername());

    MailConfiguration mailConfiguration = MailConfiguration.builder().useTtls(true)
        .smtpHost("smtp.gmail.com").smtpPort(587).fromEmail("osintdagda@gmail.com")
        .smtpLogin("osintdagda@gmail.com").smtpPass("bcspvmufkdajrukm").build();
    /*
     * MAILER_HOST=smtp.gmail.com MAILER_PORT=587 MAILER_START_TLS=REQUIRED MAILER_FROM_NAME=Dagda
     * MAILER_FROM_EMAIL=osintdagda@gmail.com MAILER_USERNAME=osintdagda@gmail.com
     * MAILER_PASSWORD=bcspvmufkdajrukm
     */

    EmailMessage message = EmailMessage.builder().targetAddress("rubenciveira@gmail.com")
        .targetName("Ruben").subject("Login").content("Hay un login")
        .htmlContent("<h1>Hay un login</h1><p>Un usuario hizo login</p>").build();
    mailer.delaySendMessage(mailConfiguration, message);
  }
}
