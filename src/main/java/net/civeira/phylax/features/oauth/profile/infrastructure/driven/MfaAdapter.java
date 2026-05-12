package net.civeira.phylax.features.oauth.profile.infrastructure.driven;

import jakarta.enterprise.inject.Vetoed;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.profile.domain.MfaSetup;
import net.civeira.phylax.features.oauth.profile.domain.gateway.MfaGateway;

/**
 * Stub adapter for MFA (TOTP) management.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real implementation that stores and verifies TOTP seeds.
 * </p>
 */
@Vetoed
@Slf4j
public class MfaAdapter implements MfaGateway {

  @Override
  public boolean isEnabled(String userUid, String tenant) {
    return false;
  }

  @Override
  public MfaSetup buildSetup(String userUid, String tenant) {
    log.warn("MfaAdapter is a stub — returning placeholder MFA setup for user {}", userUid);
    return new MfaSetup("PLACEHOLDER_SEED", null);
  }

  @Override
  public void enable(String userUid, String tenant, String seed, String otp) {
    log.warn("MfaAdapter is a stub — MFA not enabled for user {}", userUid);
  }

  @Override
  public void disable(String userUid, String tenant) {
    log.warn("MfaAdapter is a stub — MFA not disabled for user {}", userUid);
  }
}
