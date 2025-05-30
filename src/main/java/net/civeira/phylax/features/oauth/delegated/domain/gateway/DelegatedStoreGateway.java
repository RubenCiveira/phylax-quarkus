/* @autogenerated */
package net.civeira.phylax.features.oauth.delegated.domain.gateway;

import java.util.Optional;

import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;
import net.civeira.phylax.features.oauth.delegated.domain.model.DelegatedAccessExternalProvider.TokenInfo;

public interface DelegatedStoreGateway {

  Optional<TokenInfo> load(AuthRequest request, String code);

  void save(AuthRequest request, String code, TokenInfo token);
}
