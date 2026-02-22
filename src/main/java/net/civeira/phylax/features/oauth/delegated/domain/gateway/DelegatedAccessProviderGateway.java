package net.civeira.phylax.features.oauth.delegated.domain.gateway;

import java.util.List;
import java.util.Optional;

import net.civeira.phylax.features.oauth.authentication.domain.AuthRequest;
import net.civeira.phylax.features.oauth.delegated.domain.DelegatedAccessExternalProvider;
import net.civeira.phylax.features.oauth.delegated.domain.DelegatedAccessExternalProvider.UserData;

public interface DelegatedAccessProviderGateway {

  List<DelegatedAccessExternalProvider> providers(AuthRequest request);

  Optional<String> retrieveUsername(AuthRequest request, String provider, UserData userInfo);
}
