package net.civeira.phylax.features.oauth.userinvitation.infrastructure.driven;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.access.userinvitation.domain.UserInvitation;
import net.civeira.phylax.features.oauth.userinvitation.domain.gateway.InvitationAcceptGateway;

/**
 * Stub adapter for the accept cross-BC operation (user creation + invitation status update).
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real implementation in {@code access/oauth} that uses
 * {@link net.civeira.phylax.features.access.user.domain.gateway.UserWriteRepositoryGateway} and
 * {@link net.civeira.phylax.features.access.userinvitation.domain.gateway.UserInvitationWriteRepositoryGateway}.
 * </p>
 */
@ApplicationScoped
@Slf4j
public class InvitationAcceptAdapter implements InvitationAcceptGateway {

  @Override
  public String acceptAndCreateUser(UserInvitation invitation, String password, String tenantName) {
    log.warn("InvitationAcceptAdapter is a stub — user not created for invitation {}",
        invitation.getUid());
    return "";
  }
}
