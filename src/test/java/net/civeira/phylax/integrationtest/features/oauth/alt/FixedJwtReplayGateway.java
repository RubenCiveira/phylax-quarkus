package net.civeira.phylax.integrationtest.features.oauth.alt;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.client.domain.gateway.JwtReplayGateway;

/**
 * One-time JWT identifiers in memory: the first use wins, as with the primary key of
 * {@code _oauth_replay_jti}.
 */
@IfBuildProfile("test")
@Alternative
@Priority(1)
@ApplicationScoped
public class FixedJwtReplayGateway implements JwtReplayGateway {

  private final Set<String> used = ConcurrentHashMap.newKeySet();

  public void clear() {
    used.clear();
  }

  @Override
  public boolean firstUse(String tenant, String clientId, String purpose, String jti,
      Instant expiresAt) {
    return used.add(String.join("|", tenant, clientId, purpose, jti));
  }
}
