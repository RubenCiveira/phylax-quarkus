package net.civeira.phylax.features.oauth.par.application.usecase.resolveparrequest;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.par.domain.ParRequest;
import net.civeira.phylax.features.oauth.par.domain.exception.ParException;
import net.civeira.phylax.features.oauth.par.domain.gateway.ParRequestGateway;

/**
 * Resolves a {@code request_uri} into its stored authorization parameters — RFC 9126.
 *
 * <p>
 * Used by the authorization endpoint when the request carries a PAR {@code request_uri}.
 * </p>
 */
@ApplicationScoped
@RequiredArgsConstructor
public class ResolveParRequestUsecase {

  private final ParRequestGateway gateway;

  /**
   * Retrieves and marks as used the PAR identified by {@code requestUri}.
   *
   * @throws ParException when the URI is not found, already used, or expired
   */
  public ParRequest resolve(String requestUri, String tenant) {
    ParRequest parRequest =
        gateway.findByUri(requestUri, tenant).orElseThrow(ParException::invalidRequestUri);

    if (parRequest.isUsed() || parRequest.isExpired()) {
      throw ParException.invalidRequestUri();
    }

    gateway.markUsed(requestUri, tenant);
    return parRequest;
  }
}
