package net.civeira.phylax.features.access.tenant.gateway;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.tenant.Tenant;
import net.civeira.phylax.features.access.tenant.TenantRef;
import net.civeira.phylax.features.access.tenant.query.TenantCursor;
import net.civeira.phylax.features.access.tenant.query.TenantFilter;

public interface TenantWriteRepositoryGateway {

  /**
   * The items that would be returned by the query
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The items that would be returned by the query
   */
  long countForUpdate(TenantFilter filter);

  /**
   * Retrieve one single value
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param entity a filter to retrieve only matching values
   * @param verifier a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Tenant create(Tenant entity, Predicate<Tenant> verifier);

  /**
   * Retrieve one single value
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param entity a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Tenant create(Tenant entity);

  /**
   * Delete an existing record.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param entity a filter to retrieve only matching values
   */
  void delete(Tenant entity);

  /**
   * Retrieve one single value
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Tenant enrichForUpdate(TenantRef reference);

  /**
   * Retrieve one single value
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  boolean existsForUpdate(String uid, Optional<TenantFilter> filter);

  /**
   * The slide with some values
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  Optional<Tenant> findForUpdate(TenantFilter filter);

  /**
   * The slide with some values
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  List<Tenant> listForUpdate(TenantFilter filter);

  /**
   * Retrieve one single value
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Optional<Tenant> retrieveForUpdate(String uid, Optional<TenantFilter> filter);

  /**
   * The slide with some values
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @param cursor a cursor to order and skip
   * @return The slide with some values
   */
  Slider<Tenant> slideForUpdate(TenantFilter filter, TenantCursor cursor);

  /**
   * Retrieve one single value
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @param entity a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Tenant update(TenantRef reference, Tenant entity);
}
