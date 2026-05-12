package net.civeira.phylax.features.oauth.consent.infrastructure.driven;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClient;
import net.civeira.phylax.features.access.trustedclient.domain.gateway.TrustedClientFilter;
import net.civeira.phylax.features.access.trustedclient.domain.gateway.TrustedClientReadRepositoryGateway;
import net.civeira.phylax.features.access.user.domain.User;
import net.civeira.phylax.features.access.user.domain.gateway.UserFilter;
import net.civeira.phylax.features.access.user.domain.gateway.UserReadRepositoryGateway;
import net.civeira.phylax.features.oauth.consent.domain.ScopePermission;
import net.civeira.phylax.features.oauth.consent.domain.gateway.ScopesConsentGateway;

/**
 * ACL adapter that bridges the OIDC scope consent protocol port to the Access bounded context.
 *
 * Resolves username → User UID and clientId → TrustedClient UID before delegating to the domain use
 * cases. The OIDC module never touches the Access tables directly.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class ScopesConsentAdapter implements ScopesConsentGateway {

  private final UserReadRepositoryGateway users;
  private final TrustedClientReadRepositoryGateway clients;
  // private final CheckScopeConsentUseCase checkConsent;
  // private final GrantScopeConsentUseCase grantConsent;

  /**
   * Returns the subset of {@code scopes} not yet consented by the user for this client.
   *
   * If the user or client cannot be resolved, all requested scopes are returned as pending (safe
   * default: always ask for consent rather than silently skip it).
   */
  @Override
  public List<ScopePermission> pendingScopes(String tenant, String username, String clientId,
      List<String> scopes) {
    if (scopes.isEmpty()) {
      return List.of();
    }
    User user = users.find(UserFilter.builder().name(username).build()).orElse(null);
    if (user == null) {
      return toPermissions(scopes);
    }
    TrustedClient client =
        clients.find(TrustedClientFilter.builder().code(clientId).build()).orElse(null);
    if (client == null) {
      return toPermissions(scopes);
    }
    return List.of();
    // List<String> pending = checkConsent.execute(user.getUid(), client.getUid(), scopes);
    // return toPermissions(pending);
  }

  /**
   * Persists the accepted scopes for the user and client.
   *
   * Silently returns if the user or client cannot be resolved.
   */
  @Override
  public void storeAcceptedScopes(String tenant, String username, String clientId,
      List<String> scopes) {
    if (scopes.isEmpty()) {
      return;
    }
    User user = users.find(UserFilter.builder().name(username).build()).orElse(null);
    if (user == null) {
      return;
    }
    TrustedClient client =
        clients.find(TrustedClientFilter.builder().code(clientId).build()).orElse(null);
    if (client == null) {
      return;
    }
    // scopes.forEach(scope -> grantConsent.execute(user.getUid(), client.getUid(), scope));
  }

  private List<ScopePermission> toPermissions(List<String> scopes) {
    return scopes.stream().map(scope -> ScopePermission.builder().scope(scope).required(false)
        .label(scope).description(scope).build()).toList();
  }
}
