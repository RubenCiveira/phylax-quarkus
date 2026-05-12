package net.civeira.phylax.features.oauth.par.domain.gateway;

import java.util.Optional;

import net.civeira.phylax.features.oauth.par.domain.ParRequest;

/**
 * Domain port for Pushed Authorization Request storage (RFC 9126).
 */
public interface ParRequestGateway {

  /** Persists a newly created PAR. */
  void store(ParRequest request);

  /**
   * Finds a PAR by its {@code request_uri} and tenant.
   *
   * @return the PAR, or empty when not found
   */
  Optional<ParRequest> findByUri(String requestUri, String tenant);

  /** Marks the PAR as consumed so it cannot be reused. */
  void markUsed(String requestUri, String tenant);
}
