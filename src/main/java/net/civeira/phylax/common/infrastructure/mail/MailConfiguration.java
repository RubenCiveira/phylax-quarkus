package net.civeira.phylax.common.infrastructure.mail;

import java.util.Optional;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Immutable SMTP configuration for email delivery overrides.
 *
 * Used to supply per-tenant or per-message SMTP settings to {@link EmailService}. Instances are
 * created via the Lombok builder. Fields with {@link Builder.Default} use sensible defaults so
 * callers only need to set what differs from the standard configuration.
 */
@Getter
@Builder
@Accessors(fluent = false)
public final class MailConfiguration {

  /**
   * Indicates whether STARTTLS should be required.
   *
   * When true, the SMTP connection requires STARTTLS negotiation. Set to false only for local or
   * unencrypted relay servers. Defaults to true.
   */
  @Builder.Default
  private final boolean useTtls = true;

  /**
   * Generic timeout in seconds used as the base for connection and write timeouts.
   *
   * Increase this value for slow or high-latency SMTP servers. Defaults to 30 seconds.
   */
  @Builder.Default
  private final int timeout = 30;

  /**
   * SMTP hostname or IP address.
   *
   * Defaults to localhost for development environments. Set to the target SMTP server for
   * production use.
   */
  @Builder.Default
  private final String smtpHost = "localhost";

  /**
   * SMTP port.
   *
   * Common values are 25 (plain), 587 (STARTTLS) and 465 (SSL). Defaults to 25.
   */
  @Builder.Default
  private final int smtpPort = 25;

  /**
   * SMTP authentication username.
   *
   * Leave empty when the server does not require authentication. Defaults to empty string.
   */
  @Builder.Default
  private final String smtpLogin = "";

  /**
   * SMTP authentication password.
   *
   * Leave empty when the server does not require authentication. Use secure secret storage for
   * production credentials. Defaults to empty string.
   */
  @Builder.Default
  private final String smtpPass = "";

  /**
   * Optional display name used as the sender name in outgoing emails.
   *
   * When present, combined with {@link #getFromEmail()} to build the From header. When absent,
   * the mailer uses its globally configured sender name.
   */
  @Getter(lombok.AccessLevel.NONE)
  private final String fromName;

  /**
   * Optional sender email address.
   *
   * Overrides the globally configured From address for this configuration. Use a valid address
   * to avoid SMTP rejection. When absent, the mailer default is used.
   */
  @Getter(lombok.AccessLevel.NONE)
  private final String fromEmail;

  public Optional<String> getFromName() {
    return Optional.ofNullable(fromName);
  }

  public Optional<String> getFromEmail() {
    return Optional.ofNullable(fromEmail);
  }

  /**
   * Write timeout in seconds. Delegates to {@link #getTimeout()}.
   */
  public int getWriteTimeout() {
    return timeout;
  }

  /**
   * Connection timeout in seconds. Delegates to {@link #getTimeout()}.
   */
  public int getConnectionTimeout() {
    return timeout;
  }
}
