package net.civeira.phylax.features.access.securityscope.domain.gateway;

import java.util.List;
import java.util.Optional;

import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScope;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScopeRef;

public interface SecurityScopeReadRepositoryGateway {

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The items that would be returned by the query
   */
  long count(SecurityScopeFilter filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  boolean exists(String uid, Optional<SecurityScopeFilter> filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  Optional<SecurityScope> find(SecurityScopeFilter filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  List<SecurityScope> list(SecurityScopeFilter filter);

  /**
   * Retrieve full data for a reference.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  SecurityScope resolve(SecurityScopeRef reference);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Optional<SecurityScope> retrieve(String uid, Optional<SecurityScopeFilter> filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @param cursor a cursor to order and skip
   * @return The slide with some values
   */
  Slider<SecurityScope> slide(SecurityScopeFilter filter, SecurityScopeCursor cursor);
}
