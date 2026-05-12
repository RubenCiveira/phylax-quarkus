package net.civeira.phylax.features.oauth.user.domain.gateway;

import net.civeira.phylax.features.oauth.user.domain.RecoveryNotificationData;

/**
 * Domain port for sending password recovery notifications.
 *
 * <p>
 * Implemented by infrastructure adapters that use the notification outbox. Keeping this gateway in
 * the domain allows the use case to orchestrate notification without importing notification
 * infrastructure.
 * </p>
 */
public interface PasswordRecoveryMailGateway {

  /**
   * Sends a password recovery email to the user.
   *
   * @param data recovery details (recipient, name, tenant, link, expiry)
   */
  void sendPasswordRecovery(RecoveryNotificationData data);
}
