package net.civeira.phylax.features.oauth.magiclink.infrastructure.driven;

import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.features.oauth.magiclink.domain.gateway.MagicLinkEnabledGateway;

/**
 * Stub adapter for magic link feature-flag check.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real implementation that checks tenant configuration or a
 * feature flag store. Returns {@code false} to disable the feature by default.
 * </p>
 */
@ApplicationScoped
public class MagicLinkEnabledAdapter implements MagicLinkEnabledGateway {

  @Override
  public boolean isEnabled(String tenantName) {
    return false;
  }
}
