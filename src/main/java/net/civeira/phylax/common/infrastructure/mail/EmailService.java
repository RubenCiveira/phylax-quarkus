package net.civeira.phylax.common.infrastructure.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.vertx.ext.mail.MailMessage;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

/**
 * Sends email messages using either the system mailer or a per-tenant SMTP client.
 *
 * When no {@link MailConfiguration} is provided, messages are sent via the Quarkus-managed
 * {@link Mailer} configured at startup. When a configuration override is present, a dedicated
 * Vert.x {@link io.vertx.mutiny.ext.mail.MailClient} is obtained from
 * {@link DynamicMailClientCache} and used instead, allowing per-tenant SMTP credentials at runtime.
 * Both synchronous and asynchronous delivery modes are supported.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

  private final Mailer mailer;
  private final DynamicMailClientCache dynamicClientCache;

  private ExecutorService executor;


  /**
   * Sends messages asynchronously using the default configuration.
   *
   * @param message messages to send
   */
  public void delaySendMessage(EmailMessage message) {
    delaySendMessages(Optional.empty(), message);
  }

  /**
   * Sends messages asynchronously using an optional configuration override.
   *
   * @param config optional mail configuration
   * @param message messages to send
   */
  public synchronized void delaySendMessage(MailConfiguration config, EmailMessage message) {
    delaySendMessages(Optional.of(config), message);
  }

  /**
   * Sends a message synchronously using the default configuration.
   *
   * @param message message to send
   */
  public void sendMessage(EmailMessage message) {
    sendMessage(Optional.empty(), message);
  }

  /**
   * Sends a message synchronously using an optional configuration override.
   *
   * @param message message to send
   * @param config optional mail configuration
   */
  public void sendMessage(MailConfiguration config, EmailMessage messages) {
    sendMessage(Optional.of(config), messages);
  }

  /**
   * Sends messages asynchronously using the default configuration.
   *
   * @param message messages to send
   */
  public void delaySendMessages(EmailMessage... messages) {
    delaySendMessages(Optional.empty(), messages);
  }

  /**
   * Sends messages asynchronously using an optional configuration override.
   *
   * @param config optional mail configuration
   * @param message messages to send
   */
  public synchronized void delaySendMessages(MailConfiguration config, EmailMessage... messages) {
    delaySendMessages(Optional.of(config), messages);
  }

  /**
   * Sends a message synchronously using the default configuration.
   *
   * @param message message to send
   */
  public void sendMessages(EmailMessage messages) {
    sendMessage(Optional.empty(), messages);
  }

  /**
   * Sends a message synchronously using an optional configuration override.
   *
   * @param message message to send
   * @param config optional mail configuration
   */
  public void sendMessages(MailConfiguration config, EmailMessage... messages) {
    sendMessage(Optional.of(config), messages);
  }

  private synchronized void delaySendMessages(Optional<MailConfiguration> config,
      EmailMessage... message) {
    if (null == executor) {
      executor = Executors.newFixedThreadPool(10);
    }
    executor.submit(() -> sendMessage(config, message));
  }

  private void sendMessage(Optional<MailConfiguration> config, EmailMessage... messages) {
    for (EmailMessage message : messages) {
      try {
        String to = message.getTargetName() + " <" + message.getTargetAddress() + ">";
        if (config.isPresent()) {
          sendWithDynamicClient(to, message, config.get());
        } else {
          sendWithDefaultMailer(to, message);
        }
        message.getSendedObserver().ifPresent(MailSended::ok);
      } catch (Exception ex) {
        message.getFailObserver().ifPresent(obs -> obs.fail(ex));
        LOGGER.error("Imposible enviar email {} a {} <{}> por {}", message.getSubject(),
            message.getTargetName(), message.getTargetAddress(), ex.getMessage());
      }
    }
  }

  private void sendWithDefaultMailer(String to, EmailMessage message) {
    Mail mail = null == message.getHtmlContent()
        ? Mail.withText(to, message.getSubject(), message.getContent())
        : Mail.withHtml(to, message.getSubject(), message.getHtmlContent());
    attached(mail, message.getEmbbeds(), true);
    attached(mail, message.getAttacheds(), false);
    if (null != message.getHtmlContent()) {
      mail.setText(null == message.getContent() ? message.getHtmlContent() : message.getContent());
    }
    mailer.send(mail);
  }

  private void sendWithDynamicClient(String to, EmailMessage message, MailConfiguration conf) {
    MailMessage vertxMail = new MailMessage();
    vertxMail.setTo(to);
    vertxMail.setSubject(message.getSubject());

    conf.getFromEmail().ifPresent(email -> {
      String from = conf.getFromName().map(n -> n + " <" + email + ">").orElse(email);
      vertxMail.setFrom(from);
    });

    if (null != message.getHtmlContent()) {
      vertxMail.setHtml(message.getHtmlContent());
      vertxMail
          .setText(null == message.getContent() ? message.getHtmlContent() : message.getContent());
    } else {
      vertxMail.setText(message.getContent());
    }

    dynamicClientCache.clientFor(conf).sendMailAndAwait(vertxMail);
  }

  private void attached(Mail mail, List<Attach> attachs, boolean inline) {
    for (Attach attach : attachs) {
      attach.getFichero().ifPresent(file -> {
        String contentType = null;
        if (inline) {
          mail.addInlineAttachment(file.getName(), file, contentType, "<" + attach.getCid() + ">");
        } else {
          mail.addAttachment(file.getName(), file, contentType);
        }
      });
      attach.getDatasource().ifPresent(datasource -> {
        try {
          File file = Files.createTempFile("mail", ".bin").toFile();
          FileUtils.copyInputStreamToFile(datasource.getInputStream(), file);
          if (inline) {
            mail.addInlineAttachment(datasource.getName(), file, datasource.getContentType(),
                "<" + attach.getCid() + ">");
          } else {
            mail.addAttachment(datasource.getName(), file, datasource.getContentType());
          }
          file.delete();
        } catch (IOException ex) {
          LOGGER.error("Unable to retrieve attach datasource " + datasource.getName(), ex);
        }
      });
    }
  }
}
