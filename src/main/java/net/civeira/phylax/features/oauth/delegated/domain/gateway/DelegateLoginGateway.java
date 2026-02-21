package net.civeira.phylax.features.oauth.delegated.domain.gateway;

import java.util.List;
import java.util.Optional;

import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;
import net.civeira.phylax.features.oauth.delegated.domain.model.DelegatedAccessExternalProvider;
import net.civeira.phylax.features.oauth.delegated.domain.model.DelegatedAccessExternalProvider.TokenInfo;
import net.civeira.phylax.features.oauth.delegated.domain.model.DelegatedAccessExternalProvider.UserData;

/**
 * Unified port for delegated (SSO) login. Combines token persistence and provider registry.
 * Supersedes {@link DelegatedStoreGateway} +
 * {@link net.civeira.phylax.features.oauth.delegated.domain.spi.DelegatedAccessAuthValidatorSpi}.
 */
public interface DelegateLoginGateway {

  Optional<TokenInfo> loadToken(AuthRequest request, String code);

  void saveToken(AuthRequest request, String code, TokenInfo token);

  List<DelegatedAccessExternalProvider> providers(AuthRequest request);

  Optional<String> retrieveUsername(AuthRequest request, String provider, UserData userInfo);
}
