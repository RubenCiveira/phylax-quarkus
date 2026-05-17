package net.civeira.phylax.features.oauth.gdpr.infrastructure.driven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.common.infrastructure.mail.EmailMessage;
import net.civeira.phylax.common.infrastructure.mail.EmailService;
import net.civeira.phylax.features.access.user.domain.User;
import net.civeira.phylax.features.access.user.domain.UserChangeSet;
import net.civeira.phylax.features.access.user.domain.UserReference;
import net.civeira.phylax.features.access.user.domain.gateway.UserFilter;
import net.civeira.phylax.features.access.user.domain.gateway.UserWriteRepositoryGateway;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.UserAccessTemporalCode;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.UserAccessTemporalCodeChangeSet;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.gateway.UserAccessTemporalCodeFilter;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.gateway.UserAccessTemporalCodeWriteRepositoryGateway;
import net.civeira.phylax.features.access.userconsentedscopes.domain.UserConsentedScopes;
import net.civeira.phylax.features.access.userconsentedscopes.domain.gateway.UserConsentedScopesFilter;
import net.civeira.phylax.features.access.userconsentedscopes.domain.gateway.UserConsentedScopesWriteRepositoryGateway;
import net.civeira.phylax.features.oauth.gdpr.domain.gateway.AccountDeletionGateway;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class AccountDeletionAdapter implements AccountDeletionGateway {

  private static final long TOKEN_TTL_HOURS = 24L;

  private final UserWriteRepositoryGateway userWrite;
  private final UserAccessTemporalCodeWriteRepositoryGateway temporalCodeWrite;
  private final UserConsentedScopesWriteRepositoryGateway consentWrite;
  private final DataSource dataSource;
  private final EmailService emailService;

  @Override
  @Transactional
  public void requestDeletion(String userUid, String username, String email, String tenant,
      String confirmUrl) {
    String deletionCode = UUID.randomUUID().toString();
    OffsetDateTime expiration = OffsetDateTime.now().plusHours(TOKEN_TTL_HOURS);

    storeDeletionCode(userUid, deletionCode, expiration);

    String link = confirmUrl + "?uid=" + userUid + "&code=" + deletionCode;
    sendDeletionEmail(email, username, tenant, link, expiration);
  }

  @Override
  @Transactional
  public boolean confirmDeletion(String userUid, String code, String tenant) {
    Optional<UserAccessTemporalCode> temporalOpt = temporalCodeWrite.findForUpdate(
        UserAccessTemporalCodeFilter.builder().user(UserReference.of(userUid)).build());

    if (temporalOpt.isEmpty()) {
      log.warn("No temporal code found for user {} during deletion confirmation", userUid);
      return false;
    }
    UserAccessTemporalCode temporal = temporalOpt.get();

    String storedCode = temporal.getRecoveryCode().orElse("");
    OffsetDateTime storedExpiration =
        temporal.getRecoveryCodeExpiration().orElse(OffsetDateTime.MIN);

    if (!storedCode.equals(code) || storedCode.isBlank()) {
      log.warn("Deletion confirmation code mismatch for user {}", userUid);
      return false;
    }
    if (OffsetDateTime.now().isAfter(storedExpiration)) {
      log.warn("Deletion confirmation code expired for user {}", userUid);
      return false;
    }

    anonymizeUser(userUid);
    revokeAllSessions(userUid);
    deleteAllConsents(userUid);
    anonymizeAuditLog(userUid);
    clearDeletionCode(temporal);

    log.info("Account deletion completed for user {}", userUid);
    return true;
  }

  private void storeDeletionCode(String userUid, String code, OffsetDateTime expiration) {
    Optional<UserAccessTemporalCode> existing = temporalCodeWrite.findForUpdate(
        UserAccessTemporalCodeFilter.builder().user(UserReference.of(userUid)).build());

    if (existing.isPresent()) {
      UserAccessTemporalCode updated = existing.get().generatePasswordRecover("", code, expiration);
      temporalCodeWrite.update(existing.get(), updated);
    } else {
      UserAccessTemporalCode created = UserAccessTemporalCode
          .create(new UserAccessTemporalCodeChangeSet().newUid().user(UserReference.of(userUid)));
      UserAccessTemporalCode withCode = created.generatePasswordRecover("", code, expiration);
      temporalCodeWrite.update(temporalCodeWrite.create(created), withCode);
    }
  }

  private void anonymizeUser(String userUid) {
    Optional<User> userOpt = userWrite.findForUpdate(UserFilter.builder().uid(userUid).build());
    if (userOpt.isEmpty()) {
      log.warn("User {} not found during anonymization", userUid);
      return;
    }
    User original = userOpt.get();
    UserChangeSet change = new UserChangeSet();
    change.email("deleted-" + userUid + "@deleted.invalid");
    change.name("Deleted User");
    change.passwordPlain(UUID.randomUUID().toString());
    User anonymized = original.update(change).disable();
    userWrite.update(original, anonymized);
  }

  private void revokeAllSessions(String userUid) {
    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "UPDATE _oauth_session SET expiration = ?, revoked_at = ? WHERE auth_data LIKE ?")) {
      Timestamp now = new Timestamp(System.currentTimeMillis());
      stmt.setTimestamp(1, now);
      stmt.setTimestamp(2, now);
      stmt.setString(3, "%" + userUid + "%");
      int rows = stmt.executeUpdate();
      log.debug("Revoked {} sessions for deleted user {}", rows, userUid);
    } catch (SQLException ex) {
      log.error("Failed to revoke sessions for user {}: {}", userUid, ex.getMessage());
    }
  }

  private void deleteAllConsents(String userUid) {
    List<UserConsentedScopes> consents = consentWrite
        .listForUpdate(UserConsentedScopesFilter.builder().user(UserReference.of(userUid)).build());
    for (UserConsentedScopes consent : consents) {
      consentWrite.delete(consent.delete());
    }
    log.debug("Deleted {} consent records for user {}", consents.size(), userUid);
  }

  private void anonymizeAuditLog(String userUid) {
    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt =
            conn.prepareStatement("UPDATE _oauth_audit_log SET user_id = NULL WHERE user_id = ?")) {
      stmt.setString(1, userUid);
      int rows = stmt.executeUpdate();
      log.debug("Anonymized {} audit log entries for user {}", rows, userUid);
    } catch (SQLException ex) {
      log.error("Failed to anonymize audit log for user {}: {}", userUid, ex.getMessage());
    }
  }

  private void clearDeletionCode(UserAccessTemporalCode temporal) {
    temporalCodeWrite.update(temporal, temporal.resetPasswordRecover());
  }

  private void sendDeletionEmail(String email, String username, String tenant, String confirmLink,
      OffsetDateTime expiration) {
    try {
      EmailMessage msg = EmailMessage.builder().targetAddress(email).targetName(username)
          .subject("Account deletion confirmation — " + tenant)
          .content("You requested to delete your account. Click the link below to confirm:\n\n"
              + confirmLink + "\n\nThis link expires at " + expiration
              + ".\n\nIf you did not request this, you can ignore this email.")
          .htmlContent("<p>You requested to delete your account on <strong>" + escapeHtml(tenant)
              + "</strong>.</p>" + "<p><a href=\"" + escapeHtml(confirmLink)
              + "\">Confirm account deletion</a></p>" + "<p>This link expires at <strong>"
              + expiration + "</strong>.</p>"
              + "<p>If you did not request this, you can safely ignore this email.</p>")
          .build();
      emailService.delaySendMessage(msg);
    } catch (Exception ex) {
      log.error("Failed to send deletion confirmation email to {}: {}", email, ex.getMessage());
    }
  }

  private String escapeHtml(String value) {
    if (value == null) {
      return "";
    }
    return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"",
        "&quot;");
  }
}
