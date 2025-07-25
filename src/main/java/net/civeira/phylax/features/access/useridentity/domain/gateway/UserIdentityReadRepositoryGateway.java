package net.civeira.phylax.features.access.useridentity.domain.gateway;

import java.util.List;
import java.util.Optional;

import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.role.domain.Role;
import net.civeira.phylax.features.access.useridentity.domain.Roles;
import net.civeira.phylax.features.access.useridentity.domain.UserIdentity;
import net.civeira.phylax.features.access.useridentity.domain.UserIdentityRef;

public interface UserIdentityReadRepositoryGateway {

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The items that would be returned by the query
   */
  long count(UserIdentityFilter filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  boolean exists(String uid, Optional<UserIdentityFilter> filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  Optional<UserIdentity> find(UserIdentityFilter filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  List<UserIdentity> list(UserIdentityFilter filter);

  /**
   * Retrieve full data for a reference.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  UserIdentity resolve(UserIdentityRef reference);

  /**
   * Retrieve full data for a reference.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param childs
   * @return Retrieve one single value
   */
  List<Role> resolveRoles(List<Roles> childs);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Optional<UserIdentity> retrieve(String uid, Optional<UserIdentityFilter> filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @param cursor a cursor to order and skip
   * @return The slide with some values
   */
  Slider<UserIdentity> slide(UserIdentityFilter filter, UserIdentityCursor cursor);
}
