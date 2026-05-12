package net.civeira.phylax.features.oauth.userinvitation.application.usecase.accept;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HexFormat;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantFilter;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantReadRepositoryGateway;
import net.civeira.phylax.features.access.userinvitation.domain.UserInvitation;
import net.civeira.phylax.features.access.userinvitation.domain.UserInvitationStatusOptions;
import net.civeira.phylax.features.oauth.userinvitation.domain.UserInvitationSession;
import net.civeira.phylax.features.oauth.userinvitation.domain.gateway.InvitationAcceptGateway;
import net.civeira.phylax.features.oauth.userinvitation.domain.gateway.UserInvitationQueryGateway;
import net.civeira.phylax.features.oauth.userinvitation.domain.gateway.UserInvitationSessionGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class InvitationAcceptUsecase {

  private final UserInvitationQueryGateway queryGateway;
  private final InvitationAcceptGateway acceptGateway;
  private final UserInvitationSessionGateway sessionGateway;
  private final TenantReadRepositoryGateway tenants;

  public InvitationAcceptResult accept(InvitationAcceptParams params) {
    String tokenHash = sha256(params.getRawToken());

    var tenant = tenants.find(TenantFilter.builder().name(params.getTenantName()).build());
    if (tenant.isEmpty() || !tenant.get().isEnabled()) {
      throw new IllegalArgumentException("Invalid invitation.");
    }

    String tenantUid = tenant.get().getUid();

    UserInvitation invitation = queryGateway.findByTokenHash(tokenHash, tenantUid).orElseThrow(
        () -> new IllegalArgumentException("This invitation is invalid or has expired."));

    var expired = invitation.getExpiredAt().isBefore(OffsetDateTime.now(ZoneOffset.UTC));
    
    if (invitation.getStatus().orElse(null) != UserInvitationStatusOptions.PENDING || expired) {
      throw new IllegalArgumentException("This invitation is invalid or has expired.");
    }

    String email = invitation.getEmail();

    String userUid =
        acceptGateway.acceptAndCreateUser(invitation, params.getPassword(), params.getTenantName());

    UserInvitationSession session = sessionGateway
        .createSession(userUid, email, tenantUid, params.getTenantName(), params.getClientId())
        .orElse(null);

    return new InvitationAcceptResult(email, session);
  }

  private static String sha256(String value) {
    try {
      byte[] hash = MessageDigest.getInstance("SHA-256")
          .digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 not available", e);
    }
  }
}
