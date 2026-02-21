package net.civeira.phylax.testing.oauth.scenario;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.user.domain.gateway.ConsentGateway;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioConsentGateway implements ConsentGateway {
  private Supplier<Optional<String>> pendingText = () -> Optional.of("Terms and conditions");
  private final AtomicInteger acceptedCount = new AtomicInteger();
  private String lastTenant;
  private String lastUsername;

  public void whenPending(Supplier<Optional<String>> behavior) {
    this.pendingText = behavior;
  }

  public void reset() {
    pendingText = () -> Optional.of("Terms and conditions");
    acceptedCount.set(0);
    lastTenant = null;
    lastUsername = null;
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

  @Override
  public Optional<String> getPendingConsent(String tenant, String username, Locale locale) {
    return pendingText.get();
  }

  @Override
  public void storeAcceptedConsent(String tenant, String username) {
    acceptedCount.incrementAndGet();
    lastTenant = tenant;
    lastUsername = username;
  }
}
