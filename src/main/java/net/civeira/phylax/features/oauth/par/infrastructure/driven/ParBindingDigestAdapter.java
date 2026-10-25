package net.civeira.phylax.features.oauth.par.infrastructure.driven;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.crypto.SecretDigester;
import net.civeira.phylax.features.oauth.par.domain.gateway.ParBindingGateway;

/**
 * Browser binding of a pushed request as the {@link SecretDigester} digest of its URI: without the
 * pepper nobody can compute it for a stolen {@code request_uri}.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class ParBindingDigestAdapter implements ParBindingGateway {

  private static final String PURPOSE = "par-binding:";

  private final SecretDigester digester;

  @Override
  public String bind(String requestUri) {
    return digester.digest(PURPOSE + requestUri);
  }

  @Override
  public boolean verify(String requestUri, String binding) {
    return binding != null && !binding.isBlank() && digester.verify(PURPOSE + requestUri, binding);
  }
}
