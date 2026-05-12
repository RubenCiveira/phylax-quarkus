package net.civeira.phylax.features.oauth.userinvitation.infrastructure.driven;

import java.time.OffsetDateTime;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.userinvitation.domain.gateway.InvitationStoreGateway;

/**
 * Stub adapter for invitation cancel and refresh operations.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real implementation backed by
 * {@link net.civeira.phylax.features.access.userinvitation.domain.gateway.UserInvitationWriteRepositoryGateway}.
 * </p>
 */
@ApplicationScoped
@Slf4j
public class InvitationStoreAdapter implements InvitationStoreGateway {

  @Override
  public void cancel(String uid) {
    log.warn("InvitationStoreAdapter is a stub — invitation {} not cancelled", uid);
  }

  @Override
  public RefreshData refresh(String uid, String newTokenHash, OffsetDateTime newExpiresAt) {
    log.warn("InvitationStoreAdapter is a stub — invitation {} not refreshed", uid);
    return new RefreshData("", "");
  }
}
