package net.civeira.phylax.integrationtest.features.oauth.scenario;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.magiclink.domain.MagicLinkSession;
import net.civeira.phylax.features.oauth.magiclink.domain.gateway.MagicLinkSessionGateway;

@IfBuildProfile("test")
@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioMagicLinkSessionGateway implements MagicLinkSessionGateway {
  @Override
  public Optional<MagicLinkSession> createSession(String userUid, String email, String tenantUid,
      String tenantName, String clientId) {
    return Optional
        .of(new MagicLinkSession("ml-session-" + UUID.randomUUID(), Duration.ofMinutes(5)));
  }
}
