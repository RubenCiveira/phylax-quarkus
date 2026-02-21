package net.civeira.phylax.testing.oauth.alt;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientStoreGateway;
import net.civeira.phylax.features.oauth.client.domain.model.ClientDetails;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Alternative
@Priority(1)
@ApplicationScoped
public class FixedClientStoreGateway implements ClientStoreGateway {
  private final AtomicReference<List<String>> allowedScopes = new AtomicReference<>();
  private List<String> allowedGrants;

  @PostConstruct
  void init() {
    reset();
    allowedGrants = List.of("authorization_code", "password", "refresh_token", "form");
  }

  public void setAllowedScopes(List<String> scopes) {
    allowedScopes.set(List.copyOf(scopes));
  }

  public void reset() {
    allowedScopes.set(List.of("*"));
  }

  @Override
  public Optional<ClientDetails> loadPreautorized(String tenant, String clientId) {
    return matches(tenant, clientId) ? Optional.of(buildClient()) : Optional.empty();
  }

  @Override
  public Optional<ClientDetails> loadPublic(String tenant, String clientId, String redirect) {
    boolean redirectAllowed = redirect != null && redirect.contains("localhost/callback");
    return matches(tenant, clientId) && redirectAllowed ? Optional.of(buildClient())
        : Optional.empty();
  }

  @Override
  public Optional<ClientDetails> loadPrivate(String tenant, String clientId, String clientSecret) {
    boolean secretMatches = OidcTestFixtures.CLIENT_SECRET.equals(clientSecret);
    return matches(tenant, clientId) && secretMatches ? Optional.of(buildClient())
        : Optional.empty();
  }

  private boolean matches(String tenant, String clientId) {
    return OidcTestFixtures.TENANT.equals(tenant) && OidcTestFixtures.CLIENT_ID.equals(clientId);
  }

  private ClientDetails buildClient() {
    return ClientDetails.builder().clientId(OidcTestFixtures.CLIENT_ID)
        .allowedScopes(allowedScopes.get()).allowedGrants(allowedGrants).protectedWithSecret(true)
        .build();
  }
}
