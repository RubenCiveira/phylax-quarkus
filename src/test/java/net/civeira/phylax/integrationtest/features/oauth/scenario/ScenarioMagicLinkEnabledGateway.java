package net.civeira.phylax.integrationtest.features.oauth.scenario;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.magiclink.domain.gateway.MagicLinkEnabledGateway;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioMagicLinkEnabledGateway implements MagicLinkEnabledGateway {
  @Override
  public boolean isEnabled(String tenantName) {
    return false;
  }
}
