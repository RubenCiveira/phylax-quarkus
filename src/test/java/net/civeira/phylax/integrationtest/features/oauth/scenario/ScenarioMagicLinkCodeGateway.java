package net.civeira.phylax.integrationtest.features.oauth.scenario;

import java.util.UUID;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.magiclink.domain.gateway.MagicLinkCodeGateway;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioMagicLinkCodeGateway implements MagicLinkCodeGateway {
  @Override
  public String createAuthCode(String userUid, String clientId, String scope, String redirectUri,
      String tenant, String nonce) {
    return "ml-code-" + UUID.randomUUID();
  }
}
