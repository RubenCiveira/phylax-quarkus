package net.civeira.phylax.features.oauth.profile.infrastructure.driven;

import java.util.List;

import jakarta.enterprise.inject.Vetoed;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.profile.domain.ActiveSession;
import net.civeira.phylax.features.oauth.profile.domain.gateway.SessionsGateway;

/**
 * Stub adapter for active session listing and revocation.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real implementation backed by the session store.
 * </p>
 */
@Vetoed
@Slf4j
public class SessionsAdapter implements SessionsGateway {

  @Override
  public List<ActiveSession> listByUser(String userUid) {
    return List.of();
  }

  @Override
  public void revoke(String sessionId) {
    log.warn("SessionsAdapter is a stub — session {} not revoked", sessionId);
  }
}
