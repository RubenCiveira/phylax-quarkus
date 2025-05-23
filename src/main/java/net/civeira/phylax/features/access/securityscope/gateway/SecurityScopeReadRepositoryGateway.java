package net.civeira.phylax.features.access.securityscope.gateway;

import java.util.List;
import java.util.Optional;

import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.securityscope.SecurityScope;
import net.civeira.phylax.features.access.securityscope.SecurityScopeRef;
import net.civeira.phylax.features.access.securityscope.query.SecurityScopeCursor;
import net.civeira.phylax.features.access.securityscope.query.SecurityScopeFilter;

public interface SecurityScopeReadRepositoryGateway {

  /**
   * The items that would be returned by the query
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The items that would be returned by the query
   */
  long count(SecurityScopeFilter filter);

  /**
   * Retrieve one single value
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  SecurityScope enrich(SecurityScopeRef reference);

  /**
   * Retrieve one single value
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  boolean exists(String uid, Optional<SecurityScopeFilter> filter);

  /**
   * The slide with some values
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  Optional<SecurityScope> find(SecurityScopeFilter filter);

  /**
   * The slide with some values
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  List<SecurityScope> list(SecurityScopeFilter filter);

  /**
   * Retrieve one single value
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Optional<SecurityScope> retrieve(String uid, Optional<SecurityScopeFilter> filter);

  /**
   * The slide with some values
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @param cursor a cursor to order and skip
   * @return The slide with some values
   */
  Slider<SecurityScope> slide(SecurityScopeFilter filter, SecurityScopeCursor cursor);
}
