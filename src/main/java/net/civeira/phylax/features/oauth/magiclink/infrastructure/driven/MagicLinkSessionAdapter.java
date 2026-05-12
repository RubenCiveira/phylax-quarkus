package net.civeira.phylax.features.oauth.magiclink.infrastructure.driven;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.magiclink.domain.MagicLinkSession;
import net.civeira.phylax.features.oauth.magiclink.domain.gateway.MagicLinkSessionGateway;

/**
 * Stub adapter for magic link session creation.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real implementation that creates a persistent session and
 * returns the session identifier and cookie TTL.
 * </p>
 */
@ApplicationScoped
@Slf4j
public class MagicLinkSessionAdapter implements MagicLinkSessionGateway {

  @Override
  public Optional<MagicLinkSession> createSession(String userUid, String email, String tenantUid,
      String tenantName, String clientId) {
    log.warn("MagicLinkSessionAdapter is a stub — session not created for user {}", userUid);
    return Optional.empty();
  }
}
