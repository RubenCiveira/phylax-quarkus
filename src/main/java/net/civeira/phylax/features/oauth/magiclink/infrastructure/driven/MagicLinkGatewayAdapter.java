package net.civeira.phylax.features.oauth.magiclink.infrastructure.driven;

import java.util.Optional;

import jakarta.enterprise.inject.Vetoed;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.magiclink.domain.MagicLink;
import net.civeira.phylax.features.oauth.magiclink.domain.gateway.MagicLinkGateway;

/**
 * Stub adapter for magic link persistence.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real persistence implementation backed by a database or
 * cache store with TTL support.
 * </p>
 */
@Vetoed
@Slf4j
public class MagicLinkGatewayAdapter implements MagicLinkGateway {

  @Override
  public void store(MagicLink magicLink) {
    log.warn("MagicLinkGatewayAdapter is a stub — magic link {} not persisted", magicLink.getUid());
  }

  @Override
  public Optional<MagicLink> findByHash(String tokenHash, String tenantId) {
    return Optional.empty();
  }

  @Override
  public void markUsed(String uid) {
    log.warn("MagicLinkGatewayAdapter is a stub — cannot mark {} as used", uid);
  }
}
