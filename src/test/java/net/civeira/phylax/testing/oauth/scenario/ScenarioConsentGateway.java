package net.civeira.phylax.testing.oauth.scenario;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.user.domain.PendingConsent;
import net.civeira.phylax.features.oauth.user.domain.gateway.ConsentGateway;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioConsentGateway implements ConsentGateway {
  private Supplier<Optional<PendingConsent>> pendingSupplier =
      () -> Optional.of(PendingConsent.of(OidcTestFixtures.CLIENT_ID, "Terms and conditions"));
  private final AtomicInteger acceptedCount = new AtomicInteger();
  private String lastTenant;
  private String lastUsername;
  private String lastRelyingParty;

  public void whenPending(Supplier<Optional<PendingConsent>> behavior) {
    this.pendingSupplier = behavior;
  }

  public void reset() {
    pendingSupplier =
        () -> Optional.of(PendingConsent.of(OidcTestFixtures.CLIENT_ID, "Terms and conditions"));
    acceptedCount.set(0);
    lastTenant = null;
    lastUsername = null;
    lastRelyingParty = null;
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

  public String getLastRelyingParty() {
    return lastRelyingParty;
  }

  @Override
  public Optional<PendingConsent> getPendingConsent(String tenant, String username,
      List<String> audiences, Locale locale) {
    return pendingSupplier.get();
  }

  @Override
  public void storeAcceptedConsent(String tenant, String username, String relyingParty) {
    acceptedCount.incrementAndGet();
    lastTenant = tenant;
    lastUsername = username;
    lastRelyingParty = relyingParty;
  }
}
