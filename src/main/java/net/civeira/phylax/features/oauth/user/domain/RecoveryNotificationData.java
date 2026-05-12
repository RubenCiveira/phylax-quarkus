package net.civeira.phylax.features.oauth.user.domain;

import java.time.OffsetDateTime;

import net.civeira.phylax.features.access.tenant.domain.TenantRef;

/**
 * Data needed to send a password recovery notification to the user.
 *
 * Returned by {@link gateway.ChangePasswordGateway#requestForChange} so that the application layer
 * can delegate delivery to {@link gateway.PasswordRecoveryMailGateway} without coupling the domain
 * gateway to the notification infrastructure.
 */
public record RecoveryNotificationData(
    String email,
    String name,
    TenantRef tenant,
    String recoveryUrl,
    OffsetDateTime expiresAt) {
}
