package net.civeira.phylax.features.access.scopeassignation.infrastructure.driven;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.scopeassignation.domain.ScopeAssignation;
import net.civeira.phylax.features.access.scopeassignation.domain.ScopeAssignationRef;
import net.civeira.phylax.features.access.scopeassignation.domain.gateway.ScopeAssignationCursor;
import net.civeira.phylax.features.access.scopeassignation.domain.gateway.ScopeAssignationFilter;
import net.civeira.phylax.features.access.scopeassignation.domain.gateway.ScopeAssignationReadRepositoryGateway;
import net.civeira.phylax.features.access.scopeassignation.infrastructure.repository.ScopeAssignationRepository;

@ApplicationScoped
@RequiredArgsConstructor
public class ScopeAssignationReadGatewayAdapter implements ScopeAssignationReadRepositoryGateway {

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   */
  private final ScopeAssignationRepository repository;

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @return
   */
  @Override
  public long count(ScopeAssignationFilter filter) {
    return repository.count(filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param uid
   * @param filter
   * @return
   */
  @Override
  public boolean exists(String uid, Optional<ScopeAssignationFilter> filter) {
    return repository.exists(uid, filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @return
   */
  @Override
  public Optional<ScopeAssignation> find(ScopeAssignationFilter filter) {
    return repository.find(filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @return
   */
  @Override
  public List<ScopeAssignation> list(ScopeAssignationFilter filter) {
    return repository.list(filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param reference
   * @return
   */
  @Override
  public ScopeAssignation resolve(ScopeAssignationRef reference) {
    return repository.resolve(reference);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param uid
   * @param filter
   * @return
   */
  @Override
  public Optional<ScopeAssignation> retrieve(String uid, Optional<ScopeAssignationFilter> filter) {
    return repository.retrieve(uid, filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @param cursor
   * @return
   */
  @Override
  public Slider<ScopeAssignation> slide(ScopeAssignationFilter filter,
      ScopeAssignationCursor cursor) {
    return repository.slide(filter, cursor);
  }
}
