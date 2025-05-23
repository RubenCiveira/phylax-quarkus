package net.civeira.phylax.features.access.useridentity.infrastructure.driven;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.role.Role;
import net.civeira.phylax.features.access.useridentity.Roles;
import net.civeira.phylax.features.access.useridentity.UserIdentity;
import net.civeira.phylax.features.access.useridentity.UserIdentityRef;
import net.civeira.phylax.features.access.useridentity.gateway.UserIdentityReadRepositoryGateway;
import net.civeira.phylax.features.access.useridentity.infrastructure.repository.UserIdentityRepository;
import net.civeira.phylax.features.access.useridentity.query.UserIdentityCursor;
import net.civeira.phylax.features.access.useridentity.query.UserIdentityFilter;

@ApplicationScoped
@RequiredArgsConstructor
public class UserIdentityReadGatewayAdapter implements UserIdentityReadRepositoryGateway {

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   */
  private final UserIdentityRepository repository;

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @return
   */
  @Override
  public long count(UserIdentityFilter filter) {
    return repository.count(filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param reference
   * @return
   */
  @Override
  public UserIdentity enrich(UserIdentityRef reference) {
    return repository.enrich(reference);
  }

  /**
   * Retrieve one single value
   *
   * @autogenerated ReadAdaterGatewayGenerator
   * @param childs
   * @return Retrieve one single value
   */
  @Override
  public List<Role> enrichRoles(final List<Roles> childs) {
    return repository.enrichRoles(childs);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param uid
   * @param filter
   * @return
   */
  @Override
  public boolean exists(String uid, Optional<UserIdentityFilter> filter) {
    return repository.exists(uid, filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @return
   */
  @Override
  public Optional<UserIdentity> find(UserIdentityFilter filter) {
    return repository.find(filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @return
   */
  @Override
  public List<UserIdentity> list(UserIdentityFilter filter) {
    return repository.list(filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param uid
   * @param filter
   * @return
   */
  @Override
  public Optional<UserIdentity> retrieve(String uid, Optional<UserIdentityFilter> filter) {
    return repository.retrieve(uid, filter);
  }

  /**
   * @autogenerated ReadAdaterGatewayGenerator
   * @param filter
   * @param cursor
   * @return
   */
  @Override
  public Slider<UserIdentity> slide(UserIdentityFilter filter, UserIdentityCursor cursor) {
    return repository.slide(filter, cursor);
  }
}
