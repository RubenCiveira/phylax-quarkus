package net.civeira.phylax.features.oauth.profile.infrastructure.driven;

import jakarta.enterprise.inject.Vetoed;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.profile.domain.gateway.PasswordGateway;

/**
 * Stub adapter for profile password change.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real implementation that verifies the old password and
 * updates the hash via the access/user BC.
 * </p>
 */
@Vetoed
@Slf4j
public class PasswordAdapter implements PasswordGateway {

  @Override
  public boolean changePassword(String userUid, String tenant, String oldPass, String newPass) {
    log.warn("PasswordAdapter is a stub — password not changed for user {}", userUid);
    return false;
  }
}
