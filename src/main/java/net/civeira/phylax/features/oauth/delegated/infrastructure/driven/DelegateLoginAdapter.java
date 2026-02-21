package net.civeira.phylax.features.oauth.delegated.infrastructure.driven;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;
import net.civeira.phylax.features.oauth.delegated.domain.gateway.DelegateLoginGateway;
import net.civeira.phylax.features.oauth.delegated.domain.gateway.DelegatedStoreGateway;
import net.civeira.phylax.features.oauth.delegated.domain.model.DelegatedAccessExternalProvider;
import net.civeira.phylax.features.oauth.delegated.domain.model.DelegatedAccessExternalProvider.TokenInfo;
import net.civeira.phylax.features.oauth.delegated.domain.model.DelegatedAccessExternalProvider.UserData;
import net.civeira.phylax.features.oauth.delegated.domain.spi.DelegatedAccessAuthValidatorSpi;

/**
 * Adapter that bridges the unified {@link DelegateLoginGateway} to the existing legacy
 * {@link DelegatedStoreGateway} (token persistence) and {@link DelegatedAccessAuthValidatorSpi}
 * (provider registry + username resolution).
 */
@ApplicationScoped
@RequiredArgsConstructor
public class DelegateLoginAdapter implements DelegateLoginGateway {

  private final DelegatedStoreGateway store;
  private final DelegatedAccessAuthValidatorSpi validator;

  @Override
  public Optional<TokenInfo> loadToken(AuthRequest request, String code) {
    return store.load(request, code);
  }

  @Override
  public void saveToken(AuthRequest request, String code, TokenInfo token) {
    store.save(request, code, token);
  }

  @Override
  public List<DelegatedAccessExternalProvider> providers(AuthRequest request) {
    return validator.providers(request);
  }

  @Override
  public Optional<String> retrieveUsername(AuthRequest request, String provider,
      UserData userInfo) {
    return validator.retrieveUsername(request, provider, userInfo);
  }
}
