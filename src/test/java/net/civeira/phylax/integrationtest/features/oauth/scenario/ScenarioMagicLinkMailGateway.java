package net.civeira.phylax.integrationtest.features.oauth.scenario;

import java.time.OffsetDateTime;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;
import net.civeira.phylax.features.oauth.magiclink.domain.gateway.MagicLinkMailGateway;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioMagicLinkMailGateway implements MagicLinkMailGateway {
  @Override
  public void sendMagicLink(String toEmail, String userName, TenantRef tenant, String magicUrl,
      OffsetDateTime expiresAt) {
    // no-op in tests
  }
}
