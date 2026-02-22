package net.civeira.phylax.testing.oauth.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientScopeConsentGateway;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioScopeConsentGateway implements ClientScopeConsentGateway {

  /**
   * Default behavior: returns all requested scopes as pending (none pre-authorized).
   */
  private BiFunction<String, List<String>, List<String>> pendingProvider =
      (clientId, requested) -> new ArrayList<>(requested);

  private final AtomicInteger acceptedCount = new AtomicInteger();
  private String lastTenant;
  private String lastUsername;
  private String lastClientId;
  private List<String> lastAcceptedScopes;

  public void whenPending(BiFunction<String, List<String>, List<String>> behavior) {
    this.pendingProvider = behavior;
  }

  /** Simulates all scopes already authorized (nothing pending). */
  public void whenNoPending() {
    this.pendingProvider = (clientId, requested) -> List.of();
  }

  public void reset() {
    pendingProvider = (clientId, requested) -> new ArrayList<>(requested);
    acceptedCount.set(0);
    lastTenant = null;
    lastUsername = null;
    lastClientId = null;
    lastAcceptedScopes = null;
  }

  public int getAcceptedCount() {
    return acceptedCount.get();
  }

  public String getLastTenant() {
    return lastTenant;
  }

  public String getLastUsername() {
    return lastUsername;
  }

  public String getLastClientId() {
    return lastClientId;
  }

  public List<String> getLastAcceptedScopes() {
    return lastAcceptedScopes;
  }

  @Override
  public List<String> getPendingScopes(String tenant, String username, String clientId,
      List<String> requestedScopes) {
    return pendingProvider.apply(clientId, requestedScopes);
  }

  @Override
  public void storeAcceptedScopes(String tenant, String username, String clientId,
      List<String> scopes) {
    acceptedCount.incrementAndGet();
    lastTenant = tenant;
    lastUsername = username;
    lastClientId = clientId;
    lastAcceptedScopes = scopes;
  }
}
