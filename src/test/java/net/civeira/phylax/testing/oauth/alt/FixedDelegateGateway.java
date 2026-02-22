package net.civeira.phylax.testing.oauth.alt;

import java.util.List;
import java.util.Optional;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.authentication.domain.AuthRequest;
import net.civeira.phylax.features.oauth.delegated.domain.DelegatedAccessExternalProvider;
import net.civeira.phylax.features.oauth.delegated.domain.DelegatedAccessExternalProvider.TokenInfo;
import net.civeira.phylax.features.oauth.delegated.domain.DelegatedAccessExternalProvider.UserData;
import net.civeira.phylax.features.oauth.delegated.domain.gateway.DelegateLoginGateway;

@Alternative
@Priority(1)
@ApplicationScoped
public class FixedDelegateGateway implements DelegateLoginGateway {
  @Override
  public Optional<TokenInfo> loadToken(AuthRequest request, String code) {
    return Optional.empty();
  }

  @Override
  public void saveToken(AuthRequest request, String code, TokenInfo token) {}

  @Override
  public List<DelegatedAccessExternalProvider> providers(AuthRequest request) {
    return List.of();
  }

  @Override
  public Optional<String> retrieveUsername(AuthRequest request, String provider,
      UserData userInfo) {
    return Optional.empty();
  }
}
