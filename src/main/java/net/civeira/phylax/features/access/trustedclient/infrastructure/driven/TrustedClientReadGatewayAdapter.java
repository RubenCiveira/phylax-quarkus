package net.civeira.phylax.features.access.trustedclient.infrastructure.driven;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClient;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientRef;
import net.civeira.phylax.features.access.trustedclient.domain.gateway.TrustedClientCursor;
import net.civeira.phylax.features.access.trustedclient.domain.gateway.TrustedClientFilter;
import net.civeira.phylax.features.access.trustedclient.domain.gateway.TrustedClientReadRepositoryGateway;
import net.civeira.phylax.features.access.trustedclient.infrastructure.repository.TrustedClientRepository;

@ApplicationScoped
@RequiredArgsConstructor
public class TrustedClientReadGatewayAdapter implements TrustedClientReadRepositoryGateway {

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   */
  private final TrustedClientRepository repository;

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @return
   */
  @Override
  public long count(TrustedClientFilter filter) {
    return repository.count(filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param uid
   * @param filter
   * @return
   */
  @Override
  public boolean exists(String uid, Optional<TrustedClientFilter> filter) {
    return repository.exists(uid, filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @return
   */
  @Override
  public Optional<TrustedClient> find(TrustedClientFilter filter) {
    return repository.find(filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @return
   */
  @Override
  public List<TrustedClient> list(TrustedClientFilter filter) {
    return repository.list(filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param reference
   * @return
   */
  @Override
  public TrustedClient resolve(TrustedClientRef reference) {
    return repository.resolve(reference);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param uid
   * @param filter
   * @return
   */
  @Override
  public Optional<TrustedClient> retrieve(String uid, Optional<TrustedClientFilter> filter) {
    return repository.retrieve(uid, filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @param cursor
   * @return
   */
  @Override
  public Slider<TrustedClient> slide(TrustedClientFilter filter, TrustedClientCursor cursor) {
    return repository.slide(filter, cursor);
  }
}
