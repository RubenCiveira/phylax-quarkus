package net.civeira.phylax.features.oauth.gdpr.infrastructure.driven;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.common.infrastructure.mail.Attach;
import net.civeira.phylax.common.infrastructure.mail.EmailMessage;
import net.civeira.phylax.common.infrastructure.mail.EmailService;
import net.civeira.phylax.features.access.user.domain.User;
import net.civeira.phylax.features.access.user.domain.gateway.UserFilter;
import net.civeira.phylax.features.access.user.domain.gateway.UserReadRepositoryGateway;
import net.civeira.phylax.features.access.userconsentedscopes.domain.UserConsentedScopes;
import net.civeira.phylax.features.access.userconsentedscopes.domain.gateway.UserConsentedScopesFilter;
import net.civeira.phylax.features.access.userconsentedscopes.domain.gateway.UserConsentedScopesReadRepositoryGateway;
import net.civeira.phylax.features.oauth.gdpr.domain.gateway.UserDataExportGateway;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class UserDataExportAdapter implements UserDataExportGateway {

  private final UserReadRepositoryGateway userRead;
  private final UserConsentedScopesReadRepositoryGateway consentRead;
  private final DataSource dataSource;
  private final EmailService emailService;

  @Override
  public void requestExport(String userUid, String username, String email, String tenant) {
    try {
      String json = buildExportJson(userUid, username, tenant);
      sendExportEmail(email, username, tenant, json);
    } catch (Exception ex) {
      log.error("Failed to process data export for user {}: {}", userUid, ex.getMessage(), ex);
    }
  }

  private String buildExportJson(String userUid, String username, String tenant) {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    sb.append("  \"exportedAt\": \"").append(OffsetDateTime.now()).append("\",\n");
    sb.append("  \"tenant\": \"").append(escapeJson(tenant)).append("\",\n");

    Optional<User> userOpt = userRead.find(UserFilter.builder().uid(userUid).build());
    sb.append("  \"profile\": ");
    appendUserProfile(sb, userOpt);
    sb.append(",\n");

    sb.append("  \"grantedScopes\": ");
    appendConsentedScopes(sb, userOpt);
    sb.append(",\n");

    sb.append("  \"sessions\": ");
    appendSessions(sb, userUid, username);
    sb.append(",\n");

    sb.append("  \"acceptedTerms\": ");
    appendAcceptedTerms(sb, userUid);
    sb.append(",\n");

    sb.append("  \"auditLog\": ");
    appendAuditLog(sb, userUid);
    sb.append("\n}");

    return sb.toString();
  }

  private void appendUserProfile(StringBuilder sb, Optional<User> userOpt) {
    if (userOpt.isEmpty()) {
      sb.append("{}");
      return;
    }
    User u = userOpt.get();
    sb.append("{\n");
    sb.append("    \"uid\": \"").append(escapeJson(u.getUid())).append("\",\n");
    sb.append("    \"name\": \"").append(escapeJson(u.getName())).append("\",\n");
    sb.append("    \"email\": \"").append(escapeJson(u.getEmail().orElse(""))).append("\",\n");
    sb.append("    \"emailVerified\": ").append(u.isEmailVerified()).append(",\n");
    sb.append("    \"enabled\": ").append(u.isEnabled()).append("\"\n");
    sb.append("  }");
  }

  private void appendConsentedScopes(StringBuilder sb, Optional<User> userOpt) {
    if (userOpt.isEmpty()) {
      sb.append("[]");
      return;
    }
    List<UserConsentedScopes> consents =
        consentRead.list(UserConsentedScopesFilter.builder().user(userOpt.get()).build());
    sb.append("[");
    boolean first = true;
    for (UserConsentedScopes c : consents) {
      if (!first) {
        sb.append(", ");
      }
      first = false;
      sb.append("{\n");
      sb.append("    \"scope\": \"").append(escapeJson(c.getScope().orElse(""))).append("\",\n");
      sb.append("    \"clientUid\": \"").append(escapeJson(c.getTrustedClientUid()))
          .append("\",\n");
      sb.append("    \"granted\": ").append(Boolean.TRUE.equals(c.isGranted())).append(",\n");
      sb.append("    \"decisionAt\": \"").append(c.getDecisionAt().map(Object::toString).orElse(""))
          .append("\"\n");
      sb.append("  }");
    }
    sb.append("]");
  }

  private void appendSessions(StringBuilder sb, String userUid, String username) {
    sb.append("[");
    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT session, client_id, ip_address, user_agent, last_used_at, expiration FROM _oauth_session WHERE auth_data LIKE ?")) {
      stmt.setString(1, "%" + userUid + "%");
      try (ResultSet rs = stmt.executeQuery()) {
        boolean first = true;
        while (rs.next()) {
          if (!first) {
            sb.append(", ");
          }
          first = false;
          sb.append("{\n");
          sb.append("    \"sessionId\": \"").append(escapeJson(rs.getString("session")))
              .append("\",\n");
          sb.append("    \"clientId\": \"").append(escapeJson(rs.getString("client_id")))
              .append("\",\n");
          sb.append("    \"ipAddress\": \"").append(escapeJson(rs.getString("ip_address")))
              .append("\",\n");
          sb.append("    \"userAgent\": \"").append(escapeJson(rs.getString("user_agent")))
              .append("\",\n");
          sb.append("    \"lastUsedAt\": \"").append(ts(rs.getTimestamp("last_used_at")))
              .append("\",\n");
          sb.append("    \"expiration\": \"").append(ts(rs.getTimestamp("expiration")))
              .append("\"\n");
          sb.append("  }");
        }
      }
    } catch (SQLException ex) {
      log.warn("Could not query sessions for export: {}", ex.getMessage());
    }
    sb.append("]");
  }

  private void appendAcceptedTerms(StringBuilder sb, String userUid) {
    sb.append("[");
    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT terms_uid, accepted_at, ip_address FROM access_user_accepted_termns_of_use WHERE user_uid = ?")) {
      stmt.setString(1, userUid);
      try (ResultSet rs = stmt.executeQuery()) {
        boolean first = true;
        while (rs.next()) {
          if (!first) {
            sb.append(", ");
          }
          first = false;
          sb.append("{\n");
          sb.append("    \"termsUid\": \"").append(escapeJson(rs.getString("terms_uid")))
              .append("\",\n");
          sb.append("    \"acceptedAt\": \"").append(ts(rs.getTimestamp("accepted_at")))
              .append("\",\n");
          sb.append("    \"ipAddress\": \"").append(escapeJson(rs.getString("ip_address")))
              .append("\"\n");
          sb.append("  }");
        }
      }
    } catch (SQLException ex) {
      log.warn("Could not query accepted terms for export: {}", ex.getMessage());
    }
    sb.append("]");
  }

  private void appendAuditLog(StringBuilder sb, String userUid) {
    sb.append("[");
    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT event_type, created_at, ip_address, user_agent FROM _oauth_audit_log WHERE user_id = ? ORDER BY created_at DESC LIMIT 500")) {
      stmt.setString(1, userUid);
      try (ResultSet rs = stmt.executeQuery()) {
        boolean first = true;
        while (rs.next()) {
          if (!first) {
            sb.append(", ");
          }
          first = false;
          sb.append("{\n");
          sb.append("    \"eventType\": \"").append(escapeJson(rs.getString("event_type")))
              .append("\",\n");
          sb.append("    \"createdAt\": \"").append(ts(rs.getTimestamp("created_at")))
              .append("\",\n");
          sb.append("    \"ipAddress\": \"").append(escapeJson(rs.getString("ip_address")))
              .append("\",\n");
          sb.append("    \"userAgent\": \"").append(escapeJson(rs.getString("user_agent")))
              .append("\"\n");
          sb.append("  }");
        }
      }
    } catch (SQLException ex) {
      log.warn("Could not query audit log for export: {}", ex.getMessage());
    }
    sb.append("]");
  }

  private void sendExportEmail(String email, String username, String tenant, String json) {
    try {
      File tmpFile = File.createTempFile("gdpr-export-", ".json");
      Files.writeString(tmpFile.toPath(), json, StandardCharsets.UTF_8);
      EmailMessage msg = EmailMessage.builder().targetAddress(email).targetName(username)
          .subject("Your data export — " + tenant)
          .content("Please find your personal data export attached to this email.")
          .htmlContent("<p>Please find your personal data export attached to this email.</p>"
              + "<p>This export was generated in response to your data portability request (GDPR Art. 20). "
              + "The file contains your profile, active sessions, consented scopes, accepted terms, and audit log entries.</p>")
          .attacheds(List.of(new Attach("user-data-export.json", tmpFile))).sendedObserver(() -> {
            if (!tmpFile.delete()) {
              log.warn("Could not delete temporary export file: {}", tmpFile.getAbsolutePath());
            }
          }).build();
      emailService.delaySendMessage(msg);
    } catch (IOException ex) {
      log.error("Could not create temporary export file for user {}: {}", email, ex.getMessage());
    }
  }

  private String escapeJson(String value) {
    if (value == null) {
      return "";
    }
    return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r",
        "\\r");
  }

  private String ts(java.sql.Timestamp ts) {
    return ts == null ? "" : ts.toInstant().toString();
  }
}
